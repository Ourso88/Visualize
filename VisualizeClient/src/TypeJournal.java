
public class TypeJournal {
	private String dateJournal;
	private String typeJournal;
	private String utilisateur;
	private String voie;
	private String descriptionVoie;
	private String action;
	
	public TypeJournal(String dateJournal, String typeJournal, String utilisateur, 
			String voie, String descriptionVoie, String Action) {
		super();
		
		this.setDateJournal(dateJournal);
		this.setTypeJournal(typeJournal);
		this.setUtilisateur(utilisateur);
		this.setVoie(voie);
		this.setDescriptionVoie(descriptionVoie);
		this.setAction(Action);
	}

	/**
	 * @return the dateJournal
	 */
	public String getDateJournal() {
		return dateJournal;
	}

	/**
	 * @param dateJournal the dateJournal to set
	 */
	public void setDateJournal(String dateJournal) {
		this.dateJournal = dateJournal;
	}

	/**
	 * @return the typeJournal
	 */
	public String getTypeJournal() {
		return typeJournal;
	}

	/**
	 * @param typeJournal the typeJournal to set
	 */
	public void setTypeJournal(String typeJournal) {
		this.typeJournal = typeJournal;
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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the descriptionVoie
	 */
	public String getDescriptionVoie() {
		return descriptionVoie;
	}

	/**
	 * @param descriptionVoie the descriptionVoie to set
	 */
	public void setDescriptionVoie(String descriptionVoie) {
		this.descriptionVoie = descriptionVoie;
	}
}
