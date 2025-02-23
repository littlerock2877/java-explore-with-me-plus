package ru.practicum.main_service.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.dto.EventFullDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.NewEventDto;
import ru.practicum.main_service.event.dto.UpdateEventUserDto;
import java.util.List;

@Transactional(readOnly = true)
public interface EventService {
    List<EventShortDto> getEventsByUser(Integer userId, Integer from, Integer size);

    @Transactional
    EventFullDto createEvent(Integer userId, NewEventDto newEventDto);

    EventFullDto getEventFullInformation(Integer userId, Integer eventId);

    @Transactional
    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserDto updateEventUserDto);
}