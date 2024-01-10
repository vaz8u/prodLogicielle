package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.SondageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SondageServiceTest {

    @Mock
    private SondageRepository repository;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private SondageService sondageService;

    @Test
    @DisplayName("Test getById method")
    void testGetSondageById() {
        Sondage sampleSondage = new Sondage();
        when(repository.getById(anyLong())).thenReturn(sampleSondage);

        Sondage result = sondageService.getById(1L);

        assertNotNull(result, "Sondage should not be null");
        assertEquals(sampleSondage, result, "Returned Sondage should match the mocked one");
    }

    @Test
    @DisplayName("Test getAll method")
    void testGetAllSondages() {
        Sondage sampleSondage = new Sondage();
        when(repository.findAll()).thenReturn(List.of(sampleSondage));

        List<Sondage> result = sondageService.getAll();

        assertEquals(1, result.size(), "Number of sondages should be 1");
        assertEquals(sampleSondage, result.get(0), "Returned Sondage should match the mocked one");
    }

    @Test
    @DisplayName("Test create method")
    void testCreateSondage() {
        Participant sampleParticipant = new Participant();
        when(participantService.getById(anyLong())).thenReturn(sampleParticipant);

        Sondage sondageToCreate = new Sondage(/* initialize with sample data */);
        when(repository.save(any(Sondage.class))).thenReturn(sondageToCreate);

        Sondage result = sondageService.create(1L, sondageToCreate);

        assertNotNull(result, "Created sondage should not be null");
        assertEquals(sondageToCreate, result, "Returned Sondage should match the one to create");
    }

}
