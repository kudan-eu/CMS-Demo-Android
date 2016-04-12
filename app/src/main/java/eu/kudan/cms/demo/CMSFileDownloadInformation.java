package eu.kudan.cms.demo;

import org.json.JSONException;
import org.json.JSONObject;

public class CMSFileDownloadInformation
{
    private String fileTitle;
    private int fileId;
    private String downloadSource;
    private double downloadProgress;
    private Boolean isDownloading;
    private Boolean downloadIscomplete;
    private long taskIdentifier;

    public CMSFileDownloadInformation initMarkerWithJSON(JSONObject jsonObject)
    {
        try
        {
            fileId = (int) jsonObject.get("id");
            downloadSource = (String) jsonObject.get("marker");
            fileTitle = (String) jsonObject.get("markerFileName");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        downloadProgress = 0.0;
        isDownloading = false;
        downloadIscomplete = false;
        taskIdentifier = -1;

        return this;
    }

    public CMSFileDownloadInformation initAugmentationWithJSON(JSONObject jsonObject) {
        try {
            fileId = (int) jsonObject.get("id");
            downloadSource = (String) jsonObject.get("augmentation");
            fileTitle = (String) jsonObject.get("augmentationFileName");

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        downloadProgress = 0.0;
        isDownloading = false;
        downloadIscomplete = false;
        taskIdentifier = -1;

        return this;
    }


    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getDownloadSource() {
        return downloadSource;
    }

    public void setDownloadSource(String downloadSource) {
        this.downloadSource = downloadSource;
    }

    public double getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(double downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public Boolean getIsDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(Boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public Boolean getDownloadIscomplete() {
        return downloadIscomplete;
    }

    public void setDownloadIscomplete(Boolean downloadIscomplete) {
        this.downloadIscomplete = downloadIscomplete;
    }

    public long getTaskIdentifier () {
        return taskIdentifier;
    }

    public void setTaskIdentifier (long taskIdentifier) {
        this.taskIdentifier = taskIdentifier;
    }
}
