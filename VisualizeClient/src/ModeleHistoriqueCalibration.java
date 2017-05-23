import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;


public class ModeleHistoriqueCalibration extends AbstractTableModel implements AE_Constantes{
	private static final long serialVersionUID = 1L;
	private final List<TypeHistoriqueCalibration> historiqueCalibration = new ArrayList<TypeHistoriqueCalibration>();
    private final String[] entetes = {"N° voie", "Date", "Ancienne valeur", "Nouvelle Valeur", "Utilisateur"};
    
    public ModeleHistoriqueCalibration() throws ParseException {
    	super();
    }
    
    public int getRowCount() {
        return historiqueCalibration.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case JTABLE_HISTORIQUE_CALIBRATION_VOIE:
	    		return historiqueCalibration.get(rowIndex).getVoie(); 
	    	case JTABLE_HISTORIQUE_CALIBRATION_DATE:
	    		return historiqueCalibration.get(rowIndex).getDateCalibration(); 
	    	case JTABLE_HISTORIQUE_CALIBRATION_ANC_VALEUR:
	    		return historiqueCalibration.get(rowIndex).getAncValeur(); 
	    	case JTABLE_HISTORIQUE_CALIBRATION_NEW_VALEUR:
	    		return historiqueCalibration.get(rowIndex).getNewValeur(); 
	    	case JTABLE_HISTORIQUE_CALIBRATION_UTILISATEUR:
	    		return historiqueCalibration.get(rowIndex).getUtilisateur(); 
	    	default:
	    		return null;
	    		
    	}
    	
    }    
    
    public void addHistoriqueCalibration(TypeHistoriqueCalibration unHistoriqueCalibration) {
        historiqueCalibration.add(unHistoriqueCalibration);
 
        fireTableRowsInserted(historiqueCalibration.size() -1, historiqueCalibration.size() -1);
    }    
 
    public void removeZoneMaintenance(int rowIndex) {
        historiqueCalibration.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return false;
    	/*
    	if (columnIndex >= 5) {
    		return true; 
    	}
    	else {
    		return false; 
    	}
    	*/
    }    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
//        	TypeHistoriqueCalibration unHistoriqueCalibration = historiqueCalibration.get(rowIndex);
        }
    }    
    
} // Fin class
