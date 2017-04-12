package kernel;

import java.util.ArrayList;
import java.util.List;

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
	public static final List<AlarmeSeuil> tbAlarmeSeuil = new ArrayList<AlarmeSeuil>();
	public static final List<PriseEnCompte> tbPriseEnCompte = new ArrayList<PriseEnCompte>();
	public static final List<AlarmeHistorique> tbAlarmeHistorique = new ArrayList<AlarmeHistorique>();
}
