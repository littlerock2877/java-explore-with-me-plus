package ru.practicum.main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.request.model.Request;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("select p from Request as p " +
            "join Event as e ON p.event = e.id " +
            "where p.event = :eventId and e.initiator.id = :userId")
    List<Request> findAllByEventWithInitiator(@Param(value = "userId") Integer userId, @Param("eventId") Integer eventId);
}