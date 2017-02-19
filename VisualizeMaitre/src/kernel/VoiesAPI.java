package kernel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import em.fonctions.GestionLogger;
import em.general.AE_Variables;

/**
 * Remplit les tableaux mémoire des voies API
 * @author Eric Mariani
 * @since 16/02/2017
 *
 */
public interface VoiesAPI {
	public static final List<AnalogicInput> tbAnaAPI = new ArrayList<AnalogicInput>();
	public static final List<DigitalInput> tbDigiAPI = new ArrayList<DigitalInput>();
	public static final List<AlarmeEnCours> tbAlarme = new ArrayList<AlarmeEnCours>();
	
	/**
	 * Remplit le tableau d'entrees analogiques
	 */
	public static void lectureAnalogicInput() {
		// Lecture données
		try {
			ResultSet result = AE_Variables.ctnOracle.lectureData("SELECT * FROM (EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur = Capteur.idCapteur)"
					+ " WHERE TypeCapteur = 1 ORDER BY VoieApi");
			while(result.next()) {
				tbAnaAPI.add(new AnalogicInput(
				result.getLong("idCapteur"),
				result.getString("nom"),
				result.getString("description"),
				result.getLong("idEquipement"),
				result.getLong("idPosteTechnique"),
				result.getLong("idTypeMateriel"),
				result.getLong("idZoneSubstitution"),
				result.getInt("typeCapteur"),
				result.getInt("alarme"),
				result.getLong("idService"),
				result.getInt("voieApi"),
				result.getInt("inhibition"),
				result.getLong("idUnite"),
				result.getString("contact"),
				result.getLong("idEntreeAnalogique"),
				result.getInt("seuilHaut"),
				result.getInt("preSeuilHaut"),
				result.getInt("seuilBas"),
				result.getInt("preSeuilBas"),
				result.getInt("calibration"),
				result.getLong("seuilTempo"),
				result.getLong("preSeuilTempo"),
				result.getString("unite"),
				result.getInt("valeurConsigne")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table EntreeAnalogique : " + e.getMessage());
		}
	}

	/**
	 * Remplit le tableau d'entrees digitales
	 */
	public static void lectureDigitalInput() {
		// Lecture données
		try {
			ResultSet result = AE_Variables.ctnOracle.lectureData("SELECT * FROM (EntreeDigitale LEFT JOIN Capteur ON EntreeDigitale.idCapteur = Capteur.idCapteur)"
					+ " WHERE TypeCapteur = 2 ORDER BY VoieApi");
			while(result.next()) {
				tbDigiAPI.add(new DigitalInput(
				result.getLong("idCapteur"),
				result.getString("nom"),
				result.getString("description"),
				result.getLong("idEquipement"),
				result.getLong("idPosteTechnique"),
				result.getLong("idTypeMateriel"),
				result.getLong("idZoneSubstitution"),
				result.getInt("typeCapteur"),
				result.getInt("alarme"),
				result.getLong("idService"),
				result.getInt("voieApi"),
				result.getInt("inhibition"),
				result.getLong("idUnite"),
				result.getString("contact"),
				result.getLong("idEntreeDigitale"),
				result.getInt("tempo"),
				result.getInt("nOnF")
				));
			}
			result.close();
			AE_Variables.ctnOracle.closeLectureData();
		} catch (SQLException e) {
			GestionLogger.gestionLogger.warning("SGBD : Erreur lecture Table EntreeDigitale : " + e.getMessage());
		}
	}	
	
	
}
