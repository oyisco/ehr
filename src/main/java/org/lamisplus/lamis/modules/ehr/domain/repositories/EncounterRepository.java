package org.lamisplus.lamis.modules.ehr.domain.repositories;

import java.time.LocalDate;


public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    Optional<Encounter> findByPatientAndDateAndService(Patient patient, LocalDate date, Service service);
}
