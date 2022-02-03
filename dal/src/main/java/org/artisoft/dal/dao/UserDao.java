package org.artisoft.dal.dao;

import org.artisoft.domain.ChangePassword;
import org.artisoft.domain.ModToolbox.UserCompdata;
import org.artisoft.domain.Notification.JobsCompdata;
import org.artisoft.domain.Users;
import org.artisoft.domain.ValueLabel;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface UserDao extends BaseDao<Users>{
    List<ValueLabel<Long, String>> filterUserList(String label, Long modId);
    void sendMailNewUsers(Users users)throws AddressException, MessagingException, IOException;
    void sendMailTaskAssignees(Long userId)throws AddressException, MessagingException, IOException;
    long insertNew(Users users) throws AddressException, MessagingException, IOException;
    boolean updateNew(Users users) throws AddressException, MessagingException, IOException;
    String createNewUserNotificationContext(Users users);
    void addNewUserToNotificationQueue(Users users,int sendtype,int notifType);
    List<Users> filterDataByModId(HashMap<String, String> map, Long modId);
    List<Users> getUserListByCustomerId(long customerId);
    Boolean changePasword(ChangePassword changePassword);
    UserCompdata getUsersCompData();
}
