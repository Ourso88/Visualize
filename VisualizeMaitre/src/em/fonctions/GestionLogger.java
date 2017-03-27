package em.fonctions;

import java.io.IOException;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import em.general.AE_Constantes;

/**
 * Gestion du logger - Creation, formatage, utilisation
 * @author Eric Mariani
 * @since 03/01/2017
 *
 */
public class GestionLogger implements AE_Constantes {
	public static Logger gestionLogger;
	private static Handler fhXml;
	private static Handler fhTxt;
	
	/**
	 * Initialise le logger
	 * @param pClasseAppellante
	 */
	public static void initialisation(String pClasseAppellante) {
		gestionLogger = Logger.getLogger(pClasseAppellante);
		
		gestionLogger.setUseParentHandlers(false);
		
		Handler consoleHandler = new Handler(){
			MyLogFormatter formatter = new MyLogFormatter();
	        @Override
	            public void publish(LogRecord record)
	            {
	                if (getFormatter() == null)
	                {
	                    setFormatter(formatter);
	                }

	                try {
	                    String message = getFormatter().format(record);
	                    if (record.getLevel().intValue() >= Level.WARNING.intValue())
	                    {
	                        System.err.write(message.getBytes());                       
	                    }
	                    else
	                    {
	                        System.out.write(message.getBytes());
	                    }
	                } catch (Exception exception) {
	                    reportError(null, exception, ErrorManager.FORMAT_FAILURE);
	                }

	            }

	            @Override
	            public void close() throws SecurityException {}
	            @Override
	            public void flush(){}
	        };		
		
        gestionLogger.addHandler(consoleHandler);		
		
		try {
			if(fhXml == null) {
				fhXml = new FileHandler("GTC_MAITRE_Logging.xml", true);
				gestionLogger.addHandler(fhXml);
			}
			if(fhTxt == null) {
				fhTxt = new FileHandler(LOGGER_FICHIER_TEXTE, true);
				MyLogFormatter formatter = new MyLogFormatter();
				fhTxt.setFormatter(formatter);
				gestionLogger.addHandler(fhTxt);
			}
		} catch (SecurityException e) {
			gestionLogger.severe("Impossible d'associer le FileHandler");
			e.printStackTrace();
		} catch (IOException e) {
			gestionLogger.severe("Impossible d'associer le FileHandler");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sauvegarde les fichiers dans une base de données
	 */
	public static void sauvegarde() {
		// Parcours du répertoire courant
		
	}
	
}
