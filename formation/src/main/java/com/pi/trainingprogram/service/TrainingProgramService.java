package com.pi.trainingprogram.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.pi.trainingprogram.entities.TrainingProgram;
import com.pi.trainingprogram.repository.TrainingProgramRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainingProgramService {
    @Autowired
    private MailService mailService;
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
    public Map<String, Object> getStatistics() {
        List<TrainingProgram> all = trainingProgramRepository.findAll();
        Map<String, Object> stats = new HashMap<>();

        stats.put("total", all.size());
        stats.put("averagePrice", all.stream().mapToDouble(TrainingProgram::getPrice).average().orElse(0));
        stats.put("averageDuration", all.stream().mapToInt(TrainingProgram::getDuration).average().orElse(0));

        return stats;
    }
    public Map<String, Long> getCountByDurationRanges() {
        List<TrainingProgram> programs = trainingProgramRepository.findAll();
        Map<String, Long> result = new HashMap<>();

        result.put("Short (<5h)", programs.stream().filter(p -> p.getDuration() < 5).count());
        result.put("Medium (5-10h)", programs.stream().filter(p -> p.getDuration() >= 5 && p.getDuration() <= 10).count());
        result.put("Long (>10h)", programs.stream().filter(p -> p.getDuration() > 10).count());

        return result;
    }
    public List<TrainingProgram> filterPrograms(Double minPrice, Double maxPrice, Integer minDuration, Integer maxDuration) {
        return trainingProgramRepository.findAll().stream()
                .filter(p -> (minPrice == null || p.getPrice() >= minPrice))
                .filter(p -> (maxPrice == null || p.getPrice() <= maxPrice))
                .filter(p -> (minDuration == null || p.getDuration() >= minDuration))
                .filter(p -> (maxDuration == null || p.getDuration() <= maxDuration))
                .collect(Collectors.toList());
    }

    public String faqBot(String question) {
        if (question == null || question.isEmpty()) return "Veuillez poser une question.";

        question = question.toLowerCase();

        // Synonymes groupés
        if (contains(question, "inscrire", "m’inscrire", "m'inscrire", "enregistrer")) {
            return "Pour vous inscrire, cliquez sur le bouton 'S'inscrire' dans la page de la formation.";
        }

        if (contains(question, "prix", "coût", "tarif")) {
            return "Les prix varient selon la formation. Consultez la page des détails.";
        }

        if (contains(question, "certificat", "attestation", "diplôme")) {
            return "Oui, un certificat est généré automatiquement à la fin de la formation.";
        }

        if (contains(question, "durée", "temps", "combien de temps")) {
            return "Chaque formation affiche sa durée dans les détails : en général entre 2 et 10 heures.";
        }

        if (contains(question, "recommandation", "suggère", "conseille", "me conseillez")) {
            return "Notre système vous recommande automatiquement une formation selon vos intérêts.";
        }

        if (contains(question, "niveau", "débutant", "avancé", "intermédiaire")) {
            return "Les formations sont classées par niveaux : Débutant, Intermédiaire ou Avancé.";
        }

        if (contains(question, "paiement", "payer", "moyen de paiement")) {
            return "Le paiement se fait via Stripe par carte bancaire. Il est 100% sécurisé.";
        }

        if (contains(question, "support", "aide", "contacter")) {
            return "Vous pouvez contacter notre équipe via le formulaire de contact ou par e-mail.";
        }

        return "Je suis désolé, je n’ai pas compris votre question. Veuillez reformuler.";
    }

    // Méthode utilitaire pour gérer les synonymes
    private boolean contains(String question, String... keywords) {
        return Arrays.stream(keywords).anyMatch(question::contains);
    }



    public List<TrainingProgram> recommendByInterest(String interest) {
        System.out.println(">>> Intérêt reçu : " + interest);
        List<TrainingProgram> list = trainingProgramRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(interest, interest);
        System.out.println(">>> Résultat trouvé : " + list.size() + " formations");
        return list;
    }
    public byte[] generateCertificateForUser(int programId, String userName) throws IOException {
        TrainingProgram program = getTrainingProgramById(programId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText(" Certificat de Participation");
        contentStream.setFont(PDType1Font.HELVETICA, 14);
        contentStream.newLineAtOffset(0, -40);
        contentStream.showText("Ce certificat est décerné à : " + userName);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Pour avoir complété la formation : " + program.getTitle());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Durée : " + program.getDuration() + " heures");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Date : " + LocalDate.now());
        contentStream.newLineAtOffset(0, -40);
        contentStream.showText("Félicitations pour votre engagement et votre réussite !");
        contentStream.endText();
        contentStream.close();

        document.save(out);
        document.close();
        return out.toByteArray();

    }
    public List<TrainingProgram> getProgramsByCategory(String category) {
        return trainingProgramRepository.findByCategoryIgnoreCase(category);
    }

    public Map<String, Long> countByCategory() {
        return trainingProgramRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory() != null ? p.getCategory() : "Non spécifiée",
                        Collectors.counting()
                ));
    }





    public byte[] exportProgramsByCategoryToPdf(String category) throws IOException {
        List<TrainingProgram> programs = trainingProgramRepository.findByCategoryIgnoreCase(category);

        if (programs.isEmpty()) {
            throw new IllegalArgumentException("Aucune formation trouvée dans la catégorie : " + category);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("📘 Formations - Catégorie : " + category).setBold().setFontSize(16));
        doc.add(new Paragraph("Nombre de formations : " + programs.size()).setFontSize(12));

        for (TrainingProgram p : programs) {
            doc.add(new Paragraph("--------------------------------"));
            doc.add(new Paragraph("Titre : " + p.getTitle()));
            doc.add(new Paragraph("Description : " + p.getDescription()));
            doc.add(new Paragraph("Durée : " + p.getDuration() + " h"));
            doc.add(new Paragraph("Prix : " + p.getPrice() + " " + p.getCurrency()));
            doc.add(new Paragraph(""));
        }

        doc.close();
        return out.toByteArray();
    }

    public byte[] exportProgramsByCategoryToExcel(String category) throws IOException {
        List<TrainingProgram> programs = trainingProgramRepository.findByCategoryIgnoreCase(category);

        if (programs.isEmpty()) {
            throw new IllegalArgumentException("Aucune formation trouvée dans la catégorie : " + category);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Formations - " + category);

        // En-têtes
        Row header = sheet.createRow(0);
        String[] columns = {"ID", "Titre", "Description", "Durée", "Prix", "Devise", "Catégorie"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Lignes
        int rowIdx = 1;
        for (TrainingProgram p : programs) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getTitle());
            row.createCell(2).setCellValue(p.getDescription());
            row.createCell(3).setCellValue(p.getDuration());
            row.createCell(4).setCellValue(p.getPrice());
            row.createCell(5).setCellValue(p.getCurrency() != null ? p.getCurrency().name() : "");
            row.createCell(6).setCellValue(p.getCategory());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
    public void sendCertificateByEmail(int programId, String name, String email) throws IOException, MessagingException {
        byte[] pdf = generateCertificateForUser(programId, name);
        String subject = "🎓 Votre certificat de formation";
        String body = "Bonjour " + name + ",\n\nVoici votre certificat en pièce jointe.\n\nMerci pour votre participation.";
        mailService.sendCertificate(email, subject, body, pdf, "certificat_" + name + ".pdf");
    }
    public String exportAllProgramsToCsv() {
        List<TrainingProgram> programs = getAllTrainingPrograms();
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Title,Description,Duration,Price\n");
        for (TrainingProgram program : programs) {
            csvBuilder.append(program.getId()).append(",")
                    .append(program.getTitle()).append(",")
                    .append(program.getDescription()).append(",")
                    .append(program.getDuration()).append(",")
                    .append(program.getPrice()).append("\n");
        }
        return csvBuilder.toString();
    }











}
