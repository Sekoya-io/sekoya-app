package sekoya.back.config;

import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.util.transaction.CoreJpaMoreUtilTransactionPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sekoya.back.SekoyaBackPackage;

@Configuration
@Import(SekoyaBackJpaModelConfiguration.class)
@ComponentScan(
    basePackageClasses = {
      SekoyaBackPackage.class,
      CoreJpaMoreUtilTransactionPackage.class,
      CoreJpaMoreBusinessPackage.class
    },
    excludeFilters = @Filter(classes = Configuration.class))
public class SekoyaBackJpaConfiguration {}
