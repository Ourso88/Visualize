
public class TypeHistoriqueCalibration {
	private String voie;
	private String dateCalibration;
	private String utilisateur;
	private double ancValeur;
	private double newValeur;
	
	public TypeHistoriqueCalibration(String voie, String dateCalibration, String utilisateur,
			double ancValeur, double newValeur) {
		super();
		
		this.setVoie(voie);
		this.setDateCalibration(dateCalibration);
		this.setUtilisateur(utilisateur);
		this.setAncValeur(ancValeur);
		this.setNewValeur(newValeur);
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
	 * @return the dateCalibration
	 */
	public String getDateCalibration() {
		return dateCalibration;
	}

	/**
	 * @param dateCalibration the dateCalibration to set
	 */
	public void setDateCalibration(String dateCalibration) {
		this.dateCalibration = dateCalibration;
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
	 * @return the ancValeur
	 */
	public double getAncValeur() {
		return ancValeur;
	}

	/**
	 * @param ancValeur the ancValeur to set
	 */
	public void setAncValeur(double ancValeur) {
		this.ancValeur = ancValeur;
	}

	/**
	 * @return the newValeur
	 */
	public double getNewValeur() {
		return newValeur;
	}

	/**
	 * @param newValeur the newValeur to set
	 */
	public void setNewValeur(double newValeur) {
		this.newValeur = newValeur;
	}

}
