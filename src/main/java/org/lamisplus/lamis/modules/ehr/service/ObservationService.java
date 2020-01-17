package org.lamisplus.lamis.modules.ehr.service;
import lombok.extern.slf4j.Slf4j;;
import org.lamisplus.lamis.modules.ehr.domain.dto.ObservationRequest;
import org.lamisplus.lamis.modules.ehr.domain.repositories.EncounterRepository;
import org.lamisplus.lamis.modules.ehr.domain.repositories.ObservationRepository;
import org.lamisplus.lamis.modules.ehr.domain.repositories.PatientRepository;
import org.lamisplus.lamis.modules.ehr.domain.repositories.ServiceRepository;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@org.springframework.stereotype.Service
@Slf4j
public class ObservationService {
    private static final String ENTITY_NAME = "observation";
    private final PatientRepository patientRepository;
    private final ObservationRepository observationRepository;
    private final ServiceRepository serviceRepository;
    private final EncounterRepository encounterRepository;

    public ObservationService(PatientRepository patientRepository, ObservationRepository observationRepository, ServiceRepository serviceRepository, EncounterRepository encounterRepository) {
        this.patientRepository = patientRepository;
        this.observationRepository = observationRepository;
        this.serviceRepository = serviceRepository;
        this.encounterRepository = encounterRepository;
    }


    private static Object exist(Observation observation) {
        throw new BadRequestAlertException("Observation  Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Observation notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }
//check the enounter table if patientid, encounterdate, serviceid exist the return the encounterId
    //set to observation  but if it does not exist save the encounter and return the id.

    public Observation save(ObservationRequest observation) {
        LOG.debug("REST: {}", observation);
        Patient patient1 = this.patientRepository.getOne(observation.getPatientId());
        Service service = this.serviceRepository.getOne(observation.getServiceId());
        Optional<Encounter> encounterOptional = this.encounterRepository.findByPatientAndDateEncounterAndService(patient1, observation.getDateEncounter(), service);
        if (encounterOptional.isPresent()){
            Encounter encounter = encounterOptional.get();
            Observation observation1 = new Observation();
            observation1.setFormId(observation.getFormCode());
            observation1.setData(observation.getFormData());
            observation1.setPatient(patient1);
            observation1.setEncounter(encounter);
            return observationRepository.save(observation1);
        } else {
            Encounter encounter = new Encounter();
            encounter.setDate(observation.getDateEncounter());
            encounter.setPatient(patient1);
            encounter.setService(service);
            Encounter encounter1 = this.encounterRepository.save(encounter);
            Observation observation1 = new Observation();
            observation1.setFormId(observation.getFormCode());
            observation1.setData(observation.getFormData());
            observation1.setPatient(patient1);
            observation1.setEncounter(encounter1);
            return observationRepository.save(observation1);
        }
    }


    public Observation update(ObservationRequest observation) {
        Optional<Observation> patientVisit = this.observationRepository.findById(observation.getId());
        patientVisit.orElseGet(ObservationService::notExit);
        Observation observation1 = new Observation();
        observation1.setFormId(observation.getFormCode());
        observation1.setData(observation.getFormData());
        Patient patient = patientRepository.getOne(observation.getPatientId());
        observation1.setPatient(patient);
        return observationRepository.save(observation1);

    }


    public List<Observation> getAllObservationByPatientIdAndFormCode(Long id, Long formCode, String value) {
        Patient patient = patientRepository.getOne(id);
        return observationRepository.findByPatientAndFormIdTitle(patient,formCode,value);
    }


    public List<Observation> getPatientObservationsForEncounter(Long id, Long encounterId) {
        Patient patient = patientRepository.getOne(id);
        Encounter encounter = encounterRepository.getOne(encounterId);
        return observationRepository.findByPatientAndEncounter(patient, encounter);
    }


}
