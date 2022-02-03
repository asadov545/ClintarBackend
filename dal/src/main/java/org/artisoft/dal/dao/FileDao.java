package org.artisoft.dal.dao;

import org.artisoft.domain.UploadedFiles;

import java.util.List;

public interface FileDao extends  BaseDao<UploadedFiles> {

    List<Long> insertAll(List<UploadedFiles> uploadedFiles);
}
