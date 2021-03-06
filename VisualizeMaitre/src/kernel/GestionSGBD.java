package kernel;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import em.fonctions.AE_Fonctions;
import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;
import em.general.EFS_General;
import em.general.EFS_Maitre_Variable;
import em.sgbd.FonctionsSGBD;

/**
 * Gestion des echanges avec la Base de donn�es
 * @author Eric Mariani
 * @since 19/02/2017
 *
 */
public class GestionSGBD implements VoiesAPI, EFS_General {
	
	/**
	 * Teste si la connexion � la base de donn�es n'est pas ferm�e
	 */
	private static void testConnexionBase() {
		try {
			if(AE_Variables.ctnOracle.ctn.isClosed()) {
				GestionLogger.gestionLogger.warning("Erreur Connexion SGBD Ferm�e ...");
				AE_Variables.ctnOracle.ctn.clearWarnings();
				AE_Variables.ctnOracle = new FonctionsSGBD(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);
				EFS_Maitre_Variable.compteurErreurSGBD++;
				if(AE_Variables.ctnOracle.ctn.isClosed()) {
					GestionLogger.gestionLogger.warning("Erreur Connexion SGBD TOUJOURS Ferm�e ...");
					EFS_Maitre_Variable.compteurErreurSGBD++;
				}
			}
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("Erreur Connexion SGBD Ferm�e impossible de rouvrir ... ");
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}

	/**
	 * Teste si la connexion � la base de donn�es n'est pas ferm�e
	 */
	private static void testConnexionBaseHistorise() {
		try {
			if(AE_Variables.ctnHistorise.ctn.isClosed()) {
				GestionLogger.gestionLogger.warning("Erreur Connexion SGBD Ferm�e ...");
				AE_Variables.ctnHistorise.ctn.clearWarnings();
				AE_Variables.ctnHistorise = new FonctionsSGBD(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);
				EFS_Maitre_Variable.compteurErreurSGBD++;
				if(AE_Variables.ctnHistorise.ctn.isClosed()) {
					GestionLogger.gestionLogger.warning("Erreur Connexion SGBD TOUJOURS Ferm�e ...");
					EFS_Maitre_Variable.compteurErreurSGBD++;
				}
			}
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("Erreur Connexion SGBD Ferm�e impossible de rouvrir ... ");
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

			int appelAlert = 0;
			if(tbAlarme.get(indexAlarme).isAppelAlert()) {
				appelAlert = 1;
			}
			
			String strDescription = tbAlarme.get(indexAlarme).getDescriptionCapteur();
			int typeCapteur = -1;
			int voieAPI = -1;
			long seuilTempo = -1;
			long seuilHaut = -1;
			long seuilBas = -1;
			int nOnF = -1;
			String nomCapteur = "---";
			if(tbAlarme.get(indexAlarme).getTypeCapteur() == CAPTEUR_ANALOGIQUE_ENTREE) {
				typeCapteur = CAPTEUR_ANALOGIQUE_ENTREE;
				voieAPI = tbAnaAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getVoieApi();
				seuilTempo = tbAnaAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getSeuilTempo();
				seuilHaut = tbAnaAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getSeuilHaut();
				seuilBas = tbAnaAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getSeuilBas();
				nomCapteur = tbAnaAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getNom();
			} else if(tbAlarme.get(indexAlarme).getTypeCapteur() == CAPTEUR_DIGITAL_ENTREE) {
				typeCapteur = CAPTEUR_DIGITAL_ENTREE;
				voieAPI = tbDigiAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getVoieApi();
				seuilTempo = tbDigiAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getTempo();
				nOnF = tbDigiAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getnOnF();
				nomCapteur = tbDigiAPI.get(tbAlarme.get(indexAlarme).getIndexCapteur()).getNom();
			} else {
				GestionLogger.gestionLogger.severe("[DIVERS] - Erreur sur type capteur");
			}

			strSql = "INSERT INTO AlarmeHistorique (idCapteur, VoieAPI, TypeCapteur, DateApparition, DateDisparition, DatePriseEnCompte, SeuilTempo, SeuilHaut, SeuilBas, NONF,"
				   + " idPriseEncompte, CommentairePriseEnCompte, AppelAlert, idUtilisateur, DescriptionAlarme, idAlarmeService) VALUES("
				+ tbAlarme.get(indexAlarme).getIdCapteur()
				+ ", " + voieAPI
				+ ", " + typeCapteur
				+ ", '" + strDateApparition + "'"
				+ ", '" + strDateDisparition + "'"
				+ ", '" + strDatePriseEnCompte + "'"
				+ ", " + seuilTempo
				+ ", " + seuilHaut
				+ ", " + seuilBas
				+ ", " + nOnF
				+ ", " + tbAlarme.get(indexAlarme).getIdPriseEnCompte()
				+ ", '" + tbAlarme.get(indexAlarme).getCommentairePriseEnCompte() + "'"
				+ ", " + appelAlert
				+ ", " + tbAlarme.get(indexAlarme).getIdUtilisateur()
				+ ", '" + tbAlarme.get(indexAlarme).getDescriptionAlarme() + "'"
				+ ", " + tbAlarme.get(indexAlarme).getIdAlarmeService()
				+ ")";
			AE_Variables.ctnOracle.fonctionSql(strSql);
			
			// Le supprimer de la table  V2_AlarmeEnCours
			AE_Variables.ctnOracle.fonctionSql("DELETE FROM V2_AlarmeEnCours WHERE idCapteur = " + tbAlarme.get(indexAlarme).getIdCapteur());
			
			// Ajouter � la tbAlarmeHistorique
			tbAlarmeHistorique.add(new AlarmeHistorique(
			-1, // idAlarmeHistorique
			tbAlarme.get(indexAlarme).getIdCapteur(), // idCapteur
			typeCapteur, // typeCapteur
			strDateApparition, // dateApparition
			strDatePriseEnCompte, // datePriseEnCompte
			strDateDisparition, // dateDisparition
			nomCapteur, // nomCapteur
			strDescription,
			tbAlarme.get(indexAlarme).getIdAlarmeService()			
			)); 
			
			
			EFS_Maitre_Variable.nombreLectureSGBD++;
		} catch (Exception e) {
			GestionLogger.gestionLogger.severe("SGBD - Erreur historiserAlarme : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}
	
	/**
	 * Ecriture dans la SGBD des valeur API pour les voies
	 */
	public static void historiseValeurVoiesAPI() {
		testConnexionBaseHistorise();
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
				AE_Variables.ctnHistorise.fonctionSql(strSql);
				EFS_Maitre_Variable.nombreLectureSGBD++;
			}

			// Analogique
			for(int i = 0; i < tbDigiAPI.size(); i++) {
				strSql = "INSERT INTO DI_HISTORIQUE (idCapteur, VoieAPI, DateLecture, Valeur) VALUES("
					+ tbDigiAPI.get(i).getIdCapteur()
					+ ", " + tbDigiAPI.get(i).getVoieApi()
					+ ", sysdate"
					+ ", " + tbDigiAPI.get(i).getValeurAPI()
					+ ")";
				AE_Variables.ctnHistorise.fonctionSql(strSql);
				EFS_Maitre_Variable.nombreLectureSGBD++;
			}
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Erreur historiserAlarme : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}
	

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
			EFS_Maitre_Variable.nombreLectureSGBD++;
			// Table AlarmeEnCours
			strSql = "DELETE FROM AlarmeEnCours WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
			// Table Inhibition
			strSql = "DELETE FROM Inhibition WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Erreur suppression En Maintenance : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
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
					// R�paration de l'int�git� - Remise en service dans la table Capteur
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
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}		
	}
	
	/**
	 * Remplit le tableau d'entrees analogiques
	 */
	public static void lectureAnalogicInput() {
		testConnexionBase();
		// Lecture donn�es
		try {
			String strSql = "SELECT * FROM (((EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur)"
					+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement))"
					+ " LEFT JOIN AlarmeService ON Capteur.idAlarmeService = AlarmeService.idAlarmeService"
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
				result.getInt("valeurConsigne"),
				result.getInt("ActivationPreSeuil"),
				result.getLong("idAlarmeService"),
				result.getString("NomService"),
				result.getInt("IndexMotApi"),
				result.getBoolean("klaxon")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur lecture Table EntreeAnalogique : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}
	
	/**
	 * Remplit le tableau d'entrees digitales
	 */
	public static void lectureDigitalInput() {
		testConnexionBase();
		// Lecture donn�es
		try {
			String strSql = "SELECT * FROM (((EntreeDigitale LEFT JOIN Capteur ON EntreeDigitale.idCapteur = Capteur.idCapteur)"
					+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement))"
					+ " LEFT JOIN AlarmeService ON Capteur.idAlarmeService = AlarmeService.idAlarmeService"
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
				result.getInt("nOnF"),
				result.getLong("idAlarmeService"),
				result.getString("NomService"),
				result.getInt("IndexMotApi"),
				result.getBoolean("klaxon")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur lecture Table EntreeDigitale : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}	

	/**
	 * Remplit le tableau des prise en compte
	 */
	public static void lecturePriseEnCompte() {
		testConnexionBase();
		// Lecture donn�es
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
			GestionLogger.gestionLogger.severe("SGBD : Erreur lecture Table PriseEnCompte : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}

	}	
	
	/**
	 * Modification dans la SGBD du seuil bas de la voie API
	 */
	public static boolean modifierSeuilBas(long idCapteur, int seuilBas) {
		testConnexionBase();
		try {
			String strSql = "UPDATE EntreeAnalogique SET SeuilBas = " + seuilBas + " WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
			return true;
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Modification seuil bas : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
			return false;
		}
	}

	/**
	 * Modification dans la SGBD du seuil haut de la voie API
	 */
	public static boolean modifierSeuilHaut(long idCapteur, int seuilHaut) {
		testConnexionBase();
		try {
			String strSql = "UPDATE EntreeAnalogique SET SeuilHaut = " + seuilHaut + " WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
			return true;
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Modification seuil haut : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
			return false;
		}
	}
	
	/**
	 * Modification dans la SGBD de la tempo seuil de la voie API
	 */
	public static boolean modifierSeuilTempo(long idCapteur, int seuilTempo) {
		testConnexionBase();
		try {
			String strSql = "UPDATE EntreeAnalogique SET SeuilTempo = " + seuilTempo + " WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
			return true;
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("SGBD - Modification seuil tempo : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
			return false;
		}
	}

	/**
	 * Enregistre dans la base Oracle l'alarme qui vient de se d�clencher
	 * @param indexAlarme
	 */
	public static void enregistrerAlarmeEnCours(int indexAlarme) {
		testConnexionBase();
		try {
			long idCapteur = tbAlarme.get(indexAlarme).getIdCapteur();
			String strSql = "SELECT * FROM V2_AlarmeEnCours WHERE idCapteur = " + idCapteur;
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			while(result.next()) {
				AE_Variables.ctnOracle.fonctionSql("DELETE FROM V2_AlarmeEnCours WHERE idCapteur = " + idCapteur);
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
			
			// Enregistrer dans la base la nouvelle Alarme
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			String strDateApparition = tbAlarme.get(indexAlarme).getDateApparition().format(formatter);
			double valeur = tbAlarme.get(indexAlarme).getValeurAPI();
			
			strSql = "INSERT INTO V2_AlarmeEnCours (idCapteur, DateApparition, Valeur, DescriptionAlarme, TypeCapteur, idAlarmeService) VALUES("
					+ idCapteur
					+ ", '"  + strDateApparition + "'"
					+ ", " + valeur
					+ ", '" + tbAlarme.get(indexAlarme).getDescriptionAlarme() + "'"
					+ ", " + tbAlarme.get(indexAlarme).getTypeCapteur()
					+ ", " + tbAlarme.get(indexAlarme).getIdAlarmeService()
					+ ")";
				AE_Variables.ctnOracle.fonctionSql(strSql);
				EFS_Maitre_Variable.nombreLectureSGBD++;
			
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur enregistrer alarme en cours : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}

	/**
	 * Enregistre la date de disparition dans V2_AlarmeEnCours
	 * @param idCapteur
	 */
	public static void enregistrerDateDisparition(long idCapteur, LocalDateTime dateDisparition) {
		testConnexionBase();
		try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			String strDateDisparition = dateDisparition.format(formatter);
			
			String strSql = "UPDATE V2_AlarmeEnCours SET DateDisparition = '" + strDateDisparition + "' WHERE idCapteur = " + idCapteur;
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
		} catch (Exception e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur enregistrer date disparition : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}
	
	/**
	 * Prise en compte d'un Rappel
	 */
	public static boolean prendreEnCompteRappelAlert(long idCapteur) {
		testConnexionBase();
		// Recherche dans la table V2_AlarmeEnCours
		try {
			boolean prisEnCompte = false;
			String strSql = "SELECT * FROM V2_AlarmeEnCours WHERE idCapteur = " + idCapteur;
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			if(result.next()) {
				// Rechercher dans la tbAlarme
				for(int i = 0; i < tbAlarme.size(); i++) {
					if(tbAlarme.get(i).getIdCapteur() == idCapteur) {
						tbAlarme.get(i).setMnRappelAlert(result.getInt("RappelAlert"));
						tbAlarme.get(i).setDateRappelAlert(LocalDateTime.now());
						GestionAPI.gestionAlertService(false, tbAlarme.get(i).getIndexMotApi()); 
						prisEnCompte = true;
						break;
					}
				}
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
			return prisEnCompte;
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur de prise en compte via un client : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
			return false;
		}
	}
	
	
	/**
	 * Prise en compte d'une alarme par un client
	 */
	public static boolean prendreEnCompteViaAPI(long idCapteur) {
		testConnexionBase();
		// Recherche dans la table V2_AlarmeEnCours
		try {
			boolean prisEnCompte = false;
			String strSql = "SELECT * FROM V2_AlarmeEnCours WHERE idCapteur = " + idCapteur;
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			if(result.next()) {
				// Rechercher dans la tbAlarme
				for(int i = 0; i < tbAlarme.size(); i++) {
					if(tbAlarme.get(i).getIdCapteur() == idCapteur) {
				        tbAlarme.get(i).setDatePriseEnCompte(LocalDateTime.now());
				        tbAlarme.get(i).setPrisEnCompte(true);
						tbAlarme.get(i).setIdPriseEnCompte(result.getInt("idPriseEnCompte"));
						tbAlarme.get(i).setCommentairePriseEnCompte(result.getString("CommentairePriseEnCompte"));
						tbAlarme.get(i).setIdUtilisateur(result.getLong("idUtilisateur"));
						tbAlarme.get(i).setMnRappelAlert(result.getInt("RappelAlert"));
						// Couper Klaxon
						GestionAPI.gestionKlaxon(false);
						// Couper Appel Alert
						GestionAPI.gestionAlert(false);
						GestionAPI.gestionAlertService(false, tbAlarme.get(i).getIndexMotApi());
						prisEnCompte = true;
						break;
					}
				}
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
			return prisEnCompte;
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur de prise en compte via un client : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
			return false;
		}
	}

	/**
	 * Modification de valeurs par un client
	 */
	public static boolean modifierViaAPI(int typeModification, long idCapteur) {
		testConnexionBase();
		try {
			boolean retour = false;
			String strSql = "SELECT * FROM Capteur WHERE idCapteur = " + idCapteur;
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			if(result.next()) {
				int typeCapteur = result.getInt("TypeCapteur");
				

				if(typeModification == EFS_General.VIA_API_MAINTENANCE) {
					int inhibition = result.getInt("Inhibition");
					if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
						for(int i = 0; i < tbAnaAPI.size(); i++) {
							if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
								tbAnaAPI.get(i).setInhibition(inhibition);
								GestionLogger.gestionLogger.info("[SGBD] Modification Inhibition via API idCapteur = " + idCapteur + " � " + inhibition);
								retour = true;
							}
						}
					} else {
						for(int i = 0; i < tbDigiAPI.size(); i++) {
							if(tbDigiAPI.get(i).getIdCapteur() == idCapteur) {
								tbDigiAPI.get(i).setInhibition(inhibition);
								GestionLogger.gestionLogger.info("[SGBD] Modification Inhibition via API idCapteur = " + idCapteur + " � " + inhibition);
								retour = true;
							}
						}
					}
				}
				
				if(typeModification == EFS_General.VIA_API_ALARME) {
					int alarme = result.getInt("Alarme");
					if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
						for(int i = 0; i < tbAnaAPI.size(); i++) {
							if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
								tbAnaAPI.get(i).setAlarme(alarme);
								GestionLogger.gestionLogger.info("[SGBD] Modification Alarme via API idCapteur = " + idCapteur + " � " + alarme);
								retour = true;
							}
						}
					} else {
						for(int i = 0; i < tbDigiAPI.size(); i++) {
							if(tbDigiAPI.get(i).getIdCapteur() == idCapteur) {
								tbDigiAPI.get(i).setAlarme(alarme);
								GestionLogger.gestionLogger.info("[SGBD] Modification Alarme via API idCapteur = " + idCapteur + " � " + alarme);
								retour = true;
							}
						}
					}
				}
				
				if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
					strSql = "SELECT * FROM EntreeAnalogique WHERE idCapteur = " + idCapteur;
				} else {
					strSql = "SELECT * FROM EntreeDigitale WHERE idCapteur = " + idCapteur;
				}
				result = AE_Variables.ctnOracle.lectureData(strSql);
				if(result.next()) {
					switch(typeModification) {
					case EFS_General.VIA_API_TEMPO:
						if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
							long tempo = result.getLong("SeuilTempo");
							for(int i = 0; i < tbAnaAPI.size(); i++) {
								if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
									tbAnaAPI.get(i).setSeuilTempo(tempo);
									GestionLogger.gestionLogger.info("[SGBD] Modification Tempo via API idCapteur = " + idCapteur + " � " + tempo);
									retour = true;
								}
							}
						} else {
							int tempo = result.getInt("Tempo");
							for(int i = 0; i < tbDigiAPI.size(); i++) {
								if(tbDigiAPI.get(i).getIdCapteur() == idCapteur) {
									tbDigiAPI.get(i).setTempo(tempo);;
									GestionLogger.gestionLogger.info("[SGBD] Modification Tempo via API idCapteur = " + idCapteur + " � " + tempo);
									retour = true;
								}
							}
						}
					case EFS_General.VIA_API_PRE_SEUIL_TEMPO:
						if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
							long tempo = result.getLong("PreSeuilTempo");
							for(int i = 0; i < tbAnaAPI.size(); i++) {
								if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
									tbAnaAPI.get(i).setPreSeuilTempo(tempo);
									GestionLogger.gestionLogger.info("[SGBD] Modification Pre seuil Tempo via API idCapteur = " + idCapteur + " � " + tempo);
									retour = true;
								}
							}
						}

					case EFS_General.VIA_API_CALIBRATION:
						if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
							int calibration = result.getInt("Calibration");
							for(int i = 0; i < tbAnaAPI.size(); i++) {
								if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
									tbAnaAPI.get(i).setCalibration(calibration);
									GestionLogger.gestionLogger.info("[SGBD] Modification Calibration via API idCapteur = " + idCapteur + " � " + calibration);
									retour = true;
								}
							}
						}

					case EFS_General.VIA_API_NO_NF:
						if(typeCapteur == EFS_General.CAPTEUR_DIGITAL_ENTREE) {
							int nOnF = result.getInt("NONF");
							for(int i = 0; i < tbDigiAPI.size(); i++) {
								if(tbDigiAPI.get(i).getIdCapteur() == idCapteur) {
									tbDigiAPI.get(i).setnOnF(nOnF);
									GestionLogger.gestionLogger.info("[SGBD] Modification NO/NF via API idCapteur = " + idCapteur + " � " + nOnF);
									retour = true;
								}
							}
						}
					
					case EFS_General.VIA_API_SEUIL_BAS:
					case EFS_General.VIA_API_SEUIL_HAUT:
					case EFS_General.VIA_API_PRE_SEUIL_BAS:
					case EFS_General.VIA_API_PRE_SEUIL_HAUT:
						if(typeCapteur == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
							int seuilBas = result.getInt("SeuilBas");
							int seuilHaut = result.getInt("SeuilHaut");
							int preSeuilBas = result.getInt("PreSeuilBas");
							int preSeuilHaut = result.getInt("PreSeuilHaut");
							for(int i = 0; i < tbAnaAPI.size(); i++) {
								if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
									tbAnaAPI.get(i).setSeuilBas(seuilBas);
									tbAnaAPI.get(i).setSeuilHaut(seuilHaut);
									tbAnaAPI.get(i).setPreSeuilBas(preSeuilBas);
									tbAnaAPI.get(i).setPreSeuilHaut(preSeuilHaut);
									GestionLogger.gestionLogger.info("[SGBD] Modification des seuils via API idCapteur = " + idCapteur);
									retour = true;
								}
							}
						}
						break;
						
					default:
						break;
					}
				} else {
					GestionLogger.gestionLogger.warning("SGBD : Erreur modification via un client Capteur Ana ou Digi non trouv� ");
					retour = false;
				}
			} else {
				GestionLogger.gestionLogger.warning("SGBD : Erreur modification via un client Capteur non trouv� ");
				retour = false;
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
			return retour;
		} catch (SQLException e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur modification via un client : " + e.getMessage());
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
			return false;
		}
	}
	
	/**
	 *  Vide la table V2_AlarmeEnCours
	 */
	public static void viderAlarmeEnCours() {
		testConnexionBase();
		try {
			String strSql = "DELETE FROM V2_AlarmeEnCours";
			AE_Variables.ctnOracle.fonctionSql(strSql);
			EFS_Maitre_Variable.nombreLectureSGBD++;
		} catch (Exception e) {
			GestionLogger.gestionLogger.severe("SGBD : Erreur vidage V2_AlarmeEnCours");
			EFS_Maitre_Variable.nombreLectureSGBD++;
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}

	/**
	 * Test la version du programme par rapport � celle de la base de donn�es
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
			if (versionBase != AE_Constantes.VERSION) AE_Fonctions.afficheMessage(appel, "AVERTISSEMENT", "La version du programme n'est pas � jour ! Veuillez contacter le service technique ...");
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
