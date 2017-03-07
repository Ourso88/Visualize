package gui.vues;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;


public class PnlDessineCourbe extends JPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private double tbX[];
	private double tbY[];
	private int maxPoint; 
	
	private double maxX;
	private double maxY;
	private double minX;
	private double minY;
	private double seuilBas;
	private double seuilHaut;
	private double seuilCentral;
	
	
	private double decalageY;

	public int axeZeroX = 0;
	public int axeZeroY = 0;
	public int axeAbscisseX = 0;
	public int axeAbscisseY = 0;
	public int axeOrdonneX = 0;
	public int axeOrdonneY = 0;
	
	private int deltaGauche = 40;
	private int deltaBas = 50;
	private int deltaDroit = 30;
	private int deltaHaut = 25;
	private int decalage_ecriture_gauche = 4;
	
	private int nbPointX = 0;
	private int nbPointY = 0;
	private double echelleX;
	private double echelleY;
	
	private int largeurPanel = 0;
	private int hauteurPanel = 0;
	private Dimension taillePanel = new Dimension();
	
	private boolean blDessine;
	
	private int x, x1, y, y1;
	private int reticuleX;
	private int reticuleY;
	private double valeurReticuleX;
	private double valeurReticuleY;
    private Calendar cal = Calendar.getInstance();

    private Date dateDeb = new Date();
    private Date dateFin = new Date();
	private Date dateCalcul = new Date();
	
	private String affInfo = "";
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		addMouseMotionListener(this);

		SimpleDateFormat formater = null;
		formater = new SimpleDateFormat("dd/MM HH:mm:ss"); 		
		
		taillePanel = this.getSize();
		hauteurPanel =  taillePanel.height;
		largeurPanel = taillePanel.width;
		
		axeZeroX = deltaGauche;
		axeZeroY = hauteurPanel - deltaBas;
		
		axeAbscisseX = largeurPanel - deltaDroit; 
		axeAbscisseY = axeZeroY; 
		
		axeOrdonneX = axeZeroX;
		axeOrdonneY = deltaHaut;

		nbPointX =  axeAbscisseX - axeZeroX;
		nbPointY = axeZeroY - axeOrdonneY;
		echelleX = Math.abs((double) nbPointX / (double) maxX);
		echelleY = Math.abs((double) nbPointY / (double) (maxY - minY));

		decalageY = 0 - minY;
		
		// Tracé des axes
		g.setColor(Color.RED);
		g.drawLine(axeZeroX, axeZeroY, axeAbscisseX, axeAbscisseY);
		g.drawLine(axeZeroX, axeZeroY, axeOrdonneX, axeOrdonneY);
		
		// Tracé des informations
		x = decalage_ecriture_gauche; y = axeOrdonneY + 3;
		affInfo = String.valueOf(arrondi(maxY, 1));
		g.drawString(affInfo, x, y);
		x = decalage_ecriture_gauche; y = axeZeroY + 3;
		affInfo = String.valueOf(arrondi(minY, 1));
		g.drawString(affInfo, x, y);
		affInfo = formater.format(dateDeb);
		x = axeZeroX - 20; y = axeZeroY + 40;
		g.drawString(affInfo, x, y);
		affInfo = formater.format(dateFin);
		x = axeAbscisseX - 70; y = axeZeroY + 40;
		g.drawString(affInfo, x, y);
		
		
		// Tracé des seuils
		// Seuil Bas
		g.setColor(Color.BLUE);
		x = axeZeroX; x1 = axeAbscisseX;
    	y = (int) (axeZeroY - ((seuilBas + decalageY) * echelleY)); y1 = y;
		g.drawLine(x, y, x1, y1);
		x = decalage_ecriture_gauche; y = y + 5; 
		affInfo = String.valueOf(arrondi(seuilBas, 1));
		g.drawString(affInfo, x, y);
		// Seuil Haut 
		x = axeZeroX; x1 = axeAbscisseX;
		y = (int) (axeZeroY - ((seuilHaut + decalageY) * echelleY)); y1 = y;
		g.drawLine(x, y, x1, y1);
		x = decalage_ecriture_gauche; y = y + 5; 
		affInfo = String.valueOf(arrondi(seuilHaut, 1));
		g.drawString(affInfo, x, y);
		// Entre Seuil (central)
		g.setColor(Color.GREEN);
//		seuilCentral = seuilBas + ((seuilHaut - seuilBas) / 2);
		x = axeZeroX; x1 = axeAbscisseX;
    	y = (int) (axeZeroY - ((seuilCentral + decalageY) * echelleY)); y1 = y;
		g.drawLine(x, y, x1, y1);
		x = decalage_ecriture_gauche; y = y + 5; 
		g.setColor(Color.BLACK);
		affInfo = String.valueOf(arrondi(seuilCentral, 1));
		g.drawString(affInfo, x, y);
		
		
		// Tracé de la courbe
		g.setColor(Color.BLACK);
		
		blDessine = true;
		if (blDessine) {
		    
		    for(int i = 0; i < maxPoint - 1; i++) {
		    	x = (int) ((tbX[i]  * echelleX) + axeZeroX);
		    	y = (int) (axeZeroY - ((tbY[i] + decalageY) * echelleY));
		    	
		    	x1 = (int) ((tbX[i+1] * echelleX)  + axeZeroX);
		    	y1 = (int) (axeZeroY - ((tbY[i+1] + decalageY) * echelleY));
				g.drawLine(x, y, x1, y1);
			} // Fin for i
		}

		// Tracé Reticule
		if ((reticuleX >= deltaGauche) && (reticuleX <= axeAbscisseX) && (reticuleY >= deltaHaut) 
				&& (reticuleY <= axeZeroY)) {
			g.drawLine(reticuleX, axeZeroY, reticuleX, axeOrdonneY);
			g.drawLine(axeZeroX, reticuleY, axeAbscisseX, reticuleY);

			valeurReticuleX = minX + ((reticuleX - axeZeroX) * (1 / echelleX)) ;
		    cal.setTime(dateDeb);
		    cal.add(Calendar.SECOND, (int) valeurReticuleX);
		    dateCalcul = cal.getTime();
			
			affInfo = formater.format(dateCalcul);
			if (reticuleX < (largeurPanel / 2)) x = reticuleX;
				else x = reticuleX - 75; 
			y = axeZeroY + 20;
			
			g.drawString(affInfo, x, y);
			
			valeurReticuleY = minY + ((axeZeroY - reticuleY) * (1 / echelleY)) ;
			affInfo = String.valueOf(arrondi(valeurReticuleY, 1));
			x = decalage_ecriture_gauche; y = reticuleY + 5; 
			g.drawString(affInfo, x, y);
		}
		
	} // Fin paint
	
	public static double arrondi(double A, int B) {
		  return (double) ( (int) (A * Math.pow(10, B) + .5)) / Math.pow(10, B);
	}
	
	public void setDessine (boolean pDessine) {
		this.blDessine = pDessine;
		this.repaint();
	}

	/**
	 * @return the nbPointX
	 */
	public int getNbPointX() {
		return nbPointX;
	}

	/**
	 * @return the nbPointY
	 */
	public int getNbPointY() {
		return nbPointY;
	}

	/**
	 * @return the tbX
	 */
	public double[] getTbX() {
		return tbX;
	}

	/**
	 * @param tbX the tbX to set
	 */
	public void setTbX(double tbX[]) {
		this.tbX = tbX;
	}

	/**
	 * @return the tbY
	 */
	public double[] getTbY() {
		return tbY;
	}

	/**
	 * @param tbY the tbY to set
	 */
	public void setTbY(double tbY[]) {
		this.tbY = tbY;
	}

	/**
	 * @return the maxX
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * @param maxX the maxX to set
	 */
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	/**
	 * @return the maxY
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * @param maxY the maxY to set
	 */
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	/**
	 * @return the maxPoint
	 */
	public int getMaxPoint() {
		return maxPoint;
	}

	/**
	 * @param maxPoint the maxPoint to set
	 */
	public void setMaxPoint(int maxPoint) {
		this.maxPoint = maxPoint;
	}

	/**
	 * @return the minX
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * @param minX the minX to set
	 */
	public void setMinX(double minX) {
		this.minX = minX;
	}

	/**
	 * @return the minY
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * @param minY the minY to set
	 */
	public void setMinY(double minY) {
		this.minY = minY;
	}

	/**
	 * @return the decalageY
	 */
	public double getDecalageY() {
		return decalageY;
	}

	/**
	 * @param decalageY the decalageY to set
	 */
	public void setDecalageY(double decalageY) {
		this.decalageY = decalageY;
	}

	/**
	 * @return the seuilBas
	 */
	public double getSeuilBas() {
		return seuilBas;
	}

	/**
	 * @param seuilBas the seuilBas to set
	 */
	public void setSeuilBas(double seuilBas) {
		this.seuilBas = seuilBas;
	}

	/**
	 * @return the seuilHaut
	 */
	public double getSeuilHaut() {
		return seuilHaut;
	}

	/**
	 * @param seuilHaut the seuilHaut to set
	 */
	public void setSeuilHaut(double seuilHaut) {
		this.seuilHaut = seuilHaut;
	}

	/**
	 * @return the seuilCentral
	 */
	public double getSeuilCentral() {
		return seuilCentral;
	}

	/**
	 * @param seuilCentral the seuilCentral to set
	 */
	public void setSeuilCentral(double seuilCentral) {
		this.seuilCentral = seuilCentral;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		reticuleX = me.getX();
		reticuleY = me.getY();
		repaint();
	}

	/**
	 * @return the dateDeb
	 */
	public Date getDateDeb() {
		return dateDeb;
	}

	/**
	 * @param dateDeb the dateDeb to set
	 */
	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}

	/**
	 * @return the dateFin
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
} // Fin class
