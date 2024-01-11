package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.controllers;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.ParticipantDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDto;
import static fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.util.TestUtil.assertDtoListSize;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantControllerTest {
    @Mock
    private ParticipantService participantService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ParticipantController participantController;

    @Test
    @DisplayName("Test get Participant by Id")
    void testGetById() {
        Long participantId = 1L;
        Participant expectedModel = new Participant();
        ParticipantDto expectedDto = new ParticipantDto();

        when(participantService.getById(participantId)).thenReturn(expectedModel);
        when(modelMapper.map(expectedModel, ParticipantDto.class)).thenReturn(expectedDto);

        ParticipantDto result = participantController.get(participantId);

        assertDto(expectedDto,result);

        verify(participantService, times(1)).getById(participantId);
        verify(modelMapper, times(1)).map(expectedModel, ParticipantDto.class);
    }

    @Test
    @DisplayName("Test get All Participants")
    void testGetAll() {
        List<Participant> expectedModels = Arrays.asList(new Participant(), new Participant(), new Participant());

        when(participantService.getAll()).thenReturn(expectedModels);

        List<ParticipantDto> result = participantController.get();

        assertDtoListSize(expectedModels,result);

        verify(participantService, times(1)).getAll();
        verify(modelMapper, times(expectedModels.size())).map(any(Participant.class), eq(ParticipantDto.class));
    }

    @Test
    @DisplayName("Test create Participant")
    void testCreate() {
        ParticipantDto participantDto = new ParticipantDto();
        Participant expectedModel = new Participant();
        ParticipantDto expectedDto = new ParticipantDto();

        when(modelMapper.map(participantDto, Participant.class)).thenReturn(expectedModel);
        when(participantService.create(expectedModel)).thenReturn(expectedModel);
        when(modelMapper.map(expectedModel, ParticipantDto.class)).thenReturn(expectedDto);

        ParticipantDto result = participantController.create(participantDto);

        assertDto(expectedDto,result);

        verify(modelMapper, times(1)).map(participantDto, Participant.class);
        verify(participantService, times(1)).create(expectedModel);
        verify(modelMapper, times(1)).map(expectedModel, ParticipantDto.class);
    }

    @Test
    @DisplayName("Test update Participant")
    void testUpdate() {
        Long participantId = 1L;
        ParticipantDto participantDto = new ParticipantDto();
        Participant expectedModel = new Participant();
        ParticipantDto expectedDto = new ParticipantDto();

        when(modelMapper.map(participantDto, Participant.class)).thenReturn(expectedModel);
        when(participantService.update(participantId, expectedModel)).thenReturn(expectedModel);
        when(modelMapper.map(expectedModel, ParticipantDto.class)).thenReturn(expectedDto);

        ParticipantDto result = participantController.update(participantId, participantDto);

        assertDto(expectedDto,result);

        verify(modelMapper, times(1)).map(participantDto, Participant.class);
        verify(participantService, times(1)).update(participantId, expectedModel);
        verify(modelMapper, times(1)).map(expectedModel, ParticipantDto.class);
    }

    @Test
    @DisplayName("Test delete Participant")
    void testDelete() {
        Long participantId = 1L;

        participantController.delete(participantId);

        verify(participantService, times(1)).delete(participantId);
    }
}
