package gui.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import em.general.EFS_General;
import em.general.JTableConstantes;
import kernel.AlarmeEnCours;
import kernel.VoiesAPI;

public class ModeleJTableAlarmesEnCours extends AbstractTableModel implements EFS_General, JTableConstantes, VoiesAPI {
	private static final long serialVersionUID = 1L;
	private final List<AlarmeEnCours> lstAlarmeEnCours = new ArrayList<AlarmeEnCours>();
    private final String[] entetes = {"Voie", "Description", "Inventaire", "Apparition", "Disparition", "Prise en compte", "Alarme description", "Type", "Valeur", "A", "Rappel"};
    
    /**
     * Constructeur
     */
    public ModeleJTableAlarmesEnCours() {
    	super();
    }
    
    /**
     * Renvoie l'Id Capteur 
     * @param rowIndex
     * @return
     */
    public long getIdCapteur(int rowIndex) {
    	return lstAlarmeEnCours.get(rowIndex).getIdCapteur();
    }
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return lstAlarmeEnCours.size();
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
	    	case JT_ALARME_EN_COURS_NOM: 
	    		return lstAlarmeEnCours.get(rowIndex).getNomCapteur(); 
	    	case JT_ALARME_EN_COURS_DESCRIPTION: 
	    		return lstAlarmeEnCours.get(rowIndex).getDescriptionCapteur(); 
	    	case JT_ALARME_EN_COURS_INVENTAIRE: 
	    		return lstAlarmeEnCours.get(rowIndex).getInventaire(); 
	    	case JT_ALARME_EN_COURS_DATE_APPARITION: 
	    		if(lstAlarmeEnCours.get(rowIndex).getDateApparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return lstAlarmeEnCours.get(rowIndex).getDateApparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_EN_COURS_DATE_DISPARITION: 
	    		if(lstAlarmeEnCours.get(rowIndex).getDateDisparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return lstAlarmeEnCours.get(rowIndex).getDateDisparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_EN_COURS_DATE_PRISE_EN_COMPTE: 
	    		if(lstAlarmeEnCours.get(rowIndex).getDatePriseEnCompte() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return lstAlarmeEnCours.get(rowIndex).getDatePriseEnCompte().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_EN_COURS_ALARME_DESCRIPTION:
	    		return lstAlarmeEnCours.get(rowIndex).getDescriptionAlarme();
	    	case JT_ALARME_EN_COURS_TYPE: 
	    		switch(lstAlarmeEnCours.get(rowIndex).getTypeAlarme()) {
	    		case ALARME_RIEN:
	    			return "Rien";
	    		case ALARME_ALERT:
	    			return "Alarme";
	    		case ALARME_DEFAUT:
	    			return "Défaut";
	    		case ALARME_ETAT:
	    			return "Etat";
	    		default:
	    			return "???";
	    		}
	    	case JT_ALARME_EN_COURS_VALEUR: 
	    		if(lstAlarmeEnCours.get(rowIndex).getTypeCapteur() == CAPTEUR_ANALOGIQUE_ENTREE) {
	    			return lstAlarmeEnCours.get(rowIndex).getValeurAPI() / 10;
	    		} else {
	    			return lstAlarmeEnCours.get(rowIndex).getValeurAPI();
	    		}
	    	case JT_ALARME_EN_COURS_APPEL_ALERT: 
	    		if(lstAlarmeEnCours.get(rowIndex).isAppelAlert()) {
	    			return "A";
	    		} else {
	    			return "";
	    		}
	    	case JT_ALARME_EN_COURS_RAPPEL: 
	    		return 0; 
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addAlarmeEnCours(AlarmeEnCours alarmeEnCours) {
    	lstAlarmeEnCours.add(alarmeEnCours);
 
        fireTableRowsInserted(lstAlarmeEnCours.size() -1, lstAlarmeEnCours.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeAlarmeEnCours(int rowIndex) {
    	lstAlarmeEnCours.remove(rowIndex);
 
        fireTableRowsDeleted(rowIndex, rowIndex);
    }    
    
    /**
     * Rend une cellule editable 
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (columnIndex == JT_ALARME_EN_COURS_RAPPEL) {
    		return true; 
    	}
    	else {
    		return false; 
    	}
    }    
    
    /**
     * Ecrit dans une cellule 
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(aValue != null){
        	AlarmeEnCours alarmeEnCours = lstAlarmeEnCours.get(rowIndex);

        	switch(columnIndex){
            	case JT_ALARME_EN_COURS_NOM:
            		alarmeEnCours.setNomCapteur((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_ALARME_EN_COURS_DESCRIPTION:
            		alarmeEnCours.setDescriptionCapteur((String) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_ALARME_EN_COURS_DATE_APPARITION:
            		alarmeEnCours.setDateApparition((LocalDateTime) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_ALARME_EN_COURS_DATE_DISPARITION:
            		alarmeEnCours.setDateDisparition((LocalDateTime) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
            	case JT_ALARME_EN_COURS_DATE_PRISE_EN_COMPTE:
            		alarmeEnCours.setDatePriseEnCompte((LocalDateTime) aValue);
            		fireTableCellUpdated(rowIndex, columnIndex);
            		break;
    	    	case JT_ALARME_EN_COURS_RAPPEL: 
            		alarmeEnCours.setValeurAPI((Integer) aValue);
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
    		removeAlarmeEnCours(i - 1);
    	}
    }    

}
