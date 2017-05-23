import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import AE_General.AE_Constantes;


public class ModeleHistoriqueAlarme extends AbstractTableModel  {
	private static final long serialVersionUID = 1L;
	private final List<HistoriqueAlarme> alarmes = new ArrayList<HistoriqueAlarme>();
    private final String[] entetes = {"Voie", "Description", "Apparition", "Disparition", 
    		"Prise en compte", "Alarme description", "Raison", "Utilisateur", "Commentaire"};
    
    public ModeleHistoriqueAlarme() throws ParseException {
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

    public int getIdAlarmeHistorique(int rowIndex) {
    	return alarmes.get(rowIndex).getIdAlarmeHistorique();
    } 
    
    public void setMotifIdPriseEncompte(int rowIndex, int motifIdPriseEnCompte) {
    	alarmes.get(rowIndex).setMotifIdPriseEncompte(motifIdPriseEnCompte);
    }
    
    public void setRaison(int rowIndex, String raison) {
    	alarmes.get(rowIndex).setRaison(raison);
    }
    
    public void setCommentairePriseEnCompte(int rowIndex, String commentairePriseEnCompte) {
    	alarmes.get(rowIndex).setCommentairePriseEnCompte(commentairePriseEnCompte);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
    	switch(columnIndex) {
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE:
	    		return alarmes.get(rowIndex).getNom(); 
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_DESCRIPTION_VOIE:
	    		return alarmes.get(rowIndex).getDescription();
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_APPARITION:
	    		return alarmes.get(rowIndex).getApparition();
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_DISPARITION:
	    		return alarmes.get(rowIndex).getDisparition(); 
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_PRISE_EN_COMPTE:
	    		return alarmes.get(rowIndex).getAcquittement();
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_DESCRIPTION_ALARME:
	    		return alarmes.get(rowIndex).getDescriptionAlarme(); 
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_RAISON_ACQUITTEMENT:
	    		return alarmes.get(rowIndex).getRaison(); 
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_UTILISATEUR:
	    		return alarmes.get(rowIndex).getUtilisateur(); 
	    	case AE_Constantes.JTABLE_ALARME_HISTORIQUE_COMMENTAIRE_PRISE_EN_COMPTE:
	    		return alarmes.get(rowIndex).getCommentairePriseEnCompte(); 
	    	default:
	    		return null;
	    		
    	}
    	
    }    
    
    public void addAlarme(HistoriqueAlarme alarme) {
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
    
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
    	if (columnIndex == AE_Constantes.JTABLE_ALARME_HISTORIQUE_COMMENTAIRE_PRISE_EN_COMPTE) {
    		return true; 
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
            case AE_Constantes.JTABLE_ALARME_HISTORIQUE_COMMENTAIRE_PRISE_EN_COMPTE:
            	System.out.println("Message EM -- Passage setValueAT");
            	break;
            default :
            	
            }
        }
    }    
    
}
