package com.informationsecurity.PasteBinService.repositories;

import com.informationsecurity.PasteBinService.models.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {
    Paste findPasteById(long id);
}
