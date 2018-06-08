package AE_General;

import java.awt.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import AE_Communication.AE_TCP_Connection;
import AE_Communication.AE_TCP_Constantes;
import AE_Communication.AE_TCP_Modbus;


public class AE_Fonctions {
	
	public static String saisieTexte(String titreQuestion, String titreFenetre) {
		String reponse = new String("");
		
		JFrame frame = new JFrame("Saisie Texte");
	    reponse = JOptionPane.showInputDialog(frame, titreQuestion, titreFenetre, JOptionPane.QUESTION_MESSAGE + JOptionPane.OK_OPTION);		
		
		return reponse;
	}
	
	public static void testVersion(Component appel) {
		String strSql = "";
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		
		ctn.open();
		strSql = "SELECT * FROM Version"; 
		ResultSet result = ctn.lectureData(strSql);
		try {
			int versionBase = -1;
			result.last();
			versionBase = result.getInt("VersionClient");
			if (versionBase != AE_Constantes.VERSION) afficheMessage(appel, "La version du programme n'est pas à jour ! Veuillez contacter le service technique ...");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		ctn.close();		
		
	}
	
	public static String calculVersion() {
		String versionCalculee = new String("");
		int nbVersion = 0;
		int nbMiseAJour = 0;
		int nbIteration = 0;
		
		
		nbVersion = AE_Constantes.VERSION / 10000;
		nbMiseAJour = (AE_Constantes.VERSION - (nbVersion * 10000)) / 100;
		nbIteration = (AE_Constantes.VERSION - (nbVersion * 10000) - (nbMiseAJour * 100));

		versionCalculee = "Version : " + nbVersion + "." + nbMiseAJour + "." + nbIteration;
		return versionCalculee;
	}
	
	public static void ecrireMessageLog(String messageLog) {
		PrintWriter sortie;
		try {
		    Calendar cal = Calendar.getInstance();
			Date dateErreur = new Date();
		    cal.setTime(dateErreur);
		    sortie = new PrintWriter(new FileWriter("Erreur_Client.log", true));
			sortie.println(dateErreur + " : " + messageLog);
			sortie.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double arrondi(double A, int B) {
		return (double) ( (int) (A * Math.pow(10, B) + .5)) / Math.pow(10, B);
	}
	
	public static boolean messageConfirmation(Component appel, String msgConfirmation) {
    	int retour = JOptionPane.showConfirmDialog(appel, msgConfirmation, "GTC Visualize - Programme Client", JOptionPane.OK_CANCEL_OPTION);			        	
    	
    	if( retour == JOptionPane.OK_OPTION) {		
    		return true;
    	} else {
    		return false;
    	}
	} // fin messageConfirmation
	
	public static void ecrireJournal(String msgJournal, long idCapteur, int typeJournal) {
		String strSql = "";
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		
		ctn.open();
		strSql = "INSERT INTO Journal (DateJournal, Description, TypeJournal, idCapteur, idUtilisateur)" 
				+ " VALUES(sysdate, '" + msgJournal + "', " + typeJournal 
				+ ", " + idCapteur + ", " + EFS_Client_Variable.idUtilisateur
				+ ")";
		ctn.fonctionSql(strSql);
		ctn.close();
	}
	
	public static void afficheMessage(Component appel, String msgMessage) {
       JOptionPane.showMessageDialog(appel, msgMessage, "GTC Visualize - Programme Client", JOptionPane.WARNING_MESSAGE);
		
	}
	
	public static boolean isNumeric(String str)  {  
		try  {  
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  {  
			return false;  
		}  
		return true;  
	} // fin isNumeric()	
	
	public static void prevenirProgrammeMaitre(int typeModification) {
		try{
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			@SuppressWarnings("unused")
			double [] reqReponse;		
			addr = InetAddress.getByName(EFS_Client_Variable.ADR_IP_API);
			
			con = new AE_TCP_Connection(addr, AE_TCP_Constantes.MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				if (AE_TCP_Modbus.MODBUS_DEBUG) System.out.println("MotEchange : API Connecté ... ");
			}
			else {
				if (AE_TCP_Modbus.MODBUS_DEBUG) System.out.println("MotEchange : API Déconnecté ... ");
			}

			// ===== Ecriture des mots =====
			for(int i = 0; i < 5; i++) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, AE_TCP_Constantes.ADR_API_ECHANGE_MAITRE_CLIENT + i, typeModification));
			}
            con.close();
            if (AE_TCP_Modbus.MODBUS_DEBUG) System.out.println("MotEchange : API Close ... ");
		} // Fin Try
		catch (Exception e){
			System.out.println("MotEchange : Erreur écriture API ... ");
			System.err.println("MotEchange - Erreur message : " + e.getMessage());
		} // Fin catch			
	}

	/**
	 * @author Eric Mariani
	 * @since 27/03/2016
	 * @param idCapteur
	 */
	public static void modifierMaitreViaClient(int idCapteur, int typeModification) {
		try{
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			@SuppressWarnings("unused")
			double [] reqReponse;		
			addr = InetAddress.getByName(EFS_Client_Variable.ADR_IP_API);
			
			con = new AE_TCP_Connection(addr, AE_TCP_Constantes.MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				if (AE_TCP_Modbus.MODBUS_DEBUG) System.out.println("MotEchange : API Connecté ... ");
			}
			else {
				if (AE_TCP_Modbus.MODBUS_DEBUG) System.out.println("MotEchange : API Déconnecté ... ");
			}

			// ===== Ecriture des mots =====
			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, AE_Constantes.ADR_API_ECHANGE_MAITRE_CLIENT + 1, idCapteur));
			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, AE_Constantes.ADR_API_ECHANGE_MAITRE_CLIENT, typeModification));
            con.close();
            if (AE_TCP_Modbus.MODBUS_DEBUG) System.out.println("MotEchange : API Close ... ");
		} // Fin Try
		catch (Exception e){
			System.out.println("MotEchange : Erreur écriture API ... ");
			System.err.println("MotEchange - Erreur message : " + e.getMessage());
		} // Fin catch			
	}
	
	public static String[] renvoiListe(String tableSGBD, String nomChamp) {
		String tbListe[] = null;
		int cpt = 0;
		
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		ctn.open();
		// Lecture données
		ResultSet result = ctn.lectureData("SELECT * FROM " + tableSGBD);
		
		// Remplissage de la liste
		try {
			result.last();
			tbListe = new String[result.getRow()];
			result.beforeFirst();
			while(result.next()) {
				tbListe[cpt] = result.getString(nomChamp);
				cpt++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		ctn.close();
		return tbListe;
	} // fin renvoiListe()

	public static long trouveIndex(String tableSGBD, String nomChamp, String nomCherche, String nomIndex) {
		long indexTrouve = -1;
		String strSql = "";
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		ctn.open();

		// Lecture données
		strSql = "SELECT * FROM " + tableSGBD + " WHERE " + nomChamp + " ='" + nomCherche + "'";
		ResultSet result = ctn.lectureData(strSql);
		
		// Remplissage de la liste
		try {
			if(result.next()) {
				indexTrouve = result.getLong(nomIndex);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		ctn.close();

		return indexTrouve;
	} // fin trouveIndex()

	public static boolean enregistrementExiste(String tableSGBD, String nomChamp, String nomCherche) {
		String strSql = "";
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		ctn.open();

		// Lecture données
		strSql = "SELECT * FROM " + tableSGBD + " WHERE " + nomChamp + " ='" + nomCherche + "'";
		ResultSet result = ctn.lectureData(strSql);
		
		// Remplissage de la liste
		try {
			if(result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		ctn.close();

		return false;
	} // fin trouveIndex()
	
	
	
	public static boolean trouveIdDansTable(String tableSGBD, String nomChamp, long idCherche) {
		boolean trouve = false;
		String strSql = "";
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		ctn.open();

		// Lecture données
		strSql = "SELECT * FROM " + tableSGBD + " WHERE " + nomChamp + " =" + idCherche;
		ResultSet result = ctn.lectureData(strSql);
		
		// Remplissage de la liste
		try {
			if(result.next()) {
				trouve = true;
			} else {
				trouve = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		ctn.close();

		return trouve;
	} // fin trouveIndex()	

	public static Date stringToDate(String sDate, String sFormat) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        return sdf.parse(sDate);
	}
	
	/**
	 * 
	 * @param format
	 * 		Format voulu pour la date 
	 * @return
	 * 		Chaine contenant la date
	 */
	public static String formatDate(String format) {
		DateFormat dfm = new SimpleDateFormat(format);
		Calendar dateDepart = Calendar.getInstance();
		return (dfm.format(dateDepart.getTime()));
	}
	
	/**
	 * @param date
	 * 		Date sous format String 
	 * @param format
	 * 		Format voulu pour la date 
	 * @return
	 * 		Chaine contenant la date
	 */
	public static String formatDate(String date, String formatEntree, String formatSortie) {
		DateFormat dfmEntree = new SimpleDateFormat(formatEntree);
		DateFormat dfmSortie = new SimpleDateFormat(formatSortie);
		Date maDate;
		try {
			maDate = dfmEntree.parse(date);
			return dfmSortie.format(maDate);
		} catch (ParseException e) {
			return "--- pb ---";
		}
	}
	
	
	
} // fin class
