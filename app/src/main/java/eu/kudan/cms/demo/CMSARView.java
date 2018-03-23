package eu.kudan.cms.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.logging.Handler;

import eu.kudan.kudan.*;

public class CMSARView extends ARActivity implements ARImageTrackableListener {

    private CMSTrackable[] trackables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
    }

    @Override
    public void setup() {
        super.setup();
        Intent in = getIntent();
        Parcelable[] parcelables = in.getExtras().getParcelableArray("trackables");
        trackables = new CMSTrackable[parcelables.length];

        for (int i = 0; i < parcelables.length; i++) {
            trackables[i] = (CMSTrackable)parcelables[i];
        }

        setupTrackables();
    }


    // Adds Trackables to ARImageTracker
    private void setupTrackables() {
        ARImageTracker tracker = ARImageTracker.getInstance();
        tracker.initialise();
        for (CMSTrackable tempTrackable : trackables) {

            ARTrackableSet trackableSet = new ARTrackableSet();
            trackableSet.loadFromPath(tempTrackable.getMarkerFilePath());
            tracker.addTrackableSet(trackableSet);

            if (tempTrackable.getAugmentationType().equals("video")) {
                addVideo(trackableSet,tempTrackable);
            }
            else {
                addText(trackableSet, tempTrackable);
            }
        }
    }

    // Adds video nodes to trackable set
    private void addVideo(ARTrackableSet trackableSet, CMSTrackable tempTrackable) {
        for (ARImageTrackable imageTrackable : trackableSet.getTrackables()) {

            ARVideoTexture videoTexture = new ARVideoTexture();
            videoTexture.loadFromPath(tempTrackable.getAugmentationFilePath());
            ARVideoNode videoNode = new ARVideoNode(videoTexture);
            videoNode.rotateByDegrees(tempTrackable.getAugmentationRoatation(),0,0,1);
            float scale;

            if (tempTrackable.getAugmentationRoatation() == 90) {
                scale = imageTrackable.getWidth() / videoNode.getVideoTexture().getHeight();
            }
            else {
                scale = imageTrackable.getWidth() / videoNode.getVideoTexture().getWidth();
            }

            videoNode.scaleByUniform(scale);
            imageTrackable.getWorld().addChild(videoNode);
            imageTrackable.addListener(this);
        }
    }


    private void addText(ARTrackableSet trackableSet, CMSTrackable tempTrackable) {
        for (ARImageTrackable imageTrackable : trackableSet.getTrackables()) {
            imageTrackable.getWorld().setName("text");
            imageTrackable.addListener(this);
        }
    }


    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        if (arImageTrackable.getWorld().getName().equals("text")) {

            final ARImageTrackable ar = arImageTrackable;
            this.runOnUiThread(new Runnable() {
                public void run() {
                    TextView tv = (TextView) findViewById(R.id.albumTitle);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(ar.getName());
                }
            });
        }
    }


    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {

    }


    @Override
    public void didLose(ARImageTrackable arImageTrackable) {
        final ARImageTrackable ar = arImageTrackable;
        this.runOnUiThread(new Runnable() {
            public void run() {
                TextView tv = (TextView) findViewById(R.id.albumTitle);
                tv.setVisibility(View.GONE);
                tv.setText(ar.getName());
            }
        });
    }
}
