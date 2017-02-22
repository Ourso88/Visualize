package em.general;

import em.sgbd.FonctionsSGBD;

public class AE_Variables {
	public static long idUtilisateur = -1;
	public static int niveauUtilisateur = 0;
	public static String nomUtilisateur = "";
	public static String prenomUtilisateur = "";
	public static boolean modeDebug = false;
	
	public static FonctionsSGBD ctnOracle = null;
	
	public static int AE_SGBD_TYPE = AE_Constantes.AE_SGBD_ORACLE;
	public static String AE_SGBD_SERVEUR = new String("@srvbesbdd02.bfc-citrix.local:1521");
	public static String AE_SGBD_BASE = new String("GTCBFC");
	public static String AE_SGBD_USER = new String("gtcbfc");
	public static String AE_SGBD_MDP = new String("gtcbfc25");	
}
