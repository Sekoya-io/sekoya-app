package test.core.service;

import org.springframework.stereotype.Service;
import sekoya.back.business.notification.service.EmptyNotificationUrlBuilderServiceImpl;

/** Implémentation bouche-trou, uniquement pour combler la dépendance. */
@Service("coreTestNotificationUrlBuilderService")
public class CoreTestNotificationUrlBuilderServiceImpl
    extends EmptyNotificationUrlBuilderServiceImpl {}
