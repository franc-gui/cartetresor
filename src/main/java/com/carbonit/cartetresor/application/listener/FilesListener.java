package com.carbonit.cartetresor.application.listener;

import com.carbonit.cartetresor.application.job.ReadFilesJob;
import com.carbonit.cartetresor.domain.commons.entity.Directory;
import com.carbonit.cartetresor.domain.commons.entity.Filename;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

@Slf4j
@Service
public class FilesListener implements InitializingBean {

    private final ReadFilesJob jobProcessCarteTresor;

    public FilesListener(ReadFilesJob jobProcessCarteTresor) {
        this.jobProcessCarteTresor = jobProcessCarteTresor;
    }

    private void launchFilesListener() throws IOException, InterruptedException {
        log.info("launchFilesListener started");
        Path input = Files.createDirectories(Paths.get(new ClassPathResource(Directory.INPUT.getDirectory()).getPath()));
        Files.createDirectories(Paths.get(new ClassPathResource(Directory.OUTPUT.getDirectory()).getPath()));
        WatchService watchService = FileSystems.getDefault().newWatchService();
        input.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                log.info("Nouveau fichier créé : " + event.context());
                if (Filename.CARTE_TRESOR.getFullFilename().equals(event.context().toString())) {
                    this.runTaskProcessCarteTresor();
                } else {
                    log.warn("Ce fichier n'est pas géré " + event.context());
                }
            }
            key.reset();
        }
    }

    void runTaskProcessCarteTresor() {
        jobProcessCarteTresor.run();
    }

    @Override
    public void afterPropertiesSet() throws IOException, InterruptedException {
        launchFilesListener();
    }
}
