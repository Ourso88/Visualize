import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;
import EFS_Structure.StructValidationHistoriqueAlarme;


public class ModeleValidationHistoriqueAlarme extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final List<StructValidationHistoriqueAlarme> validationHistoriqueAlarmes = new ArrayList<StructValidationHistoriqueAlarme>();
    private final String[] entetes = {"DateValidation", "DateDebut", "DateFin", "Utilisateur"};
    
    public ModeleValidationHistoriqueAlarme() throws ParseException {
    	super();
    }
    
    public int getRowCount() {
        return validationHistoriqueAlarmes.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_VALIDATION:
	    		return validationHistoriqueAlarmes.get(rowIndex).getDateValidation(); 
	    	case AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_DEBUT:
	    		return validationHistoriqueAlarmes.get(rowIndex).getDateDebut(); 
	    	case AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_FIN:
	    		return validationHistoriqueAlarmes.get(rowIndex).getDateFin(); 
	    	case AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_UTILISATEUR:
	    		return validationHistoriqueAlarmes.get(rowIndex).getUtilisateur(); 
	    	default:
	    		return null;
	    		
    	}
    	
    }    
    
    public void addvalidationHistoriqueAlarme(StructValidationHistoriqueAlarme validationHistoriqueAlarme) {
    	validationHistoriqueAlarmes.add(validationHistoriqueAlarme);
 
        fireTableRowsInserted(validationHistoriqueAlarmes.size() -1, validationHistoriqueAlarmes.size() -1);
    }    
 
    public void removevalidationHistoriqueAlarme(int rowIndex) {
    	validationHistoriqueAlarmes.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    

    public void removeAllvalidationHistoriqueAlarme() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removevalidationHistoriqueAlarme(i - 1);
    	}
    }    
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (columnIndex >= 5) {
    		return false; 
    	}
    	else {
    		return false; 
    	}
    }    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
//            HistoriqueAlarme alarme = alarmes.get(rowIndex);
     
            switch(columnIndex){
                default :
            }
        }
    }    
    
}
