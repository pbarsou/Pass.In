package rocketseat.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passin.domain.event.Event;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, String> {
}
