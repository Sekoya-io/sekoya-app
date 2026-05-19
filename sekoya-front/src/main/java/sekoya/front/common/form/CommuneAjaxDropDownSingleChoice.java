package sekoya.front.common.form;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;
import sekoya.back.business.referencedata.model.Commune;
import sekoya.back.business.referencedata.search.CommuneSearchQueryData;
import sekoya.back.business.referencedata.search.CommuneSort;
import sekoya.back.business.referencedata.search.ICommuneSearchQuery;
import sekoya.front.common.renderer.CommuneRenderer;

public class CommuneAjaxDropDownSingleChoice
    extends GenericSelect2AjaxDropDownSingleChoice<Commune> {

  private static final long serialVersionUID = 1L;

  public CommuneAjaxDropDownSingleChoice(String id, IModel<Commune> model) {
    this(id, model, UnaryOperator.identity());
  }

  public CommuneAjaxDropDownSingleChoice(
      String id, IModel<Commune> model, UnaryOperator<ChoiceProvider> choicesProviderOperator) {
    super(id, model, choicesProviderOperator.apply(new ChoiceProvider()));
  }

  public static class ChoiceProvider extends AbstractLongIdGenericEntityChoiceProvider<Commune> {

    private static final long serialVersionUID = 1L;

    @SpringBean private ICommuneSearchQuery searchQuery;

    public ChoiceProvider() {
      super(Commune.class, CommuneRenderer.get());
      Injector.get().inject(this);
    }

    @Override
    protected void query(String term, int offset, int limit, Response<Commune> response) {
      CommuneSearchQueryData data = new CommuneSearchQueryData();
      data.setTerm(term);
      Map<CommuneSort, SortOrder> sorts =
          ImmutableMap.of(
              CommuneSort.POSITION, CommuneSort.POSITION.getDefaultOrder(),
              CommuneSort.LABEL, CommuneSort.LABEL.getDefaultOrder(),
              CommuneSort.ID, CommuneSort.ID.getDefaultOrder());
      response.addAll(searchQuery.list(data, sorts, offset, limit));
    }
  }
}
