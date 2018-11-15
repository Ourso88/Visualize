import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

import AE_Communication.AE_TCP_Constantes;
import AE_General.*;
import EFS_Structure.StructPriseEnCompte;

public class FenPrincipale  extends JFrame implements ActionListener, AE_General.AE_Constantes {
	private static final long serialVersionUID = 1L;

	// Commun
	private JPanel pnlAlarme = new JPanel();
	private JPanel pnlJournal = new JPanel();
	private JPanel pnlAlarmeCommande = new JPanel();
	private JPanel pnlInhibition = new JPanel();
	private JPanel pnlInhibitionCommande = new JPanel();
	private JPanel pnlAlarmeHistorique = new JPanel();
	
	private JSplitPane splHistoriqueInhibition = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlAlarmeHistorique, pnlInhibition);
	private JSplitPane splCentre = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlAlarme, pnlJournal);
	
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Journal
    private ModeleHistoriqueAlarme mdlHistoriqueAlarme = new ModeleHistoriqueAlarme();	
    private JTable tbAlarmeHistorique;
    private TableRowSorter<TableModel> sorterAlarmeHistorique;		
	
	private MaskFormatter maskSeuil;

	private JPanel pnlInformationAlarme = new JPanel();
	private JLabel lblInfoTitreVoie = new JLabel("Voie : ");
	private JLabel lblInfoVoie = new JLabel(" --- ");
	private JLabel lblInfoTitreValeur = new JLabel("Valeur : ");
	private JLabel lblInfoValeur = new JLabel(" 000 ");
	private JLabel lblInfoTitreCalibration = new JLabel("Calibration : ");
	private JLabel lblInfoCalibration = new JLabel(" 000 ");
	private JLabel lblInfoTitreSeuilHaut = new JLabel("Seuil haut : ");
//	private JLabel lblInfoSeuilHaut = new JLabel(" 000 ");
	private JFormattedTextField txtInfoSeuilHaut = new JFormattedTextField(maskSeuil);
	private JLabel lblInfoTitreSeuilBas = new JLabel("Seuil bas : ");
//	private JLabel lblInfoSeuilBas = new JLabel(" 000 ");
	private JFormattedTextField txtInfoSeuilBas = new JFormattedTextField(maskSeuil);
	private JLabel lblInfoTitreTempo = new JLabel("Tempo : ");
//	private JLabel lblInfoTempo = new JLabel(" 000 ");
	private JFormattedTextField txtInfoSeuilTempo = new JFormattedTextField(maskSeuil);
	private JLabel lblInfoTitreZoneSubstitution = new JLabel("Zone de substitution : ");
	private JLabel lblInfoZoneSubstitution = new JLabel(" --- ");
	
	private int idCapteurInformation = -1;
	private JButton btnModifSeuilBas = new JButton("Modif. seuil bas");
	private JButton btnModifSeuilHaut = new JButton("Modif. seuil haut");
	private JButton btnModifSeuilTempo = new JButton("Modif. tempo");
	
	// Inhibition
    private JButton btnInhibitionSupprimer = new JButton("Retirer de maintenance");
    private ModeleInhibition mdlInhibition = new ModeleInhibition();	
    private JTable tbInhibition;
    private TableRowSorter<TableModel> sorterInhibition;		
	
	// Alarmes
    private ModeleTpsReelAlarme mdlTpsReelAlarme = new ModeleTpsReelAlarme();	
    private JTable tbAlarme;
    private TableRowSorter<TableModel> sorter;		
    private JButton btnPriseEnCompte = new JButton("Prise en compte");
    private JButton btnInformationAlarme = new JButton("Information");
    private JButton btnAcquittement = new JButton("Acquittement");
    private JButton btnCourbe = new JButton("Courbe");
	private List<StructPriseEnCompte> tbPriseEnCompte = new ArrayList<StructPriseEnCompte>();	
	
	// Specifique
	JMenuItem mnuQuitter = new JMenuItem("Quitter");	
	JMenuItem mnuFenPosteTechnique = new JMenuItem("Poste Technique");	
	JMenuItem mnuFenService = new JMenuItem("Service");	
	JMenuItem mnuFenTypeMateriel = new JMenuItem("Type matériel");	
	JMenuItem mnuFenZoneSubstitution = new JMenuItem("Zone substitution");	
	JMenuItem mnuFenEquipement = new JMenuItem("Equipement");	
	JMenuItem mnuFenEntreeAnalogique = new JMenuItem("Entrée analogique");	
	JMenuItem mnuFenEntreeDigitale = new JMenuItem("Entrée digitale");	
	JMenuItem mnuFenSortieAnalogique = new JMenuItem("Sortie analogique");	
	JMenuItem mnuFenSortieDigitale = new JMenuItem("Sortie digitale");	
	JMenuItem mnuFenUtilisateur = new JMenuItem("Utilisateur");	
	JMenuItem mnuFenPriseEnCompte = new JMenuItem("Prise en compte - Raisons");	
	JMenuItem mnuFenHistoriqueAlarme = new JMenuItem("Historique");	
	JMenuItem mnuFenValidationHistoriqueAlarme = new JMenuItem("Historique Alarmes Validées");	
	JMenuItem mnuFenCourbe = new JMenuItem("Courbe");	
	JMenuItem mnuFenHistoriqueCalibration = new JMenuItem("Historique calibration");	
	JMenuItem mnuFenComparateur = new JMenuItem("Comparateur");	
	JMenuItem mnuFenJournal = new JMenuItem("Journal");	
	JMenuItem mnuFenAPropos = new JMenuItem("A propos");	
	JMenuItem mnuFenChoixCapteurAnalogique = new JMenuItem("Choix capteur analogique");	
	
	JMenuItem mnuLogin = new JMenuItem("Login");	
	
	// Alarmes
    
	// Timer
    private Timer tmrTpsReel = new Timer(AE_TCP_Constantes.TIMER_LECTURE_TPS_REEL, this);	
    private Timer tmrRaffraichissementMinute = new Timer(TIMER_RAFFRAICHISSEMENT_MINUTE, this);	
    
	public FenPrincipale()  throws ParseException {
		super();
		ctn.open();
		build(); // Construction de la fenêtre
		remplirPriseEnCompte();
		remplirAlarmeHistorique();
		remplirInhibition();
		EFS_Client_Variable.initialiseTableaux();
		EFS_Client_Variable.lectureVoie();
		gestionAlarmeTpsReel();
		tmrTpsReel.start();
		tmrRaffraichissementMinute.start();
		AE_Fonctions.testVersion(this);
	}	
	
	private void build() {
	    this.setTitle("GTC Visualize - Menu principal");
	    this.setSize(1200, 800);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    
	    
	    this.add("North",pnlHaut);
	    this.add("Center",splCentre);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("GTC Visualize - Menu principal");
	    pnlAlarme.setBackground(EFS_BLEU);
	    pnlJournal.setBackground(EFS_BLEU);
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));	
	    
	    // ============== Journal =============================================
	    pnlJournal.setLayout(new BorderLayout());
	    pnlAlarmeHistorique.setLayout(new BorderLayout());
	    pnlInhibition.setLayout(new BorderLayout());
	    pnlInformationAlarme.setLayout(new GridBagLayout());
	    pnlInformationAlarme.setMinimumSize(new Dimension(800, 100));	    
	    pnlInformationAlarme.setPreferredSize(new Dimension(800, 100));
	    pnlInformationAlarme.setBackground(EFS_BLEU);

	    pnlAlarmeHistorique.setMinimumSize(new Dimension(800, 200));	    
	    pnlAlarmeHistorique.setPreferredSize(new Dimension(800, 200));
	    pnlAlarmeHistorique.setBackground(EFS_BLEU);	    
	    
		try {
			maskSeuil = new MaskFormatter("######");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		maskSeuil.setValidCharacters("0123456789.");

		txtInfoSeuilBas.setMinimumSize(new Dimension(50, 25));		
		txtInfoSeuilBas.setPreferredSize(new Dimension(50, 25));		
		txtInfoSeuilHaut.setPreferredSize(new Dimension(50, 25));		
		txtInfoSeuilHaut.setMinimumSize(new Dimension(50, 25));		
		txtInfoSeuilTempo.setPreferredSize(new Dimension(50, 25));		
		txtInfoSeuilTempo.setMinimumSize(new Dimension(50, 25));		
		
	    btnModifSeuilBas.addActionListener(this);
	    btnModifSeuilHaut.addActionListener(this);
	    btnModifSeuilTempo.addActionListener(this);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 3, 0, 3);
	    
	    gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreVoie, gbc);
	    
	    gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(lblInfoVoie, gbc);

	    gbc.gridx = 0; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreZoneSubstitution, gbc);
	    gbc.gridx = 1; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(lblInfoZoneSubstitution, gbc);
	    gbc.gridx = 2; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreSeuilBas, gbc);
	    gbc.gridx = 3; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(txtInfoSeuilBas, gbc);
	    gbc.gridx = 4; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreSeuilHaut, gbc);
	    gbc.gridx = 5; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(txtInfoSeuilHaut, gbc);
	    gbc.gridx = 6; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreCalibration, gbc);
	    gbc.gridx = 7; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(lblInfoCalibration, gbc);
	    gbc.gridx = 8; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreTempo, gbc);
	    gbc.gridx = 9; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(txtInfoSeuilTempo, gbc);
	    gbc.gridx = 10; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END; gbc.weightx = 20.;
	    pnlInformationAlarme.add(lblInfoTitreValeur, gbc);
	    gbc.gridx = 11; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(lblInfoValeur, gbc);
	    
	    gbc.gridx = 2; gbc.gridy = 2; gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(btnModifSeuilBas, gbc);
	    gbc.gridx = 4; gbc.gridy = 2; gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(btnModifSeuilHaut, gbc);
	    gbc.gridx = 8; gbc.gridy = 2; gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.fill = GridBagConstraints.BOTH; gbc.anchor= GridBagConstraints.LINE_START; gbc.weightx = 50.;
	    pnlInformationAlarme.add(btnModifSeuilTempo, gbc);
	    

        tbAlarmeHistorique = new JTable(mdlHistoriqueAlarme);
        tbAlarmeHistorique.setSize(800, 400);
	    tbAlarmeHistorique.setFillsViewportHeight(true);        
	    tbAlarmeHistorique.setBackground(AE_Constantes.EFS_BLEU);
        sorterAlarmeHistorique =  new TableRowSorter<TableModel>(tbAlarmeHistorique.getModel());
        tbAlarmeHistorique.setRowSorter(sorterAlarmeHistorique);     
        sorterAlarmeHistorique.setSortsOnUpdates(false);

        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION).setCellRenderer(new DateRenderer());
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION).setCellRenderer(new DateRenderer());
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE).setCellRenderer(new DateRenderer());

        tbAlarmeHistorique.setSelectionBackground(AE_Constantes.EFS_MARRON);
        tbAlarmeHistorique.getSelectionModel();
		tbAlarmeHistorique.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE).setPreferredWidth(40);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE).setMaxWidth(40);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION).setPreferredWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION).setMaxWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION).setPreferredWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION).setMaxWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE).setPreferredWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE).setMaxWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_UTILISATEUR).setPreferredWidth(120);        
        tbAlarmeHistorique.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_UTILISATEUR).setMaxWidth(120);        

	    JLabel lblTitreAlarmeHistorique = new JLabel("GESTION ALARME HISTORIQUE");
	    lblTitreAlarmeHistorique.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreAlarmeHistorique.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreAlarmeHistorique.setBackground(EFS_MARRON);
	    lblTitreAlarmeHistorique.setOpaque(true);
        
        pnlAlarmeHistorique.add(lblTitreAlarmeHistorique, BorderLayout.NORTH);	    
        pnlAlarmeHistorique.add(new JScrollPane(tbAlarmeHistorique), BorderLayout.CENTER);	    
        
        
        

        tbInhibition = new JTable(mdlInhibition);
        tbInhibition.setSize(800, 400);
	    tbInhibition.setFillsViewportHeight(true);        
	    tbInhibition.setBackground(AE_Constantes.EFS_BLEU);
        sorterInhibition =  new TableRowSorter<TableModel>(tbInhibition.getModel());
        tbInhibition.setRowSorter(sorterInhibition);     
        sorterInhibition.setSortsOnUpdates(false);
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_DATE_INHIBITION).setCellRenderer(new DateRenderer());
        tbInhibition.setSelectionBackground(AE_Constantes.EFS_MARRON);
        tbInhibition.getSelectionModel();
		tbInhibition.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_VOIE).setPreferredWidth(40);        
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_VOIE).setMaxWidth(40);        
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_DATE_INHIBITION).setPreferredWidth(150);        
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_DATE_INHIBITION).setMaxWidth(150);        
        
	    JLabel lblTitreInhibition = new JLabel("GESTION CAPTEUR EN MAINTENANCE");
	    lblTitreInhibition.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreInhibition.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreInhibition.setBackground(EFS_MARRON);
	    lblTitreInhibition.setOpaque(true);
	    btnInhibitionSupprimer.addActionListener(this);
	    pnlInhibitionCommande.add(btnInhibitionSupprimer);
	    pnlInhibition.add(lblTitreInhibition, BorderLayout.NORTH);
        pnlInhibition.add(new JScrollPane(tbInhibition), BorderLayout.CENTER);	    
        pnlInhibition.add(pnlInhibitionCommande, BorderLayout.SOUTH);
        pnlInhibition.setBackground(Color.RED);
        
        
        pnlJournal.add(pnlInformationAlarme, BorderLayout.NORTH);
        pnlJournal.add(splHistoriqueInhibition, BorderLayout.CENTER);
	    
	    // ============== ALARMES ====================
	    pnlAlarme.setLayout(new BorderLayout());
	    
	    JLabel lblTitreAlarme = new JLabel("GESTION ALARME TEMPS REEL");
	    lblTitreAlarme.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreAlarme.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreAlarme.setBackground(EFS_MARRON);
	    lblTitreAlarme.setOpaque(true);
	    pnlAlarme.add(lblTitreAlarme, BorderLayout.NORTH);
	    
	    tbAlarme = new JTable(mdlTpsReelAlarme);
	    tbAlarme.setSize(1000, 600);
	    tbAlarme.setFillsViewportHeight(true);        
	    tbAlarme.setBackground(EFS_BLEU);
        sorter =  new TableRowSorter<TableModel>(tbAlarme.getModel());
        tbAlarme.setRowSorter(sorter);        
        sorter.setSortable(0,  false);
        sorter.setSortsOnUpdates(true);
        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VOIE).setCellRenderer(new ColorCellRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPEL_ALERT).setCellRenderer(new ColorAppelAlertCellRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPARITION).setCellRenderer(new DateRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_DISPARITION).setCellRenderer(new DateRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_PRISE_EN_COMPTE).setCellRenderer(new DateRenderer());

        tbAlarme.setSelectionBackground(EFS_MARRON);
        tbAlarme.getSelectionModel();
		tbAlarme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VOIE).setPreferredWidth(40);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VOIE).setMaxWidth(40);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_INVENTAIRE).setPreferredWidth(100);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_INVENTAIRE).setMaxWidth(100);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPARITION).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPARITION).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_DISPARITION).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_DISPARITION).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_PRISE_EN_COMPTE).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_PRISE_EN_COMPTE).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_TYPE_ALARME).setPreferredWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_TYPE_ALARME).setMaxWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VALEUR).setPreferredWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VALEUR).setMaxWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPEL_ALERT).setPreferredWidth(30);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPEL_ALERT).setMaxWidth(30);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_RAPPEL_ALERT).setPreferredWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_RAPPEL_ALERT).setMaxWidth(50);        
        
        MaskFormatter mask = null ;
        try {
            mask = new MaskFormatter("###");
        } catch (ParseException e) {
            System.err.println("Le masque est incorrect") ;
        }
        // Les caractères valides sont les chiffres et les virgules
        mask.setValidCharacters("0123456789");
        mask.setPlaceholderCharacter('0');
        JFormattedTextField field = new JFormattedTextField( mask ) ;
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_RAPPEL_ALERT).setCellEditor( new DefaultCellEditor( field ) );
        
        sorter =  new TableRowSorter<TableModel>(tbAlarme.getModel());
        tbAlarme.setRowSorter(sorter);     
        sorter.setSortable(JTABLE_ALARME_VOIE,  false);
        sorter.setSortable(JTABLE_ALARME_DESCRIPTION_VOIE,  false);
        sorter.setSortable(JTABLE_ALARME_APPARITION,  false);
        sorter.setSortable(JTABLE_ALARME_DISPARITION,  false);
        sorter.setSortable(JTABLE_ALARME_PRISE_EN_COMPTE,  false);
        sorter.setSortable(JTABLE_ALARME_DESCRIPTION_ALARME,  false);
        sorter.setSortable(JTABLE_ALARME_TYPE_ALARME,  false);
        sorter.setSortable(JTABLE_ALARME_VALEUR,  false);
        sorter.setSortsOnUpdates(false);
        
	    JScrollPane jspTbAlarme = new JScrollPane(tbAlarme);
        jspTbAlarme.setBackground(Color.GREEN);
        jspTbAlarme.setOpaque(true);
        jspTbAlarme.setPreferredSize(new Dimension(400, 200));
        jspTbAlarme.setMinimumSize(new Dimension(400, 200));
	    pnlAlarme.add(jspTbAlarme, BorderLayout.CENTER);
	    
	    btnPriseEnCompte.addActionListener(this);
	    btnInformationAlarme.addActionListener(this);
	    btnAcquittement.addActionListener(this);
	    btnCourbe.addActionListener(this);
	    
	    btnInformationAlarme.setPreferredSize(btnPriseEnCompte.getPreferredSize());
	    btnInformationAlarme.setMinimumSize(btnPriseEnCompte.getMinimumSize());
	    btnAcquittement.setPreferredSize(btnPriseEnCompte.getPreferredSize());
	    btnAcquittement.setMinimumSize(btnPriseEnCompte.getMinimumSize());
	    btnCourbe.setPreferredSize(btnPriseEnCompte.getPreferredSize());
	    btnCourbe.setMinimumSize(btnPriseEnCompte.getMinimumSize());
	    
//	    pnlAlarmeCommande.add(btnAcquittement);
	    pnlAlarmeCommande.add(btnPriseEnCompte);
	    pnlAlarmeCommande.add(btnInformationAlarme);
	    pnlAlarmeCommande.add(btnCourbe);
	    pnlAlarme.add(pnlAlarmeCommande, BorderLayout.SOUTH);
	    // ============== FIN ALARMES =========================================

	    // ============== MENU ================================================
		JMenuBar m_bar;
		JMenu m_m1_Fichier;
		JMenu m_m2_Configuration;
		JMenu m_m3_Capteur;
		JMenu m_m4_Alarme;
		JMenu m_m5_Divers;
		JMenu m_m6_Comparateur;
		JMenu m_m7_Journal;
		JMenu m_m8_APropos;
		
		// Definition de la barre de menu
		m_bar = new JMenuBar();
		m_m1_Fichier = new JMenu("Fichier");
		m_m2_Configuration = new JMenu("Configuration");
		m_m3_Capteur = new JMenu("Capteurs");
		m_m4_Alarme = new JMenu("Alarme");
		m_m5_Divers = new JMenu("Divers");
		m_m6_Comparateur = new JMenu("Comparateur");
		m_m7_Journal = new JMenu("Journal");
		m_m8_APropos = new JMenu("?");
		
		
		mnuQuitter.addActionListener(this); // installation d'un écouteur d'action
		m_m1_Fichier.add(mnuQuitter);

		mnuFenPosteTechnique.addActionListener(this); // installation d'un écouteur d'action
		mnuFenService.addActionListener(this); // installation d'un écouteur d'action
		mnuFenTypeMateriel.addActionListener(this); // installation d'un écouteur d'action
		mnuFenZoneSubstitution.addActionListener(this); // installation d'un écouteur d'action
		mnuFenEquipement.addActionListener(this); // installation d'un écouteur d'action
		mnuFenUtilisateur.addActionListener(this); // installation d'un écouteur d'action
		mnuFenPriseEnCompte.addActionListener(this); // installation d'un écouteur d'action
		m_m2_Configuration.add(mnuFenPosteTechnique);
		m_m2_Configuration.add(mnuFenService);
		m_m2_Configuration.add(mnuFenTypeMateriel);
		m_m2_Configuration.add(mnuFenZoneSubstitution);
		m_m2_Configuration.add(mnuFenEquipement);
		m_m2_Configuration.add(mnuFenUtilisateur);
		m_m2_Configuration.add(mnuFenPriseEnCompte);
		
		mnuFenEntreeAnalogique.addActionListener(this); // installation d'un écouteur d'action
		mnuFenEntreeDigitale.addActionListener(this); // installation d'un écouteur d'action
		mnuFenSortieAnalogique.addActionListener(this); // installation d'un écouteur d'action
		mnuFenSortieDigitale.addActionListener(this); // installation d'un écouteur d'action
		mnuFenCourbe.addActionListener(this); // installation d'un écouteur d'action
		mnuFenHistoriqueCalibration.addActionListener(this); // installation d'un écouteur d'action
		mnuFenChoixCapteurAnalogique.addActionListener(this);
		
		m_m3_Capteur.add(mnuFenEntreeAnalogique);
		m_m3_Capteur.add(mnuFenEntreeDigitale);
		m_m3_Capteur.add(mnuFenSortieAnalogique);
		m_m3_Capteur.add(mnuFenSortieDigitale);
		m_m3_Capteur.add(mnuFenCourbe);
		m_m3_Capteur.add(mnuFenHistoriqueCalibration);
		m_m3_Capteur.add(mnuFenChoixCapteurAnalogique);
		
		mnuFenHistoriqueAlarme.addActionListener(this); // installation d'un écouteur d'action
		mnuFenValidationHistoriqueAlarme.addActionListener(this); // installation d'un écouteur d'action
		m_m4_Alarme.add(mnuFenHistoriqueAlarme);
		m_m4_Alarme.add(mnuFenValidationHistoriqueAlarme);

		mnuLogin.addActionListener(this); // installation d'un écouteur d'action
		m_m5_Divers.add(mnuLogin);
		
		mnuFenComparateur.addActionListener(this);
		m_m6_Comparateur.add(mnuFenComparateur);

		mnuFenJournal.addActionListener(this);
		m_m7_Journal.add(mnuFenJournal);
		
		mnuFenAPropos.addActionListener(this);
		m_m8_APropos.add(mnuFenAPropos);
		
		m_bar.add(m_m1_Fichier);
		m_bar.add(m_m2_Configuration);
		m_bar.add(m_m3_Capteur);
		m_bar.add(m_m4_Alarme);
		m_bar.add(m_m5_Divers);
		m_bar.add(m_m6_Comparateur);
		m_bar.add(m_m7_Journal);
		m_bar.add(m_m8_APropos);
		m_bar.setBorder(BorderFactory.createLoweredBevelBorder());
		m_bar.setBorderPainted(true);
		setJMenuBar(m_bar);
	    
	    // ===========================================
	}
	
	private void remplirAlarmeHistorique() {
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
	    Calendar cal = Calendar.getInstance();
		String strDateDeb;
		String strDateFin;
		String strSql = "";
		int idPriseEnCompte = 0;
		
		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	    cal.setTime(dateDeb);
	    cal.add(Calendar.DAY_OF_MONTH, -3);
	    dateDeb = cal.getTime();
		
		strDateDeb = "TO_DATE('" + formater.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formater.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";

		// Vider le tableau
		mdlHistoriqueAlarme.removeAllAlarme();
		
		// Création du filtre
		strSql = "SELECT AlarmeHistorique.*, Capteur.Description, Capteur.Nom, Utilisateur.Nom AS nomUtilisateur"
				   + " FROM ((AlarmeHistorique LEFT JOIN Capteur ON AlarmeHistorique.idCapteur = Capteur.idCapteur)"
				   + " LEFT JOIN Utilisateur ON AlarmeHistorique.idUtilisateur = Utilisateur.idUtilisateur)"
				   + " WHERE DateApparition >= " + strDateDeb + " AND DateApparition <= " + strDateFin 
				   + " AND AppelAlert = 1"
				   + " ORDER BY DateApparition DESC";
		ResultSet result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
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
						result.getString("nomUtilisateur"), result.getString("commentairePriseEnCompte")
						));
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
				
	} // Fin remplirAlarmeHistorique()	

	private void remplirInhibition() {
		String strSql = "";
		int idInhibition = 0;
		int idCapteur = 0;
		String nomCapteur;
		String descriptionCapteur;
		Date dateInhibition = new Date();
		String nomUtilisateur = "";
		int idUtilisateur = 0;
		int idRaisonInhibition = 0;
		String raisonInhibition = "";
		
		mdlInhibition.removeAllInhibition();
		strSql = "SELECT Inhibition.idInhibition, Inhibition.DateInhibition, Capteur.idCapteur"
				+ ", Capteur.Nom AS nomVoie, Capteur.Description, Utilisateur.Nom AS nomUtilisateur"
				+ ", Utilisateur.Prenom, Utilisateur.idUtilisateur, RaisonInhibition.idRaisonInhibition, RaisonInhibition.Raison"
				+ " FROM ((Inhibition LEFT JOIN Capteur ON Inhibition.idCapteur = Capteur.idCapteur)"
				+ " LEFT JOIN Utilisateur ON Inhibition.idUtilisateur = Utilisateur.idUtilisateur)"
				+ " LEFT JOIN RaisonInhibition ON Inhibition.idRaisonInhibition = RaisonInhibition.idRaisonInhibition";
		
		ResultSet result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				idInhibition = result.getInt("idInhibition");
				idCapteur = result.getInt("idCapteur");
				nomCapteur = result.getString("nomVoie");
				descriptionCapteur = result.getString("Description");
				dateInhibition = result.getDate("DateInhibition");
				nomUtilisateur = result.getString("nomUtilisateur") + " " + result.getString("Prenom");
				idUtilisateur = result.getInt("idUtilisateur");
				idRaisonInhibition = result.getInt("idRaisonInhibition");
				raisonInhibition = result.getString("Raison");
				
				mdlInhibition.addInhibition(new Inhibition(idInhibition, idCapteur, nomCapteur, descriptionCapteur, dateInhibition, idUtilisateur, nomUtilisateur, idRaisonInhibition, raisonInhibition));
						
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture inhibitions");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} // fin remplirInhibition()
		
	private void remplirPriseEnCompte() {
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
	} // Fin LectureDonnees	
	
	private void gestionCourbe() {
    	if (tbAlarme.getSelectedRowCount() > 0) {
	    	int[] selection = tbAlarme.getSelectedRows();
	    	int indexSelection = selection[0];
	        int idCapteur = mdlTpsReelAlarme.getIdCapteur(indexSelection); 
	        if (idCapteur > 0) {
				FenCourbe fenCourbe = new FenCourbe();
				fenCourbe.setIdCapteur(idCapteur);

				fenCourbe.traceCourbe();
				fenCourbe.setVisible(true);
				fenCourbe.requestFocusInWindow();
	        } // Fin if
    	} else {
    		int idCapteur = -1;
			FenCourbe fenCourbe = new FenCourbe();
			fenCourbe.setIdCapteur(idCapteur);

			fenCourbe.traceCourbe();
			fenCourbe.setVisible(true);
			fenCourbe.requestFocusInWindow();
    		
    	}
	} // Fin gestionCourbe
	
	private void gestionInformationAlarme() {
		ResultSet result = null;
		String strSql = "";

		if (tbAlarme.getRowCount() > 0 && tbAlarme.getSelectedRowCount() > 0) {
        	int[] selection = tbAlarme.getSelectedRows();
        	int indexSelection = selection[0];

	        int idCapteur = mdlTpsReelAlarme.getIdCapteur(indexSelection); 
	        int voieApi = -1;

	        idCapteurInformation = idCapteur;
		    strSql = "SELECT Capteur.*, ZoneSubstitution.Nom AS NomZone FROM (Capteur LEFT JOIN ZoneSubstitution"
		    		+ " ON Capteur.idZoneSubstitution = ZoneSubstitution.idZoneSubstitution)"
		    		+ " WHERE idCapteur = " + idCapteur;
			result = ctn.lectureData(strSql);
			try {
				if(result.next()) {
			        lblInfoVoie.setText(result.getString("Nom") + "   " + result.getString("Description"));
			        lblInfoZoneSubstitution.setText(result.getString("NomZone"));

			        voieApi = (mdlTpsReelAlarme.getVoieApi(indexSelection)) -1;
			        if (result.getInt("TypeCapteur") == CAPTEUR_ANALOGIQUE_ENTREE) {
				        lblInfoValeur.setText(" " + (EFS_Client_Variable.tbValeurAI[voieApi] + EFS_Client_Variable.tbCalibrationAI[voieApi]) / 10);
				        txtInfoSeuilTempo.setText(" " + EFS_Client_Variable.tbTempoAI[voieApi]);
				        lblInfoCalibration.setText(" " + EFS_Client_Variable.tbCalibrationAI[voieApi] / 10);
				        txtInfoSeuilBas.setText(" " + EFS_Client_Variable.tbSeuilBas[voieApi] / 10 );
				        txtInfoSeuilHaut.setText(" " + EFS_Client_Variable.tbSeuilHaut[voieApi] / 10);
			        }
			        else {
				        lblInfoValeur.setText(" " + (EFS_Client_Variable.tbValeurDI[voieApi]));
				        txtInfoSeuilTempo.setText(" " + EFS_Client_Variable.tbTempoDI[voieApi]);
				        lblInfoCalibration.setText("");
				        txtInfoSeuilBas.setText("");
				        txtInfoSeuilHaut.setText("");
			        }
				} // Fin while result.next()
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Probléme lecture des voies informations");
			}
			ctn.closeLectureData();
			try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        } // Fin if
	}	
	
	private void gestionAlarmeTpsReel() {
		int cptCapteur = 0;
		boolean blAlert = false;
		boolean blTrouve = false;
		String strSql = "";
		ResultSet result = null;
		int voieApi = -1;

		// Lecture de la table AlarmeEnCours
		strSql = "SELECT V2_AlarmeEnCours.*, Capteur.VoieApi AS CapteurVoieApi, Capteur.Alarme, Capteur.Description, Capteur.TypeCapteur, Capteur.Nom, Capteur.Inhibition, Equipement.NumeroInventaire "
			   + " FROM (V2_AlarmeEnCours LEFT JOIN (Capteur LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
			   + " ON V2_AlarmeEnCours.idCapteur = Capteur.idCapteur)";
//			   + " WHERE blTempo = 1 AND inhibition = 0";
		result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				cptCapteur++;
				// voie traitée
				voieApi = result.getInt("CapteurVoieApi") - 1;
				// Regarder si déjà dans la liste
				blTrouve = false;
				for(int i = 0; i < mdlTpsReelAlarme.getRowCount(); i++) {
					if(mdlTpsReelAlarme.getIdCapteur(i) == result.getInt("idCapteur")) {
						// Mettre à jour la valeur
						if(result.getInt("TypeCapteur") == CAPTEUR_ANALOGIQUE_ENTREE) {
							mdlTpsReelAlarme.setValueAt((EFS_Client_Variable.tbValeurAI[voieApi] + EFS_Client_Variable.tbCalibrationAI[voieApi]) / 10, i, JTABLE_ALARME_VALEUR);
						} else {
							mdlTpsReelAlarme.setValueAt((double)EFS_Client_Variable.tbValeurDI[voieApi], i, JTABLE_ALARME_VALEUR);
						}
						mdlTpsReelAlarme.setValueAt(result.getDate("DateDisparition"), i, JTABLE_ALARME_DISPARITION);
						mdlTpsReelAlarme.setValueAt(result.getDate("DateApparition"), i, JTABLE_ALARME_APPARITION);
						mdlTpsReelAlarme.setValueAt(result.getDate("DatePriseEnCompte"), i, JTABLE_ALARME_PRISE_EN_COMPTE);
						blTrouve = true;
					} // Fin if
				} // Fin for i
				if(!blTrouve) {
					if(result.getInt("Alarme") == ALARME_ALERT) {
						blAlert = true;
					}
					else {
						blAlert = false;
					}
//					if ((result.getInt("Inhibition") != ALARME_INHIBITION) && (result.getInt("blTempo") == ALARME_TEMPO_DEPASSEE)) {
						mdlTpsReelAlarme.addAlarme(new TpsReelAlarme(result.getInt("idCapteur"), result.getString("Nom")
								, result.getString("Description"), result.getString("NumeroInventaire"), result.getDate("DateApparition"),
								result.getDate("DatePriseEnCompte"), result.getString("DescriptionAlarme")
								, result.getInt("VoieApi"), blAlert, result.getDate("DateDisparition"), result.getInt("Alarme")));
//					} // fin mise en tableau
				} // fin blTrouve
			} // fin while
		} 
		catch (SQLException e) {
				e.printStackTrace();
		}
		ctn.closeLectureData();
		try {
			result.close();
			result = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (mdlTpsReelAlarme.getRowCount() != cptCapteur) {
			mdlTpsReelAlarme.removeAllAlarme();
			gestionAlarmeTpsReel();
		}
	}
	
	private void gestionModificationSeuil(int typeSeuil) {
		ResultSet result = null;
		String strSql;
		int voieApi;
		String msgAvertissement = "";
		String msgJournal = "";
		
	    strSql = "SELECT * FROM Capteur WHERE idCapteur = " + idCapteurInformation;
		result = ctn.lectureData(strSql);
		try {
			if(result.next()) {
		        voieApi = result.getInt("VoieApi") -1;
		        if (result.getInt("TypeCapteur") == CAPTEUR_ANALOGIQUE_ENTREE) {
		        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_BAS) {
			        	msgAvertissement = "Voulez vous passer le seuil bas de " + EFS_Client_Variable.tbSeuilBas[voieApi] / 10
			        			+ " à " + txtInfoSeuilBas.getText() + " pour le capteur : " + result.getString("Nom")
			        			+ "  " + result.getString("Description");
			        	msgJournal = "Passage du seuil bas de " + EFS_Client_Variable.tbSeuilBas[voieApi] / 10
			        			+ " à " + txtInfoSeuilBas.getText() + " pour le capteur : " + result.getString("Nom")
			        			+ "  " + result.getString("Description");
		        	} // fin if seuil bas
		        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_HAUT) {
			        	msgAvertissement = "Voulez vous passer le seuil haut de " + EFS_Client_Variable.tbSeuilHaut[voieApi] / 10
			        			+ " à " + txtInfoSeuilHaut.getText() + " pour le capteur : " + result.getString("Nom")
			        			+ "  " + result.getString("Description");
			        	msgJournal = "Passage du seuil haut de " + EFS_Client_Variable.tbSeuilHaut[voieApi] / 10
			        			+ " à " + txtInfoSeuilHaut.getText() + " pour le capteur : " + result.getString("Nom")
			        			+ "  " + result.getString("Description");

		        	} // fin if seuil haut
		        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_TEMPO) {
			        	msgAvertissement = "Voulez vous passer la tempo seuil de " + EFS_Client_Variable.tbTempoAI[voieApi]
			        			+ " à " + txtInfoSeuilTempo.getText() + " pour le capteur : " + result.getString("Nom")
			        			+ "  " + result.getString("Description");
			        	msgJournal = "Passage de la tempo du seuil de " + EFS_Client_Variable.tbTempoAI[voieApi]
			        			+ " à " + txtInfoSeuilTempo.getText() + " pour le capteur : " + result.getString("Nom")
			        			+ "  " + result.getString("Description");

		        	} // fin if seuil haut
					ctn.closeLectureData();
					try {
						result.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}			
		        	// Demande
		        	
		        	int retour = JOptionPane.showConfirmDialog(this, msgAvertissement, "GTC Visualize - Programme Client", JOptionPane.OK_CANCEL_OPTION);			        	
		        	
		        	if( retour == JOptionPane.OK_OPTION) {
			        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_BAS) {
			        		strSql = "UPDATE EntreeAnalogique SET SeuilBas = " + (Double.parseDouble(txtInfoSeuilBas.getText()) * 10)
			        				+ " WHERE idCapteur = " + idCapteurInformation;
			        		ctn.fonctionSql(strSql);
			        		EFS_Client_Variable.tbSeuilBas[voieApi] = Double.parseDouble(txtInfoSeuilBas.getText()) * 10;
							// Prevenir les programmes maitre
							AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_SEUIL_BAS);
			        	}
			        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_HAUT) {
			        		strSql = "UPDATE EntreeAnalogique SET SeuilHaut = " + (Double.parseDouble(txtInfoSeuilHaut.getText()) * 10)
			        				+ " WHERE idCapteur = " + idCapteurInformation;
			        		ctn.fonctionSql(strSql);
			        		EFS_Client_Variable.tbSeuilHaut[voieApi] = Double.parseDouble(txtInfoSeuilHaut.getText()) * 10;
							// Prevenir les programmes maitre
							AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_SEUIL_HAUT);
			        	}
			        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_TEMPO) {
			        		strSql = "UPDATE EntreeAnalogique SET SeuilTempo = " + (Double.parseDouble(txtInfoSeuilTempo.getText()))
			        				+ " WHERE idCapteur = " + idCapteurInformation;
			        		ctn.fonctionSql(strSql);
			        		EFS_Client_Variable.tbTempoAI[voieApi] = Integer.parseInt(txtInfoSeuilTempo.getText());
							// Prevenir les programmes maitre
							AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_SEUIL_TEMPO);
			        	}
			        	if (typeSeuil ==  AE_Constantes.TYPE_SEUIL_TEMPO) {
			        		strSql = "INSERT INTO Journal (DateJournal, Description, TypeJournal, idCapteur, idUtilisateur)" 
			        				+ " VALUES(sysdate, '" + msgJournal + "', " + TYPE_JOURNAL_MODIFICATION_SEUIL_TEMPO 
			        				+ ", " + idCapteurInformation + ", " + EFS_Client_Variable.idUtilisateur
			        				+ ")";
			        		ctn.fonctionSql(strSql);
			        	} else {
			        		strSql = "INSERT INTO Journal (DateJournal, Description, TypeJournal, idCapteur, idUtilisateur)" 
			        				+ " VALUES(sysdate, '" + msgJournal + "', " + TYPE_JOURNAL_MODIFICATION_SEUIL 
			        				+ ", " + idCapteurInformation + ", " + EFS_Client_Variable.idUtilisateur
			        				+ ")";
			        		ctn.fonctionSql(strSql);
			        	}
		        	} else {
						System.out.println("Pas d'enregistrement modifications de seuil ou tempo");
		        	}
		        	
		        } // fin if Analogique
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture des voies informations");
		}
		
	} // fin gestionModificationSeuil

	private void gestionPriseEnCompte() {
		String raison = new String("");
		Date now = new Date();
		String strSql = "";
	
        if (tbAlarme.getRowCount() > 0) {
        	if (tbAlarme.getSelectedRowCount() > 0) {
		        int[] selection = tbAlarme.getSelectedRows();
		        int indexSelection = selection[0];
		        int idCapteur = mdlTpsReelAlarme.getIdCapteur(indexSelection); 
		        String strTypeAlarme = "";
		        
				// Motif Prise En Compte seulement si appel Alert
		        strTypeAlarme = (String) mdlTpsReelAlarme.getValueAt(indexSelection, JTABLE_ALARME_TYPE_ALARME);
		        if (strTypeAlarme.equals("Alarme")) {
					int idPriseEnCompte = 0;
		        	do {
						Object [] possibilites = new Object[tbPriseEnCompte.size()];
						for(int i = 0; i < tbPriseEnCompte.size(); i++) {
							possibilites[i] = tbPriseEnCompte.get(i).getNom();
						}
						String strReponse = (String)JOptionPane.showInputDialog(this, "Choisir un motif de prise en compte : ", "Prise en compte", JOptionPane.QUESTION_MESSAGE, null, possibilites, null);
						idPriseEnCompte = 1;
						if (strReponse != null) {
							for(int i = 0; i < tbPriseEnCompte.size(); i++) {
								if(strReponse.equals(tbPriseEnCompte.get(i).getNom())) {
									idPriseEnCompte = (int) tbPriseEnCompte.get(i).getId();
								}
							} // fin for
						} else {
							raison = "En attente";
						}
						
						// idPriseEncompte == 1 (Autres)
						if(idPriseEnCompte == 1) {
							// Demande de commentaire
							raison = AE_Fonctions.saisieTexte("Veuillez entrer une raison : ", "Raison commentaire ...");
							if (raison == null) raison = "";
						} else {
							raison = "---";
						}
		        	} while(raison == "");
		        	
					strSql = "UPDATE V2_AlarmeEnCours SET idPriseEnCompte = " + idPriseEnCompte
							   + " , idUtilisateur = " + EFS_Client_Variable.idUtilisateur
							   + " , CommentairePriseEnCompte = '" + raison + "'"
							   + " WHERE idCapteur = " + idCapteur;
					ctn.fonctionSql(strSql);
					mdlTpsReelAlarme.setMotifIdPriseEncompte(indexSelection, idPriseEnCompte);
		        } // fin if appelAlert

		        if(mdlTpsReelAlarme.getValueAt(indexSelection, JTABLE_ALARME_PRISE_EN_COMPTE) == null) {
					// Date prise en compte
					strSql = "UPDATE V2_AlarmeEnCours SET DatePriseEnCompte = sysdate, blPriseEnCompte = 1 WHERE idCapteur = " + idCapteur;
					ctn.fonctionSql(strSql);
					mdlTpsReelAlarme.setValueAt(now, indexSelection, JTABLE_ALARME_PRISE_EN_COMPTE);
		        } // fin if datePrsieEnCompte
	
		        // Prevenir le programme maitre
		        AE_Fonctions.modifierMaitreViaClient(idCapteur, VIA_API_PRISE_EN_COMPTE);
		        
				// Arret klaxon et Alert
//				gestionKlaxon(false);
//				gestionAlert(false);
				
        	} else {
     	       JOptionPane.showMessageDialog(this, "Vous devez sélectionner une ligne pour prendre en compte ...",
   	    		    "GTC Visualize - Programme Maitre", JOptionPane.WARNING_MESSAGE);
        	}
        } else {
//			gestionKlaxon(false);
//			gestionAlert(false);
        }
	} // Fin gestionPriseEnCompte
		
/* Retiré le 30/10/2018	
	private void gestionKlaxon(boolean sonnerie) {
		pnlInfo.setLblInformation(0, "Lancement klaxon ...");
		try{
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			@SuppressWarnings("unused")
			double [] reqReponse = null;
			
			addr = InetAddress.getByName(EFS_Client_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, AE_TCP_Constantes.MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Connecté ... ");
			}
			else {
				if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Déconnecté ... ");
			}

			if (sonnerie) {
				if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("Sonnerie");
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2010, 1));			
			} 
			else {
				if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("Fin sonnerie");
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2010, 0));			
			} // Fin if sonnerie

			con.close();
            if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Close ... ");
            
		} // Fin Try
		catch (Exception e){
			System.out.println("Erreur Ecriture Klaxon ... ");
			System.err.println(e.getMessage());
		} // Fin catch		
	} // Fin gestionKlaxon()	
*/

/* Retiré le 30/10/2018	
	private void gestionAlert(boolean alerte) {
		String strSql = "";
		if (alerte) {
			if (AE_TCP_Constantes.MODE_DEBUG_BASE) System.out.println("Appel Alerte");
			
			strSql = "UPDATE AlarmeAlerte SET Alarme = 1 WHERE idAlarmeAlerte = 1";
			ctn.fonctionSql(strSql);
		} 
		else {
			if (AE_TCP_Constantes.MODE_DEBUG_BASE) System.out.println("Fin appel Alerte");

			strSql = "UPDATE AlarmeAlerte SET Alarme = 0, RelanceProgramme = 0 WHERE idAlarmeAlerte = 1";
			ctn.fonctionSql(strSql);		
		} // Fin if alerte
	} // Fin gestionAlert()		
*/
	
	private void gestionInhibitionSupression() {
		String msgJournal = "";
		String msgAvertissement = "";
		String strSql = "";
		
		if(EFS_Client_Variable.idUtilisateur > 0) {
			// Ligne sélectionnée
	        if (tbInhibition.getRowCount() > 0) {
	        	if (tbInhibition.getSelectedRowCount() > 0) {
	    			msgAvertissement = "Voulez vous enlever ce capteur de la zone maintenance ? ";
	    			int retour = JOptionPane.showConfirmDialog(this, msgAvertissement, "GTC Visualize - Programme Maitre", JOptionPane.OK_CANCEL_OPTION);		
	    			if( retour == JOptionPane.OK_OPTION) {
				        int[] selection = tbInhibition.getSelectedRows();
				        int indexSelection = selection[0];
				        int idInhibition = mdlInhibition.getIdInhibition(indexSelection); 
				        int idCapteur = mdlInhibition.getIdCapteur(indexSelection); 
				        
				        // === Modification 27-05-2016 ===
						// Suppression dans la table Alarme en Cours
		        		strSql = "DELETE FROM AlarmeEnCours WHERE idCapteur = " + idCapteur;
		        		ctn.fonctionSql(strSql);
						// === Fin modification 27-05-2016 ===
				        
				        
						strSql = "DELETE FROM Inhibition WHERE idInhibition = " + idInhibition;
						ctn.fonctionSql(strSql);
						strSql = "UPDATE Capteur SET Inhibition = 0 WHERE idCapteur = " + idCapteur;
						ctn.fonctionSql(strSql);
						
						msgJournal = "Retrait de la zone maintenance du capteur : " + mdlInhibition.getValueAt(indexSelection, JTABLE_INHIBITION_VOIE);
		        		strSql = "INSERT INTO Journal (DateJournal, Description, TypeJournal, idCapteur, idUtilisateur)" 
		        				+ " VALUES(sysdate, '" + msgJournal + "', " + TYPE_JOURNAL_INHIBITION_RETRAIT 
		        				+ ", " + idCapteur + ", " + EFS_Client_Variable.idUtilisateur
		        				+ ")";
		        		ctn.fonctionSql(strSql);
						mdlInhibition.removeInhibition(indexSelection);
						// Prevenir les programmes maitre
						AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_INHIBITION);
	            	}
	        	} // fin if
	        } // fin if
		} else {
	       JOptionPane.showMessageDialog(this, "Vous devez être logger pour modifier ...",
	    		    "GTC Visualize - Programme Maitre", JOptionPane.WARNING_MESSAGE);
		} // fin idUtilisateur > 0

	} // fin gestionInhibitionSuppression
		
	
	@Override
	public void actionPerformed(ActionEvent e)  {
		if (e.getSource() == btnCourbe) {
			gestionCourbe();
		}
		if (e.getSource() == btnInformationAlarme) {
			gestionInformationAlarme();
		} // Fin If
		if (e.getSource() == btnModifSeuilBas) {
			gestionModificationSeuil(TYPE_SEUIL_BAS);
		} // Fin If
		if (e.getSource() == btnModifSeuilHaut) {
			gestionModificationSeuil(TYPE_SEUIL_HAUT);
		} // Fin If
		if (e.getSource() == btnModifSeuilTempo) {
			gestionModificationSeuil(TYPE_SEUIL_TEMPO);
		} // Fin If
		if (e.getSource() == btnPriseEnCompte) {
			gestionPriseEnCompte();
		} // Fin If
		if (e.getSource() == btnInhibitionSupprimer) {
			gestionInhibitionSupression();
		} // Fin If

		
		if (e.getSource() == mnuQuitter) {
			// Quitter l'application
			System.exit(1);
		}		

		if (e.getSource() == mnuFenPosteTechnique) {
			// Appel fenetre poste technique
			FenPosteTechnique fenetre = new FenPosteTechnique();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		
	
		if (e.getSource() == mnuFenService) {
			// Appel fenetre poste technique
			FenService fenetre = new FenService();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenTypeMateriel) {
			// Appel fenetre poste technique
			FenTypeMateriel fenetre = new FenTypeMateriel();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenZoneSubstitution) {
			// Appel fenetre poste technique
			FenZoneSubstitution fenetre = new FenZoneSubstitution();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenEquipement) {
			// Appel fenetre poste technique
			FenEquipement fenetre = new FenEquipement();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenUtilisateur) {
			// Appel fenetre utilisateur
			FenUtilisateur fenetre = new FenUtilisateur();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenPriseEnCompte) {
			// Appel fenetre prise en compte
			FenRaisonPriseEnCompte fenetre = new FenRaisonPriseEnCompte();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		
		
		if (e.getSource() == mnuFenEntreeAnalogique) {
			// Appel fenetre poste technique
			FenEntreeAnalogique fenetre = new FenEntreeAnalogique();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenEntreeDigitale) {
			// Appel fenetre poste technique
			FenEntreeDigitale fenetre = new FenEntreeDigitale();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenSortieAnalogique) {
			// Appel fenetre Sortie analogique
			FenSortieAnalogique fenetre = new FenSortieAnalogique();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenSortieDigitale) {
			// Appel fenetre sortie digitale
			FenSortieDigitale fenetre = new FenSortieDigitale();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		
		
		if (e.getSource() == mnuFenCourbe) {
			// Appel fenetre poste technique
			FenCourbe fenetre = new FenCourbe();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}		

		if (e.getSource() == mnuFenHistoriqueCalibration) {
			// Appel fenetre poste technique
			FenHistoriqueCalibration fenetre;
			try {
				fenetre = new FenHistoriqueCalibration();
				fenetre.setVisible(true);
				fenetre.requestFocusInWindow();		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}		

		if (e.getSource() == mnuFenChoixCapteurAnalogique) {
			// Appel fenetre ChoixCapteurAnalogique
			FenChoixCapteurAnalogique fenetre;
			try {
				fenetre = new FenChoixCapteurAnalogique();
				fenetre.setVisible(true);
				fenetre.requestFocusInWindow();		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}	
		
		if (e.getSource() == mnuFenHistoriqueAlarme) {
			// Appel fenetre poste technique
			FenHistoriqueAlarme fenetre;
			try {
				fenetre = new FenHistoriqueAlarme();
				fenetre.setVisible(true);
				fenetre.requestFocusInWindow();		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}	

		if (e.getSource() == mnuFenValidationHistoriqueAlarme) {
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
		
		if (e.getSource() == mnuFenJournal) {
			// Appel fenetre Journal
			FenJournal fenetre;
			try {
				fenetre = new FenJournal();
				fenetre.setVisible(true);
				fenetre.requestFocusInWindow();		
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}	

		if (e.getSource() == mnuFenComparateur) {
			// Appel fenetre Comparateur
			FenComparateur fenetre;
			try {
				fenetre = new FenComparateur();
				fenetre.setVisible(true);
				fenetre.requestFocusInWindow();		
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}	
		
		
		if (e.getSource() == mnuFenAPropos) {
			JOptionPane.showMessageDialog(null, "Programme réalisé par Alizé - Version V1.0", "A Propos ...", JOptionPane.INFORMATION_MESSAGE);
		}	
		
		if (e.getSource() == mnuLogin) {
			// Appel fenetre Login
			Login fenetre = new Login();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		}
		
		if (e.getSource() == tmrTpsReel) {
			EFS_Client_Variable.lectureVoie();
			gestionAlarmeTpsReel();
		}
		if (e.getSource() == tmrRaffraichissementMinute) {
			remplirInhibition();
			remplirAlarmeHistorique();
		}
		
	}

} // fin class
