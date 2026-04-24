package sekoya.back.business.referencedata.search;

import sekoya.back.business.referencedata.model.ReferenceData;

public class BasicReferenceDataSearchQueryData<T extends ReferenceData<? super T>>
    extends AbstractReferenceDataSearchQueryData<T>
    implements IBasicReferenceDataSearchQueryDataBindingInterface {}
