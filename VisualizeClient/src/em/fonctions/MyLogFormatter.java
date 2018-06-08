package em.fonctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Format pour les messages du logger
 * @author Eric Mariani
 * @since 03/01/2017
 *
 */
public class MyLogFormatter extends Formatter {
	   private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

	    public String format(LogRecord record) {
	        StringBuilder builder = new StringBuilder(1000);
	        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
	        builder.append("[").append(record.getSourceClassName()).append(".");
	        builder.append(record.getSourceMethodName()).append("] - ");
	        builder.append("[").append(record.getLevel()).append("] - ");
	        builder.append(formatMessage(record));
	        builder.append("\n");
	        return builder.toString();
	    }

	    public String getHead(Handler h) {
	        return super.getHead(h);
	    }

	    public String getTail(Handler h) {
	        return super.getTail(h);
	    }
}
