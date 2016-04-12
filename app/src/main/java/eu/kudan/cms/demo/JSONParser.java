package eu.kudan.cms.demo;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;


public class JSONParser extends AsyncTask<String,Void,JSONObject>  {
    private  JSONParserInterface jsonParserInterface;
    private Boolean completedWithoutErrors;

    public JSONParser (JSONParserInterface inJsonParserInterface) {
        this.jsonParserInterface = inJsonParserInterface;
        completedWithoutErrors = true;

    }

    // Creates JSON object from url
    protected JSONObject downloadJson(String url)
    {
        JSONObject jsonObject = new JSONObject();
        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String line;
        String jsonString;

        try {
            URL u = new URL(url);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setRequestMethod("GET");
            InputStreamReader isr = new InputStreamReader (httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader (new InputStreamReader (httpURLConnection.getInputStream()));
            stringBuilder = new StringBuilder ();

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            jsonString = stringBuilder.toString();

            try {
                jsonObject = new JSONObject(jsonString);
            }
            catch (JSONException e)
            {
                completedWithoutErrors =false;
                e.printStackTrace();
            }
            finally {
                httpURLConnection.disconnect();
            }

        }
        catch (MalformedURLException e)
        {
            completedWithoutErrors =false;
            e.printStackTrace();
        }
        catch (ProtocolException e)
        {
            completedWithoutErrors =false;
            e.printStackTrace();
        }
        catch (IOException e)
        {
            completedWithoutErrors =false;
            e.printStackTrace();
        }

        return jsonObject;
    }


    @Override
    protected JSONObject doInBackground(String... params) {
        String url = params[0];
        return downloadJson(url);
    }


    @Override
    protected void onPostExecute(JSONObject result) {
        if(completedWithoutErrors == false) {
            jsonParserInterface.couldNotDownloadJSON();
        }
        else {
            jsonParserInterface.jsonFinishedDownloading(result);
        }
    }
}
