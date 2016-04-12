package eu.kudan.cms.demo;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CMSTrackable implements Parcelable{
    private int tId;
    private String markerFileURL;
    private String augmentationFileURL;
    private String markerFilePath;
    private String augmentationFilePath;
    private String markerFileName;
    private String augmentationFileName;
    private String lastUpdated;
    private int augmentationRoatation;
    private int displayFade;
    private int resetTime;
    private int fillMarker;
    private Boolean augmentationComplete;
    private Boolean markerComplete;
    private String augmentationType;



    public CMSTrackable initWithJSON(JSONObject jsonObject) {

        try {
            tId = (int) jsonObject.get("id");
            markerFileURL = (String) jsonObject.get("marker");
            markerFileName = (String) jsonObject.get("markerFileName");
            augmentationFileURL = (String) jsonObject.get("augmentation");
            augmentationFileName = (String) jsonObject.get("augmentationFileName");
            lastUpdated = (String) jsonObject.get("lastUpdated");
            augmentationRoatation = (int)jsonObject.get("augmentationRotation");
            fillMarker = (int) jsonObject.get("fillMarker");
            markerFilePath = String.valueOf (Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory() +  "/Assets/" + tId +"/" + markerFileName));
            augmentationFilePath =String.valueOf (Environment.getExternalStoragePublicDirectory(CMSUtilityFunctions.getRootFolderDirectory()+ "/Assets/" + tId +"/" + augmentationFileName));
            displayFade = (int) jsonObject.get("displayFade");
            resetTime = (int) jsonObject.get("resetTime");
            augmentationType = (String) jsonObject.get("augmentationType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        augmentationComplete = false;
        markerComplete = false;

        return this;
    }

    public int getId() {
        return tId;
    }

    public void setId(int tId) {
        this.tId = tId;
    }

    public String getMarkerFileURL() {
        return markerFileURL;
    }

    public void setMarkerFileURL(String markerFileURL) {
        this.markerFileURL = markerFileURL;
    }

    public String getMarkerFilePath() {
        return markerFilePath;
    }

    public void setMarkerFilePath(String markerFilePath) {
        this.markerFilePath = markerFilePath;
    }

    public String getAugmentationFilePath() {
        return augmentationFilePath;
    }

    public void setAugmentationFilePath(String augmentationFilePath) {
        this.augmentationFilePath = augmentationFilePath;
    }

    public String getMarkerFileName() {
        return markerFileName;
    }

    public void setMarkerFileName(String markerFileName) {
        this.markerFileName = markerFileName;
    }

    public String getAugmentationFileName() {
        return augmentationFileName;
    }

    public void setAugmentationFileName(String augmentationFileName) {
        this.augmentationFileName = augmentationFileName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getAugmentationRoatation() {
        return augmentationRoatation;
    }

    public void setAugmentationRoatation(Integer augmentationRoatation) {
        this.augmentationRoatation = augmentationRoatation;
    }

    public int getDisplayFade() {
        return displayFade;
    }

    public void setDisplayFade(int displayFade) {
        this.displayFade = displayFade;
    }

    public int getResetTime() {
        return resetTime;
    }

    public void setResetTime(int resetTime) {
        this.resetTime = resetTime;
    }

    public Boolean getAugmentationComplete() {
        return augmentationComplete;
    }

    public void setAugmentationComplete(Boolean augmentationComplete) {
        this.augmentationComplete = augmentationComplete;
    }

    public Boolean getMarkerComplete() {
        return markerComplete;
    }

    public void setMarkerComplete(Boolean markerComplete) {
        this.markerComplete = markerComplete;
    }

    public int getFillMarker () {
        return fillMarker;
    }

    public void setFillMarker (Integer fillMarker) {
        this.fillMarker = fillMarker;
    }


    public String getAugmentationFileURL() {
        return augmentationFileURL;
    }

    public void setAugmentationFileURL(String augmentationFileURL) {
        this.augmentationFileURL = augmentationFileURL;
    }

    public String getAugmentationType() {
        return augmentationType;
    }

    public void setAugmentationType(String augmentationType) {
        this.augmentationType = augmentationType;
    }


    public CMSTrackable() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tId);
        dest.writeString(this.markerFileURL);
        dest.writeString(this.augmentationFileURL);
        dest.writeString(this.markerFilePath);
        dest.writeString(this.augmentationFilePath);
        dest.writeString(this.markerFileName);
        dest.writeString(this.augmentationFileName);
        dest.writeString(this.lastUpdated);
        dest.writeValue(this.augmentationRoatation);
        dest.writeValue(this.displayFade);
        dest.writeValue(this.resetTime);
        dest.writeValue(this.fillMarker);
        dest.writeValue(this.augmentationComplete);
        dest.writeValue(this.markerComplete);
        dest.writeString(this.augmentationType);
    }

    private CMSTrackable(Parcel in) {
        this.tId = in.readInt();
        this.markerFileURL = in.readString();
        this.augmentationFileURL = in.readString();
        this.markerFilePath = in.readString();
        this.augmentationFilePath = in.readString();
        this.markerFileName = in.readString();
        this.augmentationFileName = in.readString();
        this.lastUpdated = in.readString();
        this.augmentationRoatation = (Integer) in.readValue(Integer.class.getClassLoader());
        this.displayFade = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resetTime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fillMarker = (Integer) in.readValue(Integer.class.getClassLoader());
        this.augmentationComplete = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.markerComplete = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.augmentationType = in.readString();
    }

    public static final Creator<CMSTrackable> CREATOR = new Creator<CMSTrackable>() {
        public CMSTrackable createFromParcel(Parcel source) {
            return new CMSTrackable(source);
        }

        public CMSTrackable[] newArray(int size) {
            return new CMSTrackable[size];
        }
    };
}
