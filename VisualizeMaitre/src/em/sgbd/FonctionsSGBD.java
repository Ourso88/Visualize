package em.sgbd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import em.fonctions.GestionLogger;
import em.general.AE_Constantes;


/**
 * 
 * @author Eric Mariani
 * @since 31/10/2016
 * Fonctions pour la connexion aux bases de données MySql, Oracle, Access, SQL_Lite
 */
public class FonctionsSGBD {
	private static Connection ctn;
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
		}
	} // Fin Close
	
	/**
	 * @author Eric Mariani
	 * @return
	 *     Retourne la connexion à la base
	 */
	public Connection getConnection() {
		return ctn;
	}
	
	/**
	 * @author Eric Mariani
	 * @param strReq
	 *    Requéte SQL en texte
	 * @return
	 *    ResultSet avec les données de la requéte
	 */
	public ResultSet lectureData(String strReq) {
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
		} // Fin try - catch
		return monResultat;
	} // Fil LectureData

	/**
	 * @author Eric Mariani
	 * 		Execute la requete SQL
	 * @param strReq
	 * 	  Requéte SQL en texte
	 */
	public void fonctionSql(String strReq) {
		@SuppressWarnings("unused")
		int nb = 0;
		
		try {
			maTransmission = ctn.createStatement();
			nb = maTransmission.executeUpdate(strReq);
			maTransmission.close();
		}
		catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur fonctionSql \n " + "Requete = " + strReq + "\n" + e.getMessage());
			System.exit(0);
		} // Fin try - catch
	} // Fil LectureData	
	
	/**
	 * @author Eric Mariani
	 * 		Ferme la transmission et le résultat (mémoire) 
	 */
	public void closeLectureData() {
		try {
			maTransmission.close();
			monResultat.close();
		}
		catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur fonction closeLectureData \n  " + e.getMessage());
		} // Fin try - catch
	}
} // Fin Class
