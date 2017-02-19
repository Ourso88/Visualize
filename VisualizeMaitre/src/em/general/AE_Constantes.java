package em.general;

import java.awt.Color;
import java.awt.Font;

public interface AE_Constantes {
	
	// Constantes Login
	public static final int NIVEAU_CONSULTATION = 0;
	
	// Constantes version
	public static final int VERSION = 20001;

	// Langue
	public static final int LANGUE_FR = 0;
	public static final int LANGUE_US = 1;
		
	// Timer
	public static final int TIMER_RAFFRAICHISSEMENT_MINUTE = 60000;
	
	// Contantes Font
	public static final Font FONT_ARIAL_12 = new Font("Arial", Font.PLAIN,  12 );
	public static final Font FONT_ARIAL_14 = new Font("Arial", Font.PLAIN,  14 );
	public static final Font FONT_ARIAL_16 = new Font("Arial", Font.PLAIN,  16 );
	public static final Font FONT_ARIAL_20 = new Font("Arial", Font.PLAIN,  20 );

	public static final Font FONT_ARIAL_12_BOLD = new Font("Arial", Font.BOLD,  12 );
	public static final Font FONT_ARIAL_14_BOLD = new Font("Arial", Font.BOLD,  14 );
	public static final Font FONT_ARIAL_16_BOLD = new Font("Arial", Font.BOLD,  16 );
	public static final Font FONT_ARIAL_20_BOLD = new Font("Arial", Font.BOLD,  20 );
	
	// Constantes SGBD
	public static final int AE_SGBD_ACCESS = 1;
	public static final int AE_SGBD_MYSQL = 2;
	public static final int AE_SGBD_SQLITE = 3;
	public static final int AE_SGBD_ORACLE = 4;
	
	// Constantes couleur
	Color AE_BLEU = new Color(167, 198, 237); 
	Color AE_BLEU_FONCE = new Color(0, 0, 153); 
	Color AE_MARRON = new Color(200, 130, 66); 
	Color AE_VERT_001 = new Color(143, 214, 189);
	Color AE_BLANC = new Color(255, 255, 255);
	Color AE_VERT_002 = new Color(143, 214, 189);
	Color AE_ROUGE = new Color(255, 0, 0); 
	Color AE_NOIR = new Color(0, 0, 0); 
	
	
/*
	// Lien SGBD Oracle GE
	public static final int AE_SGBD_TYPE = AE_SGBD_ORACLE;
	public static final String AE_SGBD_SERVEUR = new String("@borop048:1830");
	public static final String AE_SGBD_BASE = new String("borop048");
	public static final String AE_SGBD_USER = new String("alize");
	public static final String AE_SGBD_MDP = new String("Alize#2016V");	
*/
	
	// Lien SGBD Axiome Oracle
	public static final int AE_SGBD_TYPE = AE_SGBD_ORACLE;
	public static String AE_SGBD_SERVEUR = new String("@localhost:1521");
	public static final String AE_SGBD_BASE = new String("xe");
	public static final String AE_SGBD_USER = new String("Alize");
	public static final String AE_SGBD_MDP = new String("Alize");
	
/*	
	// Lien SGBD
	public static final int AE_SGBD_TYPE = AE_SGBD_SQLITE;
	public static final String AE_SGBD_SERVEUR = new String("");
	public static final String AE_SGBD_BASE = new String("AE.db");
	public static final String AE_SGBD_USER = new String("");
	public static final String AE_SGBD_MDP = new String("");
*/
/*	
	public static final int AE_SGBD_TYPE = AE_SGBD_MYSQL;
	public static final String AE_SGBD_SERVEUR = new String("localhost");
	public static final String AE_SGBD_BASE = new String("AE");
	public static final String AE_SGBD_USER = new String("root");
	public static final String AE_SGBD_MDP = new String("");
*/
}
