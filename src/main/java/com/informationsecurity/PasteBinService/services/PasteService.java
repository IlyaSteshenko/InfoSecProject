package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Paste;
import com.informationsecurity.PasteBinService.repositories.PasteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PasteService {

    private PasteRepository pasteRepository;

    public List<Paste> getAll() {
        return pasteRepository.findAll();
    }

    public void save(Paste paste) {
        pasteRepository.save(paste);
    }

    public Paste findById(Long id) {
        return pasteRepository.findPasteById(id);
    }

    public List<Paste> findPatentsWithText(String searchText) {
        return pasteRepository.findPatentsWithText(searchText);
    }

    public List<Paste> findPatentsByAuthor(String authorName) {
        return pasteRepository.findPatentsByAuthor(authorName);
    }
}
