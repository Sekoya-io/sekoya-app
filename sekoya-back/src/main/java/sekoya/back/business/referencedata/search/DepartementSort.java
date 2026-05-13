package sekoya.back.business.referencedata.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.toSortOrder;

import java.util.List;
import java.util.function.Function;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.iglooproject.jpa.more.business.sort.ISort;
import sekoya.back.business.referencedata.model.Departement;

public enum DepartementSort implements ISort<Function<SearchSortFactory, SortFinalStep>> {
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
      return List.of(f -> f.field(Departement.ID).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.DESC;
    }
  },
  POSITION {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(Departement.POSITION).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  LABEL {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(Departement.LABEL_SORT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  CODE_INSEE {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(f -> f.field(Departement.CODE_INSEE).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  },
  REGION {
    @Override
    public List<Function<SearchSortFactory, SortFinalStep>> getSortFields(SortOrder sortOrder) {
      return List.of(
          f -> f.field(Departement.REGION_LABEL_SORT).order(toSortOrder(this, sortOrder)));
    }

    @Override
    public SortOrder getDefaultOrder() {
      return SortOrder.ASC;
    }
  };
}
