package em.general;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	public static boolean appelAlert = false;
	public static String siteGTC = "???";
	public static boolean modificationEnMaintenance = false;
	
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
				siteGTC = "BESANCON";
			} else if (ligne.equals("DIJON")) {
				AE_Variables.AE_SGBD_BASE = "GTCBFC";
				AE_Variables.AE_SGBD_USER = "GTCDijon";
				AE_Variables.AE_SGBD_MDP = "GTCBFC_dijon25";
				ADR_IP_API = "10.21.102.31";
				TEST_GTC_MINUTE = 30; // Modification 26/05/2016 et Modification 24/06/2016
				siteGTC = "DIJON";
			} else {
				AE_Variables.AE_SGBD_BASE = "xe";
				AE_Variables.AE_SGBD_USER = "gtcbfc";
				AE_Variables.AE_SGBD_MDP = "gtcbfc25";
				//siteEFS = "AXIOME";
				ADR_IP_API = "192.168.0.20";
				siteGTC = "AXIOME";
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
}
