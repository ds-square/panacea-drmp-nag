package org.panacea.drmp.nag.service;

import org.panacea.drmp.nag.controller.APIPostNotifyData.DataNotificationResponse;
import org.panacea.drmp.nag.exception.NAGException;
import org.panacea.drmp.nag.domain.notifications.DataNotification;

public interface OrchestratorNotificationHandlerService {

    DataNotificationResponse perform(DataNotification dataNotification) throws NAGException;

}
