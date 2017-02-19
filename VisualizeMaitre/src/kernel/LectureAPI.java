package kernel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.Timer;

import em.communication.AE_TCP_Connection;
import em.communication.AE_TCP_Modbus;
import em.fonctions.GestionLogger;
import em.general.EFS_General;
import em.general.EFS_Maitre_Variable;

/**
 * Lecture des valeurs dans l'API et gestion des résultats
 * @author Eric
 *
 */
public class LectureAPI implements VoiesAPI, ActionListener, EFS_General {
	private Timer tmrTpsReel = new Timer(TIMER_LECTURE_TPS_REEL, this);	
	private Timer tmrHistoriserValeurAPI = new Timer(TIMER_ENREGISTREMENT_HISTORIQUE, this);	
	
	/**
	 * Constructeur
	 */
	public LectureAPI() {
		tmrTpsReel.start();
		tmrHistoriserValeurAPI.start();
	}
	
	
	/**
	 * Lecture des valeurs dans l'API (automate)
	 */
	public static void lectureTpsReel() {
		int adresseLecture = 0;
		int cptLecture = 0;
		double tbAI[] = new double [MAX_AI];
		int tbDI[] = new int [MAX_DI];
		
		try {
			// ===== Ouverture de la connection =====================
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			double [] reqReponse;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, EFS_General.MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				EFS_Maitre_Variable.nombreLectureAPI++;
			}
			else {
				GestionLogger.gestionLogger.warning("Erreur connexion MODBUS ... ");
			}
			// ======================================================

			// ===== Envoi des requetes de lecture pour les AI ======
			cptLecture = 0;
			do {
				adresseLecture = ADR_API_AI_TPS_REEL + (cptLecture * NB_MOT_LECTURE_AI);
				if (adresseLecture < (ADR_API_AI_TPS_REEL + MAX_AI)) {
					reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, adresseLecture, NB_MOT_LECTURE_AI));
					for (int i = 0; i < NB_MOT_LECTURE_AI; i++) {
						tbAI[(adresseLecture - ADR_API_AI_TPS_REEL) + i] = reqReponse[i]; 
					} // Fin for i
				} // Fin if
				cptLecture++;
			} while(adresseLecture < (ADR_API_AI_TPS_REEL + MAX_AI)); // Fin while

			for(int i = 0; i < tbAnaAPI.size(); i++) {
				tbAnaAPI.get(i).setValeurAPI(tbAI[tbAnaAPI.get(i).getVoieApi()]);
			}
			// ======================================================
			
			// ===== Envoi des requetes de lecture pour les DI ======
			int adresseDI = 0;
			int bitTest = 0;
			int valeurTest = 0;
			
			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, ADR_API_DI_TPS_REEL, NB_MOT_LECTURE_DI));
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
			
			for(int i = 0; i < tbDigiAPI.size(); i++) {
				tbDigiAPI.get(i).setValeurAPI(tbDI[tbDigiAPI.get(i).getVoieApi()]);
			}
			// ======================================================
			
			
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur lecture MODBUS : " + e.getMessage());
		} // Fin catch	
	}	
	
	/**
	 * Gere la liste des alarmes en cours
	 */
	private void gestionAlarmesEnCours() {
		boolean trouve = false;
		// Parcourir tous les AI
		for(int i = 0; i < tbAnaAPI.size(); i++) {
			// Regarder si en alarme
			if(tbAnaAPI.get(i).isAlarmeEnclenchee()) {
				// Regarder si tempo dépassée
				if(tbAnaAPI.get(i).isAlarmeTempoEcoulee()) {
					// Regarder si déjà dans le tableau
					trouve = false;
					for(int j = 0; j < tbAlarme.size(); j++) {
						if(tbAnaAPI.get(i).getIdCapteur() == tbAlarme.get(j).getIdCapteur()) {
							trouve = true;
						}
					}
					if(!trouve) {
						tbAlarme.add(new AlarmeEnCours(CAPTEUR_ANALOGIQUE_ENTREE, i));
					}
				} // fin if Tempo
			} // fin if Alarme
		} // fin for(i)	

		// Parcourir tous les DI
		for(int i = 0; i < tbDigiAPI.size(); i++) {
			// Regarder si en alarme
			if(tbDigiAPI.get(i).isAlarmeEnclenchee()) {
				// Regarder si tempo dépassée
				if(tbDigiAPI.get(i).isAlarmeTempoEcoulee()) {
					// Regarder si déjà dans le tableau
					trouve = false;
					for(int j = 0; j < tbAlarme.size(); j++) {
						if(tbDigiAPI.get(i).getIdCapteur() == tbAlarme.get(j).getIdCapteur()) {
							trouve = true;
						}
					}
					if(!trouve) {
						tbAlarme.add(new AlarmeEnCours(CAPTEUR_DIGITAL_ENTREE, i));
					}
				} // fin if Tempo
			} // fin if Alarme
		} // fin for(i)	
		
		
		for(int i = 0; i < tbAlarme.size(); i++) {
			// Nouvelle Valeur AI
			if(tbAlarme.get(i).getTypeCapteur() == CAPTEUR_ANALOGIQUE_ENTREE) {
				tbAlarme.get(i).setValeurAPI(tbAnaAPI.get(tbAlarme.get(i).getIndexCapteur()).getValeurAPI());
				// Disparition
				if(!tbAnaAPI.get(tbAlarme.get(i).getIndexCapteur()).isAlarmeEnclenchee()) {
					tbAlarme.get(i).setAlarmeEnclenchee(false);
				}
			}
			// Nouvelle Valeur DI
			if(tbAlarme.get(i).getTypeCapteur() == CAPTEUR_DIGITAL_ENTREE) {
				tbAlarme.get(i).setValeurAPI(tbDigiAPI.get(tbAlarme.get(i).getIndexCapteur()).getValeurAPI());
				// Disparition
				if(!tbDigiAPI.get(tbAlarme.get(i).getIndexCapteur()).isAlarmeEnclenchee()) {
					tbAlarme.get(i).setAlarmeEnclenchee(false);
				}
			}
			// Historiser
			if(tbAlarme.get(i).isHistoriser()) {
				GestionSGBD.historiserAlarme(i);
				tbAlarme.remove(i);
			}
		}
	}
	
	/**
	 * Lance l'enregistrement des valeurs dans la base
	 */
	private void enregistrerValeurAPI() {
		new Thread(
				new Runnable() {
					public void run() {
						GestionSGBD.historiseValeurVoiesAPI();			    	  
					} // Fin run()
				} // Fin Runnable
			).start(); // Fin new Thread
	}
	
	
	/**
	 * Gestion des actions
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == tmrTpsReel) {
			try {
				tmrTpsReel.stop();
				lectureTpsReel();
				gestionAlarmesEnCours();
				tmrTpsReel.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER de lecture des valeurs ...");
			} // Fin try		
		}
		if (ae.getSource() == tmrHistoriserValeurAPI) {
			try {
				tmrHistoriserValeurAPI.stop();
				enregistrerValeurAPI();
				tmrHistoriserValeurAPI.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER d'enregistrement des valeurs ...");
			} // Fin try		
		}
	}
	
}
