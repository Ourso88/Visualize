package kernel;

import java.time.LocalDateTime;

import em.general.EFS_General;

/**
 * Gere les alarmes qui ont atteint un pré-seuil
 * @author Eric Mariani
 * @since 21/02/2017
 *
 */
public class AlarmeSeuil implements EFS_General, VoiesAPI {
	private int indexCapteur; // Index du capteur dans tbAnaAPI[] ou tbDigiAPI[]

	private long idCapteur;
	private String nomCapteur;
	private String descriptionCapteur;
	private String inventaire;
	private LocalDateTime dateApparition;
	private double valeurAPI;
	private boolean preAlarmeEnclenchee; 
	private String seuilAtteint;
	private int activationPreSeuil;
	
	/**
	 * Constructeur
	 * @param indexCapteur
	 */
	public AlarmeSeuil(int indexCapteur) {
		this.setIndexCapteur(indexCapteur);
		this.setIdCapteur(tbAnaAPI.get(indexCapteur).getIdCapteur());
		this.setNomCapteur(tbAnaAPI.get(indexCapteur).getNom());
		this.setDescriptionCapteur(tbAnaAPI.get(indexCapteur).getDescription());
		this.setValeurAPI(tbAnaAPI.get(indexCapteur).getValeurAPI());
		this.setDateApparition(tbAnaAPI.get(indexCapteur).getDatePreAlarmeApparition());
		this.setValeurAPI(tbAnaAPI.get(indexCapteur).getValeurAPI());
		this.setPreAlarmeEnclenchee(tbAnaAPI.get(indexCapteur).isPreAlarmeEnclenchee());
		this.setSeuilAtteint(tbAnaAPI.get(indexCapteur).getSeuilAtteint());
		this.setInventaire(tbAnaAPI.get(indexCapteur).getInventaire());
		this.setActivationPreSeuil(tbAnaAPI.get(indexCapteur).getActivationPreSeuil());
	}

	/**
	 * @return the indexCapteur
	 */
	public int getIndexCapteur() {
		return indexCapteur;
	}

	/**
	 * @param indexCapteur the indexCapteur to set
	 */
	public void setIndexCapteur(int indexCapteur) {
		this.indexCapteur = indexCapteur;
	}

	/**
	 * @return the idCapteur
	 */
	public long getIdCapteur() {
		return idCapteur;
	}

	/**
	 * @param idCapteur the idCapteur to set
	 */
	public void setIdCapteur(long idCapteur) {
		this.idCapteur = idCapteur;
	}

	/**
	 * @return the nomCapteur
	 */
	public String getNomCapteur() {
		return nomCapteur;
	}

	/**
	 * @param nomCapteur the nomCapteur to set
	 */
	public void setNomCapteur(String nomCapteur) {
		this.nomCapteur = nomCapteur;
	}

	/**
	 * @return the descriptionCapteur
	 */
	public String getDescriptionCapteur() {
		return descriptionCapteur;
	}

	/**
	 * @param descriptionCapteur the descriptionCapteur to set
	 */
	public void setDescriptionCapteur(String descriptionCapteur) {
		this.descriptionCapteur = descriptionCapteur;
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
	 * @return the dateApparition
	 */
	public LocalDateTime getDateApparition() {
		return dateApparition;
	}

	/**
	 * @param dateApparition the dateApparition to set
	 */
	public void setDateApparition(LocalDateTime dateApparition) {
		this.dateApparition = dateApparition;
	}

	/**
	 * @return the valeurAPI
	 */
	public double getValeurAPI() {
		return valeurAPI;
	}

	/**
	 * @param valeurAPI the valeurAPI to set
	 */
	public void setValeurAPI(double valeurAPI) {
		this.valeurAPI = valeurAPI;
	}

	/**
	 * @return the preAlarmeEnclenchee
	 */
	public boolean isPreAlarmeEnclenchee() {
		return preAlarmeEnclenchee;
	}

	/**
	 * @param preAlarmeEnclenchee the preAlarmeEnclenchee to set
	 */
	public void setPreAlarmeEnclenchee(boolean preAlarmeEnclenchee) {
		this.preAlarmeEnclenchee = preAlarmeEnclenchee;
	}

	/**
	 * @return the seuilAtteint
	 */
	public String getSeuilAtteint() {
		return seuilAtteint;
	}

	/**
	 * @param seuilAtteint the seuilAtteint to set
	 */
	public void setSeuilAtteint(String seuilAtteint) {
		this.seuilAtteint = seuilAtteint;
	}

	/**
	 * @return the activationPreSeuil
	 */
	public int getActivationPreSeuil() {
		return activationPreSeuil;
	}

	/**
	 * @param activationPreSeuil the activationPreSeuil to set
	 */
	public void setActivationPreSeuil(int activationPreSeuil) {
		this.activationPreSeuil = activationPreSeuil;
	}
	
	
}
