package gui.modeles;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import em.general.EFS_General;
import em.general.JTableConstantes;
import kernel.AlarmeSeuil;
import kernel.VoiesAPI;

public class ModeleJTableAlarmesSeuil extends AbstractTableModel implements EFS_General, JTableConstantes, VoiesAPI {
	private static final long serialVersionUID = 1L;
	private final List<AlarmeSeuil> lstAlarmeSeuil = new ArrayList<AlarmeSeuil>();
    private final String[] entetes = {"Voie", "Description", "Inventaire", "Apparition", "Valeur", "Seuil atteint"};
    
    /**
     * Constructeur
     */
    public ModeleJTableAlarmesSeuil() {
    	super();
    }
    
    /**
     * Renvoie l'Id Capteur 
     * @param rowIndex
     * @return
     */
    public long getIdCapteur(int rowIndex) {
    	return lstAlarmeSeuil.get(rowIndex).getIdCapteur();
    }
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return lstAlarmeSeuil.size();
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
	    	case JT_ALARME_SEUIL_NOM: 
	    		return lstAlarmeSeuil.get(rowIndex).getNomCapteur(); 
	    	case JT_ALARME_SEUIL_DESCRIPTION: 
	    		return lstAlarmeSeuil.get(rowIndex).getDescriptionCapteur(); 
	    	case JT_ALARME_SEUIL_INVENTAIRE: 
	    		return lstAlarmeSeuil.get(rowIndex).getInventaire(); 
	    	case JT_ALARME_SEUIL_DATE_APPARITION: 
	    		if(lstAlarmeSeuil.get(rowIndex).getDateApparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return lstAlarmeSeuil.get(rowIndex).getDateApparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_SEUIL_VALEUR: 
	    		return lstAlarmeSeuil.get(rowIndex).getValeurAPI() / 10; 
	    	case JT_ALARME_SEUIL_ATTEINT: 
	    		return lstAlarmeSeuil.get(rowIndex).getSeuilAtteint(); 
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addAlarmeSeuil(AlarmeSeuil alarmeSeuil) {
    	lstAlarmeSeuil.add(alarmeSeuil);
 
        fireTableRowsInserted(lstAlarmeSeuil.size() -1, lstAlarmeSeuil.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeAlarmeSeuil(int rowIndex) {
    	lstAlarmeSeuil.remove(rowIndex);
 
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
//        	AlarmeSeuil alarmeSeuil = lstAlarmeSeuil.get(rowIndex);

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
    		removeAlarmeSeuil(i - 1);
    	}
    }    


}
