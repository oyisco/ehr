package org.lamisplus.lamis.modules.ehr.res;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.lamis.modules.ehr.domain.dto.PatientDemography;
import org.lamisplus.lamis.modules.ehr.domain.dto.PatientRequest;
import org.lamisplus.lamis.modules.ehr.domain.dto.PatientResponseDTO;
import org.lamisplus.lamis.modules.ehr.domain.repositories.PatientRepository;
import org.lamisplus.lamis.modules.ehr.domain.repositories.ServiceEnrollmentRepository;
import org.lamisplus.lamis.modules.ehr.service.PatientServices;
import org.lamisplus.modules.base.domain.entities.Codifier;
import org.lamisplus.modules.base.domain.entities.Person;
import org.lamisplus.modules.base.domain.repositories.CodifierRepository;
import org.lamisplus.modules.base.domain.repositories.PersonRepository;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.lamisplus.modules.ehr.domain.entities.Patient;
import org.lamisplus.modules.ehr.domain.entities.ServiceEnrollment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/person")
@Slf4j
public class PersonResources {

    private static final String ENTITY_NAME = "person";
    private final PatientRepository patientRepository;
    private final PatientServices patientService;
    private final PersonRepository personRepository;
    private final ServiceEnrollmentRepository serviceEnrollmentRepository1;
    private final CodifierRepository codifierRepository;

    public PersonResources(PatientRepository patientRepository, PatientServices patientService, PersonRepository personRepository, ServiceEnrollmentRepository serviceEnrollmentRepository1, CodifierRepository codifierRepository) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        this.personRepository = personRepository;
        this.serviceEnrollmentRepository1 = serviceEnrollmentRepository1;
        this.codifierRepository = codifierRepository;
    }


    @PostMapping
    public ResponseEntity<Person> save(@RequestBody PatientRequest patientRequest) throws URISyntaxException {
        Person person = this.patientService.save(patientRequest);
        return ResponseEntity.created(new URI("/api/patient/" + person.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(person.getId()))).body(person);
    }

    @PutMapping
    public ResponseEntity<Person> update(@RequestBody PatientRequest personDTO) throws URISyntaxException {
        Person result = this.patientService.update(personDTO);
        return ResponseEntity.created(new URI("/api/patient/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId())))
                .body(result);
    }

    @GetMapping
    public ResponseEntity getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatient());
    }


    @GetMapping("/{id}")
    public ResponseEntity getSingle(@PathVariable Long id) {
        PatientResponseDTO patientResponseDTO = patientService.getSingle(id);
        return ResponseEntity.ok(patientResponseDTO);
    }

    @GetMapping(value = "/patient/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchPatient(@RequestParam(name = "search") String search) {
        List<ServiceEnrollment> patientServiceEnrollment1 = serviceEnrollmentRepository1.findByIdentifier(search);
      if(!patientServiceEnrollment1.isEmpty()){
          List<PatientDemography> patientDemography2 = new ArrayList<>();
          patientServiceEnrollment1.forEach(patientServiceEnrollment -> {
              PatientDemography patientDemography = new PatientDemography();
              Patient patient = patientRepository.getOne(patientServiceEnrollment.getPatient().getId());
              patientDemography.setPatientId(patient.getId());
              patientDemography.setDateRegistration(patient.getDateRegistration());
              patientDemography.setHospitalNumber(patientServiceEnrollment.getIdentifier());
              patientDemography.setPersonId(patient.getPerson().getId());
              patientDemography.setFirstName(patient.getPerson().getFirstName());
              patientDemography.setLastName(patient.getPerson().getLastName());
              patientDemography.setOtherNames(patient.getPerson().getOtherNames());
              Codifier maritalStatus = codifierRepository.getOne(patient.getPerson().getMaritalStatus().getId());
              patientDemography.setTitle(maritalStatus.getDisplay());
              patientDemography.setDob(patient.getPerson().getDob());
              patientDemography.setDobEstimated(patient.getPerson().getDobEstimated());
              Codifier gender = codifierRepository.getOne(patient.getPerson().getGender().getId());
              patientDemography.setGender(gender.getDisplay());
              Codifier education = codifierRepository.getOne(patient.getPerson().getEducation().getId());
              patientDemography.setEducation(education.getDisplay());
              Codifier occupation = codifierRepository.getOne(patient.getPerson().getOccupation().getId());
              patientDemography.setOccupation(occupation.getDisplay());
              patientDemography2.add(patientDemography);
          });
          return ResponseEntity.ok(patientDemography2);
      }
        List<PatientDemography> patientDemography1 = new ArrayList<>();
        List<Person> personList = this.personRepository.findByFirstNameLastName(search);
        personList.forEach(person -> {
            PatientDemography patientDemography = new PatientDemography();
            Patient patient = patientRepository.findByPerson(person);
            patientDemography.setPatientId(patient.getId());
            ServiceEnrollment patientServiceEnrollment = serviceEnrollmentRepository1.findByPatient(patient);
            patientDemography.setHospitalNumber(patientServiceEnrollment.getIdentifier());
            patientDemography.setPersonId(person.getId());
            patientDemography.setFirstName(person.getFirstName());
            patientDemography.setLastName(person.getLastName());
            patientDemography.setOtherNames(person.getOtherNames());
            patientDemography.setDateRegistration(patientServiceEnrollment.getDateRegistration());
            Codifier maritalStatus = codifierRepository.getOne(patient.getPerson().getMaritalStatus().getId());
            patientDemography.setTitle(maritalStatus.getDisplay());
            patientDemography.setDob(person.getDob());
            patientDemography.setDobEstimated(person.getDobEstimated());
            Codifier gender = codifierRepository.getOne(patient.getPerson().getGender().getId());
            patientDemography.setGender(gender.getDisplay());
            Codifier education = codifierRepository.getOne(patient.getPerson().getEducation().getId());
            patientDemography.setEducation(education.getDisplay());
            Codifier occupation = codifierRepository.getOne(patient.getPerson().getOccupation().getId());
            patientDemography.setOccupation(occupation.getDisplay());
            patientDemography1.add(patientDemography);
        });
        return ResponseEntity.ok(patientDemography1);


//
//        @PostMapping("/_search/patients")
//        public ResponseEntity<List<Patient>> getAllPatients1(@RequestParam String query, @RequestBody List<AggregateVM> aggregates,
//                Pageable pageable) {
//            LOG.debug("REST request to search Patients: {}; page: {}", query, pageable);
//            TermsAggregationBuilder genderAggregation = AggregationBuilders.terms("gender_tags")
//                    .field("gender")
//                    .order(BucketOrder.count(true));
//            TermsAggregationBuilder statusAggregation = AggregationBuilders.terms("status_tags")
//                    .field("currentStatus")
//                    .order(BucketOrder.count(true));
//            TermsAggregationBuilder entryAggregation = AggregationBuilders.terms("entry_tags")
//                    .field("entryPoint")
//                    .order(BucketOrder.count(true));
//            TermsAggregationBuilder biometricAggregation = AggregationBuilders.terms("biometric_tags")
//                    .field("biometric")
//                    .order(BucketOrder.count(true));
//            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
//                    .withIndices("patient")
//                    .addAggregation(genderAggregation)
//                    .addAggregation(statusAggregation)
//                    .addAggregation(biometricAggregation)
//                    .addAggregation(entryAggregation)
//                    .withPageable(pageable);
//
//            BoolQueryBuilder filter = null;// = FilterUtil.getFacilityFilterForCurrentUser();
//            for (AggregateVM aggregate : aggregates) {
//                if (StringUtils.isNotBlank(aggregate.getField())) {
//                    filter = filter
//                            .filter(QueryBuilders.termQuery(aggregate.getField(), aggregate.getKey()));
//                }
//            }
//
//            if (StringUtils.isBlank(query)) {
//                queryBuilder = queryBuilder.withQuery(
//                        QueryBuilders.boolQuery()
//                                .filter(filter)
//                                .must(QueryBuilders.matchAllQuery())
//                );
//            } else {
//                QueryBuilder booleanQuery = QueryBuilders.boolQuery();
//                BoolQueryBuilder builder = QueryBuilders.boolQuery()
//                        .should(
//                                QueryBuilders.matchQuery("surname", query)
//                                        .fuzziness(Fuzziness.ONE)
//                                        .prefixLength(1)
//                        )
//                        .should(
//                                QueryBuilders.matchQuery("otherNames", query)
//                                        .fuzziness(Fuzziness.ONE)
//                                        .prefixLength(1)
//                        )
//                        .should(
//                                QueryBuilders.termQuery("hospitalNum", query)
//                                        .boost(50)
//                        )
//                        .should(
//                                QueryBuilders.termQuery("uniqueId", query)
//                                        .boost(50)
//                        )
//                        .should(
//                                QueryBuilders.termQuery("phone", query)
//                                        .boost(50)
//                        );
//
//                if (aggregates.size() > 0) {
//                    builder = builder.filter(filter);
//                }
//                queryBuilder = queryBuilder.withQuery(builder);
//            }
//            SearchQuery searchQuery = queryBuilder.build()
//                    .setPageable(pageable)
//                    .addSort(pageable.getSort());
//
//            AggregatedPage<Patient> page = searchTemplate.queryForPage(searchQuery, Patient.class);
//            List<AggregateVM> responseAggregates = new ArrayList<>();
//
//            if (page.hasAggregations()) {
//                TermsAggregation aggregation = page.getAggregation("status_tags", TermsAggregation.class);
//                responseAggregates.addAll(
//                        AggregateUtil.getAggregates(aggregation, "currentStatus", "Current Status"));
//                aggregation = page.getAggregation("entry_tags", TermsAggregation.class);
//                responseAggregates.addAll(
//                        AggregateUtil.getAggregates(aggregation, "entryPoint", "Entry Point"));
//                aggregation = page.getAggregation("gender_tags", TermsAggregation.class);
//                responseAggregates.addAll(
//                        AggregateUtil.getAggregates(aggregation, "gender", "Gender"));
//                aggregation = page.getAggregation("biometric_tags", TermsAggregation.class);
//                responseAggregates.addAll(
//                        AggregateUtil.getAggregates(aggregation, "biometric", "Has Biometrics"));
//            }
//
//            Map<String, List<AggregateVM>> map = AggregateUtil.aggregateMap(responseAggregates);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/patients");
//            try {
//                String aggs = objectMapper.writeValueAsString(map);
//                headers.add("Aggregates", aggs);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//        }
//
//    }

    }
}
