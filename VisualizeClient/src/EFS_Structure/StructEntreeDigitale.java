package EFS_Structure;

public class StructEntreeDigitale {
	private long id;
	private String nom;
	private String description;
	private long tempo;
	private int noNf;
	private int alarme;
	private long idService;
	private long idZoneSubstitution;
	private long idCapteur;
	private long idEquipement;
	private long idPosteTechnique;
	private long idTypeMateriel;
	private int voieApi;
	private String contact;

	public StructEntreeDigitale(long id, String nom, String description, long tempo, int noNf, int alarme, long idService,
			long idZoneSubstitution, long idCapteur, long idEquipement, long idPosteTechnique, long idTypeMateriel,
			int voieApi, String contact) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setDescription(description);
		this.setTempo(tempo);
		this.setNoNf(noNf);
		this.setAlarme(alarme);
		this.setIdService(idService);
		this.setIdZoneSubstitution(idZoneSubstitution);
		this.setIdCapteur(idCapteur);
		this.setIdEquipement(idEquipement);
		this.setIdPosteTechnique(idPosteTechnique);
		this.setIdTypeMateriel(idTypeMateriel);
		this.setVoieApi(voieApi);
		this.setContact(contact);
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
	 * @return the tempo
	 */
	public long getTempo() {
		return tempo;
	}


	/**
	 * @param tempo the tempo to set
	 */
	public void setTempo(long tempo) {
		this.tempo = tempo;
	}


	/**
	 * @return the noNf
	 */
	public int getNoNf() {
		return noNf;
	}


	/**
	 * @param noNf the noNf to set
	 */
	public void setNoNf(int noNf) {
		this.noNf = noNf;
	}


	/**
	 * @return the alarme
	 */
	public int getAlarme() {
		return alarme;
	}


	/**
	 * @param alarme the alarme to set
	 */
	public void setAlarme(int alarme) {
		this.alarme = alarme;
	}


	/**
	 * @return the idZoneSubstitution
	 */
	public long getIdZoneSubstitution() {
		return idZoneSubstitution;
	}


	/**
	 * @param idZoneSubstitution the idZoneSubstitution to set
	 */
	public void setIdZoneSubstitution(long idZoneSubstitution) {
		this.idZoneSubstitution = idZoneSubstitution;
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
	 * @return the idEquipement
	 */
	public long getIdEquipement() {
		return idEquipement;
	}


	/**
	 * @param idEquipement the idEquipement to set
	 */
	public void setIdEquipement(long idEquipement) {
		this.idEquipement = idEquipement;
	}


	/**
	 * @return the idPosteTechnique
	 */
	public long getIdPosteTechnique() {
		return idPosteTechnique;
	}


	/**
	 * @param idPosteTechnique the idPosteTechnique to set
	 */
	public void setIdPosteTechnique(long idPosteTechnique) {
		this.idPosteTechnique = idPosteTechnique;
	}


	/**
	 * @return the idTypeMateriel
	 */
	public long getIdTypeMateriel() {
		return idTypeMateriel;
	}


	/**
	 * @param idTypeMateriel the idTypeMateriel to set
	 */
	public void setIdTypeMateriel(long idTypeMateriel) {
		this.idTypeMateriel = idTypeMateriel;
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
	 * @return the idService
	 */
	public long getIdService() {
		return idService;
	}


	/**
	 * @param idService the idService to set
	 */
	public void setIdService(long idService) {
		this.idService = idService;
	}


	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}


	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
} // Fin class
