package gui.vues;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

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
import em.general.JTableConstantes;
import gui.modeles.ModeleJTableAlarmesEnCours;
import kernel.VoiesAPI;

/**
 * Gestion de la vue des alarmes en cours 
 * @author Eric Mariani
 * @since 18/02/2017
 *
 */
public class FenAlarmesEnCours extends JFrame implements AE_Constantes, VoiesAPI, EFS_General, JTableConstantes,  ActionListener {
	private static final long serialVersionUID = 1L;
	// GUI
	private AE_BarreHaut pnlEntete = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private JPanel pnlCorps = new JPanel();
	
	// ==> pnlCorps <== 
	private JPanel pnlBoutons = new JPanel();
    private ModeleJTableAlarmesEnCours mdlAlarmesEnCours = new ModeleJTableAlarmesEnCours();	
    private JTable jtbAlarmesEnCours;
    TableRowSorter<TableModel> sorterJtbAlarmesEnCours;	
    JScrollPane jspAlarmeEnCours;
    
	// ==> pnlBoutons <==
    private JButton btnVoiesAnalogique = new JButton("Voies analogiques");
    private JButton btnVoiesDigitale = new JButton("Voies digitales");
    private JButton btnPriseEnCompte = new JButton("Prise en compte");
    
    // Timer
    private Timer tmrRefresh = new Timer(TIMER_FEN_ALARMES_EN_COURS, this);	

    
    /**
     * Constucteur
     */
    public FenAlarmesEnCours() {
    	super();
    	build();
    	tmrRefresh.start();
    }
    
	/**
	 * Description de la fenêtre
	*/
	private void build() {
		this.setTitle("Visualize MAITRE - Gestion alarmes");
	    this.setSize(800, 600);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    java.net.URL icone = getClass().getResource("/eye.png");
	    this.setIconImage(new ImageIcon(icone).getImage());
	    
	    pnlEntete.setTitreEcran("Gestion Alarmes");
	    
	    // Actions
	    btnVoiesAnalogique.addActionListener(this);
	    btnVoiesDigitale.addActionListener(this);
	    btnPriseEnCompte.addActionListener(this);
	    
	    // Definition des parties de la fenetre
	    this.add("North",pnlEntete);
	    this.add("Center", pnlCorps);
	    this.add("South", pnlInfo);	
	    
	    // Tableau jtbAlarmesEnCours
	    jtbAlarmesEnCours = new JTable(mdlAlarmesEnCours);
	    jtbAlarmesEnCours.setFillsViewportHeight(true);        
	    jtbAlarmesEnCours.setBackground(AE_BLEU);
	    
	    sorterJtbAlarmesEnCours =  new TableRowSorter<TableModel>(jtbAlarmesEnCours.getModel());
        jtbAlarmesEnCours.setRowSorter(sorterJtbAlarmesEnCours);        
        sorterJtbAlarmesEnCours.setSortable(0,  false);
        sorterJtbAlarmesEnCours.setSortsOnUpdates(true);
        
        jspAlarmeEnCours = new JScrollPane(jtbAlarmesEnCours);
        jspAlarmeEnCours.setBackground(AE_BLEU);
        jspAlarmeEnCours.setOpaque(true);

        pnlCorps.setLayout(new BorderLayout());
        pnlCorps.add("North", pnlBoutons);
        pnlCorps.add("Center", jspAlarmeEnCours);
        
        pnlBoutons.setLayout(new FlowLayout());
        pnlBoutons.add(btnVoiesAnalogique);
        pnlBoutons.add(btnVoiesDigitale);
        pnlBoutons.add(btnPriseEnCompte);
	}

	/**
	 * Gestion des alarmes en cours
	 */
	private void gererAlarmes() {
		boolean trouve = false;

		// Verifier si pas de disparu
		if(tbAlarme.size() < mdlAlarmesEnCours.getRowCount()) {
			try {
				for(int i = 0; i < mdlAlarmesEnCours.getRowCount(); i++) {
					trouve = false;
					for(int j = 0; j < tbAlarme.size(); j++) {
						if(tbAlarme.get(j).getIdCapteur() == mdlAlarmesEnCours.getIdCapteur(i)) {
							trouve = true;
						}
					}
					if(!trouve) {
						// A supprimer
						try {
							mdlAlarmesEnCours.removeAlarmeEnCours(i);
						} catch (Exception ev) {
							GestionLogger.gestionLogger.warning("Probleme suppression ..." + ev.getMessage());
						}
					}
				}
			} catch (Exception ev) {
				GestionLogger.gestionLogger.warning("Probleme verification" + ev.getMessage());
			}
		}
		
		// Parcourir tous les alarmes
		for(int i = 0; i < tbAlarme.size(); i++) {
			// Regarder si déjà dans la table
			trouve = false;
			for(int j = 0; j < mdlAlarmesEnCours.getRowCount(); j++) {
				if(tbAlarme.get(i).getIdCapteur() == mdlAlarmesEnCours.getIdCapteur(j)) {
					trouve = true;
				}
			}
			if(!trouve) {
				mdlAlarmesEnCours.addAlarmeEnCours(tbAlarme.get(i));
			}
		} // fin for(i)
	
		for(int i = 0; i < tbAlarme.size(); i++) {
			try {
				mdlAlarmesEnCours.setValueAt(tbAlarme.get(i).getValeurAPI(), i, JT_ALARME_EN_COURS_VALEUR);
				mdlAlarmesEnCours.setValueAt(tbAlarme.get(i).getDateDisparition(), i, JT_ALARME_EN_COURS_DATE_DISPARITION);
				mdlAlarmesEnCours.setValueAt(tbAlarme.get(i).getDatePriseEnCompte(), i, JT_ALARME_EN_COURS_DATE_PRISE_EN_COMPTE);
			} catch (Exception ev) {
				GestionLogger.gestionLogger.warning("Probleme dans la gererAlarme Affichage ..." + ev.getMessage());
			}
		}
		
	} // fin class

	/**
	 * Gere la prise en compte d'une alarme
	 */
	private void gererPriseEnCompte() {
        if (jtbAlarmesEnCours.getRowCount() > 0) {
        	if (jtbAlarmesEnCours.getSelectedRowCount() > 0) {
		        int[] selection = jtbAlarmesEnCours.getSelectedRows();
		        int indexSelection = selection[0];
		        
		        System.out.println("Nom : " + tbAlarme.get(indexSelection).getNomCapteur());
		        tbAlarme.get(indexSelection).setDatePriseEnCompte(LocalDateTime.now());
		        tbAlarme.get(indexSelection).setPrisEnCompte(true);
        	}
        }
	}
	
	/**
	 * Gestion des actions
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnVoiesAnalogique) {
			FenVoiesAnalogicAPI fenetre = new FenVoiesAnalogicAPI();
			fenetre.setVisible(true);
		}
		if (ae.getSource() == btnVoiesDigitale) {
			FenVoiesDigitalAPI fenetre = new FenVoiesDigitalAPI();
			fenetre.setVisible(true);
		}
		if (ae.getSource() == tmrRefresh) {
			try {
				tmrRefresh.stop();
				gererAlarmes();
				tmrRefresh.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER ...");
			} // Fin try		
		
		}
		if (ae.getSource() == btnPriseEnCompte) {
			gererPriseEnCompte();
		}
	}	
}
