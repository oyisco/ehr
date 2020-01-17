package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;

@Data
public class PersonRelativesResponse {
    private String firstName;
    private String lastName;
    private String otherNames;
    private String email;
    private String mobilePhoneNumber;
    private String alternatePhoneNumber;
    private String relationshipType;
    private String address1;
}
