package gui.vues;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import em.fonctions.AE_Fonctions;
import em.general.AE_Constantes;
import em.general.EFS_Maitre_Variable;

/**
 * Affiche une barre d'état en bas des écrans
 * @author Eric Mariani
 * @since 01/11/2016
 */
public class AE_BarreBas extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Timer tmrHeure = new Timer(1000, this);	
	private JLabel lblHeure = new JLabel("00:00:00");
	private JLabel lblInformation[] = new JLabel[10];
	private JProgressBar jpbInformation = new JProgressBar();
	
	/**
	 * Constructeur
	 */
	public AE_BarreBas() {
		super();
		build();
		lblInformation[0].setText(AE_Fonctions.calculVersion());
		tmrHeure.start();	
		jpbInformation.setVisible(false);
	}

	/**
	 * Construit la barre
	 */
	private void build (){
		// GridBagLayout
		GridBagLayout gblBarre = new GridBagLayout();
	    this.setLayout(gblBarre);
		GridBagConstraints gbc = new GridBagConstraints();	    

		jpbInformation.setMaximum(100);
		jpbInformation.setMinimum(0);
		jpbInformation.setStringPainted(true);		
		jpbInformation.setPreferredSize(new Dimension(100, 25));
		jpbInformation.setMinimumSize(new Dimension(100, 25));
		
		gbc.insets = new Insets(0, 3, 0, 3);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weightx = 50; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[0] =new JLabel("----------");
		this.add(lblInformation[0], gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[1] =new JLabel("");
		lblInformation[1].setPreferredSize(new Dimension(2, 25));
		lblInformation[1].setOpaque(true);
		lblInformation[1].setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(lblInformation[1], gbc);

		gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; gbc.weighty = 0;
		this.add(jpbInformation, gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 3; gbc.gridy = 0;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[4] =new JLabel("");
		lblInformation[4].setPreferredSize(new Dimension(2, 25));
		lblInformation[4].setOpaque(true);
		lblInformation[4].setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(lblInformation[4], gbc);
		
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 4; gbc.gridy = 0;
		gbc.weightx = 5; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[2] = new JLabel("----");
		lblInformation[2].setPreferredSize(new Dimension(200, 25));
		lblInformation[2].setMinimumSize(new Dimension(200, 25));
		lblInformation[2].setMaximumSize(new Dimension(200, 25));
		this.add(lblInformation[2], gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 5; gbc.gridy = 0;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[3] =new JLabel("");
		lblInformation[3].setPreferredSize(new Dimension(2, 25));
		lblInformation[3].setOpaque(true);
		lblInformation[3].setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(lblInformation[3], gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 6; gbc.gridy = 0;
		gbc.weightx = 5; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[4] = new JLabel("000");
		lblInformation[4].setPreferredSize(new Dimension(200, 25));
		lblInformation[4].setMinimumSize(new Dimension(200, 25));
		lblInformation[4].setMaximumSize(new Dimension(200, 25));
		this.add(lblInformation[4], gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 7; gbc.gridy = 0;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[5] =new JLabel("");
		lblInformation[5].setPreferredSize(new Dimension(2, 25));
		lblInformation[5].setOpaque(true);
		lblInformation[5].setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(lblInformation[5], gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 8; gbc.gridy = 0;
		gbc.weightx = 5; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[6] = new JLabel("000");
		lblInformation[6].setPreferredSize(new Dimension(200, 25));
		lblInformation[6].setMinimumSize(new Dimension(200, 25));
		lblInformation[6].setMaximumSize(new Dimension(200, 25));
		this.add(lblInformation[6], gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 9; gbc.gridy = 0;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		lblInformation[7] =new JLabel("");
		lblInformation[7].setPreferredSize(new Dimension(2, 25));
		lblInformation[7].setOpaque(true);
		lblInformation[7].setBorder(BorderFactory.createRaisedBevelBorder());
		this.add(lblInformation[7], gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 10; gbc.gridy = 0;
		gbc.weightx = 50; gbc.weighty = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		lblHeure.setHorizontalAlignment(JLabel.CENTER);
		lblHeure.setSize(100, 25);
		lblHeure.setPreferredSize(new Dimension(100, 25));
		lblHeure.setMinimumSize(new Dimension(100, 25));
		lblHeure.setMaximumSize(new Dimension(100, 25));
		this.add(lblHeure, gbc);

		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	
	} // Fin build

	/**
	 * Affiche un message d'inforamtion dans le cadre demandé
	 * @param index
	 * 		Index du cadre demandé
	 * @param message
	 * 		Message à afficher
	 */
	public void setLblInformation(int index, String message) {
		lblInformation[index].setText(message);
	}
	
	/**
	 * Gére la barre de progression
	 * @param value
	 * 		Valeur entre 0 et 100
	 */
	public void setJpbInformationValue(int value) {
		jpbInformation.setValue(value);
	}
	
	/**
	 * Gestion du timer pour afficher l'heure
	 */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == tmrHeure) {
			lblHeure.setText(AE_Fonctions.formatDate("HH:mm:ss"));
			lblInformation[4].setText("API : " + EFS_Maitre_Variable.compteurErreurAPI);
			if(EFS_Maitre_Variable.compteurErreurAPI > 0) {
				lblInformation[4].setBackground(AE_Constantes.AE_ROUGE);
				lblInformation[4].setOpaque(true);
				lblInformation[4].setForeground(AE_Constantes.AE_BLANC);
			}
			lblInformation[6].setText("SGBD : " + EFS_Maitre_Variable.compteurErreurSGBD);
			if(EFS_Maitre_Variable.compteurErreurSGBD > 0) {
				lblInformation[6].setBackground(AE_Constantes.AE_ROUGE);
				lblInformation[6].setOpaque(true);
				lblInformation[6].setForeground(AE_Constantes.AE_BLANC);
			}
		} 
	} 	
		
	
} // Fin Class
