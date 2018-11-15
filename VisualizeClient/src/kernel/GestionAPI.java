package kernel;

import AE_General.AE_Constantes;
import AE_General.AE_Fonctions;

/**
 * Gere les écanhes avec l'API
 * @author Eric Mariani
 * @since 15/11/2018
 *
 */
public class GestionAPI implements AE_Constantes {

	/**
	 * Previens l'automate d'un rappel pour une alarme
	 * @param idCapteur
	 */
	public static void ecrireRappelAlert(int idCapteur, int tpsRappel) {
		// Ecrire le temps dans V2_AlarmeEnCours
		GestionSGBD.gererRappelAlert(idCapteur, tpsRappel);
		// Ecrire dans l'automate
		AE_Fonctions.modifierMaitreViaClient(idCapteur, VIA_API_RAPPEL_ALERT);
	}
	
	
}
