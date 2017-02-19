package gui.vues;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.EFS_General;
import em.general.EFS_Maitre_Variable;
import em.general.JTableConstantes;
import gui.modeles.ModeleJTableVoiesAnalogicAPI;
import kernel.AnalogicInput;
import kernel.VoiesAPI;

/**
 * Suivi des valeurs API Analogiques
 * @author Eric Mariani
 * @since 16/02/2017
 *
 */
public class FenVoiesAnalogicAPI extends JFrame implements AE_Constantes, VoiesAPI, ActionListener, JTableConstantes {
	private static final long serialVersionUID = 1L;
	// GUI
	private AE_BarreHaut pnlEntete = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private JPanel pnlCorps = new JPanel();
	
	// ==> pnlCorps <== 
	private JPanel pnlBoutons = new JPanel();
    private ModeleJTableVoiesAnalogicAPI mdlAnalogicInput = new ModeleJTableVoiesAnalogicAPI();	
    private JTable jtbVoiesAPI;
    TableRowSorter<TableModel> sorterJtbVoiesAPI;	
	// ==> pnlBoutons <==
    private JButton btnValeurAPI = new JButton("Modifier valeur API");
	
    // Timer
    private Timer tmrTpsReel = new Timer(EFS_General.TIMER_FEN_VOIES_ANALOGIC, this);	

    
	/**
	 * Constructeur vide
	 */
	public FenVoiesAnalogicAPI() {
		super();
		build();
		remplirTableauVoiesAnalogiques();
		this.setVisible(true);
		tmrTpsReel.start();
	}
	
	/**
	 * Description de la fenêtre
	*/
	private void build() {
		this.setTitle("Visualize MAITRE - Voies analogiques API");
	    this.setSize(800, 600);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    pnlEntete.setTitreEcran("Voies analogiques API");
	    
	    // Actions
	    btnValeurAPI.addActionListener(this);
	    
	    // Definition des parties de la fenetre
	    this.add("North",pnlEntete);
	    this.add("Center", pnlCorps);
	    this.add("South", pnlInfo);		
	    
	    // Tableau jtbVoiesAPI
	    jtbVoiesAPI = new JTable(mdlAnalogicInput);
	    jtbVoiesAPI.setFillsViewportHeight(true);        
	    jtbVoiesAPI.setBackground(AE_BLEU);
	    
	    sorterJtbVoiesAPI =  new TableRowSorter<TableModel>(jtbVoiesAPI.getModel());
        jtbVoiesAPI.setRowSorter(sorterJtbVoiesAPI);        
        sorterJtbVoiesAPI.setSortable(0,  false);
        sorterJtbVoiesAPI.setSortsOnUpdates(true);
        
        JScrollPane jspControleVoies = new JScrollPane(jtbVoiesAPI);
        jspControleVoies.setBackground(AE_BLEU);
        jspControleVoies.setOpaque(true);
        pnlCorps.setLayout(new BorderLayout());
        pnlCorps.add("North", pnlBoutons);
        pnlCorps.add("Center", jspControleVoies);
        
        pnlBoutons.setLayout(new FlowLayout());
        pnlBoutons.add(btnValeurAPI);
	}
	
	/**
	 * Raffraichissement des valeurs du tableau
	 */
	private void raffraichissementValeur() {
		for(int i = 0; i < tbAnaAPI.size(); i++) {
			mdlAnalogicInput.setValueAt(tbAnaAPI.get(i).getValeurAPI(), i, JT_VOIES_API_VALEUR);
			mdlAnalogicInput.setValueAt(tbAnaAPI.get(i).getSeuilAtteint(), i, JT_VOIES_API_SEUIL_ATTEINT);
			mdlAnalogicInput.setValueAt(tbAnaAPI.get(i).isAlarmeEnclenchee(), i, JT_VOIES_API_ALARME_ENCLENCHEE);
			mdlAnalogicInput.setValueAt(tbAnaAPI.get(i).getDateAlarmeApparition(), i, JT_VOIES_API_DATE_APPARITION_ALARME);
			mdlAnalogicInput.setValueAt(tbAnaAPI.get(i).isAlarmeTempoEcoulee(), i, JT_VOIES_API_TEMPO_ALARME_ECOULEE);
		}
		
		pnlInfo.setLblInformation(2, "Cpt : " + EFS_Maitre_Variable.nombreLectureAPI);
	}
	
	/**
	 * Remplit le JTable avec toutes les voies analogiques
	 */
	private void remplirTableauVoiesAnalogiques() {
		for(int i = 0; i < tbAnaAPI.size(); i++) {
			mdlAnalogicInput.addVoiesAPI(new AnalogicInput(
					tbAnaAPI.get(i).getIdCapteur(),
					tbAnaAPI.get(i).getNom(),
					tbAnaAPI.get(i).getDescription(),
					tbAnaAPI.get(i).getIdEquipement(),
					tbAnaAPI.get(i).getIdPosteTechnique(),
					tbAnaAPI.get(i).getIdTypeMateriel(),
					tbAnaAPI.get(i).getIdZoneSubstitution(),
					tbAnaAPI.get(i).getTypeCapteur(),
					tbAnaAPI.get(i).getAlarme(),
					tbAnaAPI.get(i).getIdService(),
					tbAnaAPI.get(i).getVoieApi(),
					tbAnaAPI.get(i).getInhibition(),
					tbAnaAPI.get(i).getIdUnite(),
					tbAnaAPI.get(i).getContact(),
					tbAnaAPI.get(i).getIdEntreeAnalogique(),
					tbAnaAPI.get(i).getSeuilHaut(),
					tbAnaAPI.get(i).getPreSeuilHaut(),
					tbAnaAPI.get(i).getSeuilBas(),
					tbAnaAPI.get(i).getPreSeuilBas(),
					tbAnaAPI.get(i).getCalibration(),
					tbAnaAPI.get(i).getSeuilTempo(),
					tbAnaAPI.get(i).getPreSeuilTempo(),
					tbAnaAPI.get(i).getUnite(),
					tbAnaAPI.get(i).getValeurConsigne()					
					));
		}
	}

	/**
	 * Gestion des actions
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == tmrTpsReel) {
			try {
				tmrTpsReel.stop();
				raffraichissementValeur();
				tmrTpsReel.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER ...");
			} // Fin try		
		}
		if (ae.getSource() == btnValeurAPI) {
			FenGestionValeurAPI fenetre = new FenGestionValeurAPI();
			fenetre.setVisible(true);
		}
		
	}
}
