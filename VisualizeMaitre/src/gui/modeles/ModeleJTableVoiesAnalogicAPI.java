package gui.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import em.general.EFS_General;
import em.general.JTableConstantes;
import kernel.AnalogicInput;
import kernel.VoiesAPI;

/**
 * Modele pour les JTable visualisant le Voies API
 * @author Eric Mariani
 * @since 16/02/2017
 */
public class ModeleJTableVoiesAnalogicAPI extends AbstractTableModel implements JTableConstantes, VoiesAPI {
	private static final long serialVersionUID = 1L;
	private final List<AnalogicInput> lstAnalogicInput = new ArrayList<AnalogicInput>();
    private final String[] entetes = {"Nom", "Valeur API", "Seuil atteint", "Alarme enclenchée", "Date apparition alarme", "Tempo ecoulee", "Type Alarme", "Tempo (mn)", "Pre Tempo"};
    
    /**
     * Constructeur
     */
    public ModeleJTableVoiesAnalogicAPI() {
    	super();
    }
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return lstAnalogicInput.size();
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
	    	case JT_VOIES_API_NOM: // Nom
	    		return lstAnalogicInput.get(rowIndex).getNom(); 
	    	case JT_VOIES_API_VALEUR: // Valeur lue
	    		return lstAnalogicInput.get(rowIndex).getValeurAPI() / 10;
	    	case JT_VOIES_API_SEUIL_ATTEINT: // Seuil atteint en texte
	    		return lstAnalogicInput.get(rowIndex).getSeuilAtteint();
	    	case JT_VOIES_API_ALARME_ENCLENCHEE: // Alarme enclenchée
	    		if(lstAnalogicInput.get(rowIndex).isAlarmeEnclenchee()) {
	    			if(lstAnalogicInput.get(rowIndex).getAlarme() == EFS_General.ALARME_ALERT) {
	    				return "ALARME";
	    			} if(lstAnalogicInput.get(rowIndex).getAlarme() == EFS_General.ALARME_DEFAUT) {
	    				return "DEFAUT";
	    			} else {
	    				return "==> PB <==";
	    			}
	    		} else {
	    			return "---";
	    		}
	    	case JT_VOIES_API_DATE_APPARITION_ALARME: // Date Apparition Alarme
	    		if(lstAnalogicInput.get(rowIndex).getDateAlarmeApparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return lstAnalogicInput.get(rowIndex).getDateAlarmeApparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_VOIES_API_TEMPO_ALARME_ECOULEE: // Alarme enclenchée
	    		if(lstAnalogicInput.get(rowIndex).isAlarmeTempoEcoulee()) {
	    			return "APPEL";
	    		} else {
	    			return "---";
	    		}
	    	case JT_VOIES_API_TYPE_ALARME: // Type Alarme
	    		switch (lstAnalogicInput.get(rowIndex).getAlarme()) {
	    		case EFS_General.ALARME_RIEN:
	    			return "AUCUN";
	    		case EFS_General.ALARME_ALERT:
	    			return "ALARME";
	    		case EFS_General.ALARME_DEFAUT:
	    			return "DEFAUT";
	    		default:
	    			return "???";
	    		}
	    	case JT_VOIES_API_TEMPO_ALARME: // Tempo seuil
	    		return lstAnalogicInput.get(rowIndex).getSeuilTempo();
	    	case JT_VOIES_API_PRE_TEMPO_ALARME: // Tempo pre-seuil
	    		return lstAnalogicInput.get(rowIndex).getPreSeuilTempo();
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addVoiesAPI(AnalogicInput analogicInput) {
    	lstAnalogicInput.add(analogicInput);
 
        fireTableRowsInserted(lstAnalogicInput.size() -1, lstAnalogicInput.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeAnalogicInput(int rowIndex) {
    	lstAnalogicInput.remove(rowIndex);
 
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
        	AnalogicInput analogicInput = lstAnalogicInput.get(rowIndex);

        	switch(columnIndex){
            	case JT_VOIES_API_NOM: // Nom
            		analogicInput.setNom((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_VALEUR: // Valeur
            		analogicInput.setValeurAPI((Double) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_SEUIL_ATTEINT: // Seuil atteint
            		analogicInput.setSeuilAtteint((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_ALARME_ENCLENCHEE: // Alarme enclenchee
            		analogicInput.setAlarmeEnclenchee((Boolean) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_DATE_APPARITION_ALARME: // Date Apparition Alarme
            		analogicInput.setDateAlarmeApparition((LocalDateTime) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_VOIES_API_TEMPO_ALARME_ECOULEE: // Tempo ecoule
            		analogicInput.setAlarmeTempoEcoulee((Boolean) aValue);
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
    		removeAnalogicInput(i - 1);
    	}
    }    

}
