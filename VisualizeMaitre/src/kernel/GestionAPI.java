package kernel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import em.communication.AE_TCP_Connection;
import em.communication.AE_TCP_Modbus;
import em.fonctions.GestionLogger;
import em.general.AE_Constantes;
import em.general.AE_Variables;
import em.general.EFS_General;
import em.general.EFS_Maitre_Variable;

public class GestionAPI extends JFrame implements ActionListener, EFS_General, AE_TCP_Modbus, AE_Constantes  {
	// Tableaux des voies
	private int tbIdAI[] = new int [MAX_AI];
	private double tbAI[] = new double [MAX_AI];
	private double tbAncAI[] = new double [MAX_AI];
	private int tbIdDI[] = new int [MAX_DI];
	private int tbDI[] = new int [MAX_DI];
	private int tbAncDI[] = new int [MAX_DI];
	private double tbEchangeMaitreClient[] = new double [MAX_ECHANGE_MAITRE_CLIENT];
	
	private AnalogicInput tbAnaAPI[] = new AnalogicInput[MAX_AI];
	private int cptCapteurAna = 0;
    private Timer tmrTpsReel = new Timer(EFS_General.TIMER_LECTURE_TPS_REEL, this);	
	
	private JLabel lblInformation = new JLabel(" --- ");
    private long cptLecture = 0;
	
	public GestionAPI () {
		super();
		build();
//		miseAjourCapteur();
		lectureAnalogicInput();
		tmrTpsReel.start();
	}
    
	private void build() {
	    this.setTitle("EFS GTC - Programme maitre");
	    this.setSize(1000, 800);
		this.setResizable(true);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	    
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", lblInformation);

	    
	}
	
	private void miseAjourCapteur() {
		ResultSet rsVueCapteur = AE_Variables.ctnOracle.lectureData("SELECT * FROM Vue_Capteur");
		try {
			while(rsVueCapteur.next()) {
				AE_Variables.ctnOracle.fonctionSql("UPDATE Capteur SET idCapteur = " + rsVueCapteur.getLong("idCapteur") + " WHERE Nom = '" + rsVueCapteur.getString("Nom") + "'");
			}
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("Erreur de mise à jour " + e.getMessage());
		}
		
	}
	
	private void lectureAnalogicInput() {
		// Lecture données
		try {
			ResultSet result = AE_Variables.ctnOracle.lectureData("SELECT * FROM (EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur)"
					+ " WHERE TypeCapteur = 1");
			while(result.next()) {
				tbAnaAPI[cptCapteurAna++] = new AnalogicInput(
				result.getLong("idCapteur"),
				result.getString("nom"),
				result.getString("description"),
				result.getLong("idEquipement"),
				result.getLong("idPosteTechnique"),
				result.getLong("idTypeMateriel"),
				result.getLong("idZoneSubstitution"),
				result.getInt("typeCapteur"),
				result.getInt("alarme"),
				result.getLong("idService"),
				result.getInt("voieApi"),
				result.getInt("inhibition"),
				result.getLong("idUnite"),
				result.getString("contact"),
				result.getLong("idEntreeAnalogique"),
				result.getInt("seuilHaut"),
				result.getInt("preSeuilHaut"),
				result.getInt("seuilBas"),
				result.getInt("preSeuilBas"),
				result.getInt("calibration"),
				result.getLong("seuilTempo"),
				result.getLong("preSeuilTempo"),
				result.getString("unite"),
				result.getInt("valeurConsigne")
				);
			}
			result.close();
			for(int i = 0; i < cptCapteurAna; i++){
				System.out.println("Capteur i = " + i + " --> " + tbAnaAPI[i].getNom());
			}
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("Erreur lecture Table Capteur : " + e.getMessage());
		}
	}
	
	
    
	public void lectureTpsReel() {
		int adresseLecture = 0;
		int cptLecture = 0;
		int adresseDI = 0;
		int bitTest = 0;
		int valeurTest = 0;
		String strSql;
		@SuppressWarnings("unused")
		boolean blChgtAI = false;
		@SuppressWarnings("unused")
		boolean blChgtDI = false;
		double pctPrg = 0;
		int cptPrg = 0;
		
		lblInformation.setText("Lecture numéro : " + this.cptLecture);
		this.cptLecture++;
		
//		pnlInfo.setLblInformation(0, "Lecture temps réel des AI ...");
		try{
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			double [] reqReponse;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				if (MODBUS_DEBUG) System.out.println("API Connecté ... ");
			}
			else {
				if (MODBUS_DEBUG) System.out.println("API Déconnecté ... ");
			}

			// ===== Envoi des requetes de lecture pour les AI =====
			cptLecture = 0;
			do {
				adresseLecture = ADR_API_AI_TPS_REEL + (cptLecture * NB_MOT_LECTURE_AI);
				if (adresseLecture < (ADR_API_AI_TPS_REEL + MAX_AI)) {
					reqReponse = con.setRequest(con.createRequest(READ_MULTIPLE_REGISTERS, adresseLecture, NB_MOT_LECTURE_AI));
					for (int i = 0; i < NB_MOT_LECTURE_AI; i++) {
						tbAI[(adresseLecture - ADR_API_AI_TPS_REEL) + i] = reqReponse[i]; 
					} // Fin for i
				} // Fin if
				cptLecture++;
			} while(adresseLecture < (ADR_API_AI_TPS_REEL + MAX_AI)); // Fin while

			for(int i = 0; i < cptCapteurAna; i++) {
				tbAnaAPI[i].setValeurAPI(tbAI[tbAnaAPI[i].getVoieApi()]);
			}
			

			// ===== Envoi des requetes de lecture pour les DI =====
			reqReponse = con.setRequest(con.createRequest(READ_MULTIPLE_REGISTERS, ADR_API_DI_TPS_REEL, NB_MOT_LECTURE_DI));
			for (int i = 0; i < NB_MOT_LECTURE_DI; i++) {
				for (int j = 0; j < 16; j++) {
					adresseDI = (i * 16) + j;
					bitTest = (int)(Math.pow(2, j));
					valeurTest = (int) reqReponse[i];
					if ((valeurTest & bitTest) == bitTest) {
						tbDI[adresseDI] = 1;
					} // Fin if
					else {
						tbDI[adresseDI] = 0;
					}
				} // Fin for j
			} // Fin for i

			// ===== Envoi des requetes de lecture pour les échanges Maitre - Client =====
			adresseLecture = ADR_API_ECHANGE_MAITRE_CLIENT;
			reqReponse = con.setRequest(con.createRequest(READ_MULTIPLE_REGISTERS, adresseLecture, NB_MOT_LECTURE_MAITRE_CLIENT));
			for (int i = 0; i < NB_MOT_LECTURE_MAITRE_CLIENT; i++) {
				tbEchangeMaitreClient[i] = reqReponse[i]; 
			} // Fin for i
			
            con.close();
            if (MODBUS_DEBUG) System.out.println("API Close ... ");
            
		} // Fin Try
		catch (Exception e){
			System.out.println("Erreur Lecture ... ");
			System.err.println(e.getMessage());
		} // Fin catch		

		// Contrôle si changement dans les valeurs
		blChgtAI = false; blChgtDI = false;
		for(int i = 0; i < MAX_AI; i++) {
			if((tbIdAI[i] != -1) && (tbAncAI[i] != tbAI[i])) {
				blChgtAI = true;
			}
		} // fin for i
		for(int i = 0; i < MAX_DI; i++) {
			if((tbIdDI[i] != -1) && (tbAncDI[i] != tbDI[i])) {
				blChgtDI = true;
			}
		} // fin for i
		
/*
		// =====================> Enregistrement des valeurs HISTORIQUE <=====================================
        if (blEnregistrementHistorique) {
        	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));  
        	pnlInfo.setJpbInformationValue(0); cptPrg = 0; pctPrg = (double) (100 / (MAX_AI + MAX_DI));
            if (MODBUS_DEBUG) System.out.println("Enregistrement des AI Historique ");
            pnlInfo.setLblInformation(0, "Enregistrement des AI Historique ...");
			for (int i = 0; i < MAX_AI; i++) {
				if (tbIdAI[i] != -1) {
					strSql = "INSERT INTO AI_Historique (idCapteur, VoieApi, Valeur, DateLecture) "
							+ "VALUES (" + tbIdAI[i] + ", " + (i + 1) + ", " + (tbAI[i] + tbCalibrationAI[i]) + ", sysdate)"; 
					ctn.fonctionSql(strSql);
				} // Fin if != -1
				pnlInfo.setJpbInformationValue((int) ((cptPrg++) * pctPrg));
				pnlInfo.repaint();
			} // Fin for i
            pnlInfo.setLblInformation(0, "Enregistrement des DI Historique ...");
			for (int i = 0; i < MAX_DI; i++) {
				if (tbIdDI[i] != -1) {
					strSql = "INSERT INTO DI_Historique (idCapteur, VoieApi, Valeur, DateLecture) "
							+ "VALUES (" + tbIdDI[i] + ", " + (i + 1) + ", " + tbDI[i] + ", sysdate)"; 
					ctn.fonctionSql(strSql);
				} // Fin if != -1
				pnlInfo.setJpbInformationValue((int) ((cptPrg++) * pctPrg));
				pnlInfo.repaint();
			} // Fin for i
			blEnregistrementHistorique = false;
			pnlInfo.setJpbInformationValue(100);
			pnlInfo.setJpbInformationValue(0);
        	setCursor(Cursor.getDefaultCursor());
        } // Fin if blEnregistrementHistorique
		// =====================> Fin Enregistrement des valeurs HISTORIQUE <=====================================
*/
		
	} // Fin lectureTpsReel	


	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == tmrTpsReel) {
			try {
				tmrTpsReel.stop();
				lectureTpsReel();
				tmrTpsReel.start();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			} // Fin try					
		} // Fin If
		
	}
	
}
