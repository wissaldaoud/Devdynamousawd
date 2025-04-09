package com.pi.trainingprogram.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.repository.TrainingProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    public List<TrainingProgram> getAllTrainingPrograms() {
        return trainingProgramRepository.findAll();
    }

    public TrainingProgram getTrainingProgramById(int id) {
        return trainingProgramRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation introuvable"));
    }

    public TrainingProgram createTrainingProgram(TrainingProgram trainingProgram) {
        return trainingProgramRepository.save(trainingProgram);
    }

    public TrainingProgram updateTrainingProgram(int id, TrainingProgram trainingProgram) {
        if (trainingProgramRepository.existsById(id)) {
            trainingProgram.setId(id);
            return trainingProgramRepository.save(trainingProgram);
        }
        throw new EntityNotFoundException("Formation introuvable");
    }

    public void deleteTrainingProgram(int id) {
        trainingProgramRepository.deleteById(id);
    }

    public List<TrainingProgram> searchTrainingPrograms(String keyword) {
        return trainingProgramRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public List<TrainingProgram> getSortedPrograms(String sortBy) {
        return switch (sortBy) {
            case "price" -> trainingProgramRepository.findAllByOrderByPriceAsc();
            case "duration" -> trainingProgramRepository.findAllByOrderByDurationAsc();
            default -> trainingProgramRepository.findAll();
        };
    }

    public byte[] generatePdfForProgram(int id) throws IOException {
        TrainingProgram program = getTrainingProgramById(id);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Fiche de la formation"));
        doc.add(new Paragraph("Titre: " + program.getTitle()));
        doc.add(new Paragraph("Description: " + program.getDescription()));
        doc.add(new Paragraph("Durée: " + program.getDuration() + " jours"));
        doc.add(new Paragraph("Prix: " + program.getPrice() + " €"));

        doc.close();
        return out.toByteArray();
    }

    public TrainingProgram getRandomProgram() {
        List<TrainingProgram> list = trainingProgramRepository.findAll();
        if (list.isEmpty()) return null;
        return list.get(new Random().nextInt(list.size()));
    }
}
