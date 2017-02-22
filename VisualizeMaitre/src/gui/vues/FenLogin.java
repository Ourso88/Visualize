package gui.vues;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import em.fonctions.AE_Fonctions;
import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;

/**
 * Fenêtre permettant la connexion au programme
 * 		- Obligatoire pour certaines fonctions : administration, validation
 * @author Eric Mariani
 * @since 08/12/2016
 *
 */
public class FenLogin extends JFrame implements AE_Constantes, ActionListener {
	private static final long serialVersionUID = 1L;
	
	// GUI
	private AE_BarreHaut pnlEntete = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private JPanel pnlCorps = new JPanel();
	
	// =====> pnlcorps <=====
	ImageIcon icoDonnez = new ImageIcon(getClass().getResource("/Donnez.png"));
	private JLabel lblTurbine = new JLabel(icoDonnez);
	private JPanel pnlLogin = new JPanel();
	private JPanel pnlBoutons = new JPanel();
		// =====> pnlLogin <=====
	private JLabel lblTitreCompte = new JLabel("Compte");
	private JLabel lblTitreMotDePasse = new JLabel("Mot de passe");
	private JTextField txtCompte = new JTextField("");
	private JPasswordField txtMotDePasse = new JPasswordField("");
		// =====> pnlBoutons <=====
	private JButton btnValider = new JButton("Valider");
	private JButton btnRetour = new JButton("Retour");
	
	/**
	 * Constructeur
	 */
	public FenLogin() {
		super();
		build();
	}
	
	/**
	 * Description de la fenêtre
	*/	
	private void build() {
		// Description de la fenetre
		this.setTitle("Viper J - Administrateurs - Login");
	    this.setSize(640, 500);
		this.setResizable(true);
//	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/logo_efs.jpg");
	    this.setIconImage(new ImageIcon(icone).getImage());

	    // Definition des parties de la fenetre
	    this.add("North",pnlEntete);
	    this.add("Center",pnlCorps);
	    this.add("South", pnlInfo);		
	    
	    // Actions
	    btnRetour.addActionListener(this);
	    btnValider.addActionListener(this);
	    
		GridBagConstraints gbc = new GridBagConstraints();
	    
		pnlEntete.setTitreEcran("Login");
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));	
	    pnlCorps.setBackground(AE_BLEU);
	    pnlCorps.setLayout(new GridBagLayout());
	    
	    lblTurbine.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	    
	    txtCompte.setPreferredSize(new Dimension(150, 25));
	    txtCompte.setMinimumSize(new Dimension(150, 25));
	    txtMotDePasse.setPreferredSize(new Dimension(150, 25));
	    txtMotDePasse.setMinimumSize(new Dimension(150, 25));
	    
	    btnRetour.setPreferredSize(btnValider.getPreferredSize());
	    btnRetour.setMinimumSize(btnValider.getMinimumSize());
	    
	    
	    // =====> pnlBouttons <===== 
	    pnlBoutons.setBackground(AE_BLEU);
	    pnlBoutons.setLayout(new FlowLayout());
	    pnlBoutons.add(btnValider);
	    pnlBoutons.add(btnRetour);

	    // =====> pnlLogin <===== 
	    gbc.insets = new Insets(0, 5, 2, 5); // (top, left, bottom, right)
	    pnlLogin.setBackground(AE_BLEU);
	    pnlLogin.setLayout(new GridBagLayout());
	    // --- lblTitreCompte ---------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 30; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlLogin.add(lblTitreCompte, gbc);
	    // --- lblTitreMotDePasse -----------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 30; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlLogin.add(lblTitreMotDePasse, gbc);
	    // --- txtCompte --------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 70; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlLogin.add(txtCompte, gbc);
	    // --- txtMotDePasse ----------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 70; gbc.weighty = 50;
	    gbc.anchor= GridBagConstraints.LINE_START;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlLogin.add(txtMotDePasse, gbc);
	    
	    
	    
	    // === pnlCorps === 
	    gbc.insets = new Insets(0, 5, 0, 5); // (top, left, bottom, right)
	    // --- lblTurbine -------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 100; gbc.weighty = 100;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(lblTurbine, gbc);
	    gbc.insets = new Insets(5, 5, 5, 5); // (top, left, bottom, right)
	    // --- pnlLogin --------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 100; gbc.weighty = 80;
	    gbc.anchor= GridBagConstraints.PAGE_START;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(pnlLogin, gbc);
	    // --- pnlBoutons --------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 100; gbc.weighty = 20;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(pnlBoutons, gbc);
	}	
	
	/**
	 * Valide l'entrée du login et du mot de passe
	 */
	private void validerMotDePasse() {
		try {
			String strSql = "SELECT Utilisateur.idUtilisateur, Utilisateur.Nom, Utilisateur.Prenom, Utilisateur.MotDePasse, NiveauUtilisateur.Niveau"
					+ " FROM (Utilisateur LEFT JOIN NiveauUtilisateur ON Utilisateur.idNiveauUtilisateur = NiveauUtilisateur.idNiveauUtilisateur)"
					+ " WHERE Login = '" + txtCompte.getText() + "'";
			ResultSet result = AE_Variables.ctnOracle.lectureData(strSql);
			if(result.next()){
				String motDePasse = String.valueOf(txtMotDePasse.getPassword());
				if(motDePasse.equals(result.getString("MotDePasse"))) {
					AE_Variables.idUtilisateur = result.getLong("idUtilisateur");
					AE_Variables.nomUtilisateur = result.getString("Nom");
					AE_Variables.prenomUtilisateur = result.getString("Prenom");
					AE_Variables.niveauUtilisateur = result.getInt("Niveau");
					
					this.dispose();
				} else {
					AE_Fonctions.afficheMessage(this, "Visualize GTC", "Problème de mot de passe ...");
				}
			} else {
				AE_Fonctions.afficheMessage(this, "Visualize GTC", "Problème de login ...");
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD Erreur login " + e.getMessage());
		}
	}
	
	/**
	 * Gestion des actions utilisateurs :
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnRetour) {
			this.dispose();
		}
		if(ae.getSource() == btnValider) {
			validerMotDePasse();
		}
	}
	
}
