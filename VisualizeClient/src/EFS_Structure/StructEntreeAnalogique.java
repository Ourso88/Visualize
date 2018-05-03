package EFS_Structure;

public class StructEntreeAnalogique {
	private long id;
	private String nom;
	private String description;
	private long seuilBas;
	private long seuilHaut;
	private long seuilTempo;
	private long preSeuilBas;
	private long preSeuilHaut;
	private long preSeuilTempo;
	private long idService;
	private long idZoneSubstitution;
	private int alarme;
	private double calibration;
	private long idCapteur;
	private long idEquipement;
	private long idPosteTechnique;
	private long idTypeMateriel;
	private long idUnite;
	private int voieApi;
	private long valeurConsigne;
	private String contact;
	private long idAlarmeService;
	
	public StructEntreeAnalogique(long id, String nom, String description, long seuilBas, 
			long seuilHaut, long seuilTempo, long preSeuilBas, long preSeuilHaut, long preSeuilTempo, 
			long idService, long idZoneSubstitution, int alarme, double calibration, long idCapteur,
			long idEquipement, long idPosteTechnique, long idTypeMateriel, long idUnite, int voieApi,
			long valeurConsigne, String contact, long idAlarmeService) {
		super();
		this.setId(id);
		this.setNom(nom);
		this.setDescription(description);
		this.setSeuilBas(seuilBas);
		this.setSeuilHaut(seuilHaut);
		this.setSeuilTempo(seuilTempo);
		this.setPreSeuilBas(preSeuilBas);
		this.setPreSeuilHaut(preSeuilHaut);
		this.setPreSeuilTempo(preSeuilTempo);
		this.setIdService(idService);
		this.setIdZoneSubstitution(idZoneSubstitution);
		this.setAlarme(alarme);
		this.setCalibration(calibration);
		this.setIdCapteur(idCapteur);
		this.setIdEquipement(idEquipement);
		this.setIdPosteTechnique(idPosteTechnique);
		this.setIdTypeMateriel(idTypeMateriel);
		this.setIdUnite(idUnite);
		this.setVoieApi(voieApi);
		this.setValeurConsigne(valeurConsigne);
		this.setContact(contact);
		this.setIdAlarmeService(idAlarmeService);
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
	 * @return the seuilBas
	 */
	public long getSeuilBas() {
		return seuilBas;
	}


	/**
	 * @param seuilBas the seuilBas to set
	 */
	public void setSeuilBas(long seuilBas) {
		this.seuilBas = seuilBas;
	}


	/**
	 * @return the seuilHaut
	 */
	public long getSeuilHaut() {
		return seuilHaut;
	}


	/**
	 * @param seuilHaut the seuilHaut to set
	 */
	public void setSeuilHaut(long seuilHaut) {
		this.seuilHaut = seuilHaut;
	}


	/**
	 * @return the tempo
	 */
	public long getSeuilTempo() {
		return seuilTempo;
	}


	/**
	 * @param tempo the tempo to set
	 */
	public void setSeuilTempo(long seuilTempo) {
		this.seuilTempo = seuilTempo;
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
	 * @return the preSeuilBas
	 */
	public long getPreSeuilBas() {
		return preSeuilBas;
	}


	/**
	 * @param preSeuilBas the preSeuilBas to set
	 */
	public void setPreSeuilBas(long preSeuilBas) {
		this.preSeuilBas = preSeuilBas;
	}


	/**
	 * @return the preSeuilHaut
	 */
	public long getPreSeuilHaut() {
		return preSeuilHaut;
	}


	/**
	 * @param preSeuilHaut the preSeuilHaut to set
	 */
	public void setPreSeuilHaut(long preSeuilHaut) {
		this.preSeuilHaut = preSeuilHaut;
	}


	/**
	 * @return the preSeuilTempo
	 */
	public long getPreSeuilTempo() {
		return preSeuilTempo;
	}


	/**
	 * @param preSeuilTempo the preSeuilTempo to set
	 */
	public void setPreSeuilTempo(long preSeuilTempo) {
		this.preSeuilTempo = preSeuilTempo;
	}


	/**
	 * @return the calibration
	 */
	public double getCalibration() {
		return calibration;
	}


	/**
	 * @param calibration the calibration to set
	 */
	public void setCalibration(double calibration) {
		this.calibration = calibration;
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
	 * @return the valeurConsigne
	 */
	public long getValeurConsigne() {
		return valeurConsigne;
	}


	/**
	 * @param valeurConsigne the valeurConsigne to set
	 */
	public void setValeurConsigne(long valeurConsigne) {
		this.valeurConsigne = valeurConsigne;
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
} // fin class
