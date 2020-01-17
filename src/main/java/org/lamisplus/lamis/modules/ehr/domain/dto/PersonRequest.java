package org.lamisplus.lamis.modules.ehr.domain.dto;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PersonRequest
{
    private Long id;
    private String firstName;
    private String lastName;
    private String otherNames;
    private Long maritalStatusId;
    private LocalDate dob;
    private Boolean dobEstimated;
    private Long genderId;
    private Long educationId;
    private Long occupationId;
    private PersonContactRequest personContact;
    private List<PersonRelatives> personRelatives;
}
