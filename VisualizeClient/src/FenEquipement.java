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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import AE_General.*;
import EFS_Structure.StructEquipement;

public class FenEquipement extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlCorps = new JPanel();
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();	

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Propre
	private JComboBox<String> cmbEquipement = new JComboBox<String>();
	private JLabel lblNom = new JLabel("Nom : ");
	private JLabel lblDescription = new JLabel("Designation : ");
	private JLabel lblInventaire = new JLabel("Numéro inventaire : ");
	private JTextField txtNom = new JTextField("", 20);
	private JTextField txtDescription = new JTextField("", 20);
	private JTextField txtInventaire = new JTextField("", 20);
	private JButton btnModifier = new JButton("Modifier");
	private JButton btnAjouter = new JButton("Nouveau");

	private List<StructEquipement> tbEquipement = new ArrayList<StructEquipement>();	
	
	public FenEquipement() {
		super();
		build();
		System.out.println("Passage 4");		
		LectureDonnees();		
		System.out.println("Passage 5");		
	}

	public void build() {
		
		
	    this.setTitle("Equipement");
	    this.setSize(800, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    
	    this.add("North",pnlHaut);
	    this.add("Center",pnlCorps);
	    this.add("South", pnlInfo);		

	    cmbEquipement.addActionListener(this);
	    btnModifier.addActionListener(this);
	    btnAjouter.addActionListener(this);
	    
		pnlHaut.setTitreEcran("Equipement");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();

	    pnlCorps.setLayout(new GridBagLayout());
	    
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
	    pnlCorps.add(cmbEquipement, gbc);
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
	    pnlCorps.add(lblDescription, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 2;
	    pnlCorps.add(txtDescription, gbc);
	    gbc.fill = GridBagConstraints.NONE;
	    // --------------------------------------------------
	    gbc.weightx = 25;
	    gbc.gridx = 0; gbc.gridy = 3;
	    pnlCorps.add(lblInventaire, gbc);
	    // --------------------------------------------------
	    gbc.weightx = 75;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1; gbc.gridy = 3;
	    pnlCorps.add(txtInventaire, gbc);
	    gbc.fill = GridBagConstraints.NONE;	    
	    // --------------------------------------------------

	    
	    // --------------------------------------------------
	    gbc.weightx = 0;
	    gbc.insets = new Insets(20, 0, 0, 0);
	    gbc.gridx = 1; gbc.gridy = 8;
	    pnlCorps.add(btnModifier, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 8;
	    pnlCorps.add(btnAjouter, gbc);
	    // --------------------------------------------------	    
	    
	} // Fin build
	
	
	private void LectureDonnees() {
		int index = 0;
		// Connection base
		ctn.open();
		
		// Lecture données
		cmbEquipement.removeAllItems();
		tbEquipement.clear();
		
		ResultSet result = ctn.lectureData("SELECT * FROM Equipement");
		index = 0;
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				tbEquipement.add(new StructEquipement(result.getLong("idEquipement"), result.getString("Nom"), result.getString("Description"), result.getString("NumeroInventaire"), 
						result.getLong("idTypeMateriel"), result.getLong("idPosteTechnique"), result.getLong("idZoneSubstitution")));
				cmbEquipement.addItem(tbEquipement.get(index).getNom());
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
		
		if(AE_Fonctions.messageConfirmation(this, "Voulez vous modifier cet équipement  : " + txtNom.getText())) {
			ctn.open();
			strSql = "UPDATE Equipement SET Nom = '" + txtNom.getText() + "', Description = '" +  txtDescription.getText() + "', NumeroInventaire = '" + txtInventaire.getText() + "'"  
					 + " WHERE idEquipement = " + index; 
			ctn.fonctionSql(strSql);
			ctn.close();
		} // fin if
	}

	private void AjouterEnregistrement() {
		String strSql = new String();
		
		if(!AE_Fonctions.enregistrementExiste("Equipement", "Nom", txtNom.getText())) {
			if(AE_Fonctions.messageConfirmation(this, "Voulez vous créer cet équipement : " + txtNom.getText())) {
				ctn.open();
				strSql = "INSERT INTO Equipement (Nom, Description, NumeroInventaire)  VALUES ('" + txtNom.getText() + "', '" 
				       +  txtDescription.getText()  + "', '" +  txtInventaire.getText() + "')";
				System.out.println(strSql);
				ctn.fonctionSql(strSql);
				ctn.close();
				LectureDonnees();
				cmbEquipement.setSelectedIndex(cmbEquipement.getItemCount() - 1);
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Nom déjà existant ...");
		} // fin if .. else		
				
	}
		
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cmbEquipement) {
			int index = cmbEquipement.getSelectedIndex();
			if(index !=  -1) {
				txtNom.setText(tbEquipement.get(index).getNom());
				txtDescription.setText(tbEquipement.get(index).getDescription());
				txtInventaire.setText(tbEquipement.get(index).getNumeroInventaire());
			}
		}		

		if (e.getSource() == btnModifier) {
			int index = cmbEquipement.getSelectedIndex();
			
			tbEquipement.get(index).setNom(txtNom.getText());
			tbEquipement.get(index).setDescription(txtDescription.getText());
			tbEquipement.get(index).setNumeroInventaire(txtInventaire.getText());
			
			ModifierEnregistrement(tbEquipement.get(index).getId());
		}		

		if (e.getSource() == btnAjouter) {
			AjouterEnregistrement();
		}		

	} // Fin actionPerformed	
	
	
} // Fin class
