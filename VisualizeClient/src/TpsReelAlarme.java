import java.util.Date;

import AE_General.AE_Constantes;
import kernel.GestionAPI;


public class TpsReelAlarme implements AE_Constantes {
	private int idCapteur;
	private String nom;
	private String description;
	private String inventaire;
	private Date apparition;
	private Date acquittement;
	private Date disparition;
	private String acquitementPersonne;
	private boolean blKlaxon;
	private boolean blAlert;
	private String descriptionAlarme;
	private int voieApi;
	private double valeurTpsReel;
	private int appelAlert; 
	private int motifIdPriseEncompte;
	private int rappelAlert;
	private int typeAlarme;
	
	public TpsReelAlarme(int idCapteur, String nom, String description, String inventaire,  Date apparition
			, Date priseEnCompte, String descriptionAlarme, int voieApi, boolean blAlert, Date disparition, int typeAlarme) {
		super();
		
		this.setIdCapteur(idCapteur);
		this.setNom(nom);
		this.setDescription(description);
		this.setInventaire(inventaire);
		this.setApparition(apparition);
		this.setAcquittement(priseEnCompte);
		this.setDescriptionAlarme(descriptionAlarme);
		this.setVoieApi(voieApi);
		this.setBlAlert(blAlert);
		this.setDisparition(disparition);
		this.setTypeAlarme(typeAlarme);
	}
	
	public String getStringTypeAlarme() {
		switch(this.typeAlarme) {
		case ALARME_ALERT:
			return "Alarme";
		case ALARME_DEFAUT:
			return "Défaut";
		case ALARME_ETAT:
			return "Etat";
		case ALARME_RIEN:
			return "Rien";
		default:
			return "Alarme";
		}
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the apparition
	 */
	public Date getApparition() {
		return apparition;
	}

	/**
	 * @param apparition the apparition to set
	 */
	public void setApparition(Date apparition) {
		this.apparition = apparition;
	}


	/**
	 * @return the acquittement
	 */
	public Date getAcquittement() {
		return acquittement;
	}


	/**
	 * @param acquittement the acquittement to set
	 */
	public void setAcquittement(Date acquittement) {
		this.acquittement = acquittement;
	}


	/**
	 * @return the disparition
	 */
	public Date getDisparition() {
		return disparition;
	}


	/**
	 * @param disparition the disparition to set
	 */
	public void setDisparition(Date disparition) {
		this.disparition = disparition;
	}

	/**
	 * @return the acquitementPersonne
	 */
	public String getAcquitementPersonne() {
		return acquitementPersonne;
	}

	/**
	 * @param acquitementPersonne the acquitementPersonne to set
	 */
	public void setAcquitementPersonne(String acquitementPersonne) {
		this.acquitementPersonne = acquitementPersonne;
	}

	/**
	 * @return the idCapteur
	 */
	public int getIdCapteur() {
		return idCapteur;
	}

	/**
	 * @param idCapteur the idCapteur to set
	 */
	public void setIdCapteur(int idCapteur) {
		this.idCapteur = idCapteur;
	}

	/**
	 * @return the blKlaxon
	 */
	public boolean isBlKlaxon() {
		return blKlaxon;
	}

	/**
	 * @param blKlaxon the blKlaxon to set
	 */
	public void setBlKlaxon(boolean blKlaxon) {
		this.blKlaxon = blKlaxon;
	}

	/**
	 * @return the blAlert
	 */
	public boolean isBlAlert() {
		return blAlert;
	}

	/**
	 * @param blAlert the blAlert to set
	 */
	public void setBlAlert(boolean blAlert) {
		this.blAlert = blAlert;
	}

	/**
	 * @return the descriptionAlarme
	 */
	public String getDescriptionAlarme() {
		return descriptionAlarme;
	}

	/**
	 * @param descriptionAlarme the descriptionAlarme to set
	 */
	public void setDescriptionAlarme(String descriptionAlarme) {
		this.descriptionAlarme = descriptionAlarme;
	}

	/**
	 * @return the voieApi
	 */
	public int getVoieApi() {
		return voieApi;
	}

	/**
	 * @param voieApi the voieApi to set
	 */
	public void setVoieApi(int voieApi) {
		this.voieApi = voieApi;
	}

	/**
	 * @return the valeurTpsReel
	 */
	public double getValeurTpsReel() {
		return valeurTpsReel;
	}

	/**
	 * @param valeurTpsReel the valeurTpsReel to set
	 */
	public void setValeurTpsReel(double valeurTpsReel) {
		this.valeurTpsReel = valeurTpsReel;
	}

	/**
	 * @return the appelAlert
	 */
	public int getAppelAlert() {
		return appelAlert;
	}

	/**
	 * @param appelAlert the appelAlert to set
	 */
	public void setAppelAlert(int appelAlert) {
		this.appelAlert = appelAlert;
	}

	/**
	 * @return the motifIdPriseEncompte
	 */
	public int getMotifIdPriseEncompte() {
		return motifIdPriseEncompte;
	}

	/**
	 * @param motifIdPriseEncompte the motifIdPriseEncompte to set
	 */
	public void setMotifIdPriseEncompte(int motifIdPriseEncompte) {
		this.motifIdPriseEncompte = motifIdPriseEncompte;
	}

	/**
	 * @return the rappelAlert
	 */
	public int getRappelAlert() {
		return rappelAlert;
	}

	/**
	 * @param rappelAlert the rappelAlert to set
	 */
	public void setRappelAlert(int rappelAlert) {
		this.rappelAlert = rappelAlert;
		// Ecrire la valeur dans V2_AlarmeEnCours
		// Prevenir Maitre Via API
		GestionAPI.ecrireRappelAlert(idCapteur, rappelAlert);
	}

	/**
	 * @return the inventaire
	 */
	public String getInventaire() {
		return inventaire;
	}

	/**
	 * @param inventaire the inventaire to set
	 */
	public void setInventaire(String inventaire) {
		this.inventaire = inventaire;
	}

	/**
	 * @return the typeAlarme
	 */
	public int getTypeAlarme() {
		return typeAlarme;
	}

	/**
	 * @param typeAlarme the typeAlarme to set
	 */
	public void setTypeAlarme(int typeAlarme) {
		this.typeAlarme = typeAlarme;
	}
} // Fin class
