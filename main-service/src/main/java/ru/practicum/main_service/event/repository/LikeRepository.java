package ru.practicum.main_service.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.main_service.event.model.Like;
import ru.practicum.main_service.event.model.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

    void deleteByUserIdAndEventId(Integer userId, Integer eventId);

    long countByEventId(Integer eventId);
}
