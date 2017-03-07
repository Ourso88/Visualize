package gui.vues;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;
import kernel.VoiesAPI;

/**
 * Trace la courbe d'une voie analogique
 * @author Eric Mariani
 * @since 07/03/2017
 *
 */
public class FenCourbe extends JFrame implements AE_Constantes, VoiesAPI, ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel pnlInfo = new JPanel();
	private PnlDessineCourbe pnlDessineCourbe = new PnlDessineCourbe();

	private JComboBox<String> cmbCapteur = new JComboBox<String>();	
    private JButton btnTracer = new JButton("Tracer");
    
    private double tbX[] = new double[1000];
    private double tbY[] = new double[1000];
    private double maxX = 0;
    private double maxY = 0;
    private double minY = 0;
	
    private double seuilBas = 0D;
    private double seuilHaut = 0D;
    private double seuilCentral = 0.5D;
    
    private int indexCapteur;
    
    /**
     * Constructeur
     * @param indexCapteur
     */
	public FenCourbe(int indexCapteur) {
		this.indexCapteur = indexCapteur;
		build();
		lectureCapteur();
		if(indexCapteur != -1) {
			tracerCourbe(indexCapteur);
			this.repaint();
		}
	} // Fin FenCourbe
	
	/**
	 * Affichage fenètre
	 */
	private void build() {
	    this.setTitle("Historique courbe");
	    this.setSize(1000, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    btnTracer.addActionListener(this);
	    
	    pnlInfo.add(cmbCapteur);
	    pnlInfo.add(btnTracer);
	    pnlInfo.setBackground(AE_Constantes.AE_BLEU);
	    this.add("North", pnlInfo);
	    this.add("Center", pnlDessineCourbe);
	    
	} // Fin build

	/**
	 * Trace la courbe
	 * @param indexCapteur
	 */
	private void tracerCourbe(int indexCapteur) {
		String strSql = "";
		String dateApi;
		int cptPoint = 0;
		Date dateDeb = new Date();
		Date dateLue = new Date();
	    Calendar cal = Calendar.getInstance();
		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 		
		int ecart = 0;
		long diffSeconde;

	    
	    cal.setTime(dateDeb);
		pnlDessineCourbe.setDateFin(dateDeb);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    dateDeb = cal.getTime();
		pnlDessineCourbe.setDateDeb(dateDeb);
	    
	    // Lecture de la voie ...
		ResultSet result;
	    long idCapteur = tbAnaAPI.get(indexCapteur).getIdCapteur();
	    
		dateApi = "TO_DATE('" + formater.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
	    
	    strSql = "SELECT * FROM AI_Historique WHERE (idCapteur = " + idCapteur + " AND DateLecture > " + dateApi + ") ORDER BY DateLecture ASC";
		result = AE_Variables.ctnOracle.lectureData(strSql);
		try {
			while(result.next()) {
				dateLue = result.getDate("DateLecture");
				diffSeconde = dateLue.getTime() - dateDeb.getTime();
				diffSeconde = diffSeconde / 1000;
		    	tbX[cptPoint] = diffSeconde;
		    	tbY[cptPoint] = result.getDouble("Valeur") / 10;
		    	cptPoint++;
			} // Fin while result.next()
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table AI_Historique : " + e.getMessage());
		}
		AE_Variables.ctnOracle.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur fermeture result.close() : " + e.getMessage());
		}

		seuilBas = (int) tbAnaAPI.get(indexCapteur).getSeuilBas() / 10;
    	seuilHaut = (int) tbAnaAPI.get(indexCapteur).getSeuilHaut() / 10;
		seuilCentral = (int) tbAnaAPI.get(indexCapteur).getValeurConsigne() / 10;

	    maxX = tbX[0]; maxY = tbY[0]; minY = tbY[0];
	    for(int i = 0; i < cptPoint; i++) {
	    	if (maxX < tbX[i]) maxX = tbX[i];
	    	if (maxY < tbY[i]) maxY = tbY[i];
	    	if (minY > tbY[i]) minY = tbY[i];
	    }
	    
	    pnlDessineCourbe.setMaxPoint(cptPoint);
	    
	    // Seuil
	    if (maxY < seuilHaut) maxY = seuilHaut;
	    if (minY > seuilBas) minY = seuilBas;
	    pnlDessineCourbe.setSeuilHaut(seuilHaut);
	    pnlDessineCourbe.setSeuilBas(seuilBas);
	    pnlDessineCourbe.setSeuilCentral(seuilCentral);

	    ecart = Math.abs((int)((maxY - minY) * 0.2));
	    ecart = (int) Math.abs(Math.round((maxY - minY) * 0.2));
	    maxY = maxY + ecart;
	    minY = minY - ecart;
	    pnlDessineCourbe.setMaxX(maxX);
	    pnlDessineCourbe.setMaxY(maxY);
	    pnlDessineCourbe.setMinY(minY);
	    
	    pnlDessineCourbe.setTbX(tbX);
	    pnlDessineCourbe.setTbY(tbY);
		
	} // Fin traceCourbe()

	/**
	 * Remplit la liste des voies analogiques dans la comboBox
	 */
	private void lectureCapteur() {
		
		for(int i = 0; i < tbAnaAPI.size(); i++) {
			cmbCapteur.addItem(String.format("%-8s", tbAnaAPI.get(i).getNom()) + tbAnaAPI.get(i).getDescription());
			if(i == this.indexCapteur) {
				cmbCapteur.setSelectedIndex(i);
			}
		}
	} 

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnTracer) {
			tracerCourbe(cmbCapteur.getSelectedIndex());
			this.repaint();
		}
		
	} 
	
	
} // Fin class
