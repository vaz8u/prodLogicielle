package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Commentaire;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.SondageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testGetById() {
        Sondage sampleSondage = new Sondage();
        when(repository.findById(anyLong())).thenReturn(Optional.of(sampleSondage));

        Sondage result = sondageService.getById(1L);

        assertDto(sampleSondage, result);
    }

    @Test
    @DisplayName("Test getAll method")
    void testGetAll() {
        Sondage sampleSondage = new Sondage();
        when(repository.findAll()).thenReturn(List.of(sampleSondage));

        List<Sondage> result = sondageService.getAll();

        assertEquals(1, result.size(), "Number of sondages should be 1");
        assertEquals(sampleSondage, result.get(0), "Returned Sondage should match the mocked one");
    }

    @Test
    @DisplayName("Test create method")
    void testCreate() {
        Participant sampleParticipant = new Participant();
        when(participantService.getById(anyLong())).thenReturn(sampleParticipant);

        Sondage sondageToCreate = new Sondage(/* initialize with sample data */);
        when(repository.save(any(Sondage.class))).thenReturn(sondageToCreate);

        Sondage result = sondageService.create(1L, sondageToCreate);

        assertDto(sondageToCreate, result);
    }

    @Test
    @DisplayName("Test create method with parameters")
    void testCreateWithParameters() {
        Participant sampleParticipant = new Participant();
        sampleParticipant.setParticipantId(1L);
        when(participantService.getById(anyLong())).thenReturn(sampleParticipant);

        long sondageId = 1L;
        String nom = "Nom";
        String description = "Description";
        Date fin = new Date();
        boolean cloture = false;
        List<Commentaire> commentaires = null;
        List<DateSondage> dateSondage = null;

        Sondage sondageToCreate = new Sondage(sondageId, nom, description, fin, cloture, commentaires, dateSondage, sampleParticipant);
        when(repository.save(any(Sondage.class))).thenReturn(sondageToCreate);

        Sondage result = sondageService.create(1L, sondageToCreate);

        assertDto(sondageToCreate, result);
    }

    @Test
    @DisplayName("Test update Sondage")
    void testUpdate() {
        Long sondageId = 1L;
        String expectedName = "Updated Sondage";
        Sondage existingSondage = new Sondage();
        existingSondage.setSondageId(sondageId);

        Sondage updatedSondage = new Sondage();
        updatedSondage.setSondageId(sondageId);
        updatedSondage.setNom(expectedName);

        when(repository.findById(sondageId)).thenReturn(Optional.of(existingSondage));
        when(repository.save(any(Sondage.class))).thenReturn(updatedSondage);

        Sondage result = sondageService.update(sondageId, updatedSondage);

        assertDto(updatedSondage, result);
        assertEquals(expectedName, result.getNom());

        verify(repository, times(1)).findById(sondageId);
        verify(repository, times(1)).save(updatedSondage);
    }

    @Test
    @DisplayName("Test update Non-Existent Sondage")
    void testUpdateNonExistent() {
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
    void testDeleteNonExistent() {
        Long sondageId = 1L;

        when(repository.findById(sondageId)).thenReturn(Optional.empty());

        int result = sondageService.delete(sondageId);

        assertEquals(0, result, "Sondage should not be deleted");
        verify(repository, times(1)).findById(sondageId);
        verify(repository, never()).deleteById(sondageId);
    }

    @Test
    @DisplayName("Test set/get-Description method")
    void testGetDescription() {
        String description = "Description";

        Sondage sampleSondage = new Sondage();
        sampleSondage.setSondageId(1L);
        sampleSondage.setDescription(description);
        when(repository.getById(anyLong())).thenReturn(sampleSondage);

        String result = sondageService.getById(1L).getDescription();

        assertEquals(description, result, "Description should be " + description);
    }

    @Test
    @DisplayName("Test set/get-Commentaires method")
    void testGetCommentaires() {
        List<Commentaire> commentaires = null;

        Sondage sampleSondage = new Sondage();
        sampleSondage.setSondageId(1L);
        sampleSondage.setCommentaires(commentaires);
        when(repository.getById(anyLong())).thenReturn(sampleSondage);

        List<Commentaire> result = sondageService.getById(1L).getCommentaires();

        assertEquals(commentaires, result, "Commentaires should be " + commentaires);
    }

    @Test
    @DisplayName("Test set/get-DateSondage method")
    void testGetDateSondage() {
        List<DateSondage> dateSondage = null;

        Sondage sampleSondage = new Sondage();
        sampleSondage.setSondageId(1L);
        sampleSondage.setDateSondage(dateSondage);
        when(repository.getById(anyLong())).thenReturn(sampleSondage);

        List<DateSondage> result = sondageService.getById(1L).getDateSondage();

        assertEquals(dateSondage, result, "DateSondage should be " + dateSondage);
    }

    @Test
    @DisplayName("Test set/get-CreateBy method")
    void testGetCreateBy() {
        Participant createBy = new Participant();

        Sondage sampleSondage = new Sondage();
        sampleSondage.setSondageId(1L);
        sampleSondage.setCreateBy(createBy);
        when(repository.getById(anyLong())).thenReturn(sampleSondage);

        Participant result = sondageService.getById(1L).getCreateBy();

        assertEquals(createBy, result, "CreateBy should be " + createBy);
    }
}
