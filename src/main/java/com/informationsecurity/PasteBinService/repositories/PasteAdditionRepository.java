package com.informationsecurity.PasteBinService.repositories;

import com.informationsecurity.PasteBinService.models.PasteAddition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasteAdditionRepository extends JpaRepository<PasteAddition, Long> {
}
