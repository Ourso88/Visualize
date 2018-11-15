import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;

import AE_Communication.AE_TCP_Constantes;
import AE_General.AE_BarreBas;
import AE_General.AE_BarreHaut;
import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.AE_Fonctions;
import AE_General.EFS_Client_Variable;
import AE_General.EFS_Constantes;
import EFS_Structure.StructPriseEnCompte;

/**
 * Gere les alarmes pour acquittement par poste Client
 * @author Eric Mariani
 * @since 05/06/2018
 *
 */
public class GestionAlarmeTpsReel extends JFrame implements ActionListener, AE_General.AE_Constantes {
	private static final long serialVersionUID = 1L;
	
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();	
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	
	private JPanel pnlAlarme = new JPanel();
	private JPanel pnlAlarmeCommande = new JPanel();
	
	// Alarmes
    private ModeleTpsReelAlarme mdlTpsReelAlarme = new ModeleTpsReelAlarme();	
    private JTable tbAlarme;
    private TableRowSorter<TableModel> sorter;		
    private JButton btnPriseEnCompte = new JButton("Prise en compte");
    private JButton btnInformationAlarme = new JButton("Information");
    private JButton btnCourbe = new JButton("Courbe");
	
	private List<StructPriseEnCompte> tbPriseEnCompte = new ArrayList<StructPriseEnCompte>();	
    
	// Timer
    private Timer tmrTpsReel = new Timer(AE_TCP_Constantes.TIMER_LECTURE_TPS_REEL, this);	
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	
	/**
	 * Constructeur
	 */
	public GestionAlarmeTpsReel() {
		super();
		ctn.open();
		build();
		remplirPriseEnCompte();
		EFS_Client_Variable.lectureVoie();
		gestionAlarmeTpsReel();
		tmrTpsReel.start();
	}
	
	/**
	 * Construit l'écran
	 */
	private void build() {
	    this.setTitle("GTC Visualize - Gestion Alarme Temps Réel");
	    this.setSize(1200, 800);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
		pnlHaut.setTitreEcran("GTC Visualize - Gestion Alarme Temps Réel");
	    
	    
	    this.add("North", pnlHaut);
	    this.add("Center", pnlAlarme);
	    this.add("South", pnlInfo);	
	    
	    // ============== ALARMES ====================
	    pnlAlarme.setLayout(new BorderLayout());
	    
	    JLabel lblTitreAlarme = new JLabel("ALARME EN COURS");
	    lblTitreAlarme.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreAlarme.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreAlarme.setBackground(EFS_MARRON);
	    lblTitreAlarme.setOpaque(true);
	    pnlAlarme.add(lblTitreAlarme, BorderLayout.NORTH);
	    
	    tbAlarme = new JTable(mdlTpsReelAlarme);
	    tbAlarme.setSize(1000, 600);
	    tbAlarme.setFillsViewportHeight(true);        
	    tbAlarme.setBackground(EFS_BLEU);
        sorter =  new TableRowSorter<TableModel>(tbAlarme.getModel());
        tbAlarme.setRowSorter(sorter);        
        sorter.setSortable(0,  false);
        sorter.setSortsOnUpdates(true);
        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VOIE).setCellRenderer(new ColorCellRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPEL_ALERT).setCellRenderer(new ColorAppelAlertCellRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPARITION).setCellRenderer(new DateRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_DISPARITION).setCellRenderer(new DateRenderer());
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_PRISE_EN_COMPTE).setCellRenderer(new DateRenderer());

        tbAlarme.setSelectionBackground(EFS_MARRON);
        tbAlarme.getSelectionModel();
		tbAlarme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VOIE).setPreferredWidth(40);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VOIE).setMaxWidth(40);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_INVENTAIRE).setPreferredWidth(100);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_INVENTAIRE).setMaxWidth(100);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPARITION).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPARITION).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_DISPARITION).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_DISPARITION).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_PRISE_EN_COMPTE).setPreferredWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_PRISE_EN_COMPTE).setMaxWidth(120);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_TYPE_ALARME).setPreferredWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_TYPE_ALARME).setMaxWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VALEUR).setPreferredWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_VALEUR).setMaxWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPEL_ALERT).setPreferredWidth(30);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_APPEL_ALERT).setMaxWidth(30);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_RAPPEL_ALERT).setPreferredWidth(50);        
        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_RAPPEL_ALERT).setMaxWidth(50);        
        
        MaskFormatter mask = null ;
        try {
            mask = new MaskFormatter("###");
        } catch (ParseException e) {
            System.err.println("Le masque est incorrect") ;
        }
        // Les caractères valides sont les chiffres et les virgules
        mask.setValidCharacters("0123456789");
        mask.setPlaceholderCharacter('0');
        JFormattedTextField field = new JFormattedTextField( mask ) ;
//        tbAlarme.getColumnModel().getColumn(JTABLE_ALARME_RAPPEL_ALERT).setCellEditor( new DefaultCellEditor( field ) );
        
        sorter =  new TableRowSorter<TableModel>(tbAlarme.getModel());
        tbAlarme.setRowSorter(sorter);     
        sorter.setSortable(JTABLE_ALARME_VOIE,  false);
        sorter.setSortable(JTABLE_ALARME_DESCRIPTION_VOIE,  false);
        sorter.setSortable(JTABLE_ALARME_APPARITION,  false);
        sorter.setSortable(JTABLE_ALARME_DISPARITION,  false);
        sorter.setSortable(JTABLE_ALARME_PRISE_EN_COMPTE,  false);
        sorter.setSortable(JTABLE_ALARME_DESCRIPTION_ALARME,  false);
        sorter.setSortable(JTABLE_ALARME_TYPE_ALARME,  false);
        sorter.setSortable(JTABLE_ALARME_VALEUR,  false);
        sorter.setSortsOnUpdates(false);
        
	    JScrollPane jspTbAlarme = new JScrollPane(tbAlarme);
        jspTbAlarme.setBackground(Color.GREEN);
        jspTbAlarme.setOpaque(true);
        jspTbAlarme.setPreferredSize(new Dimension(400, 200));
        jspTbAlarme.setMinimumSize(new Dimension(400, 200));
	    pnlAlarme.add(jspTbAlarme, BorderLayout.CENTER);
	    
	    btnPriseEnCompte.addActionListener(this);
	    btnInformationAlarme.addActionListener(this);
	    btnCourbe.addActionListener(this);
	    
	    btnInformationAlarme.setPreferredSize(btnPriseEnCompte.getPreferredSize());
	    btnInformationAlarme.setMinimumSize(btnPriseEnCompte.getMinimumSize());
	    btnCourbe.setPreferredSize(btnPriseEnCompte.getPreferredSize());
	    btnCourbe.setMinimumSize(btnPriseEnCompte.getMinimumSize());

	    pnlAlarmeCommande.add(btnPriseEnCompte);
	    pnlAlarmeCommande.add(btnInformationAlarme);
	    pnlAlarmeCommande.add(btnCourbe);
	    pnlAlarme.add(pnlAlarmeCommande, BorderLayout.SOUTH);
	}
	
	/**
	 * Remplit les alarmes en cours
	 */
	private void gestionAlarmeTpsReel() {
		int cptCapteur = 0;
		boolean blAlert = false;
		boolean blTrouve = false;
		String strSql = "";
		ResultSet result = null;
		int voieApi = -1;

		// Lecture de la table AlarmeEnCours
		strSql = "SELECT V2_AlarmeEnCours.*, Capteur.VoieApi AS CapteurVoieApi, Capteur.Alarme, Capteur.Description, Capteur.TypeCapteur, Capteur.Nom, Capteur.Inhibition, Capteur.idAlarmeService,"
			   + " Equipement.NumeroInventaire "
			   + " FROM (V2_AlarmeEnCours LEFT JOIN (Capteur LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
			   + " ON V2_AlarmeEnCours.idCapteur = Capteur.idCapteur)"
			   + " WHERE Capteur.idAlarmeService = " + EFS_Constantes.gestionUtilisateur.getIdAlarmeService();
		result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				cptCapteur++;
				// voie traitée
				voieApi = result.getInt("CapteurVoieApi") - 1;
				// Regarder si déjà dans la liste
				blTrouve = false;
				for(int i = 0; i < mdlTpsReelAlarme.getRowCount(); i++) {
					if(mdlTpsReelAlarme.getIdCapteur(i) == result.getInt("idCapteur")) {
						// Mettre à jour la valeur
						if(result.getInt("TypeCapteur") == CAPTEUR_ANALOGIQUE_ENTREE) {
							mdlTpsReelAlarme.setValueAt((EFS_Client_Variable.tbValeurAI[voieApi] + EFS_Client_Variable.tbCalibrationAI[voieApi]) / 10, i, JTABLE_ALARME_VALEUR);
						} else {
							mdlTpsReelAlarme.setValueAt((double)EFS_Client_Variable.tbValeurDI[voieApi], i, JTABLE_ALARME_VALEUR);
						}
						mdlTpsReelAlarme.setValueAt(result.getDate("DateDisparition"), i, JTABLE_ALARME_DISPARITION);
						mdlTpsReelAlarme.setValueAt(result.getDate("DateApparition"), i, JTABLE_ALARME_APPARITION);
						mdlTpsReelAlarme.setValueAt(result.getDate("DatePriseEnCompte"), i, JTABLE_ALARME_PRISE_EN_COMPTE);
						blTrouve = true;
					} // Fin if
				} // Fin for i
				if(!blTrouve) {
					if(result.getInt("Alarme") == ALARME_ALERT) {
						blAlert = true;
					}
					else {
						blAlert = false;
					}
//					if ((result.getInt("Inhibition") != ALARME_INHIBITION) && (result.getInt("blTempo") == ALARME_TEMPO_DEPASSEE)) {
						mdlTpsReelAlarme.addAlarme(new TpsReelAlarme(result.getInt("idCapteur"), result.getString("Nom")
								, result.getString("Description"), result.getString("NumeroInventaire"), result.getDate("DateApparition"),
								result.getDate("DatePriseEnCompte"), result.getString("DescriptionAlarme")
								, result.getInt("VoieApi"), blAlert, result.getDate("DateDisparition"), result.getInt("Alarme")));
//					} // fin mise en tableau
				} // fin blTrouve
			} // fin while
		} 
		catch (SQLException e) {
				e.printStackTrace();
		}
		ctn.closeLectureData();
		try {
			result.close();
			result = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (mdlTpsReelAlarme.getRowCount() != cptCapteur) {
			mdlTpsReelAlarme.removeAllAlarme();
			gestionAlarmeTpsReel();
		}
	}
	
	/**
	 * Prise en compte des alarmes
	 */
	private void gestionPriseEnCompte() {
		String raison = new String("");
		Date now = new Date();
		String strSql = "";
	
        if (tbAlarme.getRowCount() > 0) {
        	if (tbAlarme.getSelectedRowCount() > 0) {
		        int[] selection = tbAlarme.getSelectedRows();
		        int indexSelection = selection[0];
		        int idCapteur = mdlTpsReelAlarme.getIdCapteur(indexSelection); 
		        String strTypeAlarme = "";
		        
				// Motif Prise En Compte seulement si appel Alert
		        strTypeAlarme = (String) mdlTpsReelAlarme.getValueAt(indexSelection, JTABLE_ALARME_TYPE_ALARME);
		        if (strTypeAlarme.equals("Alarme")) {
					int idPriseEnCompte = 0;
		        	do {
						Object [] possibilites = new Object[tbPriseEnCompte.size()];
						for(int i = 0; i < tbPriseEnCompte.size(); i++) {
							possibilites[i] = tbPriseEnCompte.get(i).getNom();
						}
						String strReponse = (String)JOptionPane.showInputDialog(this, "Choisir un motif de prise en compte : ", "Prise en compte", JOptionPane.QUESTION_MESSAGE, null, possibilites, null);
						idPriseEnCompte = 1;
						if (strReponse != null) {
							for(int i = 0; i < tbPriseEnCompte.size(); i++) {
								if(strReponse.equals(tbPriseEnCompte.get(i).getNom())) {
									idPriseEnCompte = (int) tbPriseEnCompte.get(i).getId();
								}
							} // fin for
						} else {
							raison = "En attente";
						}
						
						// idPriseEncompte == 1 (Autres)
						if(idPriseEnCompte == 1) {
							// Demande de commentaire
							raison = AE_Fonctions.saisieTexte("Veuillez entrer une raison : ", "Raison commentaire ...");
							if (raison == null) raison = "";
						} else {
							raison = "---";
						}
		        	} while(raison == "");
		        	
					strSql = "UPDATE V2_AlarmeEnCours SET idPriseEnCompte = " + idPriseEnCompte
							   + " , idUtilisateur = " + EFS_Client_Variable.idUtilisateur
							   + " , CommentairePriseEnCompte = '" + raison + "'"
							   + " WHERE idCapteur = " + idCapteur;
					ctn.fonctionSql(strSql);
					mdlTpsReelAlarme.setMotifIdPriseEncompte(indexSelection, idPriseEnCompte);
		        } // fin if appelAlert

		        if(mdlTpsReelAlarme.getValueAt(indexSelection, JTABLE_ALARME_PRISE_EN_COMPTE) == null) {
					// Date prise en compte
					strSql = "UPDATE V2_AlarmeEnCours SET DatePriseEnCompte = sysdate, blPriseEnCompte = 1 WHERE idCapteur = " + idCapteur;
					ctn.fonctionSql(strSql);
					mdlTpsReelAlarme.setValueAt(now, indexSelection, JTABLE_ALARME_PRISE_EN_COMPTE);
		        } // fin if datePrsieEnCompte
	
		        // Prevenir le programme maitre
		        AE_Fonctions.modifierMaitreViaClient(idCapteur, VIA_API_PRISE_EN_COMPTE);
		        
				// Arret klaxon et Alert
//				gestionKlaxon(false);
//				gestionAlert(false);
				
        	} else {
     	       JOptionPane.showMessageDialog(this, "Vous devez sélectionner une ligne pour prendre en compte ...",
   	    		    "GTC Visualize - Programme Maitre", JOptionPane.WARNING_MESSAGE);
        	}
        } else {
//			gestionKlaxon(false);
//			gestionAlert(false);
        }
	} // Fin gestionPriseEnCompte
	
	/**
	 * Remplit la liste des prise en compte
	 */
	private void remplirPriseEnCompte() {
		ResultSet result = ctn.lectureData("SELECT * FROM PriseEnCompte");
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				tbPriseEnCompte.add(new StructPriseEnCompte(result.getLong("idPriseEnCompte"), result.getString("Nom"), result.getString("Description")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // Fin LectureDonnees	
	
	/**
	 * Appel l'écran courbe
	 */
	private void gererCourbe() {
        if (tbAlarme.getRowCount() > 0) {
        	if (tbAlarme.getSelectedRowCount() > 0) {
        		int[] selection = tbAlarme.getSelectedRows();
		        int indexSelection = selection[0];
				if(indexSelection >  0) {
			        int idCapteur = mdlTpsReelAlarme.getIdCapteur(indexSelection); 
			        
					FenCourbe fenCourbe = new FenCourbe();
					fenCourbe.setIdCapteur(idCapteur);
					fenCourbe.traceCourbe();
					fenCourbe.setVisible(true);
					fenCourbe.requestFocusInWindow();
				} else {
					FenCourbe fenCourbe = new FenCourbe();
					fenCourbe.setVisible(true);
					fenCourbe.requestFocusInWindow();
				}
        	}  else {
				FenCourbe fenCourbe = new FenCourbe();
				fenCourbe.setVisible(true);
				fenCourbe.requestFocusInWindow();
        	}
        }  else {
			FenCourbe fenCourbe = new FenCourbe();
			fenCourbe.setVisible(true);
			fenCourbe.requestFocusInWindow();
        }
		
	}
	
	/**
	 * Gestion ndes actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tmrTpsReel) {
			EFS_Client_Variable.lectureVoie();
			gestionAlarmeTpsReel();
		}
		
		if (e.getSource() == btnPriseEnCompte) {
			gestionPriseEnCompte();
		} // Fin If

		if (e.getSource() == btnCourbe) {
			gererCourbe();
		} // Fin If
		
	}
}
