package em.general;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import em.fonctions.AE_Fonctions;
import em.general.AE_Constantes;

/**
 * Gére les variables propre à l'EFS
 * @author Eric Mariani
 * @since 14/02/2017
 *
 */
public class EFS_Maitre_Variable {
	public static String ADR_IP_API;

	public static int TEST_GTC_MINUTE = 10;
	
	public static long nombreLectureAPI = 0;
	public static long nombreLectureSGBD = 0;
	public static long compteurErreurSGBD = 0;
	public static long compteurErreurAPI = 0;
	
	/**
	 * Initialise les variables pour l'accès à la base de données
	 */
	public static void initialisationVariableBase() {
		// lire fichier InitBase.ini
		String nomFichier;
		String ligne;
		
		try {
			nomFichier = "InitBase.ini";
			BufferedReader entree = new BufferedReader(new FileReader(nomFichier));
			// Nom Serveur
			ligne = entree.readLine();
			AE_Variables.AE_SGBD_SERVEUR = "@" + ligne;
			ligne = entree.readLine();
			AE_Variables.AE_SGBD_SERVEUR += ":" + ligne;
			ligne = entree.readLine();
			if (ligne.equals("BESANCON")) {
				AE_Variables.AE_SGBD_BASE = "GTCBFC";
				AE_Variables.AE_SGBD_USER = "gtcbfc";
				AE_Variables.AE_SGBD_MDP = "gtcbfc25";
				ADR_IP_API = "10.27.0.10";
				TEST_GTC_MINUTE = 10;
			} else if (ligne.equals("DIJON")) {
				AE_Variables.AE_SGBD_BASE = "GTCBFC";
				AE_Variables.AE_SGBD_USER = "GTCDijon";
				AE_Variables.AE_SGBD_MDP = "GTCBFC_dijon25";
				ADR_IP_API = "10.21.4.31";
				TEST_GTC_MINUTE = 30; // Modification 26/05/2016 et Modification 24/06/2016
			} else {
				AE_Variables.AE_SGBD_BASE = "xe";
				AE_Variables.AE_SGBD_USER = "gtcbfc";
				AE_Variables.AE_SGBD_MDP = "gtcbfc25";
				//siteEFS = "AXIOME";
				ADR_IP_API = "192.168.0.20";
			}
			ligne = entree.readLine();
			TEST_GTC_MINUTE = Integer.valueOf(ligne);
			entree.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("Problème avec le fichier .ini");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problème avec le fichier .ini");
			e.printStackTrace();
		}
	}	
	
	/**
	 * Test la version du programme par rapport à celle de la base de données
	 * @param appel
	 */
	public static void testVersion(Component appel) {
		String strSql = "";
		
		strSql = "SELECT * FROM Version"; 
		ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
		try {
			int versionBase = -1;
			result.last();
			versionBase = result.getInt("VersionMaitre");
			if (versionBase != AE_Constantes.VERSION) AE_Fonctions.afficheMessage(appel, "AVERTISSEMENT", "La version du programme n'est pas à jour ! Veuillez contacter le service technique ...");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		AE_Variables.ctnOracle.closeLectureData();
		
	}
	
	
}
