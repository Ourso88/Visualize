package AE_Communication;

public interface AE_TCP_Constantes {
	public static final int POSTE_MAITRE = 1;
	
	public static final boolean MODE_DEBUG_BASE = false;
	public static final boolean MODE_DEBUG_API = false;

	public static final int MODBUS_PORT = 502;
//	public static final String ADR_IP_API = new String("192.168.0.20");
//	public static final String ADR_IP_API = new String("10.27.0.10");
	public static final int NB_MOT_LECTURE_AI = 50;
	public static final int NB_MOT_LECTURE_DI = 40;
	public static final int NB_MOT_LECTURE_MAITRE_CLIENT = 50;
	  
	public static final int ADR_API_AI_TPS_REEL = 1500;
	public static final int ADR_API_DI_TPS_REEL = 1410;
	public static final int ADR_API_ECHANGE_MAITRE_CLIENT = 2050;
	
	
	public static final int TIMER_MINUTE = 60000;
	public static final int TIMER_LECTURE_TPS_REEL = 3000;
	public static final int TIMER_ENREGISTREMENT_TPS_REEL = 10000;
	public static final int TIMER_ENREGISTREMENT_HISTORIQUE = 180000;
	public static final int TIMER_LOGIN = 300000;
	
	public static final int MAX_AI = 400;
	public static final int MAX_DI = 640;
	public static final int MAX_ECHANGE_MAITRE_CLIENT = 50;
}
