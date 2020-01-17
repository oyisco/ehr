package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;

@Data
public class AddressResponse {
    private String province;
    private String zipCode;
    private String landmark;
    private String streetAddress;
    private String city;
    private String state;
    private String country;
}
