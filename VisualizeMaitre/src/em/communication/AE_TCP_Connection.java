package em.communication;

import java.io.*;
import java.net.*;

import em.fonctions.GestionLogger;
import em.general.EFS_Maitre_Variable;

/**
 * Gestion de la communication Modbus avec l'API
 * @author Eric Mariani
 * @since 22/12/2015
 *
 */
public class AE_TCP_Connection implements AE_TCP_Modbus {

	// Variable d'instance
	private Socket m_Socket;
//	private int m_Timeout = 1500;
	private int m_Timeout = 2000; // ===== EM : Modification 29/03/2019 =====
	private boolean m_Connected;

	private InetAddress m_Address;
	private int m_Port = 502;

	private DataOutputStream buffOut = null;
	private DataInputStream buffIn = null;
	private byte [] reqOut = new byte[250];	
	
	
	/**
	 * Constructeur
	 * @param adr
	 * 		Adresse IP
	 * @param Port
	 * 		Port de communication
	 */
	public AE_TCP_Connection(InetAddress adr, int Port) {
		    m_Address = adr;
		    m_Port = Port;
	} //Fin constructor	  
	
	/**
	 * Connection avec l'API
	 */
	public synchronized void connect() {
		if(!m_Connected) {
			try {
				m_Socket = new Socket(m_Address, m_Port);
				setTimeout(m_Timeout);
				buffOut = new DataOutputStream(m_Socket.getOutputStream());
	            buffIn = new DataInputStream(m_Socket.getInputStream());
				m_Connected = true;
			} catch (IOException e) {
				GestionLogger.gestionLogger.warning("Erreur de connexion API : " + e.getMessage());
				EFS_Maitre_Variable.compteurErreurAPI++;
			}
		}
	} //Fin connect

	/**
	 * Fermeture de la connexion
	 */
	public void close() {
		if(m_Connected) {
			try {
		        m_Socket.close();
		    } catch (IOException ex) {
				GestionLogger.gestionLogger.warning("Erreur lors de la fermeture de la connexion API : " + ex.getMessage());
			}
		    m_Connected = false;
	    } // Fin if
	} // Fin close	


	/**
	 * Renvoi du Time Out de la liaison
	 * @return
	 */
	public int getTimeout() {
		return m_Timeout;
	} // Fin getTimeout	
	
	/**
	 * Parametrage du Time Out de la liaison
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		m_Timeout = timeout;
		try {
			m_Socket.setSoTimeout(m_Timeout);
		} // Fin Try
		catch (IOException ex) {
		      //handle?
		} // Fin Catch
	} // Fin setTimeout	

	
	public int getPort() {
		return m_Port;
	} // Fin getPort

	public void setPort(int port) {
		m_Port = port;
	} // Fin setPort

	public InetAddress getAddress() {
		return m_Address;
	} // Fin getAddress

	public void setAddress(InetAddress adr) {
		m_Address = adr;
	} // Fin setAddress	
	
	public boolean isConnected() {
		return m_Connected;
	} // Fin isConnected	
	
	/**
	 * Creation d'une requete de communication
	 * @param Type_Requete
	 * @param adr_Variable
	 * @param nb_Variable
	 * @return
	 */
	public int createRequest(int Type_Requete, int adr_Variable, int nb_Variable) {
		int TailleRequest = -1;
		reqOut[0] = 0;
		reqOut[1] = 0;
		reqOut[2] = 0;
		reqOut[3] = 0;
		reqOut[4] = 0;
		// Numero d'esclave / 1 par defaut
		reqOut[6] = 1;
		
		switch (Type_Requete) {
		case READ_MULTIPLE_REGISTERS :
			// Taille de la fin de la trame
			reqOut[5] = 6;
			// Fonction
			reqOut[7] = 3; // Lecture de mots
			// Adresse de la premiere variable à lire
			reqOut[8] = (byte)(adr_Variable / 256);
			reqOut[9] = (byte)(adr_Variable % 256);
			// Nombre de variables à lire
			reqOut[10] = (byte)(nb_Variable / 256);
			reqOut[11] = (byte)(nb_Variable % 256);
			TailleRequest = 12;
			break;
		case READ_INPUT_DISCRETES :
			// Taille de la fin de la trame
			reqOut[5] = 6;
			// Fonction
			reqOut[7] = 2;
			// Adresse de la premiere variable à lire
			reqOut[8] = (byte)(adr_Variable / 256);
			reqOut[9] = (byte)(adr_Variable % 256);
			// Nombre de variables à lire
			reqOut[10] = (byte)(nb_Variable / 256);
			reqOut[11] = (byte)(nb_Variable % 256);
			TailleRequest = 12;
			break;
		case WRITE_COIL :
			// Taille de la fin de la trame
			reqOut[5] = 6;
			// Fonction
			reqOut[7] = 5;
			// Adresse de la premiere variable à écrire
			reqOut[8] = (byte)(adr_Variable / 256);
			reqOut[9] = (byte)(adr_Variable % 256);
			// Nombre de variables à lire
			reqOut[10] = (byte)(255);
			reqOut[11] = (byte)(0);
			TailleRequest = 12;
			break;
		case WRITE_SINGLE_REGISTER :
			// Taille de la fin de la trame
			reqOut[5] = 6;
			// Fonction
			reqOut[7] = 6;
			// Adresse de la premiere variable à écrire
			reqOut[8] = (byte)(adr_Variable / 256);
			reqOut[9] = (byte)(adr_Variable % 256);
			// Valeur de la variable
			reqOut[10] = (byte)(nb_Variable / 256);
			reqOut[11] = (byte)(nb_Variable % 256);
			TailleRequest = 12;
			break;			
		} // Fin switch
		
		return TailleRequest;
	} // Fin createRequest
	
	/**
	 * Envoi d'une requete à l'API
	 * @param Taille_Request
	 * @return
	 */
	public double [] setRequest(int Taille_Request) {
    	byte [] reqIn = new byte[250];
		
		try {
			// Envoi de la requete
            buffOut.write(reqOut, 0, Taille_Request);
            if (MODBUS_DEBUG) System.out.println("write effectué !");
            // Flush de la requête
            buffOut.flush();
            if (MODBUS_DEBUG) System.out.println("flush effectué !");    		
            // Lecture de la réponse
            buffIn.read(reqIn);
            if (MODBUS_DEBUG) System.out.println("Lecture effectuée ...");

		} catch (IOException ex) {
			EFS_Maitre_Variable.compteurErreurAPI++;
			GestionLogger.gestionLogger.warning("[setRequest] Erreur lors de l'envoi requête 1 : " 
			                                    + ex.getMessage() + "\nNombre Erreur API : " + EFS_Maitre_Variable.compteurErreurAPI);
			
			try {
				// Envoi de la requete
	            buffOut.write(reqOut, 0, Taille_Request);
	            if (MODBUS_DEBUG) System.out.println("write effectué !");
	            // Flush de la requête
	            buffOut.flush();
	            if (MODBUS_DEBUG) System.out.println("flush effectué !");    		
	            // Lecture de la réponse
	            buffIn.read(reqIn);
	            if (MODBUS_DEBUG) System.out.println("Lecture effectuée ...");

			} catch (IOException ex2) {
				EFS_Maitre_Variable.compteurErreurAPI++;
				GestionLogger.gestionLogger.warning("[setRequest] Erreur lors de l'envoi requête 2 : " 
				                                    + ex.getMessage() + "\nNombre Erreur API : " + EFS_Maitre_Variable.compteurErreurAPI);
			} // Fin Catch
		} // Fin Catch
		
		return getReponse(reqIn);
		
	} // Fin setRequest
	
	/**
	 * Dechifrage de la réponse de l'API
	 * @param reqReponse
	 * @return
	 */
	private double [] getReponse(byte [] reqReponse) {
		double [] tbReponse = new double[200];
		int nbByte;
		int ByteReponse;
		int TestBit;
		
		switch (reqReponse[7]) {
		case READ_MULTIPLE_REGISTERS :
			nbByte = reqReponse[8];
			int unsigned;
			byte signed;
			int unsigned_1;
			byte signed_1;
			
            for (int i = 0; i < nbByte; i+=2) {
    			ByteReponse = reqReponse[9 + i];
//    			tbReponse[i/2] = (reqReponse[9+i] * 256) + reqReponse[10+i];
    			signed = (reqReponse[9 + i]);
    			unsigned = signed;
//    			unsigned = (signed & 0xff);
//    			unsigned = (signed >= 0 ? signed : 256 + signed);
//    			unsigned = (256 + signed) % 256;
    			unsigned *= 256;
    			
    			signed_1 = (reqReponse[10 + i]);
    			unsigned_1 = signed_1;
    			unsigned_1 = (signed_1 & 0xff);
    			unsigned_1 = (signed_1 >= 0 ? signed_1 : 256 + signed_1);
    			unsigned_1 = (256 + signed_1) % 256;

    			
    			
    			tbReponse[i/2] = unsigned + unsigned_1;
            } // Fin For i			
			break;

		case WRITE_SINGLE_REGISTER :
			break;			
			
		case READ_INPUT_DISCRETES :
			// Nombre de Byte à traiter
			nbByte = reqReponse[8];
			
            for (int i = 0; i < nbByte; i++) {
    			ByteReponse = reqReponse[9 + i];
                for (int j = 0; j < 8; j++) {
                	TestBit = (int)Math.pow(2, j);
                	if ((ByteReponse & TestBit) == 0) {
                		tbReponse[j +(i*8)] = 0;
                	}
                	else {
                		tbReponse[j +(i*8)] = 1;
                	} // Fin else
                } // Fin For j
            } // Fin For i			
			break;
		} // Fin switch		
		
		return tbReponse;
	} 
	
	
} // Fin AE_TCP_Connection
