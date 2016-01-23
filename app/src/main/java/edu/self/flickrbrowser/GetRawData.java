package edu.self.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mlinaberry on 1/13/16.
 */

enum DownloadStatuses {
    IDLE,
    PROCESSING,
    NOT_INITALIZED,
    FAILED_OR_EMPTY,
    OK
}

public class GetRawData {


    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mURL;
    private String mData;
    private DownloadStatuses mDownloadStatus;

    public GetRawData(String mURL) {
        this.mURL = mURL;
        this.mDownloadStatus = DownloadStatuses.IDLE; // by default, we're not downloading anything!
    }

    public void reset() {
        this.mDownloadStatus = DownloadStatuses.IDLE;
        this.mURL = null;
        this.mData = null;
    }



    public class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            mData = webData;
            Log.v(LOG_TAG, "Data returned was " + mData);
            if (mData == null) {
                if (mURL == null) {
                    mDownloadStatus = DownloadStatuses.NOT_INITALIZED;
                } else {
                    mDownloadStatus = DownloadStatuses.FAILED_OR_EMPTY;
                }
            }
            else {
                mDownloadStatus = DownloadStatuses.OK;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            if (params == null) {
                return null;
            }

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                if (input == null) {
                    // no data
                    return null;
                }
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(input));
                String aLine;
                while((aLine = reader.readLine()) != null) {
                    buffer.append(aLine + "\n");
                }
                return buffer.toString();

            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error!", e);
                return null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing the stream!", e);
                    }
                }
            }
        }
    }

    public void execute() {
        this.mDownloadStatus = DownloadStatuses.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mURL);
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatuses getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void setmURL(String mURL) {
        this.mURL = mURL;
    }
}
