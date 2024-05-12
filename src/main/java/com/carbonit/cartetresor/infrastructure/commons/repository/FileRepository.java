package com.carbonit.cartetresor.infrastructure.commons.repository;

import com.carbonit.cartetresor.domain.commons.entity.Directory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class FileRepository {

    public List<String> readFile(String filename, String folder) throws URISyntaxException, IOException {
        Path path = Paths.get(new ClassPathResource("%s/%s".formatted(Objects.requireNonNull(folder), Objects.requireNonNull(filename))).getPath());

        Stream<String> lines = Files.lines(path);
        List<String> data = lines.toList();
        lines.close();
        return data;
    }

    public boolean deleteFile(String filename, String folder) throws URISyntaxException, IOException {
        Path path = Paths.get(new ClassPathResource("%s/%s".formatted(Objects.requireNonNull(folder), Objects.requireNonNull(filename))).getPath());
        return Files.deleteIfExists(path);
    }

    public String writeFile(List<String> filelines, String filename, String folder) throws URISyntaxException, IOException {
        Path path = Paths.get(new ClassPathResource("%s/%s".formatted(Objects.requireNonNull(folder), Objects.requireNonNull(filename))).getPath());
        Files.deleteIfExists(path);
        Files.createFile(path);
        for (String line : filelines) {
            Files.writeString(path, line + System.lineSeparator(),
                    StandardOpenOption.APPEND);
        }

        return path.toString();
    }
}
