package em.fonctions;

import java.io.IOException;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Gestion du logger - Creation, formatage, utilisation
 * @author Eric Mariani
 * @since 03/01/2017
 *
 */
public class GestionLogger {
	public static Logger gestionLogger;
	private static Handler fhXml;
	private static Handler fhTxt;
	
	
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
				fhXml = new FileHandler("ViperLogging.xml", true);
				gestionLogger.addHandler(fhXml);
			}
			if(fhTxt == null) {
				fhTxt = new FileHandler("ViperLogging.log", true);
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
}
