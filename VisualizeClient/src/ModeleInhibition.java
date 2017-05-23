import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;


public class ModeleInhibition  extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final List<Inhibition> inhibitions = new ArrayList<Inhibition>();
    private final String[] entetes = {"Voie", "Description", "Date maintenance", "Raison", "Utilisateur"};
    
    public ModeleInhibition() throws ParseException {
    	super();
    }
    
    public int getRowCount() {
        return inhibitions.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
    
    public int getIdCapteur(int rowIndex) {
    	return inhibitions.get(rowIndex).getIdCapteur();
    } // Fin getIdCapteur()

    public int getIdInhibition(int rowIndex) {
    	return inhibitions.get(rowIndex).getIdInhibition();
    } 

    public int getIdUtilisateur(int rowIndex) {
    	return inhibitions.get(rowIndex).getIdUtilisateur();
    } 
    
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case AE_Constantes.JTABLE_INHIBITION_VOIE:
	    		return inhibitions.get(rowIndex).getNom(); 
	    	case AE_Constantes.JTABLE_INHIBITION_DESCRIPTION_VOIE:
	    		return inhibitions.get(rowIndex).getDescription(); 
	    	case AE_Constantes.JTABLE_INHIBITION_DATE_INHIBITION:
	    		return inhibitions.get(rowIndex).getDerniereInhibition(); 
	    	case AE_Constantes.JTABLE_INHIBITION_RAISON_INHIBITION:
	    		return inhibitions.get(rowIndex).getRaisonInhibition(); 
	    	case AE_Constantes.JTABLE_INHIBITION_UTILISATEUR:
	    		return inhibitions.get(rowIndex).getNomUtilisateur(); 
	    	default:
	    		return null;
    	}
    }    
    
    public void addInhibition(Inhibition inhibition) {
        inhibitions.add(inhibition);
 
        fireTableRowsInserted(inhibitions.size() -1, inhibitions.size() -1);
    }    
 
    public void removeInhibition(int rowIndex) {
        inhibitions.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    

    public void removeAllInhibition() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeInhibition(i - 1);
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
            @SuppressWarnings("unused")
			Inhibition inhibition = inhibitions.get(rowIndex);
     
            switch(columnIndex){
                default :
            }
        }
    }        
    
} // fin class
