package gui.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import em.general.EFS_General;
import em.general.JTableConstantes;
import kernel.DigitalInput;

/**
 * Modele pour les JTable visualisant le Voies digitales API
 * @author Eric Mariani
 * @since 19/02/2017
 */
public class ModeleJTableVoiesDigitalAPI extends AbstractTableModel implements JTableConstantes{
	private static final long serialVersionUID = 1L;
	private final List<DigitalInput> lstDigitalInput = new ArrayList<DigitalInput>();
    private final String[] entetes = {"Nom", "Valeur API", "Alarme enclenchée", "Date apparition alarme", "Tempo ecoulee", "Type Alarme", "Tempo (s)"};
    
    /**
     * Constructeur
     */
    public ModeleJTableVoiesDigitalAPI() {
    	super();
    }
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return lstDigitalInput.size();
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
	    	case JT_VOIES_API_DIGI_NOM: // Nom
	    		return lstDigitalInput.get(rowIndex).getNom(); 
	    	case JT_VOIES_API_DIGI_VALEUR: // Valeur lue
	    		return lstDigitalInput.get(rowIndex).getValeurAPI();
	    	case JT_VOIES_API_DIGI_ALARME_ENCLENCHEE: // Alarme enclenchée
	    		if(lstDigitalInput.get(rowIndex).isAlarmeEnclenchee()) {
	    			if(lstDigitalInput.get(rowIndex).getAlarme() == EFS_General.ALARME_ALERT) {
	    				return "ALARME";
	    			} if(lstDigitalInput.get(rowIndex).getAlarme() == EFS_General.ALARME_DEFAUT) {
	    				return "DEFAUT";
	    			} if(lstDigitalInput.get(rowIndex).getAlarme() == EFS_General.ALARME_ETAT) {
	    				return "ETAT";
	    			} else {
	    				return "==> PB <==";
	    			}
	    		} else {
	    			return "---";
	    		}
	    	case JT_VOIES_API_DIGI_DATE_APPARITION_ALARME: // Date Apparition Alarme
	    		if(lstDigitalInput.get(rowIndex).getDateAlarmeApparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return lstDigitalInput.get(rowIndex).getDateAlarmeApparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_VOIES_API_DIGI_TEMPO_ALARME_ECOULEE: // Alarme enclenchée
	    		if(lstDigitalInput.get(rowIndex).isAlarmeTempoEcoulee()) {
	    			return "APPEL";
	    		} else {
	    			return "---";
	    		}
	    	case JT_VOIES_API_DIGI_TYPE_ALARME: // Type Alarme
	    		switch (lstDigitalInput.get(rowIndex).getAlarme()) {
	    		case EFS_General.ALARME_RIEN:
	    			return "AUCUN";
	    		case EFS_General.ALARME_ALERT:
	    			return "ALARME";
	    		case EFS_General.ALARME_DEFAUT:
	    			return "DEFAUT";
	    		default:
	    			return "???";
	    		}
	    	case JT_VOIES_API_DIGI_TEMPO_ALARME: 
	    		return lstDigitalInput.get(rowIndex).getTempo();
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addVoiesAPI(DigitalInput digitalInput) {
    	lstDigitalInput.add(digitalInput);
 
        fireTableRowsInserted(lstDigitalInput.size() -1, lstDigitalInput.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeDigitalInput(int rowIndex) {
    	lstDigitalInput.remove(rowIndex);
 
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
        	DigitalInput digitalInput = lstDigitalInput.get(rowIndex);

        	switch(columnIndex){
            	case JT_VOIES_API_DIGI_NOM: // Nom
            		digitalInput.setNom((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_DIGI_VALEUR: // Valeur
            		digitalInput.setValeurAPI((Integer) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_DIGI_ALARME_ENCLENCHEE: // Alarme enclenchee
            		digitalInput.setAlarmeEnclenchee((Boolean) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_DIGI_DATE_APPARITION_ALARME: // Date Apparition Alarme
            		digitalInput.setDateAlarmeApparition((LocalDateTime) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_DIGI_TEMPO_ALARME_ECOULEE: // Tempo ecoule
            		digitalInput.setAlarmeTempoEcoulee((Boolean) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
                default :
            }
        }
    }    
    
    /**
     * Supprime toutes les lignes du tableau
     */
    public void removeAll() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeDigitalInput(i - 1);
    	}
    }    

}
