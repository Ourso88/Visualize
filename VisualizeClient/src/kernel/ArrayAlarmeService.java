package kernel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;
import em.fonctions.GestionLogger;

/**
 * Cree des tableaux AlarmeService
 * @author Eric Mariani
 * @since 03/05/2018
 *
 */
public class ArrayAlarmeService extends ArrayList<AlarmeService> {
	private static final long serialVersionUID = 1L;
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	

	/**
	 * Constructeur
	 */
	public ArrayAlarmeService() {
		super();
		ctn.open();
		remplirArray();
		ctn.close();
	}
	
	/**
	 * Remplit la liste avec tous les éléments
	 */
	private void remplirArray() {
		try {
			this.clear();
			String strSql = "SELECT * FROM AlarmeService";
					
			ResultSet result = ctn.lectureData(strSql);
			while(result.next()) {
				this.add(new AlarmeService(result.getLong("idAlarmeService"), result.getString("NomService"), result.getInt("IndexMotApi"), result.getBoolean("Klaxon")));
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table Inhibition : " + e.getMessage());
		}
	}

	/**
	 * Recherche d'un élément
	 * @param index
	 */
	public int trouverIndex(long id) {
		for(int i = 0; i < this.size() ; i++) {
			if (this.get(i).getIdAlarmeService() == id) {
				return i;
			}
		}
		return -1;
	} 	
	
	
}
