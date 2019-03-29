package AE_General;

import java.awt.Color;
import java.awt.Font;

public interface AE_Constantes {
	// Timer
	public static final int TIMER_RAFFRAICHISSEMENT_MINUTE = 60000;
	
	// Constantes version
//	public static final int VERSION = 10803;
	public static final int VERSION = 10804; // Correction de Probleme index dans la base de données, idCapteur --> obligé d'augmenter la taille
	
	// Contantes Font
	public static final Font FONT_ARIAL_12 = new Font("Arial", Font.PLAIN,  12 );
	public static final Font FONT_ARIAL_14 = new Font("Arial", Font.PLAIN,  14 );
	public static final Font FONT_ARIAL_16 = new Font("Arial", Font.PLAIN,  16 );
	public static final Font FONT_ARIAL_20 = new Font("Arial", Font.PLAIN,  20 );

	public static final Font FONT_ARIAL_12_BOLD = new Font("Arial", Font.BOLD,  12 );
	public static final Font FONT_ARIAL_14_BOLD = new Font("Arial", Font.BOLD,  14 );
	public static final Font FONT_ARIAL_16_BOLD = new Font("Arial", Font.BOLD,  16 );
	public static final Font FONT_ARIAL_20_BOLD = new Font("Arial", Font.BOLD,  20 );
	
	// Constantes SGBD
	public static final int AE_SGBD_ACCESS = 1;
	public static final int AE_SGBD_MYSQL = 2;
	public static final int AE_SGBD_SQLITE = 3;
	public static final int AE_SGBD_ORACLE = 4;
	
	// Logger
	public static final String LOGGER_FICHIER_TEXTE = "GTC_CLIENT_Logging.log";
	
	// Constantes couleur
	Color EFS_BLEU = new Color(167, 198, 237); 
	Color EFS_MARRON = new Color(200, 130, 66); 
	Color EFS_VERT = new Color(143, 214, 189); 
	
	// Alarme
	public static final int ALARME_RIEN = 0;
	public static final int ALARME_ALERT = 1;
	public static final int ALARME_DEFAUT = 2;
	public static final int ALARME_ETAT = 3;

	public static final int ALARME_PRISE_EN_COMPTE = 1;
	
	public static final int ALARME_INHIBITION = 1;
	public static final int ALARME_TEMPO_DEPASSEE = 1;
	
	// Capteurs
	public static final int CAPTEUR_ANALOGIQUE_ENTREE = 1;
	public static final int CAPTEUR_DIGITAL_ENTREE = 2;
	
	public static final int CAPTEUR_DIGITAL_NO = 1;
	public static final int CAPTEUR_DIGITAL_NF = 2;

	// JTable Alarme
	public static final int JTABLE_ALARME_VOIE = 0;
	public static final int JTABLE_ALARME_DESCRIPTION_VOIE = 1;
	public static final int JTABLE_ALARME_INVENTAIRE = 2;
	public static final int JTABLE_ALARME_APPARITION = 3;
	public static final int JTABLE_ALARME_DISPARITION = 4;
	public static final int JTABLE_ALARME_PRISE_EN_COMPTE = 5;
	public static final int JTABLE_ALARME_DESCRIPTION_ALARME = 6;
	public static final int JTABLE_ALARME_TYPE_ALARME = 7;
	public static final int JTABLE_ALARME_VALEUR = 8;
	public static final int JTABLE_ALARME_APPEL_ALERT = 9;
	public static final int JTABLE_ALARME_RAPPEL_ALERT = 10;
	
	// JTable HistoriqueAlarme
	public static final int JTABLE_ALARME_HISTORIQUE_VOIE = 0;
	public static final int JTABLE_ALARME_HISTORIQUE_DESCRIPTION_VOIE = 1;
	public static final int JTABLE_ALARME_HISTORIQUE_APPARITION = 2;
	public static final int JTABLE_ALARME_HISTORIQUE_DISPARITION = 3;
	public static final int JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE = 4;
	public static final int JTABLE_ALARME_HISTORIQUE_DESCRIPTION_ALARME = 5;
	public static final int JTABLE_ALARME_HISTORIQUE_RAISON_ACQUITTEMENT = 6;
	public static final int JTABLE_ALARME_HISTORIQUE_UTILISATEUR = 7;
	public static final int JTABLE_ALARME_HISTORIQUE_COMMENTAIRE_PRISE_EN_COMPTE = 8;

	// JTable JOURNAL
	public static final int JTABLE_JOURNAL_DATE = 0;
	public static final int JTABLE_JOURNAL_ACTION = 1;
	public static final int JTABLE_JOURNAL_UTILISATEUR = 2;
	public static final int JTABLE_JOURNAL_VOIE = 3;
	public static final int JTABLE_JOURNAL_DESCRIPTION_VOIE = 4;
	public static final int JTABLE_JOURNAL_TYPE = 5;
	
	// JTable Capteur analogique
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_NOM = 0;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_EQUIPEMENT = 1;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_DESCRIPTION = 2;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_ETAT = 3;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_BAS = 4;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_HAUT = 5;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_TEMPO = 6;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_CALIBRATION = 7;
	public static final int JTABLE_CHOIX_CAPTEUR_ANA_VALEUR = 8;

	// JTable Capteur digitale
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_NOM = 0;
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_EQUIPEMENT = 1;
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_DESCRIPTION = 2;
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_ETAT = 3;
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_TEMPO = 4;
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_NONF = 5;
	public static final int JTABLE_CHOIX_CAPTEUR_DIGI_VALEUR = 6;
	
	// JTable inhibition
	public static final int JTABLE_INHIBITION_VOIE = 0;
	public static final int JTABLE_INHIBITION_DESCRIPTION_VOIE = 1;
	public static final int JTABLE_INHIBITION_DATE_INHIBITION = 2;
	public static final int JTABLE_INHIBITION_RAISON_INHIBITION = 3;
	public static final int JTABLE_INHIBITION_UTILISATEUR = 4;	
	
	// JTable Historique Calibration
	public static final int JTABLE_HISTORIQUE_CALIBRATION_VOIE = 0;
	public static final int JTABLE_HISTORIQUE_CALIBRATION_DATE = 1;
	public static final int JTABLE_HISTORIQUE_CALIBRATION_ANC_VALEUR = 2;
	public static final int JTABLE_HISTORIQUE_CALIBRATION_NEW_VALEUR = 3;
	public static final int JTABLE_HISTORIQUE_CALIBRATION_UTILISATEUR = 4;

	// JTable ValidationHistoriqueAlarme
	public static final int JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_VALIDATION = 0;
	public static final int JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_DEBUT = 1;
	public static final int JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_FIN = 2;
	public static final int JTABLE_VALIDATION_HISTORIQUE_ALARME_UTILISATEUR = 3;
	
	
	// Lien SGBD Oracle Besancon
//	public static final int EFS_SGBD_TYPE = AE_SGBD_ORACLE;
//	public static final String EFS_SGBD_SERVEUR = new String("@srvbesbdd02.bfc-citrix.local:1521");
//	connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:mkyong", "username","password");	
//	public static final String EFS_SGBD_BASE = new String("GTCBFC");
//	public static final String EFS_SGBD_USER = new String("gtcbfc");
//	public static final String EFS_SGBD_MDP = new String("gtcbfc25");	
	
	
	// Lien SGBD Axiome Oracle
	public static final int EFS_SGBD_TYPE = AE_SGBD_ORACLE;
	public static final String EFS_SGBD_SERVEUR = new String("@localhost:1521");
	public static final String EFS_SGBD_BASE = new String("xe");
	public static final String EFS_SGBD_USER = new String("gtcbfc");
	public static final String EFS_SGBD_MDP = new String("gtcbfc25");

	
/*	
	// Lien SGBD
	public static final int EFS_SGBD_TYPE = AE_SGBD_SQLITE;
	public static final String EFS_SGBD_SERVEUR = new String("");
	public static final String EFS_SGBD_BASE = new String("efs.db");
	public static final String EFS_SGBD_USER = new String("");
	public static final String EFS_SGBD_MDP = new String("");
*/
/*	
	public static final int EFS_SGBD_TYPE = AE_SGBD_MYSQL;
	public static final String EFS_SGBD_SERVEUR = new String("localhost");
	public static final String EFS_SGBD_BASE = new String("efs");
	public static final String EFS_SGBD_USER = new String("root");
	public static final String EFS_SGBD_MDP = new String("");
*/

/*	
	// JTable HistoriqueAlarme
	public static final int JTABLE_ALARME_HISTORIQUE_VOIE = 0;
	public static final int JTABLE_ALARME_HISTORIQUE_DESCRIPTION_VOIE = 1;
	public static final int JTABLE_ALARME_HISTORIQUE_APPARITION = 2;
	public static final int JTABLE_ALARME_HISTORIQUE_DISPARITION = 3;
	public static final int JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE = 4;
	public static final int JTABLE_ALARME_HISTORIQUE_DESCRIPTION_ALARME = 5;
*/	
	// Type Seuil
	public static final int TYPE_SEUIL_BAS = 1;
	public static final int TYPE_SEUIL_HAUT = 2;
	public static final int TYPE_SEUIL_TEMPO = 3;
	public static final int TYPE_PRE_SEUIL_BAS = 4;
	public static final int TYPE_PRE_SEUIL_HAUT = 5;
	public static final int TYPE_PRE_SEUIL_TEMPO = 6;
	
	// Type Journal
	public static final int TYPE_JOURNAL_MODIFICATION_SEUIL = 1;
	public static final int TYPE_JOURNAL_INHIBITION_AJOUT = 2;
	public static final int TYPE_JOURNAL_INHIBITION_RETRAIT = 3;
	public static final int TYPE_JOURNAL_LOGGING_POSTE_MAITRE = 4;
	public static final int TYPE_JOURNAL_DELOGGING_POSTE_MAITRE = 5;		
	public static final int TYPE_JOURNAL_MODIFICATION_SEUIL_TEMPO = 6;
	public static final int TYPE_JOURNAL_MODIFICATION_PRE_SEUIL = 7;
	public static final int TYPE_JOURNAL_MODIFICATION_PRE_SEUIL_TEMPO = 8;
	public static final int TYPE_JOURNAL_MODIFICATION_CALIBRATION = 9;
	public static final int TYPE_JOURNAL_MODIFICATION_UNITE = 10;
	public static final int TYPE_JOURNAL_MODIFICATION_EQUIPEMENT = 11;
	public static final int TYPE_JOURNAL_MODIFICATION_POSTE_TECHNIQUE = 12;
	public static final int TYPE_JOURNAL_MODIFICATION_TYPE_MATERIEL = 13;
	public static final int TYPE_JOURNAL_MODIFICATION_ZONE_SUBSTITUTION = 14;
	public static final int TYPE_JOURNAL_MODIFICATION_ALARME = 15;
	public static final int TYPE_JOURNAL_MODIFICATION_NONF = 16;
	public static final int TYPE_JOURNAL_MODIFICATION_DESCRIPTION = 17;
	public static final int TYPE_JOURNAL_LOGGING_POSTE_CLIENT = 18;
	public static final int TYPE_JOURNAL_MODIFICATION_SERVICE = 19;
	public static final int TYPE_JOURNAL_MODIFICATION_VALEUR_CONSIGNE = 20;
	public static final int TYPE_JOURNAL_MODIFICATION_CONTACT = 21;
	
	// Echange Maitre Client
	public static final int ECHANGE_MAITRE_CLIENT_SEUIL_BAS = 1;
	public static final int ECHANGE_MAITRE_CLIENT_SEUIL_HAUT = 2;
	public static final int ECHANGE_MAITRE_CLIENT_SEUIL_TEMPO = 3;
	public static final int ECHANGE_MAITRE_CLIENT_INHIBITION = 4;
	public static final int ECHANGE_MAITRE_CLIENT_ALARME_DEFAUT = 5;
	public static final int ECHANGE_MAITRE_CLIENT_AJOUT_CAPTEUR = 6;
	public static final int ECHANGE_MAITRE_CLIENT_SUPPRESSION_CAPTEUR = 7;
	public static final int ECHANGE_MAITRE_CLIENT_CALIBRATION = 8;
	public static final int ECHANGE_MAITRE_CLIENT_PRE_SEUIL_BAS = 9;
	public static final int ECHANGE_MAITRE_CLIENT_PRE_SEUIL_HAUT = 10;
	public static final int ECHANGE_MAITRE_CLIENT_PRE_SEUIL_TEMPO = 11;
	public static final int ECHANGE_MAITRE_CLIENT_ALARME = 12;
	public static final int ECHANGE_MAITRE_CLIENT_NONF = 13;
	
	// Echange Maitre - Clients via API
	public static final int ADR_API_ECHANGE_MAITRE_CLIENT = 2060;
	public static final int VIA_API_PRISE_EN_COMPTE = 1;
	public static final int VIA_API_TEMPO = 2;
	public static final int VIA_API_PRE_SEUIL_TEMPO = 3;
	public static final int VIA_API_SEUIL_BAS = 4;
	public static final int VIA_API_SEUIL_HAUT = 5;
	public static final int VIA_API_PRE_SEUIL_BAS = 6;
	public static final int VIA_API_PRE_SEUIL_HAUT = 7;
	public static final int VIA_API_CALIBRATION = 8;
	public static final int VIA_API_ALARME = 9;
	public static final int VIA_API_MAINTENANCE = 10;
	public static final int VIA_API_NO_NF = 11;
	public static final int VIA_API_RAPPEL_ALERT = 12;
	public static final int MAX_ECHANGE_MAITRE_CLIENT = 50;
	
	
}
