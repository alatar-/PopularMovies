package eu.alatar.popularmovies.rest;

/**
 * Created by kam on 10/02/17.
 */

public final class RestService {
    private static RestService sInstance = null;
    public APIInterface mAPIInterface = null;

    public static RestService getAPIService() {
        if (sInstance == null) {
            sInstance = new RestService();
        }
        return sInstance;
    }

    public RestService() {
        mAPIInterface = RestBuilder.createAPInterfaceInstance();
    }

}
