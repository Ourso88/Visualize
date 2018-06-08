package kernel;

/**
 * Gere les utilisateurs
 * @author Eric Mariani
 * @since 07/06/2018
 *
 */
public class GestionUtilisateur {
	private int idUtilisateur;
	private int niveauUtilisateur;
	private String nomUtilisateur;
	private String prenomUtilisateur;
	private String loginUtilisateur;
	private int idNiveauUtilisateur;
	private int idAlarmeService;
	private String nomAlarmeService;
	private String descriptionAlarmeService;
	
	/**
	 * Constructeur
	 */
	public GestionUtilisateur(int idUtilisateur, int niveauUtilisateur,	String nomUtilisateur, String prenomUtilisateur, String loginUtilisateur, int idNiveauUtilisateur,
							  int idAlarmeService, String nomAlarmeService,	String descriptionAlarmeService) {
		super();
		this.setIdUtilisateur(idUtilisateur);
		this.setNiveauUtilisateur(idNiveauUtilisateur);
		this.setNomUtilisateur(nomUtilisateur);
		this.setPrenomUtilisateur(prenomUtilisateur);
		this.setLoginUtilisateur(loginUtilisateur);
		this.setIdNiveauUtilisateur(idNiveauUtilisateur);
		this.setIdAlarmeService(idAlarmeService);
		this.setNomAlarmeService(nomAlarmeService);
		this.setDescriptionAlarmeService(descriptionAlarmeService);
	}

	/**
	 * Constructeur vide
	 */
	public GestionUtilisateur() {
		super();
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
	 * @return the niveauUtilisateur
	 */
	public int getNiveauUtilisateur() {
		return niveauUtilisateur;
	}

	/**
	 * @param niveauUtilisateur the niveauUtilisateur to set
	 */
	public void setNiveauUtilisateur(int niveauUtilisateur) {
		this.niveauUtilisateur = niveauUtilisateur;
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
	 * @return the prenomUtilisateur
	 */
	public String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	/**
	 * @param prenomUtilisateur the prenomUtilisateur to set
	 */
	public void setPrenomUtilisateur(String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}

	/**
	 * @return the loginUtilisateur
	 */
	public String getLoginUtilisateur() {
		return loginUtilisateur;
	}

	/**
	 * @param loginUtilisateur the loginUtilisateur to set
	 */
	public void setLoginUtilisateur(String loginUtilisateur) {
		this.loginUtilisateur = loginUtilisateur;
	}

	/**
	 * @return the idNiveauUtilisateur
	 */
	public int getIdNiveauUtilisateur() {
		return idNiveauUtilisateur;
	}

	/**
	 * @param idNiveauUtilisateur the idNiveauUtilisateur to set
	 */
	public void setIdNiveauUtilisateur(int idNiveauUtilisateur) {
		this.idNiveauUtilisateur = idNiveauUtilisateur;
	}

	/**
	 * @return the idService
	 */
	public int getIdAlarmeService() {
		return idAlarmeService;
	}

	/**
	 * @param idService the idService to set
	 */
	public void setIdAlarmeService(int idAlarmeService) {
		this.idAlarmeService = idAlarmeService;
	}

	/**
	 * @return the nomService
	 */
	public String getNomAlarmeService() {
		return nomAlarmeService;
	}

	/**
	 * @param nomService the nomService to set
	 */
	public void setNomAlarmeService(String nomAlarmeService) {
		this.nomAlarmeService = nomAlarmeService;
	}

	/**
	 * @return the descriptionService
	 */
	public String getDescriptionAlarmeService() {
		return descriptionAlarmeService;
	}

	/**
	 * @param descriptionService the descriptionService to set
	 */
	public void setDescriptionAlarmeService(String descriptionAlarmeService) {
		this.descriptionAlarmeService = descriptionAlarmeService;
	}

}
