package ru.practicum.main_service.compilation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_service.event.dto.EventShortDto;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    int id;
    Collection<EventShortDto> events;
    boolean pinned;
    String title;
}
