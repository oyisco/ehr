package org.lamisplus.lamis.modules.ehr.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CodifierRequest {
    private Long id;

    @NotNull
    private String codifierGroup;

    private String version;

    private String code;
    @NotNull
    private List<String> display;

    private String language;


}
