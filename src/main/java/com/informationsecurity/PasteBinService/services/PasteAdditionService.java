package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.PasteAddition;
import com.informationsecurity.PasteBinService.repositories.PasteAdditionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PasteAdditionService {

    private PasteAdditionRepository pasteAdditionRepository;

    public void save(PasteAddition pasteAddition) {
        pasteAdditionRepository.save(pasteAddition);
    }

    public List<PasteAddition> findAll() {
        return pasteAdditionRepository.findAll();
    }

}
