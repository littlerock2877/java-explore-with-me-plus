package ru.practicum.main_service.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.event.model.Event;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findAllByInitiatorId(Integer userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Integer eventId, Integer userId);
}