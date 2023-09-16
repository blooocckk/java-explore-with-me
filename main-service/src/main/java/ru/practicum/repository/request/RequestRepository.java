package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.request.Request;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester_Id(Long requesterId);

    List<Request> findByEvent_Id(Long eventId);

    List<Request> findByIdInAndEvent_Id(Collection<Long> ids, Long eventId);

    boolean existsByEvent_IdAndRequester_Id(Long eventId, Long requesterId);
}
