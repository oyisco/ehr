package org.lamisplus.lamis.modules.ehr.res;

import lombok.extern.slf4j.Slf4j;
import org.lamisplus.lamis.modules.ehr.domain.dto.CodifierRequest;
import org.lamisplus.lamis.modules.ehr.service.CodifierService;
import org.lamisplus.modules.base.domain.entities.Codifier;
import org.lamisplus.modules.base.domain.repositories.CodifierRepository;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/codifier")
@Slf4j
public class CodifierResources {
    private final CodifierService codifierService;
    private final CodifierRepository codifierRepository;
    private static final String ENTITY_NAME = "codifier";

    public CodifierResources(CodifierService codifierService, CodifierRepository codifierRepository) {
        this.codifierService = codifierService;
        this.codifierRepository = codifierRepository;
    }


    private static Object exist(Codifier o) {
        throw new BadRequestAlertException("Code  Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Codifier notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody CodifierRequest codifierRequest) throws URISyntaxException {
        codifierRepository.findById(codifierRequest.getId()).map(CodifierResources::exist);
        CodifierRequest codifierRequest1 = codifierService.save(codifierRequest);
        return ResponseEntity.created(new URI("/api/codifier/" + codifierRequest1.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(codifierRequest1.getId()))).body(codifierRequest1);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CodifierRequest codifierRequest) throws URISyntaxException {
        Optional<Codifier> patient1 = codifierRepository.findById(codifierRequest.getId());
        patient1.orElseGet(CodifierResources::notExit);
        CodifierRequest codifierRequest1 = codifierService.update(codifierRequest);
        return ResponseEntity.created(new URI("/api/codifier/" + codifierRequest.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(codifierRequest.getId()))).body(codifierRequest1);

    }

    @GetMapping
    public ResponseEntity<List<Codifier>> getAllCodifier() {
        return ResponseEntity.ok(this.codifierRepository.findAll());
    }


    @GetMapping("/{system}")
    public ResponseEntity<List<Codifier>> getAllCodifier(@PathVariable String system) {
        List<Codifier> codifiers = this.codifierRepository.findByCodifierGroup(system);
        if (!codifiers.isEmpty()) return ResponseEntity.ok(codifiers);
        else throw new BadRequestAlertException("Record not found ", ENTITY_NAME, "id is  Null");

    }

}
