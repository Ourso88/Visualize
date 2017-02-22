package em.fonctions;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import em.general.AE_Constantes;
import em.general.AE_Variables;
import em.sgbd.FonctionsSGBD;

/**
 * 
 * @author Eric Mariani
 * @since 25/11/2016
 * 		Fonctions générales
 *
 */
public class AE_Fonctions implements AE_Constantes {
	
	/**
	 * Test de la version avec celle enregistrée dans la base
	 * @param appel
	 * 		Fenetre appelante
	 */
	public static void testVersion(Component appel) {
		FonctionsSGBD ctn = new FonctionsSGBD(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);	
		String strSql = "";
		
		strSql = "SELECT * FROM Version"; 
		ResultSet result = ctn.lectureData(strSql);
		try {
			int versionBase = -1;
			result.last();
			versionBase = result.getInt("VersionAdministration");
			if (versionBase != VERSION) afficheMessage(appel, "Viper - Administration", "La version du programme n'est pas à jour ! Veuillez contacter le service Performance ...");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		
	}
	
	/**
	 * Calcul du numéro de version du programme 
	 * @return
	 * 		La version sous forme de texte
	 */
	public static String calculVersion() {
		String versionCalculee = new String("");
		int nbVersion = 0;
		int nbMiseAJour = 0;
		int nbIteration = 0;
		
		
		nbVersion = VERSION / 10000;
		nbMiseAJour = (VERSION - (nbVersion * 10000)) / 100;
		nbIteration = (VERSION - (nbVersion * 10000) - (nbMiseAJour * 100));

		versionCalculee = "Version : " + nbVersion + "." + nbMiseAJour + "." + nbIteration;
		return versionCalculee;
	}
	
	/**
	 * Affiche un message
	 * @param appel
	 * 		Fenêtre appelante
	 * @param msgMessage
	 * 		Message à afficher
	 */
	public static void afficheMessage(Component appel, String titreFenetre, String msgMessage) {
	       JOptionPane.showMessageDialog(appel, msgMessage, titreFenetre, JOptionPane.WARNING_MESSAGE);
			
	}
	
	/**
	 * Demande une entrée
	 * @since 30/11/2016
	 * @param appel
	 * @param titreFenetre
	 * @param msgAffiche
	 * @return
	 */
	public static String inputBox(Component appel, String titreFenetre, String msgAffiche) {
	       return (String) JOptionPane.showInputDialog(appel, msgAffiche, titreFenetre, JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * Calcul l'identificateur maximum d'une Table
	 * @param id
	 * 		Identificateur demandé
	 * @param table
	 * 		Table demandée
	 * @return
	 * 		Identificateur maximum + 1
	 * @throws SQLException
	 */
	public static long trouveIdMax(String id, String table) throws SQLException {
		long idMax = -1L;
		
		FonctionsSGBD ctn = new FonctionsSGBD(AE_Variables.AE_SGBD_TYPE, AE_Variables.AE_SGBD_SERVEUR, AE_Variables.AE_SGBD_BASE, AE_Variables.AE_SGBD_USER, AE_Variables.AE_SGBD_MDP);
		String strSql = "SELECT MAX(" + id + ") AS MaxId FROM " + table;
		ResultSet rs = ctn.lectureData(strSql);
		if(rs.next()) {
			idMax = rs.getLong("MaxId") + 1;
		} else {
			idMax = 1;
		}
		return idMax;
	}
	
	/**
	 * Trouve le type de fichier : .xxx
	 * @since 04/12/2016
	 * @param nomFichier
	 * 		Le nom du fichier
	 * @return
	 * 		Les caractères du type. Exemple txt, xlsx, com, bat
	 */
	public static String trouveTypeFichier(String nomFichier) {
		int pos = nomFichier.lastIndexOf(".");
		return nomFichier.substring(pos + 1);
	}
	
	/**
	 * 
	 * @param format
	 * 		Format voulu pour la date 
	 * @return
	 * 		Chaine contenant la date
	 */
	public static String formatDate(String format) {
		DateFormat dfm = new SimpleDateFormat(format);
		Calendar dateDepart = Calendar.getInstance();
		return (dfm.format(dateDepart.getTime()));
	}
	
	/**
	 * @param date
	 * 		Date sous format String 
	 * @param format
	 * 		Format voulu pour la date 
	 * @return
	 * 		Chaine contenant la date
	 */
	public static String formatDate(String date, String formatEntree, String formatSortie) {
		DateFormat dfmEntree = new SimpleDateFormat(formatEntree);
		DateFormat dfmSortie = new SimpleDateFormat(formatSortie);
		Date maDate;
		try {
			maDate = dfmEntree.parse(date);
			return dfmSortie.format(maDate);
		} catch (ParseException e) {
			return "--- pb ---";
		}
	}
	
	/**
	 * Calcul l'arrondi d'un double
	 * @param A
	 * 		Le nombre à arrondir
	 * @param B
	 * 		Le nombre de chiffres derrière la virgule
	 * @return
	 * 		Le nombre arrondi
	 */
	public static double arrondi(double A, int B) {
		return (double) ( (int) (A * Math.pow(10, B) + .5)) / Math.pow(10, B);
	}	

	/**
	 * Calcul l'arrondi supérieur d'un double
	 * @param A
	 * 		Le nombre à arrondir
	 * @param B
	 * 		Le nombre de chiffres derrière la virgule
	 * @return
	 * 		Le nombre arrondi
	 */
	public static double arrondiSup(double A, int B) {
		double arrondir = A * Math.pow(10, B);
		arrondir = Math.ceil(arrondir);
		arrondir = arrondir / Math.pow(10, B);
		return arrondir;
	}	
	
	/**
	 * Calcul l'arrondi supérieur d'un double
	 * @param A
	 * 		Le nombre à arrondir
	 * @param B
	 * 		Le nombre de chiffres derrière la virgule
	 * @return
	 * 		Le nombre arrondi
	 */
	public static double arrondiInf(double A, int B) {
		double arrondir = A * Math.pow(10, B);
		arrondir = Math.floor(arrondir);
		arrondir = arrondir / Math.pow(10, B);
		return arrondir;
	}	
	
	/**
	 * Test le niveau de l'utilisateur
	 * @param niveauDemande
	 * @return
	 */
	public static boolean testNiveau(int niveauDemande) {
		if(AE_Variables.idUtilisateur == -1) {
			afficheMessage(null, "Avertissement", "Veuillez vous connecter ...");
			return false;
		} else if(AE_Variables.niveauUtilisateur >= niveauDemande) {
			return true;
		} else {
			afficheMessage(null, "Avertissement", "Vous n'avez pas le niveau pour cette action ...");
			return false;
		}
	}
	
	/**
	 * Saisie d'un tecte libre
	 * @param titreQuestion
	 * @param titreFenetre
	 * @return
	 */
	public static String saisieTexte(String titreQuestion, String titreFenetre) {
		String reponse = new String("");
		
		JFrame frame = new JFrame("Saisie Texte");
	    reponse = JOptionPane.showInputDialog(frame, titreQuestion, titreFenetre, JOptionPane.QUESTION_MESSAGE + JOptionPane.OK_OPTION);		
		
		return reponse;
	}	
}
