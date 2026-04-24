package sekoya.back.cli;

import java.util.concurrent.Callable;
import org.iglooproject.jpa.sql.BaseSqlExporterCommand;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;
import sekoya.back.config.SekoyaBackJpaModelConfiguration;

@Configuration
@Import(SekoyaBackJpaModelConfiguration.class) // entity/model scanning
public class SqlExporterCommand extends BaseSqlExporterCommand implements Callable<Integer> {

  static void main(String[] args) {
    CommandLine cl = new CommandLine(new SqlExporterCommand());
    System.exit(cl.execute(args));
  }
}
