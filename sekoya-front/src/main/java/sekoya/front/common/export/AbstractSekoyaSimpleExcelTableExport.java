package sekoya.front.common.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.wicket.Component;
import org.iglooproject.wicket.more.export.excel.AbstractSimpleExcelTableExport;

public class AbstractSekoyaSimpleExcelTableExport extends AbstractSimpleExcelTableExport {

  protected AbstractSekoyaSimpleExcelTableExport(Component component) {
    super(component);
  }

  protected AbstractSekoyaSimpleExcelTableExport(Workbook workbook, Component component) {
    super(workbook, component);
  }

  @Override
  protected void initColors() {
    setHeaderBackgroundColor("#6610f2");
    super.initColors();
  }
}
