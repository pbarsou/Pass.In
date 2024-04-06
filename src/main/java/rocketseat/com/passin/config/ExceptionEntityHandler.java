// CLASSE PARA LIDAR COM EXCEÇÕES

package rocketseat.com.passin.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;

@ControllerAdvice // capturando as exceções acionadas pelos controllers
public class ExceptionEntityHandler {
    @ExceptionHandler(EventNotFoundException.class) // informando que esse método lida com uma exceção
    public ResponseEntity handleEventNotFound(EventNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
