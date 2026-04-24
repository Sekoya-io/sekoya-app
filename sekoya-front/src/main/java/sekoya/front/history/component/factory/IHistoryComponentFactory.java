package sekoya.front.history.component.factory;

import igloo.wicket.factory.IOneParameterComponentFactory;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import sekoya.back.business.history.model.HistoryDifference;

public interface IHistoryComponentFactory
    extends IOneParameterComponentFactory<Component, IModel<HistoryDifference>> {}
