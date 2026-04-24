package sekoya.back.business.task.model;

import org.iglooproject.jpa.more.business.task.model.IQueueId;

public enum SekoyaTaskQueueId implements IQueueId {

// Define queue IDs here.
;

  @Override
  public String getUniqueStringId() {
    return name();
  }
}
