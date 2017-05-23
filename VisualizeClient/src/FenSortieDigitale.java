import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import AE_General.AE_BarreBas;
import AE_General.AE_BarreHaut;


public class FenSortieDigitale extends JFrame {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlGauche = new JPanel();
	JPanel pnlCorps = new JPanel();
	JSplitPane splCorps = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlGauche, pnlCorps);
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();	
	
	// Propre
	private JLabel lblNom = new JLabel("N° voie : ");
	private JLabel lblDescription = new JLabel("Désignation : ");
	private JTextField txtNom = new JTextField("", 20);
	private JTextField txtDescription = new JTextField("", 20);
	private JButton btnModifier = new JButton("Modifier");
	private JButton btnAjouter = new JButton("Nouveau");
	private JButton btnAnnuler = new JButton("Annuler");
	private JButton btnCourbe = new JButton("Courbe");
	
	//Gauche
	private JLabel lblTriVoie = new JLabel("Voie : ");
	String[] pt1 = {"Voie 1", "Voie 2", "Voie 3"}; 
	private JComboBox<String> cmbTriVoie = new JComboBox<String>(pt1);	
	
	public FenSortieDigitale() {
		super();
		build();
	}

	public void build() {
		
		
	    this.setTitle("Sortie digitale");
	    this.setSize(900, 300);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Sortie digitale");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlGauche.setBackground(new Color(200, 130, 66));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	    //Place la barre de séparation a 200 px
		splCorps.setDividerLocation(200);
	    
	    // =================== Corps de l'écran =============================================
	    //On définit le layout manager
	    pnlCorps.setLayout(new GridBagLayout());
	    //L'objet servant à positionner les composants
		GridBagConstraints gbc = new GridBagConstraints();

	    // --------------------------------------------------
	    gbc.insets = new Insets(4, 0, 0, 0);
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.anchor= GridBagConstraints.LINE_END;
//	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(lblNom, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 0;
	    pnlCorps.add(txtNom, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 1;
	    pnlCorps.add(lblDescription, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 1;
	    pnlCorps.add(txtDescription, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------		
		
	    
	    
	    
	    // ---- Bouton --------------------------------------
	    gbc.weightx = 0;
	    gbc.insets = new Insets(20, 0, 0, 0);
	    gbc.gridx = 1; gbc.gridy = 19;
	    pnlCorps.add(btnModifier, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 19;
	    pnlCorps.add(btnAjouter, gbc);
	    // --------------------------------------------------	    
	    gbc.insets = new Insets(20, 0, 0, 20);
	    gbc.gridx = 3; gbc.gridy = 19;
	    pnlCorps.add(btnAnnuler, gbc);
	    // --------------------------------------------------	    
	    gbc.insets = new Insets(20, 0, 0, 20);
	    gbc.gridx = 4; gbc.gridy = 19;
	    pnlCorps.add(btnCourbe, gbc);
	    // --------------------------------------------------	    
	    // ==================================================================================


	    // =================== Gauche de l'écran ============================================
	    
	    pnlGauche.setLayout(new GridBagLayout());
	    
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlGauche.add(lblTriVoie, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlGauche.add(cmbTriVoie, gbc);
	    // --------------------------------------------------	    
	    
	    
	} // Fin build
}
