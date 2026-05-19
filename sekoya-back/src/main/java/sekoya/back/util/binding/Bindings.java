package sekoya.back.util.binding;

import sekoya.back.business.announcement.model.AnnouncementBinding;
import sekoya.back.business.common.model.CodePostalBinding;
import sekoya.back.business.common.model.EmailAddressBinding;
import sekoya.back.business.history.model.HistoryDifferenceBinding;
import sekoya.back.business.history.model.HistoryLogBinding;
import sekoya.back.business.history.search.HistoryLogSearchQueryDataBinding;
import sekoya.back.business.organisation.model.OrganisationBinding;
import sekoya.back.business.organisation.search.OrganisationSearchQueryDataBinding;
import sekoya.back.business.referencedata.model.CommuneBinding;
import sekoya.back.business.referencedata.model.DepartementBinding;
import sekoya.back.business.referencedata.model.IReferenceDataBindingInterfaceBinding;
import sekoya.back.business.referencedata.model.RegionBinding;
import sekoya.back.business.referencedata.search.CommuneSearchQueryDataBinding;
import sekoya.back.business.referencedata.search.DepartementSearchQueryDataBinding;
import sekoya.back.business.referencedata.search.IBasicReferenceDataSearchQueryDataBindingInterfaceBinding;
import sekoya.back.business.referencedata.search.RegionSearchQueryDataBinding;
import sekoya.back.business.role.model.RoleBinding;
import sekoya.back.business.site.model.SiteBinding;
import sekoya.back.business.site.search.SiteSearchQueryDataBinding;
import sekoya.back.business.user.model.UserBinding;
import sekoya.back.business.user.search.UserSearchQueryDataBinding;

public final class Bindings {

  private static final EmailAddressBinding EMAIL_ADDRESS = new EmailAddressBinding();
  private static final CodePostalBinding CODE_POSTAL = new CodePostalBinding();

  private static final OrganisationBinding ORGANISATION = new OrganisationBinding();
  private static final OrganisationSearchQueryDataBinding ORGANISATION_SEARCH_QUERY_DATA =
      new OrganisationSearchQueryDataBinding();

  private static final SiteBinding SITE = new SiteBinding();
  private static final SiteSearchQueryDataBinding SITE_SEARCH_QUERY_DATA =
      new SiteSearchQueryDataBinding();

  private static final IReferenceDataBindingInterfaceBinding REFERENCE_DATA =
      new IReferenceDataBindingInterfaceBinding();
  private static final IBasicReferenceDataSearchQueryDataBindingInterfaceBinding
      BASIC_REFERENCE_DATA_SEARCH_QUERY_DATA =
          new IBasicReferenceDataSearchQueryDataBindingInterfaceBinding();
  private static final CommuneBinding COMMUNE = new CommuneBinding();
  private static final CommuneSearchQueryDataBinding COMMUNE_SEARCH_QUERY_DATA =
      new CommuneSearchQueryDataBinding();
  private static final DepartementBinding DEPARTEMENT = new DepartementBinding();
  private static final DepartementSearchQueryDataBinding DEPARTEMENT_SEARCH_QUERY_DATA =
      new DepartementSearchQueryDataBinding();
  private static final RegionBinding REGION = new RegionBinding();
  private static final RegionSearchQueryDataBinding REGION_SEARCH_QUERY_DATA =
      new RegionSearchQueryDataBinding();

  private static final UserBinding USER = new UserBinding();
  private static final UserSearchQueryDataBinding USER_SEARCH_QUERY_DATA =
      new UserSearchQueryDataBinding();

  private static final RoleBinding ROLE = new RoleBinding();

  private static final AnnouncementBinding ANNOUNCEMENT = new AnnouncementBinding();

  private static final HistoryLogBinding HISTORY_LOG = new HistoryLogBinding();
  private static final HistoryLogSearchQueryDataBinding HISTORY_LOG_SEARCH_QUERY_DATA =
      new HistoryLogSearchQueryDataBinding();
  private static final HistoryDifferenceBinding HISTORY_DIFFERENCE = new HistoryDifferenceBinding();

  public static EmailAddressBinding emailAddress() {
    return EMAIL_ADDRESS;
  }

  public static CodePostalBinding codePostal() {
    return CODE_POSTAL;
  }

  public static OrganisationBinding organisation() {
    return ORGANISATION;
  }

  public static OrganisationSearchQueryDataBinding organisationSearchQueryData() {
    return ORGANISATION_SEARCH_QUERY_DATA;
  }

  public static SiteBinding site() {
    return SITE;
  }

  public static SiteSearchQueryDataBinding siteSearchQueryData() {
    return SITE_SEARCH_QUERY_DATA;
  }

  public static IReferenceDataBindingInterfaceBinding referenceData() {
    return REFERENCE_DATA;
  }

  public static IBasicReferenceDataSearchQueryDataBindingInterfaceBinding
      basicReferenceDataSearchQueryData() {
    return BASIC_REFERENCE_DATA_SEARCH_QUERY_DATA;
  }

  public static CommuneBinding commune() {
    return COMMUNE;
  }

  public static CommuneSearchQueryDataBinding communeSearchQueryData() {
    return COMMUNE_SEARCH_QUERY_DATA;
  }

  public static DepartementBinding departement() {
    return DEPARTEMENT;
  }

  public static DepartementSearchQueryDataBinding departementSearchQueryData() {
    return DEPARTEMENT_SEARCH_QUERY_DATA;
  }

  public static RegionBinding region() {
    return REGION;
  }

  public static RegionSearchQueryDataBinding regionSearchQueryData() {
    return REGION_SEARCH_QUERY_DATA;
  }

  public static UserBinding user() {
    return USER;
  }

  public static UserSearchQueryDataBinding userSearchQueryData() {
    return USER_SEARCH_QUERY_DATA;
  }

  public static RoleBinding role() {
    return ROLE;
  }

  public static AnnouncementBinding announcement() {
    return ANNOUNCEMENT;
  }

  public static HistoryLogBinding historyLog() {
    return HISTORY_LOG;
  }

  public static HistoryLogSearchQueryDataBinding historyLogSearchQueryData() {
    return HISTORY_LOG_SEARCH_QUERY_DATA;
  }

  public static HistoryDifferenceBinding historyDifference() {
    return HISTORY_DIFFERENCE;
  }

  private Bindings() {}
}
