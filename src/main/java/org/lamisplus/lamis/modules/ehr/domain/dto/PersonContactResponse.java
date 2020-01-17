package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class PersonContactResponse {
    private Long id;
    private String mobilePhoneNumber;
    private String alternatePhoneNumber;
    private String email;
    private Long personId;
    private List<AddressResponse> address;
}
