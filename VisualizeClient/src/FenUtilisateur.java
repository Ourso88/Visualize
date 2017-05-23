import java.awt.Color;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import AE_General.*;
import EFS_Structure.StructNiveauUtilisateur;
import EFS_Structure.StructService;
import EFS_Structure.StructUtilisateur;


public class FenUtilisateur extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlCorps = new JPanel();
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();	

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Propre
	private JComboBox<String> cmbUtilisateur = new JComboBox<String>();
	private JLabel lblNom = new JLabel("Nom : ");
	private JLabel lblPrenom = new JLabel("Prenom : ");
	private JLabel lblLogin = new JLabel("Login : ");
	private JLabel lblMdp = new JLabel("Mot de passe : ");
	private JLabel lblNiveau = new JLabel("Niveau : ");
	private JLabel lblService = new JLabel("Service : ");
	private JTextField txtNom = new JTextField("", 20);
	private JTextField txtPrenom = new JTextField("", 20);
	private JTextField txtLogin = new JTextField("", 20);
	private JPasswordField txtMdp = new JPasswordField("", 20);	
	private JComboBox<String> cmbNiveau = new JComboBox<String>();
	private JButton btnModifier = new JButton("Modifier");
	private JButton btnAjouter = new JButton("Nouveau");
	private JButton btnListe = new JButton("Liste ++ ");

	private List<StructUtilisateur> tbUtilisateur = new ArrayList<StructUtilisateur>();	
	private List<StructNiveauUtilisateur> tbNiveauUtilisateur = new ArrayList<StructNiveauUtilisateur>();

	private List<StructService> tbService = new ArrayList<StructService>();	
	private DefaultListModel<String> modeleLstService = new DefaultListModel<String>();
	private JList<String> lstService = new JList<String>(modeleLstService);
	private JScrollPane spLstService = new JScrollPane(lstService);
	
//	private AE_ListeBase lstService = new AE_ListeBase(ctn);
	
	public FenUtilisateur() {
		super();
		build();
		LectureNiveauUtilisateur();
		LectureDonnees();
	}

	public void build() {
		
	    this.setTitle("Utilisateur");
	    this.setSize(800, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    
	    this.add("North",pnlHaut);
	    this.add("Center",pnlCorps);
	    this.add("South", pnlInfo);		

	    cmbUtilisateur.addActionListener(this);
	    btnModifier.addActionListener(this);
	    btnAjouter.addActionListener(this);
	    btnListe.addActionListener(this);
	    
		pnlHaut.setTitreEcran("Utilisateur");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    
	    lblLogin.setForeground(Color.red);
	    
	    lstService.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    lstService.setVisibleRowCount(6);

	    GridBagConstraints gbc = new GridBagConstraints();

	    // Corps de l'écran
	    //On définit le layout manager
	    pnlCorps.setLayout(new GridBagLayout());
	    //L'objet servant à positionner les composants
	    
	    // --------------------------------------------------
	    // Position de la case de départ
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    // taille et hauteur
	    gbc.gridheight = 1;
	    gbc.gridwidth = 1;
	    // Ancrage
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(4, 0, 0, 0);
	    pnlCorps.add(cmbUtilisateur, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 1;
	    pnlCorps.add(lblNom, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 1;
	    pnlCorps.add(txtNom, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 2;
	    pnlCorps.add(lblPrenom, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 2;
	    pnlCorps.add(txtPrenom, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 3;
	    pnlCorps.add(lblLogin, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 3;
	    pnlCorps.add(txtLogin, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 4;
	    pnlCorps.add(lblMdp, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 4;
	    pnlCorps.add(txtMdp, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 5;
	    pnlCorps.add(lblNiveau, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 5;
	    pnlCorps.add(cmbNiveau, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 6;
	    pnlCorps.add(lblService, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.gridheight = 6;
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.gridx = 1; gbc.gridy = 6;
	    pnlCorps.add(spLstService, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    gbc.gridheight = 1;
	    // --------------------------------------------------
	    gbc.insets = new Insets(0, 0, 0, 5);
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 7;
	    pnlCorps.add(btnListe, gbc);
	    gbc.ipadx = 0;
	    // --------------------------------------------------
	    
	    
	    // --------- Boutons --------------------------------
	    gbc.weightx = 0;
	    gbc.insets = new Insets(20, 0, 0, 0);
	    gbc.gridx = 1; gbc.gridy = 20;
	    pnlCorps.add(btnModifier, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 20;
	    pnlCorps.add(btnAjouter, gbc);
	    // --------------------------------------------------	    
	    
	} // Fin build
	
	private void LectureNiveauUtilisateur() {
		int index = 0;
		// RAZ combo
		cmbNiveau.removeAllItems();
		tbNiveauUtilisateur.clear();
		// Connection base
		ctn.open();
		// Lecture données
		
		ResultSet result = ctn.lectureData("SELECT * FROM NiveauUtilisateur");
		index = 0;
		// Remplissage tableau et combo NiveauUtilisateur
		try {
			while(result.next()) {
				tbNiveauUtilisateur.add(new StructNiveauUtilisateur(result.getLong("idNiveauUtilisateur"), result.getString("Nom"), result.getString("Description")));
				cmbNiveau.addItem(tbNiveauUtilisateur.get(index).getNom() + " - " + tbNiveauUtilisateur.get(index).getDescription());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.close();
	}
	
	private void LectureDonnees() {
		int index = 0;
		// Connection base
		ctn.open();
		// Lecture données
		cmbUtilisateur.removeAllItems();
		tbUtilisateur.clear();
		
		ResultSet result = ctn.lectureData("SELECT * FROM Utilisateur");
		index = 0;
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				tbUtilisateur.add(new StructUtilisateur(result.getLong("idUtilisateur"), result.getString("Nom"), result.getString("Prenom"), result.getString("Login"), result.getString("MotDePasse"), result.getLong("idNiveauUtilisateur")));
				cmbUtilisateur.addItem(tbUtilisateur.get(index).getNom() + " " + tbUtilisateur.get(index).getPrenom());
				index++;
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
		ctn.close();
		
	} // Fin LectureDonnees

	private void ModifierEnregistrement(long index) {
		String strSql = new String();
		String psw = new String(txtMdp.getPassword());
		long idNiveauUtilisateur = tbNiveauUtilisateur.get(cmbNiveau.getSelectedIndex()).getId();
		long idService = -1;
		

		
		if(AE_Fonctions.messageConfirmation(this, "Voulez vous modifier cet utilisateur  : " + txtNom.getText())) {
			ctn.open();
			strSql = "UPDATE Utilisateur SET Nom = '" + txtNom.getText() + "', Prenom = '" +  txtPrenom.getText() + "', Login = '" + txtLogin.getText() + "', MotDePasse = '" + psw + "', idNiveauUtilisateur = " +  idNiveauUtilisateur +  " WHERE idUtilisateur = " + index; 
			ctn.fonctionSql(strSql);
	
			strSql = "DELETE FROM LienUtilisateurService WHERE idUtilisateur = " + index;
			ctn.fonctionSql(strSql);
	
			int tbSelection[] = new int[tbService.size()];
			for(int i = 0; i < tbService.size(); i++) {
				tbSelection[i] = -1;
			}
			tbSelection = lstService.getSelectedIndices();
			for(int i = 0; i < tbSelection.length; i++) {
				
				System.out.println("tb  : " + tbSelection[i]);
				idService = tbService.get(tbSelection[i]).getId();
				strSql = "INSERT INTO LienUtilisateurService (idService, idUtilisateur) VALUES(" + idService + ", " + index + ")";
				ctn.fonctionSql(strSql);
			}
			
			ctn.close();
		} // fin if
	}

	private void AjouterEnregistrement() {
		String strSql = new String();
		String psw = new String(txtMdp.getPassword());
		long idNiveau = tbNiveauUtilisateur.get(cmbNiveau.getSelectedIndex()).getId();
		
		if(!AE_Fonctions.enregistrementExiste("Utilisateur", "Nom", txtNom.getText())) {
			if(AE_Fonctions.messageConfirmation(this, "Voulez vous créer cet utilisateur  : " + txtNom.getText())) {
				ctn.open();
				strSql = "INSERT INTO Utilisateur (Nom, Prenom, Login, MotDePasse, idNiveauUtilisateur)  VALUES ('" + txtNom.getText() + "', '" +  txtPrenom.getText()  + "', '" +  txtLogin.getText() + "', '" + psw + "', " + idNiveau + ")";
				System.out.println(strSql);
				ctn.fonctionSql(strSql);
		
				ctn.close();
				LectureDonnees();
				cmbUtilisateur.setSelectedIndex(cmbUtilisateur.getItemCount() - 1);
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Nom déjà existant ...");
		} // fin if .. else	
	}
		
	private void SelectionNiveauUtilisateur(long index) {
		for(int i = 0; i < tbNiveauUtilisateur.size() ; i++) {
			if (tbNiveauUtilisateur.get(i).getId() == index) {
				cmbNiveau.setSelectedIndex(i);
			}
		}
	}
	
	private void remplirService(String strSql) {
		// Connection base
		ctn.open();
		// Lecture données
		tbService.clear();
		modeleLstService.removeAllElements();
		ResultSet result = ctn.lectureData(strSql);
		// Remplissage de la liste
		try {
			while(result.next()) {
				tbService.add(new StructService(result.getLong("idService"), result.getString("Nom"), result.getString("Description")));
				modeleLstService.addElement(result.getString("Nom"));
				System.out.println(result.getString("Nom"));
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
		ctn.close();	
	}
	
	private void selectionService(int index) {
		int tbSelection[] = new int[tbService.size()];
		int cpt = 0;
		
		for(int i = 0; i < tbService.size(); i++) {
			tbSelection[i] = -1;
		}
		
		ctn.open();
		ResultSet result = ctn.lectureData("SELECT * FROM LienUtilisateurService WHERE idUtilisateur = " + tbUtilisateur.get(index).getId());
		System.out.println("tbService.size() = " + tbService.size());
		System.out.println("index = " + index);

		try {
			while(result.next()) {
				System.out.println("Passage result !");
				for(int i = 0; i < tbService.size() ; i++) {
					if (tbService.get(i).getId() == result.getLong("idService")) {
						tbSelection[cpt++] = i;
						lstService.setSelectedIndex(i);
						System.out.println("Selection de i = " + i);
					}
					System.out.println("Passage pour i = " + i);

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
		ctn.close();		

		lstService.setSelectedIndices(tbSelection);
	
	
	} // Fin selectionService
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cmbUtilisateur) {
			int index = cmbUtilisateur.getSelectedIndex();
			if(index !=  -1) {
				txtNom.setText(tbUtilisateur.get(index).getNom());
				txtPrenom.setText(tbUtilisateur.get(index).getPrenom());
				txtLogin.setText(tbUtilisateur.get(index).getLogin());
				txtMdp.setText(tbUtilisateur.get(index).getMotDePasse());
				SelectionNiveauUtilisateur(tbUtilisateur.get(index).getIdNiveauUtilisateur());
				// Remplir la liste des services
				String strSql = new String("SELECT Service.* FROM (LienUtilisateurService LEFT JOIN Service ON LienUtilisateurService.idService = Service.idService) WHERE idUtilisateur = " + tbUtilisateur.get(index).getId());
				remplirService(strSql);
//				lstService.remplirListe(strSql);
			} 
		}		

		if (e.getSource() == btnModifier) {
			int index = cmbUtilisateur.getSelectedIndex();
			String psw = new String(txtMdp.getPassword());
			long idNiveauUtilisateur = tbNiveauUtilisateur.get(cmbNiveau.getSelectedIndex()).getId();
			
			tbUtilisateur.get(index).setNom(txtNom.getText());
			tbUtilisateur.get(index).setPrenom(txtPrenom.getText());
			tbUtilisateur.get(index).setLogin(txtLogin.getText());
			tbUtilisateur.get(index).setMotDePasse(psw);
			tbUtilisateur.get(index).setIdNiveauUtilisateur(idNiveauUtilisateur);
			
			ModifierEnregistrement(tbUtilisateur.get(index).getId());
		}		

		if (e.getSource() == btnAjouter) {
			AjouterEnregistrement();
		}		

		if (e.getSource() == btnListe) {
			int index = cmbUtilisateur.getSelectedIndex();
			// Remplir la liste des services
			String strSql = new String("SELECT Service.* FROM Service"); // + tbUtilisateur.get(index).getId());
			remplirService(strSql);
			selectionService(index);
//			lstService.remplirListe(strSql);
		} // btnListe
		
		
	} // Fin actionPerformed		
	
} // Fin class
