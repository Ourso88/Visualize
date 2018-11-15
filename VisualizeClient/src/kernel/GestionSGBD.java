package kernel;

import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;

/**
 * Gestion des échanges avec la SGBD
 * @author Eric Mariani
 * @since 15/11/2018
 *
 */
public class GestionSGBD implements AE_Constantes {
	
	/**
	 * 
	 * @param idCpateur
	 * @param tpsRappel
	 */
	public static void gererRappelAlert(int idCapteur, int tpsRappel ) {
		AE_ConnectionBase ctn = new AE_ConnectionBase(EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		
		ctn.open();
		String strSql = "UPDATE V2_AlarmeEnCours SET RappelAlert = " + tpsRappel + " WHERE idCapteur = " + idCapteur;
		ctn.fonctionSql(strSql);
		ctn.close();
	}
	
}
