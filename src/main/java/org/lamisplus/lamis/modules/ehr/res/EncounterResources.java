package org.lamisplus.lamis.modules.ehr.res;

import org.lamisplus.lamis.modules.ehr.domain.repositories.EncounterRepository;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.lamisplus.modules.ehr.domain.entities.Encounter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient-encounter")
public class EncounterResources {
    private static String ENTITY_NAME = "patient-encounter";
    private final EncounterRepository encounterRepository;

    public EncounterResources(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    private static Object exist(Encounter encounter) {
        throw new BadRequestAlertException("Patient Encounter Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Encounter notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }

    @PostMapping
    public ResponseEntity<Encounter> save(@RequestBody Encounter encounter) throws URISyntaxException {
        Optional<Encounter> patient1 = this.encounterRepository.findById(encounter.getId());
        patient1.map(EncounterResources::exist);
        Encounter result = encounterRepository.save(encounter);
        return ResponseEntity.created(new URI("/api/patient-encounter/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId()))).body(result);
    }

    @PutMapping
    public ResponseEntity<Encounter> update(@RequestBody Encounter encounter) throws URISyntaxException {
        Optional<Encounter> patient1 = this.encounterRepository.findById(encounter.getId());
        patient1.orElseGet(EncounterResources::notExit);
        Encounter result = encounterRepository.save(encounter);
        return ResponseEntity.created(new URI("/api/patient-encounter/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId()))).body(result);
    }

    @GetMapping
    public ResponseEntity getAllEncounter() {
        return ResponseEntity.ok(encounterRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getSingle(@PathVariable Long id) {
        Optional<Encounter> patientEncounter = this.encounterRepository.findById(id);
        patientEncounter.orElseGet(EncounterResources::notExit);
        Encounter patientEncounter1 = patientEncounter.get();
        return ResponseEntity.ok(patientEncounter1);
    }

}
