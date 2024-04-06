package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events") // mapeia os requests do endpoint mencionado, os quais o controller vai ser capaz de lidar
@RequiredArgsConstructor
public class EventController {
    private final EventService service;
    @GetMapping("/{eventId}") // pega a partir da URL
    public ResponseEntity<String> getEvent(@PathVariable String eventId) { // informando que a variável vai vir da URL (Path Variable)
        this.service.getEventDetail(eventId);
        return ResponseEntity.ok("sucesso!");
    }
}
