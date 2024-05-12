package com.carbonit.cartetresor.domain.cartetresor.usecase.process;

import com.carbonit.cartetresor.domain.cartetresor.entity.Aventurier;
import com.carbonit.cartetresor.domain.cartetresor.entity.Carte;
import com.carbonit.cartetresor.domain.cartetresor.entity.Position;
import com.carbonit.cartetresor.domain.cartetresor.entity.response.ProcessCarteTresorResponse;
import com.carbonit.cartetresor.domain.cartetresor.usecase.port.input.IProcessCarteTresor;
import com.carbonit.cartetresor.domain.cartetresor.usecase.port.output.ProcessCarteTresorRepository;
import com.carbonit.cartetresor.domain.commons.entity.Directory;
import com.carbonit.cartetresor.domain.commons.entity.Filename;
import com.carbonit.cartetresor.domain.commons.error.FunctionalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.carbonit.cartetresor.domain.cartetresor.entity.Orientation.valueOfOrientation;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessCarteTresorUseCase implements IProcessCarteTresor {
    private static final DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

    private final ProcessCarteTresorRepository repository;

    @Override
    public ProcessCarteTresorResponse process() {
        try {
            var inputFileLines = readAndDeleteInputCarteTresorFile();
            var carteAndAventuriersData = convertToCarteAndAventuriersData(inputFileLines);
            processMovements(carteAndAventuriersData.getLeft(), carteAndAventuriersData.getRight());
            var outputFilelines = convertToFilelines(carteAndAventuriersData.getLeft(), carteAndAventuriersData.getRight());
            var pathOutputFile = writeOutputFile(outputFilelines);
            return ProcessCarteTresorResponse.defaultResponse(pathOutputFile);
        } catch (RuntimeException e) {
            return ProcessCarteTresorResponse.errorResponse(e.getMessage());
        }

    }

    private List<String> readAndDeleteInputCarteTresorFile() {
        var inputFilelines = repository.readFile(Filename.CARTE_TRESOR.getFullFilename(), Directory.INPUT.getDirectory());
        repository.deleteFile(Filename.CARTE_TRESOR.getFullFilename(), Directory.INPUT.getDirectory());
        return inputFilelines;
    }

    private Pair<Carte, List<Aventurier>> convertToCarteAndAventuriersData(List<String> fileLines) {
        final Carte carte = new Carte();
        List<Aventurier> aventuriers = new ArrayList<>();
        fileLines.forEach(line -> {
            if (!line.startsWith("#")) {
                var parts = line.split(" - ");
                switch (parts[0]) {
                    case "C" -> {
                        carte.setLargeur(Integer.parseInt(parts[1]));
                        carte.setHauteur(Integer.parseInt(parts[2]));
                    }
                    case "M" -> carte.addMontagne(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    case "T" ->
                            carte.addTresor(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                    case "A" ->
                            aventuriers.add(new Aventurier(parts[1], new Position(Integer.parseInt(parts[2]), Integer.parseInt(parts[3])), valueOfOrientation(parts[4]), parts[5], 0));
                    default ->
                            throw new FunctionalErrorException("Ce caractère n'est pas reconnu : %s".formatted(parts[0]));
                }
            }
        });
        return Pair.of(carte, aventuriers);
    }

    private void processMovements(Carte carte, List<Aventurier> aventuriers) {
        aventuriers.forEach(aventurier -> aventurier.processSequence(carte));
    }

    private List<String> convertToFilelines(Carte carte, List<Aventurier> aventuriers) {
        var filelines = new ArrayList<String>();
        filelines.add("C - " + carte.getLargeur() + " - " + carte.getHauteur());
        carte.getMontagnes().forEach(montagne -> filelines.add("M - " + montagne.getX() + " - " + montagne.getY()));
        carte.getTresors().forEach((key, value) -> filelines.add("T - " + key.getX() + " - " + key.getY() + " - " + value));
        aventuriers.forEach(aventurier -> filelines.add("A - " + aventurier.getNom() + " - " + aventurier.getPosition().getX() + " - " +
                aventurier.getPosition().getY() + " - " + aventurier.getOrientation().getOrientation() + " - " + aventurier.getTresorsPickedNb()));
        return filelines;
    }

    private String writeOutputFile(List<String> filelines) {
        return repository.writeFile(filelines,
                Filename.CARTE_TRESOR.getOutputFullFilename(LocalDateTime.now().format(fileFormatter)),
                Directory.OUTPUT.getDirectory());
    }
}
