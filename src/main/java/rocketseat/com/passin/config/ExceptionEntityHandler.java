// CLASSE PARA LIDAR COM EXCEÇÕES

package rocketseat.com.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passin.domain.checkIn.exceptions.CheckInAlreadyExistsExeption;
import rocketseat.com.passin.domain.event.exceptions.EventFullException;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.general.ErrorResponseDTO;

@ControllerAdvice // capturando as exceções acionadas pelos controllers
public class ExceptionEntityHandler {
    @ExceptionHandler(EventNotFoundException.class) // informando que esse método lida com uma exceção
    public ResponseEntity handleEventNotFound(EventNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EventFullException.class) // informando que esse método lida com uma exceção
    public ResponseEntity<ErrorResponseDTO> handleEventFullException(EventFullException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExists(AttendeeAlreadyExistException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistsExeption.class)
    public ResponseEntity handleCheckInAlreadyExists(CheckInAlreadyExistsExeption exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
