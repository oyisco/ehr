package org.lamisplus.lamis.modules.ehr.extensions;

import com.foreach.across.core.annotations.ModuleConfiguration;
import com.foreach.across.modules.hibernate.jpa.AcrossHibernateJpaModule;
import com.foreach.across.modules.hibernate.provider.HibernatePackageConfigurer;
import com.foreach.across.modules.hibernate.provider.HibernatePackageRegistry;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.lamis.modules.ehr.domain.EHRDomain;
import org.lamisplus.modules.base.domain.BaseDomain;

@ModuleConfiguration(AcrossHibernateJpaModule.NAME)
@Slf4j
public class EntityScanConfiguration implements HibernatePackageConfigurer
{
	@Override
	public void configureHibernatePackage( HibernatePackageRegistry hibernatePackageRegistry ) {
		hibernatePackageRegistry.addPackageToScan(EHRDomain.class, BaseDomain.class, CoreDomain.class);
	}
}
