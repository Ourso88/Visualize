package em.general;

public interface EFS_General {
	public static final int POSTE_MAITRE = 1;
	
	public static final boolean MODE_DEBUG_BASE = false;

	public static final int MODBUS_PORT = 502;
//	public static final String ADR_IP_API = new String("10.27.0.10");
	public static final int NB_MOT_LECTURE_AI = 50;
	public static final int NB_MOT_LECTURE_DI = 40;
	public static final int NB_MOT_LECTURE_MAITRE_CLIENT = 50;
	  
//	public static final int ADR_API_AI_TPS_REEL = 500; // Adresse API Axiome
    public static final int ADR_API_AI_TPS_REEL = 1500; // Adresse AI - API Besançon et Dijon
	public static final int ADR_API_DI_TPS_REEL = 1410; // Adresse DI - API Besançon et Dijon
	
	// Gestion API - Alert
	public static final int ADR_API_CONTROLE_MAITRE = 2200;
	public static final int ADR_API_TEST_HORAIRE = 2201;
	public static final int ADR_API_ALARME = 2202;	
	public static final int ADR_API_ALARME_CONTROLE_MAITRE = 2203;

	public static final int ADR_API_ALARME_SERVICE = 2250;	
	
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
	
	
	
	public static final int MAX_AI = 400;
	public static final int MAX_DI = 640;
	public static final int MAX_ALARME_EN_COURS = 100;
	
	public static final int TEST_GTC_HEURE = 16;
//	public static final int TEST_GTC_MINUTE = 10;
	
	
	// Alarme
	public static final int ALARME_RIEN = 0;
	public static final int ALARME_ALERT = 1;
	public static final int ALARME_DEFAUT = 2;
	public static final int ALARME_ETAT = 3;
	
	// Type Capteur
	public static final int CAPTEUR_ANALOGIQUE_ENTREE = 1;
	public static final int CAPTEUR_DIGITAL_ENTREE = 2;
	
	public static final int CAPTEUR_DIGITAL_NO = 1;
	public static final int CAPTEUR_DIGITAL_NF = 2;

	// Inhibition
	public static final int CAPTEUR_EN_SERVICE = 0;
	public static final int CAPTEUR_EN_MAINTENANCE = 1;
	
	// PreSeuil
	public static final int PRE_SEUIL_INACTIF = 0;
	public static final int PRE_SEUIL_EN_ACTIVITE = 1;
	
	// Timer general
	
	// Timer rafraichissement fenetre
	public static final int TIMER_FEN_ALARMES_EN_COURS = 1000;
	public static final int TIMER_FEN_VOIES_ANALOGIC = 1000;
	public static final int TIMER_FEN_VOIES_DIGITAL = 1000;
	public static final int TIMER_FEN_PRINCIPALE = 1000;
	
	// Timer SGBD
	public static final int TIMER_MINUTE = 60000;
	public static final int TIMER_ENREGISTREMENT_TPS_REEL = 10000;
	public static final int TIMER_ENREGISTREMENT_HISTORIQUE = 60000; // 180000;
	public static final int TIMER_LOGIN = 300000;
	
	// Timer API
	public static int TIMER_LECTURE_TPS_REEL = 1000; //20000;
	public static int TIMER_LECTURE_TPS_REEL_ACCELERE = 3000;
	
}
