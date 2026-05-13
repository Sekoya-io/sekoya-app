package test.core.metamodel;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import sekoya.back.business.common.model.CodePostal;
import sekoya.back.business.common.model.EmailAddress;
import test.core.AbstractSekoyaTestCase;
import test.core.config.SekoyaBackSpringBootTest;

@SekoyaBackSpringBootTest
class TestMetaModel extends AbstractSekoyaTestCase {

  @Test
  void testMetaModel() throws NoSuchFieldException, SecurityException {
    // Class<?> est utilisé sur GenericEntityReference ; ATTENTION,
    // l'annotation @Type est nécessaire pour un traitement correct par Hibernate.
    super.testMetaModel(
        EmailAddress.class, CodePostal.class, Locale.class, Class.class, Comparable.class);
  }
}
