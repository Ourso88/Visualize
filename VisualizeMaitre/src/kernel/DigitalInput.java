package kernel;

import java.time.LocalDateTime;

import em.fonctions.GestionLogger;
import em.general.EFS_General;

/**
 * Gere les capteurs digital entrant (Entrées API)
 * @author Eric Mariani
 * @since 15/02/2017
 */
public class DigitalInput extends Capteur implements EFS_General {
	private long idEntreeDigitale;
	private int tempo;
	private int nOnF;
	
	private int valeurAPI;
	private boolean alarmeEnclenchee;
	
	private LocalDateTime dateAlarmeApparition;
	private boolean alarmeTempoEcoulee;
	private boolean appelAlert;
	
	/**
	 * Constructeur vide
	 */
	public DigitalInput() {
	}

	/**
	 * Constructeur complet
	 * @param idCapteur
	 * @param nom
	 * @param description
	 * @param idEquipement
	 * @param idPosteTechnique
	 * @param idTypeMateriel
	 * @param idZoneSubstitution
	 * @param typeCapteur
	 * @param alarme
	 * @param idService
	 * @param voieApi
	 * @param inhibition
	 * @param idUnite
	 * @param contact
	 * @param idEntreeDigitale
	 * @param temo
	 * @param nOnF
	 */
	public DigitalInput(long idCapteur, String nom, String description, long idEquipement, long idPosteTechnique, long idTypeMateriel,	long idZoneSubstitution, 
			 int typeCapteur, int alarme, long idService, int voieApi, int inhibition, long idUnite, String contact, String inventaire, long idEntreeDigitale, int tempo, int nOnF) {
		this.setIdCapteur(idCapteur);
		this.setNom(nom);
		this.setDescription(description);
		this.setIdEquipement(idEquipement);
		this.setIdPosteTechnique(idPosteTechnique);
		this.setIdTypeMateriel(idTypeMateriel);
		this.setIdZoneSubstitution(idZoneSubstitution);
		this.setTypeCapteur(typeCapteur);
		this.setAlarme(alarme);
		this.setIdService(idService);
		this.setVoieApi(voieApi);
		this.setInhibition(inhibition);
		this.setIdUnite(idUnite);
		this.setContact(contact);
		this.setInventaire(inventaire);
		this.setIdEntreeDigitale(idEntreeDigitale);
		this.setTempo(tempo);
		this.setnOnF(nOnF);
	}

	/**
	 * @return the idEntreeDigitale
	 */
	public long getIdEntreeDigitale() {
		return idEntreeDigitale;
	}

	/**
	 * @param idEntreeDigitale the idEntreeDigitale to set
	 */
	public void setIdEntreeDigitale(long idEntreeDigitale) {
		this.idEntreeDigitale = idEntreeDigitale;
	}

	/**
	 * @return the tempo
	 */
	public int getTempo() {
		return tempo;
	}

	/**
	 * @param tempo the tempo to set
	 */
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	/**
	 * @return the nOnF
	 */
	public int getnOnF() {
		return nOnF;
	}

	/**
	 * @param nOnF the nOnF to set
	 */
	public void setnOnF(int nOnF) {
		this.nOnF = nOnF;
	}

	/**
	 * @return the valeurAPI
	 */
	public int getValeurAPI() {
		return valeurAPI;
	}

	/**
	 * @param valeurAPI the valeurAPI to set
	 */
	public void setValeurAPI(int valeurAPI) {
		if((valeurAPI == 1) && (this.getnOnF() == CAPTEUR_DIGITAL_NO)) {
			if(((this.getAlarme() == ALARME_ALERT) || (this.getAlarme() == ALARME_DEFAUT) || (this.getAlarme() == ALARME_ETAT)) && (this.getInhibition() == 0)) {
				this.setAlarmeEnclenchee(true);
			}
		} else if((valeurAPI == 0) && (this.getnOnF() == CAPTEUR_DIGITAL_NF)) {
			if(((this.getAlarme() == ALARME_ALERT) || (this.getAlarme() == ALARME_DEFAUT) || (this.getAlarme() == ALARME_ETAT)) && (this.getInhibition() == 0)) {
				this.setAlarmeEnclenchee(true);
			}
		} else {
			this.setAlarmeEnclenchee(false);
		}
		this.valeurAPI = valeurAPI;
	}

	/**
	 * @return the alarmeEnclenchee
	 */
	public boolean isAlarmeEnclenchee() {
		return alarmeEnclenchee;
	}

	/**
	 * @param alarmeEnclenchee the alarmeEnclenchee to set
	 */
	public void setAlarmeEnclenchee(boolean alarmeEnclenchee) {
		if(!this.alarmeEnclenchee && alarmeEnclenchee) {
			this.setDateAlarmeApparition(LocalDateTime.now());
		}
		// Calcul si tempo est passé
		if(alarmeEnclenchee) {
			if(this.getDateAlarmeApparition().plusSeconds(this.getTempo()).isBefore(LocalDateTime.now())) {
				this.setAlarmeTempoEcoulee(true);
			} else {
				this.setAlarmeTempoEcoulee(false);
			}
		}
		this.alarmeEnclenchee = alarmeEnclenchee;
	}

	/**
	 * @return the dateAlarmeApparition
	 */
	public LocalDateTime getDateAlarmeApparition() {
		return dateAlarmeApparition;
	}

	/**
	 * @param dateAlarmeApparition the dateAlarmeApparition to set
	 */
	public void setDateAlarmeApparition(LocalDateTime dateAlarmeApparition) {
		this.dateAlarmeApparition = dateAlarmeApparition;
	}

	/**
	 * @return the alarmeTempoEcoulee
	 */
	public boolean isAlarmeTempoEcoulee() {
		return alarmeTempoEcoulee;
	}

	/**
	 * @param alarmeTempoEcoulee the alarmeTempoEcoulee to set
	 */
	public void setAlarmeTempoEcoulee(boolean alarmeTempoEcoulee) {
		if(this.getAlarme() == ALARME_ALERT) {
			if(!this.isAlarmeTempoEcoulee() && alarmeTempoEcoulee) {
				// Appel Alert
				GestionSGBD.gestionAlert(true); 
				GestionAPI.gestionKlaxon(true);
				GestionLogger.gestionLogger.info("<== APPEL ALERT ==> Capteur :" + this.getNom());
				this.setAppelAlert(true);
			}
		} else {
			this.setAppelAlert(false);
		}
		this.alarmeTempoEcoulee = alarmeTempoEcoulee;
	}
	/**
	 * @return the appelAlert
	 */
	public boolean isAppelAlert() {
		return appelAlert;
	}

	/**
	 * @param appelAlert the appelAlert to set
	 */
	public void setAppelAlert(boolean appelAlert) {
		this.appelAlert = appelAlert;
	}

	
}
