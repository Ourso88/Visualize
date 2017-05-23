package EFS_Structure;

public class StructEquipement {
	private long id;
	private String nom;
	private String description;
	private String numeroInventaire;
	private long idTypeMateriel;
	private long idPosteTechnique;
	private long idZoneSubstitution;
	

	public StructEquipement(long id, String nom, String description, String numeroInventaire, long idTypeMateriel, long idPosteTechnique, long idZoneSubstitution) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setDescription(description);
		this.setNumeroInventaire(numeroInventaire);
		this.setIdTypeMateriel(idTypeMateriel);
		this.setIdPosteTechnique(idPosteTechnique);
		this.setIdZoneSubstitution(idZoneSubstitution);
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
	 * @return the numeroInventaire
	 */
	public String getNumeroInventaire() {
		return numeroInventaire;
	}


	/**
	 * @param numeroInventaire the numeroInventaire to set
	 */
	public void setNumeroInventaire(String numeroInventaire) {
		this.numeroInventaire = numeroInventaire;
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
}
