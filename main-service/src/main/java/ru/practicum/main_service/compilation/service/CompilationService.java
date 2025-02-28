package ru.practicum.main_service.compilation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.compilation.model.Compilation;

import java.util.List;

@Transactional(readOnly = true)
public interface CompilationService {
    List<Compilation> getAll();
}
