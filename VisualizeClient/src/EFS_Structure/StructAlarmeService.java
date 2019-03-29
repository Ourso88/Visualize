package EFS_Structure;

/**
 * Structure de la table AlarmeService
 * @author Eric Mariani
 * @since 14/02/2019
 *
 */
public class StructAlarmeService {
	private long id;
	private String nomService;
	private int indexMotApi;
	private int klaxon;
	
	/**
	 * Constructeur
	 * @param id
	 * @param nomService
	 * @param indexMotApi
	 * @param klaxon
	 */
	public StructAlarmeService(long id, String nomService, int indexMotApi, int klaxon) {
		super();
		this.setId(id);
		this.setNomService(nomService);
		this.setIndexMotApi(indexMotApi);
		this.setKlaxon(klaxon);
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	public int getKlaxon() {
		return klaxon;
	}
	/**
	 * @param klaxon the klaxon to set
	 */
	public void setKlaxon(int klaxon) {
		this.klaxon = klaxon;
	}
	
	
}
