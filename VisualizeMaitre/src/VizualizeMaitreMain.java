
import em.fonctions.GestionLogger;
import em.general.AE_Variables;
import em.general.EFS_Maitre_Variable;
import em.sgbd.FonctionsSGBD;
import gui.vues.FenPrincipale;
import kernel.GestionAPI;
import kernel.GestionSGBD;
import kernel.VoiesAPI;

/**
 * Programme main
 * 		* Lancement du programme 
 * @author Eric Mariani
 * @since 06/12/2016
 *
 */
public class VizualizeMaitreMain implements VoiesAPI {
	public static void main(String[] args) {
		GestionLogger.initialisation(VizualizeMaitreMain.class.getName());
		GestionLogger.gestionLogger.info("Lancement de l'application");
		
		// initialisation base
		GestionLogger.gestionLogger.info("Initialisation des variables");
		EFS_Maitre_Variable.initialisationVariableBase();

		GestionLogger.gestionLogger.info("Connexion à la base de donnée");
		AE_Variables.ctnOracle = new FonctionsSGBD(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);
		
		// Test d'integrité de la base
		GestionSGBD.testIntegriteBase();
		
		// Chargement des voies API
		GestionLogger.gestionLogger.info("Chargement des voies API");
		GestionSGBD.lectureAnalogicInput();
		GestionSGBD.lectureDigitalInput();
		GestionSGBD.lecturePriseEnCompte();
		
		GestionLogger.gestionLogger.info("Lecture tps réel des voies API");
		@SuppressWarnings("unused")
		GestionAPI lectureAPI = new GestionAPI();
		
		GestionLogger.gestionLogger.info("Appel fenetre Voies API");
		FenPrincipale fenetre = new FenPrincipale();
		fenetre.setVisible(true);
	}
}
