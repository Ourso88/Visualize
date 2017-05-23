
public class TypeZoneMaintenance {
	private String voie;
	private String description;
	private String raisonMaintenance;
	
	public TypeZoneMaintenance(String voie, String description, String raisonMaintance) {
		super();
		
		this.setVoie(voie);
		this.setDescription(description);
		this.setRaisonMaintenance(raisonMaintance);
	}

	/**
	 * @return the voie
	 */
	public String getVoie() {
		return voie;
	}

	/**
	 * @param voie the voie to set
	 */
	public void setVoie(String voie) {
		this.voie = voie;
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
} // Fin class
