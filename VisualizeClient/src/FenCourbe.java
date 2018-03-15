import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.toedter.calendar.JDateChooser;

import filters.HourFilter;
import AE_General.*;
import EFS_Structure.StructCapteur;


public class FenCourbe extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// Commun
	private JPanel pnlTri = new JPanel();
	private JPanel pnlCorps = new JPanel();
	private JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTri, pnlCorps);
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();	
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Corps (Courbe)
	private PnlDessineCourbe pnlDessineCourbe = new PnlDessineCourbe();
	private JLabel lblVoie = new JLabel("A000");
	private JLabel lblTitreMoyenne = new JLabel("Moyenne : ");
	private JLabel lblMoyenne = new JLabel("000");
	private JLabel lblTitreMinimum = new JLabel("Minimum : ");
	private JLabel lblMinimum = new JLabel("000");
	private JLabel lblTitreMaximum = new JLabel("Maximum : ");
	private JLabel lblMaximum = new JLabel("000");
	private JPanel pnlInfoCourbe = new JPanel();
	
	private JPanel pnlInfoArchive = new JPanel();
	private ButtonGroup bgArchive = new ButtonGroup();
	private JRadioButton optActuelle = new JRadioButton("Actuelle");
	private JRadioButton optArchive = new JRadioButton("Archive");

	
	private int idCapteur;
    
    private double tbX[] = new double[5000];
    private double tbY[] = new double[5000];
    private double maxX = 0;
    private double maxY = 0;
    private double minY = 0;
	
    private double seuilBas = 0D;
    private double seuilHaut = 0D;
    private double seuilCentral = 0D;
	
	// Tri
	private JLabel lblDateDebutTri = new JLabel("Date début : ");
	private JDateChooser jdcDateDebut = new JDateChooser();
	private JTextField txtHeureDebut;
	
	
	private JLabel lblDateFinTri = new JLabel("Date Fin : ");
	private JDateChooser jdcDateFin = new JDateChooser();
	private JTextField txtHeureFin;
	private JLabel lblCapteurTri = new JLabel("Capteur : ");
    private JButton btnTracer = new JButton("Tracer");
    private JButton btnImprimer = new JButton("Imprimer");

//    private String[] ptTous = {"Tous"}; 
    
	private List<StructCapteur> tbCapteur = new ArrayList<StructCapteur>();	
	private JComboBox<String> cmbCapteur = new JComboBox<String>();	
	
	public FenCourbe() {
		super();
		build();
//		pack();
		lectureCapteur();
	}
	
	protected JTextField getTxtHeureDebutFilterHour() {
		if (txtHeureDebut == null) {
			txtHeureDebut = new JTextField();
			txtHeureDebut.setDocument(new HourFilter());
	        txtHeureDebut.setMinimumSize(new Dimension(60, 20));
	        txtHeureDebut.setPreferredSize(new Dimension(60, 20));
	        txtHeureDebut.setText("00:00:00");
		}
		return txtHeureDebut;
	}
	protected JTextField getTxtHeureFinFilterHour() {
		if (txtHeureFin == null) {
			txtHeureFin = new JTextField();
			txtHeureFin.setDocument(new HourFilter());
	        txtHeureFin.setMinimumSize(new Dimension(60, 20));
	        txtHeureFin.setPreferredSize(new Dimension(60, 20));
	        txtHeureFin.setText("00:00:00");
		}
		return txtHeureFin;
	}
	
	public void build() {
	    // Modifier l'icône de JFrame
	    Toolkit kit = Toolkit.getDefaultToolkit();
	    Image img = kit.getImage("visualization.jpg");
	    setIconImage(img);	    

	    
	    this.setTitle("Historique courbe");
	    this.setSize(1000, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);

	    
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Historique courbe");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlTri.setBackground(new Color(200, 130, 66));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    
	    //Place la barre de séparation a 100 px
		splCorps.setDividerLocation(100);
		GridBagConstraints gbc = new GridBagConstraints();
	    

	    // =================== Corps de l'écran =============================================
	    //On définit le layout manager
//	    pnlCorps.setLayout(new GridBagLayout());
	    pnlCorps.setLayout(new BorderLayout());
	    pnlCorps.setBackground(Color.white);
	    
	    pnlCorps.add(pnlInfoCourbe, BorderLayout.NORTH);
	    pnlCorps.add(pnlDessineCourbe, BorderLayout.CENTER);

	    
	    // ================== Info Courbe ==================================================
	    pnlInfoCourbe.setLayout(new GridBagLayout());
	    pnlInfoCourbe.setBackground(AE_Constantes.EFS_BLEU);

	    lblVoie.setForeground(Color.RED);
//	    lblMoyenne.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    lblMoyenne.setBackground(AE_Constantes.EFS_MARRON);
	    
	    gbc.insets = new Insets(10, 5, 10, 0);
	    // ------- Voie -------------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 100;
	    gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlInfoCourbe.add(lblVoie, gbc);
	    // ------- Titre Moyenne ----------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 10;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInfoCourbe.add(lblTitreMoyenne, gbc);  gbc.anchor = GridBagConstraints.LINE_END;
	    // ------- Moyenne ----------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 10;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlInfoCourbe.add(lblMoyenne, gbc);
	    // ------- Titre Minimum ----------------------------------------------
	    gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 10;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInfoCourbe.add(lblTitreMinimum, gbc);  gbc.anchor = GridBagConstraints.LINE_END;
	    // ------- Minimum ----------------------------------------------------
	    gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 10;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlInfoCourbe.add(lblMinimum, gbc);
	    // ------- Titre Maximum ----------------------------------------------
	    gbc.gridx = 5; gbc.gridy = 0; gbc.weightx = 10;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInfoCourbe.add(lblTitreMaximum, gbc);  gbc.anchor = GridBagConstraints.LINE_END;
	    // ------- Maximum ----------------------------------------------------
	    gbc.gridx = 6; gbc.gridy = 0; gbc.weightx = 10;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlInfoCourbe.add(lblMaximum, gbc);
	    
	    
	    // ================= TRI ===========================================================
	    pnlTri.setLayout(new GridBagLayout());
	    
        lblDateDebutTri.setMinimumSize(new Dimension(150, 25));
        lblDateDebutTri.setPreferredSize(new Dimension(150, 25));
        lblDateFinTri.setMinimumSize(new Dimension(150, 25));
        lblDateFinTri.setPreferredSize(new Dimension(150, 25));

        btnTracer.setPreferredSize(btnImprimer.getPreferredSize());
        btnTracer.setMinimumSize(btnImprimer.getMinimumSize());

        
	    Calendar cal = Calendar.getInstance();
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
//        Date now = new Date();
	    cal.setTime(dateDeb);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    dateDeb = cal.getTime();
        
        jdcDateDebut.setDate(dateDeb);
        jdcDateFin.setDate(dateFin);
        
        btnTracer.addActionListener(this);
        btnImprimer.addActionListener(this);
        optActuelle.addActionListener(this);
        optArchive.addActionListener(this);
        optActuelle.setSelected(true);
        
        // ===== pnlInfoArchive =====
	    bgArchive.add(optActuelle);
	    bgArchive.add(optArchive);
	    pnlInfoArchive.setBackground(AE_Constantes.EFS_MARRON);
        pnlInfoArchive.add(optActuelle);
        pnlInfoArchive.add(optArchive);
	    optActuelle.setBackground(AE_Constantes.EFS_MARRON);
	    optArchive.setBackground(AE_Constantes.EFS_MARRON);
        
	    // --------------------------------------------------
	    gbc.insets = new Insets(0, 10, 0, 0);
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblDateDebutTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(jdcDateDebut, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlTri.add(getTxtHeureDebutFilterHour(), gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblDateFinTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(jdcDateFin, gbc);
	    // --------------------------------------------------	   
	    gbc.gridx = 1; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlTri.add(getTxtHeureFinFilterHour(), gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblCapteurTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(cmbCapteur, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(pnlInfoArchive, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 3; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnTracer, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 4; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnImprimer, gbc);
	    // --------------------------------------------------	   
	    // ==================================================================================
	}

	public void traceCourbe() {
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
		String strDateDeb;
		String strDateFin;
		String strSql = "";
		int cptPoint = 0;
		Date dateLue = new Date();
		int ecart = 0;
		long diffSeconde;
		boolean blCapteurAnalogique = true;
		double calcul = 0D;
		
		
		SimpleDateFormat formaterDebut = null;
		formaterDebut = new SimpleDateFormat("dd/MM/yyyy " + txtHeureDebut.getText());
		SimpleDateFormat formaterFin = null;
		formaterFin = new SimpleDateFormat("dd/MM/yyyy " + txtHeureFin.getText());
		
		dateDeb = jdcDateDebut.getDate();
		dateFin = jdcDateFin.getDate();

		strDateDeb = "TO_DATE('" + formaterDebut.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formaterFin.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";

		try {
			dateDeb = AE_Fonctions.stringToDate(formaterDebut.format(dateDeb), "dd/MM/yyyy HH:mm");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		try {
			dateFin = AE_Fonctions.stringToDate(formaterFin.format(dateFin), "dd/MM/yyyy HH:mm");
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		System.out.println("Date Deb parse : " + dateDeb);		
		System.out.println("Date Fin parse : " + dateFin);		
		
		pnlDessineCourbe.setDateDeb(dateDeb);
		pnlDessineCourbe.setDateFin(dateFin);
	    
	    // Lecture du capteur ...
		ResultSet result;
	    ctn.open();
	    idCapteur = (int) tbCapteur.get(cmbCapteur.getSelectedIndex()).getId();
		strSql = "SELECT Capteur.*, Equipement.Nom AS nomEquipement "
				+ "FROM (Capteur LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
				+ " WHERE idCapteur = " + idCapteur;					
//	    strSql = "SELECT * FROM Capteur WHERE idCapteur = " + idCapteur;  
		result = ctn.lectureData(strSql);
		try {
			if(result.next()) {
				if(result.getInt("TypeCapteur") == AE_Constantes.CAPTEUR_ANALOGIQUE_ENTREE) {
					blCapteurAnalogique = true;
				} else {
					blCapteurAnalogique = false;
				}
				lblVoie.setText(result.getString("Nom") + " - " + result.getString("nomEquipement") + " - " + result.getString("Description"));
			} // Fin if()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture table capteur");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Lecture de la voie Analogique ...
	    idCapteur = (int) tbCapteur.get(cmbCapteur.getSelectedIndex()).getId();
		if(blCapteurAnalogique) {
		    strSql = "SELECT * FROM AI_Historique WHERE (idCapteur = " + idCapteur 
		    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")"
		    		+ " ORDER BY DateLecture ASC";
		} else {
		    strSql = "SELECT * FROM DI_Historique WHERE (idCapteur = " + idCapteur 
		    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")"
		    		+ " ORDER BY DateLecture ASC";
		}
		result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				dateLue = result.getDate("DateLecture");
				diffSeconde = dateLue.getTime() - dateDeb.getTime();
				diffSeconde = diffSeconde / 1000;
		    	tbX[cptPoint] = diffSeconde;
				if(blCapteurAnalogique) {
					tbY[cptPoint] = result.getDouble("Valeur") / 10;
				} else {
					tbY[cptPoint] = result.getDouble("Valeur");
				}
		    	cptPoint++;
			} // Fin while result.next()
			if(blCapteurAnalogique && optArchive.isSelected()) {
			    strSql = "SELECT * FROM AI_Historique_Archive WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")"
			    		+ " ORDER BY DateLecture ASC";
				result = ctn.lectureData(strSql);
				while(result.next()) {
					dateLue = result.getDate("DateLecture");
					diffSeconde = dateLue.getTime() - dateDeb.getTime();
					diffSeconde = diffSeconde / 1000;
			    	tbX[cptPoint] = diffSeconde;
					if(blCapteurAnalogique) {
						tbY[cptPoint] = result.getDouble("Valeur") / 10;
					} else {
						tbY[cptPoint] = result.getDouble("Valeur");
					}
			    	cptPoint++;
				} // Fin while result.next()
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture de l'historique");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Calcul de la moyenne
		if(blCapteurAnalogique) {
			if(optArchive.isSelected()) {
				strSql = "SELECT AVG(Valeur) AS Moyenne FROM AI_Historique_Archive WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")";
			} else {
				strSql = "SELECT AVG(Valeur) AS Moyenne FROM AI_Historique WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")";
			}
			result = ctn.lectureData(strSql);
			try {
				if(result.next()) {
					calcul = result.getDouble("Moyenne") / 10;
					calcul = AE_Fonctions.arrondi(calcul, 1);
					lblMoyenne.setText(String.valueOf(calcul));
				}
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println("Probléme lecture de la moyenne");
			}

			if(optArchive.isSelected()) {
				strSql = "SELECT MIN(Valeur) AS Minimum FROM AI_Historique_Archive WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")";
			} else {
				strSql = "SELECT MIN(Valeur) AS Minimum FROM AI_Historique WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")";
			}
			result = ctn.lectureData(strSql);
			try {
				if(result.next()) {
					calcul = result.getDouble("Minimum") / 10;
					calcul = AE_Fonctions.arrondi(calcul, 1);
					lblMinimum.setText(String.valueOf(calcul));
				}
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println("Probléme lecture du minimum");
			}
			
			if(optArchive.isSelected()) {
				strSql = "SELECT MAX(Valeur) AS Maximum FROM AI_Historique_Archive WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")";
			} else {
				strSql = "SELECT MAX(Valeur) AS Maximum FROM AI_Historique WHERE (idCapteur = " + idCapteur 
			    		+ " AND DateLecture > " + strDateDeb + " AND DateLecture < " + strDateFin + ")";
			}
			result = ctn.lectureData(strSql);
			try {
				if(result.next()) {
					calcul = result.getDouble("Maximum") / 10;
					calcul = AE_Fonctions.arrondi(calcul, 1);
					lblMaximum.setText(String.valueOf(calcul));
				}
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println("Probléme lecture du maximum");
			}
			
			
			ctn.closeLectureData();
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		if(blCapteurAnalogique) {
			// Recherche des seuils
			
			result = ctn.lectureData("SELECT EntreeAnalogique.*, Capteur.Nom, Capteur.Description, Capteur.TypeCapteur"
					+ " FROM (EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur) "
					+ " WHERE EntreeAnalogique.idCapteur = " + idCapteur);

			try {
				if(result.next()) {
			    	seuilBas = (double) (result.getInt("SeuilBas") / 10D);
			    	seuilHaut = (double) (result.getInt("SeuilHaut") / 10D);
			    	seuilCentral = (double) (result.getInt("ValeurConsigne") / 10D);
				} // Fin while result.next()
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Probléme lecture des voies analogiques");
			}
			ctn.closeLectureData();
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // Fin Analogique

		
		
		ctn.close();

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
	    if (blCapteurAnalogique) {
		    pnlDessineCourbe.setSeuilHaut(seuilHaut);
		    pnlDessineCourbe.setSeuilBas(seuilBas);
		    pnlDessineCourbe.setSeuilCentral(seuilCentral);
		    pnlDessineCourbe.setBlCapteurAnalogique(true);
	    } else {
		    pnlDessineCourbe.setSeuilHaut(1);
		    pnlDessineCourbe.setSeuilBas(0);
		    pnlDessineCourbe.setSeuilCentral(0.5);
		    pnlDessineCourbe.setBlCapteurAnalogique(false);
	    }
	    	

	    ecart = Math.abs((int)((maxY - minY) * 0.2));
	    ecart = (int) Math.abs(Math.round((maxY - minY) * 0.2));
		System.out.println("ecart : " + ecart);
	    maxY = maxY + ecart;
	    minY = minY - ecart;
	    pnlDessineCourbe.setMaxX(maxX);
	    if (blCapteurAnalogique) {
		    pnlDessineCourbe.setMaxY(maxY);
		    pnlDessineCourbe.setMinY(minY);
	    } else {
		    pnlDessineCourbe.setMaxY(2);
		    pnlDessineCourbe.setMinY(-1);
	    }
	    	
	    pnlDessineCourbe.setTbX(tbX);
	    pnlDessineCourbe.setTbY(tbY);
		
	} // Fin traceCourbe()	

	private boolean testService(int idService) {
		// Filtre sur le service
		if(EFS_Client_Variable.niveauUtilisateur < 40) {
			for(int i = 0; i < 100; i++) {
				if((EFS_Client_Variable.tbService[i]  == idService) && idService > 0) {
					return true;
				}
			} // fin for
			return false;
		} else {
			return true;
		}
	}
	
	private void lectureCapteur() {
		int index = 0;
		int idService = -1;
		// Connection base
		ctn.open();
		
		ResultSet result = ctn.lectureData("SELECT * FROM Capteur ORDER BY TypeCapteur, VoieApi ASC");
		index = 0;
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				idService = result.getInt("idService");
				if(testService(idService)) {
					tbCapteur.add(new StructCapteur(result.getLong("idCapteur"), result.getString("Nom"), result.getString("Description"), 
							result.getInt("idEquipement"), result.getInt("idPosteTechnique"), result.getInt("idTypeMateriel"),
							 result.getInt("idZoneSubstitution"),  result.getInt("TypeCapteur"),  result.getInt("idService"),
							 result.getInt("Alarme"),  result.getInt("VoieApi"),  result.getInt("Inhibition")));
					
					cmbCapteur.addItem(String.format("%-8s", tbCapteur.get(index).getNom()) + tbCapteur.get(index).getDescription());
					index++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.close();
		
	} // Fin LectureDonnees

	public void setIdCapteur(int idCapteur) {
		this.idCapteur = idCapteur;
		// Trouver dans la combo
		for(int i = 0; i < cmbCapteur.getItemCount(); i++) {
			if (tbCapteur.get(i).getId() == idCapteur) {
				cmbCapteur.setSelectedIndex(i);
			} // if
		} // for i
	} 	
	
	private void gestionImpression() {
		EM_PrintPanel imp = new EM_PrintPanel(pnlCorps);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.repaint();

	
		imp.setOrientation(EM_PrintPanel.LANDSCAPE);
		imp.setFitIntoPage(true);
//		imp.setDocumentTitle("");
		imp.print();
		
	} // Fin gestionImpression
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnTracer) {
			traceCourbe();
			this.repaint();
		}
		if (ae.getSource() == btnImprimer) {
			gestionImpression();
		}
	}


} // Fin class
