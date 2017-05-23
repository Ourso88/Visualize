package AE_General;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class AE_BarreHaut extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel lblTitreEcran = new JLabel("---");

	public AE_BarreHaut() {
		super();
		build();
		
		
	}
	
	public void build() {
		ImageIcon iconPhoto = new ImageIcon(getClass().getResource("/logo_efs.jpg"));
	    JLabel lblTitreEFS = new JLabel("Etablissement Français du Sang");
	    JLabel lblImgEFS = new JLabel(iconPhoto);

		GridBagLayout gblBarre = new GridBagLayout();
	    this.setLayout(gblBarre);		
		GridBagConstraints gbc = new GridBagConstraints();		
		
	    this.setBackground(new Color(50, 98, 149));
	    

	    lblImgEFS.setAlignmentX(LEFT_ALIGNMENT);
	    
	    lblTitreEFS.setAlignmentX(LEFT_ALIGNMENT);
	    lblTitreEFS.setForeground(Color.white);
	    lblTitreEcran.setAlignmentX(LEFT_ALIGNMENT);
	    lblTitreEcran.setForeground(Color.yellow);
	    Font font_16 = new Font("Arial",Font.BOLD,16);
	    lblTitreEFS.setFont(font_16);
	    Font font_18 = new Font("Arial",Font.BOLD,24);
	    lblTitreEcran.setFont(font_18);
		
		
	    // Position de la case de départ
//		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 10; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblImgEFS, gbc);		    

		
//	    gbc.insets = new Insets(0, 50, 20, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.weightx = 50; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblTitreEFS, gbc);			

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2; gbc.gridy = 0;
		gbc.weightx = 50; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblTitreEcran, gbc);			

		
	}

	public void setTitreEcran(String txtTitre) {
		lblTitreEcran.setText(txtTitre);
	}
	
	
}
