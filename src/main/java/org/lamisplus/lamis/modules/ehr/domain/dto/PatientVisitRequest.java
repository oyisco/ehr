package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PatientVisitRequest {
    private Long id;
    private LocalDate dateVisitStart;
    private LocalTime timeVisitStart;
    private Long patientId;
    private Long visitTypeId;

}
