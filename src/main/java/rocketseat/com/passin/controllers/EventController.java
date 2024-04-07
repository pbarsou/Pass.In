package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dto.attendee.AttendeeDetailDTO;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events") // mapeia os requests do endpoint mencionado, os quais o controller vai ser capaz de lidar
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService ateendeeService;

    // LISTA INFORMAÇÕES DE UM EVENTO
    @GetMapping("/{id}") // pega a partir da URL
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) { // informando que a variável vai vir da URL (Path Variable)
        EventResponseDTO event = this.eventService.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    // CRIAÇÃO DE UM EVENTO
    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri(); // criando uma URI de devolução (para o usuário poder consultar informações do evento)

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    // INSCRIÇÃO EM UM EVENTO
    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

    // LISTAGEM DE PARTICIPANTES DE UM EVENTO
    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeesListResponseDTO attendees = this.ateendeeService.getEventAttendees(id);
        return ResponseEntity.ok(attendees);
    }
}
