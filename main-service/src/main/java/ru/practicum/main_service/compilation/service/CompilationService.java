package ru.practicum.main_service.compilation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;

import java.util.List;

@Transactional(readOnly = true)
public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getById(int id);

    @Transactional
    CompilationDto add(NewCompilationDto dto);

    @Transactional
    CompilationDto update(int id, UpdateCompilationDto dto);

    void delete(int id);
}
