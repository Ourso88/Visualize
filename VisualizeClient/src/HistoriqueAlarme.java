import java.util.Date;


public class HistoriqueAlarme {
	private int idCapteur;
	private String nom;
	private String description;
	private Date apparition;
	private Date acquittement;
	private Date disparition;
	private String acquitementPersonne;
	private String descriptionAlarme;
	private String raison;
	private String utilisateur;
	private String commentairePriseEnCompte;
	private int idAlarmeHistorique;
	private int motifIdPriseEncompte;
	
	public HistoriqueAlarme(int idCapteur, String nom, String description, Date apparition, Date disparition, 
			Date acquitement, String descriptionAlarme, String raison, String utilisateur, String commentairePriseEnCompte) {
		super();
		
		this.setIdCapteur(idCapteur);
		this.setNom(nom);
		this.setDescription(description);
		this.setApparition(apparition);
		this.setDisparition(disparition);
		this.setAcquittement(acquitement);
		this.setDescriptionAlarme(descriptionAlarme);
		this.setRaison(raison);
		this.setUtilisateur(utilisateur);
		this.setCommentairePriseEnCompte(commentairePriseEnCompte);
		this.setIdAlarmeHistorique(-1);
	}

	public HistoriqueAlarme(int idCapteur, String nom, String description, Date apparition, Date disparition, 
			Date acquitement, String descriptionAlarme, String raison, String utilisateur, String commentairePriseEnCompte,
			int idAlarmeHistorique) {
		super();
		
		this.setIdCapteur(idCapteur);
		this.setNom(nom);
		this.setDescription(description);
		this.setApparition(apparition);
		this.setDisparition(disparition);
		this.setAcquittement(acquitement);
		this.setDescriptionAlarme(descriptionAlarme);
		this.setRaison(raison);
		this.setUtilisateur(utilisateur);
		this.setCommentairePriseEnCompte(commentairePriseEnCompte);
		this.setIdAlarmeHistorique(idAlarmeHistorique);
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
	 * @return the raison
	 */
	public String getRaison() {
		return raison;
	}

	/**
	 * @param raison the raison to set
	 */
	public void setRaison(String raison) {
		this.raison = raison;
	}

	/**
	 * @return the utilisateur
	 */
	public String getUtilisateur() {
		return utilisateur;
	}

	/**
	 * @param utilisateur the utilisateur to set
	 */
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
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

	/**
	 * @return the idAlarmeHistorique
	 */
	public int getIdAlarmeHistorique() {
		return idAlarmeHistorique;
	}

	/**
	 * @param idAlarme the idAlarmeHistorique to set
	 */
	public void setIdAlarmeHistorique(int idAlarmeHistorique) {
		this.idAlarmeHistorique = idAlarmeHistorique;
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

}
