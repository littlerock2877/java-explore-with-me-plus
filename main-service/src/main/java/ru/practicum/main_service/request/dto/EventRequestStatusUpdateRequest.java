package ru.practicum.main_service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.request.enums.RequestStatus;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}
