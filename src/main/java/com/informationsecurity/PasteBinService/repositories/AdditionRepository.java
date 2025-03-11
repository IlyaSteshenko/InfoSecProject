package com.informationsecurity.PasteBinService.repositories;

import com.informationsecurity.PasteBinService.models.Addition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditionRepository extends JpaRepository<Addition, Long> {

    @Query("SELECT a FROM Addition a WHERE a.pasteId = :id")
    List<Addition> findAdditionsWithPasteId(@Param("id") Long id);
}
