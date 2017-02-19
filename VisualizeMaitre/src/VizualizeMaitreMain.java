import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;
import em.general.EFS_Maitre_Variable;
import em.sgbd.FonctionsSGBD;
import gui.vues.FenAlarmesEnCours;
import kernel.LectureAPI;
import kernel.VoiesAPI;

/**
 * Programme main
 * 		* Lancement du programme 
 * @author Eric Mariani
 * @since 06/12/2016
 *
 */
public class VizualizeMaitreMain implements AE_Constantes, VoiesAPI {
	public static void main(String[] args) {
		GestionLogger.initialisation(VizualizeMaitreMain.class.getName());
		GestionLogger.gestionLogger.info("Lancement de l'application");
		
		// initialisation base
		GestionLogger.gestionLogger.info("Initialisation des variables");
		EFS_Maitre_Variable.initialisationVariableBase();

		GestionLogger.gestionLogger.info("Connexion à la base de donnée");
		AE_Variables.ctnOracle = new FonctionsSGBD(AE_Constantes.AE_SGBD_TYPE, EFS_Maitre_Variable.EFS_SGBD_SERVEUR, EFS_Maitre_Variable.EFS_SGBD_BASE, EFS_Maitre_Variable.EFS_SGBD_USER, EFS_Maitre_Variable.EFS_SGBD_MDP);
		
		// Chargement des voies API
		GestionLogger.gestionLogger.info("Chargement des voies API");
		VoiesAPI.lectureAnalogicInput();
		VoiesAPI.lectureDigitalInput();

		GestionLogger.gestionLogger.info("Lecture tps réel des voies API");
		@SuppressWarnings("unused")
		LectureAPI lectureAPI = new LectureAPI();
		
		GestionLogger.gestionLogger.info("Appel fenetre Voies API");
		FenAlarmesEnCours fenetre = new FenAlarmesEnCours();
		fenetre.setVisible(true);
	}
}
