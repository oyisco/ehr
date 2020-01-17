package org.lamisplus.lamis.modules.ehr.config;

import org.lamisplus.lamis.modules.ehr.domain.EHRDomain;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {EHRDomain.class})
public class DomainConfiguration {
}
