import java.util.Date;


public class Inhibition {
	private int idInhibition;
	private int idCapteur;
	private String nom;
	private String description;
	private Date derniereInhibition;
	private int idUtilisateur;
	private String nomUtilisateur;
	private int idRaisonInhibition;
	private String raisonInhibition;
	
	public Inhibition(int idInhibition, int idCapteur, String nom, String description, Date derniereInhibition, int idUtilisateur
			, String nomUtilisateur, int idRaisonInhibition, String raisonInhibition) {
		super();
		this.setIdInhibition(idInhibition);
		this.setIdCapteur(idCapteur);
		this.setNom(nom);
		this.setDescription(description);
		this.setDerniereInhibition(derniereInhibition);
		this.setIdUtilisateur(idUtilisateur);
		this.setNomUtilisateur(nomUtilisateur);
		this.setIdRaisonInhibition(idRaisonInhibition);
		this.setRaisonInhibition(raisonInhibition);
		
	} // fin Inhibition

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
	 * @return the derniereInhibition
	 */
	public Date getDerniereInhibition() {
		return derniereInhibition;
	}

	/**
	 * @param derniereInhibition the derniereInhibition to set
	 */
	public void setDerniereInhibition(Date derniereInhibition) {
		this.derniereInhibition = derniereInhibition;
	}

	/**
	 * @return the idUtilisateur
	 */
	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	/**
	 * @param idUtilisateur the idUtilisateur to set
	 */
	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	/**
	 * @return the nomUtilisateur
	 */
	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	/**
	 * @param nomUtilisateur the nomUtilisateur to set
	 */
	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	/**
	 * @return the idInhibition
	 */
	public int getIdInhibition() {
		return idInhibition;
	}

	/**
	 * @param idInhibition the idInhibition to set
	 */
	public void setIdInhibition(int idInhibition) {
		this.idInhibition = idInhibition;
	}

	/**
	 * @return the idRaisonInhibition
	 */
	public int getIdRaisonInhibition() {
		return idRaisonInhibition;
	}

	/**
	 * @param idRaisonInhibition the idRaisonInhibition to set
	 */
	public void setIdRaisonInhibition(int idRaisonInhibition) {
		this.idRaisonInhibition = idRaisonInhibition;
	}

	/**
	 * @return the raisonInhibition
	 */
	public String getRaisonInhibition() {
		return raisonInhibition;
	}

	/**
	 * @param raisonInhibition the raisonInhibition to set
	 */
	public void setRaisonInhibition(String raisonInhibition) {
		this.raisonInhibition = raisonInhibition;
	}
	
}
