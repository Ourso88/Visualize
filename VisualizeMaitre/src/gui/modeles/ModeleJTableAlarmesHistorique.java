package gui.modeles;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import em.general.EFS_General;
import em.general.JTableConstantes;
import kernel.AlarmeHistorique;

/**
 * Modele pour le JTable Historique Alarmes
 * @author Eric Mariani
 * @since 21/02/2017
 *
 */
public class ModeleJTableAlarmesHistorique extends AbstractTableModel implements EFS_General, JTableConstantes {
	private static final long serialVersionUID = 1L;
	private final List<AlarmeHistorique> lstAlarmeHistorique = new ArrayList<AlarmeHistorique>();
    private final String[] entetes = {"Voie", "Description", "Apparition", "Disparition", "Prise en compte"};
    
    /**
     * Constructeur
     */
    public ModeleJTableAlarmesHistorique() {
    	super();
    }
    
    /**
     * Renvoie l'Id Capteur 
     * @param rowIndex
     * @return
     */
    public long getIdCapteur(int rowIndex) {
    	return lstAlarmeHistorique.get(rowIndex).getIdCapteur();
    }
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return lstAlarmeHistorique.size();
    }
 
    /**
     * 
     * @return
     * 		Le nombre de colonnes
     */
    public int getColumnCount() {
        return entetes.length;
    }
 
    /**
     * 
     * @param columnIndex
     * 		Index de la colonne
     * @return
     * 		Le titre de la colonne
     */
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    /**
     * 
     * @param rowIndex
     * 		Index de la ligne
     * @param columnIndex
     * 		Index de la colonne
     * @return
     * 		Valeur de la case (ligne, colonne)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case JT_ALARME_HISTORIQUE_NOM: 
	    		return lstAlarmeHistorique.get(rowIndex).getNomCapteur(); 
	    	case JT_ALARME_HISTORIQUE_DESCRIPTION: 
	    		return lstAlarmeHistorique.get(rowIndex).getDescriptionCapteur(); 
	    	case JT_ALARME_HISTORIQUE_DATE_APPARITION: 
	    		return lstAlarmeHistorique.get(rowIndex).getDateApparition();
	    	case JT_ALARME_HISTORIQUE_DATE_DISPARITION: 
	    		return lstAlarmeHistorique.get(rowIndex).getDateDisparition();
	    	case JT_ALARME_HISTORIQUE_DATE_PRISE_EN_COMPTE: 
	    		return lstAlarmeHistorique.get(rowIndex).getDatePriseEnCompte();
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addAlarmeHistorique(AlarmeHistorique alarmeHistorique) {
    	lstAlarmeHistorique.add(alarmeHistorique);
 
        fireTableRowsInserted(lstAlarmeHistorique.size() -1, lstAlarmeHistorique.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeAlarmeHistorique(int rowIndex) {
    	lstAlarmeHistorique.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    
    
    /**
     * Rend une cellule editable 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
   		return false; 
    }    
    
    /**
     * Ecrit dans une cellule 
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
//        	AlarmeHistorique alarmeHistorique = lstAlarmeHistorique.get(rowIndex);

        	switch(columnIndex){
                default :
            }
        }
    }    
    
    /**
     * Supprime toutes les lignes du tableau
     */
    public void removeAll() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeAlarmeHistorique(i - 1);
    	}
    }    
    
}
