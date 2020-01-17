package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PatientDemography {
    private Long patientId;
    private LocalDate dateRegistration;
    private String hospitalNumber;
    private Long personId;
    private String firstName;
    private String lastName;
    private String otherNames;
    private String title;
    private LocalDate dob;
    private Boolean dobEstimated;
    private String gender;
    private String education;
    private String occupation;
}
