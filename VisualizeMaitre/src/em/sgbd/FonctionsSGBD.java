package em.sgbd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;
import em.general.EFS_Maitre_Variable;


/**
 * 
 * @author Eric Mariani
 * @since 31/10/2016
 * Fonctions pour la connexion aux bases de donn�es MySql, Oracle, Access, SQL_Lite
 */
public class FonctionsSGBD {
	public Connection ctn;
	private int typeSgbd;

	private Statement maTransmission;
	private ResultSet monResultat = null;
	
	/**
	 * @author Eric Mariani
	 * Classe constructeur
	 * @param Type_Sgbd
	 * @param Server
	 * @param Base
	 * @param Utilisateur
	 * @param Pwd
	 */
	public FonctionsSGBD(int typeSgbd, String server, String base, String utilisateur, String pwd) {
		this.typeSgbd = typeSgbd;
		ctn = ConnexionSGBD.getInstance(typeSgbd, base, server, utilisateur, pwd);
	} // Fin AE_connectionBase

	/**
	 * @author Eric Mariani
	 * Classe constructeur
	 * @param Type_Sgbd
	 * @param Base
	 * @param Utilisateur
	 * @param Pwd
	 */
	public FonctionsSGBD(int typeSgbd, String base, String utilisateur, String pwd) {
		ctn = ConnexionSGBD.getInstance(typeSgbd, base, null, utilisateur, pwd);
	} // Fin AE_connectionBase
	
	
	/**
	 * @author Eric Mariani
	 * Fermeture de la connexion
	 */
	public void close() {
		try {
			ctn.close();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("Erreur close \n " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	} // Fin Close
	
	/**
	 * @author Eric Mariani
	 * @return
	 *     Retourne la connexion � la base
	 */
	public Connection getConnection() {
		return ctn;
	}
	
	/**
	 * @author Eric Mariani
	 * @param strReq
	 *    Requ�te SQL en texte
	 * @return
	 *    ResultSet avec les donn�es de la requ�te
	 */
	public ResultSet lectureData(String strReq) {
		testConnexionBase();
		try {
			if (typeSgbd != AE_Constantes.AE_SGBD_SQLITE) {
				maTransmission = ctn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}
			else {
				maTransmission = ctn.createStatement();
			}
			monResultat = maTransmission.executeQuery(strReq);
		}
			catch (Exception e) {
				GestionLogger.gestionLogger.warning("Erreur lectureData\n " + "Requete = " + strReq + "\n" + e.getMessage());
				EFS_Maitre_Variable.compteurErreurSGBD++;
		} // Fin try - catch
			
		return monResultat;
	} // Fil LectureData

	/**
	 * @author Eric Mariani
	 * 		Execute la requete SQL
	 * @param strReq
	 * 	  Requ�te SQL en texte
	 */
	public void fonctionSql(String strReq) {
		@SuppressWarnings("unused")
		int nb = 0;
		
		testConnexionBase();
		try {
			int cptErreur = 0;
			boolean transmis = false;
			
			do {
				try {
					maTransmission = ctn.createStatement();
					nb = maTransmission.executeUpdate(strReq);
					maTransmission.close();
					transmis = true;
				} catch (Exception e) {
					GestionLogger.gestionLogger.warning("Erreur fonctionSql \n " + "Requete = " + strReq + "\n" + e.getMessage());
					EFS_Maitre_Variable.compteurErreurSGBD++;
					cptErreur++;
				}
			} while (cptErreur < 5 && !transmis);
			
		}
		catch (Exception e) {
			GestionLogger.gestionLogger.severe("5 Erreurs fonctionSql \n " + "Requete = " + strReq + "\n" + e.getMessage());
			EFS_Maitre_Variable.compteurErreurSGBD++;
		} // Fin try - catch
	} // Fil LectureData	
	
	/**
	 * @author Eric Mariani
	 * 		Ferme la transmission et le r�sultat (m�moire) 
	 */
	public void closeLectureData() {
		try {
			maTransmission.close();
			monResultat.close();
		}
		catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur fonction closeLectureData \n  " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurSGBD++;
		} // Fin try - catch
	}

	/**
	 * Teste si la connexion � la base de donn�es n'est pas ferm�e
	 */
	private void testConnexionBase() {
		try {
			if(ctn.isClosed()) {
				GestionLogger.gestionLogger.warning("Erreur Connexion SGBD Ferm�e ...");
				ctn = ConnexionSGBD.getInstance(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);
				if(ctn.isClosed()) {
					GestionLogger.gestionLogger.warning("Erreur Connexion SGBD TOUJOURS Ferm�e ...");
				}
			}
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("Erreur Connexion SGBD Ferm�e impossible de rouvrir ... ");
			EFS_Maitre_Variable.compteurErreurSGBD++;
		}
	}
	

} // Fin Class
