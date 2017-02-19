package kernel;


import java.time.LocalDateTime;

import em.general.EFS_General;

/**
 * Gere les capteurs analogiques entrant (Entr�es API)
 * @author Eric Mariani
 * @since 15/02/2017
 */
public class AnalogicInput extends Capteur implements EFS_General {
	private long idEntreeAnalogique;
	private int seuilHaut;
	private int preSeuilHaut;
	private int seuilBas;
	private int preSeuilBas;
	private int calibration;
	private long seuilTempo;
	private long preSeuilTempo;
	private String unite;
	private int valeurConsigne;
	
	private double valeurAPI;
	
	private boolean seuilHautAtteint;
	private boolean preSeuilHautAtteint;
	private boolean seuilBasAtteint;
	private boolean preSeuilBasAtteint;
	
	private String seuilAtteint;
	private boolean alarmeEnclenchee;
	private boolean preAlarmeEnclenchee;
	
	private LocalDateTime dateAlarmeApparition;
	private boolean alarmeTempoEcoulee;
	
	/**
	 * Constructeur vide
	 */
	public AnalogicInput() {
		
	}
	
	/**
	 * Constructeur complet � partir de la table Capteur et de la table CapteurAnalogique
	 * @param idCapteur
	 * @param nom
	 * @param description
	 * @param idEquipement
	 * @param idPosteTechnique
	 * @param idTypeMateriel
	 * @param idZoneSubstitution
	 * @param typeCapteur
	 * @param alarme
	 * @param idService
	 * @param voieApi
	 * @param inhibition
	 * @param idUnite
	 * @param contact
	 * @param idEntreeAnalogique
	 * @param seuilHaut
	 * @param preSeuilHaut
	 * @param seuilBas
	 * @param preSeuilBas
	 * @param calibration
	 * @param seuilTempo
	 * @param preSeuilTempo
	 * @param unite
	 * @param valeurConsigne
	 */
	public AnalogicInput(long idCapteur, String nom, String description, long idEquipement, long idPosteTechnique, long idTypeMateriel,	long idZoneSubstitution, 
						 int typeCapteur, int alarme, long idService, int voieApi, int inhibition, long idUnite, String contact, long idEntreeAnalogique,
						 int seuilHaut, int preSeuilHaut, int seuilBas,	int preSeuilBas, int calibration, long seuilTempo, long preSeuilTempo, String unite, int valeurConsigne) {
		this.setIdCapteur(idCapteur);
		this.setNom(nom);
		this.setDescription(description);
		this.setIdEquipement(idEquipement);
		this.setIdPosteTechnique(idPosteTechnique);
		this.setIdTypeMateriel(idTypeMateriel);
		this.setIdZoneSubstitution(idZoneSubstitution);
		this.setTypeCapteur(typeCapteur);
		this.setAlarme(alarme);
		this.setIdService(idService);
		this.setVoieApi(voieApi);
		this.setInhibition(inhibition);
		this.setIdUnite(idUnite);
		this.setContact(contact);
		this.setIdEntreeAnalogique(idEntreeAnalogique);
		this.setSeuilHaut(seuilHaut);
		this.setPreSeuilHaut(preSeuilHaut);
		this.setSeuilBas(seuilBas);
		this.setPreSeuilBas(preSeuilBas);
		this.setCalibration(calibration);
		this.setSeuilTempo(seuilTempo);
		this.setPreSeuilTempo(preSeuilTempo);
		this.setUnite(unite);
		this.setValeurConsigne(valeurConsigne);
	}
	

	/**
	 * @return valeur
	 */
	public double getValeurAPI() {
		return valeurAPI;
	}
	
	/**
	 * @param valeur to set
	 */
	public void setValeurAPI(double valeurAPI) {
		this.valeurAPI = valeurAPI;
		
		if(valeurAPI < seuilBas) {
			setSeuilBasAtteint(true);
			setPreSeuilBasAtteint(false);
			setSeuilHautAtteint(false);
			setPreSeuilHautAtteint(false);
			setSeuilAtteint("BAS");
		} else if(valeurAPI < preSeuilBas) {
			setSeuilBasAtteint(false);
			setPreSeuilBasAtteint(true);
			setSeuilHautAtteint(false);
			setPreSeuilHautAtteint(false);
			setSeuilAtteint("PRE - BAS");
		} else if(valeurAPI > seuilHaut) {
			setSeuilBasAtteint(false);
			setPreSeuilBasAtteint(false);
			setSeuilHautAtteint(true);
			setPreSeuilHautAtteint(false);
			setSeuilAtteint("HAUT");
		} else if(valeurAPI > preSeuilHaut) {
			setSeuilBasAtteint(false);
			setPreSeuilBasAtteint(false);
			setSeuilHautAtteint(false);
			setPreSeuilHautAtteint(true);
			setSeuilAtteint("PRE - HAUT");
		} else {
			setSeuilBasAtteint(false);
			setPreSeuilBasAtteint(false);
			setSeuilHautAtteint(false);
			setPreSeuilHautAtteint(false);
			setSeuilAtteint("AUCUN");
		}
	}
	
	/**
	 * @return the idEntreeAnalogique
	 */
	public long getIdEntreeAnalogique() {
		return idEntreeAnalogique;
	}

	/**
	 * @param idEntreeAnalogique the idEntreeAnalogique to set
	 */
	public void setIdEntreeAnalogique(long idEntreeAnalogique) {
		this.idEntreeAnalogique = idEntreeAnalogique;
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
	 * @return the seuilHaut
	 */
	public int getSeuilHaut() {
		return seuilHaut;
	}

	/**
	 * @param seuilHaut the seuilHaut to set
	 */
	public void setSeuilHaut(int seuilHaut) {
		this.seuilHaut = seuilHaut;
	}

	/**
	 * @return the preSeuilHaut
	 */
	public int getPreSeuilHaut() {
		return preSeuilHaut;
	}

	/**
	 * @param preSeuilHaut the preSeuilHaut to set
	 */
	public void setPreSeuilHaut(int preSeuilHaut) {
		this.preSeuilHaut = preSeuilHaut;
	}

	/**
	 * @return the seuilBas
	 */
	public int getSeuilBas() {
		return seuilBas;
	}

	/**
	 * @param seuilBas the seuilBas to set
	 */
	public void setSeuilBas(int seuilBas) {
		this.seuilBas = seuilBas;
	}

	/**
	 * @return the preSeuilBas
	 */
	public int getPreSeuilBas() {
		return preSeuilBas;
	}

	/**
	 * @param preSeuilBas the preSeuilBas to set
	 */
	public void setPreSeuilBas(int preSeuilBas) {
		this.preSeuilBas = preSeuilBas;
	}

	/**
	 * @return the calibration
	 */
	public int getCalibration() {
		return calibration;
	}

	/**
	 * @param calibration the calibration to set
	 */
	public void setCalibration(int calibration) {
		this.calibration = calibration;
	}

	/**
	 * @return the seuilTempo
	 */
	public long getSeuilTempo() {
		return seuilTempo;
	}

	/**
	 * @param seuilTempo the seuilTempo to set
	 */
	public void setSeuilTempo(long seuilTempo) {
		this.seuilTempo = seuilTempo;
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
	 * @return the unite
	 */
	public String getUnite() {
		return unite;
	}

	/**
	 * @param unite the unite to set
	 */
	public void setUnite(String unite) {
		this.unite = unite;
	}

	/**
	 * @return the valeurConsigne
	 */
	public int getValeurConsigne() {
		return valeurConsigne;
	}

	/**
	 * @param valeurConsigne the valeurConsigne to set
	 */
	public void setValeurConsigne(int valeurConsigne) {
		this.valeurConsigne = valeurConsigne;
	}

	/**
	 * @return the seuilHautAtteint
	 */
	public boolean isSeuilHautAtteint() {
		return seuilHautAtteint;
	}

	/**
	 * @param seuilHautAtteint the seuilHautAtteint to set
	 */
	public void setSeuilHautAtteint(boolean seuilHautAtteint) {
		this.seuilHautAtteint = seuilHautAtteint;
		if(seuilHautAtteint) {
			if(((this.getAlarme() == ALARME_ALERT) || (this.getAlarme() == ALARME_DEFAUT)) && (this.getInhibition() == 0)) {
				this.setAlarmeEnclenchee(true);
			}
		} else if(!this.seuilBasAtteint) {
			this.setAlarmeEnclenchee(false);
		}
	}

	/**
	 * @return the preSeuilHautAtteint
	 */
	public boolean isPreSeuilHautAtteint() {
		return preSeuilHautAtteint;
	}

	/**
	 * @param preSeuilHautAtteint the preSeuilHautAtteint to set
	 */
	public void setPreSeuilHautAtteint(boolean preSeuilHautAtteint) {
		this.preSeuilHautAtteint = preSeuilHautAtteint;
		if(preSeuilHautAtteint) {
			if(this.getAlarme() == ALARME_ALERT && (this.getInhibition() == 0)) {
				this.setPreAlarmeEnclenchee(true);
			}
		} else if(!this.preSeuilBasAtteint) {
			this.setPreAlarmeEnclenchee(false);
		}
	}

	/**
	 * @return the seuilBasAtteint
	 */
	public boolean isSeuilBasAtteint() {
		return seuilBasAtteint;
	}

	/**
	 * @param seuilBasAtteint the seuilBasAtteint to set
	 */
	public void setSeuilBasAtteint(boolean seuilBasAtteint) {
		this.seuilBasAtteint = seuilBasAtteint;
		if(seuilBasAtteint) {
			if(((this.getAlarme() == ALARME_ALERT) || (this.getAlarme() == ALARME_DEFAUT)) && (this.getInhibition() == 0)) {
				this.setAlarmeEnclenchee(true);
			}
		} else if(!this.seuilHautAtteint) {
			this.setAlarmeEnclenchee(false);
		}
	}

	/**
	 * @return the preSeuilBasAtteint
	 */
	public boolean isPreSeuilBasAtteint() {
		return preSeuilBasAtteint;
	}

	/**
	 * @param preSeuilBasAtteint the preSeuilBasAtteint to set
	 */
	public void setPreSeuilBasAtteint(boolean preSeuilBasAtteint) {
		this.preSeuilBasAtteint = preSeuilBasAtteint;
		if(preSeuilBasAtteint) {
			if(this.getAlarme() == ALARME_ALERT && (this.getInhibition() == 0)) {
				this.setPreAlarmeEnclenchee(true);
			}
		} else if(!this.preSeuilHautAtteint) {
			this.setPreAlarmeEnclenchee(false);
		}
	}

	/**
	 * @return the seuilAtteint
	 */
	public String getSeuilAtteint() {
		return seuilAtteint;
	}

	/**
	 * @param seuilAtteint the seuilAtteint to set
	 */
	public void setSeuilAtteint(String seuilAtteint) {
		this.seuilAtteint = seuilAtteint;
	}

	/**
	 * @return the alarmeEnclenchee
	 */
	public boolean isAlarmeEnclenchee() {
		return alarmeEnclenchee;
	}

	/**
	 * @param alarmeEnclenchee the alarmeEnclenchee to set
	 */
	public void setAlarmeEnclenchee(boolean alarmeEnclenchee) {
		if(!this.alarmeEnclenchee && alarmeEnclenchee) {
			this.setDateAlarmeApparition(LocalDateTime.now());
		}
		// Calcul si tempo est pass�
		if(alarmeEnclenchee) {
			if(this.getDateAlarmeApparition().plusMinutes(this.getSeuilTempo()).isBefore(LocalDateTime.now())) {
				this.setAlarmeTempoEcoulee(true);
			} else {
				this.setAlarmeTempoEcoulee(false);
			}
		}
		this.alarmeEnclenchee = alarmeEnclenchee;
	}

	/**
	 * @return the preAlarmeEnclenchee
	 */
	public boolean isPreAlarmeEnclenchee() {
		return preAlarmeEnclenchee;
	}

	/**
	 * @param preAlarmeEnclenchee the preAlarmeEnclenchee to set
	 */
	public void setPreAlarmeEnclenchee(boolean preAlarmeEnclenchee) {
		this.preAlarmeEnclenchee = preAlarmeEnclenchee;
	}

	/**
	 * @return the dateAlarmeApparition
	 */
	public LocalDateTime getDateAlarmeApparition() {
		return dateAlarmeApparition;
	}

	/**
	 * @param dateAlarmeApparition the dateAlarmeApparition to set
	 */
	public void setDateAlarmeApparition(LocalDateTime dateAlarmeApparition) {
		this.dateAlarmeApparition = dateAlarmeApparition;
	}

	/**
	 * @return the alarmeTempoEcoulee
	 */
	public boolean isAlarmeTempoEcoulee() {
		return alarmeTempoEcoulee;
	}

	/**
	 * @param alarmeTempoEcoulee the alarmeTempoEcoulee to set
	 */
	public void setAlarmeTempoEcoulee(boolean alarmeTempoEcoulee) {
		this.alarmeTempoEcoulee = alarmeTempoEcoulee;
	}
	
}
