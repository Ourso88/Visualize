package gui.vues;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Graphisme du haut de chaque écran
 * @author Eric Mariani
 * @since 29/10/2016
 * 
 */
public class AE_BarreHaut extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel lblTitreEcran = new JLabel("---");


	/**
	 * Constructeur vide
	 */
	public AE_BarreHaut() {
		super();
		build();
	}

	
	/**
	 * Construit la barre haute
	 */
	public void build() {
		ImageIcon iconPhoto = new ImageIcon(getClass().getResource("/logo_efs.jpg"));
	    JLabel lblTitreGE = new JLabel("GTC - Visualize Maitre");
	    JLabel lblImgGE = new JLabel(iconPhoto);

		GridBagLayout gblBarre = new GridBagLayout();
	    this.setLayout(gblBarre);		
		GridBagConstraints gbc = new GridBagConstraints();		
		
	    this.setBackground(new Color(50, 98, 149));
	    

	    lblImgGE.setAlignmentX(LEFT_ALIGNMENT);
	    
	    lblTitreGE.setAlignmentX(LEFT_ALIGNMENT);
	    lblTitreGE.setForeground(Color.white);
	    lblTitreEcran.setAlignmentX(LEFT_ALIGNMENT);
	    lblTitreEcran.setForeground(Color.yellow);
	    Font font_16 = new Font("Arial",Font.BOLD,16);
	    lblTitreGE.setFont(font_16);
	    Font font_18 = new Font("Arial",Font.BOLD,24);
	    lblTitreEcran.setFont(font_18);
		
		
	    // Position de la case de départ
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 10; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblImgGE, gbc);		    
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.weightx = 50; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblTitreGE, gbc);			

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2; gbc.gridy = 0;
		gbc.weightx = 50; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblTitreEcran, gbc);			
	}

	/**
	 * Change le titre de l'écran
	 * @param txtTitre
	 */
	public void setTitreEcran(String txtTitre) {
		lblTitreEcran.setText(txtTitre);
	}
	
	/**
	 * Récupére le titre de l'écran
	 * @param txtTitre
	 */
	public String getTitreEcran() {
		return lblTitreEcran.getText();
	}
	
}
