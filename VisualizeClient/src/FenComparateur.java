import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import AE_General.AE_BarreBas;
import AE_General.AE_BarreHaut;
import AE_General.AE_ConnectionBase;
import AE_General.AE_Constantes;
import AE_General.EFS_Client_Variable;
import AE_General.EFS_Constantes;


public class FenComparateur extends JFrame implements AE_Constantes, EFS_Constantes, ActionListener {
	private static final long serialVersionUID = 1L;

	private AE_ConnectionBase ctn = new AE_ConnectionBase(AE_Constantes.EFS_SGBD_TYPE, EFS_Client_Variable.EFS_SGBD_SERVEUR, EFS_Client_Variable.EFS_SGBD_BASE, EFS_Client_Variable.EFS_SGBD_USER, EFS_Client_Variable.EFS_SGBD_MDP);	

	private JPanel pnlCorps = new JPanel();
	private JPanel pnlCommande = new JPanel();
	private JSplitPane splCorps = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlCommande, pnlCorps);
	private AE_BarreBas pnlInfo = new AE_BarreBas();
	private AE_BarreHaut pnlHaut = new AE_BarreHaut();
	
	private JButton btnChoixFichier = new JButton("Choix fichier");
	private JButton btnComparer = new JButton("Comparer");
	
	private File ficExcel;
	private String ficSortie;
	
	public FenComparateur() {
		super();
		ctn.open();
		build();
	} // fin FenComparateur()
	
	
	private void build() {
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	ctn.close();
            }
        });
		
	    this.setTitle("Comparateur fichier Excel");
	    this.setSize(1000, 800);
		this.setResizable(true);
	    this.setLocationRelativeTo(null);
		
	    btnChoixFichier.addActionListener(this);
	    btnComparer.addActionListener(this);
	    pnlCommande.add(btnChoixFichier);
	    pnlCommande.add(btnComparer);
	    
	    pnlCommande.setBackground(EFS_MARRON);
	    pnlCorps.setBackground(EFS_BLEU);
	    
	    this.add("North", pnlHaut);
	    this.add("Center", splCorps);
	    this.add("South", pnlInfo);		
		
		pnlHaut.setTitreEcran("Comparateur fichier Excel");
	    
	    
	} // fin build()

	private void gestionChoixFichier() {
		int pos = 0;
		JFileChooser diaChoix = new JFileChooser(new File("."));
		
		if(diaChoix.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			ficExcel = diaChoix.getSelectedFile();

			ficSortie = ficExcel.getPath();			
			pos = ficSortie.indexOf('.', 0);
			ficSortie = ficSortie.substring(0, pos);
			ficSortie += "_Comp.xls";
		}
		
	} // fin gestionChoixFichier()
	
	private void gestionComparaison() {
		String nomCapteur = null;
		String descriptionCapteur = null;
		String inventaireCapteur = null;
		ResultSet result;
		String msgErreur = "";
		
		int numVoie = -1;
//		int idCapteur = -1;
		int seuilHaut = 0;
		int seuilBas = 0;
		int temporisation = 0;
//		int calibration = 0;

		int alarme = -1;
		String strAlarme;
		int noNf = -1;
		String strNoNf;
		
//		int idService = -1;
//		String nomService;

//		int idZoneSubstitution = -1;
//		String nomZoneSubstitution;
		
		if(ficExcel != null) {
			try {
				FileOutputStream fileOut;
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(ficExcel));
				
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheetAna = wb.getSheetAt(0);
				HSSFSheet sheetDigi = wb.getSheetAt(1);
				HSSFRow row = null;
				HSSFCell cellVoie = null;
				HSSFCell cellDescription = null;
				HSSFCell cellInventaire = null;
				HSSFCell cellSeuilBas = null;
				HSSFCell cellSeuilHaut = null;
				HSSFCell cellTempo = null;
//				HSSFCell cellCalibration = null;
				HSSFCell cellAlarme = null;
				HSSFCell cellNoNf = null;
				HSSFCellStyle cellStyle = null;

				// Style fond rouge pour cellule 
				cellStyle = wb.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.RED.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				// =============== Analogique ======================================
				for (Iterator<?> rowIt = sheetAna.rowIterator(); rowIt.hasNext();) {
					row = (HSSFRow) rowIt.next();
					if (row.getRowNum() > 0) {
						cellVoie = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_VOIE);
						if (cellVoie != null) {
							cellDescription = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_DESCRIPTION);
							if (cellDescription != null) {
								descriptionCapteur = cellDescription.getStringCellValue();
							} else {
								descriptionCapteur = null;
							}
							cellInventaire = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_INVENTAIRE);
							if (cellInventaire != null) {
								if (cellInventaire.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									inventaireCapteur = Long.toString((long)cellInventaire.getNumericCellValue());
								} else {
									inventaireCapteur = cellInventaire.getStringCellValue();
								}
							} else {
								inventaireCapteur = null;
							}
							cellSeuilHaut = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_HAUT);
							seuilHaut = 0;
							if (cellSeuilHaut != null) {
								seuilHaut = (int)(cellSeuilHaut.getNumericCellValue() * 10);
							}
							cellSeuilBas = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_BAS);
							seuilBas = 0;
							if (cellSeuilBas != null) {
								seuilBas = (int)(cellSeuilBas.getNumericCellValue() * 10);
							}
							cellTempo = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_TEMPO);
							temporisation = 0;
							if (cellTempo != null) {
								temporisation = (int)cellTempo.getNumericCellValue();
							}
							cellAlarme = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_ALARME);
							alarme = AE_Constantes.ALARME_RIEN;
							if (cellAlarme != null) {
								strAlarme = cellAlarme.getStringCellValue();
								if (strAlarme.equals("Alarme")) {
									alarme = AE_Constantes.ALARME_ALERT;
								}
								if (strAlarme.equals("Défaut")) {
									alarme = AE_Constantes.ALARME_DEFAUT;
								}
								if (strAlarme.equals("Etat")) {
									alarme = AE_Constantes.ALARME_ETAT;
								}
							} // Fin if Alarme
							
							msgErreur = "";
							if(!descriptionCapteur.equals("-")) {
								numVoie = (int) cellVoie.getNumericCellValue();
								nomCapteur = "A" + String.valueOf(Integer.valueOf((int) cellVoie.getNumericCellValue()));
								// Recherche si existe dans base si non enregistre
								result = ctn.lectureData("SELECT Capteur.*, EntreeAnalogique.*, Equipement.Nom AS Inventaire FROM "
										+ "((EntreeAnalogique LEFT JOIN Capteur ON EntreeAnalogique.idCapteur "
										+ "= Capteur.idCapteur) "
										+ "LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement) "
										+ "WHERE Capteur.Nom = '" + nomCapteur + "'");
								try {
									if(result.next()) {
//										System.out.println("Voie : " + numVoie );
										// Verifier correspondance entre les champs
										if (!descriptionCapteur.equals(result.getString("Description"))) {
											if (cellDescription != null) {
												cellDescription.setCellStyle(cellStyle);
												msgErreur += " Description ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_ANA_DESCRIPTION).setCellValue("-- Pb --");											
												cellDescription = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_DESCRIPTION);
												cellDescription.setCellStyle(cellStyle);
												msgErreur += " Description ";
											}
										} // Fin if description

										if(inventaireCapteur != null) {
											if (!inventaireCapteur.equals(result.getString("Inventaire"))) {
												if (cellInventaire != null) {
													cellInventaire.setCellStyle(cellStyle);
													msgErreur += " Inventaire ";
												}
												else {
													row.createCell(EFS_COMPARATEUR_COLONNE_ANA_INVENTAIRE).setCellValue("-- Pb --");											
													cellInventaire = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_INVENTAIRE);
													cellInventaire.setCellStyle(cellStyle);
													msgErreur += " Inventaire ";
												}
											} // Fin if inventaire
										}
										
										if (seuilHaut != result.getInt("SeuilHaut")) {
											if (cellSeuilHaut != null) {
												cellSeuilHaut.setCellStyle(cellStyle);
												msgErreur += " Seuil haut ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_HAUT).setCellValue("-- Pb --");											
												cellSeuilHaut = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_HAUT);
												cellSeuilHaut.setCellStyle(cellStyle);
												msgErreur += " Seuil haut ";
											}
										} // Fin if seuilHaut
										if (seuilBas != result.getInt("SeuilBas")) {
											if (cellSeuilBas != null) {
												cellSeuilBas.setCellStyle(cellStyle);
												msgErreur += " Seuil bas ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_BAS).setCellValue("-- Pb --");											
												cellSeuilBas = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_BAS);
												cellSeuilBas.setCellStyle(cellStyle);
												msgErreur += " Seuil bas ";
											}
										} // Fin if seuilBas
										if (temporisation != result.getInt("SeuilTempo")) {
											if (cellTempo != null) {
												cellTempo.setCellStyle(cellStyle);
												msgErreur += " Seuil tempo ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_TEMPO).setCellValue("-- Pb --");											
												cellTempo = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_SEUIL_TEMPO);
												cellTempo.setCellStyle(cellStyle);
												msgErreur += " Seuil tempo ";
											}
										} // Fin if tempo
										if (alarme != result.getInt("Alarme")) {
											if (cellAlarme != null) {
												cellAlarme.setCellStyle(cellStyle);
												msgErreur += " Alarme ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_ANA_ALARME).setCellValue("-- Pb --");											
												cellAlarme = row.getCell(EFS_COMPARATEUR_COLONNE_ANA_ALARME);
												cellAlarme.setCellStyle(cellStyle);
												msgErreur += " Alarme ";
											}
										} // Fin if alarme
									}
									else {
										cellVoie.setCellStyle(cellStyle);
										msgErreur += " non trouvée ";
									} // Fin if
									result.close();
								} catch (SQLException e) {
									e.printStackTrace();
								} // Fin try
							} // Fin if
						}
						else {
//							System.out.println("voie (zone vide) : " + cellVoie.getNumericCellValue());
						} // fin if
						if (!msgErreur.equals("")) {
							msgErreur = " Voie A " + cellVoie + " - " + msgErreur;
							System.out.println(msgErreur);
						}
					} // Fin if 
				} // Fin for Analogique
				
				// =============== Digitale ======================================
				for (Iterator<?> rowIt = sheetDigi.rowIterator(); rowIt.hasNext();) {
					row = (HSSFRow) rowIt.next();
					if (row.getRowNum() > 0) {
						cellVoie = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_VOIE);
						if (cellVoie != null) {
							cellDescription = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_DESCRIPTION);
							if(cellDescription != null) {
								descriptionCapteur = cellDescription.getStringCellValue();
							} else {
								descriptionCapteur = null;
							}
							cellInventaire = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_INVENTAIRE);
							if (cellInventaire != null) {
								if (cellInventaire.getCellType() == Cell.CELL_TYPE_NUMERIC) {
									inventaireCapteur = Long.toString((long)cellInventaire.getNumericCellValue());
								} else {
									inventaireCapteur = cellInventaire.getStringCellValue();
								}
							} else {
								inventaireCapteur = null;
							}

							cellNoNf = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_NONF);
							noNf = 0;
							if (cellNoNf != null) {
								strNoNf = cellNoNf.getStringCellValue();
								if (strNoNf.equals("NF")) {
									noNf = AE_Constantes.CAPTEUR_DIGITAL_NF;
								}
								if (strNoNf.equals("NO")) {
									noNf = AE_Constantes.CAPTEUR_DIGITAL_NO;
								}
							}
							
							cellAlarme = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_ALARME);
							alarme = AE_Constantes.ALARME_RIEN;
							if (cellAlarme != null) {
								strAlarme = cellAlarme.getStringCellValue();
								if (strAlarme.equals("Alarme")) {
									alarme = AE_Constantes.ALARME_ALERT;
								}
								if (strAlarme.equals("Défaut")) {
									alarme = AE_Constantes.ALARME_DEFAUT;
								}
							} // Fin if Alarme

							cellTempo = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_TEMPO);
							temporisation = 0;
							if (cellTempo != null) {
								temporisation = (int)cellTempo.getNumericCellValue();
							}
							
							msgErreur = "";
							if(!descriptionCapteur.equals("-")) {
								numVoie = (int) cellVoie.getNumericCellValue();
								nomCapteur = "D" + String.valueOf(Integer.valueOf((int) cellVoie.getNumericCellValue()));
								// Recherche si existe dans base si non enregistre
								result = ctn.lectureData("SELECT Capteur.*, EntreeDigitale.*, Equipement.Nom AS Inventaire FROM "
										+ "((EntreeDigitale LEFT JOIN Capteur ON EntreeDigitale.idCapteur "
										+ "= Capteur.idCapteur) "
										+ "LEFT JOIN Equipement ON Capteur.idEquipement = Equipement.idEquipement) "
										+ "WHERE Capteur.Nom = '" + nomCapteur + "'");
								try {
									if(result.next()) {
										// Verifier correspondance entre les champs
										if (!descriptionCapteur.equals(result.getString("Description"))) {
											if (cellDescription != null) {
												cellDescription.setCellStyle(cellStyle);
												msgErreur += " Description ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_DIGI_DESCRIPTION).setCellValue("-- Pb --");											
												cellDescription = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_DESCRIPTION);
												cellDescription.setCellStyle(cellStyle);
												msgErreur += " Description ";
											}
										} // Fin if description
										if(inventaireCapteur != null) {
											if (!inventaireCapteur.equals(result.getString("Inventaire"))) {
												if (cellInventaire != null) {
													cellInventaire.setCellStyle(cellStyle);
													msgErreur += " Inventaire ";
												}
												else {
													row.createCell(EFS_COMPARATEUR_COLONNE_DIGI_INVENTAIRE).setCellValue("-- Pb --");											
													cellInventaire = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_INVENTAIRE);
													cellInventaire.setCellStyle(cellStyle);
													msgErreur += " Inventaire ";
												}
											} // Fin if inventaire
										}
										
										if (temporisation != result.getInt("Tempo")) {
											if (cellTempo != null) {
												cellTempo.setCellStyle(cellStyle);
												msgErreur += " Tempo ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_DIGI_TEMPO).setCellValue("-- Pb --");											
												cellTempo = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_TEMPO);
												cellTempo.setCellStyle(cellStyle);
												msgErreur += " Tempo ";
											}
										} // Fin if tempo
										if (alarme != result.getInt("Alarme")) {
											if (cellAlarme != null) {
												cellAlarme.setCellStyle(cellStyle);
												msgErreur += " Alarme ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_DIGI_ALARME).setCellValue("-- Pb --");											
												cellAlarme = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_ALARME);
												cellAlarme.setCellStyle(cellStyle);
												msgErreur += " Alarme ";
											}
										} // Fin if alarme
										if (noNf != result.getInt("NoNf")) {
											if (cellNoNf != null) {
												cellNoNf.setCellStyle(cellStyle);
												msgErreur += " NoNf ";
											}
											else {
												row.createCell(EFS_COMPARATEUR_COLONNE_DIGI_NONF).setCellValue("-- Pb --");											
												cellAlarme = row.getCell(EFS_COMPARATEUR_COLONNE_DIGI_NONF);
												cellAlarme.setCellStyle(cellStyle);
												msgErreur += " NoNf ";
											}
										} // Fin if noNf
									}
									else {
										cellVoie.setCellStyle(cellStyle);
										System.out.println("Voie Digitale non trouvée : " + numVoie );
									} // Fin if
									if (!msgErreur.equals("")) {
										msgErreur = " Voie D " + cellVoie + " - " + msgErreur;
										System.out.println(msgErreur);
									}
									result.close();
								} catch (SQLException e) {
									e.printStackTrace();
								} // Fin try
							} // Fin if
						}
						else {
//							System.out.println("voie (zone vide) : " + cellVoie.getNumericCellValue());
						} // fin if
					} // Fin if
				} // Fin for Digitale				
				
				
				fileOut = new FileOutputStream(ficSortie);
				wb.write(fileOut);
			    fileOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // Fin try	
		} 
	} // fin gestionComparaison()
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == btnChoixFichier) {
			// Choix du fichier à traiter
			gestionChoixFichier();
		}
		if(ae.getSource() == btnComparer) {
			gestionComparaison();
		}
		
		
	} // fin actionPerformed
	
	
	
} // fin class
