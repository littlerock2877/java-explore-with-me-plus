package ru.practicum.main_service.compilation.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CompilationEventKey implements Serializable {
    Integer compilationId;
    Integer eventId;
}
