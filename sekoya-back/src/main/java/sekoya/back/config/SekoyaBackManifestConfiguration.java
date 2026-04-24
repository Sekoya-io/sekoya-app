package sekoya.back.config;

import org.iglooproject.config.bootstrap.spring.annotations.ManifestPropertySource;
import org.springframework.context.annotation.Configuration;

@Configuration
@ManifestPropertySource(prefix = "sekoya.back")
public class SekoyaBackManifestConfiguration {}
