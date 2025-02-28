package ru.practicum.main_service.compilation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CompilationEventsKey implements Serializable {
    @Column(name = "compilation_id")
    Integer compilationId;
    @Column(name = "event_id")
    Integer eventId;
}
