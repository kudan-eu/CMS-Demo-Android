package eu.kudan.cms.demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import eu.kudan.kudan.ARAPIKey;

public class MainActivity extends Activity implements CMSContentManagementInterface {
public static String packageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARAPIKey key = ARAPIKey.getInstance();
        key.setAPIKey("sVmoznmKZ+4nFEHD6HoslwpC26PNuBZGHrikUwyon2BKSvza1yu2CqbSrae+pHPr1NHjhsf5pHQOZn8IEqXlqXFodGsrOJhxJANbMOdvnRLUi9/QWGqyRL9FViDmyohw6e5R7U4Ex8H7d7spLLvhfp5HFv56DgLr8c8sC2ipDtv9g1IjOTaY7UGxata3eulG2A/UkIdRv2NcotZXqan01xQUWFAislEwlGguParEYiwu11T4mqtU3dQBbfxpvxbczjdYz493YG3rAO2RHgT+5M5TJShJsz2irkNo71JD2Fzqf4AR2b4+7t1c55zKjegXzGS6Xa/rpNn9yiXUn7rUYIHNvN3cEQa9HsZiVxAV4vJgxFS+T/AxfWqKrEg1uj6xF5MsodZ2EkZ8mqliYIsxZqnFz+Re2HeWG8wvrEob0ZwRIO0TxppAemZc3HChTAPLcNt5gzeBk0oRP4wnrFAFFBDi8XjDocwTSVw++hWZb1qNHzt6bKLsMDRT057UVuuZB6M8f7EOQD79Oah0Vrx/3DUK6e9BEV8oGFNHtk1wyYEkg0i6RLhVSokGx//Qj36A4gCz3h1OjtfB0OuukbNq7xI1L/FcNQLmGYNGZwszARjGr9ESw1gVAkbQMxaV27uo/KoIq4+nR7RL8iT7t7NAaXCFIi24RR+7WGjTvKqWYjA=");
        packageName = getApplicationContext().getPackageName();
        setContentView(R.layout.activity_main);
        permissionsRequest();
    }


    // Requests app permissions
    public void permissionsRequest() {

        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},111);

        }
        else {
            CMSContentManagement newContent = new CMSContentManagement(this,this);
            newContent.downloadJSON();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
     String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {
                    CMSContentManagement newContent = new CMSContentManagement(this,this);
                    newContent.downloadJSON();
                } else {
                    permissionsNotSelected();
                }
            }
        }
    }


    @Override
    public void setUpTrackers(CMSTrackable[] trackers) {
        Intent intent = new Intent(MainActivity.this, CMSARView.class);
        intent.putExtra("trackables", trackers);
        startActivity(intent);
    }


    @Override
    public void cannotDownload() {
        displayCantDownload();
    }


    private void permissionsNotSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle("Permissions requred");
        builder.setMessage("Please enable request permissions in the app settings to use the application");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener () {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                System.exit(1);
            }
        });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }


    private void displayCantDownload() {
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle("No internet connection");
        builder.setMessage("Please connect to the internet to download trackables");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener () {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog noInternet = builder.create();
        noInternet.show();
    }
}
