package com.carbonit.cartetresor.infrastructure.adapter.cartetresor;


import com.carbonit.cartetresor.domain.cartetresor.usecase.port.output.ProcessCarteTresorRepository;
import com.carbonit.cartetresor.infrastructure.commons.repository.FileRepository;
import com.carbonit.cartetresor.infrastructure.error.InfrastructureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessCarteTresorAdapter implements ProcessCarteTresorRepository {
    private final FileRepository fileRepository;

    @Override
    public List<String> readFile(String filename, String folder) {
        try {
            return fileRepository.readFile(filename, folder);
        } catch (URISyntaxException | IOException e) {
            throw new InfrastructureException(e);
        }
    }

    @Override
    public void deleteFile(String filename, String folder) {
        try {
            if (!fileRepository.deleteFile(filename, folder)) {
                log.warn("Le fichier %s/%s n'existait plus au moment de le supprimer".formatted(folder, filename));
            }
        } catch (URISyntaxException | IOException e) {
            throw new InfrastructureException(e);
        }
    }

    @Override
    public String writeFile(List<String> filelines, String filename, String folder) {
        try {
            return fileRepository.writeFile(filelines, filename, folder);
        } catch (URISyntaxException | IOException e) {
            throw new InfrastructureException(e);
        }
    }
}
