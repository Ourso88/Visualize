package kernel;

import java.time.LocalDateTime;

import em.general.EFS_General;

/**
 * Gere les alarmes en cours
 * @author Eric Mariani
 * @since 18/02/2017
 */
public class AlarmeEnCours implements EFS_General, VoiesAPI {
	private int typeCapteur; // AI ou DI
	private int indexCapteur; // Index du capteur dans tbAnaAPI[] ou tbDigiAPI[]

	private long idCapteur;
	private String nomCapteur;
	private String descriptionCapteur;
	private String inventaire;
	private LocalDateTime dateApparition;
	private LocalDateTime dateDisparition;
	private LocalDateTime datePriseEnCompte;
	private double valeurAPI;
	private boolean alarmeEnclenchee; 
	private boolean prisEnCompte;
	private boolean historiser;
	private int idPriseEnCompte;
	private String commentairePriseEnCompte;
	
	public AlarmeEnCours(int typeCapteur, int indexCapteur) {
		this.setTypeCapteur(typeCapteur);
		this.setIndexCapteur(indexCapteur);
		if(typeCapteur == CAPTEUR_ANALOGIQUE_ENTREE) {
			this.setIdCapteur(tbAnaAPI.get(indexCapteur).getIdCapteur());
			this.setNomCapteur(tbAnaAPI.get(indexCapteur).getNom());
			this.setDescriptionCapteur(tbAnaAPI.get(indexCapteur).getDescription());
			this.setValeurAPI(tbAnaAPI.get(indexCapteur).getValeurAPI());
			this.setDateApparition(tbAnaAPI.get(indexCapteur).getDateAlarmeApparition());
			this.setValeurAPI(tbAnaAPI.get(indexCapteur).getValeurAPI());
			this.setAlarmeEnclenchee(tbAnaAPI.get(indexCapteur).isAlarmeEnclenchee());
		} else if(typeCapteur == CAPTEUR_DIGITAL_ENTREE) {
			this.setIdCapteur(tbDigiAPI.get(indexCapteur).getIdCapteur());
			this.setNomCapteur(tbDigiAPI.get(indexCapteur).getNom());
			this.setDescriptionCapteur(tbDigiAPI.get(indexCapteur).getDescription());
			this.setValeurAPI(tbDigiAPI.get(indexCapteur).getValeurAPI());
			this.setDateApparition(tbDigiAPI.get(indexCapteur).getDateAlarmeApparition());
			this.setValeurAPI(tbDigiAPI.get(indexCapteur).getValeurAPI());
			this.setAlarmeEnclenchee(tbDigiAPI.get(indexCapteur).isAlarmeEnclenchee());
		}
		this.setPrisEnCompte(false);
		this.setHistoriser(false);
		this.setIdPriseEnCompte(-1);
		this.setCommentairePriseEnCompte("---");
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
	 * @return the dateDisparition
	 */
	public LocalDateTime getDateDisparition() {
		return dateDisparition;
	}

	/**
	 * @param dateDisparition the dateDisparition to set
	 */
	public void setDateDisparition(LocalDateTime dateDisparition) {
		this.dateDisparition = dateDisparition;
	}

	/**
	 * @return the datePriseEnCompte
	 */
	public LocalDateTime getDatePriseEnCompte() {
		return datePriseEnCompte;
	}

	/**
	 * @param datePriseEnCompte the datePriseEnCompte to set
	 */
	public void setDatePriseEnCompte(LocalDateTime datePriseEnCompte) {
		this.datePriseEnCompte = datePriseEnCompte;
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
	 * @return the alarmeEnclenchee
	 */
	public boolean isAlarmeEnclenchee() {
		return alarmeEnclenchee;
	}

	/**
	 * @param alarmeEnclenchee the alarmeEnclenchee to set
	 */
	public void setAlarmeEnclenchee(boolean alarmeEnclenchee) {
		if(this.alarmeEnclenchee && !alarmeEnclenchee) {
			this.setDateDisparition(LocalDateTime.now());
		}
		if(this.prisEnCompte && !alarmeEnclenchee) {
			this.setHistoriser(true);
		}
		
		this.alarmeEnclenchee = alarmeEnclenchee;
	}

	/**
	 * @return the prisEncompte
	 */
	public boolean isPrisEnCompte() {
		return prisEnCompte;
	}

	/**
	 * @param prisEncompte the prisEncompte to set
	 */
	public void setPrisEnCompte(boolean prisEnCompte) {
		if(prisEnCompte && !this.isAlarmeEnclenchee()) {
			this.setHistoriser(true);
		}
		this.prisEnCompte = prisEnCompte;
	}

	/**
	 * @return the historiser
	 */
	public boolean isHistoriser() {
		return historiser;
	}

	/**
	 * @param historiser the historiser to set
	 */
	public void setHistoriser(boolean historiser) {
		this.historiser = historiser;
	}

	/**
	 * @return the idPriseEnCompte
	 */
	public int getIdPriseEnCompte() {
		return idPriseEnCompte;
	}

	/**
	 * @param idPriseEnCompte the idPriseEnCompte to set
	 */
	public void setIdPriseEnCompte(int idPriseEnCompte) {
		this.idPriseEnCompte = idPriseEnCompte;
	}

	/**
	 * @return the commentairePriseEnCompte
	 */
	public String getCommentairePriseEnCompte() {
		return commentairePriseEnCompte;
	}

	/**
	 * @param commentairePriseEnCompte the commentairePriseEnCompte to set
	 */
	public void setCommentairePriseEnCompte(String commentairePriseEnCompte) {
		this.commentairePriseEnCompte = commentairePriseEnCompte;
	}

	
}
