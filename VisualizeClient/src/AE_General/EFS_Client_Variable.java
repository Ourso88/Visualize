package AE_General;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import AE_Communication.AE_TCP_Connection;
import AE_Communication.AE_TCP_Constantes;
import AE_Communication.AE_TCP_Modbus;

public class EFS_Client_Variable implements AE_TCP_Constantes {
	public static int idUtilisateur;
	public static int niveauUtilisateur;
	public static String nomUtilisateur;
	public static String prenomUtilisateur;
	public static int[] tbService = new int[100];
	public static double tbValeurAI[] = new double[MAX_AI];
	public static int tbValeurDI[] = new int[MAX_DI];
	
	public static short tbTypeCapteur[] = new short[MAX_AI + MAX_DI];

	public static int tbIdAI[] = new int [MAX_AI];
	public static double tbAncAI[] = new double [MAX_AI];
	public static double tbSeuilBas[] = new double [MAX_AI];
	public static double tbSeuilHaut[] = new double [MAX_AI];
	public static int tbAlarmeAI[] = new int [MAX_AI];
	public static int tbTempoAI[] = new int [MAX_AI];
	public static int tbCalibrationAI[] = new int [MAX_AI];
	public static boolean tbEtatAlarmeAI[] = new boolean [MAX_AI];
	
	public static int tbIdDI[] = new int [MAX_DI];
	public static int tbAncDI[] = new int [MAX_DI];
	public static int tbAlarmeDI[] = new int [MAX_DI];
	public static int tbTempoDI[] = new int [MAX_DI];
	public static int tbNoNfDI[] = new int [MAX_DI];
	public static boolean tbEtatAlarmeDI[] = new boolean [MAX_DI];

	public static String EFS_SGBD_SERVEUR;
	public static String EFS_SGBD_BASE;
	public static String EFS_SGBD_USER;
	public static String EFS_SGBD_MDP;	
	public static String siteEFS;	
	public static String ADR_IP_API;
	
	public static void initialisationVariableBase() {
		// lire fichier InitBase.ini
		String nomFichier;
		String ligne;
		
		try {
			nomFichier = "InitBase.ini";
			BufferedReader entree = new BufferedReader(new FileReader(nomFichier));
			// Nom Serveur
			ligne = entree.readLine();
			EFS_SGBD_SERVEUR = "@" + ligne;
			ligne = entree.readLine();
			EFS_SGBD_SERVEUR += ":" + ligne;
			ligne = entree.readLine();
			if (ligne.equals("BESANCON")) {
				EFS_SGBD_BASE = "GTCBFC";
				EFS_SGBD_USER = "gtcbfc";
				EFS_SGBD_MDP = "gtcbfc25";
				siteEFS = "BESANCON";
				ADR_IP_API = "10.27.0.10";
			} else if (ligne.equals("DIJON"))  {
				EFS_SGBD_BASE = "GTCBFC";
				EFS_SGBD_USER = "GTCDijon";
				EFS_SGBD_MDP = "GTCBFC_dijon25";
				siteEFS = "DIJON";
				ADR_IP_API = "10.21.102.31";
			} else {
				EFS_SGBD_BASE = "xe";
				EFS_SGBD_USER = "gtcbfc";
				EFS_SGBD_MDP = "gtcbfc25";
				siteEFS = "AXIOME";
				ADR_IP_API = "192.168.0.20";
			}
			entree.close();
		} catch (FileNotFoundException e) {
			System.out.println("Problème avec le fichier .ini");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problème avec le fichier .ini");
			e.printStackTrace();
		}
	}
	
	public static void initialiseTableaux() {
		short idCapteur = -1;
		int idAI = -1;
		int idDI = -1;
		ResultSet result;
		
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		ctn.open();
		
		// Initialisation
		for (int i = 0; i < MAX_AI + MAX_DI; i++) {
			tbTypeCapteur[i] = -1;
		} // Fin for i
		for (int i = 0; i < MAX_AI; i++) {
			tbIdAI[i] = -1;
			tbAncAI[i] = -200;
			tbCalibrationAI[i] = 0;
			tbEtatAlarmeAI[i] = false;
		} // Fin for i
		for (int i = 0; i < MAX_DI; i++) {
			tbIdDI[i] = -1;
			tbAncDI[i] = -1;
			tbEtatAlarmeDI[i] = false;
		} // Fin for i		

		// Lecture des capteurs
		result = ctn.lectureData("SELECT * FROM Capteur");
		try {
			while(result.next()) {
				idCapteur = result.getShort("idCapteur");
				tbTypeCapteur[idCapteur] = idCapteur;
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture des voies analogiques");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Lecture des voies analogiques
		result = ctn.lectureData("SELECT Capteur.*, EntreeAnalogique.* FROM (EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur)");
		System.out.println("Lecture des voies analogiques");
		try {
			while(result.next()) {
				idAI = result.getInt("VoieApi") - 1;
//System.out.println("idAI : " + idAI);				
				tbIdAI[idAI] = result.getInt("idCapteur");
				tbSeuilHaut[idAI] = result.getInt("SeuilHaut");
				tbSeuilBas[idAI] = result.getInt("SeuilBas");
				tbAlarmeAI[idAI] = result.getInt("Alarme");
				tbTempoAI[idAI] = result.getInt("SeuilTempo");
				tbCalibrationAI[idAI] = result.getInt("Calibration");
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture des voies analogiques");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Lecture des voies digitales
		result = ctn.lectureData("SELECT Capteur.*, EntreeDigitale.* FROM (EntreeDigitale LEFT JOIN Capteur ON EntreeDigitale.idCapteur = Capteur.idCapteur)");
		System.out.println("Lecture des voies digitales");
		try {
			while(result.next()) {
				idDI = result.getInt("VoieApi") - 1;
				tbIdDI[idDI] = result.getInt("idCapteur");
				tbAlarmeDI[idDI] = result.getInt("Alarme");
				tbTempoDI[idDI] = result.getInt("Tempo");
				tbNoNfDI[idDI] = result.getInt("NoNf");
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture des voies digitales");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		ctn.close();
	
	}	// fin initialiseTableaux()

	public static void lectureVoie() {
		// ===== Envoi des requetes de lecture pour les AI =====
		int cptLecture = 0;
		int adresseLecture = 0;
		InetAddress addr = null; // Adresse IP du serveur	
		AE_TCP_Connection con = null; //the connection
		double [] reqReponse;
		int adresseDI = 0;
		int bitTest = 0;
		int valeurTest = 0;
		
		try {
			addr = InetAddress.getByName(ADR_IP_API);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		con = new AE_TCP_Connection(addr, AE_TCP_Constantes.MODBUS_PORT);
		try {
			con.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (con.isConnected()) {
			if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Connecté ... ");
		}
		else {
			if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Déconnecté ... ");
		}
		
		do {
			adresseLecture = AE_TCP_Constantes.ADR_API_AI_TPS_REEL + (cptLecture * AE_TCP_Constantes.NB_MOT_LECTURE_AI);
			if (adresseLecture < (AE_TCP_Constantes.ADR_API_AI_TPS_REEL + AE_TCP_Constantes.MAX_AI)) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, adresseLecture, AE_TCP_Constantes.NB_MOT_LECTURE_AI));
				for (int i = 0; i < AE_TCP_Constantes.NB_MOT_LECTURE_AI; i++) {
					EFS_Client_Variable.tbValeurAI[(adresseLecture - AE_TCP_Constantes.ADR_API_AI_TPS_REEL) + i] = reqReponse[i]; 
				} // Fin for i
			} // Fin if
			cptLecture++;
		} while(adresseLecture < (AE_TCP_Constantes.ADR_API_AI_TPS_REEL + AE_TCP_Constantes.MAX_AI)); // Fin while

		// ===== Envoi des requetes de lecture pour les DI =====
		reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, ADR_API_DI_TPS_REEL, NB_MOT_LECTURE_DI));
		for (int i = 0; i < NB_MOT_LECTURE_DI; i++) {
			for (int j = 0; j < 16; j++) {
				adresseDI = (i * 16) + j;
				bitTest = (int)(Math.pow(2, j));
				valeurTest = (int) reqReponse[i];
				if ((valeurTest & bitTest) == bitTest) {
					tbValeurDI[adresseDI] = 1;
				} // Fin if
				else {
					tbValeurDI[adresseDI] = 0;
				}
			} // Fin for j
		} // Fin for i
		con.close();
	}		
	
} // fin class
