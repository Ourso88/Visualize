package gui.vues;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import em.fonctions.AE_Fonctions;
import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;
import em.general.EFS_General;
import em.general.JTableConstantes;
import gui.modeles.ModeleJTableAlarmesEnCours;
import gui.modeles.ModeleJTableAlarmesHistorique;
import gui.modeles.ModeleJTableAlarmesSeuil;
import gui.modeles.ModeleJTableCapteurMaintenance;
import gui.renderers.JTableAlarmesEnCoursColorCellRenderer;
import kernel.AlarmeHistorique;
import kernel.CapteurMaintenance;
import kernel.GestionAPI;
import kernel.GestionSGBD;
import kernel.VoiesAPI;

/**
 * Fenetre principale du programme MAITRE
 * @author Eric Mariani
 * @since 20/02/2017
 */
public class FenPrincipale extends JFrame  implements AE_Constantes, VoiesAPI, EFS_General, JTableConstantes,  ActionListener {
	private static final long serialVersionUID = 1L;
	// GUI
	private AE_BarreHaut pnlEntete = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();

	private JPanel pnlEnCours = new JPanel();
	private JPanel pnlSeuil = new JPanel();
	private JSplitPane splAlarme = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlEnCours, pnlSeuil);
	
	private JPanel pnlEnMaintenance = new JPanel();
	private JPanel pnlHistorique = new JPanel();
	private JSplitPane splInformation = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlEnMaintenance, pnlHistorique);
	
	private JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splAlarme, splInformation);
	
	
	// ==> pnlEnCours <== 
	private JPanel pnlTitreEnCours = new JPanel();
    private ModeleJTableAlarmesEnCours mdlAlarmesEnCours = new ModeleJTableAlarmesEnCours();	
    private JTable jtbAlarmesEnCours;
    TableRowSorter<TableModel> sorterJtbAlarmesEnCours;	
    JScrollPane jspAlarmeEnCours;	
	private JPanel pnlBoutons = new JPanel();
	// ==> pnlSeuil <== 
    private ModeleJTableAlarmesSeuil mdlAlarmesSeuil = new ModeleJTableAlarmesSeuil();	
    private JTable jtbAlarmesSeuil;
    TableRowSorter<TableModel> sorterJtbAlarmesSeuil;	
    JScrollPane jspAlarmeSeuil;	
	// ==> pnlEnMaintenance <== 
    private ModeleJTableCapteurMaintenance mdlCapteurMaintenance = new ModeleJTableCapteurMaintenance();	
    private JTable jtbCapteurMaintenance;
    TableRowSorter<TableModel> sorterJtbCapteurMaintenance;	
    JScrollPane jspCapteurMaintenance;	
	private JPanel pnlBoutonsMaintenance = new JPanel();
	// ==> pnlHistorique <== 
    private ModeleJTableAlarmesHistorique mdlAlarmesHistorique = new ModeleJTableAlarmesHistorique();	
    private JTable jtbAlarmesHistorique;
    TableRowSorter<TableModel> sorterJtbAlarmesHistorique;	
    JScrollPane jspAlarmeHistorique;	

    
    
	// ==> pnlBoutons <==
    private JButton btnVoiesAnalogique = new JButton("Voies analogiques");
    private JButton btnVoiesDigitale = new JButton("Voies digitales");
    private JButton btnPriseEnCompte = new JButton("Prise en compte");
    private JButton btnInformation = new JButton("Information");
    private JButton btnCourbe = new JButton("Courbe");

    private JButton btnRetirerMaintenance = new JButton("Retirer de maintenance");

	ImageIcon iconLogin = new ImageIcon(getClass().getResource("/login.png"));
    private JButton btnLogin = new JButton("Connexion", iconLogin);

    
    // Timer
    private Timer tmrRefresh = new Timer(TIMER_FEN_PRINCIPALE, this);	
	
	
    /**
     * Constucteur
     */
    public FenPrincipale() {
    	super();
    	build();
    	remplirHistoriqueAlarme();
    	remplirEnMaintenance();
    	tmrRefresh.start();
    }	
	
	/**
	 * Description de la fen�tre
	*/
	private void build() {
		this.setTitle("Visualize MAITRE - Gestion alarmes");
	    this.setSize(1200, 800);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    pnlEntete.setTitreEcran("Gestion Alarmes");
	    
	    // Actions
	    this.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	if(AE_Fonctions.testNiveau(40)) {
        			GestionLogger.gestionLogger.info("Fin de l'application");
	        		System.exit(0);
	        	}
	        }        
	      }
	    );
	    btnVoiesAnalogique.addActionListener(this);
	    btnVoiesDigitale.addActionListener(this);
	    btnPriseEnCompte.addActionListener(this);
	    btnCourbe.addActionListener(this);
	    btnInformation.addActionListener(this);
	    btnRetirerMaintenance.addActionListener(this);
	    btnLogin.addActionListener(this);
	    
	    // Taille
	    btnVoiesDigitale.setPreferredSize(btnVoiesAnalogique.getPreferredSize());
	    btnVoiesDigitale.setMinimumSize(btnVoiesAnalogique.getMinimumSize());
	    btnPriseEnCompte.setPreferredSize(btnVoiesAnalogique.getPreferredSize());
	    btnPriseEnCompte.setMinimumSize(btnVoiesAnalogique.getMinimumSize());
	    btnCourbe.setPreferredSize(btnVoiesAnalogique.getPreferredSize());
	    btnCourbe.setMinimumSize(btnVoiesAnalogique.getMinimumSize());
	    btnInformation.setPreferredSize(btnVoiesAnalogique.getPreferredSize());
	    btnInformation.setMinimumSize(btnVoiesAnalogique.getMinimumSize());
	    
	    splAlarme.setDividerLocation(300);
	    splCorps.setDividerLocation(500);
	    splInformation.setDividerLocation(400);
	    
	    // Definition des parties de la fenetre
	    this.add("North",pnlEntete);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);
	    
	    // Layout
        pnlEnCours.setLayout(new BorderLayout());
        pnlSeuil.setLayout(new BorderLayout());
        pnlEnMaintenance.setLayout(new BorderLayout());
        pnlHistorique.setLayout(new BorderLayout());
	    
        // Couleurs
	    pnlEnCours.setBackground(AE_BLEU);
	    pnlSeuil.setBackground(AE_BLEU);
	    pnlEnMaintenance.setBackground(AE_BLEU);
	    pnlHistorique.setBackground(AE_BLEU);
	    pnlBoutons.setBackground(AE_BLEU);
	    pnlBoutonsMaintenance.setBackground(AE_BLEU);
	    pnlTitreEnCours.setBackground(AE_MARRON);
	    btnLogin.setForeground(AE_ROUGE);
	    
	    // Tableau jtbAlarmesEnCours
	    jtbAlarmesEnCours = new JTable(mdlAlarmesEnCours);
	    jtbAlarmesEnCours.setFillsViewportHeight(true);
	    jtbAlarmesEnCours.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    jtbAlarmesEnCours.setBackground(AE_BLEU);
	    
	    sorterJtbAlarmesEnCours =  new TableRowSorter<TableModel>(jtbAlarmesEnCours.getModel());
        jtbAlarmesEnCours.setRowSorter(sorterJtbAlarmesEnCours);        
        sorterJtbAlarmesEnCours.setSortable(0,  false);
        sorterJtbAlarmesEnCours.setSortsOnUpdates(true);
        
        jtbAlarmesEnCours.getColumnModel().getColumn(JT_ALARME_EN_COURS_NOM).setCellRenderer(new JTableAlarmesEnCoursColorCellRenderer());
        
        jspAlarmeEnCours = new JScrollPane(jtbAlarmesEnCours);
        jspAlarmeEnCours.setBackground(AE_BLEU);
        jspAlarmeEnCours.setOpaque(true);

	    // Tableau jtbAlarmesSeuil
	    jtbAlarmesSeuil = new JTable(mdlAlarmesSeuil);
	    jtbAlarmesSeuil.setFillsViewportHeight(true);
	    jtbAlarmesSeuil.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    jtbAlarmesSeuil.setBackground(AE_BLEU);
	    
	    sorterJtbAlarmesSeuil =  new TableRowSorter<TableModel>(jtbAlarmesSeuil.getModel());
        jtbAlarmesSeuil.setRowSorter(sorterJtbAlarmesSeuil);        
        sorterJtbAlarmesSeuil.setSortable(0,  false);
        sorterJtbAlarmesSeuil.setSortsOnUpdates(true);
        
        jspAlarmeSeuil = new JScrollPane(jtbAlarmesSeuil);
        jspAlarmeSeuil.setBackground(AE_BLEU);
        jspAlarmeSeuil.setOpaque(true);

	    // Tableau jtbCapteurMaintenance
	    jtbCapteurMaintenance = new JTable(mdlCapteurMaintenance);
	    jtbCapteurMaintenance.setFillsViewportHeight(true);
	    jtbCapteurMaintenance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    jtbCapteurMaintenance.setBackground(AE_BLEU);
	    
	    sorterJtbCapteurMaintenance =  new TableRowSorter<TableModel>(jtbCapteurMaintenance.getModel());
        jtbCapteurMaintenance.setRowSorter(sorterJtbCapteurMaintenance);        
        sorterJtbCapteurMaintenance.setSortable(0,  false);
        sorterJtbCapteurMaintenance.setSortsOnUpdates(true);
        
        jspCapteurMaintenance = new JScrollPane(jtbCapteurMaintenance);
        jspCapteurMaintenance.setBackground(AE_BLEU);
        jspCapteurMaintenance.setOpaque(true);        
        
	    // Tableau jtbAlarmesHistorique
	    jtbAlarmesHistorique = new JTable(mdlAlarmesHistorique);
	    jtbAlarmesHistorique.setFillsViewportHeight(true);
	    jtbAlarmesHistorique.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    jtbAlarmesHistorique.setBackground(AE_BLEU);
	    
	    sorterJtbAlarmesHistorique =  new TableRowSorter<TableModel>(jtbAlarmesHistorique.getModel());
        jtbAlarmesHistorique.setRowSorter(sorterJtbAlarmesHistorique);        
        sorterJtbAlarmesHistorique.setSortable(0,  false);
        sorterJtbAlarmesHistorique.setSortsOnUpdates(true);
        
        jspAlarmeHistorique = new JScrollPane(jtbAlarmesHistorique);
        jspAlarmeHistorique.setBackground(AE_BLEU);
        jspAlarmeHistorique.setOpaque(true);
        
	    //pnlEnCours
	    JLabel lblTitreEnCours = new JLabel("GESTION ALARME EN COURS");
	    lblTitreEnCours.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreEnCours.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreEnCours.setBackground(AE_MARRON);
	    lblTitreEnCours.setOpaque(true);
	    
		GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0, 5, 0, 10); // (top, left, bottom, right)
	    pnlTitreEnCours.setLayout(new GridBagLayout());
	    // --- btnLogin ---------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlTitreEnCours.add(btnLogin, gbc);
	    // --- lblTitreEncours --------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 90;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTitreEnCours.add(lblTitreEnCours, gbc);
	    
	    pnlEnCours.add(pnlTitreEnCours, BorderLayout.NORTH);	    
        pnlEnCours.add(jspAlarmeEnCours, BorderLayout.CENTER);	    
        pnlEnCours.add(pnlBoutons, BorderLayout.SOUTH);	    
	    
	    //pnlSeuil
	    JLabel lblTitreSeuil = new JLabel("GESTION ALARME SEUIL");
	    lblTitreSeuil.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreSeuil.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreSeuil.setBackground(AE_MARRON);
	    lblTitreSeuil.setOpaque(true);
        pnlSeuil.add(lblTitreSeuil, BorderLayout.NORTH);	    
        pnlSeuil.add(jspAlarmeSeuil, BorderLayout.CENTER);	    

	    //pnlEnMaintenance
	    JLabel lblTitreEnMaintenance = new JLabel("EN MAINTENANCE");
	    lblTitreEnMaintenance.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreEnMaintenance.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreEnMaintenance.setBackground(AE_MARRON);
	    lblTitreEnMaintenance.setOpaque(true);
        pnlEnMaintenance.add(lblTitreEnMaintenance, BorderLayout.NORTH);	    
        pnlEnMaintenance.add(jspCapteurMaintenance, BorderLayout.CENTER);	    
        pnlEnMaintenance.add(pnlBoutonsMaintenance, BorderLayout.SOUTH);	    

        //pnlBoutons Maintenance
        pnlBoutonsMaintenance.setLayout(new FlowLayout());
        pnlBoutonsMaintenance.add(btnRetirerMaintenance);
	    
	    //pnlHistorique
	    JLabel lblTitreHistorique = new JLabel("GESTION ALARME HISTORIQUE");
	    lblTitreHistorique.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreHistorique.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreHistorique.setBackground(AE_MARRON);
	    lblTitreHistorique.setOpaque(true);
        pnlHistorique.add(lblTitreHistorique, BorderLayout.NORTH);	    
        pnlHistorique.add(jspAlarmeHistorique, BorderLayout.CENTER);	    
	    
	    //pnlBoutons
        pnlBoutons.setLayout(new FlowLayout());
        pnlBoutons.add(btnVoiesAnalogique);
        pnlBoutons.add(btnVoiesDigitale);
        pnlBoutons.add(btnPriseEnCompte);
        pnlBoutons.add(btnInformation);
        pnlBoutons.add(btnCourbe);
	}
  
	/**
	 * Gestion des alarmes en cours
	 */
	private void gererAlarmes() {
		boolean trouve = false;

		// Verifier si pas de disparu
		if(tbAlarme.size() < mdlAlarmesEnCours.getRowCount()) {
			try {
				for(int i = 0; i < mdlAlarmesEnCours.getRowCount(); i++) {
					trouve = false;
					for(int j = 0; j < tbAlarme.size(); j++) {
						if(tbAlarme.get(j).getIdCapteur() == mdlAlarmesEnCours.getIdCapteur(i)) {
							trouve = true;
						}
					}
					if(!trouve) {
						// A supprimer
						try {
							mdlAlarmesEnCours.removeAlarmeEnCours(i);
							remplirHistoriqueAlarme();
						} catch (Exception ev) {
							GestionLogger.gestionLogger.warning("Probleme suppression ..." + ev.getMessage());
						}
					}
				}
			} catch (Exception ev) {
				GestionLogger.gestionLogger.warning("Probleme verification" + ev.getMessage());
			}
		}
		
		// V�rifier si pas de nouveau ...
		for(int i = 0; i < tbAlarme.size(); i++) {
			// Regarder si d�j� dans la table
			trouve = false;
			for(int j = 0; j < mdlAlarmesEnCours.getRowCount(); j++) {
				if(tbAlarme.get(i).getIdCapteur() == mdlAlarmesEnCours.getIdCapteur(j)) {
					trouve = true;
				}
			}
			if(!trouve) {
				mdlAlarmesEnCours.addAlarmeEnCours(tbAlarme.get(i));
			}
		} // fin for(i)

		int selection = -1;
        if (jtbAlarmesEnCours.getRowCount() > 0) {
        	if (jtbAlarmesEnCours.getSelectedRowCount() > 0) {
        		selection = jtbAlarmesEnCours.getSelectedRow();
        	}
        }
		mdlAlarmesEnCours.fireTableDataChanged();
        if (jtbAlarmesEnCours.getRowCount() > 0) {
        	if(selection != -1) {
				jtbAlarmesEnCours.setRowSelectionInterval(selection, selection);
        	}
        }
	} // fin class	
	
	/**
	 * Gestion des pr� seuils
	 */
	private void gererPreSeuils() {
		boolean trouve = false;

		// Verifier si pas de disparu
		if(tbAlarmeSeuil.size() < mdlAlarmesSeuil.getRowCount()) {
			try {
				for(int i = 0; i < mdlAlarmesSeuil.getRowCount(); i++) {
					trouve = false;
					for(int j = 0; j < tbAlarmeSeuil.size(); j++) {
						if(tbAlarmeSeuil.get(j).getIdCapteur() == mdlAlarmesSeuil.getIdCapteur(i)) {
							trouve = true;
						}
					}
					if(!trouve) {
						// A supprimer
						try {
							mdlAlarmesSeuil.removeAlarmeSeuil(i);
						} catch (Exception ev) {
							GestionLogger.gestionLogger.warning("Probleme suppression ..." + ev.getMessage());
						}
					}
				}
			} catch (Exception ev) {
				GestionLogger.gestionLogger.warning("Probleme verification" + ev.getMessage());
			}
		}
		
		// V�rifier si pas de nouveau ...
		for(int i = 0; i < tbAlarmeSeuil.size(); i++) {
			// Regarder si d�j� dans la table
			trouve = false;
			for(int j = 0; j < mdlAlarmesSeuil.getRowCount(); j++) {
				if(tbAlarmeSeuil.get(i).getIdCapteur() == mdlAlarmesSeuil.getIdCapteur(j)) {
					trouve = true;
				}
			}
			if(!trouve) {
				mdlAlarmesSeuil.addAlarmeSeuil(tbAlarmeSeuil.get(i));
			}
		} // fin for(i)
	}
	
	/**
	 * Gere la prise en compte d'une alarme
	 */
	private void gererPriseEnCompte() {
        if (jtbAlarmesEnCours.getRowCount() > 0) {
        	if (jtbAlarmesEnCours.getSelectedRowCount() > 0) {
        		int indexSelection = jtbAlarmesEnCours.getSelectedRow();
		        
		        tbAlarme.get(indexSelection).setDatePriseEnCompte(LocalDateTime.now());
		        tbAlarme.get(indexSelection).setPrisEnCompte(true);
				mdlAlarmesEnCours.fireTableDataChanged();
				// Couper Klaxon
				GestionAPI.gestionKlaxon(false);
				// Couper Appel Alert
				GestionSGBD.gestionAlert(false);
				
				long idPriseEnCompte = 1; // Autre
				String raison = "En attente";
				// Demande du motif de prise en compte
				if(tbAlarme.get(indexSelection).getTypeAlarme() == ALARME_ALERT) {
					Object  [] possibilites = GestionSGBD.renvoieRaisonPriseEnCompte(); 
					String strReponse = (String)JOptionPane.showInputDialog(this, "Choisir un motif de prise en compte : ", "Prise en compte", JOptionPane.QUESTION_MESSAGE, null, possibilites, null);
					if(strReponse != null) {
						for(int i = 0; i < tbPriseEnCompte.size(); i++) {
							if(strReponse.equals(tbPriseEnCompte.get(i).getNom())) {
								idPriseEnCompte = tbPriseEnCompte.get(i).getIdPriseEncompte();
							}
						} // fin for
					}
					// Si Prise en compte est : Autres (1)
					if (idPriseEnCompte == 1) {
						do {
							raison = AE_Fonctions.saisieTexte("Veuillez entrer une raison : ", "Raison commentaire ...");
							if (raison == null) raison = "";
						} while(raison == "" || raison.isEmpty());
					} else {
						raison = "---";
					}
				}
        	}
        }
	}	
	
	/**
	 * Remplit le tableau Alarme Historique
	 */
	private void remplirHistoriqueAlarme() {
		try {
			mdlAlarmesHistorique.removeAll();
			String strSql = "SELECT * FROM (V2_AlarmeHistorique LEFT JOIN Capteur ON V2_AlarmeHistorique.idCapteur = Capteur.idCapteur) ORDER BY DateDisparition DESC";
					
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			while(result.next()) {
				mdlAlarmesHistorique.addAlarmeHistorique(new AlarmeHistorique(
				result.getLong("idAlarmeHistorique"),
				result.getLong("idCapteur"),
				result.getInt("TypeCapteur"),
				result.getString("DateApparition"),
				result.getString("DatePriseEnCompte"),
				result.getString("DateDisparition"),
				result.getString("Nom"),
				result.getString("Description")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table Historique : " + e.getMessage());
		}
	}

	/**
	 * Remplit le tableau En Maintenance
	 */
	private void remplirEnMaintenance() {
		try {
			mdlCapteurMaintenance.removeAll();
			String strSql = "SELECT Inhibition.idInhibition, Inhibition.DateInhibition, Capteur.idCapteur, Capteur.TypeCapteur, Capteur.Nom AS NomCapteur, Utilisateur.Nom, Utilisateur.Prenom, RaisonInhibition.Raison"
					+ " FROM (((Inhibition LEFT JOIN Capteur ON Inhibition.idCapteur = Capteur.idCapteur)"
					+ " LEFT JOIN Utilisateur ON Inhibition.idUtilisateur = Utilisateur.idUtilisateur)"
					+ " LEFT JOIN RaisonInhibition ON Inhibition.idRaisonInhibition = RaisonInhibition.idRaisonInhibition)";
					
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			while(result.next()) {
				mdlCapteurMaintenance.addCapteurMaintenance(new CapteurMaintenance(
				result.getLong("idInhibition"),
				result.getLong("idCapteur"),
				result.getInt("TypeCapteur"),
				result.getString("NomCapteur"),
				result.getString("DateInhibition"),
				result.getString("Nom"),
				result.getString("Raison")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table Historique : " + e.getMessage());
		}
	}

	/**
	 * Gestion des Capteur en maintenance : retrait.
	 */
	private void gererRetirerMaintenance() {
		int selection = -1;
        if (jtbCapteurMaintenance.getRowCount() > 0) {
        	if (jtbCapteurMaintenance.getSelectedRowCount() > 0) {
        		selection = jtbCapteurMaintenance.getSelectedRow();
        		// Retirer de la table Inhibition
        		long idCapteur = mdlCapteurMaintenance.getIdCapteur(selection);
        		GestionSGBD.enleverDeMaintenance(idCapteur);
        		int typeCapteur = mdlCapteurMaintenance.getTypeCapteur(selection);
        		boolean trouve = false;
        		if(typeCapteur == CAPTEUR_ANALOGIQUE_ENTREE) {
        			for(int i = 0; i < tbAnaAPI.size(); i++) {
        				if(tbAnaAPI.get(i).getIdCapteur() == idCapteur) {
        					tbAnaAPI.get(i).setInhibition(CAPTEUR_EN_SERVICE);
        					trouve = true;
        				}
        			}
        		} else if(typeCapteur == CAPTEUR_DIGITAL_ENTREE) {
        			for(int i = 0; i < tbDigiAPI.size(); i++) {
        				if(tbDigiAPI.get(i).getIdCapteur() == idCapteur) {
        					tbDigiAPI.get(i).setInhibition(CAPTEUR_EN_SERVICE);
        					trouve = true;
        				}
        			}
        		}
        		if(trouve) {
        			GestionLogger.gestionLogger.info("<== MAINTENANCE ==> Retrait du capteur : " + idCapteur);
        			remplirEnMaintenance();
        		} else {
        			GestionLogger.gestionLogger.warning("<== MAINTENANCE ==> probl�me lors de la suppression : " + idCapteur);
        		}
        	}
        }
	}
	
	/**
	 * Gestion des actions
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnVoiesAnalogique) {
			FenVoiesAnalogicAPI fenetre = new FenVoiesAnalogicAPI();
			fenetre.setVisible(true);
		}
		if (ae.getSource() == btnVoiesDigitale) {
			FenVoiesDigitalAPI fenetre = new FenVoiesDigitalAPI();
			fenetre.setVisible(true);
		}
		if (ae.getSource() == tmrRefresh) {
			try {
				tmrRefresh.stop();
				gererAlarmes();
				gererPreSeuils();
				mdlAlarmesSeuil.fireTableDataChanged();
				tmrRefresh.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER ...");
			} // Fin try		
		
		}
		if (ae.getSource() == btnPriseEnCompte) {
			if(AE_Fonctions.testNiveau(40)) {
				gererPriseEnCompte();
			}
		}
		if (ae.getSource() == btnRetirerMaintenance) {
			gererRetirerMaintenance();
		}
		
		if (ae.getSource() == btnInformation) {
			GestionSGBD.gestionAlert(false);
			GestionAPI.gestionKlaxon(false);
		}

		if (ae.getSource() == btnLogin) {
			if(AE_Variables.idUtilisateur != -1) {
				AE_Variables.idUtilisateur = -1;
				AE_Variables.nomUtilisateur = "";
				AE_Variables.prenomUtilisateur = "";
				AE_Variables.niveauUtilisateur = 0;
				
			} else {
				FenLogin fenetre = new FenLogin();
				fenetre.setVisible(true);
			}
		}

		
	}		
	
}
