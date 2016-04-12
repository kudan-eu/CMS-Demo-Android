package eu.kudan.cms.demo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CMSUtilityFunctions {

    public static Boolean compareDates(String local, String downloaded) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            Date d1 = format.parse(local);
            Date d2 = format.parse(downloaded);
            if (d1.before(d2)) {
                return true;
            }
        }
        catch (Exception e) {

        }
        return false;
    }


    public static void deleteFileIfexists(CMSFileDownloadInformation tempFdl) {
        File myFile = Environment.getExternalStoragePublicDirectory(getRootFolderDirectory() +"/Assets/"+tempFdl.getFileId()+"/" + tempFdl.getFileTitle());
        if (myFile.exists()) {
            myFile.delete();
        }
    }


    public static Uri getUriFromFileDownloadInformation(CMSFileDownloadInformation tempFdl) {
        File folder = Environment.getExternalStoragePublicDirectory(getRootFolderDirectory() +"/Assets/"+tempFdl.getFileId());
        folder.mkdirs();
        String fileName = tempFdl.getFileTitle();
        Uri temp = Uri.fromFile(new File(folder, fileName));

        return  temp;
    }

    public static String getRootFolderDirectory() {
        return Environment.DIRECTORY_DOWNLOADS+ "/" + MainActivity.packageName;
    }


    // Returns JSON object from local JSON file
    public static JSONObject getLocalJSON() {
        File jsonFile = Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() +  "/data.json");
        if (jsonFile.exists()) {
            try {
                String jsonString = readFile(jsonFile.getPath());
                JSONObject jsonObject = new JSONObject(jsonString);
                return  jsonObject;
            } catch (Exception e) {

            }
        }
        return null;
    }


    public static ArrayList<CMSTrackable> loadTrackablesFromJSONObject(JSONObject jsonObject) {
        ArrayList<CMSTrackable> trackables = new ArrayList<CMSTrackable>();
        if (jsonObject != null) {
            try {
                trackables = new ArrayList<CMSTrackable>();
                JSONArray tempJSONArray = (JSONArray) jsonObject.get("results");

                for (int i = 0; i < tempJSONArray.length(); i++) {
                    CMSTrackable tempTrackable = new CMSTrackable();
                    trackables.add(tempTrackable.initWithJSON(tempJSONArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trackables;
    }


    private static String readFile (String filePath) throws IOException {
        BufferedReader br = new BufferedReader (new FileReader (filePath));
        StringBuilder stringBuilder = new StringBuilder ();
        String line;

        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }
}
