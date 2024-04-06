package rocketseat.com.passin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events") // mapeia os requests do endpoint mencionado, os quais o controller vai ser capaz de lidar
public class EventController {
    @GetMapping
    public ResponseEntity<String> getTeste() {
        return ResponseEntity.ok("sucesso!");
    }
}
