package EFS_Structure;

import java.util.Date;

public class StructValidationHistoriqueAlarme {
	private Date dateValidation;
	private Date dateDebut;
	private Date dateFin;
	private String utilisateur;
	
	public StructValidationHistoriqueAlarme(Date dateValidation, Date dateDebut, Date dateFin, String utilisateur) {
		super();
		
		this.setDateValidation(dateValidation);
		this.setDateDebut(dateDebut);
		this.setDateFin(dateFin);
		this.setUtilisateur(utilisateur);
	}

	/**
	 * @return the dateValidation
	 */
	public Date getDateValidation() {
		return dateValidation;
	}

	/**
	 * @param dateValidation the dateValidation to set
	 */
	public void setDateValidation(Date dateValidation) {
		this.dateValidation = dateValidation;
	}

	/**
	 * @return the dateDebut
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut the dateDebut to set
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return the dateFin
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return the utilisateur
	 */
	public String getUtilisateur() {
		return utilisateur;
	}

	/**
	 * @param Utilisateur the utilisateur to set
	 */
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
	
}
