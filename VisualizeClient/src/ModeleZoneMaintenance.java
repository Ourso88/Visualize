import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class ModeleZoneMaintenance extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private final List<TypeZoneMaintenance> zoneMaintenance = new ArrayList<TypeZoneMaintenance>();
    private final String[] entetes = {"N° voie", "Description", "Raison de maintenance"};
    
    public ModeleZoneMaintenance() throws ParseException {
    	super();
    	
        zoneMaintenance.add(new TypeZoneMaintenance("DI12", "Description de DI12", "Raison inconnue"));
        zoneMaintenance.add(new TypeZoneMaintenance("AI33", "Description de AI12", "En repos"));
        zoneMaintenance.add(new TypeZoneMaintenance("DI25", "Description de DI25", "Controle cuve"));
    	
    }
    
    public int getRowCount() {
        return zoneMaintenance.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case 0:
	    		return zoneMaintenance.get(rowIndex).getVoie(); 
	    	case 1:
	    		return zoneMaintenance.get(rowIndex).getDescription(); 
	    	case 2:
	    		return zoneMaintenance.get(rowIndex).getRaisonMaintenance(); 
	    	default:
	    		return null;
	    		
    	}
    	
    }    
    
    public void addZoneMaintenance(TypeZoneMaintenance uneZoneMaintenance) {
        zoneMaintenance.add(uneZoneMaintenance);
 
        fireTableRowsInserted(zoneMaintenance.size() -1, zoneMaintenance.size() -1);
    }    
 
    public void removeZoneMaintenance(int rowIndex) {
        zoneMaintenance.remove(rowIndex);
 
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
//        	TypeZoneMaintenance uneZoneMaintenance = zoneMaintenance.get(rowIndex);
     
        	/*
            switch(columnIndex){
            	case 0:
            		alarme.setDisparition((Date) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case 4:
            		alarme.setAcquittement((Date) aValue);
                    fireTableCellUpdated(rowIndex, columnIndex);
            		break;
                default :
            } // fin switch
            */
        }
    }    
    
}
