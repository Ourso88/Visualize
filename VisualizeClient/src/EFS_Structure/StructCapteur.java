package EFS_Structure;

public class StructCapteur {
	private long id;
	private String nom;
	private String description;
	private long idEquipement;
	private long idPosteTechnique;
	private long idTypeMateriel;
	private long idZoneSubstitution;
	private int typeCapteur;
	private int alarme;
	private long idService;
	private int voieApi;
	private int inhibition;
	
	
	public StructCapteur(long id, String nom, String description, long idEquipement, long idPosteTechnique, long idTypeMateriel,
			long idZoneSubstitution, int typeCapteur, int alarme, long idService, int voieApi, int inhibition) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setDescription(description);
		this.setIdEquipement(idEquipement);
		this.setIdPosteTechnique(idPosteTechnique);
		this.setIdTypeMateriel(idTypeMateriel);
		this.setIdZoneSubstitution(idZoneSubstitution);
		this.setTypeCapteur(typeCapteur);
		this.setIdService(idService);
		this.setAlarme(alarme);
		this.setVoieApi(voieApi);
		this.setInhibition(inhibition);
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
	 * @return the inhibition
	 */
	public int getInhibition() {
		return inhibition;
	}


	/**
	 * @param inhibition the inhibition to set
	 */
	public void setInhibition(int inhibition) {
		this.inhibition = inhibition;
	}

}
