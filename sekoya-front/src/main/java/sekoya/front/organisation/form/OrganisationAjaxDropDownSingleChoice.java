package sekoya.front.organisation.form;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.wicket.more.markup.html.select2.AbstractLongIdGenericEntityChoiceProvider;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2AjaxDropDownSingleChoice;
import org.wicketstuff.select2.Response;
import sekoya.back.business.organisation.model.Organisation;
import sekoya.back.business.organisation.search.IOrganisationSearchQuery;
import sekoya.back.business.organisation.search.OrganisationSearchQueryData;
import sekoya.back.business.organisation.search.OrganisationSort;
import sekoya.front.organisation.renderer.OrganisationRenderer;

public class OrganisationAjaxDropDownSingleChoice
    extends GenericSelect2AjaxDropDownSingleChoice<Organisation> {

  private static final long serialVersionUID = 7076114890845943476L;

  public OrganisationAjaxDropDownSingleChoice(String id, IModel<Organisation> model) {
    this(id, model, new ChoiceProvider());
  }

  public OrganisationAjaxDropDownSingleChoice(
      String id,
      IModel<Organisation> model,
      org.wicketstuff.select2.ChoiceProvider<Organisation> choiceProvider) {
    super(id, model, choiceProvider);
  }

  private static class ChoiceProvider
      extends AbstractLongIdGenericEntityChoiceProvider<Organisation> {

    private static final long serialVersionUID = 1L;

    @SpringBean private IOrganisationSearchQuery searchQuery;

    public ChoiceProvider() {
      super(Organisation.class, OrganisationRenderer.get());
      Injector.get().inject(this);
    }

    @Override
    protected void query(String term, int offset, int limit, Response<Organisation> response) {
      OrganisationSearchQueryData data = new OrganisationSearchQueryData();
      data.setNom(term);
      Map<OrganisationSort, SortOrder> sorts =
          ImmutableMap.of(
              OrganisationSort.SCORE, OrganisationSort.SCORE.getDefaultOrder(),
              OrganisationSort.NOM, OrganisationSort.NOM.getDefaultOrder(),
              OrganisationSort.ID, OrganisationSort.ID.getDefaultOrder());
      response.addAll(searchQuery.list(data, sorts, offset, limit));
    }
  }
}
