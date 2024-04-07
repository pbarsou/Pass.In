package rocketseat.com.passin.domain.checkIn.exceptions;

public class CheckInAlreadyExistsExeption extends RuntimeException{
    public CheckInAlreadyExistsExeption(String message) {
        super(message);
    }
}
