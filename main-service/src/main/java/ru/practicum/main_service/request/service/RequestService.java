package ru.practicum.main_service.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.request.dto.RequestDto;
import java.util.List;

@Transactional(readOnly = true)
public interface RequestService {
    List<RequestDto> getRequestsByOwnerOfEvent(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest);

    RequestDto createRequest(Integer userId, Integer eventId);

    List<RequestDto> getCurrentUserRequests(Integer userId);

    RequestDto cancelRequests(Integer userId, Integer requestId);
}