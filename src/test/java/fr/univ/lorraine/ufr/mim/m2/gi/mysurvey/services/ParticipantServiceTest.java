package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {
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
}
