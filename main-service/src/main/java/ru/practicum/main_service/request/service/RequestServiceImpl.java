package ru.practicum.main_service.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.NotFoundException;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.repository.RequestRepository;
import java.security.InvalidParameterException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<RequestDto> getRequestsByOwnerOfEvent(Integer userId, Integer eventId) {
        return requestRepository.findAllByEventWithInitiator(userId, eventId).stream().map(request -> requestMapper.toRequestDto(request)).toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }

        List<Request> requests = requestRepository.findAllByEventWithInitiator(userId, eventId);
        List<Request> requestsToUpdate = requests.stream().filter(request -> requestStatusUpdateRequest.getRequestIds().contains(request.getId())).toList();

        if (requestsToUpdate.stream().anyMatch(request -> request.getStatus().equals(RequestStatus.CONFIRMED) && requestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED))) {
            throw new InvalidParameterException("request already confirmed");
        }

        if (event.getConfirmedRequests() + requestsToUpdate.size() > event.getParticipantLimit() && requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new InvalidParameterException("exceeding the limit of participants");
        }

        for (Request x : requestsToUpdate) {
            x.setStatus(RequestStatus.valueOf(requestStatusUpdateRequest.getStatus().toString()));
        }
        requestRepository.saveAll(requestsToUpdate);
        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
           event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
        }
        eventRepository.save(event);
        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requestsToUpdate.stream().map(request -> requestMapper.toParticipationRequestDto(request)).toList());
        }

        if (requestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
            result.setRejectedRequests(requestsToUpdate.stream().map(request -> requestMapper.toParticipationRequestDto(request)).toList());
        }
        return result;
    }
}