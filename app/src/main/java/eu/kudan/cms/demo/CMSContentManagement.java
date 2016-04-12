package eu.kudan.cms.demo;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CMSContentManagement implements CMSDownloadManagerInterface, JSONParserInterface {

    private static final boolean useOwnServer = false;

    private JSONObject downloadedJSON;
    private JSONObject localJSON;
    ArrayList<CMSFileDownloadInformation> fileDownloadInformation;
    ArrayList<CMSTrackable> trackables;
    private CMSContentManagementInterface contentManagementInferace;
    private Context context;
    private CMSDownloadManager downloadsManager;
    private JSONParser jsonParser;


    // Sets up listeners
    public CMSContentManagement(CMSContentManagementInterface inListener, Context inContext) {
        this.contentManagementInferace = inListener;
        this.context = inContext;
        downloadsManager = new CMSDownloadManager (this);
        jsonParser = new JSONParser (this);
    }


    // Downloads JSON file
    public void downloadJSON() {
        String[] url = new String[1];
        if (useOwnServer) {
            url[0] = "https://YOURSERVER/CMS/JSON/test.json";
        }
        else {
            url[0] = "https://api.kudan.eu/CMS/JSON/test.json";
        }
        jsonParser.execute(url);
    }


    // Creates file download information from downloaded json, if the file has been updated
    private void addFileDownloadInformationFromJSON() {
        try {
            fileDownloadInformation = new ArrayList<CMSFileDownloadInformation>();
            if (downloadedJSON != null) {
                if (localJSON == null) {
                    JSONArray tempJSONArray = (JSONArray) downloadedJSON.get("results");
                    for (int i = 0; i < tempJSONArray.length(); i++) {
                        JSONObject tempJSON = (JSONObject) tempJSONArray.get(i);
                        addFileDownloadInformation(tempJSON);

                        }
                    }
                    else {
                        if (CMSUtilityFunctions.compareDates(localJSON.getString("lastUpdated"), downloadedJSON.getString("lastUpdated"))) {
                            JSONArray tempJSONArray = (JSONArray) downloadedJSON.get("results");

                            for (int i = 0; i < tempJSONArray.length(); i++) {
                                JSONObject tempJSON = (JSONObject) tempJSONArray.get(i);

                                if (CMSUtilityFunctions.compareDates(localJSON.getString("lastUpdated"), tempJSON.getString("lastUpdated"))) {
                                    addFileDownloadInformation(tempJSON);
                                }
                            }
                        }
                        else {
                            JSONArray tempJSONArray = (JSONArray) downloadedJSON.get("results");
                            for (int i = 0; i < tempJSONArray.length(); i++) {
                                JSONObject tempJSON = (JSONObject) tempJSONArray.get(i);

                                if (filesMissingFromDirectory(tempJSON)) {
                                    addFileDownloadInformation(tempJSON);
                                }
                            }
                        }
                    }
                }

            }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addFileDownloadInformation (JSONObject tempJSON) {
        try {
            CMSFileDownloadInformation augmentationFileDownloadinInformation = new CMSFileDownloadInformation();
            CMSFileDownloadInformation markerFileDownloadinInformation = new CMSFileDownloadInformation();
            if (tempJSON.get("augmentationType").equals("video")) {
                fileDownloadInformation.add(augmentationFileDownloadinInformation.initAugmentationWithJSON(tempJSON));
            }
            fileDownloadInformation.add(markerFileDownloadinInformation.initMarkerWithJSON(tempJSON));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Saves downloaded JSON to local file
    private void saveLocalJSON() {
        try {
            File checkFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + MainActivity.packageName + "/data.json");
            if (checkFile.exists()) {
                checkFile.delete();
            }
            checkFile.createNewFile();
            FileWriter file = new FileWriter (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS +  "/" + MainActivity.packageName + "/data.json"));
            file.write(downloadedJSON.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean filesMissingFromDirectory(JSONObject tempJSON) {
        try {
            File markerFile = Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/"+tempJSON.getString("id")+"/" + tempJSON.getString("markerFileName"));
            File completedFile =Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/"+tempJSON.getString("id")+"/" + "completed.txt");

            if (!markerFile.exists()) {
                return true;
            }
            if (!completedFile.exists()) {
                return true;
            }
            if (tempJSON.getString("augmentationType").equals("video")) {
                File videoFile = Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() + "/Assets/"+tempJSON.getString("id")+"/" + tempJSON.getString("augmentationFileName"));
                if(!videoFile.exists()) {
                    return true;
                }
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public void finishedDownload() {
        if (fileDownloadInformation.size() > 0 ) {
            saveLocalJSON();
        }
        localJSON = CMSUtilityFunctions.getLocalJSON();
        trackables = CMSUtilityFunctions.loadTrackablesFromJSONObject(localJSON);
        CMSTrackable[] trackablesArray = new CMSTrackable[trackables.size()];
        trackables.toArray(trackablesArray);
        contentManagementInferace.setUpTrackers(trackablesArray);
    }


    @Override
    public void couldNotDownloadFile() {
        contentManagementInferace.cannotDownload();
    }


    @Override
    public void jsonFinishedDownloading(JSONObject jsonObject) {
        downloadedJSON= new JSONObject();
        downloadedJSON = jsonObject;
        localJSON = CMSUtilityFunctions.getLocalJSON();
        addFileDownloadInformationFromJSON();

        if (fileDownloadInformation.size() > 0 ) {
            downloadsManager.downloadTrackables(fileDownloadInformation, context, downloadedJSON);
        }
        else {
            finishedDownload();
        }
    }


    @Override
    public void couldNotDownloadJSON() {
        contentManagementInferace.cannotDownload();
    }
}
