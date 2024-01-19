package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondee;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Sondage;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.repositories.DateSondageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateSondageServiceTest {
    @Mock
    private DateSondageRepository repository;

    @Mock
    private SondageService sondageService;

    @InjectMocks
    private DateSondageService dateSondageService;

    @Test
    @DisplayName("Test getById method")
    void testGetById() {
        Long dateSondageId = 1L;
        DateSondage sampleDateSondage = new DateSondage();
        when(repository.findById(dateSondageId)).thenReturn(Optional.of(sampleDateSondage));

        DateSondage result = dateSondageService.getById(dateSondageId);

        assertDto(sampleDateSondage, result);
    }

    @Test
    @DisplayName("Test getBySondageId method")
    void testGetBySondageId() {
        Long sondageId = 1L;
        DateSondage sampleDateSondage = new DateSondage();
        when(repository.getAllBySondage(sondageId)).thenReturn(Collections.singletonList(sampleDateSondage));

        List<DateSondage> result = dateSondageService.getBySondageId(sondageId);

        assertEquals(1, result.size(), "Number of dateSondages should be 1");
        assertDto(sampleDateSondage, result.get(0));
    }

    @Test
    @DisplayName("Test create DateSondage")
    void testCreate() {
        Long sondageId = 1L;
        DateSondage dateSondageToCreate = new DateSondage();
        DateSondage expectedDateSondage = new DateSondage();

        when(sondageService.getById(sondageId)).thenReturn(new Sondage());
        when(repository.save(any(DateSondage.class))).thenReturn(expectedDateSondage);

        DateSondage result = dateSondageService.create(sondageId, dateSondageToCreate);

        assertDto(expectedDateSondage, result);

        verify(sondageService, times(1)).getById(sondageId);
        verify(repository, times(1)).save(any(DateSondage.class));
    }

    @Test
    @DisplayName("Test create DateSondage with parameters")
    void testCreateWithParameters() {
        Long sondageId = 1L;
        Long dateSondageId = 2L;
        Date date = new Date();
        Sondage sondage = new Sondage();
        List<DateSondee> dateSondee = Collections.singletonList(new DateSondee());
        DateSondage dateSondageToCreate = new DateSondage(dateSondageId, date, sondage, dateSondee);

        DateSondage expectedDateSondage = new DateSondage();
        expectedDateSondage.setDateSondageId(dateSondageId);
        expectedDateSondage.setDate(date);
        expectedDateSondage.setSondage(sondage);
        expectedDateSondage.setDateSondee(dateSondee);

        when(sondageService.getById(sondageId)).thenReturn(new Sondage());
        when(repository.save(any(DateSondage.class))).thenReturn(expectedDateSondage);

        DateSondage result = dateSondageService.create(sondageId, dateSondageToCreate);

        assertDto(expectedDateSondage, result);
    }

    @Test
    @DisplayName("Test delete DateSondage")
    void testDelete() {
        Long dateSondageId = 1L;
        DateSondage existingDateSondage = new DateSondage();
        existingDateSondage.setDateSondageId(dateSondageId);

        when(repository.findById(dateSondageId)).thenReturn(Optional.of(existingDateSondage));

        int result = dateSondageService.delete(dateSondageId);

        assertEquals(1, result, "DateSondage should be deleted");

        verify(repository, times(1)).findById(dateSondageId);
        verify(repository, times(1)).deleteById(dateSondageId);
    }

    @Test
    @DisplayName("Test getDate")
    void testGetDate() {
        Long dateSondageId = 1L;
        DateSondage existingDateSondage = new DateSondage();
        existingDateSondage.setDateSondageId(dateSondageId);
        Date date = new Date();
        existingDateSondage.setDate(date);

        Date result = existingDateSondage.getDate();

        assertEquals(date, result, "DateSondage should be deleted");
    }

    @Test
    @DisplayName("Test getDateSondee")
    void testGetDateSondee() {
        Long dateSondageId = 1L;
        DateSondage existingDateSondage = new DateSondage();
        existingDateSondage.setDateSondageId(dateSondageId);
        List<DateSondee> dateSondee = Collections.singletonList(new DateSondee());
        existingDateSondage.setDateSondee(dateSondee);

        List<DateSondee> result = existingDateSondage.getDateSondee();

        assertEquals(dateSondee, result, "DateSondage should be deleted");
    }

    @Test
    @DisplayName("Test delete Non-Existent DateSondage")
    void testDeleteNonExistent() {
        Long dateSondageId = 1L;

        when(repository.findById(dateSondageId)).thenReturn(Optional.empty());

        int result = dateSondageService.delete(dateSondageId);

        assertEquals(0, result, "DateSondage should not be deleted");

        verify(repository, times(1)).findById(dateSondageId);
        verify(repository, never()).deleteById(dateSondageId);
    }
}
