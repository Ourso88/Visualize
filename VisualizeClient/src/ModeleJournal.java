import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;


public class ModeleJournal extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final List<TypeJournal> historiqueJournal = new ArrayList<TypeJournal>();
    private final String[] entetes = {"Date", "Action", "Utilisateur", "Voie", "Descrition voie", "Type"};
    
    public ModeleJournal() throws ParseException {
    	super();
    }
    
    public int getRowCount() {
        return historiqueJournal.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case AE_Constantes.JTABLE_JOURNAL_DATE:
	    		return historiqueJournal.get(rowIndex).getDateJournal(); 
	    	case AE_Constantes.JTABLE_JOURNAL_TYPE:
	    		return historiqueJournal.get(rowIndex).getTypeJournal(); 
	    	case AE_Constantes.JTABLE_JOURNAL_UTILISATEUR:
	    		return historiqueJournal.get(rowIndex).getUtilisateur(); 
	    	case AE_Constantes.JTABLE_JOURNAL_VOIE:
	    		return historiqueJournal.get(rowIndex).getVoie(); 
	    	case AE_Constantes.JTABLE_JOURNAL_DESCRIPTION_VOIE:
	    		return historiqueJournal.get(rowIndex).getDescriptionVoie(); 
	    	case AE_Constantes.JTABLE_JOURNAL_ACTION:
	    		return historiqueJournal.get(rowIndex).getAction(); 
	    	default:
	    		return null;
    	}
    }    
    
    public void addJournal(TypeJournal unhistoriqueJournal) {
    	historiqueJournal.add(unhistoriqueJournal);
 
        fireTableRowsInserted(historiqueJournal.size() -1, historiqueJournal.size() -1);
    }    
 
    public void removeJournal(int rowIndex) {
    	historiqueJournal.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    
 
    public void removeAll() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeJournal(i - 1);
    	}
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
//        	TypeJournal unhistoriqueJournal = historiqueJournal.get(rowIndex);
        }
    }    
} // Fin class
