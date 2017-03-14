package eu.alatar.popularmovies.rest;

import android.util.LruCache;

import eu.alatar.popularmovies.preferences.Preferences;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kam on 10/02/17.
 */

public final class RestService {
    private static RestService sInstance = null;
//    private LruCache<Class<?>, Observable<?>> apiObservables = new LruCache<>(1);

    public APIInterface api = null;

    public static RestService getAPIService() {
        if (sInstance == null) {
            sInstance = new RestService();
        }
        return sInstance;
    }

    public RestService() {
        api = RestBuilder.createAPInterfaceInstance();
    }

//    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable, Class<?> clazz){
//        // https://www.captechconsulting.com/blogs/a-mvp-approach-to-lifecycle-safe-requests-with-retrofit-20-and-rxjava
//        Observable<?> preparedObservable = this.apiObservables.get(clazz);
//
//        if (preparedObservable == null) { // not cached already
//            // run new request
//            preparedObservable = unPreparedObservable
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread());
//
//            // cache request
//            preparedObservable = preparedObservable.cache();
//            apiObservables.put(clazz, preparedObservable);
//        }
//
//        return preparedObservable;
//    }

}
