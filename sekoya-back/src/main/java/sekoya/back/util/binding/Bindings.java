package sekoya.back.util.binding;

import sekoya.back.business.announcement.model.AnnouncementBinding;
import sekoya.back.business.common.model.EmailAddressBinding;
import sekoya.back.business.common.model.PhoneNumberBinding;
import sekoya.back.business.common.model.PostalCodeBinding;
import sekoya.back.business.history.model.HistoryDifferenceBinding;
import sekoya.back.business.history.model.HistoryLogBinding;
import sekoya.back.business.history.search.HistoryLogSearchQueryDataBinding;
import sekoya.back.business.referencedata.model.CityBinding;
import sekoya.back.business.referencedata.model.IReferenceDataBindingInterfaceBinding;
import sekoya.back.business.referencedata.search.CitySearchQueryDataBinding;
import sekoya.back.business.referencedata.search.IBasicReferenceDataSearchQueryDataBindingInterfaceBinding;
import sekoya.back.business.role.model.RoleBinding;
import sekoya.back.business.user.model.UserBinding;
import sekoya.back.business.user.search.UserSearchQueryDataBinding;

public final class Bindings {

  private static final EmailAddressBinding EMAIL_ADDRESS = new EmailAddressBinding();
  private static final PhoneNumberBinding PHONE_NUMBER = new PhoneNumberBinding();
  private static final PostalCodeBinding POSTAL_CODE = new PostalCodeBinding();

  private static final IReferenceDataBindingInterfaceBinding REFERENCE_DATA =
      new IReferenceDataBindingInterfaceBinding();
  private static final IBasicReferenceDataSearchQueryDataBindingInterfaceBinding
      BASIC_REFERENCE_DATA_SEARCH_QUERY_DATA =
          new IBasicReferenceDataSearchQueryDataBindingInterfaceBinding();
  private static final CityBinding CITY = new CityBinding();
  private static final CitySearchQueryDataBinding CITY_SEARCH_QUERY_DATA =
      new CitySearchQueryDataBinding();

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

  public static PhoneNumberBinding phoneNumber() {
    return PHONE_NUMBER;
  }

  public static PostalCodeBinding postalCode() {
    return POSTAL_CODE;
  }

  public static IReferenceDataBindingInterfaceBinding referenceData() {
    return REFERENCE_DATA;
  }

  public static IBasicReferenceDataSearchQueryDataBindingInterfaceBinding
      basicReferenceDataSearchQueryData() {
    return BASIC_REFERENCE_DATA_SEARCH_QUERY_DATA;
  }

  public static CityBinding city() {
    return CITY;
  }

  public static CitySearchQueryDataBinding citySearchQueryData() {
    return CITY_SEARCH_QUERY_DATA;
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
