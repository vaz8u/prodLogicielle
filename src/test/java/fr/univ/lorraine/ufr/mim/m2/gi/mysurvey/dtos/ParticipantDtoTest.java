package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class ParticipantDtoTest {
    @Test
    @DisplayName("Test get/set-ParticipantId method")
    void testGetSetParticipantId() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setParticipantId(1L);

        Long result = participantDto.getParticipantId();

        assertEquals(1L, result);
    }

    @Test
    @DisplayName("Test get/set-Nom method")
    void testGetSetNom() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setNom("Nom");

        String result = participantDto.getNom();

        assertEquals("Nom", result);
    }

    @Test
    @DisplayName("Test get/set-Prenom method")
    void testGetSetPrenom() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setPrenom("Prenom");

        String result = participantDto.getPrenom();

        assertEquals("Prenom", result);
    }

    @Test
    @DisplayName("Test equals method")
    void testEquals() {
        ParticipantDto participantDto = new ParticipantDto();
        ParticipantDto participantDto1 = new ParticipantDto();
        assertEquals(participantDto, participantDto1);

        participantDto.setParticipantId(1L);
        participantDto.setNom("Nom");
        participantDto.setPrenom("Prenom");

        ParticipantDto participantDto2 = new ParticipantDto();
        participantDto2.setParticipantId(1L);
        participantDto2.setNom("Nom");
        participantDto2.setPrenom("Prenom");

        assertEquals(participantDto, participantDto2);

        participantDto2.setParticipantId(2L);

        assertNotEquals(participantDto, participantDto2);

        participantDto2.setParticipantId(1L);
        participantDto2.setNom("Nom2");

        assertNotEquals(participantDto, participantDto2);

        participantDto2.setNom("Nom");
        participantDto2.setPrenom("Prenom2");

        assertNotEquals(participantDto, participantDto2);
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        ParticipantDto participantDto = new ParticipantDto();
        int result = participantDto.hashCode();

        assertEquals(961, result, result);
    }

    @Test
    @DisplayName("Test toString method")
    void testToString() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setParticipantId(1L);
        participantDto.setNom("Nom");
        participantDto.setPrenom("Prenom");

        String result = participantDto.toString();

        assertEquals("ParticipantDto{participantId=1, nom='Nom', prenom='Prenom'}", result);
    }
}
