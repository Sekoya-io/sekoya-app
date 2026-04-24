package sekoya.back.util.init.service;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.util.init.service.AbstractImportDataServiceImpl;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Service;
import sekoya.back.business.SekoyaBackCommonBusinessPackage;
import sekoya.back.business.role.model.Role;
import sekoya.back.business.user.model.User;

@Service
public class ImportDataServiceImpl extends AbstractImportDataServiceImpl {

  @Override
  protected List<String> getReferenceDataPackagesToScan() {
    return Lists.newArrayList(SekoyaBackCommonBusinessPackage.class.getPackage().getName());
  }

  @Override
  protected void importMainBusinessItems(
      Map<String, Map<String, GenericEntity<Long, ?>>> idsMapping, Workbook workbook) {
    doImportItem(idsMapping, workbook, Role.class);
    doImportItem(idsMapping, workbook, User.class);
  }

  @Override
  protected void customizeConversionService(GenericConversionService conversionService) {
    // no customization
  }
}
