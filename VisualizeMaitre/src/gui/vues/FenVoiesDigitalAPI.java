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
import gui.modeles.ModeleJTableVoiesDigitalAPI;
import kernel.DigitalInput;
import kernel.VoiesAPI;

/**
 * Suivi des valeurs API Digitales
 * @author Eric Mariani
 * @since 19/02/2017
 *
 */
public class FenVoiesDigitalAPI extends JFrame implements AE_Constantes, VoiesAPI, ActionListener, JTableConstantes  {
	private static final long serialVersionUID = 1L;
	// GUI
	private AE_BarreHaut pnlEntete = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private JPanel pnlCorps = new JPanel();
	
	// ==> pnlCorps <== 
	private JPanel pnlBoutons = new JPanel();
    private ModeleJTableVoiesDigitalAPI mdlDigitalInput = new ModeleJTableVoiesDigitalAPI();	
    private JTable jtbVoiesAPI;
    TableRowSorter<TableModel> sorterJtbVoiesAPI;	
	// ==> pnlBoutons <==
    private JButton btnValeurAPI = new JButton("Modifier valeur API");
	
    // Timer
    private Timer tmrTpsReel = new Timer(EFS_General.TIMER_FEN_VOIES_DIGITAL, this);	

    
	/**
	 * Constructeur vide
	 */
	public FenVoiesDigitalAPI() {
		super();
		build();
		remplirTableauVoiesDigitales();
		this.setVisible(true);
		tmrTpsReel.start();
	}
	
	/**
	 * Description de la fenêtre
	*/
	private void build() {
		this.setTitle("Visualize MAITRE - Voies digitales API");
	    this.setSize(800, 600);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    pnlEntete.setTitreEcran("Voies digitales API");
	    
	    // Actions
	    btnValeurAPI.addActionListener(this);
	    
	    // Definition des parties de la fenetre
	    this.add("North",pnlEntete);
	    this.add("Center", pnlCorps);
	    this.add("South", pnlInfo);		
	    
	    // Tableau jtbVoiesAPI
	    jtbVoiesAPI = new JTable(mdlDigitalInput);
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
		for(int i = 0; i < tbDigiAPI.size(); i++) {
			mdlDigitalInput.setValueAt(tbDigiAPI.get(i).getValeurAPI(), i, JT_VOIES_API_DIGI_VALEUR);
			mdlDigitalInput.setValueAt(tbDigiAPI.get(i).isAlarmeEnclenchee(), i, JT_VOIES_API_DIGI_ALARME_ENCLENCHEE);
			mdlDigitalInput.setValueAt(tbDigiAPI.get(i).getDateAlarmeApparition(), i, JT_VOIES_API_DIGI_DATE_APPARITION_ALARME);
			mdlDigitalInput.setValueAt(tbDigiAPI.get(i).isAlarmeTempoEcoulee(), i, JT_VOIES_API_DIGI_TEMPO_ALARME_ECOULEE);
		}
		
		pnlInfo.setLblInformation(2, "Cpt : " + EFS_Maitre_Variable.nombreLectureAPI);
	}	
	
	/**
	 * Remplit le JTable avec toutes les voies analogiques
	 */
	private void remplirTableauVoiesDigitales() {
		for(int i = 0; i < tbDigiAPI.size(); i++) {
			mdlDigitalInput.addVoiesAPI(new DigitalInput(
					tbDigiAPI.get(i).getIdCapteur(),
					tbDigiAPI.get(i).getNom(),
					tbDigiAPI.get(i).getDescription(),
					tbDigiAPI.get(i).getIdEquipement(),
					tbDigiAPI.get(i).getIdPosteTechnique(),
					tbDigiAPI.get(i).getIdTypeMateriel(),
					tbDigiAPI.get(i).getIdZoneSubstitution(),
					tbDigiAPI.get(i).getTypeCapteur(),
					tbDigiAPI.get(i).getAlarme(),
					tbDigiAPI.get(i).getIdService(),
					tbDigiAPI.get(i).getVoieApi(),
					tbDigiAPI.get(i).getInhibition(),
					tbDigiAPI.get(i).getIdUnite(),
					tbDigiAPI.get(i).getContact(),
					tbDigiAPI.get(i).getIdEntreeDigitale(),
					tbDigiAPI.get(i).getTempo(),
					tbDigiAPI.get(i).getnOnF()
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
