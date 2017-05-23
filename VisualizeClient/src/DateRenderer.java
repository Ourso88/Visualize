import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.table.DefaultTableCellRenderer;


public class DateRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	DateFormat formatter;
    
    public DateRenderer() {
        super();
    }      
 
    public void setValue(Object value) {
        if (formatter==null) {
             formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        }
        setText((value == null) ? "" : formatter.format(value));
    }
}
