package ru.practicum.main_service.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.compilation.model.CompilationEvent;
import ru.practicum.main_service.compilation.model.CompilationEventKey;

public interface CompilationEventsRepository extends JpaRepository<CompilationEvent, CompilationEventKey> {
}
