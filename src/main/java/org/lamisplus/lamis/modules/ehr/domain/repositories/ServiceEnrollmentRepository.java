package org.lamisplus.lamis.modules.ehr.domain.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceEnrollmentRepository extends JpaRepository<ServiceEnrollment, Long> {

  ServiceEnrollment findByPatient(Patient patient);

  List<ServiceEnrollment> findByIdentifier(String identifyValue);
}
