package gui.renderers;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import em.general.JTableConstantes;


/**
 * Gestion de la couleur du JTable AlarmesEnCours pour la colonne Type Alarme
 * @author Eric Mariani
 * @since 21/02/2017
 *
 */
public class JTableAlarmesEnCoursColorCellRenderer extends DefaultTableCellRenderer implements JTableConstantes {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       
        // GTC Visualize
        String typeVoie = (String) table.getModel().getValueAt(row, JT_ALARME_EN_COURS_TYPE);
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
