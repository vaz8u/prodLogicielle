package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.controllers;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.DateSondeeDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.DateSondee;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.DateSondageService;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.DateSondeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateSondageControllerTest {
    @Mock
    private DateSondageService dateSondageService;

    @Mock
    private DateSondeeService dateSondeeService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DateSondageController dateSondageController;

    @Test
    @DisplayName("Test delete DateSondage")
    void testDelete() {
        Long dateSondageId = 1L;

        dateSondageController.delete(dateSondageId);

        verify(dateSondageService, times(1)).delete(dateSondageId);
    }

    @Test
    @DisplayName("Test createParticipation")
    void testCreateParticipation() {
        Long dateSondageId = 1L;
        Long participantId = 2L;

        DateSondeeDto dateSondeeDto = new DateSondeeDto();
        dateSondeeDto.setParticipant(participantId);

        DateSondee dateSondeeToCreate = new DateSondee();
        DateSondee expectedDateSondee = new DateSondee();

        when(modelMapper.map(dateSondeeDto, DateSondee.class)).thenReturn(dateSondeeToCreate);
        when(dateSondeeService.create(dateSondageId, participantId, dateSondeeToCreate)).thenReturn(expectedDateSondee);
        when(modelMapper.map(expectedDateSondee, DateSondeeDto.class)).thenReturn(dateSondeeDto);

        DateSondeeDto result = dateSondageController.createParticipation(dateSondageId, dateSondeeDto);

        assertDto(dateSondeeDto, result);

        verify(modelMapper, times(1)).map(dateSondeeDto, DateSondee.class);
        verify(dateSondeeService, times(1)).create(dateSondageId, participantId, dateSondeeToCreate);
        verify(modelMapper, times(1)).map(expectedDateSondee, DateSondeeDto.class);
    }
}
