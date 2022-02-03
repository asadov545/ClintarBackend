package org.artisoft.domain.ModToolbox.report;

import org.artisoft.domain.Report.DownloadRequest;

public class DownloadRequestProject extends DownloadRequest {
    private long projectId;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}
