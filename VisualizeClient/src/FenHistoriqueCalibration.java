import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import AE_General.*;

public class FenHistoriqueCalibration extends JFrame implements AE_General.AE_Constantes {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlCorps = new JPanel();
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	

	// Corps
    private ModeleHistoriqueCalibration mdlHistoriqueCalibration = new ModeleHistoriqueCalibration();	
    private JTable tblHistoriqueCalibration;
    TableRowSorter<TableModel> sorter;	
	
	public FenHistoriqueCalibration() throws ParseException {
		super();
		build();
		remplirHistoriqueCalibration();
	}

	public void build() {
	    this.setTitle("Historique calibration");
	    this.setSize(1000, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", pnlCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Historique calibration");
	    pnlCorps.setBackground(EFS_BLEU);
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    
	    pnlCorps.setLayout(new BorderLayout());

        tblHistoriqueCalibration = new JTable(mdlHistoriqueCalibration);
        tblHistoriqueCalibration.setSize(800, 400);
        tblHistoriqueCalibration.setFillsViewportHeight(true);        
        tblHistoriqueCalibration.setBackground(EFS_BLEU);
        sorter =  new TableRowSorter<TableModel>(tblHistoriqueCalibration.getModel());
        tblHistoriqueCalibration.setRowSorter(sorter);        
        sorter.setSortable(0,  false);
        sorter.setSortsOnUpdates(true);
        
        pnlCorps.add(new JScrollPane(tblHistoriqueCalibration), BorderLayout.CENTER);	    
	    
	}
	
	private void remplirHistoriqueCalibration() {
		String strSql = "";
		
		String dateModification = "";
		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 		
		
		ctn.open();
		
		strSql = "SELECT HistoriqueCalibration.*, Capteur.Nom AS voie, Utilisateur.Nom AS nomUtilisateur,"
			   + " Utilisateur.Prenom AS prenomUtilisateur FROM"
			   + " ((HistoriqueCalibration LEFT JOIN Capteur ON HistoriqueCalibration.idCapteur = Capteur.idCapteur) "
			   + " LEFT JOIN Utilisateur ON HistoriqueCalibration.idUtilisateur = Utilisateur.idUtilisateur)"	
			   + " ORDER BY DateModification DESC";
		
		ResultSet result = ctn.lectureData(strSql);

		// Remplissage de la liste
		try {
			while(result.next()) {
				dateModification = formater.format(result.getDate("DateModification"));
				mdlHistoriqueCalibration.addHistoriqueCalibration(new TypeHistoriqueCalibration(result.getString("voie"), dateModification,
					result.getString("nomUtilisateur") + " " + result.getString("prenomUtilisateur"), 
					result.getDouble("AncienneValeur") / 10, result.getDouble("NouvelleValeur") / 10));
			} // fin while
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.closeLectureData();
		ctn.close();
	} // Fin RemplirHistoriqueCalibration	
} // fin class
