package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passin.domain.checkIn.CheckIn;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.dto.attendee.AttendeeDetailDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    // LISTAGEM DIRETA DO BANCO
    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
       return this.attendeeRepository.findAllByEventId(eventId);
    }

    // LISTA OS PARTICIPANTES JÁ COM TRATAMENTO DE INFORMAÇÕES JUNTO DO DTO
    public AttendeesListResponseDTO getEventAttendees(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailDTOList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetailDTO(
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getCreatedAt(),
                    checkedInAt
            );
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailDTOList);
    }

    // VERIFICA SE O ATTENDEE JÁ ESTÁ INSCRITO NO EVENTO
    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered!");
    }

    // REGISTRO DE ATTENDEE
    public Attendee registerAtendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    // GET ATTENDEE

    private Attendee getAttendee(String attendeId) {
        return this.attendeeRepository.findById(attendeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeId));
    }

    // CHECK IN ATTENDEE

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    // GET ATTENDEE BADGE

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(
                attendee.getName(),
                attendee.getEmail(),
                uri,
                attendee.getEvent().getId()
        );

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }

}
