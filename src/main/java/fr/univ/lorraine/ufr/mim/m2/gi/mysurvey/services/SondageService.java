package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.SondageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SondageService {

    private final SondageRepository repository;
    private final ParticipantService participantService;

    public SondageService(SondageRepository repository, ParticipantService p) {
        this.repository = repository;
        this.participantService = p;
    }

    public Sondage getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Sondage> getAll() {
        return repository.findAll();
    }

    public Sondage create(Long idParticipant, Sondage sondage) {
        sondage.setCreateBy(this.participantService.getById(idParticipant));
        return repository.save(sondage);
    }

    public Sondage update(Long id, Sondage sondage) {
        Optional<Sondage> existingSondageOptional = repository.findById(id);
        if (existingSondageOptional.isPresent()) {
            Sondage existingSondage = existingSondageOptional.get();

            sondage.setSondageId(id);
            sondage.setCreateBy(existingSondage.getCreateBy());
            return repository.save(sondage);
        }
        return null;
    }

    public int delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return 1;
        }
        return 0;
    }
}
