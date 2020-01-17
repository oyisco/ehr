package org.lamisplus.lamis.modules.ehr;

import com.foreach.across.core.annotations.AcrossDepends;
import com.foreach.across.core.context.configurer.ComponentScanConfigurer;
import com.foreach.across.modules.hibernate.jpa.AcrossHibernateJpaModule;
import org.lamisplus.modules.base.LamisModule;

@AcrossDepends(required = AcrossHibernateJpaModule.NAME)
public class EHRModule extends LamisModule {
    public static final String NAME = "EHRModule";
    public EHRModule() {
        super();
        addApplicationContextConfigurer(new ComponentScanConfigurer(getClass().getPackage().getName() + ".res",
                getClass().getPackage().getName() + ".service")); }

    @Override
    public String getName() {
        return NAME;
    }
}
