import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;

public class Login extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel lblLogin = new JLabel("Login : ");
	private JLabel lblMdp = new JLabel("Mot de passe : ");
	private JLabel lblSite = new JLabel("Site");
	private JTextField txtLogin = new JTextField("", 20);
	private JPasswordField txtMdp = new JPasswordField("", 20);
	private JButton btnValider = new JButton("Valider");
	private JButton btnAnnuler = new JButton("Annuler");
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	public Login() {
		super();
	    this.setTitle("Login");
	    this.setSize(500, 200);
	    this.setLocationRelativeTo(null);	
	    
	    this.lblSite.setText(EFS_Client_Variable.siteEFS); 
	    lblSite.setForeground(Color.RED);
	    Font font_16 = new Font("Arial",Font.BOLD, 16);
	    lblSite.setFont(font_16);
	    
	    //Le conteneur principal
	    JPanel content = new JPanel();
	    content.setPreferredSize(new Dimension(500, 200));
	    content.setBackground(Color.WHITE);
	    //On définit le layout manager
	    content.setLayout(new GridBagLayout());
			
	    //L'objet servant à positionner les composants
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    // --------------------------------------------------
	    // Position de la case de départ
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    // taille et hauteur
	    gbc.gridheight = 1;
	    gbc.gridwidth = 2;
	    // Ancrage
	    gbc.anchor= GridBagConstraints.CENTER;
	    content.add(lblSite, gbc);
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.gridwidth = 1;
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    content.add(lblLogin, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    content.add(txtLogin, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 2;
	    content.add(lblMdp, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 2;
	    content.add(txtMdp, gbc);
	    // --------------------------------------------------
//	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(20, 0, 0, 0);
	    gbc.gridx = 1; gbc.gridy = 3;
	    content.add(btnValider, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 3;
	    content.add(btnAnnuler, gbc);
	    // --------------------------------------------------
	    
	    // Prise en compte des boutons
	    btnValider.addActionListener(this);
	    btnAnnuler.addActionListener(this);
	    //On ajoute le conteneur
	    this.setContentPane(content);	    
	    
	    
	    
	    this.setVisible(true);
	    // Focus
	    txtLogin.requestFocusInWindow();

	}

	private void gestionLogin() {
		ResultSet result = null;
		String strSql = "";
		String msgJournal = "";
		
		ctn.open();
		// Verifier login et Mdp
		strSql = "SELECT Utilisateur.*, NiveauUtilisateur.Niveau FROM"
				+ " (Utilisateur LEFT JOIN NiveauUtilisateur ON Utilisateur.idNiveauUtilisateur = NiveauUtilisateur.idNiveauUtilisateur)"
				+ " WHERE Login = '" + txtLogin.getText() + "'";
		System.out.println("strSql : " + strSql);
		result = ctn.lectureData(strSql);
		try {
			if(result.next()) {
				char[] c = txtMdp.getPassword();
				String mdpEntree = new String(c);
				int cpt = 0;
				
				if(result.getString("MotDePasse").equals(mdpEntree)) {
					// Ok
					EFS_Client_Variable.idUtilisateur = result.getInt("idUtilisateur");
					EFS_Client_Variable.niveauUtilisateur = result.getInt("Niveau");
					EFS_Client_Variable.nomUtilisateur = result.getString("Nom");
					EFS_Client_Variable.prenomUtilisateur = result.getString("Prenom");

					msgJournal = "Connection sur poste client de " + EFS_Client_Variable.nomUtilisateur + " " + EFS_Client_Variable.prenomUtilisateur;
		    		strSql = "INSERT INTO Journal (DateJournal, Description, TypeJournal, idCapteur, idUtilisateur)" 
		    				+ " VALUES(sysdate, '" + msgJournal + "', " + AE_Constantes.TYPE_JOURNAL_LOGGING_POSTE_CLIENT 
		    				+ ", -1" + ", " + EFS_Client_Variable.idUtilisateur
		    				+ ")";
		    		ctn.fonctionSql(strSql);

		    		// Lecture des services
		    		for(int i = 0; i < 100; i++) {
		    			EFS_Client_Variable.tbService[i] = -1;
		    		}

		    		strSql = "SELECT * FROM LienUtilisateurService WHERE idUtilisateur = " + EFS_Client_Variable.idUtilisateur;
		    		result = ctn.lectureData(strSql);
		    		try {
		    			while(result.next()) {
		    				EFS_Client_Variable.tbService[cpt++] = result.getInt("idService");
			    			System.out.println("tbService = " + result.getInt("idService"));
		    			}
		    		} catch(SQLException e) {
		    			e.printStackTrace();
		    			System.out.println("Problème lecture dans login");
		    			
		    		}
					this.setVisible(false);
System.out.println("Niveau : " + EFS_Client_Variable.niveauUtilisateur);					
					if(EFS_Client_Variable.niveauUtilisateur >= 40) {
						FenPrincipale fenetre;
						try {
							fenetre = new FenPrincipale();
							fenetre.setVisible(true);
							fenetre.requestFocusInWindow();		
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						FenChoixCapteurAnalogique fenetre;
						try {
							fenetre = new FenChoixCapteurAnalogique();
							fenetre.setVisible(true);
							fenetre.requestFocusInWindow();		
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					this.dispose();
				}
				else {
					// Erreur mot de passe
					JOptionPane.showMessageDialog(this, "Erreur mot de passe", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
					
			} // Fin if result.next()
			else {
				// Login faux
				JOptionPane.showMessageDialog(this, "Erreur de login", "Erreur", JOptionPane.ERROR_MESSAGE);
			} // 
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Problème lecture dans login");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ctn.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnValider) {
			gestionLogin();
		} // Fin If
		if (e.getSource() == btnAnnuler) {
			this.setVisible(false);
			this.dispose();
		} // Fin If
		
	}
	
	
}
