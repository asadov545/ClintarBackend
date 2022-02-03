package org.artisoft.dal.dao.notification;

import org.artisoft.dal.dao.BaseDao;

import org.artisoft.domain.Notification.MobileNotifcationList;
import org.artisoft.domain.Notification.SendNotificationByMailList;
import org.artisoft.domain.Notification.NotificationQueue;

import java.util.List;

public interface NotificationQueueDao extends BaseDao<NotificationQueue> {
    List<SendNotificationByMailList> getQueueForMailList();
    void NotifQueStatusUpdate(long notifQueId,int sendStatus);
    List<MobileNotifcationList> getQueueForMobileList(Long user_id);
    List<MobileNotifcationList> getAllActiveMobileList();
    boolean updateDeviceId(long userId,String deviceId);
}
