package ru.practicum.main_service.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.compilation.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    @Query("select c from Compilation c " +
            "where (:pinned is null OR c.pinned = :pinned)")
    List<Compilation> findCompilations(@Param("pinned") Boolean pinned, Pageable pageable);
}
