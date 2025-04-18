package app.ewarehouse.exception;



public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 2575598267375658297L;

public ResourceNotFoundException(){
        super("Resource not found");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

}
