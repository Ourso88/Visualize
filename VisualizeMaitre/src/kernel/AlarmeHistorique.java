package kernel;


/**
 * Gere l'historique des alarmes
 * @author Eric Mariani
 * @since 21/02/2017
 */
public class AlarmeHistorique {
	private long idAlarmeHistorique;
	private long idCapteur;
	private int typeCapteur;
	private String dateApparition;
	private String datePriseEnCompte;
	private String dateDisparition;
	private String nomCapteur; 	
	private String descriptionCapteur; 	
	private long idAlarmeService;
	
	/**
	 * Constructeur
	 */
	public AlarmeHistorique(long idAlarmeHistorique, long idCapteur, int typeCapteur, String dateApparition, String datePriseEnCompte, String dateDisparition,	String nomCapteur, String descriptionCapteur,
							long idAlarmeService) {
		this.setIdAlarmeHistorique(idAlarmeHistorique);
		this.setIdCapteur(idCapteur);
		this.setTypeCapteur(typeCapteur);
		this.setDateApparition(dateApparition);
		this.setDatePriseEnCompte(datePriseEnCompte);
		this.setDateDisparition(dateDisparition);
		this.setNomCapteur(nomCapteur);
		this.setDescriptionCapteur(descriptionCapteur);
		this.setIdAlarmeService(idAlarmeService);
	}

	/**
	 * @return the idAlarmeHistorique
	 */
	public long getIdAlarmeHistorique() {
		return idAlarmeHistorique;
	}

	/**
	 * @param idAlarmeHistorique the idAlarmeHistorique to set
	 */
	public void setIdAlarmeHistorique(long idAlarmeHistorique) {
		this.idAlarmeHistorique = idAlarmeHistorique;
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
	 * @return the typeCapteur
	 */
	public int getTypeCapteur() {
		return typeCapteur;
	}

	/**
	 * @param typeCapteur the typeCapteur to set
	 */
	public void setTypeCapteur(int typeCapteur) {
		this.typeCapteur = typeCapteur;
	}

	/**
	 * @return the dateApparition
	 */
	public String getDateApparition() {
		return dateApparition;
	}

	/**
	 * @param dateApparition the dateApparition to set
	 */
	public void setDateApparition(String dateApparition) {
		this.dateApparition = dateApparition;
	}

	/**
	 * @return the datePriseEnCompte
	 */
	public String getDatePriseEnCompte() {
		return datePriseEnCompte;
	}

	/**
	 * @param datePriseEnCompte the datePriseEnCompte to set
	 */
	public void setDatePriseEnCompte(String datePriseEnCompte) {
		this.datePriseEnCompte = datePriseEnCompte;
	}

	/**
	 * @return the dateDisparition
	 */
	public String getDateDisparition() {
		return dateDisparition;
	}

	/**
	 * @param dateDisparition the dateDisparition to set
	 */
	public void setDateDisparition(String dateDisparition) {
		this.dateDisparition = dateDisparition;
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
	 * @return the idAlarmeService
	 */
	public long getIdAlarmeService() {
		return idAlarmeService;
	}

	/**
	 * @param idAlarmeService the idAlarmeService to set
	 */
	public void setIdAlarmeService(long idAlarmeService) {
		this.idAlarmeService = idAlarmeService;
	}



}
