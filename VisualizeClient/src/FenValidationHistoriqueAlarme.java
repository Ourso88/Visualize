import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import AE_General.AE_BarreBas;
import AE_General.AE_BarreHaut;
import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;
import EFS_Structure.StructValidationHistoriqueAlarme;

import com.toedter.calendar.JDateChooser;


public class FenValidationHistoriqueAlarme extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// Commun
	private JPanel pnlTri = new JPanel();
	private JPanel pnlCorps = new JPanel();
	private JPanel pnlInformation = new JPanel();
	private JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTri, pnlCorps);
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();	
	
	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Corps
    private ModeleValidationHistoriqueAlarme mdlValidationHistoriqueAlarme = new ModeleValidationHistoriqueAlarme();	
    private JTable tbValidationHistoriqueAlarme;
    private TableRowSorter<TableModel> sorterValidationHistoriqueAlarme;		
	
    // Informations
    private JLabel lblInfoDate = new JLabel("Date : ");
    private JLabel lblInfoDateDeb = new JLabel("Date debut : ");
    private JLabel lblInfoDateFin = new JLabel("Date fin : ");
    private JLabel lblInfoUtilisateur = new JLabel("Nom utilisateur : ");
    
    
	// Tri
    private JLabel lblDateDebutTri = new JLabel("Date début : ");
    private JDateChooser jdcDateDebut = new JDateChooser();
    private JLabel lblDateFinTri = new JLabel("Date Fin : ");
    private JDateChooser jdcDateFin = new JDateChooser();
    private JButton btnFiltrer = new JButton("Filtrer");
	
	public FenValidationHistoriqueAlarme() throws ParseException {
		super();
		build();
		remplirValidationHistoriqueAlarme();
	}

	public void build() {
	    this.setTitle("Historique Alarmes Validées");
	    this.setSize(1000, 600);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Historique Alarmes Validées");
	    pnlCorps.setBackground(new Color(167, 198, 237));
	    pnlTri.setBackground(new Color(200, 130, 66));
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	    //Place la barre de séparation a 100 px
		splCorps.setDividerLocation(100);
	    
		GridBagConstraints gbc = new GridBagConstraints();

	    // =================== Corps de l'écran =============================================
	    //On définit le layout manager
	    pnlCorps.setLayout(new BorderLayout());

        tbValidationHistoriqueAlarme = new JTable(mdlValidationHistoriqueAlarme);
        tbValidationHistoriqueAlarme.setSize(800, 400);
	    tbValidationHistoriqueAlarme.setFillsViewportHeight(true);        
	    tbValidationHistoriqueAlarme.setBackground(AE_Constantes.EFS_BLEU);

	    sorterValidationHistoriqueAlarme =  new TableRowSorter<TableModel>(tbValidationHistoriqueAlarme.getModel());
        tbValidationHistoriqueAlarme.setRowSorter(sorterValidationHistoriqueAlarme);     
        sorterValidationHistoriqueAlarme.setSortsOnUpdates(false);
	    
	    

//        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_ALARME_HISTORIQUE_VOIE).setCellRenderer(new ColorCellRenderer());
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_VALIDATION).setCellRenderer(new DateRenderer());
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_DEBUT).setCellRenderer(new DateRenderer());
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_FIN).setCellRenderer(new DateRenderer());

        tbValidationHistoriqueAlarme.setSelectionBackground(AE_Constantes.EFS_MARRON);
        tbValidationHistoriqueAlarme.getSelectionModel();
		tbValidationHistoriqueAlarme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_VALIDATION).setPreferredWidth(120);        
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_VALIDATION).setMaxWidth(120);        
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_DEBUT).setPreferredWidth(120);        
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_DEBUT).setMaxWidth(120);        
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_FIN).setPreferredWidth(120);        
        tbValidationHistoriqueAlarme.getColumnModel().getColumn(AE_Constantes.JTABLE_VALIDATION_HISTORIQUE_ALARME_DATE_FIN).setMaxWidth(120);        

//        tbValidationHistoriqueAlarme.setFont(AE_Constantes.FONT_ARIAL_20);
        
        pnlInformation.setBackground(AE_Constantes.EFS_MARRON);
        pnlInformation.add(lblInfoDate);
        pnlInformation.add(lblInfoDateDeb);
        pnlInformation.add(lblInfoDateFin);
        pnlInformation.add(lblInfoUtilisateur);
	    pnlCorps.add(pnlInformation, BorderLayout.NORTH);
        pnlCorps.add(new JScrollPane(tbValidationHistoriqueAlarme), BorderLayout.CENTER);	    
	    
	    // ==================================================================================

	    // ================= TRI ===========================================================
	    pnlTri.setLayout(new GridBagLayout());
	    
        lblDateDebutTri.setMinimumSize(new Dimension(150, 25));
        lblDateDebutTri.setPreferredSize(new Dimension(150, 25));
        lblDateFinTri.setMinimumSize(new Dimension(150, 25));
        lblDateFinTri.setPreferredSize(new Dimension(150, 25));
        
	    Calendar cal = Calendar.getInstance();
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
//        Date now = new Date();
	    cal.setTime(dateDeb);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    dateDeb = cal.getTime();
        
        jdcDateDebut.setDate(dateDeb);
        jdcDateFin.setDate(dateFin);
        
        btnFiltrer.addActionListener(this);
        
	    // --------------------------------------------------
	    gbc.insets = new Insets(0, 10, 0, 0);
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblDateDebutTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(jdcDateDebut, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 0;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblDateFinTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(jdcDateFin, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 5; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    gbc.anchor= GridBagConstraints.CENTER ;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(btnFiltrer, gbc);
	    // --------------------------------------------------	   

	    // ==================================================================================
	    
	} // Fin build

/*	
	private boolean testService(int idService) {
		// Filtre sur le service
		if(EFS_Client_Variable.niveauUtilisateur < 40) {
			for(int i = 0; i < 100; i++) {
				if((EFS_Client_Variable.tbService[i]  == idService) && idService > 0) {
					return true;
				}
			} // fin for
			return false;
		} else {
			return true;
		}
	}
*/
	
	private void remplirValidationHistoriqueAlarme() {
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
		String strDateDeb;
		String strDateFin;
		String strSql = "";
		String utilisateur = "";
//		int idService = -1;

		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 		
		
		SimpleDateFormat formaterDeb = null;
		formaterDeb = new SimpleDateFormat("dd/MM/yyyy 00:00:01");
		SimpleDateFormat formaterFin = null;
		formaterFin = new SimpleDateFormat("dd/MM/yyyy 23:59:59");
		
		lblInfoDate.setText("Date : " + formater.format(dateDeb) + "    ");

		dateDeb = jdcDateDebut.getDate();
		dateFin = jdcDateFin.getDate();
		strDateDeb = "TO_DATE('" + formaterDeb.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formaterFin.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";

		lblInfoDateDeb.setText("Date : " + formaterDeb.format(dateDeb) + "    ");
		lblInfoDateFin.setText("Date : " + formaterFin.format(dateFin) + "    ");
		
		lblInfoUtilisateur.setText("Nom : " + EFS_Client_Variable.nomUtilisateur + " " + EFS_Client_Variable.prenomUtilisateur);
		
		// Vider le tableau
		mdlValidationHistoriqueAlarme.removeAllvalidationHistoriqueAlarme();
		
		ctn.open();
		// Création du filtre
		strSql = "SELECT ValidationHistoriqueAlarme.*, Utilisateur.Nom AS NomUtilisateur, Utilisateur.Prenom AS PrenomUtilisateur FROM (ValidationHistoriqueAlarme" 
			   + " LEFT JOIN Utilisateur ON ValidationHistoriqueAlarme.idUtilisateur = Utilisateur.idUtilisateur)"
     		   + " WHERE DateValidation >= " + strDateDeb + " AND DateValidation <= " + strDateFin; 
		
		strSql += " ORDER BY DateValidation DESC";

		ResultSet result = ctn.lectureData(strSql);
		try {
			while(result.next()) {
				utilisateur = result.getString("NomUtilisateur") + " " + result.getString("PrenomUtilisateur");
				mdlValidationHistoriqueAlarme.addvalidationHistoriqueAlarme(new StructValidationHistoriqueAlarme(result.getDate("DateValidation"), result.getDate("DateDebut")
						, result.getDate("DateFin"), utilisateur));
			} // Fin while result.next()
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Probléme ValidationHistoriqueAlarme");
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ctn.close();
		
	} // Fin remplirValidationHistoriqueAlarme

	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnFiltrer) {
			remplirValidationHistoriqueAlarme();
		}

	}
	
	
}
