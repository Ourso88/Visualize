package EFS_Structure;

public class StructTypeJournal {
	private int numeroTypeJournal;
	private String description;
	
	public StructTypeJournal(int numeroTypeJournal, String description) {
		this.setNumeroTypeJournal(numeroTypeJournal);
		this.setDescription(description);
	}
	
	/**
	 * @return the numeroTypeJournal
	 */
	public int getNumeroTypeJournal() {
		return numeroTypeJournal;
	}
	/**
	 * @param numeroTypeJournal the numeroTypeJournal to set
	 */
	public void setNumeroTypeJournal(int numeroTypeJournal) {
		this.numeroTypeJournal = numeroTypeJournal;
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
}
