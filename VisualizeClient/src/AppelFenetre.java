import AE_General.EFS_Client_Variable;


public class AppelFenetre {

	public static void main(String[] args) {
		// Initialisation
		EFS_Client_Variable.initialisationVariableBase();
		@SuppressWarnings("unused")
		Login fenetre;
		fenetre = new Login();
//		fenetre.setVisible(true);
//		fenetre.requestFocusInWindow();
	}
}
