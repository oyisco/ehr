package org.lamisplus.lamis.modules.ehr.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentifierTypeRepository extends JpaRepository<IdentifierType, Long>
    {
    IdentifierType findByName(String name);
     }
