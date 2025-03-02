package ru.practicum.main_service.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.service.CompilationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService service;

    @PostMapping
    public CompilationDto addNew(@RequestBody NewCompilationDto dto) {
        log.info("Creating new compilation - Started.");
        CompilationDto addedComp = service.add(dto);
        log.info("Creating new compilation - Finished.");
        return addedComp;
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable int compId) {
        log.info("Deleting compilation with id {} - Started.", compId);
        service.delete(compId);
        log.info("Deleting compilation with id {} - Finished.", compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable int compId,
                                 @RequestBody UpdateCompilationDto dto) {
        log.info("Updating compilation with id {} - Started.", compId);
        CompilationDto updatedComp = service.update(compId, dto);
        log.info("Updating compilation with id {} - Finished.", compId);
        return updatedComp;
    }
}
