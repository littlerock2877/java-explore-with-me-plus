package ru.practicum.main_service.compilation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CompilationEventKey.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationEvent {
    @Id
    int compilationId;
    @Id
    int eventId;
}
