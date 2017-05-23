import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;


public class ModeleCapteurDigitale  extends AbstractTableModel implements AE_Constantes  {
	private static final long serialVersionUID = 1L;

	private final List<TypeCapteurDigitale> capteurDigitale = new ArrayList<TypeCapteurDigitale>();
    private final String[] entetes = {"Voie", "Equipement", "Designation", "Etat", "Tempo", "NO/NF", "Valeur"};
    
    public ModeleCapteurDigitale() throws ParseException {
    	super();
    }
    
    public int getRowCount() {
        return capteurDigitale.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public int getIdCapteur(int rowIndex) {
    	return capteurDigitale.get(rowIndex).getIdCapteur();
    }
    
    public int getVoieApi(int rowIndex) {
    	return capteurDigitale.get(rowIndex).getVoieApi();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_NOM:
	    		return capteurDigitale.get(rowIndex).getNomCapteur(); 
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_EQUIPEMENT:
	    		return capteurDigitale.get(rowIndex).getNomEquipement(); 
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_DESCRIPTION:
	    		return capteurDigitale.get(rowIndex).getDescriptionCapteur(); 
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_ETAT:
	    		return capteurDigitale.get(rowIndex).getEtatAlarme(); 
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_TEMPO:
	    		return capteurDigitale.get(rowIndex).getTempo(); 
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_NONF:
	    		return capteurDigitale.get(rowIndex).getStringNoNf(); 
	    	case JTABLE_CHOIX_CAPTEUR_DIGI_VALEUR:
	    		return capteurDigitale.get(rowIndex).getValeur(); 
	    	default:
	    		return null;
    	}
    }    
    
    public void addCapteurDigitale(TypeCapteurDigitale unCapteurDigitale) {
    	capteurDigitale.add(unCapteurDigitale);
 
        fireTableRowsInserted(capteurDigitale.size() -1, capteurDigitale.size() -1);
    }    
 
    public void removeCapteurDigitale(int rowIndex) {
    	capteurDigitale.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    
    
    public void removeAll() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeCapteurDigitale(i - 1);
    	}
    }    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return false;
    }    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            TypeCapteurDigitale unCapteurDigitale = capteurDigitale.get(rowIndex);
     
            switch(columnIndex){
            	case JTABLE_CHOIX_CAPTEUR_DIGI_VALEUR:
            		unCapteurDigitale.setValeur((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
                default :
            }
        }
    }  // fin setValueAt  
}// fin class
