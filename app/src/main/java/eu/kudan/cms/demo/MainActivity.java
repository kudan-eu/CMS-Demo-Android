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
        key.setAPIKey("GAXAE-F9AU4-58C4L-35HMV-EY6C9-GD6XG-2RJKX-BACQZ-A9KQB-XYYXC-7LCB2-8UUN2-FEXW5-W6CVL-27QYS-QU");
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
