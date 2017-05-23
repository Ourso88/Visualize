import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import AE_General.AE_Constantes;


public class ColorCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       
        // GTC Visualize
        String typeVoie = (String) table.getModel().getValueAt(row, AE_Constantes.JTABLE_ALARME_TYPE_ALARME);
//        if (!isSelected) {
	        if (typeVoie.equals("Alarme")) {
	        	setBackground(Color.RED);
	        	setForeground(Color.WHITE);
	        }
	        else if (typeVoie.equals("Défaut")) {
	        	setBackground(Color.ORANGE);
	        	setForeground(Color.BLACK);
	        }
	        else if (typeVoie.equals("Etat")) {
	        	setBackground(Color.GREEN);
	        	setForeground(Color.BLACK);
	        }
//        }
        return this;
    }	
}
