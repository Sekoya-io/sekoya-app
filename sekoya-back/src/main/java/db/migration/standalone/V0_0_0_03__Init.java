package db.migration.standalone;

import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.stereotype.Component;
import sekoya.back.business.upgrade.model.AbstractDataUpgradeMigration;
import sekoya.back.business.upgrade.model.DataUpgrade_InitDataExcel;
import sekoya.back.business.upgrade.model.DataUpgrade_InitStorageUnit;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
@Component
public class V0_0_0_03__Init extends AbstractDataUpgradeMigration {

  @Override
  protected Collection<Class<? extends IDataUpgrade>> getDataUpgradeClasses() {
    return List.of(DataUpgrade_InitDataExcel.class, DataUpgrade_InitStorageUnit.class);
  }
}
