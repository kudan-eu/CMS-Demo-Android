package eu.kudan.cms.demo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CMSDownloadManager {
    ArrayList<CMSFileDownloadInformation> fileDownloadInformation;
    ArrayList<CMSTrackable> trackables;
    private DownloadManager downloadsManager;
    private CMSDownloadManagerInterface downloadManagerInterface;
    private JSONObject downloadedJSON;


    public CMSDownloadManager (CMSDownloadManagerInterface inListener) {
        this.downloadManagerInterface = inListener;
    }


    // Uses file download information to download a list of Trackables
    public void downloadTrackables(ArrayList<CMSFileDownloadInformation> fdl, Context context,JSONObject inDownloadedJSON) {
        this.fileDownloadInformation = fdl;
        this.downloadedJSON = inDownloadedJSON;

        trackables = new ArrayList<CMSTrackable>();
        trackables = CMSUtilityFunctions.loadTrackablesFromJSONObject(downloadedJSON);

        for (int i = 0; i < fdl.size(); i++) {
            CMSFileDownloadInformation tempFdl = fdl.get(i);
            downloadsManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(tempFdl.getDownloadSource());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            setupRequest(request,tempFdl);
        }

        IntentFilter intentComplete = new IntentFilter (DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(onComplete, intentComplete);
    }


    private void setupRequest(DownloadManager.Request request, CMSFileDownloadInformation tempFdl ) {
        CMSUtilityFunctions.deleteFileIfexists(tempFdl);
        request.setTitle(tempFdl.getFileTitle());
        request.setDestinationUri(CMSUtilityFunctions.getUriFromFileDownloadInformation(tempFdl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        tempFdl.setTaskIdentifier(downloadsManager.enqueue(request));
        tempFdl.setIsDownloading(true);
    }


    private void markFileCompleted(long downloadReference) {
        for (int i = 0; i < fileDownloadInformation.size(); i++) {
            if (fileDownloadInformation.get(i).getTaskIdentifier () == downloadReference) {
                fileDownloadInformation.get(i).setIsDownloading(false);
                fileDownloadInformation.get(i).setDownloadIscomplete(true);
                markTrackableCompleted(fileDownloadInformation.get(i));
            }
        }
    }


    // Marks trackables as completed
    private  void markTrackableCompleted(CMSFileDownloadInformation fileDownloadInformation) {
        for (int i = 0; i < trackables.size(); i++) {
            if (fileDownloadInformation.getFileId() == trackables.get(i).getId()) {
                if (fileDownloadInformation.getFileTitle().equals(trackables.get(i).getAugmentationFileName())) {
                    trackables.get(i).setAugmentationComplete(true);
                }
                else {
                    trackables.get(i).setMarkerComplete(true);
                    if(trackables.get(i).getAugmentationType().equals("text")){
                        trackables.get(i).setAugmentationComplete(true);
                    }
                }
                if (trackables.get(i).getAugmentationComplete()&& trackables.get(i).getMarkerComplete()) {
                    writeCompletedTextFile(trackables.get(i));
                }
            }
        }
    }

    // Writes a completed text file to the trackables folder if it has finished downloading
    private void writeCompletedTextFile(CMSTrackable trackable) {
        try {
            File checkFile = Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() +  "/Assets/" +  trackable.getId()+"/completed.txt");
            if (checkFile.exists()) {
                checkFile.delete();
            }
            checkFile.createNewFile();
            FileWriter file = new FileWriter (Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/" +  trackable.getId()+"/completed.txt"));
            file.write("Completed");
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Boolean checkDownloadsFinished() {
        int numberCompleted = 0;
        for (int i = 0; i < fileDownloadInformation.size(); i++) {
            if (fileDownloadInformation.get(i).getDownloadIscomplete()) {
                numberCompleted++;
            }
        }

        if (numberCompleted == fileDownloadInformation.size()) {
            return true;
        }

        return false;
    }


    // Receives broadcast if a download has completed
    BroadcastReceiver onComplete=new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadReference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            markFileCompleted(downloadReference);

            if (checkDownloadsFinished()) {
                context.unregisterReceiver (onComplete);
                downloadManagerInterface.finishedDownload();
            }
        }
    };
}
