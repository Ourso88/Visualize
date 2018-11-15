
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EtchedBorder;

import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.AE_Fonctions;
import AE_General.EFS_Client_Variable;


public class FenMotDePasse extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	// Données
	
	// GUI
	private JPanel pnlCorps = new JPanel();
	
	// =====> pnlcorps <=====
	private JPanel pnlMotDePasse = new JPanel();
	private JPanel pnlBoutons = new JPanel();
		// =====> pnlMotDePasse <=====
	private JLabel lblTitreNouveau = new JLabel("Nouveau");
	private JLabel lblTitreConfirmation = new JLabel("Confirmation");
	private JPasswordField txtNouveau = new JPasswordField("");
	private JPasswordField txtConfirmation = new JPasswordField("");
		// =====> pnlBoutons <=====
	private JButton btnValider = new JButton("Valider");
	private JButton btnRetour = new JButton("Retour");	

	/**
	 * Constructeur
	 */
	public FenMotDePasse() {
		super();
		build();
	}

	/**
	 * Description de la fenêtre
	*/	
	private void build() {
		// Général
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            }
        });

		// Description de la fenetre
		this.setTitle("Viper J - Administrateurs - Mot de passe");
	    this.setSize(640, 500);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLocationRelativeTo(null);

	    // Definition des parties de la fenetre
	    this.add("Center",pnlCorps);
	    
	    // Actions
	    btnRetour.addActionListener(this);
	    btnValider.addActionListener(this);
	    
		GridBagConstraints gbc = new GridBagConstraints();
	    
	    pnlCorps.setLayout(new GridBagLayout());
	    
	    
	    btnRetour.setPreferredSize(btnValider.getPreferredSize());
	    btnRetour.setMinimumSize(btnValider.getMinimumSize());
	    
	    
	    // =====> pnlBouttons <===== 
	    pnlBoutons.setLayout(new FlowLayout());
	    pnlBoutons.add(btnValider);
	    pnlBoutons.add(btnRetour);

	    // =====> pnlMotDePasse <===== 
	    gbc.insets = new Insets(0, 5, 2, 5); // (top, left, bottom, right)
	    pnlMotDePasse.setLayout(new GridBagLayout());
	    // --- lblTitreNouveau --------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 30; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlMotDePasse.add(lblTitreNouveau, gbc);
	    // --- lblTitreConfirmation ---------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 30; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlMotDePasse.add(lblTitreConfirmation, gbc);
	    // --- txtNouveau -------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 70; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlMotDePasse.add(txtNouveau, gbc);
	    // --- txtConfirmation --------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 70; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlMotDePasse.add(txtConfirmation, gbc);
	    
	    // === pnlCorps === 
	    gbc.insets = new Insets(60, 20, 0, 10); // (top, left, bottom, right)
	    // --- pnlMotDePasse ----------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 100; gbc.weighty = 80;
	    gbc.anchor= GridBagConstraints.PAGE_START;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(pnlMotDePasse, gbc);
	    // --- pnlBoutons --------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 100; gbc.weighty = 20;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(pnlBoutons, gbc);
	}	

	/**
	 * Modidifie le mot de passe
	 */
	private void modifierMotDePasse() {
		AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		ctn.open();
		String nouveau = String.valueOf(txtNouveau.getPassword());
		String confirmation = String.valueOf(txtConfirmation.getPassword());
		if(nouveau.equals(confirmation)) {
			String reqSql = "UPDATE Utilisateur SET MotDePasse = '" + nouveau + "' WHERE idUtilisateur = " + EFS_Client_Variable.idUtilisateur; 
			ctn.fonctionSql(reqSql);
			this.dispose();
			Login fenetre = new Login();
			fenetre.setVisible(true);
		} else {
			AE_Fonctions.afficheMessage(this, "Erreur entre les deux mots de passe !");
		}
		ctn.close();
	}
	
	
	/**
	 * Gestion des actions utilisateurs :
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnRetour) {
			this.dispose();
			Login fenetre = new Login();
			fenetre.setVisible(true);
		}
		if(ae.getSource() == btnValider) {
			modifierMotDePasse();
		}
	}	

}
