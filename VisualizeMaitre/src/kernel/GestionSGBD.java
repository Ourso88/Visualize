package kernel;

import java.time.format.DateTimeFormatter;

import em.fonctions.GestionLogger;
import em.general.AE_Variables;

/**
 * Gestion des echanges avec la Base de données
 * @author Eric Mariani
 * @since 19/02/2017
 *
 */
public class GestionSGBD implements VoiesAPI{
	
	/**
	 * Historise une alarme
	 * @param indexAlarme
	 */
	public static void historiserAlarme(int indexAlarme) {
		try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String strSql = "";
			
			String strDateApparition = tbAlarme.get(indexAlarme).getDateApparition().format(formatter);
			String strDateDisparition = tbAlarme.get(indexAlarme).getDateDisparition().format(formatter);
			String strDatePriseEnCompte = tbAlarme.get(indexAlarme).getDatePriseEnCompte().format(formatter);

			
			strSql = "INSERT INTO V2_AlarmeHistorique (idCapteur, VoieAPI, DateApparition, DatePriseEnCompte, DateDisparition, idPriseEncompte, CommentairePriseEnCompte) VALUES("
				+ tbAlarme.get(indexAlarme).getIdCapteur()
				+ ", " + tbAnaAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getVoieApi()
				+ ", '" + strDateApparition + "'"
				+ ", '" + strDateDisparition + "'"
				+ ", '" + strDatePriseEnCompte + "'"
				+ ", " + tbAlarme.get(indexAlarme).getIdPriseEnCompte()
				+ ", '" + tbAlarme.get(indexAlarme).getCommentairePriseEnCompte() + "'"
				+ ")";
			AE_Variables.ctnOracle.fonctionSql(strSql);
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Erreur historiserAlarme : " + e.getMessage());
		}
	}
	
	/**
	 * Ecriture dans la SGBD des valeur API pour les voies
	 */
	public static void historiseValeurVoiesAPI() {
		try {
			String strSql = "";
			// Analogique
			for(int i = 0; i < tbAnaAPI.size(); i++) {
				strSql = "INSERT INTO AI_HISTORIQUE (idCapteur, VoieAPI, DateLecture, Valeur) VALUES("
					+ tbAnaAPI.get(i).getIdCapteur()
					+ ", " + tbAnaAPI.get(i).getVoieApi()
					+ ", sysdate"
					+ ", " + tbAnaAPI.get(i).getValeurAPI()
					+ ")";
				AE_Variables.ctnOracle.fonctionSql(strSql);
			}

			// Analogique
			for(int i = 0; i < tbDigiAPI.size(); i++) {
				strSql = "INSERT INTO DI_HISTORIQUE (idCapteur, VoieAPI, DateLecture, Valeur) VALUES("
					+ tbDigiAPI.get(i).getIdCapteur()
					+ ", " + tbDigiAPI.get(i).getVoieApi()
					+ ", sysdate"
					+ ", " + tbDigiAPI.get(i).getValeurAPI()
					+ ")";
				AE_Variables.ctnOracle.fonctionSql(strSql);
			}
			
			
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Erreur historiserAlarme : " + e.getMessage());
		}
	}
	
	
	
}
