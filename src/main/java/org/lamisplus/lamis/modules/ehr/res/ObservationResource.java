package org.lamisplus.lamis.modules.ehr.res;

import lombok.extern.slf4j.Slf4j;
import org.lamisplus.lamis.modules.ehr.domain.dto.ObservationRequest;
import org.lamisplus.lamis.modules.ehr.domain.repositories.ObservationRepository;
import org.lamisplus.lamis.modules.ehr.service.ObservationService;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api/observation")
@Slf4j
public class ObservationResource {
    private static final String ENTITY_NAME = "observation";
    private final ObservationService observationService;
    private final ObservationRepository observationRepository;

    public ObservationResource(ObservationService observationService, ObservationRepository observationRepository) {
        this.observationService = observationService;
        this.observationRepository = observationRepository;
    }


    @PostMapping
    public ResponseEntity<Observation> save(@RequestBody ObservationRequest observation) throws URISyntaxException {
        Observation result = this.observationService.save(observation);
        return ResponseEntity.created(new URI("/api/patient-observations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }


    @PutMapping
    public ResponseEntity<Observation> update(@RequestBody ObservationRequest observation) {
        Observation result = this.observationService.save(observation);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, observation.getId().toString()))
                .body(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity getObservation(@PathVariable Long id) {
        Observation  observation = this.observationRepository.getOne(id);
        return ResponseEntity.ok(observation);

    }

    @GetMapping("/patient/{id}/{form-code}/{value}")
    public ResponseEntity<List<Observation>> getPatientObservations(@PathVariable("id") Long id, @PathVariable("form-code") Long formCode,@PathVariable String value) {
        List<Observation> observations = this.observationService.getAllObservationByPatientIdAndFormCode(id,formCode,value);
        return ResponseEntity.ok(observations);
    }



    @GetMapping("/patient/{id}/encounter/{encounterId}")
    public ResponseEntity<List<Observation>> getPatientObservationsForEncounter(@PathVariable Long id, @PathVariable Long encounterId) {
        List<Observation> observationList = this.observationService.getPatientObservationsForEncounter(id, encounterId);
        return ResponseEntity.ok(observationList);
    }



}
