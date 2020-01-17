package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.extern.slf4j.Slf4j;
import org.lamisplus.modules.base.domain.entities.Person;
import org.lamisplus.modules.base.domain.entities.PersonContact;
import org.lamisplus.modules.base.domain.entities.RelatedPerson;
import org.modelmapper.ModelMapper;

@Slf4j
public class DtoToEntity {


    public static Person convertPersonDTOToPerson(PersonRequest personRequest) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(personRequest, Person.class);
    }


    public static PersonResponse convertPersonToPersonDTO1(Person person) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(person, PersonResponse.class);
    }


    public static PersonContact convertPersonContactDTOToPersonContact(PersonContactRequest personContactRequest) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(personContactRequest, PersonContact.class);
    }


    public static RelatedPerson convertPersonRelativeDTORelative(PersonRelatives personRelatives) {
        return new ModelMapper().map(personRelatives, RelatedPerson.class);
    }


}
