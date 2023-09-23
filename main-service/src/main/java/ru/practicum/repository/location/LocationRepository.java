package ru.practicum.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
