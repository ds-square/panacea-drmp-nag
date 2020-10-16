package org.panacea.drmp.nag.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.nag.NAGGenerator;
import org.panacea.drmp.nag.controller.APIPostNotifyData;
import org.panacea.drmp.nag.domain.notifications.DataNotification;
import org.panacea.drmp.nag.exception.NAGException;
import org.panacea.drmp.nag.service.OrchestratorNotificationHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class OrchestratorNotificationHandlerServiceImpl implements OrchestratorNotificationHandlerService {

    public static final String INVALID_NOTIFICATION_ERR_MSG = "Invalid Data Notification Body.";

    @Autowired
	NAGGenerator nagGenerator;

    @Override
    public APIPostNotifyData.DataNotificationResponse perform(DataNotification notification) throws NAGException {
        log.info("[NAG] Received Data Notification from Orchestrator: {}", notification);
        try {
//            if(notification.getEnvironment() == null){
//                throw new NAGException("No environment defined for notification.");
//            }
            nagGenerator.generateNAG(notification);


            return new APIPostNotifyData.DataNotificationResponse(notification.getEnvironment(), notification.getSnapshotId(), notification.getSnapshotTime());
        } catch (NAGException e) {
            log.info("NAGException occurred: ", e);
            throw new NAGException(INVALID_NOTIFICATION_ERR_MSG, e);
        }

    }




}
