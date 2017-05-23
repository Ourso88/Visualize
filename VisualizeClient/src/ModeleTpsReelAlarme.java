import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class ModeleTpsReelAlarme  extends AbstractTableModel implements AE_General.AE_Constantes {
	private static final long serialVersionUID = 1L;
	private final List<TpsReelAlarme> alarmes = new ArrayList<TpsReelAlarme>();
    private final String[] entetes = {"Voie", "Description", "Inventaire", "Apparition", "Disparition", 
    		"Prise en compte", "Alarme description", "Type", "Valeur", "A", "Rappel"};
    
    public ModeleTpsReelAlarme() {
    	super();
    }
    
    public int getRowCount() {
        return alarmes.size();
    }
 
    public int getColumnCount() {
        return entetes.length;
    }
 
    public String getColumnName(int columnIndex) {
        return entetes[columnIndex];
    }
 
    public int getIdCapteur(int rowIndex) {
    	return alarmes.get(rowIndex).getIdCapteur();
    } // Fin getIdCapteur()

    public int getVoieApi(int rowIndex) {
    	return alarmes.get(rowIndex).getVoieApi();
    } // Fin getIdCapteur()
    
    public String getNom(int rowIndex) {
    	return alarmes.get(rowIndex).getNom();
    } // Fin getIdCapteur()
    
    public Boolean isBlAlert(int rowIndex) {
    	return alarmes.get(rowIndex).isBlAlert();
    } // Fin getIdCapteur()
   
    public void razDisparition(int rowIndex) {
    	TpsReelAlarme alarme = alarmes.get(rowIndex);
		alarme.setDisparition(null);
        fireTableCellUpdated(rowIndex, JTABLE_ALARME_DISPARITION);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case JTABLE_ALARME_VOIE:
	    		return alarmes.get(rowIndex).getNom();
	    	case JTABLE_ALARME_DESCRIPTION_VOIE:
	    		return alarmes.get(rowIndex).getDescription();
	    	case JTABLE_ALARME_INVENTAIRE:
	    		return alarmes.get(rowIndex).getInventaire();
	    	case JTABLE_ALARME_APPARITION:
	    		return alarmes.get(rowIndex).getApparition();
	    	case JTABLE_ALARME_DISPARITION:
	    		return alarmes.get(rowIndex).getDisparition(); 
	    	case JTABLE_ALARME_PRISE_EN_COMPTE:
	    		return alarmes.get(rowIndex).getAcquittement();
	    	case JTABLE_ALARME_DESCRIPTION_ALARME:
	    		return alarmes.get(rowIndex).getDescriptionAlarme(); 
	    	case JTABLE_ALARME_TYPE_ALARME:
	    		return alarmes.get(rowIndex).getStringTypeAlarme(); 
	    	case JTABLE_ALARME_VALEUR:
	    		return alarmes.get(rowIndex).getValeurTpsReel(); 
	    	case JTABLE_ALARME_APPEL_ALERT:
	    		return alarmes.get(rowIndex).getAppelAlert();
	    	case JTABLE_ALARME_RAPPEL_ALERT:
	    		return alarmes.get(rowIndex).getRappelAlert();
	    	default :
	    		return null;
    	}
    }    
    
    public void addAlarme(TpsReelAlarme alarme) {
        alarmes.add(alarme);
 
        fireTableRowsInserted(alarmes.size() -1, alarmes.size() -1);
    }    
 
    public void removeAlarme(int rowIndex) {
        alarmes.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    

    public void removeAllAlarme() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeAlarme(i - 1);
    	}
    }    
   
    public void setMotifIdPriseEncompte(int rowIndex, int motifIdPriseEnCompte) {
    	alarmes.get(rowIndex).setMotifIdPriseEncompte(motifIdPriseEnCompte);
    }
    
    public int getMotifIdPriseEncompte(int rowIndex) {
    	return alarmes.get(rowIndex).getMotifIdPriseEncompte();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (columnIndex == JTABLE_ALARME_RAPPEL_ALERT) {
    		return true; 
    	}
    	else {
    		return false; 
    	}
    }    
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
            TpsReelAlarme alarme = alarmes.get(rowIndex);
     
            switch(columnIndex){
            	case JTABLE_ALARME_APPARITION:
            		alarme.setApparition((Date) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JTABLE_ALARME_DISPARITION:
            		alarme.setDisparition((Date) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JTABLE_ALARME_PRISE_EN_COMPTE:
            		alarme.setAcquittement((Date) aValue);
                    fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JTABLE_ALARME_VALEUR:
            		alarme.setValeurTpsReel((double) aValue);
                    fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JTABLE_ALARME_APPEL_ALERT:
            		alarme.setAppelAlert((int) aValue);
                    fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JTABLE_ALARME_RAPPEL_ALERT:
            		try {
            			alarme.setRappelAlert(Integer.parseInt((String) aValue));
            		} catch (Exception ex) {
            			alarme.setRappelAlert((int) aValue);
//            			ex.printStackTrace();
            		}
                    fireTableCellUpdated(rowIndex, columnIndex);
            		break;
                default :
            }
        }
    }    
    
} // Fin class
