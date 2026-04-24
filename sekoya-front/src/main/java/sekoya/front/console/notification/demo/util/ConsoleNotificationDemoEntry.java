package sekoya.front.console.notification.demo.util;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import sekoya.back.business.notification.service.ISekoyaNotificationContentDescriptorFactory;

public abstract class ConsoleNotificationDemoEntry
    implements IModel<INotificationContentDescriptor> {

  private static final long serialVersionUID = 1L;

  @SpringBean protected ISekoyaNotificationContentDescriptorFactory descriptorService;

  private final String messageKeySuffix;

  protected ConsoleNotificationDemoEntry(String messageKeySuffix) {
    super();
    Injector.get().inject(this);
    this.messageKeySuffix = messageKeySuffix;
  }

  public IModel<String> getLabelModel() {
    return new ResourceModel(
        "console.notification.demo.entry.label." + messageKeySuffix, messageKeySuffix);
  }

  @Override
  public final INotificationContentDescriptor getObject() {
    return getDescriptor();
  }

  protected abstract INotificationContentDescriptor getDescriptor();
}
