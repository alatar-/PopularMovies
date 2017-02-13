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

//    public List<Movie> queryPopularMovies() {
//        Call<MovieSet> call = mAPIInterface.getPopularMovies();
//        call.enqueue(new Callback<MovieSet>() {
//            @Override
//            public void onResponse(Call<MovieSet> call, Response<MovieSet> response) {
//                int statusCode = response.code();
//                if (statusCode != 200) {
//                    Log.e(Preferences.TAG, "Failure on downloading movies data: status code " + String.valueOf(statusCode));
//                } else {
//                    MovieSet moviesSet = response.body();
//                    if (moviesSet != null) {
//                        mMovieListAdapter.addMovies(moviesSet.getResults());
//                    } else {
//                        Log.e(Preferences.TAG, "Body is empty!");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSet> call, Throwable t) {
//                Log.e(Preferences.TAG, "API request failed");
//                Log.e(Preferences.TAG, t.getMessage());
//                return null;
//            }
//        });
//    }
}
