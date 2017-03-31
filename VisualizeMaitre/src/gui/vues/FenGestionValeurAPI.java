package gui.vues;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import em.communication.AE_TCP_Connection;
import em.communication.AE_TCP_Modbus;
import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.EFS_General;
import em.general.EFS_Maitre_Variable;

/**
 * Controle des valeurs API
 * @author Eric Mariani
 * @since 16/02/2017
 *
 */
public class FenGestionValeurAPI extends JFrame implements AE_Constantes, ActionListener{
	private static final long serialVersionUID = 1L;
	// GUI
	private AE_BarreHaut pnlEntete = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private JPanel pnlCorps = new JPanel();

	// ==> pnlCorps <== 
	private JLabel lblTitreAdresse = new JLabel("Adresse API :");
	private JLabel lblTitreValeur = new JLabel("Valeur API :");
	private JTextField txtAdresse = new JTextField("0");
	private JTextField txtValeur = new JTextField("0");
	private JPanel pnlBoutons = new JPanel();
	// ==> pnlBoutons <== 
    private JButton btnLecture = new JButton("Lecture");
    private JButton btnModifier = new JButton("Modifier");
	
	/**
	 * Constructeur vide
	 */
	public FenGestionValeurAPI() {
		super();
		build();
		this.setVisible(true);
	}
	
	/**
	 * Description de la fenêtre
	*/
	private void build() {
		this.setTitle("Visualize MAITRE - Gestion valeurs API");
	    this.setSize(500, 300);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    pnlEntete.setTitreEcran("Gestion valeurs API");
	    
	    // Definition des parties de la fenetre
	    this.add("North",pnlEntete);
	    this.add("Center", pnlCorps);
	    this.add("South", pnlInfo);		
	    
	    // Actions
	    btnLecture.addActionListener(this);
	    btnModifier.addActionListener(this);
	    
		GridBagConstraints gbc = new GridBagConstraints();

	    pnlCorps.setLayout(new GridBagLayout());
	    pnlCorps.setBackground(AE_BLEU);

	    gbc.insets = new Insets(2, 10, 2, 10); // (top, left, bottom, right)
	    // --- lblTitreAdresse --------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 60;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(lblTitreAdresse, gbc);
	    // --- txtAdresse -------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 40;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtAdresse, gbc);
	    // --- lblTitreValeur ---------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 60;
	    gbc.anchor= GridBagConstraints.LINE_END;
	    gbc.fill = GridBagConstraints.NONE;
	    pnlCorps.add(lblTitreValeur, gbc);
	    // --- txtAdresse -------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.weightx = 40;
	    gbc.anchor= GridBagConstraints.CENTER;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(txtValeur, gbc);
	    // --- pnlBoutons -------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 2;
	    gbc.gridheight = 1; gbc.gridwidth = 2;
	    gbc.weightx = 30;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlCorps.add(pnlBoutons, gbc);
	    
	    
        pnlBoutons.setLayout(new FlowLayout());
	    pnlBoutons.setBackground(AE_BLEU);
        pnlBoutons.add(btnLecture);
        pnlBoutons.add(btnModifier);

	}

	/**
	 * Lit la valeur dans l'API
	 */
	private void lectureValeur() {
		try {
			int adresseLecture = Integer.valueOf(txtAdresse.getText());

			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			double [] reqReponse;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, EFS_General.MODBUS_PORT);
			con.connect();
			if (!con.isConnected()) {
				GestionLogger.gestionLogger.warning("Erreur connexion MODBUS ... ");
			}
			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, adresseLecture, 1));
			txtValeur.setText(String.valueOf(reqReponse[0]));
			con.close();
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur lecture MODBUS : " + e.getMessage());
		} // Fin catch	
		
	}
	
	/**
	 * Modidifie une valeur dans l'API
	 */
	private void modifierValeur() {
		try {
			int adresseLecture = Integer.valueOf(txtAdresse.getText());
			int valeurAPI = Integer.valueOf(txtValeur.getText());
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			@SuppressWarnings("unused")
			double [] reqReponse = null;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, EFS_General.MODBUS_PORT);
			con.connect();
			if (!con.isConnected()) {
				GestionLogger.gestionLogger.warning("Erreur connexion MODBUS ... ");
			}
//			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, adresseLecture, valeurAPI));			
			GestionLogger.gestionLogger.warning("[API] Modification valeur API adresse = " + adresseLecture + " - Vaaleur = " + valeurAPI);
			con.close();
            
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture MODBUS : " + e.getMessage());
		} // Fin catch				
	}
	
	/**
	 * Gestion des actions
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnLecture) {
			lectureValeur();
		}
		if(ae.getSource() == btnModifier) {
			modifierValeur();
		}
	}
}
