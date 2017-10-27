package kernel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
	private int cptActivite = 0;

	private Timer tmrTpsReel = new Timer(TIMER_LECTURE_TPS_REEL, this);	
	private Timer tmrHistoriserValeurAPI = new Timer(TIMER_ENREGISTREMENT_HISTORIQUE, this);	
	private Timer tmrMinute = new Timer(TIMER_MINUTE, this);	
	private boolean blTestHoraire = false;
	
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
			
			// Fermeture connection
			con.close();
			
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
			
			// ===== Envoi des requetes de lecture pour les Mots d'échange ======
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
					} else {
						GestionLogger.gestionLogger.info("Prise en compte via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;
				case VIA_API_TEMPO:
					if (!GestionSGBD.modifierViaAPI(VIA_API_TEMPO, (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur Tempo Modifier via API ... ");
					} else {
						GestionLogger.gestionLogger.info("Modification Tempo via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;
				case VIA_API_PRE_SEUIL_TEMPO:
					if (!GestionSGBD.modifierViaAPI(VIA_API_PRE_SEUIL_TEMPO, (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur Tempo Pre Seuil Modifier via API ... ");
					} else {
						GestionLogger.gestionLogger.info("Modification Tempo pre seuil via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;
				case VIA_API_CALIBRATION:
					if (!GestionSGBD.modifierViaAPI(VIA_API_CALIBRATION, (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur Calibration Modifier via API ... ");
					} else {
						GestionLogger.gestionLogger.info("Modification Calibration via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;
				case VIA_API_NO_NF:
					if (!GestionSGBD.modifierViaAPI(VIA_API_NO_NF, (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur NO/NF Modifier via API ... ");
					} else {
						GestionLogger.gestionLogger.info("Modification NO/NF via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;
				case VIA_API_ALARME:
					if (!GestionSGBD.modifierViaAPI(VIA_API_ALARME, (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur Alarme Modifier via API ... ");
					} else {
						GestionLogger.gestionLogger.info("Modification Alarme via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;
				case VIA_API_MAINTENANCE:
					if (!GestionSGBD.modifierViaAPI(VIA_API_MAINTENANCE, (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur Maintenance Modifier via API ... ");
					} else {
						EFS_Maitre_Variable.modificationEnMaintenance = true;
						GestionLogger.gestionLogger.info("Modification Maintenance via API idCapteur = " + tbEchange[1]);
					}
					resetEchangeViaAPI();
					break;

					
				case VIA_API_SEUIL_BAS:
				case VIA_API_SEUIL_HAUT:
				case VIA_API_PRE_SEUIL_BAS:
				case VIA_API_PRE_SEUIL_HAUT:
					if (!GestionSGBD.modifierViaAPI((int) tbEchange[0], (long)tbEchange[1])) {
						GestionLogger.gestionLogger.info("Erreur Seuil Modifier via API ... ");
					} else {
						GestionLogger.gestionLogger.info("Modification Seuil via API idCapteur = " + tbEchange[1]);
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
		ecritureActivite();
	}
	
	/**
	 * Test si Alert fonctionne (vers 16h ...)
	 */
	private void testAlarmeHoraire() {
		// Test horaire du soir
	    Calendar cal = Calendar.getInstance();
		Date now = new Date(); 
		    
	   cal.setTime(now);
	   int heure = cal.get(Calendar.HOUR_OF_DAY);
	   int minute = cal.get(Calendar.MINUTE);
	   
	   if (((heure >= TEST_GTC_HEURE && minute > EFS_Maitre_Variable.TEST_GTC_MINUTE) && 
			   (heure < TEST_GTC_HEURE + 1 && minute < EFS_Maitre_Variable.TEST_GTC_MINUTE + 10))) {
		   if (!blTestHoraire) {
		       blTestHoraire = true;
		       gestionTestAlert(true);
		       // Mise à jour de l'heure automate
		       ecrireDate();
		   }
	   }
	   else {
		   if (blTestHoraire) {
			   gestionTestAlert(false);
		   }			   
	       blTestHoraire = false;
	   }
	} // Fin testAlarmeHoraire()	
	
	/**
	 * Remet l'automate à l'heure
	 */
	private void ecrireDate() {
		Date now = new Date();
		String dateSeconde = "";
		String dateMinute = "";
		String dateHeure = "";
		String dateJour = "";
		String dateMois = "";
		String dateAnnee = "";
		SimpleDateFormat formaterSeconde = null;
		SimpleDateFormat formaterMinute = null;
		SimpleDateFormat formaterHeure = null;
		SimpleDateFormat formaterJour = null;
		SimpleDateFormat formaterMois = null;
		SimpleDateFormat formaterAnnee = null;
		formaterSeconde = new SimpleDateFormat("ss"); 		
		formaterMinute = new SimpleDateFormat("mm"); 		
		formaterHeure = new SimpleDateFormat("HH"); 		
		formaterJour = new SimpleDateFormat("dd"); 		
		formaterMois = new SimpleDateFormat("MM"); 		
		formaterAnnee = new SimpleDateFormat("yyyy"); 		

		dateSeconde = formaterSeconde.format(now);
		dateMinute = formaterMinute.format(now);
		dateHeure = formaterHeure.format(now);
		dateJour = formaterJour.format(now);
		dateMois = formaterMois.format(now);
		dateAnnee = formaterAnnee.format(now);
		
		
		try {
			// ===== Ouverture de la connection =====
			//  Variables TCP
			InetAddress addr = null; // Adresse IP du serveur	
			AE_TCP_Connection con = null; //the connection
//			double [] reqReponse;
			
			int echangeDate = 0;
			int var1 = 0;
			int var2 = 0;
			int var3 = 0;
			
			addr = InetAddress.getByName(EFS_Maitre_Variable.ADR_IP_API);
			con = new AE_TCP_Connection(addr, MODBUS_PORT);
			con.connect();

			// ===== Ecriture d'un mot =====
			echangeDate = Integer.parseInt(dateSeconde);  //28; // Seconde 28 s
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			echangeDate = (var1 * 16) + var2;
			echangeDate *= 256;
			
//			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2005, echangeDate));
			con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2005, echangeDate));

			
			echangeDate =  Integer.parseInt(dateMinute); //35; // 35 mn
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			var3 = (var1 * 16) + var2;	
			
			echangeDate =  Integer.parseInt(dateHeure); //13; // 13 h
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			echangeDate = (((var1 * 16) + var2) * 256) + var3;	
			
//			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2006, echangeDate));
			con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2006, echangeDate));


			echangeDate =  Integer.parseInt(dateJour);//25; // 22 jour
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			var3 = (var1 * 16) + var2;	
			
			echangeDate =  Integer.parseInt(dateMois); // 12; // 12 mois
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			echangeDate = (((var1 * 16) + var2) * 256) + var3;	
			
//			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2007, echangeDate));			
			con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2007, echangeDate));			
			
			echangeDate =  Integer.parseInt(dateAnnee) - 2000;//17; // 22 
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			var3 = (var1 * 16) + var2;	
			
			echangeDate = 20; // 12 
			var1 = echangeDate / 10;
			var2 = echangeDate % 10;
			
			echangeDate = (((var1 * 16) + var2) * 256) + var3;	
			
//			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2008, echangeDate));			
			con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2008, echangeDate));			
//			reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2009, 1));
			con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, 2009, 1));
			
            con.close();
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture MODBUS remise à lheure automate : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch			
	} // fin EcrireDate()
	
	
	/**
	 * Gestion des rappel Alert
	 */
	private void gererRappelAlert() {
		for(int i = 0; i < tbAlarme.size(); i++) {
			if(tbAlarme.get(i).getMnRappelAlert() > 0) {
				// Suppression si disparition
				if(!tbAlarme.get(i).isAlarmeEnclenchee()) {
					tbAlarme.get(i).setMnRappelAlert(0);
				} else {
					if(tbAlarme.get(i).getDateRappelAlert().plusMinutes(tbAlarme.get(i).getMnRappelAlert()).isBefore(LocalDateTime.now())) {
						GestionLogger.gestionLogger.info("[DIVERS] Rappel Alert après " + tbAlarme.get(i).getMnRappelAlert() + " mn");
						gestionAlert(true);
						tbAlarme.get(i).setMnRappelAlert(0);
					}
				}
			}
		}
	}
	
	/**
	 * Test le Systeme Alert
	 */
	private void gestionTestAlert(boolean alerte) {
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
			if (alerte) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, EFS_General.ADR_API_TEST_HORAIRE, 1));			
			} 
			else {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, EFS_General.ADR_API_TEST_HORAIRE, 0));			
			} // Fin if sonnerie
			con.close();
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture MODBUS Test horaire : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch

	} // Fin gestionTestAlert()			
	
	/**
	 * Test le systeme Alert
	 */
	public static void gestionAlert(boolean alerte) {
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
			if (alerte) {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, EFS_General.ADR_API_ALARME, 1));			
				GestionLogger.gestionLogger.info("[API] - Passage AlarmeAlerte.Alarme à 1");
			} 
			else {
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, EFS_General.ADR_API_ALARME, 0));			
				GestionLogger.gestionLogger.info("[API] - Passage AlarmeAlerte.Alarme à 0");
			} // Fin if sonnerie
			con.close();
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture MODBUS Alarme : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch

	} // Fin gestionTestAlert()	
	
	/**
	 * Ecriture dans API de activite
	 */
	private void ecritureActivite() {
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
				cptActivite++;
				if(cptActivite > 20000) cptActivite = 0;
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, EFS_General.ADR_API_CONTROLE_MAITRE, cptActivite));			
				reqReponse = con.setRequest(con.createRequest(AE_TCP_Modbus.WRITE_SINGLE_REGISTER, EFS_General.ADR_API_ALARME_CONTROLE_MAITRE, 0));			
			}
			else {
				GestionLogger.gestionLogger.warning("Erreur connexion MODBUS ... ");
			}
			con.close();
		} // Fin Try
		catch (Exception e){
			GestionLogger.gestionLogger.warning("Erreur ecriture Activite : " + e.getMessage());
			EFS_Maitre_Variable.compteurErreurAPI++;
		} // Fin catch

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
				testAlarmeHoraire();
				gererRappelAlert();
				tmrMinute.start();
			} catch (Exception ex) {
				GestionLogger.gestionLogger.warning("Probleme dans le TIMER Minute GestionActivite ...");
				EFS_Maitre_Variable.compteurErreurSGBD++;
				tmrMinute.start();
			} // Fin try		
		}
	}
	
}
