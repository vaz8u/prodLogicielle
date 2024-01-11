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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    @Test
    @DisplayName("Test update Sondage")
    void testUpdate() {
        Long sondageId = 1L;
        Sondage existingSondage = new Sondage();
        existingSondage.setSondageId(sondageId);

        Sondage updatedSondage = new Sondage();
        updatedSondage.setSondageId(sondageId);
        updatedSondage.setNom("Updated Sondage");

        when(repository.findById(sondageId)).thenReturn(Optional.of(existingSondage));
        when(repository.save(any(Sondage.class))).thenReturn(updatedSondage);

        Sondage result = sondageService.update(sondageId, updatedSondage);

        assertEquals(updatedSondage, result, "Sondage should be updated");
        verify(repository, times(1)).findById(sondageId);
        verify(repository, times(1)).save(updatedSondage);
    }

    @Test
    @DisplayName("Test update Non-Existent Sondage")
    void testUpdateNonExistentSondage() {
        Long sondageId = 1L;
        Sondage updatedSondage = new Sondage();
        updatedSondage.setSondageId(sondageId);
        updatedSondage.setNom("Updated Sondage");

        when(repository.findById(sondageId)).thenReturn(Optional.empty());

        Sondage result = sondageService.update(sondageId, updatedSondage);

        assertNull(result, "Sondage should not be updated");
        verify(repository, times(1)).findById(sondageId);
        verify(repository, never()).save(updatedSondage);
    }

    @Test
    @DisplayName("Test delete Sondage")
    void testDelete() {
        Long sondageId = 1L;
        Sondage existingSondage = new Sondage();
        existingSondage.setSondageId(sondageId);

        when(repository.findById(sondageId)).thenReturn(Optional.of(existingSondage));

        int result = sondageService.delete(sondageId);

        assertEquals(1, result, "Sondage should be deleted");
        verify(repository, times(1)).findById(sondageId);
        verify(repository, times(1)).deleteById(sondageId);
    }

    @Test
    @DisplayName("Test delete Non-Existent Sondage")
    void testDeleteNonExistentSondage() {
        Long sondageId = 1L;

        when(repository.findById(sondageId)).thenReturn(Optional.empty());

        int result = sondageService.delete(sondageId);

        assertEquals(0, result, "Sondage should not be deleted");
        verify(repository, times(1)).findById(sondageId);
        verify(repository, never()).deleteById(sondageId);
    }
}
