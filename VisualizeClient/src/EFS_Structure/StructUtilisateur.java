package EFS_Structure;

public class StructUtilisateur {
	private long id;
	private String nom;
	private String prenom;
	private String login;
	private String motDePasse;
	private long idNiveauUtilisateur;
	private long idAlarmeService;
	

	public StructUtilisateur(long id, String nom, String prenom, String login, String motDePasse, long idNiveauUtilisateur, long idAlarmeService) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setLogin(login);
		this.setMotDePasse(motDePasse);
		this.setIdNiveauUtilisateur(idNiveauUtilisateur);
		this.setIdAlarmeService(idAlarmeService);
	}
	
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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


	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}


	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}


	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}


	/**
	 * @return the motDePasse
	 */
	public String getMotDePasse() {
		return motDePasse;
	}


	/**
	 * @param motDePasse the motDePasse to set
	 */
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}


	/**
	 * @return the idNiveauUtilisateur
	 */
	public long getIdNiveauUtilisateur() {
		return idNiveauUtilisateur;
	}


	/**
	 * @param idNiveauUtilisateur the idNiveauUtilisateur to set
	 */
	public void setIdNiveauUtilisateur(long idNiveauUtilisateur) {
		this.idNiveauUtilisateur = idNiveauUtilisateur;
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
