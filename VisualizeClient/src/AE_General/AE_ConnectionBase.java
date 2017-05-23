package AE_General;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AE_ConnectionBase {
	private Connection m_ctn; 
	private int m_Type_Sgbd = 0;
	private String m_Base = "";
	private String m_Server = "";
	private String m_Utilisateur = "";
	private String m_Pwd = "";

	private Statement maTransmission;
	private ResultSet monResultat = null;
	
	
	public AE_ConnectionBase(int Type_Sgbd, String Server, String Base, String Utilisateur, String Pwd) {
		m_Base = Base;
		m_Server = Server;
		m_Type_Sgbd = Type_Sgbd;
		m_Utilisateur = Utilisateur;
		m_Pwd = Pwd;
	} // Fin AE_connectionBase

	public AE_ConnectionBase(int Type_Sgbd, String Base, String Utilisateur, String Pwd) {
		m_Base = Base;
		m_Type_Sgbd = Type_Sgbd;
		m_Utilisateur = Utilisateur;
		m_Pwd = Pwd;
	} // Fin AE_connectionBase
	
	
	public void open() {
		switch (m_Type_Sgbd) {
			case AE_Constantes.AE_SGBD_ORACLE : {
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					m_ctn = DriverManager.getConnection("jdbc:oracle:thin:" + m_Server + ":" + m_Base, m_Utilisateur, m_Pwd);
//					System.out.println("Connecté Oracle");
				} 
				catch (Exception e) {
					System.out.println("Erreur Connexion : ");
					System.out.println(e.getMessage());
					AE_Fonctions.ecrireMessageLog("Erreur connexion base Oracle");
				} // Fin try - catch
				break;
			}			
			case AE_Constantes.AE_SGBD_MYSQL : {
				try {
					System.out.println("Test Class.forName");
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("Test Connexion");
					m_ctn = DriverManager.getConnection("jdbc:mysql://" + m_Server + "/" + m_Base, m_Utilisateur, m_Pwd);
					System.out.println("Connecté MySql");
				} 
				catch (Exception e) {
					System.out.println("Erreur Connexion : ");
					System.out.println(e.getMessage());
				} // Fin try - catch
				break;
			}
			case AE_Constantes.AE_SGBD_ACCESS : {
				try {
					Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					m_ctn = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + m_Base);
					System.out.println("Connecté Access");
					} 
				catch (Exception e) {
					System.out.println(e.getMessage());
				} // Fin try - catch
				break;
			}
			case AE_Constantes.AE_SGBD_SQLITE : {
				try {
					Class.forName("org.sqlite.JDBC");
					m_ctn = DriverManager.getConnection("jdbc:sqlite:" + m_Base);	
					System.out.println("Connecté sqLite");
					} 
				catch (Exception e) {
					System.out.println(e.getMessage());
				} // Fin try - catch
				break;
			}		} // Fin Switch
	} // Fin Open
	
	public void close() {
		try {
			m_ctn.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	} // Fin Close
	
	public Connection getConnection() {
		return m_ctn;
	}
	
	public ResultSet lectureData(String strReq) {
		try {
			if (m_Type_Sgbd != AE_Constantes.AE_SGBD_SQLITE) {
				maTransmission = m_ctn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}
			else {
				maTransmission = m_ctn.createStatement();
			}
			monResultat = maTransmission.executeQuery(strReq);
		} catch (Exception e) {
			System.out.println("Message Erreur LectureData");
			System.out.println(e.getMessage());
			AE_Fonctions.ecrireMessageLog("Erreur LectureData SGBD");
			System.exit(0);
		} // Fin try - catch
		return monResultat;
	} // Fil LectureData

	public void fonctionSql(String strReq) {
		@SuppressWarnings("unused")
		int Nb = 0;
		
		try {
			maTransmission = m_ctn.createStatement();
			Nb = maTransmission.executeUpdate(strReq);
			maTransmission.close();
//			System.out.println("Nb : " + Nb);
		}
		catch (Exception e) {
			System.out.println("Message Erreur FonctionSql Master");
			System.out.println(e.getMessage());
			AE_Fonctions.ecrireMessageLog("Erreur fonctionSql SGBD");
			System.exit(0);
		} // Fin try - catch
	} // Fil LectureData	
	
	public void closeLectureData() {
		try {
			maTransmission.close();
			monResultat.close();
		}
		catch (Exception e) {
			System.out.println("Message Erreur closeLectureData");
			System.out.println(e.getMessage());
		} // Fin try - catch
	}
} // Fin Class
