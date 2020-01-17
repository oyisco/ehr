package org.lamisplus.lamis.modules.ehr.service;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.lamis.modules.ehr.domain.dto.*;
import org.lamisplus.lamis.modules.ehr.domain.repositories.*;
import org.lamisplus.modules.base.domain.entities.*;
import org.lamisplus.modules.base.domain.repositories.CodifierRepository;
import org.lamisplus.modules.base.domain.repositories.CountryRepository;
import org.lamisplus.modules.base.domain.repositories.ModuleRepository;
import org.lamisplus.modules.base.domain.repositories.StateRepository;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.ehr.domain.entities.IdentifierType;
import org.lamisplus.modules.ehr.domain.entities.Patient;
import org.lamisplus.modules.ehr.domain.entities.Service;
import org.lamisplus.modules.ehr.domain.entities.ServiceEnrollment;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class PatientServices {
    private static final String ENTITY_NAME = "patient";
    private final PatientRepository patientRepository;
    private final PersonRepository personRepository;
    private final ServiceRepository serviceRepository;
    private final IdentifierTypeRepository identifierTypeRepository;
    private final ServiceEnrollmentRepository serviceEnrollmentRepository1;
    private final PersonContactRepository personContactsRepository;
    private final RelatedPersonRepository personRelativeRepository;
    private final ModuleRepository moduleRepository;
    private final AddressRepository addressRepository;
    private final CodifierRepository codifierRepository;
    private final CountryRepository countriesRepository;
    private final StateRepository stateRepository;
    private final ProvinceRepository provinceRepository;

    public PatientServices(PatientRepository patientRepository, PersonRepository personRepository, ServiceRepository serviceRepository, IdentifierTypeRepository identifierTypeRepository, ServiceEnrollmentRepository serviceEnrollmentRepository1, PersonContactRepository personContactsRepository, RelatedPersonRepository personRelativeRepository, ModuleRepository moduleRepository, AddressRepository addressRepository, CodifierRepository codifierRepository, CountryRepository countriesRepository, StateRepository stateRepository, ProvinceRepository provinceRepository) {
        this.patientRepository = patientRepository;
        this.personRepository = personRepository;
        this.serviceRepository = serviceRepository;
        this.identifierTypeRepository = identifierTypeRepository;
        this.serviceEnrollmentRepository1 = serviceEnrollmentRepository1;
        this.personContactsRepository = personContactsRepository;
        this.personRelativeRepository = personRelativeRepository;
        this.moduleRepository = moduleRepository;
        this.addressRepository = addressRepository;
        this.codifierRepository = codifierRepository;
        this.countriesRepository = countriesRepository;
        this.stateRepository = stateRepository;
        this.provinceRepository = provinceRepository;
    }


    private static Object exist(Patient o) {
        throw new BadRequestAlertException("Patient Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Patient notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }

    public Person save(PatientRequest patientRequest) {
        Optional<Patient> patient1 = this.patientRepository.findById(patientRequest.getId());
        patient1.map(PatientServices::exist);
        LOG.info("SAVING... " + patientRequest);
        Person person = new Person();
        person.setFirstName(patientRequest.getPerson().getFirstName());
        person.setLastName(patientRequest.getPerson().getLastName());
        person.setOtherNames(patientRequest.getPerson().getOtherNames());
        Codifier maritalStatus = codifierRepository.getOne(patientRequest.getPerson().getMaritalStatusId());
        person.setMaritalStatus(maritalStatus);
        person.setDob(patientRequest.getPerson().getDob());
        person.setDobEstimated(patientRequest.getPerson().getDobEstimated());
        Codifier gender = codifierRepository.getOne(patientRequest.getPerson().getGenderId());
        person.setGender(gender);
        Codifier education = codifierRepository.getOne(patientRequest.getPerson().getEducationId());
        person.setEducation(education);
        Codifier occupation = codifierRepository.getOne(patientRequest.getPerson().getOccupationId());
        person.setOccupation(occupation);
        Person person1 = this.personRepository.save(person);
        Patient p = new Patient();
        p.setPerson(person1);
        Patient patient2 = this.patientRepository.save(p);
        Service service = this.serviceRepository.findByName("GEN_SERVICE");
        if (service == null){
            Module module = new Module();
            module.setName("PATIENT");
            Module module1 = moduleRepository.save(module);
            Service setService = new Service();
            setService.setName("GEN_SERVICE");
            setService.setModule(module1);
            this.serviceRepository.save(setService);
        }

        IdentifierType identifierType = this.identifierTypeRepository.findByName("HOSPITAL_NUMBER");
        if (identifierType == null){
            IdentifierType identifierType1 = new IdentifierType();
            identifierType1.setName("HOSPITAL_NUMBER");
            identifierType1.setValidator("");
            this.identifierTypeRepository.save(identifierType1);
        }

        Service service3 = serviceRepository.findByName("GEN_SERVICE");
        IdentifierType identifierType4 = identifierTypeRepository.findByName("HOSPITAL_NUMBER");
        ServiceEnrollment serviceEnrollment = new ServiceEnrollment();
        serviceEnrollment.setDateEnrollment(patientRequest.getDateRegistration());
        serviceEnrollment.setIdentifierType(identifierType4);
        serviceEnrollment.setIdentifier(patientRequest.getHospitalNumber());
        serviceEnrollment.setPatient(patient2);
        serviceEnrollment.setService(service3);
        this.serviceEnrollmentRepository1.save(serviceEnrollment);

        PersonContact personContact = DtoToEntity.convertPersonContactDTOToPersonContact(patientRequest.getPerson().getPersonContact());
        personContact.setPerson(person1);
        PersonContact personContact1 = this.personContactsRepository.save(personContact);

        List<AddressDTO> address1 = patientRequest.getPerson().getPersonContact().getAddress();
        address1.forEach(addressDTO -> {
            Address address = new Address();
            Province province = provinceRepository.getOne(addressDTO.getProvinceId());
            address.setProvince(province);
            State state = stateRepository.getOne(addressDTO.getState());
            state.setId(addressDTO.getState());
            address.setState(state);
            Country country = countriesRepository.getOne(addressDTO.getCountry());
            address.setCountry(country);
            address.setPersonContact(personContact1);
            address.setCity(addressDTO.getCity());
            address.setLandmark(addressDTO.getLandmark());
            address.setStreetAddress(addressDTO.getStreetAddress());
            address.setZipCode(addressDTO.getZipCode());
            this.addressRepository.save(address);
        });


        List<PersonRelatives> personRelatives = patientRequest.getPerson().getPersonRelatives();
        personRelatives.stream().map(DtoToEntity::convertPersonRelativeDTORelative).forEach(personRelative -> {
            personRelative.setPerson(person1);
            this.personRelativeRepository.save(personRelative);
        });
        return person;
    }


    public Person update(PatientRequest patientRequest) {
        Optional<Patient> patient1 = this.patientRepository.findById(patientRequest.getId());
        patient1.orElseGet(PatientServices::notExit);
        Person person = DtoToEntity.convertPersonDTOToPerson(patientRequest.getPerson());
        Person person1 = this.personRepository.save(person);
        Patient p = new Patient();
        LOG.info("PERSON ID " + person1.getId());
        p.setPerson(person1);
        this.patientRepository.save(p);

        Optional<PersonContact> personContact = this.personContactsRepository.findById(patientRequest.getPerson().getPersonContact().getId());
        personContact.map(personContact1 -> {
            personContact1 = DtoToEntity.convertPersonContactDTOToPersonContact(patientRequest.getPerson().getPersonContact());
            personContact1.setPerson(person1);
            personContactsRepository.save(personContact1);
            return null;
        });

        List<RelatedPerson> personRelative1 = this.personRelativeRepository.findByPerson(person1);
        if (!personRelative1.isEmpty()) this.personRelativeRepository.deleteAll(personRelative1);
        List<RelatedPerson> personRelatives = patientRequest.getPerson().getPersonRelatives();
        personRelatives.stream().map(DtoToEntity::convertPersonRelativeDTORelative).forEach(personRelative -> {
            personRelative.setPerson(person1);
            this.personRelativeRepository.save(personRelative);
        });
        return person;
    }


    public List<PatientResponseDTO> getAllPatient() {
        List<Patient> patientList = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOS = new ArrayList<>();
        patientList.forEach(patient -> {
            Person person = personRepository.getOne(patient.getPerson().getId());
            PersonContact personContact = personContactsRepository.findByPerson(patient.getPerson());
            List<Address> addressList = addressRepository.findByPersonContact(personContact);
            List<AddressResponse> addressList1 = new ArrayList<>();
            addressList.forEach(address -> {
                AddressResponse addressDTO = new AddressResponse();
                Province province = provinceRepository.getOne(address.getProvince().getId());
                LOG.info("PROVIN ID " + address.getProvince().getId());
                LOG.info("PROVIN NAME " + province.getName());
                addressDTO.setProvince(province.getName());
                addressDTO.setZipCode(address.getZipCode());
                addressDTO.setLandmark(address.getLandmark());
                addressDTO.setStreetAddress(address.getStreetAddress());
                addressDTO.setCity(address.getCity());
                State state = stateRepository.getOne(address.getState().getId());
                addressDTO.setState(state.getName());
                Country country = countriesRepository.getOne(address.getCountry().getId());
                addressDTO.setCountry(country.getName());
                addressList1.add(addressDTO);
            });

            ModelMapper modelMapper = new ModelMapper();
            PatientResponseDTO patientResponseDTO = modelMapper.map(patient, PatientResponseDTO.class);
            ServiceEnrollment patientServiceEnrollment = serviceEnrollmentRepository1.findByPatient(patient);
            patientResponseDTO.setHospitalNumber(patientServiceEnrollment.getIdentifier());
            PersonResponse personResponse = DtoToEntity.convertPersonToPersonDTO1(person);
            PersonContactResponse personContactRequestDTO = new PersonContactResponse();
            personContactRequestDTO.setMobilePhoneNumber(personContact.getMobilePhoneNumber());
            personContactRequestDTO.setAlternatePhoneNumber(personContact.getAlternatePhoneNumber());
            personContactRequestDTO.setEmail(personContact.getEmail());
            personContactRequestDTO.setAddress(addressList1);
            personResponse.setPersonContact(personContactRequestDTO);
            System.out.println("GENDER "+patient.getPerson().getGender().getId());
            Codifier gender = codifierRepository.getOne(patient.getPerson().getGender().getId());

            personResponse.setGender(gender.getDisplay());

            Codifier title = codifierRepository.getOne(patient.getPerson().getMaritalStatus().getId());
            personResponse.setTitle(title.getDisplay());

            Codifier occupation = codifierRepository.getOne(patient.getPerson().getOccupation().getId());
            personResponse.setOccupation(occupation.getDisplay());

            Codifier education = codifierRepository.getOne(patient.getPerson().getEducation().getId());
            personResponse.setEducation(education.getDisplay());

            List<RelatedPerson> personRelative = personRelativeRepository.findByPerson(patient.getPerson());
            List<PersonRelativesResponse> personRelativesDTOList = new ArrayList<>();
            personRelative.forEach(personRelative1 -> {
                PersonRelativesResponse personRelativesResponse = new PersonRelativesResponse();
                personRelativesResponse.setFirstName(personRelative1.getFirstName());
                personRelativesResponse.setLastName(personRelative1.getLastName());
                personRelativesResponse.setOtherNames(personRelative1.getOtherNames());
                personRelativesResponse.setEmail(personRelative1.getEmail());
                personRelativesResponse.setMobilePhoneNumber(personRelative1.getMobilePhoneNumber());
                personRelativesResponse.setAlternatePhoneNumber(personRelative1.getAlternatePhoneNumber());
                Codifier codifier = codifierRepository.getOne(patient.getPerson().getEducation().getId());
                personRelativesResponse.setRelationshipType(codifier.getDisplay());
                personRelativesResponse.setAddress1(personRelative1.getAddress());
                personRelativesDTOList.add(personRelativesResponse);
            });
            personResponse.setPersonRelatives(personRelativesDTOList);
            patientResponseDTO.setPerson(personResponse);
            patientResponseDTOS.add(patientResponseDTO);
        });
        return patientResponseDTOS;
    }


    public PatientResponseDTO getSingle(Long id) {
        LOG.info("Gender {} ", id);
        Optional<Patient> patient1 = patientRepository.findById(id);
        patient1.orElseGet(PatientServices::notExit);
        Patient patient = patient1.get();
        Person person = personRepository.getOne(patient.getPerson().getId());
        PersonContact personContact = personContactsRepository.findByPerson(patient.getPerson());
        LOG.info("Gender {} ", id);
        person.setPersonContact(personContact);
        ModelMapper modelMapper = new ModelMapper();
        PatientResponseDTO patientResponseDTO = modelMapper.map(patient, PatientResponseDTO.class);
        ServiceEnrollment patientServiceEnrollment = serviceEnrollmentRepository1.findByPatient(patient);
        patientResponseDTO.setHospitalNumber(patientServiceEnrollment.getIdentifier());
        PersonResponse personResponse = DtoToEntity.convertPersonToPersonDTO1(person);
        Codifier gender = codifierRepository.getOne(patient.getPerson().getGender().getId());
        LOG.debug("Gender {} ", gender.getDisplay());
        LOG.info("Gender {} ", id);
        personResponse.setGender(gender.getDisplay());
        personResponse.setTitle(codifierRepository.getOne(patient.getPerson().getMaritalStatus().getId()).getDisplay());
        personResponse.setOccupation(codifierRepository.getOne(patient.getPerson().getOccupation().getId()).getDisplay());
        personResponse.setEducation(codifierRepository.getOne(patient.getPerson().getEducation().getId()).getDisplay());
        LOG.info("Gender {} ", id);
        List<Address> addressList = addressRepository.findByPersonContact(personContact);
        List<AddressResponse> addressList1 = new ArrayList<>();
        addressList.forEach(address -> {
            AddressResponse addressDTO = new AddressResponse();
            Province province = provinceRepository.getOne(address.getProvince().getId());
            LOG.info("PROVIN ID " + address.getProvince().getId());
            LOG.info("PROVIN NAME " + province.getName());
            addressDTO.setProvince(province.getName());
            addressDTO.setZipCode(address.getZipCode());
            addressDTO.setLandmark(address.getLandmark());
            addressDTO.setStreetAddress(address.getStreetAddress());
            addressDTO.setCity(address.getCity());
            State state = stateRepository.getOne(address.getState().getId());
            addressDTO.setState(state.getName());
            Country country = countriesRepository.getOne(address.getCountry().getId());
            addressDTO.setCountry(country.getName());
            addressList1.add(addressDTO);
        });
       PersonContactResponse personContactResponse = new PersonContactResponse();
       personContactResponse.setAddress(addressList1);
        List<PersonRelative> personRelative = personRelativeRepository.findByPerson(patient.getPerson());
        List<PersonRelativesResponse> personRelativesDTOList = new ArrayList<>();
        personRelative.forEach(personRelative1 -> {
            PersonRelativesResponse personRelativesResponse = new PersonRelativesResponse();
            personRelativesResponse.setFirstName(personRelative1.getFirstName());
            personRelativesResponse.setLastName(personRelative1.getLastName());
            personRelativesResponse.setOtherNames(personRelative1.getOtherNames());
            personRelativesResponse.setEmail(personRelative1.getEmail());
            personRelativesResponse.setMobilePhoneNumber(personRelative1.getMobilePhoneNumber());
            personRelativesResponse.setAlternatePhoneNumber(personRelative1.getAlternatePhoneNumber());
            personRelativesResponse.setRelationshipType(codifierRepository.getOne(patient.getPerson().getEducation().getId()).getDisplay());
            personRelativesResponse.setAddress1(personRelative1.getAddress());
            personRelativesDTOList.add(personRelativesResponse);
        });

        personResponse.setPersonRelatives(personRelativesDTOList);
        patientResponseDTO.setPerson(personResponse);
        return patientResponseDTO;
    }


}
