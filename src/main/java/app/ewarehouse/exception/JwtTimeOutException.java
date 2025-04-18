package app.ewarehouse.exception;



public class JwtTimeOutException extends RuntimeException{

 
	private static final long serialVersionUID = -5044650370006455853L;

public JwtTimeOutException(){
        super("Token time out");
    }

    public JwtTimeOutException(String message){
        super(message);
    }

}
