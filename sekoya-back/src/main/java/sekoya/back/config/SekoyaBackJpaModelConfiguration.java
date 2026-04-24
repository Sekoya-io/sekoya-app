package sekoya.back.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import sekoya.back.business.SekoyaBackCommonBusinessPackage;

@Configuration
@Import(SekoyaBackManifestConfiguration.class)
@EntityScan(basePackageClasses = SekoyaBackCommonBusinessPackage.class)
public class SekoyaBackJpaModelConfiguration {}
