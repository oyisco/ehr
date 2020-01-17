package org.lamisplus.lamis.modules.ehr.res;
import org.lamisplus.lamis.modules.ehr.domain.dto.PatientVisitRequest;
import org.lamisplus.lamis.modules.ehr.domain.repositories.PatientRepository;
import org.lamisplus.lamis.modules.ehr.domain.repositories.VisitRepository;
import org.lamisplus.modules.base.domain.entities.Codifier;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/visit")
public class VisitResource {
    private static final String ENTITY_NAME = "patient-visit";
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final CodifierRepository codifierRepository;

    public VisitResource(VisitRepository visitRepository, PatientRepository patientRepository, CodifierRepository codifierRepository) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.codifierRepository = codifierRepository;
    }


    private static Object exist(Visit o) {
        throw new BadRequestAlertException("Patient Visit Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Visit notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }


    @PostMapping
    public ResponseEntity save(@RequestBody PatientVisitRequest patientVisitRequest) throws URISyntaxException {
        visitRepository.findById(patientVisitRequest.getId()).map(VisitResource::exist);
        Visit visit = new Visit();
        Patient patient = patientRepository.getOne(patientVisitRequest.getPatientId());
        visit.setPatient(patient);
        visit.setDateVisitStart(patientVisitRequest.getDateVisitStart());
        visit.setTimeVisitStart(patientVisitRequest.getTimeVisitStart());
        Codifier codifier = codifierRepository.getOne(patientVisitRequest.getVisitTypeId());
        visit.setVisitTypeId(codifier);
        Visit result = visitRepository.save(visit);
        return ResponseEntity.created(new URI("/api/patient-visit/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId()))).body(result);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody PatientVisitRequest patientVisitRequest) throws URISyntaxException {
        Optional<Visit> country1 = this.visitRepository.findById(patientVisitRequest.getId());
        country1.orElseGet(VisitResource::notExit);
        Visit patientVisit = new Visit();
        Patient patient = patientRepository.getOne(patientVisitRequest.getPatientId());
        patientVisit.setPatient(patient);
        patientVisit.setDateVisitStart(patientVisitRequest.getDateVisitStart());
        patientVisit.setTimeVisitStart(patientVisitRequest.getTimeVisitStart());
        Codifier codifier = codifierRepository.getOne(patientVisitRequest.getVisitTypeId());
        patientVisit.setVisitTypeId(codifier);
        Visit result = visitRepository.save(patientVisit);
        return ResponseEntity.created(new URI("/api/patient-visit/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId())))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<PatientVisitRequest>> getAllPatientVisit() {
        List<PatientVisitRequest> patientVisitRequestList = new ArrayList<>();
        List<Visit> patientVisit = visitRepository.findAll();
        patientVisit.forEach(patientVisit1 -> {
            PatientVisitRequest patientVisitRequest = new PatientVisitRequest();
            patientVisitRequest.setId(patientVisit1.getId());
            patientVisitRequest.setPatientId(patientVisit1.getPatient().getId());
            patientVisitRequest.setDateVisitStart(patientVisit1.getDateVisitStart());
            patientVisitRequest.setDateVisitStart(patientVisit1.getDateVisitStart());
            patientVisitRequest.setVisitTypeId(patientVisit1.getVisitTypeId().getId());
            patientVisitRequestList.add(patientVisitRequest);
        });
        return ResponseEntity.ok(patientVisitRequestList);

    }


    @GetMapping("/{id}")
    public ResponseEntity<List<PatientVisitRequest>> getSingle(@PathVariable Long id) {
        Optional<Visit> patientVisit = visitRepository.findById(id);
        patientVisit.map(patientVisit1 -> {
            PatientVisitRequest patientVisitRequest = new PatientVisitRequest();
            patientVisitRequest.setPatientId(patientVisit1.getPatient().getId());
            patientVisitRequest.setDateVisitStart(patientVisit1.getDateVisitStart());
            patientVisitRequest.setDateVisitStart(patientVisit1.getDateVisitStart());
            patientVisitRequest.setVisitTypeId(patientVisit1.getVisitTypeId().getId());
            return patientVisitRequest;
        });
        throw new BadRequestAlertException("No record found", ENTITY_NAME, "id is null");
    }
}
