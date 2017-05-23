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
import EFS_Structure.StructTypeMateriel;


public class FenTypeMateriel extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlCorps = new JPanel();
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();	

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
		
	// Propre
	private JComboBox<String> cmbTypeMateriel = new JComboBox<String>();
	private JLabel lblNom = new JLabel("Nom : ");
	private JLabel lblDescription = new JLabel("Description : ");
	private JTextField txtNom = new JTextField("", 20);
	private JTextField txtDescription = new JTextField("", 20);
	private JButton btnModifier = new JButton("Modifier");
	private JButton btnAjouter = new JButton("Nouveau");

	private List<StructTypeMateriel> tbTypeMateriel = new ArrayList<StructTypeMateriel>();	
	
	public FenTypeMateriel() {
		super();
		build();
		LectureDonnees();
	}

	public void build() {
		
		
	    this.setTitle("Type Matériel");
	    this.setSize(800, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    
	    this.add("North",pnlHaut);
	    this.add("Center",pnlCorps);
	    this.add("South", pnlInfo);		

	    cmbTypeMateriel.addActionListener(this);
	    btnModifier.addActionListener(this);
	    btnAjouter.addActionListener(this);	    
	    
		pnlHaut.setTitreEcran("Type Matériel");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

	    GridBagConstraints gbc = new GridBagConstraints();

	    // Corps de l'écran
	    //On définit le layout manager
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
	    pnlCorps.add(cmbTypeMateriel, gbc);
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
	    gbc.weightx = 0;
	    gbc.insets = new Insets(20, 0, 0, 0);
	    gbc.gridx = 1; gbc.gridy = 3;
	    pnlCorps.add(btnModifier, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 3;
	    pnlCorps.add(btnAjouter, gbc);
	    // --------------------------------------------------	    
	    gbc.insets = new Insets(20, 0, 0, 20);
	    gbc.gridx = 3; gbc.gridy = 3;
	    // --------------------------------------------------	    
	    
	} // Fin build
	
	private void LectureDonnees() {
		int index = 0;
		// RAZ combo
		cmbTypeMateriel.removeAllItems();
		tbTypeMateriel.clear();
		// Connection base
		ctn.open();
		// Lecture données
		ResultSet result = ctn.lectureData("SELECT * FROM TypeMateriel");

		// Remplissage tableau et combo
		try {
			while(result.next()) {
				tbTypeMateriel.add(new StructTypeMateriel(result.getLong("idTypeMateriel"), result.getString("Nom"), result.getString("Description")));
				cmbTypeMateriel.addItem(tbTypeMateriel.get(index).getNom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		// Fermeture base
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		ctn.close();
		
	} // Fin LectureDonnees

	private void ModifierEnregistrement(long index) {
		String strSql = new String();
		
		if(AE_Fonctions.messageConfirmation(this, "Voulez vous modifier ce type de matériel  : " + txtNom.getText())) {
			ctn.open();
			strSql = "UPDATE TypeMateriel SET Nom = '" + txtNom.getText() + "', Description = '" +  txtDescription.getText() + "' WHERE idTypeMateriel = " + index; 
			ctn.fonctionSql(strSql);
			ctn.close();
		}
	}

	private void AjouterEnregistrement() {
		String strSql = new String();
		
		if(!AE_Fonctions.enregistrementExiste("TypeMateriel", "Nom", txtNom.getText())) {
			if(AE_Fonctions.messageConfirmation(this, "Voulez vous créer ce type de matériel  : " + txtNom.getText())) {
				ctn.open();
				strSql = "INSERT INTO TypeMateriel (Nom, Description)  VALUES ('" + txtNom.getText() + "', '" +  txtDescription.getText() + "')"; 
				ctn.fonctionSql(strSql);
				ctn.close();
				LectureDonnees();
				cmbTypeMateriel.setSelectedIndex(cmbTypeMateriel.getItemCount() - 1);
			}
		} else {
			AE_Fonctions.afficheMessage(this, "Nom déjà existant ...");
		} // fin if .. else		
		
	}
	
		
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cmbTypeMateriel) {
			int index = cmbTypeMateriel.getSelectedIndex();
			if(index !=  -1) {
				txtNom.setText(tbTypeMateriel.get(index).getNom());
				txtDescription.setText(tbTypeMateriel.get(index).getDescription());
			} 
		}		

		if (e.getSource() == btnModifier) {
			int index = cmbTypeMateriel.getSelectedIndex();
			tbTypeMateriel.get(index).setNom(txtNom.getText());
			tbTypeMateriel.get(index).setDescription(txtDescription.getText());
			ModifierEnregistrement(tbTypeMateriel.get(index).getId());
		}		

		if (e.getSource() == btnAjouter) {
			AjouterEnregistrement();
		}		

	} // Fin actionPerformed		
	
} // Fin class
