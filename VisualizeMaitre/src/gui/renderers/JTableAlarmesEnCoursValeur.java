package gui.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import em.general.AE_Constantes;
import em.general.EFS_General;
import kernel.VoiesAPI;

/**
 * Gestion des couleurs pour le JTable colonne Valeur
 * @author Eric Mariani
 * @since 25/03/2017
 */
public class JTableAlarmesEnCoursValeur extends DefaultTableCellRenderer implements AE_Constantes, VoiesAPI {
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       
        // GTC Visualize
        if(tbAlarme.get(row).getTypeCapteur() == EFS_General.CAPTEUR_ANALOGIQUE_ENTREE) {
	        double valeurActuelle = (double) value;
	        int minSeuil = tbAnaAPI.get(tbAlarme.get(row).getIndexCapteur()).getSeuilBas() / 10;
	        int maxSeuil = tbAnaAPI.get(tbAlarme.get(row).getIndexCapteur()).getSeuilHaut() / 10;
	
	        if (valeurActuelle <= minSeuil || valeurActuelle > maxSeuil) {
	        	setBackground(AE_ROUGE);
	        	setForeground(AE_BLANC);
	        }
	        else {
	        	setBackground(AE_BLEU);
	        	setForeground(AE_NOIR);
	        }
        } else {
        	setBackground(AE_BLEU);
        	setForeground(AE_NOIR);
        }
        
        return this;
    } 
}
