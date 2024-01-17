package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Commentaire;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondee;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.ParticipantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {
    @Mock
    private ParticipantRepository repository;

    @InjectMocks
    private ParticipantService participantService;

    @Test
    @DisplayName("Test getById method")
    void testGetById() {
        Participant sampleParticipant = new Participant();
        when(repository.getById(anyLong())).thenReturn(sampleParticipant);

        Participant result = participantService.getById(1L);

        assertDto(sampleParticipant,result);
    }

    @Test
    @DisplayName("Test getNom method")
    void testGetName() {
        String nom = "John";

        Participant sampleParticipant = new Participant(1L, "John", "Smith");

        String result = sampleParticipant.getNom();

        assertEquals(nom, result, "Nom should be " + nom);
    }

    @Test
    @DisplayName("Test getAll method")
    void testGetAll() {
        Participant sampleParticipant = new Participant();
        when(repository.findAll()).thenReturn(List.of(sampleParticipant));

        List<Participant> result = participantService.getAll();

        assertEquals(1, result.size(), "Number of participants should be 1");
        assertEquals(sampleParticipant, result.get(0), "Returned Participant should match the mocked one");
    }

    @Test
    @DisplayName("Test create method")
    void testCreate() {
        Participant participantToCreate = new Participant();
        when(repository.save(any(Participant.class))).thenReturn(participantToCreate);

        Participant result = participantService.create(participantToCreate);

        assertDto(participantToCreate,result);
    }

    @Test
    @DisplayName("Test create method with parameters")
    void testCreateWithParameters() {
        long participantId = 1L;
        String prenom = "John";
        String nom = "Smith";

        Participant participantToCreate = new Participant(participantId, prenom, nom);

        when(repository.save(any(Participant.class))).thenReturn(participantToCreate);

        Participant result = participantService.create(new Participant());
        result.setParticipantId(participantId);
        result.setPrenom(prenom);
        result.setNom(nom);

        assertDto(participantToCreate,result);
    }

    @Test
    @DisplayName("Test update Participant")
    void testUpdate() {
        Long participantId = 1L;
        String expectedFirstname = "John";
        Participant existingParticipant = new Participant();
        existingParticipant.setParticipantId(participantId);

        Participant updatedParticipant = new Participant();
        updatedParticipant.setParticipantId(participantId);
        updatedParticipant.setPrenom(expectedFirstname);

        when(repository.findById(participantId)).thenReturn(Optional.of(existingParticipant));
        when(repository.save(any(Participant.class))).thenReturn(updatedParticipant);

        Participant result = participantService.update(participantId, updatedParticipant);

        assertDto(updatedParticipant, result);
        assertEquals(expectedFirstname, result.getPrenom(), "Firstname should be updated");

        verify(repository, times(1)).findById(participantId);
        verify(repository, times(1)).save(updatedParticipant);
    }

    @Test
    @DisplayName("Test update Non-Existent Participant")
    void testUpdateNonExistent() {
        Long participantId = 1L;
        Participant updatedParticipant = new Participant();
        updatedParticipant.setParticipantId(participantId);
        updatedParticipant.setPrenom("John");

        when(repository.findById(participantId)).thenReturn(Optional.empty());

        Participant result = participantService.update(participantId, updatedParticipant);

        assertNull(result, "Participant should not be updated");
        verify(repository, times(1)).findById(participantId);
        verify(repository, never()).save(updatedParticipant);
    }

    @Test
    @DisplayName("Test delete Participant")
    void testDelete() {
        Long participantId = 1L;
        Participant existingParticipant = new Participant();
        existingParticipant.setParticipantId(participantId);

        when(repository.findById(participantId)).thenReturn(Optional.of(existingParticipant));

        int result = participantService.delete(participantId);

        assertEquals(1, result, "Participant should be deleted");
        verify(repository, times(1)).findById(participantId);
        verify(repository, times(1)).deleteById(participantId);
    }

    @Test
    @DisplayName("Test delete Non-Existent Participant")
    void testDeleteNonExistent() {
        Long participantId = 1L;

        when(repository.findById(participantId)).thenReturn(Optional.empty());

        int result = participantService.delete(participantId);

        assertEquals(0, result, "Participant should not be deleted");
        verify(repository, times(1)).findById(participantId);
        verify(repository, never()).deleteById(participantId);
    }

    @Test
    @DisplayName("Test setCommentaire method")
    void testGetCommentaire() {
        Participant sampleParticipant = new Participant();

        sampleParticipant.setCommentaire(List.of(new Commentaire()));

        List<Commentaire> result = sampleParticipant.getCommentaire();

        assertEquals(1, result.size(), "Number of commentaires should be 1");
    }

    @Test
    @DisplayName("Test getCommentaire method")
    void testSetCommentaire() {
        Participant sampleParticipant = new Participant();

        Commentaire commentaire = new Commentaire();

        sampleParticipant.setCommentaire(List.of(commentaire));

        List<Commentaire> result = sampleParticipant.getCommentaire();

        assertEquals(commentaire, result.get(0), "Returned Commentaire should match the mocked one");
    }

    @Test
    @DisplayName("Test setSondages method")
    void testGetSondage() {
        Participant sampleParticipant = new Participant();

        sampleParticipant.setSondages(List.of(new Sondage()));

        List<Sondage> result = sampleParticipant.getSondages();

        assertEquals(1, result.size(), "Number of sondages should be 1");
    }

    @Test
    @DisplayName("Test getSondages method")
    void testSetSondage() {
        Participant sampleParticipant = new Participant();

        Sondage sondage = new Sondage();

        sampleParticipant.setSondages(List.of(sondage));

        List<Sondage> result = sampleParticipant.getSondages();

        assertEquals(sondage, result.get(0), "Returned Sondage should match the mocked one");
    }

    @Test
    @DisplayName("Test setDateSondee method")
    void testGetDateSondee() {
        Participant sampleParticipant = new Participant();

        sampleParticipant.setDateSondee(List.of(new DateSondee()));

        List<DateSondee> result = sampleParticipant.getDateSondee();

        assertEquals(1, result.size(), "Number of dateSondee should be 1");
    }

    @Test
    @DisplayName("Test equals after all if method")
    void testEquals() {
        Commentaire commentaire = new Commentaire();
        Sondage sondage = new Sondage();
        DateSondee dateSondee = new DateSondee();

        Participant participant = new Participant();
        participant.setParticipantId(1L);
        participant.setPrenom("John");
        participant.setNom("Smith");
        participant.setCommentaire(List.of(commentaire));
        participant.setSondages(List.of(sondage));
        participant.setDateSondee(List.of(dateSondee));

        Participant participant2 = new Participant();
        participant2.setParticipantId(1L);
        participant2.setPrenom("John");
        participant2.setNom("Smith");
        participant2.setCommentaire(List.of(commentaire));
        participant2.setSondages(List.of(sondage));
        participant2.setDateSondee(List.of(dateSondee));

        assertEquals(participant, participant2, "Participants should be equal");
        assertNotEquals(participant, null, "Participant should not be equal to null");
        assertNotEquals(participant, new Object(), "Participant should not be equal to new Object()");

        Participant participant3 = new Participant();
        participant3.setParticipantId(2L); // Different ID
        participant3.setPrenom("John");
        participant3.setNom("Smith");
        participant3.setCommentaire(List.of(commentaire));
        participant3.setSondages(List.of(sondage));
        participant3.setDateSondee(List.of(dateSondee));

        assertNotEquals(participant, participant3, "Participants with different IDs should not be equal");
    }


    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        Participant participant = new Participant();
        participant.setParticipantId(1L);
        participant.setPrenom("John");
        participant.setNom("Smith");

        int result = participant.hashCode();

        assertEquals(262655257, result, "Hash code should be 262655257");
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        Participant participant = new Participant();
        participant.setParticipantId(1L);
        participant.setPrenom("Smith");
        participant.setNom("John");

        String result = participant.toString();

        assertEquals("Participant{participantId=1, nom='John', prenom='Smith'}", result, "String should be Participant{participantId=1, nom='John', prenom='Smith'}");
    }
}
