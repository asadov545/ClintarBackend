package org.artisoft.domain.Report;

public class DownloadRequest {
    public enum ExportType {
        PDF, Word, Html, Excel
    }

    public enum DownloadModule {
        TOOLBOX_PROJECT
    }

    private DownloadModule downloadModule;
    private ExportType exportType;

    public DownloadModule getDownloadModule() {
        return downloadModule;
    }

    public void setDownloadModule(DownloadModule downloadModule) {
        this.downloadModule = downloadModule;
    }

    public ExportType getExportType() {
        return exportType;
    }

    public void setExportType(ExportType exportType) {
        this.exportType = exportType;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "downloadModule=" + downloadModule +
                ", exportType=" + exportType +
                '}';
    }
}
