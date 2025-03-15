package com.informationsecurity.PasteBinService.repositories;

import com.informationsecurity.PasteBinService.models.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {
    Paste findPasteById(long id);

    @Query("SELECT p FROM Paste p WHERE p.title LIKE %:searchText%" +
            " OR p.text LIKE %:searchText%" +
            " OR p.author LIKE %:searchText%")
    List<Paste> findPatentsWithText(@Param("searchText") String searchText);
}