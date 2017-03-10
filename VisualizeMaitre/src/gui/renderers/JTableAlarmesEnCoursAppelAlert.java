package gui.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import em.general.AE_Constantes;


public class JTableAlarmesEnCoursAppelAlert  extends DefaultTableCellRenderer implements AE_Constantes {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       
        // GTC Visualize
        String appelAlert = (String) value;

        if (appelAlert.equals("A")) {
        	setBackground(AE_ROUGE);
        	setForeground(AE_BLANC);
        }
        else {
        	setBackground(AE_BLEU);
        	setForeground(AE_NOIR);
        }
        return this;
    } 
} // Fin class
