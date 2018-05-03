import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import AE_Communication.AE_TCP_Constantes;
import AE_General.*;
import EFS_Structure.StructEntreeDigitale;
import EFS_Structure.StructEquipement;
import EFS_Structure.StructPosteTechnique;
import EFS_Structure.StructService;
import EFS_Structure.StructTypeMateriel;
import EFS_Structure.StructZoneSubstitution;
import kernel.ArrayAlarmeService;


public class FenEntreeDigitale extends JFrame implements AE_Constantes, ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlGauche = new JPanel();
	JPanel pnlCorps = new JPanel();
	JSplitPane splCorps = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlGauche, pnlCorps);
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();	

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Variables
	private int voieApiAffichee = -1;
	
	// Timer
    private Timer tmrTpsReel = new Timer(AE_TCP_Constantes.TIMER_LECTURE_TPS_REEL, this);	
		
	// Propre
	private JLabel lblNom = new JLabel("N° voie : ");
	private JLabel lblDescription = new JLabel("Designation : ");
	private JLabel lblEquipement = new JLabel("Equipement : ");
	private JLabel lblPosteTechnique = new JLabel("Poste technique : ");
	private JLabel lblTypeMateriel = new JLabel("Type matériel : ");
	private JLabel lblZoneSubstitution = new JLabel("Zone substitution : ");
	private JLabel lblTempo = new JLabel("Temporisation seuil : ");
	private JLabel lblService = new JLabel("Service : ");
	private JLabel lblContact = new JLabel("Contact : ");
	
	private JTextField txtNom = new JTextField("", 20);
	private JTextField txtDescription = new JTextField("", 20);
	private JTextField txtTempo = new JTextField("");
	private JTextField txtContact = new JTextField("");
	
	private JComboBox<String> cmbZoneSubstitution = new JComboBox<String>();
	private JComboBox<String> cmbEquipement = new JComboBox<String>();
	private JComboBox<String> cmbPosteTechnique = new JComboBox<String>();
	private JComboBox<String> cmbTypeMateriel = new JComboBox<String>();
	private JComboBox<String> cmbService = new JComboBox<String>();
	private List<StructZoneSubstitution> tbZoneSubstitution = new ArrayList<StructZoneSubstitution>();	
	private List<StructEquipement> tbEquipement = new ArrayList<StructEquipement>();	
	private List<StructPosteTechnique> tbPosteTechnique = new ArrayList<StructPosteTechnique>();	
	private List<StructTypeMateriel> tbTypeMateriel = new ArrayList<StructTypeMateriel>();	
	private List<StructService> tbService = new ArrayList<StructService>();	
	
	private JLabel lblAlarme = new JLabel("Alarme : ");
	private JPanel pnlAlarme = new JPanel();
	private ButtonGroup bgAlarme = new ButtonGroup();
	private JRadioButton optAlarmeAlert = new JRadioButton("Alarme");
	private JRadioButton optAlarmeDefaut = new JRadioButton("Défaut");
	private JRadioButton optAlarmeEtat = new JRadioButton("Etat");
	private JRadioButton optAlarmeRien = new JRadioButton("Rien");
	
	// ===== Modifications 03/05/2018 =====
	private JComboBox<String> cmbAlarmeService = new JComboBox<String>();
	private ArrayAlarmeService tbAlarmeService = new ArrayAlarmeService();	
	private JPanel pnlGestionAlarme = new JPanel();
	// ===== Fin modifications 03/05/2018 =====
	
	private JLabel lblOnOff = new JLabel("NO / NF : ");
	private JPanel pnlOnOff = new JPanel();
	private ButtonGroup bgOnOff = new ButtonGroup();
	private JRadioButton optOn = new JRadioButton("NO");
	private JRadioButton optOff = new JRadioButton("NF");

	private JLabel lblOnOffEnCours = new JLabel("Valeur en cours : ");
	private JPanel pnlOnOffEnCours = new JPanel();
	private ButtonGroup bgOnOffEnCours = new ButtonGroup();
	private JRadioButton optOnEnCours = new JRadioButton("ON");
	private JRadioButton optOffEnCours = new JRadioButton("OFF");
	
	private JLabel lblZoneMaintenance = new JLabel("Zone Maintenance : ");
	private JPanel pnlZoneMaintenance = new JPanel();
	private ButtonGroup bgZoneMaintenance = new ButtonGroup();
	private JRadioButton optZoneMaintenanceOui = new JRadioButton("Oui");
	private JRadioButton optZoneMaintenanceNon = new JRadioButton("Non");
	
	
	private JButton btnModifierDescription = new JButton("Modifier désignation");
	private JButton btnModifierEquipement = new JButton("Modifier équipement");
	private JButton btnModifierPosteTechnique = new JButton("Modifier poste technique");
	private JButton btnModifierTypeMateriel = new JButton("Modifier type matériel");
	private JButton btnModifierAlarme = new JButton("Modifier alarme");
	private JButton btnModifierInhibition = new JButton("Modifier zone maintenance");
	private JButton btnModifierOnOff = new JButton("Modifier NO / NF");
	private JButton btnModifierSeuilTempo = new JButton("Modifier seuil tempo");
	private JButton btnModifierZoneSubstitution = new JButton("Modifier zone substitution");
	private JButton btnModifierService = new JButton("Modifier service");
	private JButton btnModifierContact = new JButton("Modifier contact");
	
	private JButton btnAjouter = new JButton("Nouveau");
	private JButton btnCourbe = new JButton("Courbe");
	private JPanel pnlCommande = new JPanel();
	
	
	// Tri (à gauche)
	private String[] ptTous = {"Tous"}; 
	private JLabel lblServiceGauche = new JLabel("Service : ");
	private JComboBox<String> cmbTriService = new JComboBox<String>(ptTous);	
	private JLabel lblPosteTechniqueGauche = new JLabel("Poste technique : ");
	private JComboBox<String> cmbTriPosteTechnique = new JComboBox<String>(ptTous);
	private JLabel lblEquipementGauche = new JLabel("Equipement : ");
	private JComboBox<String> cmbTriEquipement = new JComboBox<String>(ptTous);	
	private JLabel lblTypeMaterielGauche = new JLabel("Type matériel : ");
	private JComboBox<String> cmbTriTypeMateriel = new JComboBox<String>(ptTous);	
	private JLabel lblCapteurGauche = new JLabel("Capteurs : ");
	private List<StructTypeMateriel> tbTriTypeMateriel = new ArrayList<StructTypeMateriel>();	
	private List<StructService> tbTriService = new ArrayList<StructService>();	
	private List<StructEquipement> tbTriEquipement = new ArrayList<StructEquipement>();	
	private List<StructPosteTechnique> tbTriPosteTechnique = new ArrayList<StructPosteTechnique>();	

	private List<StructEntreeDigitale> tbCapteur = new ArrayList<StructEntreeDigitale>();
	private DefaultListModel<String> modelCapteur = new DefaultListModel<String>();
	private JList<String> lstCapteur = new JList<String>(modelCapteur);
	private JScrollPane spLstCapteur = new JScrollPane(lstCapteur);	
	
	private JButton btnTriFiltrer = new JButton("Filtrer");
	
	
	public FenEntreeDigitale() {
		super();
		ctn.open();
		build();
		remplirListeCapteur();
		lectureEquipement();
		lectureTypeMateriel();
		lecturePosteTechnique();
		lectureZoneSubstitution();
		lectureService();
		lectureAlarmeService();
		tmrTpsReel.start();
	}

	public void build() {
		
	    this.setTitle("Entrée digitale");
	    this.setSize(1350, 800);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		

	    lstCapteur.addListSelectionListener(this);
	    
		pnlHaut.setTitreEcran("Entrée digitale");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlGauche.setBackground(new Color(200, 130, 66));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	    //Place la barre de séparation a 320 px
		splCorps.setDividerLocation(320);
	    
		GridBagConstraints gbc = new GridBagConstraints();

	    // =================== Corps de l'écran =============================================
	    pnlCorps.setLayout(new GridBagLayout());

	    btnCourbe.addActionListener(this);
	    btnAjouter.addActionListener(this);
	    
	    pnlCommande.add(btnAjouter);
	    pnlCommande.add(btnCourbe);
	    pnlCommande.setBackground(EFS_BLEU);
	    pnlCommande.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    
	    btnModifierDescription.addActionListener(this);
	    btnModifierEquipement.addActionListener(this);
	    btnModifierPosteTechnique.addActionListener(this);
	    btnModifierTypeMateriel.addActionListener(this);
	    btnModifierAlarme.addActionListener(this);
	    btnModifierInhibition.addActionListener(this);
	    btnModifierOnOff.addActionListener(this);
	    btnModifierSeuilTempo.addActionListener(this);
	    btnModifierZoneSubstitution.addActionListener(this);
	    btnModifierService.addActionListener(this);
	    btnModifierContact.addActionListener(this);
	    
	    btnModifierDescription.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierDescription.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierEquipement.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierEquipement.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierPosteTechnique.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierPosteTechnique.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierTypeMateriel.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierTypeMateriel.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierAlarme.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierAlarme.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierOnOff.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierOnOff.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierSeuilTempo.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierSeuilTempo.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierZoneSubstitution.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierZoneSubstitution.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierService.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierService.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierContact.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierContact.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    
	    btnCourbe.setPreferredSize(btnAjouter.getPreferredSize());
	    btnCourbe.setMinimumSize(btnAjouter.getMinimumSize());
	    
	    
	    bgAlarme.add(optAlarmeAlert);
	    bgAlarme.add(optAlarmeDefaut);
	    bgAlarme.add(optAlarmeEtat);
	    bgAlarme.add(optAlarmeRien);
	    pnlAlarme.add(optAlarmeAlert);
	    pnlAlarme.add(optAlarmeDefaut);
	    pnlAlarme.add(optAlarmeEtat);
	    pnlAlarme.add(optAlarmeRien);
	    pnlAlarme.setOpaque(false);
	    pnlGestionAlarme.setOpaque(false);
	    optAlarmeAlert.setOpaque(false);
	    optAlarmeDefaut.setOpaque(false);
	    optAlarmeEtat.setOpaque(false);
	    optAlarmeRien.setOpaque(false);

	    bgZoneMaintenance.add(optZoneMaintenanceOui);
	    bgZoneMaintenance.add(optZoneMaintenanceNon);
	    pnlZoneMaintenance.add(optZoneMaintenanceOui);
	    pnlZoneMaintenance.add(optZoneMaintenanceNon);
	    pnlZoneMaintenance.setOpaque(false);
	    optZoneMaintenanceOui.setOpaque(false);
	    optZoneMaintenanceNon.setOpaque(false);
	    optZoneMaintenanceNon.setEnabled(false);

	    bgOnOff.add(optOn);
	    bgOnOff.add(optOff);
	    pnlOnOff.add(optOn);
	    pnlOnOff.add(optOff);
	    pnlOnOff.setOpaque(false);
	    optOn.setOpaque(false);
	    optOff.setOpaque(false);

	    bgOnOffEnCours.add(optOnEnCours);
	    bgOnOffEnCours.add(optOffEnCours);
	    pnlOnOffEnCours.add(optOnEnCours);
	    pnlOnOffEnCours.add(optOffEnCours);
	    pnlOnOffEnCours.setOpaque(false);
	    optOnEnCours.setOpaque(false);
	    optOffEnCours.setOpaque(false);
	    optOnEnCours.setSelected(true);
	    optOnEnCours.setForeground(Color.red);
	    
	    spLstCapteur.setPreferredSize(new Dimension(300, 300));
	    spLstCapteur.setMinimumSize(new Dimension(300, 300));

	    gbc.insets = new Insets(4, 5, 0, 0);

	    int titreWeightX = 20;
	    int saisieWeightX = 20;
	    int modificationWeightX = 20;
	    
	    // ------- Titre Nom Capteur -------- ----------------------
	    gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1; // gbc.weightx = 40;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblNom, gbc);
	    // ------- Saisie Nom Capteur ------------------------------
	    gbc.gridx = 1; gbc.gridy = 0; // gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtNom, gbc);
	    
	    // ------- Titre Description -------------------------------
	    gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblDescription, gbc);
	    // ------- Saisie description ------------------------------
	    gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtDescription, gbc);
	    // ------- Modification description ------------------------
	    gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierDescription, gbc);
	    
	    // ------- Titre equipement --------------------------------
	    gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblEquipement, gbc);
	    // ------- Saisie equipement -------------------------------
	    gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbEquipement, gbc);
	    // ------- Modification equipement -------------------------
	    gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierEquipement, gbc);

	    
	    // ------- Titre Poste Technique ---------------------------
	    gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblPosteTechnique, gbc);
	    // ------- Saisie Poste Technique --------------------------
	    gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbPosteTechnique, gbc);
	    // ------- Modification Poste Technique --------------------
	    gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierPosteTechnique, gbc);
	    
	    // ------- Titre Type Matériel -----------------------------
	    gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblTypeMateriel, gbc);
	    // ------- Saisie Type Matériel ----------------------------
	    gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbTypeMateriel, gbc);
	    // ------- Modification Type Matériel ----------------------
	    gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierTypeMateriel, gbc);

	    // ------- pnlAlarme ------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlGestionAlarme.add(pnlAlarme, gbc);
	    // ------- cmbAlarmeService ------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER; 
	    pnlGestionAlarme.add(cmbAlarmeService, gbc);
	    // ------- Titre Alarme ------------------------------------
	    gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblAlarme, gbc);
	    // ------- Saisie Alarme -----------------------------------
	    gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor= GridBagConstraints.CENTER;
	    pnlCorps.add(pnlGestionAlarme, gbc);
	    // ------- Modification Alarme -----------------------------
	    gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierAlarme, gbc);
	    
	    // ------- Titre inhibition --------------------------------
	    gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblZoneMaintenance, gbc);
	    // ------- Saisie inhibition -------------------------------
	    gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START;
	    pnlCorps.add(pnlZoneMaintenance, gbc);
	    // ------- Modification Inhibition -------------------------
	    gbc.gridx = 2; gbc.gridy = 6; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierInhibition, gbc);

	    // ------- Titre On/Off ------------------------------------
	    gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblOnOff, gbc);
	    // ------- Saisie On/Off -----------------------------------
	    gbc.gridx = 1; gbc.gridy = 7; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START;
	    pnlCorps.add(pnlOnOff, gbc);
	    // ------- Modification On/Off -----------------------------
	    gbc.gridx = 2; gbc.gridy = 7; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierOnOff, gbc);
	    
	    // ------- Titre Temporisation  ----------------------------
	    gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblTempo, gbc);
	    // ------- Saisie Temporisation ----------------------------
	    gbc.gridx = 1; gbc.gridy = 8; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtTempo, gbc);
	    // ------- Modification Temporisation ----------------------
	    gbc.gridx = 2; gbc.gridy = 8; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierSeuilTempo, gbc);
	    
	    // ------- Titre En cours ----------------------------------
	    gbc.gridx = 0; gbc.gridy = 9; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_END; 
	    pnlCorps.add(lblOnOffEnCours, gbc);
	    // ------- Saisie En cours ---------------------------------
	    gbc.gridx = 1; gbc.gridy = 9; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START;
	    pnlCorps.add(pnlOnOffEnCours, gbc);

	    // ------- Titre Zone substitution--------------------------
	    gbc.gridx = 0; gbc.gridy = 10; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblZoneSubstitution, gbc);
	    // ------- Saisie Zone substitution ------------------------
	    gbc.gridx = 1; gbc.gridy = 10; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbZoneSubstitution, gbc);
	    // ------- Modification Zone substitution ------------------
	    gbc.gridx = 2; gbc.gridy = 10; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierZoneSubstitution, gbc);

	    // ------- Titre Service -----------------------------------
	    gbc.gridx = 0; gbc.gridy = 11; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblService, gbc);
	    // ------- Saisie Service ----------------------------------
	    gbc.gridx = 1; gbc.gridy = 11; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbService, gbc);
	    // ------- Modification Service ----------------------------
	    gbc.gridx = 2; gbc.gridy = 11; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierService, gbc);

	    // ------- Titre Contact -----------------------------------
	    gbc.gridx = 0; gbc.gridy = 12; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblContact, gbc);
	    // ------- Saisie Contact ----------------------------------
	    gbc.gridx = 1; gbc.gridy = 12; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtContact, gbc);
	    // ------- Modification Contact ----------------------------
	    gbc.gridx = 2; gbc.gridy = 12; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierContact, gbc);
	    
	    // ---- Commandes ------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 20; // gbc.weightx = 100;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(pnlCommande, gbc);	    
	    // ==================================================================================

	    // =================== Gauche de l'écran ============================================
	    btnTriFiltrer.addActionListener(this);
	    
	    pnlGauche.setLayout(new GridBagLayout());
	    gbc.insets = new Insets(4, 5, 0, 5);
	    
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlGauche.add(lblServiceGauche, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(cmbTriService, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlGauche.add(lblPosteTechniqueGauche, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 3;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(cmbTriPosteTechnique, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 4;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlGauche.add(lblEquipementGauche, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 5;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(cmbTriEquipement, gbc);
	    // --------------------------------------------------	    
	    gbc.gridx = 0; gbc.gridy = 6;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlGauche.add(lblTypeMaterielGauche, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 7;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(cmbTriTypeMateriel, gbc);
	    // --------------------------------------------------	    
	    gbc.gridx = 0; gbc.gridy = 8;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlGauche.add(lblCapteurGauche, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 9;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(spLstCapteur, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 10;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(btnTriFiltrer, gbc);
	    // ==================================================================================	    
	    
	} // Fin build

	private void remplirListeCapteur() {
		String strSql = "";
		int index = 0;
		
		// Lecture données
		tbCapteur.clear();
		modelCapteur.removeAllElements();
		strSql = "SELECT Capteur.*, EntreeDigitale.* "
				+ "FROM (EntreeDigitale LEFT JOIN Capteur ON EntreeDigitale.idCapteur = Capteur.idCapteur)"
				+ " WHERE TypeCapteur = 2";
		// Service
		index = cmbTriService.getSelectedIndex();
		if (index > 0) {
			strSql += " AND idService = " + tbTriService.get(index - 1).getId();
		}
		// Poste technique
		index = cmbTriPosteTechnique.getSelectedIndex();
		if (index > 0) {
			strSql += " AND idPosteTechnique = " + tbTriPosteTechnique.get(index - 1).getId();
		}
		// Equipement
		index = cmbTriEquipement.getSelectedIndex();
		if (index > 0) {
			strSql += " AND idEquipement = " + tbTriEquipement.get(index - 1).getId();
		}
		// Type Materiel
		index = cmbTriTypeMateriel.getSelectedIndex();
		if (index > 0) {
			strSql += " AND idTypeMateriel = " + tbTriTypeMateriel.get(index - 1).getId();
		}
		
		strSql += " ORDER BY VoieApi";
		
		ResultSet result = ctn.lectureData(strSql);

		// Remplissage de la liste
		try {
			while(result.next()) {
				tbCapteur.add(new StructEntreeDigitale(result.getLong("idEntreeDigitale"), result.getString("Nom"), result.getString("Description")
						, result.getLong("Tempo"), result.getInt("NoNf"), result.getInt("Alarme"), result.getLong("idService"), result.getLong("idZoneSubstitution")
						, result.getLong("idCapteur"), result.getLong("idEquipement"), result.getLong("idPosteTechnique"), result.getLong("idTypeMateriel")
						, result.getInt("voieApi"), result.getString("Contact"), result.getLong("idAlarmeService")));
				modelCapteur.addElement(result.getString("Nom") + " - " + result.getString("Description"));
			}
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
	} // Fin RemplirListeCapteur

	private void lectureEquipement() {
		int index = 0;
		// RAZ combo
		cmbEquipement.removeAllItems();
		tbEquipement.clear();
		tbTriEquipement.clear();
		
		ResultSet result = ctn.lectureData("SELECT * FROM Equipement ORDER BY Nom");
		index = 0;
		try {
			while(result.next()) {
				tbEquipement.add(new StructEquipement(result.getLong("idEquipement"), result.getString("Nom"), 
						result.getString("Description"), result.getString("Nom"), -1, -1, -1));
				cmbEquipement.addItem(tbEquipement.get(index).getNom());
				tbTriEquipement.add(new StructEquipement(result.getLong("idEquipement"), result.getString("Nom"), 
						result.getString("Description"), result.getString("Nom"), -1, -1, -1));
				cmbTriEquipement.addItem(tbTriEquipement.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbEquipement.setSelectedIndex(-1);
		
	} // Fin lectureEquipement			

	private void lectureTypeMateriel() {
		int index = 0;
		// RAZ combo
		cmbTypeMateriel.removeAllItems();
		tbTypeMateriel.clear();
		tbTriTypeMateriel.clear();
		// Connection base
		
		// Lecture données
		
		ResultSet result = ctn.lectureData("SELECT * FROM TypeMateriel ORDER BY Nom");
		index = 0;
		// Remplissage tableau et combo PosteTechnique
		try {
			while(result.next()) {
				tbTypeMateriel.add(new StructTypeMateriel(result.getLong("idTypeMateriel"), result.getString("Nom"), result.getString("Description")));
				cmbTypeMateriel.addItem(tbTypeMateriel.get(index).getNom());
				tbTriTypeMateriel.add(new StructTypeMateriel(result.getLong("idTypeMateriel"), result.getString("Nom"), result.getString("Description")));
				cmbTriTypeMateriel.addItem(tbTriTypeMateriel.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbTypeMateriel.setSelectedIndex(-1);
		// Fermeture base
		
	}	// Fin LectureTypeMateriel	

	private void lecturePosteTechnique() {
		int index = 0;
		// RAZ combo
		cmbPosteTechnique.removeAllItems();
		tbPosteTechnique.clear();
		tbTriPosteTechnique.clear();
		
		ResultSet result = ctn.lectureData("SELECT * FROM PosteTechnique ORDER BY Nom");
		index = 0;
		try {
			while(result.next()) {
				tbPosteTechnique.add(new StructPosteTechnique(result.getLong("idPosteTechnique"), result.getString("Nom"), 
						result.getString("Description")));
				cmbPosteTechnique.addItem(tbPosteTechnique.get(index).getNom());
				tbTriPosteTechnique.add(new StructPosteTechnique(result.getLong("idPosteTechnique"), result.getString("Nom"), 
						result.getString("Description")));
				cmbTriPosteTechnique.addItem(tbTriPosteTechnique.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbPosteTechnique.setSelectedIndex(-1);
		
	} // Fin lecturePosteTechnique		

	private void lectureZoneSubstitution() {
		int index = 0;
		// RAZ combo
		cmbZoneSubstitution.removeAllItems();
		tbZoneSubstitution.clear();
		// Connection base
		
		// Lecture données
		
		ResultSet result = ctn.lectureData("SELECT * FROM ZoneSubstitution ORDER BY Nom");
		index = 0;
		// Remplissage tableau et combo ZoneSubstitution
		try {
			while(result.next()) {
				tbZoneSubstitution.add(new StructZoneSubstitution(result.getLong("idZoneSubstitution"), result.getString("Nom"), result.getString("Description")));
				cmbZoneSubstitution.addItem(tbZoneSubstitution.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbZoneSubstitution.setSelectedIndex(-1);
		// Fermeture base
		
	} // Fin LectureZoneSubstitution		

	private void lectureService() {
		int index = 0;
		// Lecture données
		ResultSet result = ctn.lectureData("SELECT * FROM Service ORDER BY Nom");
		index = 0;
		// Remplissage tableau et combo PosteTechnique
		try {
			while(result.next()) {
				tbTriService.add(new StructService(result.getLong("idService"), result.getString("Nom"), result.getString("Description")));
				cmbTriService.addItem(tbTriService.get(index).getNom());
				tbService.add(new StructService(result.getLong("idService"), result.getString("Nom"), result.getString("Description")));
				cmbService.addItem(tbTriService.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}		

	/**
	 * Remplit la comboBox cmbAlarmeService
	 * @since 03/05/2018
	 */
	private void lectureAlarmeService() {
		for(int i = 0; i < tbAlarmeService.size(); i++) {
			cmbAlarmeService.addItem(tbAlarmeService.get(i).getNomService());
		}
		cmbAlarmeService.setSelectedIndex(-1);
	}	
	
	private void selectionZoneSubstitution(long index) {
		for(int i = 0; i < tbZoneSubstitution.size() ; i++) {
			if (tbZoneSubstitution.get(i).getId() == index) {
				cmbZoneSubstitution.setSelectedIndex(i);
			}
		}
	} // Fin SelectionZoneSubstitution

	private void selectionEquipement(long index) {
		for(int i = 0; i < tbEquipement.size() ; i++) {
			if (tbEquipement.get(i).getId() == index) {
				cmbEquipement.setSelectedIndex(i);
			}
		}
	} // Fin SelectionEquipement

	private void selectionPosteTechnique(long index) {
		for(int i = 0; i < tbPosteTechnique.size() ; i++) {
			if (tbPosteTechnique.get(i).getId() == index) {
				cmbPosteTechnique.setSelectedIndex(i);
			}
		}
	} // Fin selectionPosteTechnique

	private void selectionTypeMateriel(long index) {
		for(int i = 0; i < tbTypeMateriel.size() ; i++) {
			if (tbTypeMateriel.get(i).getId() == index) {
				cmbTypeMateriel.setSelectedIndex(i);
			}
		}
	} // Fin selectionTypeMateriel	

	private void selectionService(long index) {
		for(int i = 0; i < tbService.size() ; i++) {
			if (tbService.get(i).getId() == index) {
				cmbService.setSelectedIndex(i);
			}
		}
	} // Fin selectionService		
	
	private void gestionModifierInhibition() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		boolean blInhibition = false;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();

		if(optZoneMaintenanceOui.isSelected()) {
			blInhibition = true;
		} else {
			blInhibition = false;
		}
		if (blInhibition){
			// Demande confirmation
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " dans la zone de maintenance ?";
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Regardez si pas déjà dans la table
				boolean blDejaInhibition = false;
				blDejaInhibition = AE_Fonctions.trouveIdDansTable("Inhibition", "idCapteur", idCapteur);
				
				if (!blDejaInhibition) {
					// Motif 
					String strReponse = (String)JOptionPane.showInputDialog(this, "Choisir un motif de mise en zone maintenance : ", "Prise en compte", JOptionPane.QUESTION_MESSAGE, null, AE_Fonctions.renvoiListe("RaisonInhibition", "Raison"), null);
					if(strReponse != null) {
						long idRaisonInhibition = 0;
						// Rechercher l'index de RaisonInhibition
						idRaisonInhibition = AE_Fonctions.trouveIndex("RaisonInhibition", "Raison", strReponse, "idRaisonInhibition");
						// Ecriture dans la base

						strSql = "INSERT INTO Inhibition (idCapteur, DateInhibition, idUtilisateur, idRaisonInhibition) VALUES(" + idCapteur + ", sysdate, " + EFS_Client_Variable.idUtilisateur + ", " + idRaisonInhibition + ")";
						ctn.fonctionSql(strSql);
						strSql = "UPDATE Capteur SET Inhibition = 1 WHERE idCapteur = " + idCapteur;
						ctn.fonctionSql(strSql);
						
						// Ajout dans journal
			        	msgJournal = "Passage en zone maintenance du capteur : " + nomCapteur + "  " + descriptionCapteur;
						AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_INHIBITION_AJOUT);
						// Prevenir les programmes maitre
				        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_MAINTENANCE);
						// Réaffichage capteur
					} // fin choix Inhibition
				} // fin if
				afficheCapteur();
			}
		} else {
			msgConfirmation = "Voulez vous retirer " + nomCapteur + " de la zone de maintenance ?";
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base

				// === Modification 27-05-2016 ===
				// Suppression dans la table Alarme en Cours
        		strSql = "DELETE FROM AlarmeEnCours WHERE idCapteur = " + idCapteur;
        		ctn.fonctionSql(strSql);
				// === Fin modification 27-05-2016 ===

				strSql = "DELETE FROM Inhibition WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				strSql = "UPDATE Capteur SET Inhibition = 0 WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Ajout dans journal
	        	msgJournal = "Retrait de la zone maintenance du capteur : " + nomCapteur + "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_INHIBITION_RETRAIT);
				// Prevenir les programmes maitre
				AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_INHIBITION);
				// Réaffichage capteur
				afficheCapteur();
			}
		}
	
	} // fin gestionModifierInhibition()
	
	private void gestionModifierEquipement() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;

		long idEquipement;
		long idNewEquipement;
		String equipement = "";
		String newEquipement = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		
		// Nouvelle
		int indexNewEquipement = cmbEquipement.getSelectedIndex();
		idNewEquipement = tbEquipement.get(indexNewEquipement).getId();
		newEquipement = (String) cmbEquipement.getSelectedItem();
		
		// Ancienne
		idEquipement = tbCapteur.get(index).getIdEquipement();
		
		strSql = "SELECT * FROM Equipement WHERE idEquipement = " + idEquipement;
		ResultSet result = ctn.lectureData(strSql);				
		try {
			if(result.next()) {
				equipement = result.getString("Nom");
			} else {
				equipement = "???";
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException eSql) {
			eSql.printStackTrace();
		} // Fin try
				
		
		// Demande confirmation
		msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " l'équipement de : " + equipement + " pour " + newEquipement;
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		if(blRetour) {
			// Ecriture dans la base
			
			strSql = "UPDATE Capteur SET idEquipement = " + idNewEquipement + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			
			// Chgt dans tableau
			tbCapteur.get(index).setIdEquipement(idNewEquipement);
			// Ajout dans journal
        	msgJournal = "Passage équipement de " + equipement + " à " + newEquipement + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_EQUIPEMENT);
			// Réaffichage capteur
			afficheCapteur();
		}
	} // fin gestionModifierEquipement
	
	private void gestionModifierPosteTechnique() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;

		long idPosteTechnique;
		long idNewPosteTechnique;
		String posteTechnique = "";
		String newPosteTechnique = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		
		// Nouvelle
		int indexNewPosteTechnique = cmbPosteTechnique.getSelectedIndex();
		idNewPosteTechnique = tbPosteTechnique.get(indexNewPosteTechnique).getId();
		newPosteTechnique = (String) cmbPosteTechnique.getSelectedItem();
		
		// Ancienne
		idPosteTechnique = tbCapteur.get(index).getIdPosteTechnique();
		
		strSql = "SELECT * FROM PosteTechnique WHERE idPosteTechnique = " + idPosteTechnique;
		ResultSet result = ctn.lectureData(strSql);				
		try {
			if(result.next()) {
				posteTechnique = result.getString("Nom");
			} else {
				posteTechnique = "???";
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException eSql) {
			eSql.printStackTrace();
		} // Fin try
				
		
		// Demande confirmation
		msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le poste technique de : " + posteTechnique + " pour " + newPosteTechnique;
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		if(blRetour) {
			// Ecriture dans la base
			
			strSql = "UPDATE Capteur SET idPosteTechnique = " + idNewPosteTechnique + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			
			// Chgt dans tableau
			tbCapteur.get(index).setIdPosteTechnique(idNewPosteTechnique);
			// Ajout dans journal
        	msgJournal = "Passage poste technique de " + posteTechnique + " à " + newPosteTechnique + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_POSTE_TECHNIQUE);
			// Réaffichage capteur
			afficheCapteur();
		}
	} // fin gestionModifierPosteTechnique
	
	private void gestionModifierTypeMateriel() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;

		long idTypeMateriel;
		long idNewTypeMateriel;
		String typeMateriel = "";
		String newTypeMateriel = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		
		// Nouvelle
		int indexNewTypeMateriel = cmbTypeMateriel.getSelectedIndex();
		idNewTypeMateriel = tbTypeMateriel.get(indexNewTypeMateriel).getId();
		newTypeMateriel = (String) cmbTypeMateriel.getSelectedItem();
		
		// Ancienne
		idTypeMateriel = tbCapteur.get(index).getIdTypeMateriel();
		
		strSql = "SELECT * FROM TypeMateriel WHERE idTypeMateriel = " + idTypeMateriel;
		ResultSet result = ctn.lectureData(strSql);				
		try {
			if(result.next()) {
				typeMateriel = result.getString("Nom");
			} else {
				typeMateriel = "???";
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException eSql) {
			eSql.printStackTrace();
		} // Fin try
				
		
		// Demande confirmation
		msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le type matériel de : " + typeMateriel + " pour " + newTypeMateriel;
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		if(blRetour) {
			// Ecriture dans la base
			
			strSql = "UPDATE Capteur SET idTypeMateriel = " + idNewTypeMateriel + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			
			// Chgt dans tableau
			tbCapteur.get(index).setIdTypeMateriel(idNewTypeMateriel);
			// Ajout dans journal
        	msgJournal = "Passage type matériel de " + typeMateriel + " à " + newTypeMateriel + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_TYPE_MATERIEL);
			// Réaffichage capteur
			afficheCapteur();
		}
	} // fin gestionModifierTypeMateriel

	private void gestionModifierZoneSubstitution() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;

		long idZoneSubstitution;
		long idNewZoneSubstitution;
		String zoneSubstitution = "";
		String newZoneSubstitution = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		
		// Nouvelle
		int indexNewZoneSubstitution = cmbZoneSubstitution.getSelectedIndex();
		idNewZoneSubstitution = tbZoneSubstitution.get(indexNewZoneSubstitution).getId();
		newZoneSubstitution = (String) cmbZoneSubstitution.getSelectedItem();
		
		// Ancienne
		idZoneSubstitution = tbCapteur.get(index).getIdZoneSubstitution();
		
		strSql = "SELECT * FROM ZoneSubstitution WHERE idZoneSubstitution = " + idZoneSubstitution;
		ResultSet result = ctn.lectureData(strSql);				
		try {
			if(result.next()) {
				zoneSubstitution = result.getString("Nom");
			} else {
				zoneSubstitution = "???";
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException eSql) {
			eSql.printStackTrace();
		} // Fin try
				
		
		// Demande confirmation
		msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " la zone de substitution de : " + zoneSubstitution + " pour " + newZoneSubstitution;
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		if(blRetour) {
			// Ecriture dans la base
			
			strSql = "UPDATE Capteur SET idZoneSubstitution = " + idNewZoneSubstitution + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			
			// Chgt dans tableau
			tbCapteur.get(index).setIdZoneSubstitution(idNewZoneSubstitution);
			// Ajout dans journal
        	msgJournal = "Passage zone de substitution de " + zoneSubstitution + " à " + newZoneSubstitution + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_ZONE_SUBSTITUTION);
			// Réaffichage capteur
			afficheCapteur();
		}
	} // fin gestionModifierZoneSubstitution
		
	private void gestionModifierAlarme() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		int newAlarme = AE_Constantes.ALARME_ALERT;
		int oldAlarme = AE_Constantes.ALARME_RIEN;
		
		long idAlarmeService = tbAlarmeService.get(cmbAlarmeService.getSelectedIndex()).getIdAlarmeService();
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();

		// Ancienne valeur
		oldAlarme = tbCapteur.get(index).getAlarme();
		
		
		if(optAlarmeAlert.isSelected()) {
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " en Alarme";
			newAlarme = ALARME_ALERT;
		}
		if(optAlarmeDefaut.isSelected()) {
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " en Défaut";
			newAlarme = ALARME_DEFAUT;
		}
		if(optAlarmeEtat.isSelected()) {
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " en Etat";
			newAlarme = ALARME_ETAT;
		}
		if(optAlarmeRien.isSelected()) {
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " en Rien";
			newAlarme = ALARME_RIEN;
		}
		
		msgConfirmation += " et en alarme pour le service : " + tbAlarmeService.get(cmbAlarmeService.getSelectedIndex()).getNomService() + " ?";
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		
		if(blRetour) {
			strSql = "UPDATE Capteur SET Alarme = " + newAlarme + ", idAlarmeService = " + idAlarmeService + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			// Chgt dans tableau
			tbCapteur.get(index).setAlarme(newAlarme);
			tbCapteur.get(index).setIdAlarmeService(idAlarmeService);
			// Ajout dans journal
        	msgJournal = "Passage Alarme de " + oldAlarme + " à " + newAlarme + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_ALARME);
			// Prevenir les programmes maitre
	        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_ALARME);
			// Réaffichage capteur
			afficheCapteur();
		} else {
			afficheCapteur();
		}
	} // fin gestionModifierAlarme()	
	
	private void gestionModifierSeuilTempo() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double seuilTempo = 0D;
		double newSeuilTempo = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		seuilTempo = (double) tbCapteur.get(index).getTempo();
		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtTempo.getText())){
			newSeuilTempo = Double.parseDouble(txtTempo.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Seuil Tempo de : " + seuilTempo + " pour " + newSeuilTempo;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeDigitale SET Tempo = " + (newSeuilTempo) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setTempo((long)(newSeuilTempo));
				// Ajout dans journal
	        	msgJournal = "Passage du seuil Tempo de " + seuilTempo	+ " à " + newSeuilTempo + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_SEUIL_TEMPO);
				// Prevenir les programmes maitre
//				AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_SEUIL_TEMPO);
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_TEMPO);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierSeuilTempo()		
	
	private void gestionModifierNoNf() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		int newNoNf = 0;
		int oldNoNf = 0;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();

		// Ancienne valeur
		oldNoNf = tbCapteur.get(index).getNoNf();
		
		
		if(optOn.isSelected()) {
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " en NO ?";
			newNoNf = CAPTEUR_DIGITAL_NO;
		}
		if(optOff.isSelected()) {
			msgConfirmation = "Voulez vous mettre " + nomCapteur + " en NF ?";
			newNoNf = CAPTEUR_DIGITAL_NF;
		}
		
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		
		if(blRetour) {
			strSql = "UPDATE EntreeDigitale SET NoNf = " + newNoNf + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			// Chgt dans tableau
			tbCapteur.get(index).setNoNf(newNoNf);
			// Ajout dans journal
        	msgJournal = "Passage NO/NF de " + oldNoNf + " à " + newNoNf + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_NONF);
			// Prevenir les programmes maitre
	        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_NO_NF);
			// Réaffichage capteur
			afficheCapteur();
		} else {
			afficheCapteur();
		}
	} // fin gestionModifierAlarme()		
	
	private void gestionModifierDescription() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		String newDescription = "";
		String oldDescription = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();

		// Ancienne valeur
		oldDescription = tbCapteur.get(index).getDescription();
		newDescription = txtDescription.getText();
		
		msgConfirmation = "Voulez vous passez la description de " + oldDescription + " à " + newDescription
				+ " pour le capteur " + nomCapteur ;
		
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		
		if(blRetour) {
			strSql = "UPDATE Capteur SET Description = '" + newDescription + "' WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			// Chgt dans tableau
			tbCapteur.get(index).setDescription(newDescription);
			// Ajout dans journal
        	msgJournal = "Passage Description de " + oldDescription + " à " + newDescription + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_DESCRIPTION);
			// Réaffichage capteur
			afficheCapteur();
		} else {
			afficheCapteur();
		}		
	}
	
	private void appelFenetreCourbe() {
		int index = lstCapteur.getSelectedIndex();
		if(index !=  -1) {
	        int idCapteur = (int) tbCapteur.get(index).getIdCapteur(); 
	        
			FenCourbe fenCourbe = new FenCourbe();
			fenCourbe.setIdCapteur(idCapteur);
			fenCourbe.traceCourbe();
			fenCourbe.setVisible(true);
			fenCourbe.requestFocusInWindow();
		} else {
			FenCourbe fenCourbe = new FenCourbe();
			fenCourbe.setVisible(true);
			fenCourbe.requestFocusInWindow();
		}
	}
		
	private void lectureVoieTpsReel() {
		if(voieApiAffichee > 0) {
			int valeurLue = 0;
			
			valeurLue = EFS_Client_Variable.tbValeurDI[voieApiAffichee - 1];
			if(valeurLue == CAPTEUR_DIGITAL_NO) {
				optOnEnCours.setSelected(true);
			} else {
				optOffEnCours.setSelected(true);
			}
		}
	} // fin lectureVoieTpsReel()

	private void videFicheCapteur() {
		txtNom.setText("");
		txtDescription.setText("");
		txtTempo.setText("");
		txtContact.setText("");

		cmbTypeMateriel.setSelectedIndex(-1);
		cmbZoneSubstitution.setSelectedIndex(-1);
		cmbEquipement.setSelectedIndex(-1);
		cmbPosteTechnique.setSelectedIndex(-1);
		cmbService.setSelectedIndex(-1);
		
		optAlarmeAlert.setSelected(false);
		optAlarmeDefaut.setSelected(false);
		optAlarmeEtat.setSelected(false);
		optAlarmeRien.setSelected(false);
		optZoneMaintenanceOui.setSelected(false);
		optZoneMaintenanceNon.setSelected(false);
		optOn.setSelected(false);
		optOff.setSelected(false);
		optOnEnCours.setSelected(false);
		optOffEnCours.setSelected(false);
		
	} // fin videFicheCapteur()
	
	private void afficheCapteur() {
		long idCapteur = -1;
		long seuilTempo = 0;
		String strSql = "";
		
		videFicheCapteur();
		int index = lstCapteur.getSelectedIndex();
		if(index !=  -1) {
			txtNom.setText(tbCapteur.get(index).getNom());
			txtDescription.setText(tbCapteur.get(index).getDescription());
			seuilTempo = tbCapteur.get(index).getTempo();
			idCapteur = tbCapteur.get(index).getIdCapteur();

			voieApiAffichee = tbCapteur.get(index).getVoieApi();
			
			txtTempo.setText(String.valueOf(seuilTempo));
			txtContact.setText(tbCapteur.get(index).getContact());

			selectionZoneSubstitution(tbCapteur.get(index).getIdZoneSubstitution());
			selectionEquipement(tbCapteur.get(index).getIdEquipement());
			selectionPosteTechnique(tbCapteur.get(index).getIdPosteTechnique());
			selectionTypeMateriel(tbCapteur.get(index).getIdTypeMateriel());
			selectionService(tbCapteur.get(index).getIdService());
			
			cmbAlarmeService.setSelectedIndex(tbAlarmeService.trouverIndex(tbCapteur.get(index).getIdAlarmeService()));

			// Alarme
			switch(tbCapteur.get(index).getAlarme()) {
			case AE_Constantes.ALARME_ALERT:
				optAlarmeAlert.setSelected(true);
				break;
			case AE_Constantes.ALARME_DEFAUT:
				optAlarmeDefaut.setSelected(true);
				break;
			case AE_Constantes.ALARME_ETAT:
				optAlarmeEtat.setSelected(true);
				break;
			case AE_Constantes.ALARME_RIEN:
				optAlarmeRien.setSelected(true);
				break;
			default:
				optAlarmeAlert.setSelected(false);
			} // fin switch
			
			// NoNf
			if(tbCapteur.get(index).getNoNf() == CAPTEUR_DIGITAL_NO) {
				optOn.setSelected(true);
			} else if(tbCapteur.get(index).getNoNf() == CAPTEUR_DIGITAL_NF) {
				optOff.setSelected(true);
			} else {
				optOn.setSelected(false);
				optOff.setSelected(false);
			}
			
			// Zone maintenance (inhibition)
			
			strSql = "SELECT * FROM Inhibition WHERE idCapteur = " + idCapteur;
			ResultSet result = ctn.lectureData(strSql);				
			try {
				if(result.next()) {
					optZoneMaintenanceOui.setSelected(true);
				} else {
					optZoneMaintenanceNon.setSelected(true);

				}
				result.close();
				ctn.closeLectureData();
			} catch (SQLException eSql) {
				eSql.printStackTrace();
			} // Fin try
			
		} // Fin if 
		
	} // fin afficheCapteur	
	
	private void gestionModifierContact() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		String newContact = "";
		String oldContact = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();

		// Ancienne valeur
		oldContact = tbCapteur.get(index).getContact();
		newContact = txtContact.getText();
		
		msgConfirmation = "Voulez vous passez le contact de " + oldContact + " à " + newContact
				+ " pour le capteur " + nomCapteur ;
		
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		
		if(blRetour) {
			strSql = "UPDATE Capteur SET Contact = '" + newContact + "' WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			// Chgt dans tableau
			tbCapteur.get(index).setContact(newContact);
			// Ajout dans journal
        	msgJournal = "Passage Contact de " + oldContact + " à " + newContact + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_CONTACT);
			// Réaffichage capteur
			afficheCapteur();
		} else {
			afficheCapteur();
		}		
	}	

	private void gestionModifierService() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;

		long idService;
		long idNewService;
		String service = "";
		String newService = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		
		// Nouvelle
		int indexNewService = cmbService.getSelectedIndex();
		idNewService = tbService.get(indexNewService).getId();
		newService = (String) cmbService.getSelectedItem();
		
		// Ancienne
		idService = tbCapteur.get(index).getIdService();
		
		strSql = "SELECT * FROM Service WHERE idService = " + idService;
		ResultSet result = ctn.lectureData(strSql);				
		try {
			if(result.next()) {
				service = result.getString("Nom");
			} else {
				service = "???";
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException eSql) {
			eSql.printStackTrace();
		} // Fin try
				
		
		// Demande confirmation
		msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le service de : " + service + " pour " + newService;
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		if(blRetour) {
			// Ecriture dans la base
			
			strSql = "UPDATE Capteur SET idService = " + idNewService + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			
			// Chgt dans tableau
			tbCapteur.get(index).setIdService(idNewService);
			// Ajout dans journal
        	msgJournal = "Passage service de " + service + " à " + newService + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_SERVICE);
			// Réaffichage capteur
			afficheCapteur();
		}
	} // fin gestionModifierService	
	
	private void gestionAjoutCapteur() {
		// regarder si exite déjà
		String strSql = new String();
		
		if(!AE_Fonctions.enregistrementExiste("Capteur", "Nom", txtNom.getText())) {
			if(AE_Fonctions.messageConfirmation(this, "Voulez vous créer cette voie  : " + txtNom.getText())) {
				
				long idNewEquipement;
				int indexNewEquipement = cmbEquipement.getSelectedIndex();
				if(indexNewEquipement != -1)
					idNewEquipement = tbEquipement.get(indexNewEquipement).getId();
				else
					idNewEquipement = -1;

				long idNewPosteTechnique;
				int indexNewPosteTechnique = cmbPosteTechnique.getSelectedIndex();
				if(indexNewPosteTechnique != -1)
					idNewPosteTechnique = tbPosteTechnique.get(indexNewPosteTechnique).getId();
				else
					idNewPosteTechnique = -1;
				
				long idNewTypeMateriel;
				int indexNewTypeMateriel = cmbTypeMateriel.getSelectedIndex();
				if(indexNewTypeMateriel != -1)
					idNewTypeMateriel = tbTypeMateriel.get(indexNewTypeMateriel).getId();
				else
					idNewTypeMateriel = -1;
					
				long idNewZoneSubstitution;
				int indexNewZoneSubstitution = cmbZoneSubstitution.getSelectedIndex();
				if(indexNewZoneSubstitution != -1)
					idNewZoneSubstitution = tbZoneSubstitution.get(indexNewZoneSubstitution).getId();
				else
					idNewZoneSubstitution = -1;
				
				long idNewService;
				int indexNewService = cmbService.getSelectedIndex();
				if(indexNewService != -1)
					idNewService = tbService.get(indexNewService).getId();
				else
					idNewService = -1;
				
				int newAlarme = AE_Constantes.ALARME_ALERT;
				if(optAlarmeAlert.isSelected()) {
					newAlarme = ALARME_ALERT;
				}
				if(optAlarmeDefaut.isSelected()) {
					newAlarme = ALARME_DEFAUT;
				}
				if(optAlarmeEtat.isSelected()) {
					newAlarme = ALARME_ETAT;
				}
				if(optAlarmeRien.isSelected()) {
					newAlarme = ALARME_RIEN;
				}				

				String strVoieApi = txtNom.getText();
				strVoieApi = strVoieApi.substring(1);
				Long voieApi = Long.valueOf(strVoieApi);
				
				// Partie capteur
				strSql = "INSERT INTO Capteur (Nom, Description, idEquipement, idPosteTechnique, idTypeMateriel,"
					   + " idZoneSubstitution, TypeCapteur, Alarme, idService, VoieApi, Inhibition, idUnite, Contact)"
					   + " VALUES ('" + txtNom.getText() + "', '" +  txtDescription.getText() + "', " + idNewEquipement
					   + ", " + idNewPosteTechnique + ", " + idNewTypeMateriel + ", " + idNewZoneSubstitution + ", "
					   + CAPTEUR_DIGITAL_ENTREE + ", " + newAlarme + ", " + idNewService + ", " + voieApi + ", 0, -1"
					   + ", '" + txtContact.getText() + "')";
				
System.out.println(strSql);				
				
				ctn.fonctionSql(strSql);
				// Partie digitale
				long idCapteur = AE_Fonctions.trouveIndex("Capteur", "Nom", txtNom.getText(), "idCapteur");
				if(idCapteur != -1) {
					double newSeuilTempo = Double.parseDouble(txtTempo.getText());
					int newNoNf = 0;
					if(optOn.isSelected()) {
						newNoNf = CAPTEUR_DIGITAL_NO;
					}
					if(optOff.isSelected()) {
						newNoNf = CAPTEUR_DIGITAL_NF;
					}
					strSql = "INSERT INTO EntreeDigitale (idCapteur, Tempo, NoNf)"
						   + " VALUES(" + idCapteur + ", " + newSeuilTempo + ", " + newNoNf
						   + ")";
					ctn.fonctionSql(strSql);
				}
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Nom déjà existant ...");
		} // fin if .. else		
		
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == lstCapteur) {
			afficheCapteur();
		}	// fin if		
	} // Fin valueChanged
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnModifierDescription) {
			gestionModifierDescription();
		}	
		if(e.getSource() == btnModifierOnOff) {
			gestionModifierNoNf();
		}		
		if(e.getSource() == btnModifierSeuilTempo) {
			gestionModifierSeuilTempo();
		}		
		
		if(e.getSource() == btnModifierInhibition) {
			gestionModifierInhibition();
		}
		
		if(e.getSource() == btnModifierAlarme) {
			gestionModifierAlarme();
		}	
		if(e.getSource() == btnModifierEquipement) {
			gestionModifierEquipement();
		}
		if(e.getSource() == btnModifierPosteTechnique) {
			gestionModifierPosteTechnique();
		}
		if(e.getSource() == btnModifierTypeMateriel) {
			gestionModifierTypeMateriel();
		}
		if(e.getSource() == btnModifierZoneSubstitution) {
			gestionModifierZoneSubstitution();
		}
		if(e.getSource() == btnModifierService) {
			gestionModifierService();
		}
		if(e.getSource() == btnModifierContact) {
			gestionModifierContact();
		}		
		
		if(e.getSource() == btnTriFiltrer) {
			remplirListeCapteur();
		}
		
		if(e.getSource() == btnAjouter) {
			gestionAjoutCapteur();
		}
		if(e.getSource() == btnCourbe) {
			appelFenetreCourbe();
		}
		
		if(e.getSource() == tmrTpsReel) {
			lectureVoieTpsReel();
		}
	}	
} // Fin class
