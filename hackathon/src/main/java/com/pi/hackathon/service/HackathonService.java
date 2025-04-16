package com.pi.hackathon.service;

import com.pi.hackathon.clients.PostClient;
import com.pi.hackathon.dtos.Post;
import com.pi.hackathon.entities.Hackathon;
import com.pi.hackathon.entities.HackathonParticipation;
import com.pi.hackathon.entities.NiveauDifficulte;
import com.pi.hackathon.entities.NiveauImportance;
import com.pi.hackathon.repository.HackathonParticipationRepository;
import com.pi.hackathon.repository.HackathonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HackathonService {
    @Autowired
    private PostClient postClient;
    private final HackathonRepository hackathonRepository;
    private final HackathonParticipationRepository hackathonParticipationRepository;
    private MailService mailService;
    public Hackathon createHackathon(Hackathon hackathon) {
        this.mailService.sendEmail("slimfady.hanafi7@gmail.com","Ajout d'un Hackathon - Notification","Nous souhaitons vous informer qu'un hackathon a été ajouté");
        return hackathonRepository.save(hackathon);
    }

    // Get a Hackathon by ID
    public Optional<Hackathon> getHackathonById(int id) {
        return hackathonRepository.findById(id);
    }

    // Get all Hackathons with pagination
    public Page<Hackathon> getAllHackathons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hackathonRepository.findAll(pageable);
    }

    // Update an existing Hackathon
    public Hackathon updateHackathon(int id, Hackathon updatedHackathon) {
        return hackathonRepository.findById(id).map(hackathon -> {
            hackathon.setTitle(updatedHackathon.getTitle());
            hackathon.setDescription(updatedHackathon.getDescription());
            hackathon.setLocation(updatedHackathon.getLocation());
            hackathon.setStartTime(updatedHackathon.getStartTime());
            hackathon.setEndTime(updatedHackathon.getEndTime());
            hackathon.setNbrPlaces(updatedHackathon.getNbrPlaces());
            hackathon.setNiveauDifficulte(updatedHackathon.getNiveauDifficulte());
            hackathon.setNiveauImportance(updatedHackathon.getNiveauImportance());
            this.mailService.sendEmail("slimfady.hanafi7@gmail.com","Modification d'un Hackathon - Notification","Nous souhaitons vous informer qu'un hackathon a été modifié");
            return hackathonRepository.save(hackathon);
        }).orElseThrow(() -> new RuntimeException("Hackathon not found"));
    }

    // Delete a Hackathon
    public void deleteHackathon(int id) {
        hackathonRepository.deleteById(id);
    }

    // Get all participants of a Hackathon
    public List<HackathonParticipation> getParticipants(int hackathonId) {
        return hackathonRepository.findById(hackathonId)
                .map(Hackathon::getParticipations)
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));
    }

    public Map<String, Object> getStatisticsNiceauDiffuclte() {
        int participationTRESDIFFICILE = 0;
        int participationDIFFICILE = 0;
        int participationMOYEN = 0;
        int participationFACILE = 0;

        int participationTRESIMPORTANT = 0;
        int participationIMPORTANT = 0;
        int participationNMOYEN = 0;
        int participationPEUIMPORTANT = 0;

        int hackthonTRESDIFFICILE = 0;
        int hackthonDIFFICILE = 0;
        int hackthonMOYEN = 0;
        int hackthonFACILE = 0;

        int hackthonTRESIMPORTANT = 0;
        int hackthonIMPORTANT = 0;
        int hackthonNMOYEN = 0;
        int hackthonPEUIMPORTANT = 0;

        List<HackathonParticipation> listParticipations = hackathonParticipationRepository.findAll();
        List<Hackathon> listHackathons = hackathonRepository.findAll();


        for (HackathonParticipation hp : listParticipations) {
            if (hp.getHackathon().getNiveauDifficulte() == NiveauDifficulte.TRESDIFFICILE) {
                participationTRESDIFFICILE++;
            } else if (hp.getHackathon().getNiveauDifficulte() == NiveauDifficulte.DIFFICILE) {
                participationDIFFICILE++;
            } else if (hp.getHackathon().getNiveauDifficulte() == NiveauDifficulte.MOYEN) {
                participationMOYEN++;
            } else if (hp.getHackathon().getNiveauDifficulte() == NiveauDifficulte.FACILE) {
                participationFACILE++;
            }

            if (hp.getHackathon().getNiveauImportance() == NiveauImportance.TRESIMPORTANT) {
                participationTRESIMPORTANT++;
            } else if (hp.getHackathon().getNiveauImportance() == NiveauImportance.IMPORTANT) {
                participationIMPORTANT++;
            } else if (hp.getHackathon().getNiveauImportance() == NiveauImportance.MOYEN) {
                participationNMOYEN++;
            } else if (hp.getHackathon().getNiveauImportance() == NiveauImportance.PEUIMPORTANT) {
                participationPEUIMPORTANT++;
            }
        }


        for (Hackathon h : listHackathons) {
            if (h.getNiveauDifficulte() == NiveauDifficulte.TRESDIFFICILE) {
                hackthonTRESDIFFICILE++;
            } else if (h.getNiveauDifficulte() == NiveauDifficulte.DIFFICILE) {
                hackthonDIFFICILE++;
            } else if (h.getNiveauDifficulte() == NiveauDifficulte.MOYEN) {
                hackthonMOYEN++;
            } else if (h.getNiveauDifficulte() == NiveauDifficulte.FACILE) {
                hackthonFACILE++;
            }

            if (h.getNiveauImportance() == NiveauImportance.TRESIMPORTANT) {
                hackthonTRESIMPORTANT++;
            } else if (h.getNiveauImportance() == NiveauImportance.IMPORTANT) {
                hackthonIMPORTANT++;
            } else if (h.getNiveauImportance() == NiveauImportance.MOYEN) {
                hackthonNMOYEN++;
            } else if (h.getNiveauImportance() == NiveauImportance.PEUIMPORTANT) {
                hackthonPEUIMPORTANT++;
            }
        }


        Map<String, Object> responseNiveauDifficulteParticipation = new HashMap<>();
        responseNiveauDifficulteParticipation.put("count", Arrays.asList(participationTRESDIFFICILE, participationDIFFICILE, participationMOYEN, participationFACILE));
        responseNiveauDifficulteParticipation.put("labels", Arrays.asList("TRESDIFFICILE", "DIFFICILE", "MOYEN", "FACILE"));

        Map<String, Object> responseNiveauImportanceParticipation = new HashMap<>();
        responseNiveauImportanceParticipation.put("count", Arrays.asList(participationTRESIMPORTANT, participationIMPORTANT, participationNMOYEN, participationPEUIMPORTANT));
        responseNiveauImportanceParticipation.put("labels", Arrays.asList("TRESIMPORTANT", "IMPORTANT", "MOYEN", "PEUIMPORTANT"));

        Map<String, Object> responseNiveauDifficulteHackathon = new HashMap<>();
        responseNiveauDifficulteHackathon.put("count", Arrays.asList(hackthonTRESDIFFICILE, hackthonDIFFICILE, hackthonMOYEN, hackthonFACILE));
        responseNiveauDifficulteHackathon.put("labels", Arrays.asList("TRESDIFFICILE", "DIFFICILE", "MOYEN", "FACILE"));

        Map<String, Object> responseNiveauImportanceHackathon = new HashMap<>();
        responseNiveauImportanceHackathon.put("count", Arrays.asList(hackthonTRESIMPORTANT, hackthonIMPORTANT, hackthonNMOYEN, hackthonPEUIMPORTANT));
        responseNiveauImportanceHackathon.put("labels", Arrays.asList("TRESIMPORTANT", "IMPORTANT", "MOYEN", "PEUIMPORTANT"));


        Map<String, Object> response = new HashMap<>();
        response.put("NiveauDifficulteParticipation", responseNiveauDifficulteParticipation);
        response.put("NiveauImportanceParticipation", responseNiveauImportanceParticipation);
        response.put("NiveauDifficulteHackathon", responseNiveauDifficulteHackathon);
        response.put("NiveauImportanceHackathon", responseNiveauImportanceHackathon);
        return response;
    }

    // @Scheduled(cron = "0 0 8 * * ?")
    // @Scheduled(cron = "*/30 * * * * ?")
    public void executeDailyTask() {
        List<Hackathon> listHackathons = hackathonRepository.findAll();

        Date currentDate = new Date();
        Date nextDay = new Date(currentDate.getTime() + TimeUnit.DAYS.toMillis(1));

        System.out.println(nextDay + " Next Day");

        for (Hackathon hackathon : listHackathons) {
            Date startTime = hackathon.getStartTime();

            // Calculate the difference in days
            long diffInMillies = startTime.getTime() - nextDay.getTime();
            long daysLeftUntilForum = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (daysLeftUntilForum <= 2) {
                List<HackathonParticipation> listeParticipations = hackathonParticipationRepository.findByHackathonId(hackathon.getId());

                for (HackathonParticipation hp : listeParticipations) {
                    this.mailService.sendEmail(
                            "slimfady.hanafi7@gmail.com",
                            "Rappel de participation à un Hackathon - Notification",
                            "Nous souhaitons vous informer qu'un hackathon auquel vous participez commencera bientôt."
                    );
                }
            }
        }
    }

    public Page<Post> getAllPosts() {
        return postClient.getAllPosts(0,10,"id","asc");
    }

    public Post getPostById(int id) {
        return postClient.getPostById(id);
    }

    public List<Post> getBestPosts(int candidateId) {
        Hackathon hackathon = hackathonRepository.findById(candidateId).get();
        return hackathon.getBestPosts().stream()
                .map(postClient::getPostById)
                .collect(Collectors.toList());
    }
    public void saveBestPost(int postId, int hackthonId) {
        Hackathon hackathon = hackathonRepository.findById(hackthonId).get();
        hackathon.getBestPosts().add(postId);
        hackathonRepository.save(hackathon);
    }

}
