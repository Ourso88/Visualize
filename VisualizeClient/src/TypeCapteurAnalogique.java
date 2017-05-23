
public class TypeCapteurAnalogique {
	private int idEntreeAnalogique;
	private int idCapteur;
	private int idEquipement;
	private String nomCapteur;
	private String nomEquipement;
	private String descriptionCapteur;
	private String posteTechnique;
	private String etatAlarme;
	private int voieApi;
	private String SeuilBas;
	private String seuilHaut;
	private String tempo;
	private String calibration;
	
	
	private String valeur;
	
	
	public TypeCapteurAnalogique(int idEntreeAnalogique, int idCapteur, int idEquipement, String nomCapteur, String nomEquipement,
			String descriptionCapteur, String posteTechnique, String etatAlarme, int voieApi,
			String seuilBas, String seuilHaut, String tempo, String calibration) {
		super();
		
		this.setIdEntreeAnalogique(idEntreeAnalogique);
		this.setIdCapteur(idCapteur);
		this.setIdEquipement(idEquipement);
		this.setNomCapteur(nomCapteur);
		this.setNomEquipement(nomEquipement);
		this.setDescriptionCapteur(descriptionCapteur);
		this.setPosteTechnique(posteTechnique);
		this.setEtatAlarme(etatAlarme);
		this.setVoieApi(voieApi);
		this.setSeuilBas(seuilBas);
		this.setSeuilHaut(seuilHaut);
		this.setTempo(tempo);
		this.setCalibration(calibration);
	}


	/**
	 * @return the idEntreeAnalogique
	 */
	public int getIdEntreeAnalogique() {
		return idEntreeAnalogique;
	}


	/**
	 * @param idEntreeAnalogique the idEntreeAnalogique to set
	 */
	public void setIdEntreeAnalogique(int idEntreeAnalogique) {
		this.idEntreeAnalogique = idEntreeAnalogique;
	}


	/**
	 * @return the idCapteur
	 */
	public int getIdCapteur() {
		return idCapteur;
	}


	/**
	 * @param idCapteur the idCapteur to set
	 */
	public void setIdCapteur(int idCapteur) {
		this.idCapteur = idCapteur;
	}


	/**
	 * @return the idEquipement
	 */
	public int getIdEquipement() {
		return idEquipement;
	}


	/**
	 * @param idEquipement the idEquipement to set
	 */
	public void setIdEquipement(int idEquipement) {
		this.idEquipement = idEquipement;
	}


	/**
	 * @return the nomCapteur
	 */
	public String getNomCapteur() {
		return nomCapteur;
	}


	/**
	 * @param nomCapteur the nomCapteur to set
	 */
	public void setNomCapteur(String nomCapteur) {
		this.nomCapteur = nomCapteur;
	}


	/**
	 * @return the nomEquipement
	 */
	public String getNomEquipement() {
		return nomEquipement;
	}


	/**
	 * @param nomEquipement the nomEquipement to set
	 */
	public void setNomEquipement(String nomEquipement) {
		this.nomEquipement = nomEquipement;
	}


	/**
	 * @return the descriptionCapteur
	 */
	public String getDescriptionCapteur() {
		return descriptionCapteur;
	}


	/**
	 * @param descriptionCapteur the descriptionCapteur to set
	 */
	public void setDescriptionCapteur(String descriptionCapteur) {
		this.descriptionCapteur = descriptionCapteur;
	}


	/**
	 * @return the posteTechnique
	 */
	public String getPosteTechnique() {
		return posteTechnique;
	}


	/**
	 * @param posteTechnique the posteTechnique to set
	 */
	public void setPosteTechnique(String posteTechnique) {
		this.posteTechnique = posteTechnique;
	}


	/**
	 * @return the etatAlarme
	 */
	public String getEtatAlarme() {
		return etatAlarme;
	}


	/**
	 * @param etatAlarme the etatAlarme to set
	 */
	public void setEtatAlarme(String etatAlarme) {
		this.etatAlarme = etatAlarme;
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
	 * @return the valeur
	 */
	public String getValeur() {
		return valeur;
	}


	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}


	/**
	 * @return the seuilBas
	 */
	public String getSeuilBas() {
		return SeuilBas;
	}


	/**
	 * @param seuilBas the seuilBas to set
	 */
	public void setSeuilBas(String seuilBas) {
		SeuilBas = seuilBas;
	}


	/**
	 * @return the seuilHaut
	 */
	public String getSeuilHaut() {
		return seuilHaut;
	}


	/**
	 * @param seuilHaut the seuilHaut to set
	 */
	public void setSeuilHaut(String seuilHaut) {
		this.seuilHaut = seuilHaut;
	}


	/**
	 * @return the tempo
	 */
	public String getTempo() {
		return tempo;
	}


	/**
	 * @param tempo the tempo to set
	 */
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}


	/**
	 * @return the calibration
	 */
	public String getCalibration() {
		return calibration;
	}


	/**
	 * @param calibration the calibration to set
	 */
	public void setCalibration(String calibration) {
		this.calibration = calibration;
	}


} // fin class
