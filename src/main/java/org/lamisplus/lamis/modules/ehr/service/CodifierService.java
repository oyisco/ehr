package org.lamisplus.lamis.modules.ehr.service;

import org.lamisplus.lamis.modules.ehr.domain.dto.CodifierRequest;
import org.lamisplus.modules.base.domain.entities.Codifier;
import org.lamisplus.modules.base.domain.repositories.CodifierRepository;
;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CodifierService {

    private final CodifierRepository codifierRepository;

    public CodifierService(CodifierRepository codifierRepository) {
        this.codifierRepository = codifierRepository;
    }

    public CodifierRequest save(CodifierRequest codifierRequest) {
        List<String> codifierDTODisplay = codifierRequest.getDisplay();
        codifierDTODisplay.forEach(value -> {
            Codifier codifier = new Codifier();
            codifier.setLanguage(codifierRequest.getLanguage());
            codifier.setCodifierGroup(codifierRequest.getCodifierGroup());
            codifier.setVersion(codifierRequest.getVersion());
            codifier.setCode(codifierRequest.getCode());
            codifier.setCode(value);
            this.codifierRepository.save(codifier);
        });
        return codifierRequest;
    }


    public CodifierRequest update(CodifierRequest codifierRequest) {
        List<Codifier> country = codifierRepository.findByCodifierGroup(codifierRequest.getCodifierGroup());
        country.forEach(this.codifierRepository::delete);
        List<String> codifierRequestValue = codifierRequest.getDisplay();
        codifierRequestValue.forEach(value -> {
            Codifier codifier = new Codifier();
            codifier.setLanguage(codifierRequest.getLanguage());
            codifier.setCodifierGroup(codifierRequest.getCodifierGroup());
            codifier.setVersion(codifierRequest.getVersion());
            codifier.setCode(codifierRequest.getCode());
            codifier.setDisplay(value);
            this.codifierRepository.save(codifier);

        });
        return codifierRequest;
    }
}
