import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import AE_General.*;
import EFS_Structure.StructCapteur;
import EFS_Structure.StructPriseEnCompte;

public class FenHistoriqueAlarme extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// Commun
	private JPanel pnlTri = new JPanel();
	private JPanel pnlCorps = new JPanel();
	private JPanel pnlInformation = new JPanel();
	private JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTri, pnlCorps);
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();	
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Corps
    private ModeleHistoriqueAlarme mdlHistoriqueAlarme = new ModeleHistoriqueAlarme();	
    private JTable tbAlarme;
    private TableRowSorter<TableModel> sorter;		
	
    // Informations
    private JLabel lblInfoDate = new JLabel("Date : ");
    private JLabel lblInfoDateDeb = new JLabel("Date debut : ");
    private JLabel lblInfoDateFin = new JLabel("Date fin : ");
    private JLabel lblInfoUtilisateur = new JLabel("Nom utilisateur : ");
    
    
	// Tri
    private JLabel lblDateDebutTri = new JLabel("Date début : ");
    private JDateChooser jdcDateDebut = new JDateChooser();
    private JLabel lblDateFinTri = new JLabel("Date Fin : ");
    private JDateChooser jdcDateFin = new JDateChooser();
    private JButton btnFiltrer = new JButton("Filtrer");
    private JButton btnCourbe = new JButton("Courbe");
    private JButton btnImprimer = new JButton("Imprimer");
    private JButton btnValider = new JButton("Valider");
    private JButton btnAppelEcranValidee = new JButton("Ecran Alarmes validées");

	private JLabel lblAppelAlert = new JLabel("Appel Alert : ");
	private JPanel pnlAppelAlert = new JPanel();
	private ButtonGroup bgAppelAlert = new ButtonGroup();
	private JRadioButton optAppelAlertOui = new JRadioButton("Oui");
	private JRadioButton optAppelAlertTous = new JRadioButton("Tous");
    
    
	private String[] ptTous = {"Tous"}; 
    
	private JLabel lblVoieTri = new JLabel("Voie : ");
	private List<StructCapteur> tbCapteur = new ArrayList<StructCapteur>();	
	private JComboBox<String> cmbCapteur = new JComboBox<String>(ptTous);	
	
	private List<StructPriseEnCompte> tbPriseEnCompte = new ArrayList<StructPriseEnCompte>();	
	
	public FenHistoriqueAlarme() throws ParseException {
		super();
		build();
		remplirPriseEnCompte();
		lectureCapteur();
		remplirAlarme();
	}

	public void build() {
	    this.setTitle("Historique alarme");
	    this.setSize(1000, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Historique alarme");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlTri.setBackground(new Color(200, 130, 66));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	    //Place la barre de séparation a 100 px
		splCorps.setDividerLocation(100);
	    
		GridBagConstraints gbc = new GridBagConstraints();

	    // =================== Corps de l'écran =============================================
	    //On définit le layout manager
	    pnlCorps.setLayout(new BorderLayout());

        tbAlarme = new JTable(mdlHistoriqueAlarme);
        tbAlarme.setSize(800, 400);
	    tbAlarme.setFillsViewportHeight(true);        
	    tbAlarme.setBackground(AE_Constantes.EFS_BLEU);

	    sorter =  new TableRowSorter<TableModel>(tbAlarme.getModel());
        tbAlarme.setRowSorter(sorter);     
        sorter.setSortsOnUpdates(false);
	    
	    

//        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE).setCellRenderer(new ColorCellRenderer());
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION).setCellRenderer(new DateRenderer());
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION).setCellRenderer(new DateRenderer());
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE).setCellRenderer(new DateRenderer());

        tbAlarme.setSelectionBackground(AE_Constantes.EFS_MARRON);
        tbAlarme.getSelectionModel();
		tbAlarme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE).setPreferredWidth(40);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE).setMaxWidth(40);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE).setMaxWidth(120);        

        tbAlarme.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent ae) {
				int indRow = tbAlarme.rowAtPoint(ae.getPoint()); 
				int indCol = tbAlarme.columnAtPoint(ae.getPoint());
				switch(indCol) {
				case AE_Constantes.JTABLE_ALARME_HISTORIQUE_COMMENTAIRE_PRISE_EN_COMPTE:
					System.out.println("Changer le commentaire ... ");
					break;
				case AE_Constantes.JTABLE_ALARME_HISTORIQUE_RAISON_ACQUITTEMENT:
					// Si niveau utilisateur >= 30
					
					if(EFS_Client_Variable.niveauUtilisateur >= 30 ) {
						int idAlarme = mdlHistoriqueAlarme.getIdAlarmeHistorique(indRow);
						System.out.println("Changer la raison de l'alarme : " + idAlarme);
						// Demander la nouvelle raison
							
						String raison = "---";
						int idPriseEnCompte = 0;
						String strReponse = "";
						Object [] possibilites = new Object[tbPriseEnCompte.size()];
						for(int i = 0; i < tbPriseEnCompte.size(); i++) {
							possibilites[i] = tbPriseEnCompte.get(i).getNom();
						}
						strReponse = (String)JOptionPane.showInputDialog(null, "Choisir un motif de prise en compte : ", "Prise en compte", JOptionPane.QUESTION_MESSAGE, null, possibilites, null);
						if (strReponse != null) {
							for(int i = 0; i < tbPriseEnCompte.size(); i++) {
								if(strReponse.equals(tbPriseEnCompte.get(i).getNom())) {
									idPriseEnCompte = (int) tbPriseEnCompte.get(i).getId();
								}
							} // fin for
						}
						
						// idPriseEncompte == 1 (Autres)
						raison = "---";
						if(idPriseEnCompte == 1) {
							// Demande de commentaire
							raison = AE_Fonctions.saisieTexte("Veuillez entrer une raison : ", "Raison commentaire ...");
							System.out.println("Raison : " + raison);
							if (raison == null) raison = "";
						} else {
							raison = "---";
						}
						if(idPriseEnCompte > 0 ) {	
							String strSql = "UPDATE AlarmeHistorique SET idPriseEnCompte = " + idPriseEnCompte
									   + " , idUtilisateur = " + EFS_Client_Variable.idUtilisateur
									   + " , CommentairePriseEnCompte = '" + raison + "'"
									   + " WHERE idAlarmeHistorique = " + idAlarme;
		System.out.println("EM raison : " + strSql);
							ctn.open();
							ctn.fonctionSql(strSql);
							ctn.close();
							mdlHistoriqueAlarme.setMotifIdPriseEncompte(indRow, idPriseEnCompte);
							mdlHistoriqueAlarme.setRaison(indRow, strReponse);
							mdlHistoriqueAlarme.setCommentairePriseEnCompte(indRow, raison);
						}
					} // Fin if nivUtilisateur > 30
					
					break;
				default:
					Object contenuCellule = tbAlarme.getValueAt(indRow , indCol);
					System.out.println("EM tbAlarme.addMouseListener : " + contenuCellule);
					AE_Fonctions.afficheMessage(null, contenuCellule.toString());
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
        });
        
        
//        tbAlarme.setFont(AE_Constantes.FONT_ARIAL_20);
        
        pnlInformation.setBackground(AE_Constantes.EFS_MARRON);
        pnlInformation.add(lblInfoDate);
        pnlInformation.add(lblInfoDateDeb);
        pnlInformation.add(lblInfoDateFin);
        pnlInformation.add(lblInfoUtilisateur);
	    pnlCorps.add(pnlInformation, BorderLayout.NORTH);
        pnlCorps.add(new JScrollPane(tbAlarme), BorderLayout.CENTER);	    
	    
	    // ==================================================================================

	    // ================= TRI ===========================================================
	    pnlTri.setLayout(new GridBagLayout());
	    
        lblDateDebutTri.setMinimumSize(new Dimension(150, 25));
        lblDateDebutTri.setPreferredSize(new Dimension(150, 25));
        lblDateFinTri.setMinimumSize(new Dimension(150, 25));
        lblDateFinTri.setPreferredSize(new Dimension(150, 25));
        
	    Calendar cal = Calendar.getInstance();
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
//        Date now = new Date();
	    cal.setTime(dateDeb);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    dateDeb = cal.getTime();
        
        jdcDateDebut.setDate(dateDeb);
        jdcDateFin.setDate(dateFin);
        
	    bgAppelAlert.add(optAppelAlertOui);
	    bgAppelAlert.add(optAppelAlertTous);
	    pnlAppelAlert.add(optAppelAlertOui);
	    pnlAppelAlert.add(optAppelAlertTous);
	    pnlAppelAlert.setOpaque(false);
	    optAppelAlertOui.setOpaque(false);
	    optAppelAlertTous.setOpaque(false);
	    optAppelAlertOui.setSelected(true);
        
        btnFiltrer.addActionListener(this);
        
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
	    gbc.gridx = 2; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblVoieTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(cmbCapteur, gbc);
	    // --------------------------------------------------	   
	    gbc.gridx = 4; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblAppelAlert, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 4; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(pnlAppelAlert, gbc);
	    // --------------------------------------------------	   
	    gbc.gridx = 5; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnFiltrer, gbc);
	    // --------------------------------------------------	   
	    btnAppelEcranValidee.addActionListener(this);
	    gbc.gridx = 5; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnAppelEcranValidee, gbc);
	    // --------------------------------------------------	   
	    btnCourbe.addActionListener(this);
	    gbc.gridx = 6; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnCourbe, gbc);
	    // --------------------------------------------------	   
	    btnValider.addActionListener(this);
	    gbc.gridx = 7; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnValider, gbc);
	    // --------------------------------------------------	   
	    btnImprimer.addActionListener(this);
	    gbc.gridx = 7; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnImprimer, gbc);
	    gbc.insets = new Insets(0, 0, 0, 0);
	    // --------------------------------------------------	   
	    
	    // ==================================================================================
	    
	} // Fin build

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
	
	private void remplirAlarme() {
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
		String strDateDeb;
		String strDateFin;
		String strSql = "";
		int idService = -1;
		int idPriseEnCompte = 0;

		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 		
		
		SimpleDateFormat formaterDeb = null;
		formaterDeb = new SimpleDateFormat("dd/MM/yyyy 00:00:01");
		SimpleDateFormat formaterFin = null;
		formaterFin = new SimpleDateFormat("dd/MM/yyyy 23:59:59");
		
		lblInfoDate.setText("Date : " + formater.format(dateDeb) + "    ");

		dateDeb = jdcDateDebut.getDate();
		dateFin = jdcDateFin.getDate();
		strDateDeb = "TO_DATE('" + formaterDeb.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formaterFin.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";

		lblInfoDateDeb.setText("Date : " + formaterDeb.format(dateDeb) + "    ");
		lblInfoDateFin.setText("Date : " + formaterFin.format(dateFin) + "    ");
		
		lblInfoUtilisateur.setText("Nom : " + EFS_Client_Variable.nomUtilisateur + " " + EFS_Client_Variable.prenomUtilisateur);
		
		// Vider le tableau
		mdlHistoriqueAlarme.removeAllAlarme();
		
		ctn.open();
		// Création du filtre
		strSql = "SELECT AlarmeHistorique.*, Capteur.Description, Capteur.Nom, Capteur.idService, Utilisateur.Nom AS nomUtilisateur"
				   + " FROM ((AlarmeHistorique LEFT JOIN Capteur ON AlarmeHistorique.idCapteur = Capteur.idCapteur)"
				   + " LEFT JOIN Utilisateur ON AlarmeHistorique.idUtilisateur = Utilisateur.idUtilisateur)"
				   + " WHERE DateApparition >= " + strDateDeb + " AND DateApparition <= " + strDateFin; 
		
		if(cmbCapteur.getSelectedIndex() > 0) {
			strSql += " AND Capteur.idCapteur = " + tbCapteur.get(cmbCapteur.getSelectedIndex()-1).getId();
		}
		
		if(optAppelAlertOui.isSelected()) {
			strSql += " AND AppelAlert = 1";
		}
		
		strSql += " ORDER BY DateApparition DESC";

		ResultSet result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				idService = result.getInt("idService");
				if(testService(idService)) {
					if(result.getInt("idPriseEnCompte") > 0 ) {
						// Recherche de l'indice
						for (int i  = 0; i < tbPriseEnCompte.size(); i++) {
							if (tbPriseEnCompte.get(i).getId() == result.getInt("idPriseEnCompte")) {
								idPriseEnCompte = i;
							}
						}
					} else {
						idPriseEnCompte = 0;
					}
					mdlHistoriqueAlarme.addAlarme(new HistoriqueAlarme(result.getInt("idCapteur"), result.getString("Nom")
							, result.getString("Description"), result.getDate("DateApparition"),
							result.getDate("DateDisparition"), result.getDate("DatePriseEnCompte"), 
							result.getString("DescriptionAlarme"), tbPriseEnCompte.get(idPriseEnCompte).getNom(),
							result.getString("nomUtilisateur"), result.getString("commentairePriseEnCompte"),
							result.getInt("idAlarmeHistorique")));
				}
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
		ctn.close();
		
	} // Fin remplirAlarme

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

	private void gestionCourbe() {
    	int[] selection = tbAlarme.getSelectedRows();
    	int indexSelection = selection[0];
        int idCapteur = mdlHistoriqueAlarme.getIdCapteur(indexSelection); 
        
        if (idCapteur > 0) {
			FenCourbe fenCourbe = new FenCourbe();
			fenCourbe.setIdCapteur(idCapteur);
			fenCourbe.traceCourbe();
			fenCourbe.setVisible(true);
			fenCourbe.requestFocusInWindow();
        } // Fin if
        
	} // Fin gestionCourbe
	
	private void remplirPriseEnCompte() {
		ctn.open();
		ResultSet result = ctn.lectureData("SELECT * FROM PriseEnCompte");
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				tbPriseEnCompte.add(new StructPriseEnCompte(result.getLong("idPriseEnCompte"), result.getString("Nom"), result.getString("Description")));
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
		ctn.close();
	} // Fin LectureDonnees	
		
	private void gestionImpression() {
/*		
		// Enregistrer dans la base la validation
		String strSql = "";
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
		String strDateDeb;
		String strDateFin;
		
		SimpleDateFormat formaterDeb = null;
		formaterDeb = new SimpleDateFormat("dd/MM/yyyy 00:00:01");
		SimpleDateFormat formaterFin = null;
		formaterFin = new SimpleDateFormat("dd/MM/yyyy 23:59:59");

		dateDeb = jdcDateDebut.getDate();
		dateFin = jdcDateFin.getDate();
		strDateDeb = "TO_DATE('" + formaterDeb.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formaterFin.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";

		ctn.open();
		strSql = "INSERT INTO ValidationHistoriqueAlarme (DateValidation, DateDebut, DateFin, idUtilisateur) VALUES("
			   + "sysdate, " + strDateDeb + ", " + strDateFin + ", "	+ EFS_Client_Variable.idUtilisateur + ")";
		ctn.fonctionSql(strSql);
		ctn.close();
*/		
		
		
		EM_PrintPanel imp = new EM_PrintPanel(pnlCorps);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.repaint();

	
		imp.setOrientation(EM_PrintPanel.LANDSCAPE);
		imp.setFitIntoPage(true);
//		imp.setDocumentTitle("");
		imp.print();
		
	} // Fin gestionImpression
	
	
	private void gestionValidation() {
		String strSql = "";
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
		String strDateDeb;
		String strDateFin;
		
		SimpleDateFormat formaterDeb = null;
		formaterDeb = new SimpleDateFormat("dd/MM/yyyy 00:00:01");
		SimpleDateFormat formaterFin = null;
		formaterFin = new SimpleDateFormat("dd/MM/yyyy 23:59:59");

		dateDeb = jdcDateDebut.getDate();
		dateFin = jdcDateFin.getDate();
		strDateDeb = "TO_DATE('" + formaterDeb.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formaterFin.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";

		ctn.open();
		/*
		strSql = "INSERT INTO ValidationHistoriqueAlarme (DateValidation, DateDebut, DateFin, idUtilisateur) VALUES("
			   + "sysdate, " + strDateDeb + ", " + strDateFin + ", "	+ EFS_Client_Variable.idUtilisateur + ")";
		*/
		// ---- Modifications 11/12/2015 ---------
		strSql = "INSERT INTO ValidationHistoriqueAlarme (DateValidation, DateDebut, DateFin, idUtilisateur) VALUES("
				   + "sysdate, " + strDateDeb + ", sysdate, " + EFS_Client_Variable.idUtilisateur + ")";
		// ---- Fin modifications 11/12/2015 -----
		ctn.fonctionSql(strSql);
		ctn.close();	
	} // Fin gestionValidation
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnFiltrer) {
			remplirAlarme();
		}

		if (ae.getSource() == btnCourbe) {
			gestionCourbe();
		}

		if (ae.getSource() == btnImprimer) {
			gestionImpression();
		}

		if (ae.getSource() == btnValider) {
			gestionValidation();
		}
	
		if (ae.getSource() == btnAppelEcranValidee) {
			// Appel fenetre poste technique
			FenValidationHistoriqueAlarme fenetre;
			try {
				fenetre = new FenValidationHistoriqueAlarme();
				fenetre.setVisible(true);
				fenetre.requestFocusInWindow();		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}	
	}
	
	
} // Fin class
