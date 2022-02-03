package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.Customer;
import org.artisoft.domain.ValueLabel;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface CustomerDao extends BaseDao<Customer> {
    long insertNew(Customer customer) throws AddressException, MessagingException, IOException;
    boolean updateNew(Customer customer) throws AddressException, MessagingException, IOException;
    List<Customer> filterDataByModId(HashMap<String, String> map, Long modId);
    List<ValueLabel<Long, String>> filterCustomerList( Long modId);
    List<ValueLabel<Long, String>> filterCustomerListByLabel(String label,Long modId);
}

