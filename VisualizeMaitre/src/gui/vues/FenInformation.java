package gui.vues;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import em.general.AE_Constantes;
import kernel.VoiesAPI;

/**
 * 
 * @author Eric Mariani
 * @since 07/03/2017
 *
 */
public class FenInformation extends JFrame implements AE_Constantes, VoiesAPI, ActionListener {
	private static final long serialVersionUID = 1L;

	private JPanel pnlInformation = new JPanel();
	private JLabel lblTitreSeuilBas = new JLabel("Seuil bas : ");
	private JTextField txtSeuilBas = new JTextField("");
	private JLabel lblTitreSeuilHaut = new JLabel("Seuil Haut : ");
	private JTextField txtSeuilHaut = new JTextField("");
	private JLabel lblTitreTempo = new JLabel("Tempo : ");
	private JTextField txtTempo = new JTextField("");
	private JLabel lblTitreCalibration = new JLabel("Calibration :");
	private JLabel lblCalibration = new JLabel("0");

	private JButton btnModifierSeuilBas = new JButton("Modif. seuil bas");
	private JButton btnModifierSeuilHaut = new JButton("Modif. seuil haut");
	private JButton btnModifierTempo = new JButton("Modif. tempo");
	
	private int indexCapteur;
	
	/**
	 * Constructeur
	 * @param indexCapteur
	 */
	public FenInformation(int indexCapteur) {
		this.indexCapteur = indexCapteur;
		build();
		remplirFiche();
	}
	
	/**
	 * Construit la fenêtre
	 */
	private void build() {
	    this.setTitle("Information capteur");
	    this.setSize(800, 200);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    // Actions
	    btnModifierSeuilBas.addActionListener(this);
	    btnModifierSeuilHaut.addActionListener(this);
	    btnModifierTempo.addActionListener(this);
	    
	    JLabel lblTitreEnCours = new JLabel("Capteur : " + tbAnaAPI.get(indexCapteur).getNom() + " : " + tbAnaAPI.get(indexCapteur).getDescription());
	    lblTitreEnCours.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreEnCours.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreEnCours.setBackground(AE_MARRON);
	    lblTitreEnCours.setOpaque(true);
	    
		GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(0, 5, 10, 10); // (top, left, bottom, right)
	    pnlInformation.setLayout(new GridBagLayout());
	    // --- lblTitreSeuilBas -------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInformation.add(lblTitreSeuilBas, gbc);
	    // --- txtSeuilBas ------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(txtSeuilBas, gbc);
	    // --- lblTitreSeuilHaut ------------------------------------
	    gbc.gridx = 2; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInformation.add(lblTitreSeuilHaut, gbc);
	    // --- txtSeuilHaut -----------------------------------------
	    gbc.gridx = 3; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(txtSeuilHaut, gbc);
	    // --- lblTitreTempo ----------------------------------------
	    gbc.gridx = 4; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInformation.add(lblTitreTempo, gbc);
	    // --- txtTempo ---------------------------------------------
	    gbc.gridx = 5; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(txtTempo, gbc);
	    // --- lblTitreCalibration ----------------------------------
	    gbc.gridx = 6; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlInformation.add(lblTitreCalibration, gbc);
	    // --- lblCalibration ---------------------------------------
	    gbc.gridx = 7; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 10;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(lblCalibration, gbc);
	    // --- btnModifierSeuilBas ----------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 30;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(btnModifierSeuilBas, gbc);
	    // --- btnModifierSeuilHaut ---------------------------------
	    gbc.gridx = 2; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 30;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(btnModifierSeuilHaut, gbc);
	    // --- btnModifierTempo ----------------------------------
	    gbc.gridx = 4; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 30;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlInformation.add(btnModifierTempo, gbc);
	    
	    this.add("North", lblTitreEnCours);
	    this.add("Center", pnlInformation);
	}

	/**
	 * Remplit les inforamations
	 */
	private void remplirFiche() {
		txtSeuilBas.setText(String.valueOf(tbAnaAPI.get(indexCapteur).getSeuilBas() / 10));
		txtSeuilHaut.setText(String.valueOf(tbAnaAPI.get(indexCapteur).getSeuilHaut() / 10));
		txtTempo.setText(String.valueOf(tbAnaAPI.get(indexCapteur).getSeuilTempo()));
		lblCalibration.setText(String.valueOf(tbAnaAPI.get(indexCapteur).getCalibration() / 10));
	}
	
	/**
	 * Gere les actions des boutons
	 * @param ae
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
	}
}
