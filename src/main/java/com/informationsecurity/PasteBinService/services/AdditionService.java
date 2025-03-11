package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Addition;
import com.informationsecurity.PasteBinService.repositories.AdditionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdditionService {

    private AdditionRepository additionRepository;

    public void save(Addition addition) {
        additionRepository.save(addition);
    }

    public List<Addition> findAdditionsWithPasteId(Long id) {
        return additionRepository.findAdditionsWithPasteId(id);
    }

}
