package kernel;

/**
 * Definition Prise en compte
 * @author Eric Mariani
 * @since 22/02/2017
 *
 */
public class PriseEnCompte {
	long idPriseEncompte;
	String nom;
	String description;
	
	/**
	 * Constructeur
	 */
	public PriseEnCompte(long idPriseEncompte, String nom, String description) {
		this.setIdPriseEncompte(idPriseEncompte);
		this.setNom(nom);
		this.setDescription(description);
	}

	/**
	 * @return the idPriseEncompte
	 */
	public long getIdPriseEncompte() {
		return idPriseEncompte;
	}

	/**
	 * @param idPriseEncompte the idPriseEncompte to set
	 */
	public void setIdPriseEncompte(long idPriseEncompte) {
		this.idPriseEncompte = idPriseEncompte;
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
	
	
}
