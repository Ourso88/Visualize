package em.sgbd;

import java.sql.Connection;
import java.sql.DriverManager;

import em.fonctions.GestionLogger;
import em.general.AE_Constantes;

/**
 * 
 * @author Eric Mariani
 * @since 25/11/2016
 * 		Connexion aux bases de données
 * 		Avec instance unique pour chaque base 
 *
 */
public class ConnexionSGBD {
	private static Connection ctnOracle; 
	private static Connection ctnMySql; 
	private static Connection ctnAccess; 
	private static Connection ctnSqlLite;
	
	private static Connection ctnBase;
	
	/**
	 * @author Eric Mariani
	 * Connexion à la base de données
	 */
	public static Connection getInstance(int typeSgbd, String base, String server, String utilisateur, String pwd) {
		switch (typeSgbd) {
			case AE_Constantes.AE_SGBD_ORACLE : {
				if(ctnOracle == null) {
					try {
						Class.forName("oracle.jdbc.driver.OracleDriver");
						ctnOracle = DriverManager.getConnection("jdbc:oracle:thin:" + server + ":" + base, utilisateur, pwd);
						GestionLogger.gestionLogger.info("Connexion à la base Oracle");
					} catch (Exception e) {
						GestionLogger.gestionLogger.warning("Erreur connexion à la base Oracle\n  " + e.getMessage());
					} // Fin try - catch
				} // fin if
				ctnBase = ctnOracle;
				break;
			} // Fin case Oracle	
			case AE_Constantes.AE_SGBD_MYSQL : {
				if(ctnMySql == null) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						ctnMySql = DriverManager.getConnection("jdbc:mysql://" + server + "/" + base, utilisateur, pwd);
						GestionLogger.gestionLogger.info("Connexion à la base MySQL");
					} catch (Exception e) {
						GestionLogger.gestionLogger.warning("Erreur connexion à la base MySql\n  " + e.getMessage());
					} // Fin try - catch
				} // fin if
				ctnBase = ctnMySql;
				break;
			} // Fin case MySql
			case AE_Constantes.AE_SGBD_ACCESS : {
				if(ctnAccess == null) {
					try {
						Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
						ctnAccess = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + base);
						GestionLogger.gestionLogger.info("Connexion à la base ACCESS");
					} catch (Exception e) {
						GestionLogger.gestionLogger.warning("Erreur connexion à la base Access\n  " + e.getMessage());
					} // Fin try - catch
				} // fin if
				ctnBase = ctnAccess;
				break;
			} // fin case Access
			case AE_Constantes.AE_SGBD_SQLITE : {
				if(ctnSqlLite == null) {
					try {
						Class.forName("org.sqlite.JDBC");
						ctnSqlLite = DriverManager.getConnection("jdbc:sqlite:" + base);	
						GestionLogger.gestionLogger.info("Connexion à la base SqlLite");
					} catch (Exception e) {
						GestionLogger.gestionLogger.warning("Erreur connexion à la base SqlLite\n  " + e.getMessage());
					} // Fin try - catch
				} // fin if
				ctnBase = ctnSqlLite;
				break;
			}
		} // Fin Switch

		return ctnBase;
	} // Fin getInstance	
	

}
