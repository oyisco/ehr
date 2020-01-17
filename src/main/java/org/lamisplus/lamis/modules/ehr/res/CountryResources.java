package org.lamisplus.lamis.modules.ehr.res;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.lamis.modules.ehr.service.CountryServices;
import org.lamisplus.modules.base.domain.entities.Country;
import org.lamisplus.modules.base.domain.repositories.CountryRepository;
import org.lamisplus.modules.base.web.errors.BadRequestAlertException;
import org.lamisplus.modules.base.web.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/country")
@Slf4j
public class CountryResources {

    private final CountryRepository countryRepository;
    private final CountryServices countryServices;
    private static final String ENTITY_NAME = "country";

    public CountryResources(CountryRepository countryRepository, CountryServices countryServices) {
        this.countryRepository = countryRepository;
        this.countryServices = countryServices;
    }


    private static Object exist(Country o) {
        throw new BadRequestAlertException("Country Already Exist", ENTITY_NAME, "id Already Exist");
    }

    private static Country notExit() {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
    }

    @PostMapping
    public ResponseEntity<Country> save(@RequestBody Country country) throws URISyntaxException {
        Optional<Country> patient1 = countryRepository.findById(country.getId());
        patient1.map(CountryResources::exist);
        Country result = countryServices.save(country);
        return ResponseEntity.created(new URI("/api/country/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId()))).body(result);
    }

    @PutMapping
    public ResponseEntity<Country> update(@RequestBody Country country) throws URISyntaxException {
        Optional<Country> country1 = countryRepository.findById(country.getId());
        country1.orElseGet(CountryResources::notExit);
        Country result = countryServices.update(country);
        return ResponseEntity.created(new URI("/api/country/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, String.valueOf(result.getId())))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountry() {
        return ResponseEntity.ok(countryRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Country> getSingle(@PathVariable Long id) {
        Optional<Country> country = this.countryRepository.findById(id);
        if (country.isPresent()){
            Country countryArchive = country.get();
            return ResponseEntity.ok(countryArchive);
        } else throw new BadRequestAlertException("Record not found with id ", ENTITY_NAME, "id is  Null");

    }


}
