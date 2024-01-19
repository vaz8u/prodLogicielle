package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Choix;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondee;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.DateSondeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateSondeeServiceTest {
    @Mock
    private DateSondeeRepository repository;

    @Mock
    private DateSondageService dateSondageService;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private DateSondeeService dateSondeeService;

    @Test
    @DisplayName("Test create DateSondee")
    void testCreate() {
        Long dateSondageId = 1L;
        Long participantId = 2L;

        DateSondee dateSondeeToCreate = new DateSondee();
        DateSondage dateSondage = new DateSondage();
        dateSondage.getSondage().setCloture(false);

        when(dateSondageService.getById(dateSondageId)).thenReturn(dateSondage);
        when(participantService.getById(participantId)).thenReturn(null);
        when(repository.save(any(DateSondee.class))).thenReturn(dateSondeeToCreate);

        DateSondee result = dateSondeeService.create(dateSondageId, participantId, dateSondeeToCreate);

        assertDto(dateSondeeToCreate, result);

        verify(dateSondageService, times(1)).getById(dateSondageId);
        verify(participantService, times(1)).getById(participantId);
        verify(repository, times(1)).save(any(DateSondee.class));
    }

    @Test
    @DisplayName("Test create DateSondee with parameters & setters")
    void testCreateParams() {
        Long dateSondageId = 1L;
        Long participantId = 2L;
        Long dateSondeeId = 3L;
        Participant participant = new Participant();
        Choix choix = Choix.DISPONIBLE;

        DateSondage dateSondage = new DateSondage();
        dateSondage.getSondage().setCloture(false);

        DateSondee dateSondeeToCreate = new DateSondee(dateSondeeId, dateSondage, participant, choix);

        when(dateSondageService.getById(dateSondageId)).thenReturn(dateSondage);
        when(participantService.getById(participantId)).thenReturn(null);
        when(repository.save(any(DateSondee.class))).thenReturn(dateSondeeToCreate);

        DateSondee result = dateSondeeService.create(dateSondageId, participantId, dateSondeeToCreate);
        result.setChoix(choix.name());
        result.setParticipant(participant);
        result.setDateSondeeId(dateSondeeId);
        result.setDateSondage(dateSondage);

        assertDto(dateSondeeToCreate, result);

        verify(dateSondageService, times(1)).getById(dateSondageId);
        verify(participantService, times(1)).getById(participantId);
        verify(repository, times(1)).save(any(DateSondee.class));
    }

    @Test
    @DisplayName("Test create DateSondee - Cloture DateSondage")
    void testCreateClotureDateSondage() {
        Long dateSondageId = 1L;
        Long participantId = 2L;

        DateSondee dateSondeeToCreate = new DateSondee();
        DateSondage dateSondage = new DateSondage();
        dateSondage.getSondage().setCloture(true);

        when(dateSondageService.getById(dateSondageId)).thenReturn(dateSondage);

        DateSondee result = dateSondeeService.create(dateSondageId, participantId, dateSondeeToCreate);

        assertNull(result, "DateSondee should not be created when DateSondage is closed");

        verify(dateSondageService, times(1)).getById(dateSondageId);
        verify(participantService, never()).getById(participantId);
        verify(repository, never()).save(any(DateSondee.class));
    }

    @Test
    @DisplayName("Test bestDate")
    void testBestDate() {
        Long dateSondeeId = 1L;
        List<Date> expectedBestDates = Collections.singletonList(new Date());

        when(repository.bestDate(dateSondeeId)).thenReturn(expectedBestDates);

        List<Date> result = dateSondeeService.bestDate(dateSondeeId);

        assertDto(expectedBestDates, result);

        verify(repository, times(1)).bestDate(dateSondeeId);
    }

    @Test
    @DisplayName("Test maybeBestDate")
    void testMaybeBestDate() {
        Long dateSondeeId = 1L;
        List<Date> expectedMaybeBestDates = Collections.singletonList(new Date());

        when(repository.maybeBestDate(dateSondeeId)).thenReturn(expectedMaybeBestDates);

        List<Date> result = dateSondeeService.maybeBestDate(dateSondeeId);

        assertDto(expectedMaybeBestDates, result);

        verify(repository, times(1)).maybeBestDate(dateSondeeId);
    }

    @Test
    @DisplayName("Test getDateSondeeId")
    void testGetDateSondeeId() {
        Long dateSondeeId = 1L;
        DateSondee expectedDateSondee = new DateSondee();
        expectedDateSondee.setDateSondeeId(dateSondeeId);

        Long result = expectedDateSondee.getDateSondeeId();

        assertDto(dateSondeeId, result);
    }

    @Test
    @DisplayName("Test getDateSondage")
    void testGetDateSondage() {
        DateSondage expectedDateSondage = new DateSondage();
        DateSondee dateSondee = new DateSondee();
        dateSondee.setDateSondage(expectedDateSondage);

        DateSondage result = dateSondee.getDateSondage();

        assertDto(expectedDateSondage, result);
    }

    @Test
    @DisplayName("Test getParticipant")
    void testGetParticipant() {
        Participant expectedParticipant = new Participant();
        DateSondee dateSondee = new DateSondee();
        dateSondee.setParticipant(expectedParticipant);

        Participant result = dateSondee.getParticipant();

        assertDto(expectedParticipant, result);
    }

    @Test
    @DisplayName("Test getChoix")
    void testGetChoix() {
        Choix expectedChoix = Choix.DISPONIBLE;
        DateSondee dateSondee = new DateSondee();
        dateSondee.setChoix(expectedChoix.name());

        String result = dateSondee.getChoix();

        assertDto(expectedChoix.name(), result);
    }

    @Test
    @DisplayName("Test Choix")
    void testChoix() {
        for (Choix choix : Choix.values())
            assertNotNull(choix);
    }
}
