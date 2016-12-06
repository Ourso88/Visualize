package em.fonctions;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ViperLogger {
	public static Logger viperLogger;
	private static Handler fhXml;
	private static Handler fhTxt;
	
	
	public static void initialisation(String pClasseAppellante) {
		viperLogger = Logger.getLogger(pClasseAppellante);
		try {
			if(fhXml == null) {
				fhXml = new FileHandler("ViperLogging.xml", true);
				viperLogger.addHandler(fhXml);
			}
			if(fhTxt == null) {
				fhTxt = new FileHandler("ViperLogging.log", true);
				fhTxt.setFormatter(new SimpleFormatter());
			}
			viperLogger.addHandler(fhTxt);
		} catch (SecurityException e) {
			viperLogger.severe("Impossible d'associer le FileHandler");
			e.printStackTrace();
		} catch (IOException e) {
			viperLogger.severe("Impossible d'associer le FileHandler");
			e.printStackTrace();
		}
	}
}
