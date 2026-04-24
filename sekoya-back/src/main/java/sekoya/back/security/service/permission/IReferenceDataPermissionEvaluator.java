package sekoya.back.security.service.permission;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;
import sekoya.back.business.referencedata.model.ReferenceData;
import sekoya.back.business.user.model.User;

public interface IReferenceDataPermissionEvaluator
    extends IGenericPermissionEvaluator<User, ReferenceData<?>> {}
