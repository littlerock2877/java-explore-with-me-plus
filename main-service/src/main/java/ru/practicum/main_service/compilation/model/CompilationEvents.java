package ru.practicum.main_service.compilation.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationEvents {
    @EmbeddedId
    CompilationEventsKey id;
    @Column(name = "compilation_id")
    int compilationId;
    @Column(name = "event_id")
    int eventId;
}
