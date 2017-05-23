import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import AE_General.AE_Constantes;


public class ColorAppelAlertCellRenderer  extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       
        // GTC Visualize
        int appelAlert = (int) value;

        if (appelAlert == 1) {
        	setBackground(Color.RED);
        }
        else {
        	setBackground(AE_Constantes.EFS_BLEU);
        }
        return this;
    } 
} // Fin class
