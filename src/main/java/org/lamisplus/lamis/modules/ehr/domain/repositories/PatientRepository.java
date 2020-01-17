package org.lamisplus.lamis.modules.ehr.domain.repositories;

import org.lamisplus.modules.base.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByPerson(Person person);
}