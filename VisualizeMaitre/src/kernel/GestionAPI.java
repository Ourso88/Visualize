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
public class GestionAPI implements VoiesAPI, ActionListener, EFS_General {
	private Timer tmrTpsReel = new Timer(TIMER_LECTURE_TPS_REEL, this);	
	private Timer tmrHistoriserValeurAPI = new Timer(TIMER_ENREGISTREMENT_HISTORIQUE, this);	
	private Timer tmrMinute = new Timer(TIMER_MINUTE, this);	
	
	/**
	 * Constructeur
	 */
	public GestionAPI() {
		tmrTpsReel.start();
		tmrHistoriserValeurAPI.start();
		tmrMinute.start();
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
				EFS_Maitre_Variable.compteurErreurAPI++;
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
				tbAnaAPI.get(i).setValeurAPI(tbAI[tbAnaAPI.get(i).getVoieApi() - 1]);
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
				tbDigiAPI.get(i).setValeurAPI(tbDI[tbDigiAPI.get(i).getVoieApi() - 1]);
			}
			// ======================================================
			
			
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur lecture MODBUS : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch	
	}	
	
	/**
	 * Gere la liste des alarmes en cours
	 */
	private void gestionAlarmesEnCours() {
		boolean trouve = false;
		try {
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
							GestionSGBD.enregistrerAlarmeEnCours(tbAlarme.size() - 1);
						}
					} // fin if Tempo
				} // fin if Alarme
			} // fin for(i)
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Lecture des AI : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		}

		// Parcourir tous les DI
		try {
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
							GestionSGBD.enregistrerAlarmeEnCours(tbAlarme.size() - 1);
						}
					} // fin if Tempo
				} // fin if Alarme
			} // fin for(i)	
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Lecture des DI : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		}

		try {
			for(int i = 0; i < tbAlarme.size(); i++) {
				// Nouvelle Valeur AI
				if(tbAlarme.get(i).getTypeCapteur() == CAPTEUR_ANALOGIQUE_ENTREE) {
					tbAlarme.get(i).setValeurAPI(tbAnaAPI.get(tbAlarme.get(i).getIndexCapteur()).getValeurAPI());
					// Modification date Apparition
					if(tbAlarme.get(i).getDateApparition() != tbAnaAPI.get(tbAlarme.get(i).getIndexCapteur()).getDateAlarmeApparition()) {
						tbAlarme.get(i).setDateApparition(tbAnaAPI.get(tbAlarme.get(i).getIndexCapteur()).getDateAlarmeApparition());
					}
					// Disparition
					if(!tbAnaAPI.get(tbAlarme.get(i).getIndexCapteur()).isAlarmeEnclenchee()) {
						tbAlarme.get(i).setAlarmeEnclenchee(false);
					} else {
						tbAlarme.get(i).setAlarmeEnclenchee(true);
					}
				}
				// Nouvelle Valeur DI
				if(tbAlarme.get(i).getTypeCapteur() == CAPTEUR_DIGITAL_ENTREE) {
					tbAlarme.get(i).setValeurAPI(tbDigiAPI.get(tbAlarme.get(i).getIndexCapteur()).getValeurAPI());
					// Modification Date Apparition
					if(tbAlarme.get(i).getDateApparition() != tbDigiAPI.get(tbAlarme.get(i).getIndexCapteur()).getDateAlarmeApparition()) {
						tbAlarme.get(i).setDateApparition(tbDigiAPI.get(tbAlarme.get(i).getIndexCapteur()).getDateAlarmeApparition());
					}
					// Disparition
					if(!tbDigiAPI.get(tbAlarme.get(i).getIndexCapteur()).isAlarmeEnclenchee()) {
						tbAlarme.get(i).setAlarmeEnclenchee(false);
					} else {
						tbAlarme.get(i).setAlarmeEnclenchee(true);
					}
				}
				// Historiser
				if(tbAlarme.get(i).isHistoriser()) {
					GestionSGBD.historiserAlarme(i);
					tbAlarme.remove(i);
					break;
				}
			}
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Gestion des alarmes : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		}
			
	}
	/**
	 * Gere les pre seuil
	 */
	private void gestionAlarmesPreSeuil() {
		boolean trouve = false;
		
		try {
			// Parcourir tous les tempos non enclenchées
			for(int i = 0; i < tbAnaAPI.size(); i++) {
				// Regarder si en alarme
				if(tbAnaAPI.get(i).isAlarmeEnclenchee() && (tbAnaAPI.get(i).getActivationPreSeuil() == PRE_SEUIL_EN_ACTIVITE)) {
					// Regarder si tempo dépassée
					if(!tbAnaAPI.get(i).isAlarmeTempoEcoulee()) {
						// Regarder si déjà dans le tableau
						trouve = false;
						for(int j = 0; j < tbAlarmeSeuil.size(); j++) {
							if(tbAnaAPI.get(i).getIdCapteur() == tbAlarmeSeuil.get(j).getIdCapteur()) {
								trouve = true;
							}
						}
						if(!trouve) {
							tbAlarmeSeuil.add(new AlarmeSeuil(i));
						}
					} // fin if Tempo
				} // fin if Alarme
			} // fin for(i)	
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Tempo non enclenchées : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		}
		
		try {
			// Parcourir tous les pre seuil enclenchées
			for(int i = 0; i < tbAnaAPI.size(); i++) {
				// Regarder si en alarme
				if(tbAnaAPI.get(i).isPreAlarmeEnclenchee()) {
					// Regarder si tempo dépassée
					if(tbAnaAPI.get(i).isPreAlarmeTempoEcoulee()) {
						// Regarder si déjà dans le tableau
						trouve = false;
						for(int j = 0; j < tbAlarmeSeuil.size(); j++) {
							if(tbAnaAPI.get(i).getIdCapteur() == tbAlarmeSeuil.get(j).getIdCapteur()) {
								trouve = true;
							}
						}
						if(!trouve) {
							tbAlarmeSeuil.add(new AlarmeSeuil(i));
						}
					} // fin if Tempo
				} // fin if Alarme
			} // fin for(i)
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Pre Seuil enclenchées : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		}
		
		try {
			// Retirer ceux qui ont changé de statut
			for(int i = 0; i < tbAlarmeSeuil.size(); i++) {
				// Nouvelle Valeur AI
				tbAlarmeSeuil.get(i).setValeurAPI(tbAnaAPI.get(tbAlarmeSeuil.get(i).getIndexCapteur()).getValeurAPI());
				tbAlarmeSeuil.get(i).setSeuilAtteint(tbAnaAPI.get(tbAlarmeSeuil.get(i).getIndexCapteur()).getSeuilAtteint());
				// Disparition
				if(!tbAnaAPI.get(tbAlarmeSeuil.get(i).getIndexCapteur()).isPreAlarmeEnclenchee()) {
					if(!tbAnaAPI.get(tbAlarmeSeuil.get(i).getIndexCapteur()).isAlarmeEnclenchee()) {
						tbAlarmeSeuil.remove(i);
						break;
					}
				}
				if(tbAnaAPI.get(tbAlarmeSeuil.get(i).getIndexCapteur()).isAlarmeEnclenchee() && tbAnaAPI.get(tbAlarmeSeuil.get(i).getIndexCapteur()).isAlarmeTempoEcoulee()) {
					tbAlarmeSeuil.remove(i);
					break;
				}
			}
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Changement de statut : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		}
		
	}
	
	/**
	 * Lance l'enregistrement des valeurs dans la base
	 */
	private void enregistrerValeurAPI() {
//		GestionSGBD.historiseValeurVoiesAPI();
		
		new Thread(
				new Runnable() {
					public void run() {
						GestionSGBD.historiseValeurVoiesAPI();			    	  
					} // Fin run()
				} // Fin Runnable
			).start(); // Fin new Thread
			
	}
	
	/**
	 * Gestion du Klaxon commandé par API
	 * @param sonnerie
	 */
	public static void gestionKlaxon(boolean sonnerie) {
/*		
		try {
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			@SuppressWarnings("unused")
			double [] reqReponse = null;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				EFS_Maitre_Variable.nombreLectureAPI++;
			}
			else {
				GestionLogger.gestionLogger.warning("Erreur connexion MODBUS ... ");
			}
			if (sonnerie) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2010, 1));			
			} 
			else {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2010, 0));			
			} // Fin if sonnerie
			con.close();
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture MODBUS Klaxon : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch
*/		
	} // Fin gestionKlaxon()
	
	/**
	 * Gere les echanges via l'automate
	 */
	private void gestionEchangeViaAPI() {
		double tbEchange[] = new double [MAX_ECHANGE_MAITRE_CLIENT];
		
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
				EFS_Maitre_Variable.compteurErreurAPI++;
			}
			// ======================================================
			
			// ===== Envoi des requetes de lecture pour les AI ======
			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.READ_MULTIPLE_REGISTERS, ADR_API_ECHANGE_MAITRE_CLIENT, MAX_ECHANGE_MAITRE_CLIENT));
			for (int i = 0; i < MAX_ECHANGE_MAITRE_CLIENT; i++) {
				tbEchange[i] = reqReponse[i]; 
			} // Fin for i

			// ======================================================
			con.close();

			if(tbEchange[0] > 0) {
				switch((int) tbEchange[0]) {
				case VIA_API_PRISE_EN_COMPTE:
					if (!GestionSGBD.prendreEnCompteViaAPI((long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur prise en compte via API ... ");
					}
					resetEchangeViaAPI();
					break;
				default:
					break;
				}
			}
			
		} catch (Exception e) {
			GestionLogger.gestionLogger.warning("Erreur lecture MODBUS : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch	
		
	}
	
	/**
	 * Remet à zéro la zone d'échange API
	 */
	private void resetEchangeViaAPI() {
		try{
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
			@SuppressWarnings("unused")
			double [] reqReponse = null;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, MODBUS_PORT);
			con.connect();
			if (con.isConnected()) {
				EFS_Maitre_Variable.nombreLectureAPI++;
			}
			else {
				GestionLogger.gestionLogger.warning("Erreur connexion MODBUS ... ");
			}
			for(int i = 0; i < MAX_ECHANGE_MAITRE_CLIENT; i++) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, ADR_API_ECHANGE_MAITRE_CLIENT + i, 0));
			}
			con.close();
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture MODBUS reset zone échange via API : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch
		
	}
	
	/**
	 * Inscrit dans la base l'activite du programme
	 */
	private void gestionActivite() {
//		GestionSGBD.ecritureActivite();
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
				gestionAlarmesPreSeuil();
				gestionEchangeViaAPI();
				tmrTpsReel.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER de lecture des valeurs ...");
				EFS_Maitre_Variable.compteurErreurAPI++;
				tmrTpsReel.start();
			} // Fin try		
		}
		if (ae.getSource() == tmrHistoriserValeurAPI) {
			try {
				tmrHistoriserValeurAPI.stop();
				enregistrerValeurAPI();
				tmrHistoriserValeurAPI.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER d'enregistrement des valeurs ...");
				EFS_Maitre_Variable.compteurErreurAPI++;
				tmrHistoriserValeurAPI.start();
			} // Fin try		
		}
		if (ae.getSource() == tmrMinute) {
			try {
				tmrMinute.stop();
				gestionActivite();
				tmrMinute.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER Minute GestionActivite ...");
				EFS_Maitre_Variable.compteurErreurSGBD++;
				tmrMinute.start();
			} // Fin try		
		}
	}
	
}
