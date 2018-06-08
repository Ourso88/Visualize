package kernel;

/**
 * Gere la table AlarmeService pour les appels vers les services
 * @author Eric Mariani
 * @since 03/05/2018
 *
 */
public class AlarmeService {
	private long idAlarmeService;
	private String nomService;
	private int indexMotApi;
	private boolean klaxon;
	
	/**
	 * Constructeur
	 */
	public AlarmeService(long idAlarmeService, String nomService, int indexMotApi, boolean klaxon) {
		super();
		this.setIdAlarmeService(idAlarmeService);
		this.setNomService(nomService);
		this.setIndexMotApi(indexMotApi);
		this.setKlaxon(klaxon);
	}

	/**
	 * @return the idAlarmeService
	 */
	public long getIdAlarmeService() {
		return idAlarmeService;
	}

	/**
	 * @param idAlarmeService the idAlarmeService to set
	 */
	public void setIdAlarmeService(long idAlarmeService) {
		this.idAlarmeService = idAlarmeService;
	}

	/**
	 * @return the nomService
	 */
	public String getNomService() {
		return nomService;
	}

	/**
	 * @param nomService the nomService to set
	 */
	public void setNomService(String nomService) {
		this.nomService = nomService;
	}

	/**
	 * @return the indexMotApi
	 */
	public int getIndexMotApi() {
		return indexMotApi;
	}

	/**
	 * @param indexMotApi the indexMotApi to set
	 */
	public void setIndexMotApi(int indexMotApi) {
		this.indexMotApi = indexMotApi;
	}

	/**
	 * @return the klaxon
	 */
	public boolean isKlaxon() {
		return klaxon;
	}

	/**
	 * @param klaxon the klaxon to set
	 */
	public void setKlaxon(boolean klaxon) {
		this.klaxon = klaxon;
	}
	
}
