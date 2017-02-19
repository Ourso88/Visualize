package em.communication;

public interface AE_TCP_Modbus {

	  public static final boolean MODBUS_DEBUG = false;
	
	  public static final int READ_MULTIPLE_REGISTERS = 3;

	  public static final int WRITE_MULTIPLE_REGISTERS = 16;

	  public static final int READ_COILS = 1;

	  public static final int READ_INPUT_DISCRETES = 2;

	  public static final int READ_INPUT_REGISTERS = 4;

	  public static final int WRITE_COIL = 5;

	  public static final int WRITE_MULTIPLE_COILS = 15;

	  public static final int WRITE_SINGLE_REGISTER = 6;
	  
}
