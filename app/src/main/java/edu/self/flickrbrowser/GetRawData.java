package edu.self.flickrbrowser;

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
    }
}
