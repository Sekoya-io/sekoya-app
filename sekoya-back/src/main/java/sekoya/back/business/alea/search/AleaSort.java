package sekoya.back.business.alea.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import java.util.List;
import java.util.function.Function;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;
import sekoya.back.business.alea.model.Alea;

public enum AleaSort implements ISort<Function<SearchSortFactory, SortFinalStep>> {
  SCORE {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.score());
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  ID {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(Alea.ID).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  TYPE {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(Alea.TYPE).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  IMPACT_POTENTIEL_BRUT {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(Alea.IMPACT_POTENTIEL_BRUT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  };
}
