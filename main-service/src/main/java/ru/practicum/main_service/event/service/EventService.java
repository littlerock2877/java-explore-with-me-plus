package ru.practicum.main_service.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.event.dto.*;

import java.util.List;

@Transactional(readOnly = true)
public interface EventService {
    List<EventShortDto> getEventsByUser(Integer userId, Integer from, Integer size);

    @Transactional
    EventFullDto createEvent(Integer userId, NewEventDto newEventDto);

    EventFullDto getEventFullInformation(Integer userId, Integer eventId);

    @Transactional
    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserDto updateEventUserDto);

    @Transactional
    EventFullDto adminUpdateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> adminGetAllEvents(AdminEventParams adminEventParams);

    List<EventShortDto> publicGetAllEvents(EventRequestParam eventRequestParam);

    EventFullDto publicGetEvent(Integer eventId);
}