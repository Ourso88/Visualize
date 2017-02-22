package gui.modeles;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import em.general.EFS_General;
import em.general.JTableConstantes;
import kernel.CapteurMaintenance;

/**
 * Modele pour le JTable Capteur Maintenance
 * @author Eric Mariani
 * @since 21/02/2017
 *
 */
public class ModeleJTableCapteurMaintenance extends AbstractTableModel implements EFS_General, JTableConstantes {
	private static final long serialVersionUID = 1L;
	private final List<CapteurMaintenance> lstCapteurMaintenance = new ArrayList<CapteurMaintenance>();
    private final String[] entetes = {"Voie", "Date", "Raison", "Utilisateur"};
    
    /**
     * Constructeur
     */
    public ModeleJTableCapteurMaintenance() {
    	super();
    }
    
    /**
     * Renvoie l'Id Capteur 
     * @param rowIndex
     * @return
     */
    public long getIdCapteur(int rowIndex) {
    	return lstCapteurMaintenance.get(rowIndex).getIdCapteur();
    }

    /**
     * Renvoie le type Capteur 
     * @param rowIndex
     * @return
     */
    public int getTypeCapteur(int rowIndex) {
    	return lstCapteurMaintenance.get(rowIndex).getTypeCapteur();
    }
    
    
    /**
     * 
     * @return
     * 		Le nombre de lignes
     */
    public int getRowCount() {
        return lstCapteurMaintenance.size();
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
	    	case JT_CAPTEUR_MAINTENANCE_NOM: 
	    		return lstCapteurMaintenance.get(rowIndex).getNomCapteur(); 
	    	case JT_CAPTEUR_MAINTENANCE_DATE: 
	    		return lstCapteurMaintenance.get(rowIndex).getDateInhibition(); 
	    	case JT_CAPTEUR_MAINTENANCE_RAISON: 
	    		return lstCapteurMaintenance.get(rowIndex).getRaisonMaintenance();
	    	case JT_CAPTEUR_MAINTENANCE_UTILISATEUR: 
	    		return lstCapteurMaintenance.get(rowIndex).getUtilisateur();
	    	default:
	    		return null;
    	}
    }    
    
    /**
     * Rajoute une ligne au tableau
     */
    public void addCapteurMaintenance(CapteurMaintenance capteurMaintenance) {
    	lstCapteurMaintenance.add(capteurMaintenance);
 
        fireTableRowsInserted(lstCapteurMaintenance.size() -1, lstCapteurMaintenance.size() -1);
    }    
 
    /**
     * Retire une ligne au tableau
     * @param rowIndex
     * 		Index de la ligne
     */
    public void removeCapteurMaintenance(int rowIndex) {
    	lstCapteurMaintenance.remove(rowIndex);
 
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
//        	CapteurMaintenance capteurMaintenance = lstCapteurMaintenance.get(rowIndex);

        	switch(columnIndex){
                default :
            }
        }
    }    
    
    /**
     * Supprime toutes les lignes du tableau
     */
    public void removeAll() {
    	for(int i = this.getRowCount(); i > 0; --i) {
    		removeCapteurMaintenance(i - 1);
    	}
    }    
    

}
