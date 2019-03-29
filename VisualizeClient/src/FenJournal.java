import java.awt.BorderLayout;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import AE_General.AE_BarreBas;
import AE_General.AE_BarreHaut;
import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;
import EFS_Structure.StructCapteur;
import EFS_Structure.StructTypeJournal;
import EFS_Structure.StructUtilisateur;


public class FenJournal extends JFrame implements AE_General.AE_Constantes, ActionListener {
	private static final long serialVersionUID = 1L;
	// Commun
	JPanel pnlTri = new JPanel();
	JPanel pnlCorps = new JPanel();
	JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTri, pnlCorps);
	AE_BarreBas pnlInfo = new AE_BarreBas();
	AE_BarreHaut pnlHaut = new AE_BarreHaut();

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	
	
	// Corps
    private ModeleJournal mdlJournal = new ModeleJournal();	
    private JTable tbJournal;
    TableRowSorter<TableModel> sorterJournal;	

	// Tri
    JLabel lblDateDebutTri = new JLabel("Date début : ");
    JDateChooser jdcDateDebut = new JDateChooser();
    JLabel lblDateFinTri = new JLabel("Date Fin : ");
    JDateChooser jdcDateFin = new JDateChooser();
    JLabel lblCapteurTri = new JLabel("Capteur : ");    
    JLabel lblTypeActionTri = new JLabel("Type action journal : ");    
    JLabel lblUtilisateurTri = new JLabel("Utilisateur : ");    

	private String[] ptTous = {"Tous"}; 
	private JComboBox<String> cmbTypeJournal = new JComboBox<String>(ptTous);    
	private List<StructTypeJournal> tbTypeJournal = new ArrayList<StructTypeJournal>();	
	private List<StructUtilisateur> tbUtilisateur = new ArrayList<StructUtilisateur>();	
	private JComboBox<String> cmbUtilisateur = new JComboBox<String>(ptTous);    
	private List<StructCapteur> tbCapteur = new ArrayList<StructCapteur>();	
	private JComboBox<String> cmbCapteur = new JComboBox<String>(ptTous);    
	
	private JButton btnFiltre = new JButton("Filtre");
	
	public FenJournal() throws ParseException {
		super();
		build();
		lectureCapteur();
		lectureUtilisateur();
		lectureTypeJournal();
		remplirListeJournal();
	}

	public void build() {
		
		
	    this.setTitle("Journal");
	    this.setSize(900, 800);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Journal");
	    pnlCorps.setBackground(EFS_BLEU);
	    pnlTri.setBackground(EFS_MARRON);
	    pnlInfo.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	    //Place la barre de séparation a 200 px
		splCorps.setDividerLocation(200);
	    

	    // =================== Corps de l'écran =============================================
		
	    pnlCorps.setLayout(new BorderLayout());

        tbJournal = new JTable(mdlJournal);
        tbJournal.setSize(800, 400);
        tbJournal.setFillsViewportHeight(true);        
        tbJournal.setBackground(EFS_BLEU);
        sorterJournal =  new TableRowSorter<TableModel>(tbJournal.getModel());
        tbJournal.setRowSorter(sorterJournal);        
        sorterJournal.setSortable(0,  false);
        sorterJournal.setSortsOnUpdates(true);
        
        tbJournal.getColumnModel().getColumn(AE_Constantes.JTABLE_JOURNAL_DATE).setPreferredWidth(120);        
        tbJournal.getColumnModel().getColumn(AE_Constantes.JTABLE_JOURNAL_DATE).setMaxWidth(120);        
        tbJournal.getColumnModel().getColumn(AE_Constantes.JTABLE_JOURNAL_VOIE).setPreferredWidth(40);        
        tbJournal.getColumnModel().getColumn(AE_Constantes.JTABLE_JOURNAL_VOIE).setMaxWidth(40);        
        
        
        pnlCorps.add(new JScrollPane(tbJournal), BorderLayout.CENTER);		
		
	    // ==================================================================================
        
	    // =================== Tri ============================================
        btnFiltre.addActionListener(this);
        
        lblDateDebutTri.setMinimumSize(new Dimension(150, 25));
        lblDateDebutTri.setPreferredSize(new Dimension(150, 25));
        lblDateFinTri.setMinimumSize(new Dimension(150, 25));
        lblDateFinTri.setPreferredSize(new Dimension(150, 25));
	    
	    Calendar cal = Calendar.getInstance();
	    Date dateDeb = new Date();
	    Date dateFin = new Date();
	    cal.setTime(dateDeb);
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    dateDeb = cal.getTime();
        
        jdcDateDebut.setDate(dateDeb);
        jdcDateFin.setDate(dateFin);
        
	    pnlTri.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

	    gbc.insets = new Insets(0, 10, 0, 0);
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 0;
	    gbc.anchor= GridBagConstraints.CENTER ; gbc.fill = GridBagConstraints.HORIZONTAL;
	    pnlTri.add(lblDateDebutTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 0; gbc.gridy = 1;
	    pnlTri.add(jdcDateDebut, gbc);
	    
	    // --------------------------------------------------        
	    gbc.gridx = 1; gbc.gridy = 0;
	    pnlTri.add(lblDateFinTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 1; gbc.gridy = 1;
	    pnlTri.add(jdcDateFin, gbc);
	    // --------------------------------------------------
	    
	    gbc.gridx = 2; gbc.gridy = 0;
	    pnlTri.add(lblTypeActionTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 2; gbc.gridy = 1;
	    pnlTri.add(cmbTypeJournal, gbc);

	    // --------------------------------------------------        
	    gbc.gridx = 3; gbc.gridy = 0;
	    pnlTri.add(lblUtilisateurTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 3; gbc.gridy = 1;
	    pnlTri.add(cmbUtilisateur, gbc);
	    // --------------------------------------------------
	    
	    gbc.gridx = 4; gbc.gridy = 0;
	    pnlTri.add(lblCapteurTri, gbc);
	    // --------------------------------------------------
	    gbc.gridx = 4; gbc.gridy = 1;
	    gbc.gridheight = 1; gbc.gridwidth = 1;
	    pnlTri.add(cmbCapteur, gbc);
	    // --------------------------------------------------        

	    gbc.gridx = 5; gbc.gridy = 1;
	    pnlTri.add(btnFiltre, gbc);
	    // --------------------------------------------------
	}

	private void remplirListeJournal() {
		String strSql = "";

		Date dateDeb = new Date();
	    Date dateFin = new Date();
		String strDateDeb = "";
		String strDateFin = "";
		
		String dateJournal = "";
		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 		
		
		dateDeb = jdcDateDebut.getDate();
		dateFin = jdcDateFin.getDate();
		strDateDeb = "TO_DATE('" + formater.format(dateDeb) + "', 'DD/MM/YYYY HH24:MI:SS')";
		strDateFin = "TO_DATE('" + formater.format(dateFin) + "', 'DD/MM/YYYY HH24:MI:SS')";
		
		ctn.open();
		
		mdlJournal.removeAll();
		strSql = "SELECT Journal.DateJournal, Capteur.Nom, Capteur.Description, Utilisateur.Nom AS nomUtilisateur,"
				+ " Journal.Description AS Action, TypeJournal.Description AS TypeAction" 
				+ " FROM (((Journal LEFT JOIN Capteur ON Journal.idCapteur = Capteur.idCapteur)"
				+ " LEFT JOIN Utilisateur ON Journal.idUtilisateur = Utilisateur.idUtilisateur)"
				+ " LEFT JOIN TypeJournal ON Journal.TypeJournal = TypeJournal.NumeroTypeJournal)"
				+ " WHERE DateJournal >= " + strDateDeb + " AND DateJournal <= " + strDateFin;				
		if(cmbCapteur.getSelectedIndex() > 0) {
			strSql += " AND Capteur.idCapteur = " + tbCapteur.get(cmbCapteur.getSelectedIndex()-1).getId();
		}
		if(cmbUtilisateur.getSelectedIndex() > 0) {
			strSql += " AND Journal.idUtilisateur = " + tbUtilisateur.get(cmbUtilisateur.getSelectedIndex()-1).getId();
		}
		if(cmbTypeJournal.getSelectedIndex() > 0) {
			strSql += " AND Journal.TypeJournal = " + tbTypeJournal.get(cmbTypeJournal.getSelectedIndex()-1).getNumeroTypeJournal();
		}
				
		strSql += " ORDER BY DateJournal DESC";
		
		ResultSet result = ctn.lectureData(strSql);

		// Remplissage de la liste
		try {
			while(result.next()) {
				dateJournal = formater.format(result.getDate("DateJournal"));
				mdlJournal.addJournal(new TypeJournal(dateJournal, result.getString("TypeAction"), result.getString("nomUtilisateur")
						, result.getString("Nom"), result.getString("Description"), result.getString("Action")));
				
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
		
		
	} // Fin RemplirListeJournal	

	private void lectureCapteur() {
		int index = 0;
		
		// Connection base
		ctn.open();
		
		ResultSet result = ctn.lectureData("SELECT * FROM Capteur ORDER BY TypeCapteur, VoieApi ASC");
		index = 0;
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				tbCapteur.add(new StructCapteur(result.getLong("idCapteur"), result.getString("Nom"), result.getString("Description"), 
						result.getInt("idEquipement"), result.getInt("idPosteTechnique"), result.getInt("idTypeMateriel"),
						 result.getInt("idZoneSubstitution"),  result.getInt("TypeCapteur"),  result.getInt("idService"),
						 result.getInt("Alarme"),  result.getInt("VoieApi"),  result.getInt("Inhibition")));
				
				cmbCapteur.addItem(String.format("%-8s", tbCapteur.get(index).getNom()) + tbCapteur.get(index).getDescription());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.close();
		
	} // Fin LectureDonnees	

	private void lectureTypeJournal() {
		int index = 0;
		
		// Connection base
		ctn.open();
		
		ResultSet result = ctn.lectureData("SELECT * FROM TypeJournal");
		index = 0;
		try {
			while(result.next()) {
				tbTypeJournal.add(new StructTypeJournal(result.getInt("NumeroTypeJournal"), result.getString("Description")));
				cmbTypeJournal.addItem(tbTypeJournal.get(index).getDescription());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ctn.closeLectureData();
		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.close();
		
	} // Fin LectureDonnees		
	
	private void lectureUtilisateur() {
		int index = 0;
		// Connection base
		ctn.open();
		
		ResultSet result = ctn.lectureData("SELECT * FROM Utilisateur");
		index = 0;
		// Remplissage tableau et combo Utilisateur
		try {
			while(result.next()) {
				tbUtilisateur.add(new StructUtilisateur(result.getLong("idUtilisateur"), result.getString("Nom"), result.getString("Prenom")
						          , result.getString("Login"), result.getString("MotDePasse"), result.getLong("idNiveauUtilisateur")
						          , result.getLong("idAlarmeService")));
				cmbUtilisateur.addItem(tbUtilisateur.get(index).getNom() + " " + tbUtilisateur.get(index).getPrenom());
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fermeture base
		ctn.close();
		
	} // Fin lectureUtilisateur

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnFiltre) {
			remplirListeJournal();
		}
	}
	
} // Fin class




