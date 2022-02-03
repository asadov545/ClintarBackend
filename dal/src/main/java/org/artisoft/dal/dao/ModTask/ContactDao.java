package org.artisoft.dal.dao.ModTask;

import org.artisoft.dal.dao.BaseDao;
import org.artisoft.domain.ModTask.Contact;

import java.util.List;

public interface ContactDao extends BaseDao<Contact> {
    List<Contact> getAllContactByUserId(long id);
    List<Contact> getAllContactByBranchesId(long id);
    String getContactByBranchesId(long branchesId,long type);
    String getContactByUserId(long branchesId,long type);
}
