package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.controllers;

import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.dtos.ParticipantDto;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.models.Participant;
import fr.univ.lorraine.ufr.mim.m2.gi.mysurvey.services.ParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/participant")
@Api(tags = "API Participant")
public class ParticipantController {

    private final ParticipantService service;
    private final ModelMapper mapper;

    public ParticipantController(ParticipantService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Récupérer un participant")
    public ParticipantDto get(@PathVariable("id") Long id) {
        var model = service.getById(id);
        return mapper.map(model, ParticipantDto.class);
    }

    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Récupérer tous les participants")
    public List<ParticipantDto> get() {
        var models = service.getAll();
        return models.stream().map(model -> mapper.map(model, ParticipantDto.class)).collect(Collectors.toList());
    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation(value = "Créer un nouveau participant")
    public ParticipantDto create(@RequestBody ParticipantDto participantDto) {
        var model = mapper.map(participantDto, Participant.class);
        var result = service.create(model);
        return mapper.map(result, ParticipantDto.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Mettre à jour un participant")
    public ParticipantDto update(@PathVariable("id") Long id, @RequestBody ParticipantDto participantDto) {
        var model = mapper.map(participantDto, Participant.class);
        var result = service.update(id, model);
        return mapper.map(result, ParticipantDto.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Supprimer un participant")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}