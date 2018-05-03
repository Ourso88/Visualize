package kernel;

/**
 * Definition des capteurs (voies API)
 * @author Eric Mariani
 * @since 15/02/2017
 *
 */
public class Capteur {
	long idCapteur;
	String nom;
	String description;
	long idEquipement;
	long idPosteTechnique;
	long idTypeMateriel;
	long idZoneSubstitution;
	int typeCapteur;
	int alarme;
	long idService;
	int voieApi;
	int inhibition;
	long idUnite;
	String contact;
	String inventaire;
	long idAlarmeService;
	String nomService;
	int indexMotApi;
	boolean klaxon;

	/**
	 * Constructeur vide
	 */
	public Capteur() {
		
	}
	
	
	/**
	 * @return the inventaire
	 */
	public String getInventaire() {
		return inventaire;
	}

	/**
	 * @param inventaire the inventaire to set
	 */
	public void setInventaire(String inventaire) {
		this.inventaire = inventaire;
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
	/**
	 * @return the idUnite
	 */
	public long getIdUnite() {
		return idUnite;
	}
	/**
	 * @param idUnite the idUnite to set
	 */
	public void setIdUnite(long idUnite) {
		this.idUnite = idUnite;
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


	/**
	 * @return the idAlarmeService
	 */
	public long getIdAlarmeService() {
		return idAlarmeService;
	}


	/**
	 * @param idAlarmeService the idAlarmeService to set
	 */
	public void setIdAlarmeService(long idAlarmeService) {
		this.idAlarmeService = idAlarmeService;
	}


	/**
	 * @return the nomService
	 */
	public String getNomService() {
		return nomService;
	}


	/**
	 * @param nomService the nomService to set
	 */
	public void setNomService(String nomService) {
		this.nomService = nomService;
	}


	/**
	 * @return the indexMotApi
	 */
	public int getIndexMotApi() {
		return indexMotApi;
	}


	/**
	 * @param indexMotApi the indexMotApi to set
	 */
	public void setIndexMotApi(int indexMotApi) {
		this.indexMotApi = indexMotApi;
	}


	/**
	 * @return the klaxon
	 */
	public boolean isKlaxon() {
		return klaxon;
	}


	/**
	 * @param klaxon the klaxon to set
	 */
	public void setKlaxon(boolean klaxon) {
		this.klaxon = klaxon;
	}

	
}
