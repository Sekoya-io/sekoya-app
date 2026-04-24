package db.migration.production;

import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.stereotype.Component;
import sekoya.back.business.upgrade.model.AbstractUnitDataUpgradeMigration;
import sekoya.back.business.upgrade.model.DataUpgrade_InitStorageUnit;

@SuppressWarnings("squid:S00101") // class named on purpose, skip class name rule
@Component
public class V0_0_0_03__Init_StorageUnit extends AbstractUnitDataUpgradeMigration {

  @Override
  protected Class<? extends IDataUpgrade> getDataUpgradeClass() {
    return DataUpgrade_InitStorageUnit.class;
  }
}
