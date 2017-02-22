package kernel;

/**
 * Gere les capteurs en maintenance
 * @author Eric Mariani
 * @since 21/02/2017
 */
public class CapteurMaintenance {
	private long idInhibition;
	private long idCapteur;
	private int typeCapteur;
	private String nomCapteur;
	private String dateInhibition;
	private String utilisateur;
	private String raisonMaintenance;
	
	/**
	 * Constructeur
	 * @param idInhibition
	 * @param idCapteur
	 * @param dateInhibition
	 * @param utilisateur
	 * @param raisonMaintenance
	 */
	public CapteurMaintenance(long idInhibition, long idCapteur, int typeCapteur, String nomCapteur, String dateInhibition,	String utilisateur,	String raisonMaintenance) {
		this.setIdInhibition(idInhibition);
		this.setIdCapteur(idCapteur);
		this.setTypeCapteur(typeCapteur);
		this.setNomCapteur(nomCapteur);
		this.setDateInhibition(dateInhibition);
		this.setUtilisateur(utilisateur);
		this.setRaisonMaintenance(raisonMaintenance);
	}

	/**
	 * @return the idInhibition
	 */
	public long getIdInhibition() {
		return idInhibition;
	}

	/**
	 * @param idInhibition the idInhibition to set
	 */
	public void setIdInhibition(long idInhibition) {
		this.idInhibition = idInhibition;
	}

	/**
	 * @return the dateInhibition
	 */
	public String getDateInhibition() {
		return dateInhibition;
	}

	/**
	 * @param dateInhibition the dateInhibition to set
	 */
	public void setDateInhibition(String dateInhibition) {
		this.dateInhibition = dateInhibition;
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
	 * @return the raisonMaintenance
	 */
	public String getRaisonMaintenance() {
		return raisonMaintenance;
	}

	/**
	 * @param raisonMaintenance the raisonMaintenance to set
	 */
	public void setRaisonMaintenance(String raisonMaintenance) {
		this.raisonMaintenance = raisonMaintenance;
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
	
}
