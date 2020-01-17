package org.lamisplus.lamis.modules.ehr.domain.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findByName(String name);
}
