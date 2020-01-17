package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long provinceId;
    private String zipCode;
    private  String landmark;
    private String streetAddress;
    private String city;
    private Long state;
    private Long country;
}
