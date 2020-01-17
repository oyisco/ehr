package org.lamisplus.lamis.modules.ehr.domain.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObservationRequest implements Serializable {
    private Long id;
    @NotNull
    private JsonNode formData;
    @NotNull
    private String formCode;
    @NotNull
    private Long patientId;
    @NotNull
    private Long serviceId;
    @NotNull
    private LocalDate dateEncounter;


}
