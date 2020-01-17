package org.lamisplus.lamis.modules.ehr.res;

import lombok.extern.slf4j.Slf4j;
import org.lamisplus.modules.base.domain.entities.Province;
import org.lamisplus.modules.base.domain.entities.State;
import org.lamisplus.modules.base.domain.repositories.ProvinceRepository;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/province")
@Slf4j
public class ProvinceResources {
    private static final String ENTITY_NAME = "province";
    private final ProvinceRepository provinceRepository;
    private final StateRepository stateRepository;

    public ProvinceResources(ProvinceRepository provinceRepository, StateRepository stateRepository) {
        this.provinceRepository = provinceRepository;
        this.stateRepository = stateRepository;
    }

    private static Object exist(Province province) {
        throw new BadRequestAlertException("Province Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Province notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }

    @PostMapping
    public ResponseEntity<Province> save(@RequestBody Province province) throws URISyntaxException {
        provinceRepository.findById(province.getId()).map(ProvinceResources::exist);
        Optional<State> state = this.stateRepository.findById(province.getState().getId());
        if (state.isPresent()){
            State state1 = state.get();
            province.setState(state1);
            Province result = this.provinceRepository.save(province);
            return ResponseEntity.created(new URI("/api/province/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId()))).body(result);
        } else throw new BadRequestAlertException("State id did not exist ", ENTITY_NAME, "id is null");

    }

    @PutMapping
    public ResponseEntity<Province> update(@RequestBody Province province) throws URISyntaxException {
        Optional<Province> province11 = this.provinceRepository.findById(province.getId());
        province11.orElseGet(ProvinceResources::notExit);
        State state = this.stateRepository.getOne(province.getState().getId());
        province.setState(state);
        Province result = this.provinceRepository.save(province);
        return ResponseEntity.created(new URI("/api/province/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId())))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<Province>> getAllProvince() {
        return ResponseEntity.ok(this.provinceRepository.findAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> archiveProvince(@PathVariable Long id) {
        Optional<Province> state = this.provinceRepository.findById(id);
        if (state.isPresent()){
            Province stateArchive = state.get();
            stateArchive.setArchive(Boolean.TRUE);
        } else throw new BadRequestAlertException("Record not found with id ", ENTITY_NAME, "id is  Null");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/state/{id}")
    public ResponseEntity<List<Province>> getAllProvince(@PathVariable Long id) {
        State state = this.stateRepository.getOne(id);
        List<Province> stateSet = this.provinceRepository.findByStateOrderByName(state);
        return ResponseEntity.ok(stateSet);
    }


}
