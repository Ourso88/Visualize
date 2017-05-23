package AE_General;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;





import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

public class AE_BarreBas extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Timer tmrHeure = new Timer(1000, this);	
	private JLabel lblHeure = new JLabel("00:00:00");
	private JLabel lblInformation[] = new JLabel[10];
	private JProgressBar jpbInformation = new JProgressBar();
	
	private DateFormat dfm = new SimpleDateFormat("EEE HH:mm:ss");
	
	public AE_BarreBas() {
		super();
		build();
		lblInformation[0].setText(AE_Fonctions.calculVersion());
		tmrHeure.start();	
	}
	
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
		lblInformation[0] = new JLabel("----------");
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
		lblInformation[2] =new JLabel("----");
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

	public void setLblInformation(int index, String message) {
		lblInformation[index].setText(message);
	}
	
	public void setJpbInformationValue(int value) {
		jpbInformation.setValue(value);
	}
	
	public void testJpbInformation() {
		for(int i = 0; i < 100; i++) {
			setJpbInformationValue(i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == tmrHeure) {
			Calendar dateDepart = Calendar.getInstance();
			lblHeure.setText(dfm.format(dateDepart.getTime()));
		} // Fin If
	} // Fin actionPerformed	
		
	
} // Fin Class
