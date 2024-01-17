package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Commentaire;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.CommentaireRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentaireServiceTest {
    @Mock
    private CommentaireRepository repository;

    @Mock
    private SondageService sondageService;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private CommentaireService commentaireService;

    @Test
    @DisplayName("Test getBySondageId method")
    void testGetById() {
        Long sondageId = 1L;
        Commentaire sampleCommentaire = new Commentaire();
        when(repository.getAllBySondage(sondageId)).thenReturn(Collections.singletonList(sampleCommentaire));

        List<Commentaire> result = commentaireService.getBySondageId(sondageId);

        assertEquals(1, result.size(), "Number of commentaires should be 1");
        assertEquals(sampleCommentaire, result.get(0), "Returned Commentaire should match the mocked one");
    }

    @Test
    @DisplayName("Test addCommentaire method")
    void testAddCommentaire() {
        Long sondageId = 1L;
        Long participantId = 2L;
        Commentaire commentaireToCreate = new Commentaire();
        Commentaire expectedCommentaire = new Commentaire();

        when(sondageService.getById(sondageId)).thenReturn(new Sondage());
        when(participantService.getById(participantId)).thenReturn(new Participant());
        when(repository.save(any(Commentaire.class))).thenReturn(expectedCommentaire);

        Commentaire result = commentaireService.addCommantaire(sondageId, participantId, commentaireToCreate);

        assertDto(expectedCommentaire,result);

        verify(sondageService, times(1)).getById(sondageId);
        verify(participantService, times(1)).getById(participantId);
        verify(repository, times(1)).save(any(Commentaire.class));
    }

    @Test
    @DisplayName("Test update Commentaire")
    void testUpdate() {
        Long commentaireId = 1L;
        String expectedComment = "Updated Commentaire";
        Commentaire existingCommentaire = new Commentaire();
        existingCommentaire.setCommentaireId(commentaireId);

        Commentaire updatedCommentaire = new Commentaire();
        updatedCommentaire.setCommentaireId(commentaireId);
        updatedCommentaire.setCommentaire(expectedComment);

        when(repository.findById(commentaireId)).thenReturn(Optional.of(existingCommentaire));
        when(repository.save(any(Commentaire.class))).thenReturn(updatedCommentaire);

        Commentaire result = commentaireService.update(commentaireId, updatedCommentaire);

        assertDto(updatedCommentaire,result);
        assertEquals(expectedComment, result.getCommentaire());

        verify(repository, times(1)).findById(commentaireId);
        verify(repository, times(1)).save(updatedCommentaire);
    }

    @Test
    @DisplayName("Test update Non-Existent Commentaire")
    void testUpdateNonExistent() {
        Long commentaireId = 1L;
        Commentaire updatedCommentaire = new Commentaire();
        updatedCommentaire.setCommentaireId(commentaireId);
        updatedCommentaire.setCommentaire("Updated Commentaire");

        when(repository.findById(commentaireId)).thenReturn(Optional.empty());

        Commentaire result = commentaireService.update(commentaireId, updatedCommentaire);

        assertNull(result, "Commentaire should not be updated");

        verify(repository, times(1)).findById(commentaireId);
        verify(repository, never()).save(updatedCommentaire);
    }

    @Test
    @DisplayName("Test delete Commentaire")
    void testDelete() {
        Long commentaireId = 1L;
        Commentaire existingCommentaire = new Commentaire();
        existingCommentaire.setCommentaireId(commentaireId);

        when(repository.findById(commentaireId)).thenReturn(Optional.of(existingCommentaire));

        int result = commentaireService.delete(commentaireId);

        assertEquals(1, result, "Commentaire should be deleted");

        verify(repository, times(1)).findById(commentaireId);
        verify(repository, times(1)).deleteById(commentaireId);
    }

    @Test
    @DisplayName("Test delete Non-Existent Commentaire")
    void testDeleteNonExistent() {
        Long commentaireId = 1L;

        when(repository.findById(commentaireId)).thenReturn(Optional.empty());

        int result = commentaireService.delete(commentaireId);

        assertEquals(0, result, "Commentaire should not be deleted");

        verify(repository, times(1)).findById(commentaireId);
        verify(repository, never()).deleteById(commentaireId);
    }

    @Test
    @DisplayName("Test set/getParticipant method")
    void testGetPartipant() {
        Long sondageId = 2L;
        Sondage sampleSondage = new Sondage();
        sampleSondage.setSondageId(sondageId);

        Long commentaireId = 1L;
        Commentaire sampleCommentaire = new Commentaire();
        sampleCommentaire.setCommentaireId(commentaireId);
        sampleCommentaire.setSondage(sampleSondage);

        Participant sampleParticipant = new Participant();
        sampleCommentaire.setParticipant(sampleParticipant);

        sampleSondage.setCommentaires(Collections.singletonList(sampleCommentaire));

        when(repository.getAllBySondage(sondageId)).thenReturn(Collections.singletonList(sampleCommentaire));

        Participant setResult = commentaireService.getBySondageId(sondageId).get(0).getParticipant();

        Participant getResult = sampleCommentaire.getParticipant();

        assertEquals(sampleParticipant, setResult, "Returned Participant should match the mocked one");
        assertEquals(sampleParticipant, getResult, "Returned Participant should match the mocked one");
    }

    @Test
    @DisplayName("Test set/getSondage method")
    void testGetSondage() {
        Long commentaireId = 1L;
        Commentaire sampleCommentaire = new Commentaire();
        sampleCommentaire.setCommentaireId(commentaireId);

        Long sondageId = 2L;
        Sondage sampleSondage = new Sondage();
        sampleSondage.setSondageId(sondageId);
        sampleCommentaire.setSondage(sampleSondage);

        when(repository.getAllBySondage(sondageId)).thenReturn(Collections.singletonList(sampleCommentaire));

        Sondage setResult = commentaireService.getBySondageId(sondageId).get(0).getSondage();
        Sondage getResult = sampleCommentaire.getSondage();

        assertEquals(sampleSondage, setResult, "Returned Sondage should match the mocked one");
        assertEquals(sampleSondage, getResult, "Returned Sondage should match the mocked one");
    }
}
