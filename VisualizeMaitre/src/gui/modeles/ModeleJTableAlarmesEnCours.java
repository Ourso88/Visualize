package gui.modeles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.table.AbstractTableModel;

import em.general.JTableConstantes;
import kernel.AlarmeEnCours;
import kernel.VoiesAPI;

/**
 * Modele pour les JTable visualisant les alrmes en cours
 * @author Eric Mariani
 * @since 18/02/2017
 */
public class ModeleJTableAlarmesEnCours extends AbstractTableModel implements JTableConstantes, VoiesAPI {
	private static final long serialVersionUID = 1L;
//	private final List<AlarmeEnCours> lstAlarmeEncours = new ArrayList<AlarmeEnCours>();
    private final String[] entetes = {"Nom", "Description", "Date apparition", "Date disparition", "Date prise en compte", "Valeur"};
    
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
    	return tbAlarme.get(rowIndex).getIdCapteur();
    }
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return tbAlarme.size();
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
	    		return tbAlarme.get(rowIndex).getNomCapteur(); 
	    	case JT_ALARME_EN_COURS_DESCRIPTION: 
	    		return tbAlarme.get(rowIndex).getDescriptionCapteur(); 
	    	case JT_ALARME_EN_COURS_DATE_APPARITION: 
	    		if(tbAlarme.get(rowIndex).getDateApparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return tbAlarme.get(rowIndex).getDateApparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_EN_COURS_DATE_DISPARITION: 
	    		if(tbAlarme.get(rowIndex).getDateDisparition() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return tbAlarme.get(rowIndex).getDateDisparition().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_EN_COURS_DATE_PRISE_EN_COMPTE: 
	    		if(tbAlarme.get(rowIndex).getDatePriseEnCompte() != null) {
		    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		    		return tbAlarme.get(rowIndex).getDatePriseEnCompte().format(formatter);
	    		} else {
	    			return "---";
	    		}
	    	case JT_ALARME_EN_COURS_VALEUR: 
	    		return tbAlarme.get(rowIndex).getValeurAPI() / 10; 
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addAlarmeEnCours(AlarmeEnCours alarmeEnCours) {
    	tbAlarme.add(alarmeEnCours);
 
        fireTableRowsInserted(tbAlarme.size() -1, tbAlarme.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeAlarmeEnCours(int rowIndex) {
    	tbAlarme.remove(rowIndex);
 
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
        	AlarmeEnCours alarmeEnCours = tbAlarme.get(rowIndex);

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
    	    	case JT_ALARME_EN_COURS_VALEUR: 
            		alarmeEnCours.setValeurAPI((Double) aValue);
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
