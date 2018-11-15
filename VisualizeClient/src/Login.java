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
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.AE_Fonctions;
import AE_General.EFS_Client_Variable;
import AE_General.EFS_Constantes;

public class Login extends JFrame implements AE_General.AE_Constantes, ActionListener, EFS_Constantes  {
	private static final long serialVersionUID = 1L;
	private JPanel pnlCorps = new JPanel();
	private JPanel pnlCommande = new JPanel();
	private JLabel lblLogin = new JLabel("Login : ");
	private JLabel lblMdp = new JLabel("Mot de passe : ");
	private JTextField txtLogin = new JTextField("", 20);
	private JPasswordField txtMdp = new JPasswordField("", 20);
	private JButton btnValider = new JButton("Valider");
	private JButton btnAnnuler = new JButton("Annuler");
	private JButton btnModifierMotDePasse = new JButton("Modifier Mot de passe");
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	public Login() {
		super();
	    this.setTitle("Login");
	    this.setSize(600, 170);
	    this.setLocationRelativeTo(null);	
	    
	    JLabel lblTitreAlarme = new JLabel("CONNEXION");
	    lblTitreAlarme.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreAlarme.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreAlarme.setBackground(EFS_MARRON);
	    lblTitreAlarme.setOpaque(true);
	    
	    this.add("North", lblTitreAlarme);
	    this.add("Center", pnlCorps);
	    
	    //Le conteneur principal
	    pnlCorps.setBackground(Color.WHITE);
	    pnlCommande.setBackground(Color.WHITE);

	    btnValider.setPreferredSize(btnModifierMotDePasse.getPreferredSize());
	    btnValider.setMinimumSize(btnModifierMotDePasse.getMinimumSize());
	    btnAnnuler.setPreferredSize(btnModifierMotDePasse.getPreferredSize());
	    btnAnnuler.setMinimumSize(btnModifierMotDePasse.getMinimumSize());
	    
	    pnlCommande.add(btnValider);
	    pnlCommande.add(btnModifierMotDePasse);
	    pnlCommande.add(btnAnnuler);
	    
	    //L'objet servant à positionner les composants
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    gbc.insets = new Insets(10, 0, 0, 0); // (top, left, bottom, right)
	    pnlCorps.setLayout(new GridBagLayout());
	    // --- lblLogin ---------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 20; gbc.weighty = 30;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(lblLogin, gbc);
	    // --- txtLogin ---------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 80; gbc.weighty = 30;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtLogin, gbc);
	    gbc.insets = new Insets(0, 0, 0, 0); // (top, left, bottom, right)
	    // --- lblMdp ---------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 20; gbc.weighty = 30;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(lblMdp, gbc);
	    // --- txtMdp ---------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 80; gbc.weighty = 30;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtMdp, gbc);
	    // --- txtMdp ---------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 80; gbc.weighty = 30;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(pnlCommande, gbc);
	    
	    // Prise en compte des boutons
	    btnValider.addActionListener(this);
	    btnAnnuler.addActionListener(this);
	    btnModifierMotDePasse.addActionListener(this);
	    //On ajoute le conteneur
	    
	    this.setVisible(true);
	    // Focus
	    txtLogin.requestFocusInWindow();

	}

	private void gestionLogin(boolean modificationMDP) {
		ResultSet result = null;
		String strSql = "";
		String msgJournal = "";
		
		ctn.open();
		// Verifier login et Mdp
		strSql = "SELECT Utilisateur.*, NiveauUtilisateur.Niveau, AlarmeService.idAlarmeService FROM"
				+ " (Utilisateur LEFT JOIN NiveauUtilisateur ON Utilisateur.idNiveauUtilisateur = NiveauUtilisateur.idNiveauUtilisateur)"
				+ " LEFT JOIN AlarmeService ON Utilisateur.idAlarmeService = AlarmeService.idAlarmeService"
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
					
					// Classe GestionUtilisateur
					gestionUtilisateur.setIdUtilisateur(result.getInt("idUtilisateur"));
					gestionUtilisateur.setNiveauUtilisateur(result.getInt("Niveau"));
					gestionUtilisateur.setNomUtilisateur(result.getString("Nom"));
					gestionUtilisateur.setPrenomUtilisateur(result.getString("Prenom"));
					gestionUtilisateur.setIdAlarmeService(result.getInt("idAlarmeService"));
					
					
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
					if(!modificationMDP) {
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
					} else {
						FenMotDePasse fenetre = new FenMotDePasse();
						fenetre.setVisible(true);
						fenetre.requestFocusInWindow();		
					}
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

	private void correctionBase() {
		// Lecture de la table AI_HISTORIQUE
		ctn.open();
//		ResultSet result = ctn.lectureData("SELECT * FROM DI_HISTORIQUE2 WHERE DATELECTURE > '07/06/2018 03:12:00' ORDER BY DATELECTURE ASC");
		ResultSet result = ctn.lectureData("SELECT * FROM AI_HISTORIQUE2"); // WHERE DATELECTURE > '07/06/2018 03:12:00' ORDER BY DATELECTURE ASC");

		try {
			String strSql = "";
			
			String strDateLecture = "";
			while(result.next()) {
//				String strDateApparition = result.getDate("DateLecture").format(formatter);
				
				System.out.println("Date Lecture : " + result.getString("DateLecture"));
				strDateLecture = AE_Fonctions.formatDate(result.getString("DateLecture"), "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss");
				System.out.println("strDateLecture : " + strDateLecture);
				
				strSql = "INSERT INTO AI_Historique (idCapteur, VoieApi, DateLecture, Valeur)" 
	    				+ " VALUES("
	    				+ result.getInt("idCapteur")  
	    				+ ", " + result.getInt("VoieApi")
	    				+ ",'" + strDateLecture + "'" 
	    				+ ", " + result.getDouble("Valeur") 
	    				+ ")";
	    		ctn.fonctionSql(strSql);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Problème lecture dans AI_Historique2");
		}
		ctn.close();
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnValider) {
			gestionLogin(false);
		} // Fin If
		if (e.getSource() == btnAnnuler) {
			this.setVisible(false);
			this.dispose();
		} // Fin If
			
		
		if (e.getSource() == btnModifierMotDePasse) {
			gestionLogin(true);
		} // Fin If
		
	}
	
	
}
