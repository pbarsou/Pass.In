package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.domain.event.exceptions.EventFullException;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor // gera o construtor apenas com os atributos que são requireds
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    // PEGA DETALHES DO EVENTO
    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.getEventById(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeesList.size());
    }

    // CRIAÇÃO DO EVENTO
    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();

        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId()); // retorna ID do evento criado
    }

    // REGISTRA ATTENDEE EM UM EVENTO
    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {

        // verificando se o participante já está inscrito no evento
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);

        // verificando se ainda tem vagas disponíveis no evento

        Event event = getEventById(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeesList.size()) throw new EventFullException("Event is full.");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());

        this.attendeeService.registerAtendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    public Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event not found with ID: " + eventId)); // lança uma exceção de não existir
    }

    // CRIAÇÃO DO SLUG
    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD); // coloca os acentos como caracteres únicos. Ex: "Sa~o Paulo"
        return normalized
                .replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "") // seleciona os acentos e substitui por ""
                .replaceAll("[^\\w\\s]", "") // tira tudo o que não for letra e número
                .replaceAll("\\s+", "-") // substitui qualquer espaço por -
                .toLowerCase();
    }
}
