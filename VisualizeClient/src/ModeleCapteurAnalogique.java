import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;


public class ModeleCapteurAnalogique extends AbstractTableModel implements AE_Constantes {
	private static final long serialVersionUID = 1L;

	private final List<TypeCapteurAnalogique> capteurAnalogique = new ArrayList<TypeCapteurAnalogique>();
    private final String[] entetes = {"Voie", "Equipement", "Designation", "Etat", "Seuil Bas", "Seuil Haut", "Tempo", "Calibration", "Valeur"};
    
    public ModeleCapteurAnalogique() throws ParseException {
    	super();
    }
    
    public int getRowCount() {
        return capteurAnalogique.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public int getIdCapteur(int rowIndex) {
    	return capteurAnalogique.get(rowIndex).getIdCapteur();
    }
    
    public int getVoieApi(int rowIndex) {
    	return capteurAnalogique.get(rowIndex).getVoieApi();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case JTABLE_CHOIX_CAPTEUR_ANA_NOM:
	    		return capteurAnalogique.get(rowIndex).getNomCapteur(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_EQUIPEMENT:
	    		return capteurAnalogique.get(rowIndex).getNomEquipement(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_DESCRIPTION:
	    		return capteurAnalogique.get(rowIndex).getDescriptionCapteur(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_ETAT:
	    		return capteurAnalogique.get(rowIndex).getEtatAlarme(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_BAS:
	    		return capteurAnalogique.get(rowIndex).getSeuilBas(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_SEUIL_HAUT:
	    		return capteurAnalogique.get(rowIndex).getSeuilHaut(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_TEMPO:
	    		return capteurAnalogique.get(rowIndex).getTempo(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_CALIBRATION:
	    		return capteurAnalogique.get(rowIndex).getCalibration(); 
	    	case JTABLE_CHOIX_CAPTEUR_ANA_VALEUR:
	    		return capteurAnalogique.get(rowIndex).getValeur(); 
	    	default:
	    		return null;
    	}
    }    
    
    public void addCapteurAnalogique(TypeCapteurAnalogique unCapteurAnalogique) {
    	capteurAnalogique.add(unCapteurAnalogique);
 
        fireTableRowsInserted(capteurAnalogique.size() -1, capteurAnalogique.size() -1);
    }    
 
    public void removeCapteurAnalogique(int rowIndex) {
    	capteurAnalogique.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    
    
    public void removeAll() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeCapteurAnalogique(i - 1);
    	}
    }    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	return false;
    }    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            TypeCapteurAnalogique unCapteurAnalogique = capteurAnalogique.get(rowIndex);
     
            switch(columnIndex){
            	case JTABLE_CHOIX_CAPTEUR_ANA_VALEUR:
            		unCapteurAnalogique.setValeur((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
                default :
            }
        }
    }  // fin setValueAt  
}
