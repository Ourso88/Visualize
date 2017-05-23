import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import AE_Communication.AE_TCP_Constantes;
import AE_General.AE_BarreBas;
import AE_General.AE_BarreHaut;
import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;

public class FenChoixCapteurAnalogique extends JFrame implements AE_General.AE_Constantes, ActionListener {
	private static final long serialVersionUID = 1L;

	// Commun
	private JPanel pnlVoieAna = new JPanel();
	private JPanel pnlVoieDigi = new JPanel();
	private JPanel pnlMaintenance = new JPanel();
	private JSplitPane splVoie = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlVoieAna, pnlVoieDigi);
	private JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splVoie, pnlMaintenance);
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Timer
    private Timer tmrTpsReel = new Timer(AE_TCP_Constantes.TIMER_LECTURE_TPS_REEL, this);	
	
	// Analogique
    private JLabel lblTitreVoieAna = new JLabel("Voies Analogiques");
    private JLabel lblTitreVoieDigi = new JLabel("Voies Digitales");
	private JPanel pnlCommande = new JPanel();
	private JButton btnCourbe = new JButton("Courbe");
	private JButton btnHistoriqueAlarme = new JButton("Historique Alarme");
	private JButton btnParametre = new JButton("Paramétres voie");
    
    private ModeleCapteurAnalogique mdlCapteurAnalogique = new ModeleCapteurAnalogique();	
    private JTable tbCapteurAna;
    TableRowSorter<TableModel> sorterCapteurAna;	

    private ModeleCapteurDigitale mdlCapteurDigitale = new ModeleCapteurDigitale();	
    private JTable tbCapteurDigi;
    TableRowSorter<TableModel> sorterCapteurDigi;	
    
    
	// Maintenance
    private JLabel lblTitreMaintenance = new JLabel("Zone de maintenance");
	// Inhibition
    private ModeleInhibition mdlInhibition = new ModeleInhibition();	
    private JTable tbInhibition;
    private TableRowSorter<TableModel> sorterInhibition;		
	
	public FenChoixCapteurAnalogique() throws ParseException {
		super();
		build();
		ctn.open();
		remplirListeCapteur();
		remplirInhibition();
		lectureVoieAnalogique();
		lectureVoieTpsReel();
		tmrTpsReel.start();
	}

	public void build() {
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	ctn.close();
            }
        });
		
	    this.setTitle("Choix capteur analogique");
	    this.setSize(1000, 800);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    if(EFS_Client_Variable.niveauUtilisateur < 40) {
	    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Choix capteur analogique");
	    pnlVoieAna.setBackground(EFS_BLEU);
	    pnlMaintenance.setBackground(EFS_BLEU);
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		splCorps.setDividerLocation(600);
		splVoie.setDividerLocation(350);
		
	    // =================== Voies analogiques ==============================
	    pnlVoieAna.setLayout(new BorderLayout());

	    lblTitreVoieAna.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreVoieAna.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreVoieAna.setBackground(EFS_MARRON);
	    lblTitreVoieAna.setOpaque(true);
	    
	    btnCourbe.addActionListener(this);
		btnHistoriqueAlarme.addActionListener(this);
		btnParametre.addActionListener(this);
		
	    btnCourbe.setPreferredSize(btnHistoriqueAlarme.getPreferredSize());
	    btnCourbe.setMinimumSize(btnHistoriqueAlarme.getMinimumSize());
	    btnParametre.setPreferredSize(btnHistoriqueAlarme.getPreferredSize());
	    btnParametre.setMinimumSize(btnHistoriqueAlarme.getMinimumSize());
		
		pnlCommande.add(btnCourbe);
		pnlCommande.add(btnHistoriqueAlarme);
		pnlCommande.add(btnParametre);
		
        tbCapteurAna = new JTable(mdlCapteurAnalogique);
        tbCapteurAna.setSize(800, 400);
        tbCapteurAna.setFillsViewportHeight(true);        
        tbCapteurAna.setBackground(EFS_BLEU);
        sorterCapteurAna =  new TableRowSorter<TableModel>(tbCapteurAna.getModel());
        tbCapteurAna.setRowSorter(sorterCapteurAna);        
        sorterCapteurAna.setSortable(0,  false);
        sorterCapteurAna.setSortsOnUpdates(true);
        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_NOM).setPreferredWidth(40);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_NOM).setMaxWidth(40);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_ETAT).setPreferredWidth(100);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_ETAT).setMaxWidth(100);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_BAS).setPreferredWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_BAS).setMaxWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_HAUT).setPreferredWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_HAUT).setMaxWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_TEMPO).setPreferredWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_TEMPO).setMaxWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_CALIBRATION).setPreferredWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_CALIBRATION).setMaxWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_VALEUR).setPreferredWidth(80);        
        tbCapteurAna.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_ANA_VALEUR).setMaxWidth(80);        
        
        tbCapteurAna.setSelectionBackground(EFS_MARRON);
        tbCapteurAna.getSelectionModel();
		tbCapteurAna.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        pnlCommande.add(btnCourbe);
        
        pnlVoieAna.add(lblTitreVoieAna, BorderLayout.NORTH);
        pnlVoieAna.add(pnlCommande, BorderLayout.SOUTH);
        pnlVoieAna.add(new JScrollPane(tbCapteurAna), BorderLayout.CENTER);		
		
	    // =================== Voies digitales ==============================
	    pnlVoieDigi.setLayout(new BorderLayout());

	    lblTitreVoieDigi.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreVoieDigi.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreVoieDigi.setBackground(EFS_MARRON);
	    lblTitreVoieDigi.setOpaque(true);        

        tbCapteurDigi = new JTable(mdlCapteurDigitale);
        tbCapteurDigi.setSize(800, 400);
        tbCapteurDigi.setFillsViewportHeight(true);        
        tbCapteurDigi.setBackground(EFS_BLEU);
        sorterCapteurDigi =  new TableRowSorter<TableModel>(tbCapteurDigi.getModel());
        tbCapteurDigi.setRowSorter(sorterCapteurDigi);        
        sorterCapteurDigi.setSortable(0,  false);
        sorterCapteurDigi.setSortsOnUpdates(true);
        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_NOM).setPreferredWidth(40);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_NOM).setMaxWidth(40);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_ETAT).setPreferredWidth(100);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_ETAT).setMaxWidth(100);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_TEMPO).setPreferredWidth(80);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_TEMPO).setMaxWidth(80);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_VALEUR).setPreferredWidth(80);        
        tbCapteurDigi.getColumnModel().getColumn(JTABLE_CHOIX_CAPTEUR_DIGI_VALEUR).setMaxWidth(80);        
        
        tbCapteurDigi.setSelectionBackground(EFS_MARRON);
        tbCapteurDigi.getSelectionModel();
		tbCapteurDigi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    
        pnlVoieDigi.add(lblTitreVoieDigi, BorderLayout.NORTH);
//        pnlVoieDigi.add(pnlCommande, BorderLayout.SOUTH);
        pnlVoieDigi.add(new JScrollPane(tbCapteurDigi), BorderLayout.CENTER);		
	    
	    
	    
	    // =================== Maintenance ====================================
	    pnlMaintenance.setLayout(new BorderLayout());

	    lblTitreMaintenance.setHorizontalAlignment(JLabel.CENTER);
	    lblTitreMaintenance.setFont(FONT_ARIAL_16_BOLD);
	    lblTitreMaintenance.setBackground(EFS_MARRON);
	    lblTitreMaintenance.setOpaque(true);

        tbInhibition = new JTable(mdlInhibition);
        tbInhibition.setSize(800, 400);
	    tbInhibition.setFillsViewportHeight(true);        
	    tbInhibition.setBackground(AE_Constantes.EFS_BLEU);
        sorterInhibition =  new TableRowSorter<TableModel>(tbInhibition.getModel());
        tbInhibition.setRowSorter(sorterInhibition);     
        sorterInhibition.setSortsOnUpdates(false);
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_DATE_INHIBITION).setCellRenderer(new DateRenderer());
        tbInhibition.setSelectionBackground(AE_Constantes.EFS_MARRON);
        tbInhibition.getSelectionModel();
		tbInhibition.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_VOIE).setPreferredWidth(40);        
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_VOIE).setMaxWidth(40);        
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_DATE_INHIBITION).setPreferredWidth(150);        
        tbInhibition.getColumnModel().getColumn(JTABLE_INHIBITION_DATE_INHIBITION).setMaxWidth(150);        
	    
        pnlMaintenance.add(lblTitreMaintenance, BorderLayout.NORTH);
        pnlMaintenance.add(new JScrollPane(tbInhibition), BorderLayout.CENTER);
	}

	private void remplirListeCapteur() {
		String strSql = "";
		
		int idEntreeAnalogique = -1;
		int idCapteur = -1;
		int idEquipement = -1;
		int idService = -1;
		String nomCapteur = "";
		String nomEquipement = "";
		String descriptionCapteur = "";
		String posteTechnique = "";
		String etatAlarme = "";
		int voieApi = 0;
		String seuilBas = "";
		String seuilHaut = "";
		String tempo = "";
		String calibration = "";

		
		// Analogique
		mdlCapteurAnalogique.removeAll();
		strSql = "SELECT Capteur.*, EntreeAnalogique.*, Equipement.Nom AS nomEquipement "
				+ "FROM ((Capteur LEFT JOIN EntreeAnalogique ON Capteur.idCapteur = EntreeAnalogique.idCapteur)"
				+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
				+ " WHERE TypeCapteur = 1";
		
		ResultSet result = ctn.lectureData(strSql);

		// Remplissage de la liste
		try {
			while(result.next()) {
				idEntreeAnalogique = result.getInt("idEntreeAnalogique");
				idCapteur = result.getInt("idCapteur");
				idEquipement = result.getInt("idEquipement");
				idService = result.getInt("idService");
				nomCapteur = result.getString("Nom");
				nomEquipement = result.getString("nomEquipement");
				descriptionCapteur = result.getString("Description");
				posteTechnique = "Poste T...";
				
				if (result.getInt("Alarme") == ALARME_ALERT) {
					etatAlarme = "Alarme";
				} else if (result.getInt("Alarme") == ALARME_DEFAUT) {
					etatAlarme = "Défaut";
				} else {
					etatAlarme = "";
				}
				
				voieApi = result.getInt("voieApi");
				seuilBas = String.valueOf((result.getDouble("SeuilBas") / 10));
				seuilHaut = String.valueOf((result.getDouble("SeuilHaut") / 10));
				tempo = String.valueOf(result.getInt("SeuilTempo"));
				calibration = String.valueOf(result.getDouble("calibration") / 10);

				if(testService(idService)) {
					mdlCapteurAnalogique.addCapteurAnalogique(new TypeCapteurAnalogique(idEntreeAnalogique, idCapteur, idEquipement, nomCapteur, nomEquipement,
							descriptionCapteur, posteTechnique, etatAlarme, voieApi, seuilBas, seuilHaut, tempo, calibration));
				}
				
			} // fin while
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture 
		ctn.closeLectureData();
		
		
		// Digitale
		
		int idEntreeDigitale = -1;
		int noNf = -1;
		
		mdlCapteurDigitale.removeAll();
		strSql = "SELECT Capteur.*, EntreeDigitale.*, Equipement.Nom AS nomEquipement "
				+ "FROM ((Capteur LEFT JOIN EntreeDigitale ON Capteur.idCapteur = EntreeDigitale.idCapteur)"
				+ " LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement)"
				+ " WHERE TypeCapteur = 2";
		
		result = ctn.lectureData(strSql);

		// Remplissage de la liste
		try {
			while(result.next()) {
				idEntreeDigitale = result.getInt("idEntreeDigitale");
				idCapteur = result.getInt("idCapteur");
				idEquipement = result.getInt("idEquipement");
				idService = result.getInt("idService");
				nomCapteur = result.getString("Nom");
				nomEquipement = result.getString("nomEquipement");
				descriptionCapteur = result.getString("Description");
				posteTechnique = "Poste T...";
				noNf = result.getInt("NoNf");
				
				if (result.getInt("Alarme") == ALARME_ALERT) {
					etatAlarme = "Alarme";
				} else if (result.getInt("Alarme") == ALARME_DEFAUT) {
					etatAlarme = "Défaut";
				} else {
					etatAlarme = "";
				}
				
				voieApi = result.getInt("voieApi");
				tempo = String.valueOf(result.getInt("Tempo"));

				if(testService(idService)) {
					mdlCapteurDigitale.addCapteurDigitale(new TypeCapteurDigitale(idEntreeDigitale, idCapteur, idEquipement, nomCapteur, nomEquipement,
							descriptionCapteur, posteTechnique, etatAlarme, voieApi, tempo, noNf));
				}
				
			} // fin while
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		
	} // Fin RemplirListeCapteur
	
	private boolean testService(int idService) {
		// Filtre sur le service
		if(EFS_Client_Variable.niveauUtilisateur < 40) {
			for(int i = 0; i < 100; i++) {
				if((EFS_Client_Variable.tbService[i]  == idService) && idService > 0) {
					return true;
				}
			} // fin for
			return false;
		} else {
			return true;
		}
	}
	
	private void appelFenetreCourbe() {
        if (tbCapteurAna.getRowCount() > 0) {
        	if (tbCapteurAna.getSelectedRowCount() > 0) {
        		int[] selection = tbCapteurAna.getSelectedRows();
		        int indexSelection = selection[0];
				if(indexSelection >  0) {
			        int idCapteur = mdlCapteurAnalogique.getIdCapteur(indexSelection); 
			        
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
    } // fin appelCourbe

	private void appelFenetreHistoriqueAlarme() {
		// Appel fenetre Historique Alarme
		FenHistoriqueAlarme fenetre;
		try {
			fenetre = new FenHistoriqueAlarme();
			fenetre.setVisible(true);
			fenetre.requestFocusInWindow();		
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	} // fin appelFenetreHistoriqueAlarme

	private void appelFenetreCapteurAnalogique() {
        if (tbCapteurAna.getRowCount() > 0) {
        	if (tbCapteurAna.getSelectedRowCount() > 0) {
        		int[] selection = tbCapteurAna.getSelectedRows();
		        int indexSelection = selection[0];
				if(indexSelection >  0) {
			        int idCapteur = mdlCapteurAnalogique.getIdCapteur(indexSelection); 
			        
					FenEntreeAnalogique fenAna = new FenEntreeAnalogique();
					fenAna.selectionCapteur(idCapteur);
					fenAna.setVisible(true);
					fenAna.requestFocusInWindow();
				} else {
					FenEntreeAnalogique fenAna = new FenEntreeAnalogique();
					fenAna.setVisible(true);
					fenAna.requestFocusInWindow();
				}
        	}  else {
				FenEntreeAnalogique fenAna = new FenEntreeAnalogique();
				fenAna.setVisible(true);
				fenAna.requestFocusInWindow();
        	}
        }  else {
			FenEntreeAnalogique fenAna = new FenEntreeAnalogique();
			fenAna.setVisible(true);
			fenAna.requestFocusInWindow();
        }

	} // fin appelFenetreCapteurAnalogique
	
	private void lectureVoieTpsReel() {
		int voieApi = 0;
		double calibration = 0D;
		double valeur = 0D;
		String strValeur = "";
		
		// Analogique
		for(int i = 0; i < mdlCapteurAnalogique.getRowCount(); i++) {
			voieApi = mdlCapteurAnalogique.getVoieApi(i);
			calibration = Double.parseDouble((String)mdlCapteurAnalogique.getValueAt(i, JTABLE_CHOIX_CAPTEUR_ANA_CALIBRATION));
			valeur = (EFS_Client_Variable.tbValeurAI[voieApi - 1] / 10) + calibration;
			
			//strValeur = String.valueOf(valeur);
			strValeur = String.format("%3.1f", valeur);
			
			// Mettre à jour la valeur
			mdlCapteurAnalogique.setValueAt(strValeur, i, JTABLE_CHOIX_CAPTEUR_ANA_VALEUR);
		} // Fin for i

		for(int i = 0; i < mdlCapteurDigitale.getRowCount(); i++) {
			voieApi = mdlCapteurDigitale.getVoieApi(i);
			valeur = EFS_Client_Variable.tbValeurDI[voieApi - 1];
			
			if(valeur == 0) {
				strValeur = "Ouvert";
			} else {
				strValeur = "Fermé";
			}
			
			// Mettre à jour la valeur
			mdlCapteurDigitale.setValueAt(strValeur, i, JTABLE_CHOIX_CAPTEUR_DIGI_VALEUR);
		} // Fin for i
	
	
	} // fin lectureVoieTpsReel()

	private void lectureVoieAnalogique() {
		EFS_Client_Variable.lectureVoie();

/*		
		// ===== Envoi des requetes de lecture pour les AI =====
		int cptLecture = 0;
		int adresseLecture = 0;
		InetAddress addr = null; // Adresse IP du serveur	
		AE_TCP_Connection con = null; //the connection
		double [] reqReponse;
		
		try {
			addr = InetAddress.getByName(EFS_Client_Variable.ADR_IP_API);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		con = new AE_TCP_Connection(addr, AE_TCP_Constantes.MODBUS_PORT);
		try {
			con.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (con.isConnected()) {
			if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Connecté ... ");
		}
		else {
			if (AE_TCP_Constantes.MODE_DEBUG_API) System.out.println("API Déconnecté ... ");
		}
		
		do {
			adresseLecture = AE_TCP_Constantes.ADR_API_AI_TPS_REEL + (cptLecture * AE_TCP_Constantes.NB_MOT_LECTURE_AI);
			if (adresseLecture < (AE_TCP_Constantes.ADR_API_AI_TPS_REEL + AE_TCP_Constantes.MAX_AI)) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, adresseLecture, AE_TCP_Constantes.NB_MOT_LECTURE_AI));
				for (int i = 0; i < AE_TCP_Constantes.NB_MOT_LECTURE_AI; i++) {
					EFS_Client_Variable.tbValeurAI[(adresseLecture - AE_TCP_Constantes.ADR_API_AI_TPS_REEL) + i] = reqReponse[i]; 
				} // Fin for i
			} // Fin if
			cptLecture++;
		} while(adresseLecture < (AE_TCP_Constantes.ADR_API_AI_TPS_REEL + AE_TCP_Constantes.MAX_AI)); // Fin while
		con.close();
*/		
	} // fin lectureVoieAnalogique	
	
	private void remplirInhibition() {
		String strSql = "";
		int idInhibition = 0;
		int idCapteur = 0;
		String nomCapteur;
		String descriptionCapteur;
		Date dateInhibition = new Date();
		String nomUtilisateur = "";
		int idUtilisateur = 0;
		int idRaisonInhibition = 0;
		String raisonInhibition = "";
		int idService = -1;
		
		mdlInhibition.removeAllInhibition();
		strSql = "SELECT Inhibition.idInhibition, Inhibition.DateInhibition, Capteur.idCapteur, Capteur.idService"
				+ ", Capteur.Nom AS nomVoie, Capteur.Description, Utilisateur.Nom AS nomUtilisateur"
				+ ", Utilisateur.Prenom, Utilisateur.idUtilisateur, RaisonInhibition.idRaisonInhibition, RaisonInhibition.Raison"
				+ " FROM ((Inhibition LEFT JOIN Capteur ON Inhibition.idCapteur = Capteur.idCapteur)"
				+ " LEFT JOIN Utilisateur ON Inhibition.idUtilisateur = Utilisateur.idUtilisateur)"
				+ " LEFT JOIN RaisonInhibition ON Inhibition.idRaisonInhibition = RaisonInhibition.idRaisonInhibition";
		
		ResultSet result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				idInhibition = result.getInt("idInhibition");
				idCapteur = result.getInt("idCapteur");
				nomCapteur = result.getString("nomVoie");
				descriptionCapteur = result.getString("Description");
				dateInhibition = result.getDate("DateInhibition");
				nomUtilisateur = result.getString("nomUtilisateur") + " " + result.getString("Prenom");
				idUtilisateur = result.getInt("idUtilisateur");
				idRaisonInhibition = result.getInt("idRaisonInhibition");
				raisonInhibition = result.getString("Raison");
				idService = result.getInt("idService");

				if(testService(idService)) {
					mdlInhibition.addInhibition(new Inhibition(idInhibition, idCapteur, nomCapteur, descriptionCapteur, dateInhibition, idUtilisateur, nomUtilisateur, idRaisonInhibition, raisonInhibition));
				}
						
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme lecture inhibitions");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} // fin remplirInhibition()
		
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnCourbe) {
			appelFenetreCourbe();
		}
		
		if(ae.getSource() == btnHistoriqueAlarme) {
			appelFenetreHistoriqueAlarme();
		}

		if(ae.getSource() == btnParametre) {
			appelFenetreCapteurAnalogique();
		}
		
		if(ae.getSource() == tmrTpsReel) {
			EFS_Client_Variable.lectureVoie();
			// lectureVoieAnalogique();
			lectureVoieTpsReel();
		}
	}	
	
} // Fin class
