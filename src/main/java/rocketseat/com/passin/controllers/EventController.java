package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events") // mapeia os requests do endpoint mencionado, os quais o controller vai ser capaz de lidar
@RequiredArgsConstructor
public class EventController {
    private final EventService service;
    @GetMapping("/{eventId}") // pega a partir da URL
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId) { // informando que a variável vai vir da URL (Path Variable)
        EventResponseDTO event = this.service.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.service.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri(); // criando uma URI de devolução (para o usuário poder consultar informações do evento)

        return ResponseEntity.created(uri).body(eventIdDTO);
    }
}
