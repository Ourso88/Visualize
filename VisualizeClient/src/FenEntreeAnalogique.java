import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import EFS_Structure.StructEntreeAnalogique;
import EFS_Structure.StructEquipement;
import EFS_Structure.StructPosteTechnique;
import EFS_Structure.StructService;
import EFS_Structure.StructTypeMateriel;
import EFS_Structure.StructUnite;
import EFS_Structure.StructZoneSubstitution;
import kernel.ArrayAlarmeService;

public class FenEntreeAnalogique extends JFrame implements AE_Constantes, ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	// Variables
	private int voieApiAffichee = -1;
	
	// Timer
    private Timer tmrTpsReel = new Timer(AE_TCP_Constantes.TIMER_LECTURE_TPS_REEL, this);	
	
	// Commun
	JPanel pnlGauche = new JPanel();
	JPanel pnlCorps = new JPanel();
	JSplitPane splCorps = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlGauche, pnlCorps);
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();	

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Propre
	private JLabel lblNom = new JLabel("N° voie : ");
	private JLabel lblDescription = new JLabel("Désignation : ");
	private JLabel lblEquipement = new JLabel("N° inventaire : ");
	private JLabel lblPosteTechnique = new JLabel("Poste technique : ");
	private JLabel lblTypeMateriel = new JLabel("Type matériel : ");
	
	private int nbColonneSaisie = 10;
	private JTextField txtNom = new JTextField("", nbColonneSaisie);
	private JTextField txtDescription = new JTextField("", nbColonneSaisie);
	private JButton btnAjouter = new JButton("Nouveau");
	private JButton btnCourbe = new JButton("Courbe");
	
	private JLabel lblAlarme = new JLabel("Alarme : ");
	private JPanel pnlAlarme = new JPanel();
	private ButtonGroup bgAlarme = new ButtonGroup();
	private JRadioButton optAlarmeAlert = new JRadioButton("Alarme");
	private JRadioButton optAlarmeDefaut = new JRadioButton("Défaut");
	private JRadioButton optAlarmeEtat = new JRadioButton("Etat");
	private JRadioButton optAlarmeRien = new JRadioButton("Rien");

	private JLabel lblZoneMaintenance = new JLabel("Zone Maintenance : ");
	private JPanel pnlZoneMaintenance = new JPanel();
	private ButtonGroup bgZoneMaintenance = new ButtonGroup();
	private JRadioButton optZoneMaintenanceOui = new JRadioButton("Oui");
	private JRadioButton optZoneMaintenanceNon = new JRadioButton("Non");

	private JPanel pnlCommande = new JPanel();
	
	// Analogique
	private JLabel lblSeuilBas = new JLabel("Seuil bas : ");
	private JLabel lblSeuilHaut = new JLabel("Seuil haut : ");
	private JLabel lblTempo = new JLabel("Tempo(mn) seuil : ");
	private JLabel lblPreSeuilBas = new JLabel("Pré-seuil bas : ");
	private JLabel lblPreSeuilHaut = new JLabel("Pré-seuil haut : ");
	private JLabel lblPreTempo = new JLabel("Tempo(mn) pré-seuil : ");
	private JLabel lblCalibration = new JLabel("Calibration : ");
	private JLabel lblUnite = new JLabel("Unité : ");
	private JLabel lblValeurEnCours = new JLabel("Valeur en cours : ");
	private JLabel lblZoneSubstitution = new JLabel("Zone substitution : ");
	private JLabel lblService = new JLabel("Service : ");
	private JLabel lblValeurConsigne = new JLabel("Valeur consigne : ");
	private JLabel lblContact = new JLabel("Contact : ");
	private JTextField txtSeuilBas = new JTextField("");
	private JTextField txtSeuilHaut = new JTextField("");
	private JTextField txtTempo = new JTextField("");
	private JTextField txtPreSeuilBas = new JTextField("");
	private JTextField txtPreSeuilHaut = new JTextField("");
	private JTextField txtPreTempo = new JTextField("");
	private JTextField txtCalibration = new JTextField("");
	private JTextField txtValeurEnCours = new JTextField("00.0");
	private JTextField txtValeurConsigne = new JTextField("00.0");
	private JTextField txtContact = new JTextField("");
	private JComboBox<String> cmbUnite = new JComboBox<String>();
	private JComboBox<String> cmbZoneSubstitution = new JComboBox<String>();
	private JComboBox<String> cmbEquipement = new JComboBox<String>();
	private JComboBox<String> cmbPosteTechnique = new JComboBox<String>();
	private JComboBox<String> cmbTypeMateriel = new JComboBox<String>();
	private JComboBox<String> cmbService = new JComboBox<String>();
	private List<StructZoneSubstitution> tbZoneSubstitution = new ArrayList<StructZoneSubstitution>();	
	private List<StructUnite> tbUnite = new ArrayList<StructUnite>();	
	private List<StructEquipement> tbEquipement = new ArrayList<StructEquipement>();	
	private List<StructPosteTechnique> tbPosteTechnique = new ArrayList<StructPosteTechnique>();	
	private List<StructTypeMateriel> tbTypeMateriel = new ArrayList<StructTypeMateriel>();	
	private List<StructService> tbService = new ArrayList<StructService>();	

	// ===== Modifications 03/05/2018 =====
	private JComboBox<String> cmbAlarmeService = new JComboBox<String>();
	private ArrayAlarmeService tbAlarmeService = new ArrayAlarmeService();	
	private JPanel pnlGestionAlarme = new JPanel();
	// ===== Fin modifications 03/05/2018 =====
	
	
	private JButton btnModifierDescription = new JButton("Modifier désignation");
	private JButton btnModifierEquipement = new JButton("Modifier équipement");
	private JButton btnModifierPosteTechnique = new JButton("Modifier poste technique");
	private JButton btnModifierTypeMateriel = new JButton("Modifier type matériel");
	private JButton btnModifierAlarme = new JButton("Modifier alarme");
	private JButton btnModifierInhibition = new JButton("Modifier zone maintenance");
	private JButton btnModifierSeuilBas = new JButton("Modifier seuil bas");
	private JButton btnModifierSeuilHaut = new JButton("Modifier seuil haut");
	private JButton btnModifierSeuilTempo = new JButton("Modifier seuil tempo");
	private JButton btnModifierPreSeuilBas = new JButton("Modifier pré-seuil bas");
	private JButton btnModifierPreSeuilHaut = new JButton("Modifier pré-seuil haut");
	private JButton btnModifierPreSeuilTempo = new JButton("Modifier pré-seuil tempo");
	private JButton btnModifierCalibration = new JButton("Modifier calibration");
	private JButton btnModifierUnite = new JButton("Modifier unité");
	private JButton btnModifierZoneSubstitution = new JButton("Modifier zone substitution");
	private JButton btnModifierService = new JButton("Modifier service");
	private JButton btnModifierValeurConsigne = new JButton("Modifier valeur consigne");
	private JButton btnModifierContact = new JButton("Modifier contact");
	
	
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

	private List<StructEntreeAnalogique> tbCapteur = new ArrayList<StructEntreeAnalogique>();
	private DefaultListModel<String> modelCapteur = new DefaultListModel<String>();
	private JList<String> lstCapteur = new JList<String>(modelCapteur);
	private JScrollPane spLstCapteur = new JScrollPane(lstCapteur);	
	
	private JButton btnTriFiltrer = new JButton("Filtrer");
	
	public FenEntreeAnalogique() {
		super();
		ctn.open();
		build();
		lectureEquipement();
		lectureTypeMateriel();
		lecturePosteTechnique();
		lectureZoneSubstitution();
		lectureUnite();
		lectureService();
		lectureAlarmeService();
		remplirListeCapteur();
		tmrTpsReel.start();
	} // Fin FenEntreeAnalogique

	public void build() {
		
		// Général
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	ctn.close();
            }
        });
		
	    this.setTitle("Entrée analogique");
	    this.setSize(1200, 800);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		

	    lstCapteur.addListSelectionListener(this);
	    
		pnlHaut.setTitreEcran("Entrée analogique");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlGauche.setBackground(new Color(200, 130, 66));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	    //Place la barre de séparation a 400 px
		splCorps.setDividerLocation(400);
	    
		GridBagConstraints gbc = new GridBagConstraints();

	    // =================== Corps de l'écran =============================================
	    pnlCorps.setLayout(new GridBagLayout());

	    pnlCommande.add(btnAjouter);
	    pnlCommande.add(btnCourbe);
	    pnlCommande.setBackground(EFS_BLEU);
	    pnlCommande.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    
	    btnCourbe.addActionListener(this);
		if(EFS_Client_Variable.niveauUtilisateur >= 40) {	    
		    btnAjouter.addActionListener(this);

		    btnModifierDescription.addActionListener(this);
		    btnModifierEquipement.addActionListener(this);
		    btnModifierPosteTechnique.addActionListener(this);
		    btnModifierTypeMateriel.addActionListener(this);
		    btnModifierAlarme.addActionListener(this);
		    btnModifierInhibition.addActionListener(this);
		    btnModifierSeuilBas.addActionListener(this);
		    btnModifierSeuilHaut.addActionListener(this);
		    btnModifierSeuilTempo.addActionListener(this);
		    btnModifierPreSeuilBas.addActionListener(this);
		    btnModifierPreSeuilHaut.addActionListener(this);
		    btnModifierPreSeuilTempo.addActionListener(this);
		    btnModifierCalibration.addActionListener(this);
		    btnModifierUnite.addActionListener(this);
		    btnModifierZoneSubstitution.addActionListener(this);
		    btnModifierService.addActionListener(this);
		    btnModifierValeurConsigne.addActionListener(this);
		    btnModifierContact.addActionListener(this);
		    
		    optZoneMaintenanceNon.setEnabled(false);
		    
		} else if(EFS_Client_Variable.niveauUtilisateur == 30) {
			
		    btnModifierSeuilBas.addActionListener(this);
		    btnModifierSeuilHaut.addActionListener(this);
		    btnModifierSeuilTempo.addActionListener(this);
		    btnModifierPreSeuilBas.addActionListener(this);
		    btnModifierPreSeuilHaut.addActionListener(this);
		    btnModifierPreSeuilTempo.addActionListener(this);
			
			
			btnModifierDescription.setVisible(false);
		    btnModifierEquipement.setVisible(false);
		    btnModifierPosteTechnique.setVisible(false);
		    btnModifierTypeMateriel.setVisible(false);
		    btnModifierAlarme.setVisible(false);
		    btnModifierInhibition.setVisible(false);
/*
		    btnModifierSeuilBas.setVisible(false);
		    btnModifierSeuilHaut.setVisible(false);
		    btnModifierSeuilTempo.setVisible(false);
		    btnModifierPreSeuilBas.setVisible(false);
		    btnModifierPreSeuilHaut.setVisible(false);
		    btnModifierPreSeuilTempo.setVisible(false);
*/
		    btnModifierCalibration.setVisible(false);
		    btnModifierUnite.setVisible(false);
		    btnModifierZoneSubstitution.setVisible(false);
		    btnModifierService.setVisible(false);
		    btnModifierValeurConsigne.setVisible(false);
		    btnModifierContact.setVisible(false);
		    
		    btnAjouter.setVisible(false);
		    
		    txtNom.setEditable(false);
		    txtDescription.setEditable(false);
/*
		    txtSeuilBas.setEditable(false);
		    txtSeuilHaut.setEditable(false);
		    txtTempo.setEditable(false);
		    txtPreSeuilBas.setEditable(false);
		    txtPreSeuilHaut.setEditable(false);
		    txtPreTempo.setEditable(false);
*/
		    
		    txtCalibration.setEditable(false);
		    txtValeurEnCours.setEditable(false);
		    txtValeurConsigne.setEditable(false);
		    txtContact.setEditable(false);
		    
		    cmbEquipement.setEnabled(false);
		    cmbPosteTechnique.setEnabled(false);
		    cmbTypeMateriel.setEnabled(false);
		    cmbZoneSubstitution.setEnabled(false);
		    cmbService.setEnabled(false);
		    cmbUnite.setEnabled(false);
		    cmbAlarmeService.setEnabled(false);
		    
		    optAlarmeAlert.setEnabled(false);
		    optAlarmeDefaut.setEnabled(false);
		    optAlarmeEtat.setEnabled(false);
		    optAlarmeRien.setEnabled(false);

		    optZoneMaintenanceNon.setEnabled(false);
		    optZoneMaintenanceOui.setEnabled(false);

		} else {
			btnModifierDescription.setVisible(false);
		    btnModifierEquipement.setVisible(false);
		    btnModifierPosteTechnique.setVisible(false);
		    btnModifierTypeMateriel.setVisible(false);
		    btnModifierAlarme.setVisible(false);
		    btnModifierInhibition.setVisible(false);
		    btnModifierSeuilBas.setVisible(false);
		    btnModifierSeuilHaut.setVisible(false);
		    btnModifierSeuilTempo.setVisible(false);
		    btnModifierPreSeuilBas.setVisible(false);
		    btnModifierPreSeuilHaut.setVisible(false);
		    btnModifierPreSeuilTempo.setVisible(false);
		    btnModifierCalibration.setVisible(false);
		    btnModifierUnite.setVisible(false);
		    btnModifierZoneSubstitution.setVisible(false);
		    btnModifierService.setVisible(false);
		    btnModifierValeurConsigne.setVisible(false);
		    btnModifierContact.setVisible(false);
		    
		    btnAjouter.setVisible(false);
		    
		    txtNom.setEditable(false);
		    txtDescription.setEditable(false);
		    txtSeuilBas.setEditable(false);
		    txtSeuilHaut.setEditable(false);
		    txtTempo.setEditable(false);
		    txtPreSeuilBas.setEditable(false);
		    txtPreSeuilHaut.setEditable(false);
		    txtPreTempo.setEditable(false);
		    txtCalibration.setEditable(false);
		    txtValeurEnCours.setEditable(false);
		    txtValeurConsigne.setEditable(false);
		    txtContact.setEditable(false);
		    
		    cmbEquipement.setEnabled(false);
		    cmbPosteTechnique.setEnabled(false);
		    cmbTypeMateriel.setEnabled(false);
		    cmbZoneSubstitution.setEnabled(false);
		    cmbService.setEnabled(false);
		    cmbUnite.setEnabled(false);
		    cmbAlarmeService.setEnabled(false);
		    
		    optAlarmeAlert.setEnabled(false);
		    optAlarmeDefaut.setEnabled(false);
		    optAlarmeEtat.setEnabled(false);
		    optAlarmeRien.setEnabled(false);

		    optZoneMaintenanceNon.setEnabled(false);
		    optZoneMaintenanceOui.setEnabled(false);

		}
			
	    
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
	    btnModifierSeuilBas.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierSeuilBas.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierSeuilHaut.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierSeuilHaut.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierSeuilTempo.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierSeuilTempo.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierPreSeuilBas.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierPreSeuilBas.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierPreSeuilHaut.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierPreSeuilHaut.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierPreSeuilTempo.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierPreSeuilTempo.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierCalibration.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierCalibration.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierUnite.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierUnite.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierZoneSubstitution.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierZoneSubstitution.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierService.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierService.setMinimumSize(btnModifierInhibition.getMinimumSize());
	    btnModifierValeurConsigne.setPreferredSize(btnModifierInhibition.getPreferredSize());
	    btnModifierValeurConsigne.setMinimumSize(btnModifierInhibition.getMinimumSize());
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
	    pnlGestionAlarme.setOpaque(false);
	    pnlAlarme.setOpaque(false);
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
	    
	    txtValeurEnCours.setForeground(Color.red);
	    
	    spLstCapteur.setPreferredSize(new Dimension(200, 300));
	    spLstCapteur.setMinimumSize(new Dimension(200, 300));
	    
	    // --------------------------------------------------
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
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER; 
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
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_START;
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

	    // ------- Titre Seuil bas ----------------------------------
	    gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblSeuilBas, gbc);
	    // ------- Saisie Seuil bas ---------------------------------
	    gbc.gridx = 1; gbc.gridy = 7; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtSeuilBas, gbc);
	    // ------- Modification Seuil bas ---------------------------
	    gbc.gridx = 2; gbc.gridy = 7; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierSeuilBas, gbc);

	    // ------- Titre Seuil haut --------------------------------
	    gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblSeuilHaut, gbc);
	    // ------- Saisie Seuil haut -------------------------------
	    gbc.gridx = 1; gbc.gridy = 8; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtSeuilHaut, gbc);
	    // ------- Modification Seuil haut -------------------------
	    gbc.gridx = 2; gbc.gridy = 8; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierSeuilHaut, gbc);

	    // ------- Titre Tempo Seuil  ------------------------------
	    gbc.gridx = 0; gbc.gridy = 9; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblTempo, gbc);
	    // ------- Saisie Tempo Seuil ------------------------------
	    gbc.gridx = 1; gbc.gridy = 9; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtTempo, gbc);
	    // ------- Modification Tempo Seuil ------------------------
	    gbc.gridx = 2; gbc.gridy = 9; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierSeuilTempo, gbc);
	    
	    // ------- Titre Pre Seuil bas ------------------------------
	    gbc.gridx = 0; gbc.gridy = 10; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblPreSeuilBas, gbc);
	    // ------- Saisie Pre Seuil bas -----------------------------
	    gbc.gridx = 1; gbc.gridy = 10; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtPreSeuilBas, gbc);
	    // ------- Modification Pre Seuil bas -----------------------
	    gbc.gridx = 2; gbc.gridy = 10; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierPreSeuilBas, gbc);

	    // ------- Titre Pre Seuil haut -----------------------------
	    gbc.gridx = 0; gbc.gridy = 11; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblPreSeuilHaut, gbc);
	    // ------- Saisie Pre Seuil haut ----------------------------
	    gbc.gridx = 1; gbc.gridy = 11; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtPreSeuilHaut, gbc);
	    // ------- Modification Seuil haut -------------------------
	    gbc.gridx = 2; gbc.gridy = 11; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierPreSeuilHaut, gbc);

	    // ------- Titre Tempo Pre Seuil  --------------------------
	    gbc.gridx = 0; gbc.gridy = 12; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblPreTempo, gbc);
	    // ------- Saisie Tempo Pre Seuil --------------------------
	    gbc.gridx = 1; gbc.gridy = 12; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtPreTempo, gbc);
	    // ------- Modification Tempo Pre Seuil --------------------
	    gbc.gridx = 2; gbc.gridy = 12; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierPreSeuilTempo, gbc);
	    
	    // ------- Titre Calibration -------------------------------
	    gbc.gridx = 0; gbc.gridy = 13; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblCalibration, gbc);
	    // ------- Saisie Calibration ------------------------------
	    gbc.gridx = 1; gbc.gridy = 13; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtCalibration, gbc);
	    // ------- Modification Calibration ------------------------
	    gbc.gridx = 2; gbc.gridy = 13; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierCalibration, gbc);
	    
	    // ------- Titre Unite  ------------------------------------
	    gbc.gridx = 0; gbc.gridy = 14; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblUnite, gbc);
	    // ------- Saisie Unite ------------------------------------
	    gbc.gridx = 1; gbc.gridy = 14; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbUnite, gbc);
	    // ------- Modification Unite ------------------------------
	    gbc.gridx = 2; gbc.gridy = 14; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierUnite, gbc);

	    // ------- Titre Valeur en cours  --------------------------
	    gbc.gridx = 0; gbc.gridy = 15; // gbc.weightx = 40;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblValeurEnCours, gbc);
	    // ------- Saisie valeur en cours---------------------------
	    gbc.gridx = 1; gbc.gridy = 15; // gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtValeurEnCours, gbc);

	    // ------- Titre Zone substitution--------------------------
	    gbc.gridx = 0; gbc.gridy = 16; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblZoneSubstitution, gbc);
	    // ------- Saisie Zone substitution ------------------------
	    gbc.gridx = 1; gbc.gridy = 16; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbZoneSubstitution, gbc);
	    // ------- Modification Zone substitution ------------------
	    gbc.gridx = 2; gbc.gridy = 16; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierZoneSubstitution, gbc);

	    // ------- Titre Service -----------------------------------
	    gbc.gridx = 0; gbc.gridy = 17; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblService, gbc);
	    // ------- Saisie Service ----------------------------------
	    gbc.gridx = 1; gbc.gridy = 17; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(cmbService, gbc);
	    // ------- Modification Service ----------------------------
	    gbc.gridx = 2; gbc.gridy = 17; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierService, gbc);

	    // ------- Titre Valeur consigne ---------------------------
	    gbc.gridx = 0; gbc.gridy = 18; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblValeurConsigne, gbc);
	    // ------- Saisie Valeur consigne --------------------------
	    gbc.gridx = 1; gbc.gridy = 18; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtValeurConsigne, gbc);
	    // ------- Modification Valeur consigne --------------------
	    gbc.gridx = 2; gbc.gridy = 18; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierValeurConsigne, gbc);

	    // ------- Titre Contact -----------------------------------
	    gbc.gridx = 0; gbc.gridy = 19; gbc.weightx = titreWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor= GridBagConstraints.LINE_END;
	    pnlCorps.add(lblContact, gbc);
	    // ------- Saisie Contact ----------------------------------
	    gbc.gridx = 1; gbc.gridy = 19; gbc.weightx = saisieWeightX;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtContact, gbc);
	    // ------- Modification Contact ----------------------------
	    gbc.gridx = 2; gbc.gridy = 19; gbc.weightx = modificationWeightX;
	    gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.LINE_START; 
	    pnlCorps.add(btnModifierContact, gbc);
	    
	    // ---- Commandes ------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 20; // gbc.weightx = 100;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(pnlCommande, gbc);

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
	
	private void remplirListeCapteur() {
		String strSql = "";
		int index = 0;
		int idService = -1;
		
		// Lecture données
		tbCapteur.clear();
		modelCapteur.removeAllElements();
		
		strSql = "SELECT Capteur.*, EntreeAnalogique.*, Equipement.Nom AS nomEquipement "
				+ "FROM ((Capteur LEFT JOIN EntreeAnalogique ON Capteur.idCapteur = EntreeAnalogique.idCapteur)"
				+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
				+ " WHERE TypeCapteur = 1";		
/*		
		strSql = "SELECT Capteur.*, EntreeAnalogique.* "
				+ "FROM (EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur)"
				+ " WHERE TypeCapteur = 1";
*/
		
		// Service
		index = cmbTriService.getSelectedIndex();
		if (index > 0) {
			strSql += " AND idService = " + tbTriService.get(index - 1).getId();
		}
		// Poste technique
		index = cmbTriPosteTechnique.getSelectedIndex();
		if (index > 0) {
			strSql += " AND Capteur.idPosteTechnique = " + tbTriPosteTechnique.get(index - 1).getId();
		}
		// Equipement
		index = cmbTriEquipement.getSelectedIndex();
		if (index > 0) {
			strSql += " AND Capteur.idEquipement = " + tbTriEquipement.get(index - 1).getId();
		}
		// Type Materiel
		index = cmbTriTypeMateriel.getSelectedIndex();
		if (index > 0) {
			strSql += " AND Capteur.idTypeMateriel = " + tbTriTypeMateriel.get(index - 1).getId();
		}
		
		strSql += " ORDER BY VoieApi";
		
		ResultSet result = ctn.lectureData(strSql);

		// Remplissage de la liste
		try {
			while(result.next()) {
				idService = result.getInt("idService");
				if(testService(idService)) {
					tbCapteur.add(new StructEntreeAnalogique(result.getLong("idEntreeAnalogique"), result.getString("Nom"), result.getString("Description")
							, result.getLong("SeuilBas"), result.getLong("SeuilHaut"), result.getLong("SeuilTempo"), result.getLong("PreSeuilBas"), result.getLong("PreSeuilHaut")
							, result.getLong("PreSeuilTempo"), result.getLong("idService"), result.getLong("idZoneSubstitution"), result.getInt("Alarme")
							, result.getDouble("Calibration"), result.getLong("idCapteur"), result.getLong("idEquipement")
							, result.getLong("idPosteTechnique"), result.getLong("idTypeMateriel")
							, result.getLong("idUnite"), result.getInt("voieApi"), result.getLong("ValeurConsigne")
							, result.getString("Contact"), result.getLong("idAlarmeService")));
					modelCapteur.addElement(result.getString("Nom") + " - " + result.getString("nomEquipement") + " - " + result.getString("Description"));
				}
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
				cmbPosteTechnique.addItem(tbPosteTechnique.get(index).getNom() + "  -  " + tbPosteTechnique.get(index).getDescription());
				tbTriPosteTechnique.add(new StructPosteTechnique(result.getLong("idPosteTechnique"), result.getString("Nom"), 
						result.getString("Description")));
				cmbTriPosteTechnique.addItem(tbTriPosteTechnique.get(index).getNom() + "  -  " + tbTriPosteTechnique.get(index).getDescription());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbPosteTechnique.setSelectedIndex(-1);
		
	} // Fin lecturePosteTechnique		
	
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
//		cmbTriService.setSelectedIndex(1);
		// Fermeture base
		
	}	

	private void lectureUnite() {
		int index = 0;
		// RAZ combo
		cmbUnite.removeAllItems();
		tbUnite.clear();
		// Connection base
		
		// Lecture données
		
		ResultSet result = ctn.lectureData("SELECT * FROM Unite ORDER BY Nom");
		index = 0;
		// Remplissage tableau et combo PosteTechnique
		try {
			while(result.next()) {
				tbUnite.add(new StructUnite(result.getLong("idUnite"), result.getString("Nom"), result.getString("Description")));
				cmbUnite.addItem(tbUnite.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbUnite.setSelectedIndex(-1);
		// Fermeture base
		
	} // fin lectureUnite()
	
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
	
	private void selectionUnite(long index) {
		for(int i = 0; i < tbUnite.size() ; i++) {
			if (tbUnite.get(i).getId() == index) {
				cmbUnite.setSelectedIndex(i);
			}
		}
	} // Fin selectionUnite

	private void selectionService(long index) {
		for(int i = 0; i < tbService.size() ; i++) {
			if (tbService.get(i).getId() == index) {
				cmbService.setSelectedIndex(i);
			}
		}
	} // Fin selectionService	
	
	private void videFicheCapteur() {
		txtNom.setText("");
		txtDescription.setText("");
		txtSeuilBas.setText("");
		txtSeuilHaut.setText("");
		txtTempo.setText("");
		txtPreSeuilBas.setText("");
		txtPreSeuilHaut.setText("");
		txtPreTempo.setText("");
		txtCalibration.setText("");
		txtValeurConsigne.setText("");
		txtContact.setText("");

		cmbTypeMateriel.setSelectedIndex(-1);
		cmbZoneSubstitution.setSelectedIndex(-1);
		cmbEquipement.setSelectedIndex(-1);
		cmbPosteTechnique.setSelectedIndex(-1);
		cmbUnite.setSelectedIndex(-1);
		cmbService.setSelectedIndex(-1);
		
		optAlarmeAlert.setSelected(false);
		optAlarmeDefaut.setSelected(false);
		optAlarmeEtat.setSelected(false);
		optAlarmeRien.setSelected(false);
		optZoneMaintenanceOui.setSelected(false);
		optZoneMaintenanceNon.setSelected(false);
		
	} // fin videFicheCapteur()
	
	private void afficheCapteur() {
		long idCapteur = -1;
		double seuilBas = 0D;
		double seuilHaut = 0D;
		long seuilTempo = 0;
		double preSeuilBas = 0D;
		double preSeuilHaut = 0D;
		long preSeuilTempo = 0;
		double calibration = 0;
		double valeurConsigne = 0D;
		String strSql = "";
		
		videFicheCapteur();
		int index = lstCapteur.getSelectedIndex();
		if(index !=  -1) {
			txtNom.setText(tbCapteur.get(index).getNom());
			txtDescription.setText(tbCapteur.get(index).getDescription());
			seuilBas = (double) tbCapteur.get(index).getSeuilBas() / 10;
			seuilHaut = (double) tbCapteur.get(index).getSeuilHaut() / 10;
			seuilTempo = tbCapteur.get(index).getSeuilTempo();
			preSeuilBas = (double) tbCapteur.get(index).getPreSeuilBas() / 10;
			preSeuilHaut = (double) tbCapteur.get(index).getPreSeuilHaut() / 10;
			preSeuilTempo = tbCapteur.get(index).getPreSeuilTempo();
			calibration = tbCapteur.get(index).getCalibration() / 10;
			idCapteur = tbCapteur.get(index).getIdCapteur();
			valeurConsigne = (double) tbCapteur.get(index).getValeurConsigne() / 10;

			voieApiAffichee = tbCapteur.get(index).getVoieApi();
			
			txtSeuilBas.setText(String.valueOf(seuilBas));
			txtSeuilHaut.setText(String.valueOf(seuilHaut));
			txtTempo.setText(String.valueOf(seuilTempo));
			txtPreSeuilBas.setText(String.valueOf(preSeuilBas));
			txtPreSeuilHaut.setText(String.valueOf(preSeuilHaut));
			txtPreTempo.setText(String.valueOf(preSeuilTempo));
			txtCalibration.setText(String.valueOf(calibration));
			txtValeurConsigne.setText(String.valueOf(valeurConsigne));
			txtContact.setText(tbCapteur.get(index).getContact());

			selectionZoneSubstitution(tbCapteur.get(index).getIdZoneSubstitution());
			selectionEquipement(tbCapteur.get(index).getIdEquipement());
			selectionPosteTechnique(tbCapteur.get(index).getIdPosteTechnique());
			selectionTypeMateriel(tbCapteur.get(index).getIdTypeMateriel());
			selectionUnite(tbCapteur.get(index).getIdUnite());
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
	
	private void gestionModifierSeuilBas() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double seuilBas = 0D;
		double newSeuilBas = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		seuilBas = (double) tbCapteur.get(index).getSeuilBas() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtSeuilBas.getText())){
			newSeuilBas = Double.parseDouble(txtSeuilBas.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Seuil bas de : " + seuilBas + " pour " + newSeuilBas;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET SeuilBas = " + (newSeuilBas * 10) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setSeuilBas((long)(newSeuilBas * 10));
				// Ajout dans journal
	        	msgJournal = "Passage du seuil bas de " + seuilBas	+ " à " + newSeuilBas + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_SEUIL);
				// Prevenir les programmes maitre
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_SEUIL_BAS);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierSeuilBas()
	
	private void gestionModifierSeuilHaut() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double seuilHaut = 0D;
		double newSeuilHaut = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		seuilHaut = (double) tbCapteur.get(index).getSeuilHaut() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtSeuilHaut.getText())){
			newSeuilHaut = Double.parseDouble(txtSeuilHaut.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Seuil haut de : " + seuilHaut + " pour " + newSeuilHaut;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET SeuilHaut = " + (newSeuilHaut * 10) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setSeuilHaut((long)(newSeuilHaut * 10));
				// Ajout dans journal
	        	msgJournal = "Passage du seuil haut de " + seuilHaut	+ " à " + newSeuilHaut + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_SEUIL);
				// Prevenir les programmes maitre
				AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_SEUIL_HAUT);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierSeuilHaut()	
	
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
		seuilTempo = (double) tbCapteur.get(index).getSeuilTempo() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtTempo.getText())){
			newSeuilTempo = Double.parseDouble(txtTempo.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Seuil Tempo de : " + seuilTempo + " pour " + newSeuilTempo;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET SeuilTempo = " + (newSeuilTempo) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setSeuilTempo((long)(newSeuilTempo));
				// Ajout dans journal
	        	msgJournal = "Passage du seuil Tempo de " + seuilTempo	+ " à " + newSeuilTempo + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_SEUIL_TEMPO);
				// Prevenir les programmes maitre
//				AE_Fonctions.prevenirProgrammeMaitre(ECHANGE_MAITRE_CLIENT_SEUIL_TEMPO);
		        // Prevenir le programme maitre
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_TEMPO);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierSeuilTempo()		
	
	private void gestionModifierPreSeuilBas() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double preSeuilBas = 0D;
		double newPreSeuilBas = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		preSeuilBas = (double) tbCapteur.get(index).getPreSeuilBas() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtPreSeuilBas.getText())){
			newPreSeuilBas = Double.parseDouble(txtPreSeuilBas.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Pré-seuil bas de : " + preSeuilBas + " pour " + newPreSeuilBas;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET PreSeuilBas = " + (newPreSeuilBas * 10) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setPreSeuilBas((long)(newPreSeuilBas * 10));
				// Ajout dans journal
	        	msgJournal = "Passage du pré-seuil bas de " + preSeuilBas	+ " à " + newPreSeuilBas + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_PRE_SEUIL);
				// Prevenir les programmes maitre
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_PRE_SEUIL_BAS);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierPreSeuilBas()	
	
	private void gestionModifierPreSeuilHaut() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double preSeuilHaut = 0D;
		double newPreSeuilHaut = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		preSeuilHaut = (double) tbCapteur.get(index).getPreSeuilHaut() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtPreSeuilHaut.getText())){
			newPreSeuilHaut = Double.parseDouble(txtPreSeuilHaut.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Pre-euil haut de : " + preSeuilHaut + " pour " + newPreSeuilHaut;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET PreSeuilHaut = " + (newPreSeuilHaut * 10) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setPreSeuilHaut((long)(newPreSeuilHaut * 10));
				// Ajout dans journal
	        	msgJournal = "Passage du pre-seuil haut de " + preSeuilHaut	+ " à " + newPreSeuilHaut + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_PRE_SEUIL);
				// Prevenir les programmes maitre
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_PRE_SEUIL_HAUT);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierPreSeuilHaut()	
	
	private void gestionModifierPreSeuilTempo() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double preSeuilTempo = 0D;
		double newPreSeuilTempo = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		preSeuilTempo = (double) tbCapteur.get(index).getPreSeuilTempo() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtTempo.getText())){
			newPreSeuilTempo = Double.parseDouble(txtPreTempo.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " le Pre-seuil Tempo de : " + preSeuilTempo + " pour " + newPreSeuilTempo;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET PreSeuilTempo = " + (newPreSeuilTempo) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setPreSeuilTempo((long)(newPreSeuilTempo));
				// Ajout dans journal
	        	msgJournal = "Passage du pre-seuil Tempo de " + preSeuilTempo	+ " à " + newPreSeuilTempo + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_PRE_SEUIL_TEMPO);
				// Prevenir les programmes maitre
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_PRE_SEUIL_TEMPO);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierPreSeuilTempo()	

	private void gestionModifierCalibration() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double calibration = 0D;
		double newCalibration = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		calibration = (double) tbCapteur.get(index).getCalibration() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtCalibration.getText())){
			newCalibration = Double.parseDouble(txtCalibration.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " la calibration de : " + calibration + " pour " + newCalibration;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET calibration = " + (newCalibration * 10) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);

				strSql = "INSERT INTO HistoriqueCalibration (DateModification, AncienneValeur, NouvelleValeur, idUtilisateur, idCapteur) "
					   + "VALUES(sysdate, " + (calibration * 10) + ", " + (newCalibration * 10) + ", " + EFS_Client_Variable.idUtilisateur
					   + ", " + idCapteur + ")";
System.out.println(strSql);				
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setCalibration((long)(newCalibration * 10));
				// Ajout dans journal
	        	msgJournal = "Passage de la calibration de " + calibration	+ " à " + newCalibration + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_CALIBRATION);
				// Prevenir les programmes maitre
		        AE_Fonctions.modifierMaitreViaClient((int) idCapteur, VIA_API_CALIBRATION);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifiercalibration()
		
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
	
	private void gestionModifierUnite() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;

		long idUnite;
		long idNewUnite;
		String unite = "";
		String newUnite = "";
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		
		// Nouvelle
		int indexNewUnite = cmbUnite.getSelectedIndex();
		idNewUnite = tbUnite.get(indexNewUnite).getId();
		newUnite = (String) cmbUnite.getSelectedItem();
		
		// Ancienne
		idUnite = tbCapteur.get(index).getIdUnite();
		
		strSql = "SELECT * FROM Unite WHERE idUnite = " + idUnite;
		ResultSet result = ctn.lectureData(strSql);				
		try {
			if(result.next()) {
				unite = result.getString("Nom");
			} else {
				unite = "???";
			}
			result.close();
			ctn.closeLectureData();
		} catch (SQLException eSql) {
			eSql.printStackTrace();
		} // Fin try
				
		
		// Demande confirmation
		msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " l'unité de : " + unite + " pour " + newUnite;
		blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
		if(blRetour) {
			// Ecriture dans la base
			
			strSql = "UPDATE Capteur SET idUnite = " + idNewUnite + " WHERE idCapteur = " + idCapteur;
			ctn.fonctionSql(strSql);
			
			// Chgt dans tableau
			tbCapteur.get(index).setIdUnite(idNewUnite);
			// Ajout dans journal
        	msgJournal = "Passage unité de " + unite	+ " à " + newUnite + " pour le capteur : " + nomCapteur
        			+ "  " + descriptionCapteur;
			AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_UNITE);
			// Réaffichage capteur
			afficheCapteur();
		}
	} // fin gestionModifierUnite

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

	private void gestionModifierValeurConsigne() {
		boolean blRetour = false;
		String strSql = "";
		String msgConfirmation = "";
		String msgJournal = "";
		String nomCapteur = "";
		String descriptionCapteur = "";
		long idCapteur = -1;
		double valeurConsigne = 0D;
		double newValeurConsigne = 0D;
		
		int index = lstCapteur.getSelectedIndex();
		idCapteur = tbCapteur.get(index).getIdCapteur();
		nomCapteur = tbCapteur.get(index).getNom();
		descriptionCapteur = tbCapteur.get(index).getDescription();
		valeurConsigne = (double) tbCapteur.get(index).getValeurConsigne() / 10;

		
		// Vérifier si c'est bien un nombre
		if (AE_Fonctions.isNumeric(txtValeurConsigne.getText())){
			newValeurConsigne = Double.parseDouble(txtValeurConsigne.getText());
		
			// Demande confirmation
			msgConfirmation = "Voulez vous changez pour le capteur " + nomCapteur + " la valeur consigne de : " + valeurConsigne + " pour " + newValeurConsigne;
			blRetour = AE_Fonctions.messageConfirmation(this, msgConfirmation);		
			if(blRetour) {
				// Ecriture dans la base
				
				strSql = "UPDATE EntreeAnalogique SET ValeurConsigne = " + (newValeurConsigne * 10) + " WHERE idCapteur = " + idCapteur;
				ctn.fonctionSql(strSql);
				
				// Chgt dans tableau
				tbCapteur.get(index).setValeurConsigne((long)(newValeurConsigne * 10));
				// Ajout dans journal
	        	msgJournal = "Passage de la valeur consigne de " + valeurConsigne	+ " à " + newValeurConsigne + " pour le capteur : " + nomCapteur
	        			+ "  " + descriptionCapteur;
				AE_Fonctions.ecrireJournal(msgJournal, idCapteur, TYPE_JOURNAL_MODIFICATION_VALEUR_CONSIGNE);
				// Réaffichage capteur
				afficheCapteur();
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Vous devez rentrer un nombre (1, 2 ,3, ., etc)  ");
		}
	
	} // fin gestionModifierSeuilHaut()		
	
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
	
	private void lectureVoieTpsReel() {
		if(voieApiAffichee > 0) {
			double valeurLue = 0D;
			double calibration = 0D;
			int index = lstCapteur.getSelectedIndex();
			
			if(index >= 0 ) {
				calibration = tbCapteur.get(index).getCalibration() / 10;
				valeurLue = (EFS_Client_Variable.tbValeurAI[voieApiAffichee - 1] / 10) + calibration;
				txtValeurEnCours.setText(String.valueOf(valeurLue));
			}
			
		}
	} // fin lectureVoieTpsReel()

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
	
	public void selectionCapteur(int idCapteur) {
		for(int i = 0; i < tbCapteur.size(); i++) {
			if(idCapteur == tbCapteur.get(i).getIdCapteur()) {
				lstCapteur.setSelectedIndex(i);
			}
		} // fin for
	} // fin selectionCapteur
	
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

				long idNewUnite;
				int indexNewUnite = cmbUnite.getSelectedIndex();
				if(indexNewUnite != -1)
					idNewUnite = tbUnite.get(indexNewUnite).getId();
				else
					idNewUnite = -1;
				
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
					   + CAPTEUR_ANALOGIQUE_ENTREE + ", " + newAlarme + ", " + idNewService + ", " + voieApi + ", 0, " + idNewUnite 
					   + ", '" + txtContact.getText() + "')";
				
System.out.println(strSql);				
				
				ctn.fonctionSql(strSql);
				// Partie analogique
				long idCapteur = AE_Fonctions.trouveIndex("Capteur", "Nom", txtNom.getText(), "idCapteur");
				if(idCapteur != -1) {
					double newSeuilTempo = Double.parseDouble(txtTempo.getText());
					double newSeuilHaut = Double.parseDouble(txtSeuilHaut.getText()) * 10;
					double newPreSeuilHaut = Double.parseDouble(txtPreSeuilHaut.getText()) * 10;
					double newSeuilBas = Double.parseDouble(txtSeuilBas.getText()) * 10;
					double newPreSeuilBas = Double.parseDouble(txtPreSeuilBas.getText()) * 10;
					double newCalibration = Double.parseDouble(txtCalibration.getText());
					double newPreSeuilTempo = Double.parseDouble(txtPreTempo.getText());
					double newValeurConsigne = (Double.parseDouble(txtValeurConsigne.getText())) * 10;

					
					strSql = "INSERT INTO EntreeAnalogique (idCapteur, SeuilHaut, PreSeuilHaut, SeuilBas,"
						   + " PreSeuilBas, Calibration, SeuilTempo, Unite, PreSeuilTempo, ValeurConsigne)"
						   + " VALUES(" + idCapteur + ", " + newSeuilHaut + ", " + newPreSeuilHaut + ", "
						   + newSeuilBas + ", " + newPreSeuilBas + ", " + newCalibration + ", " + newSeuilTempo
						   + ", '', " + newPreSeuilTempo + ", " + newValeurConsigne  
						   + ")";
					ctn.fonctionSql(strSql);
				}
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Nom déjà existant ...");
		} // fin if .. else		
		
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnModifierDescription) {
			gestionModifierDescription();
		}	
		if(e.getSource() == btnModifierSeuilBas) {
			gestionModifierSeuilBas();
		}
		if(e.getSource() == btnModifierSeuilHaut) {
			gestionModifierSeuilHaut();
		}
		if(e.getSource() == btnModifierSeuilTempo) {
			gestionModifierSeuilTempo();
		}
		if(e.getSource() == btnModifierPreSeuilBas) {
			gestionModifierPreSeuilBas();
		}
		if(e.getSource() == btnModifierPreSeuilHaut) {
			gestionModifierPreSeuilHaut();
		}
		if(e.getSource() == btnModifierPreSeuilTempo) {
			gestionModifierPreSeuilTempo();
		}
		if(e.getSource() == btnModifierCalibration) {
			gestionModifierCalibration();
		}
		if(e.getSource() == btnModifierInhibition) {
			gestionModifierInhibition();
		}
		if(e.getSource() == btnModifierAlarme) {
			gestionModifierAlarme();
		}
		if(e.getSource() == btnModifierUnite) {
			gestionModifierUnite();
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
		if(e.getSource() == btnModifierValeurConsigne) {
			gestionModifierValeurConsigne();
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
		
		
	} // Fin actionPerformed

	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == lstCapteur) {
			afficheCapteur();
		}	// fin if		
	} // Fin valueChanged

	
} // Fin class
