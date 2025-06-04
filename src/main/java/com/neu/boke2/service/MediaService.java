package com.neu.boke2.service;

import com.neu.boke2.entity.Media;
import com.neu.boke2.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    public List<Media> findAll() {
        return mediaRepository.findAll();
    }

    public Optional<Media> findById(Long id) {
        return mediaRepository.findById(id);
    }

    public Media save(Media media) {
        return mediaRepository.save(media);
    }

    public void deleteById(Long id) {
        mediaRepository.deleteById(id);
    }
} 