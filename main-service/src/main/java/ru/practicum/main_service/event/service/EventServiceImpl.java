package ru.practicum.main_service.event.service;

import client.RestStatClient;
import exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.event.dto.*;
import ru.practicum.main_service.event.enums.EventState;
import ru.practicum.main_service.event.enums.StateActionForAdmin;
import ru.practicum.main_service.event.enums.StateActionForUser;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.event.repository.LocationRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final RestStatClient restStatClient;

    @Override
    public List<EventShortDto> getEventsByUser(Integer userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, page).stream().map(event -> eventMapper.toEventShortDto(event)).toList();
    }

    @Override
    public EventFullDto createEvent(Integer userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null && !newEventDto.getEventDate().isAfter(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new DataIntegrityViolationException("Event date should be in 2+ hours after now");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", newEventDto.getCategory())));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        Event event = eventMapper.toModelByNew(newEventDto, category, user);
        event.setLocation(locationRepository.save(newEventDto.getLocation()));
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventFullInformation(Integer userId, Integer eventId) {
        return eventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId))));
    }

    @Override
    public EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventUserDto updateEventUserDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (updateEventUserDto.getEventDate() != null && !updateEventUserDto.getEventDate().isAfter(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new DataIntegrityViolationException("Event date should be in 2+ hours after now");
        }
        if (updateEventUserDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserDto.getAnnotation());
        }
        if (updateEventUserDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", updateEventUserDto.getCategory())));
            event.setCategory(category);
        }
        if (updateEventUserDto.getDescription() != null) {
            event.setDescription(updateEventUserDto.getDescription());
        }
        if (updateEventUserDto.getEventDate() != null) {
            event.setEventDate(updateEventUserDto.getEventDate());
        }
        if (updateEventUserDto.getLocation() != null) {
            event.setLocation(updateEventUserDto.getLocation());
        }
        if (updateEventUserDto.getPaid() != null) {
            event.setPaid(updateEventUserDto.getPaid());
        }
        if (updateEventUserDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserDto.getParticipantLimit());
        }
        if (updateEventUserDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserDto.getRequestModeration());
        }
        if (updateEventUserDto.getTitle() != null) {
            event.setTitle(updateEventUserDto.getTitle());
        }

        if (updateEventUserDto.getStateAction() != null) {
            if (updateEventUserDto.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else {
                event.setState(EventState.CANCELED);
            }
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto adminUpdateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (updateEventAdminRequest.getStateAction() == StateActionForAdmin.PUBLISH_EVENT && event.getState() != EventState.PENDING) {
            throw new DataIntegrityViolationException("Event should be in PENDING state");
        }
        if (updateEventAdminRequest.getStateAction() == StateActionForAdmin.REJECT_EVENT && event.getState() == EventState.PUBLISHED) {
            throw new DataIntegrityViolationException("Event can be rejected only in PENDING state");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionForAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequest.getStateAction().equals(StateActionForAdmin.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", updateEventAdminRequest.getCategory())));
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(locationRepository.save(updateEventAdminRequest.getLocation()));
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> adminGetAllEvents(AdminEventParams adminEventParams) {
        Pageable page = PageRequest.of(adminEventParams.getFrom(), adminEventParams.getSize());

        if (adminEventParams.getRangeStart() == null || adminEventParams.getRangeEnd() == null) {
            adminEventParams.setRangeStart(LocalDateTime.now());
            adminEventParams.setRangeEnd(adminEventParams.getRangeStart().plusYears(1));
        }
        List<EventFullDto> events = eventMapper.toEventFullDto(eventRepository.findAdminEvents(
                adminEventParams.getUsers(),
                adminEventParams.getStates(),
                adminEventParams.getCategories(),
                adminEventParams.getRangeStart(),
                adminEventParams.getRangeEnd(),
                page));
        if (events.isEmpty()) {
            return List.of();
        }
        return addViews(events);
    }

    @Override
    public List<EventShortDto> publicGetAllEvents(EventRequestParam eventRequestParam) {
        Pageable page = PageRequest.of(eventRequestParam.getFrom(), eventRequestParam.getSize());

        if (eventRequestParam.getRangeStart() == null || eventRequestParam.getRangeEnd() == null) {
            eventRequestParam.setRangeStart(LocalDateTime.now());
            eventRequestParam.setRangeEnd(eventRequestParam.getRangeStart().plusYears(1));
        }
        List<Event> events = eventRepository.findPublicEvents(
                eventRequestParam.getText(),
                eventRequestParam.getCategory(),
                eventRequestParam.getPaid(),
                eventRequestParam.getRangeStart(),
                eventRequestParam.getRangeEnd(),
                eventRequestParam.getOnlyAvailable(),
                page);
        if (events.isEmpty()) {
            return List.of();
        }

        if (eventRequestParam.getSort() != null) {
            return switch (eventRequestParam.getSort()) {
                case EVENT_DATE -> events.stream()
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .map(eventMapper::toEventShortDto)
                        .toList();
                case VIEWS -> events.stream()
                        .sorted(Comparator.comparing(Event::getViews))
                        .map(eventMapper::toEventShortDto)
                        .toList();
            };
        }
        addViews(events.stream()
                .map(eventMapper::toEventFullDto)
                .toList());
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    @Override
    public EventFullDto publicGetEvent(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(String.format("Event with id=%d was not published", eventId));
        }
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return addViews(List.of(eventFullDto)).getFirst();
    }

    private List<EventFullDto> addViews(List<EventFullDto> events) {
        Map<String, EventFullDto> eventDtoMap = new HashMap<>();
        List<String> uris = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime earlyPublished = LocalDateTime.now().minusHours(1);
        for (EventFullDto event : events) {
            String uri = "/events/" + event.getId();
            eventDtoMap.put(uri, event);
            uris.add(uri);
            if (event.getPublishedOn() != null) {
                LocalDateTime dtoPublishDate = event.getPublishedOn();
                if (dtoPublishDate.isBefore(earlyPublished)) {
                    earlyPublished = dtoPublishDate;
                }
            }
        }
        String start = earlyPublished.format(formatter);
        String end = LocalDateTime.now().format(formatter);

        try {
            restStatClient.getStats(start, end, uris, true)
                    .forEach(viewStatsDto -> {
                        EventFullDto eventDto = eventDtoMap.get(viewStatsDto.getUri());
                        if (eventDto != null) {
                            eventDto.setViews(viewStatsDto.getHits());
                        }
                    });
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }
        return eventDtoMap.values().stream().toList();
    }

}