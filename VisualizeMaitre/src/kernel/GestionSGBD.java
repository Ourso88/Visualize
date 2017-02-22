package kernel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import em.fonctions.GestionLogger;
import em.general.AE_Variables;
import em.general.EFS_General;
import em.general.EFS_Maitre_Variable;
import em.sgbd.FonctionsSGBD;

/**
 * Gestion des echanges avec la Base de données
 * @author Eric Mariani
 * @since 19/02/2017
 *
 */
public class GestionSGBD implements VoiesAPI{
	
	/**
	 * Teste si la connexion à la base de données n'est pas fermée
	 */
	private static void testConnexionBase() {
		try {
			if(AE_Variables.ctnOracle.ctn.isClosed()) {
				GestionLogger.gestionLogger.warning("Erreur Connexion SGBD Fermée ...");
				AE_Variables.ctnOracle.ctn.clearWarnings();
				AE_Variables.ctnOracle = new FonctionsSGBD(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);
				EFS_Maitre_Variable.compteurErreurSGBD++;
				if(AE_Variables.ctnOracle.ctn.isClosed()) {
					GestionLogger.gestionLogger.warning("Erreur Connexion SGBD TOUJOURS Fermée ...");
					EFS_Maitre_Variable.compteurErreurSGBD++;
				}
			}
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("Erreur Connexion SGBD Fermée impossible de rouvrir ... ");
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}
	
	/**
	 * Historise une alarme
	 * @param indexAlarme
	 */
	public static void historiserAlarme(int indexAlarme) {
		testConnexionBase();
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
		testConnexionBase();
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
	
	/**
	 * Gere la gestion des Appels Alert à travers de la SGBD 
	 * @param alerte
	 */
	public static void gestionAlert(boolean alerte) {
		testConnexionBase();
		try {
			String strSql = "";
			if (alerte) {
				strSql = "UPDATE AlarmeAlerte SET Alarme = 1 WHERE idAlarmeAlerte = 1";
				AE_Variables.ctnOracle.fonctionSql(strSql);
			} 
			else {
	
				strSql = "UPDATE AlarmeAlerte SET Alarme = 0, RelanceProgramme = 0 WHERE idAlarmeAlerte = 1";
				AE_Variables.ctnOracle.fonctionSql(strSql);		
			} // Fin if alerte
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Erreur APPEL ALERT : " + e.getMessage());
		}
	} // Fin gestionAlert()	
	
	/**
	 * Gestion du retrait d'un capteur en maintance
	 * @param idCapteur
	 */
	public static void enleverDeMaintenance(long idCapteur) {
		testConnexionBase();
		try {
			// Table Capteur
			String strSql = "UPDATE Capteur SET Inhibition = 0 WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			// Table AlarmeEnCours
			strSql = "DELETE FROM AlarmeEnCours WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			// Table Inhibition
			strSql = "DELETE FROM Inhibition WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Erreur suppression En Maintenance : " + e.getMessage());
		}
	}
	
	/**
	 * Test l'integrite de la base de donnees
	 */
	public static void testIntegriteBase() {
		testConnexionBase();
		try {
			// Test En Maintenance : Capteur --> Inhibition
			String strSql = "SELECT * FROM Capteur WHERE Inhibition = " + EFS_General.CAPTEUR_EN_MAINTENANCE;
			ResultSet rsCapteur = AE_Variables.ctnOracle.lectureData(strSql);
			while(rsCapteur.next()) {
				strSql = "SELECT * FROM Inhibition WHERE idCapteur = " + rsCapteur.getLong("idCapteur");
				ResultSet rsMaintenance = AE_Variables.ctnOracle.lectureData(strSql);
				if(!rsMaintenance.next()) {
					GestionLogger.gestionLogger.warning("<== INTEGRITE ==> Test En Maintenance: Capteur --> Inhibition / idCapteur : " + rsCapteur.getLong("idCapteur"));
					// Réparation de l'intégité - Remise en service dans la table Capteur
					AE_Variables.ctnOracle.fonctionSql("UPDATE Capteur SET Inhibition = " + EFS_General.CAPTEUR_EN_SERVICE + " WHERE idCapteur = " + rsCapteur.getLong("idCapteur"));
				}
				rsMaintenance.close();
			}
			rsCapteur.close();
			AE_Variables.ctnOracle.closeLectureData();

			// Test En Maintenance : Inhibition --> Capteur
			strSql = "SELECT * FROM Inhibition LEFT JOIN Capteur ON Inhibition.idCapteur = Capteur.idCapteur";
			ResultSet rsMaintenance = AE_Variables.ctnOracle.lectureData(strSql);
			while(rsMaintenance.next()) {
				if(rsMaintenance.getInt("Inhibition") == EFS_General.CAPTEUR_EN_SERVICE) {
					GestionLogger.gestionLogger.warning("<== INTEGRITE ==> Test En Maintenance : Inhibition --> Capteur / idCapteur : " + rsCapteur.getLong("idCapteur"));
				}
			}
			rsMaintenance.close();
			AE_Variables.ctnOracle.closeLectureData();

		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur Test Integrite : " + e.getMessage());
		}		
	}
	
	/**
	 * Remplit le tableau d'entrees analogiques
	 */
	public static void lectureAnalogicInput() {
		testConnexionBase();
		// Lecture données
		try {
			String strSql = "SELECT * FROM ((EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur)"
					+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
					+ " WHERE TypeCapteur = 1 ORDER BY VoieApi";
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			while(result.next()) {
				tbAnaAPI.add(new AnalogicInput(
				result.getLong("idCapteur"),
				result.getString("nom"),
				result.getString("description"),
				result.getLong("idEquipement"),
				result.getLong("idPosteTechnique"),
				result.getLong("idTypeMateriel"),
				result.getLong("idZoneSubstitution"),
				result.getInt("typeCapteur"),
				result.getInt("alarme"),
				result.getLong("idService"),
				result.getInt("voieApi"),
				result.getInt("inhibition"),
				result.getLong("idUnite"),
				result.getString("contact"),
				result.getString("NumeroInventaire"),
				result.getLong("idEntreeAnalogique"),
				result.getInt("seuilHaut"),
				result.getInt("preSeuilHaut"),
				result.getInt("seuilBas"),
				result.getInt("preSeuilBas"),
				result.getInt("calibration"),
				result.getLong("seuilTempo"),
				result.getLong("preSeuilTempo"),
				result.getString("unite"),
				result.getInt("valeurConsigne")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table EntreeAnalogique : " + e.getMessage());
		}
	}
	
	/**
	 * Remplit le tableau d'entrees digitales
	 */
	public static void lectureDigitalInput() {
		testConnexionBase();
		// Lecture données
		try {
			String strSql = "SELECT * FROM ((EntreeDigitale LEFT JOIN Capteur ON EntreeDigitale.idCapteur = Capteur.idCapteur)"
					+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
					+ " WHERE TypeCapteur = 2 ORDER BY VoieApi";
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			while(result.next()) {
				tbDigiAPI.add(new DigitalInput(
				result.getLong("idCapteur"),
				result.getString("nom"),
				result.getString("description"),
				result.getLong("idEquipement"),
				result.getLong("idPosteTechnique"),
				result.getLong("idTypeMateriel"),
				result.getLong("idZoneSubstitution"),
				result.getInt("typeCapteur"),
				result.getInt("alarme"),
				result.getLong("idService"),
				result.getInt("voieApi"),
				result.getInt("inhibition"),
				result.getLong("idUnite"),
				result.getString("contact"),
				result.getString("NumeroInventaire"),
				result.getLong("idEntreeDigitale"),
				result.getInt("tempo"),
				result.getInt("nOnF")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table EntreeDigitale : " + e.getMessage());
		}
	}	

	/**
	 * Remplit le tableau des prise en compte
	 */
	public static void lecturePriseEnCompte() {
		testConnexionBase();
		// Lecture données
		try {
			String strSql = "SELECT * FROM PriseEnCompte";
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			while(result.next()) {
				tbPriseEnCompte.add(new PriseEnCompte(
				result.getLong("idPriseEnCompte"),
				result.getString("Nom"),
				result.getString("Description")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table PriseEnCompte : " + e.getMessage());
		}

	}	
	
	/**
	 * Renvoie un tableau des raisons de prise en compte
	 */
	public static String[] renvoieRaisonPriseEnCompte() {
		String tbRaison[] = new String[tbPriseEnCompte.size()];
		for(int i = 0; i < tbPriseEnCompte.size(); i++) {
			tbRaison[i] = tbPriseEnCompte.get(i).getNom();
		}
		return tbRaison;
	}
}
