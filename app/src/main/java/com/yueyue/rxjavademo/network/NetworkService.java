package com.yueyue.rxjavademo.network;

import android.util.Log;

import com.yueyue.rxjavademo.network.api.GankApi;
import com.yueyue.rxjavademo.utils.ToastUtil;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author : yueyue on 2018/4/17 22:02
 * desc   :
 */
public class NetworkService {
    private static final String TAG = NetworkService.class.getSimpleName();
    private static Retrofit sRetrofit;
    private static GankApi gankApi;

    static {
        initRetrofit();
    }


    private static void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("http://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        gankApi = sRetrofit.create(GankApi.class);
    }

    /**
     * 其实cancel网络请求的时候，如果还未和服务器建立连接，它会回调到onFailure()方法中，
     * 还有一种情况就是它会在onResponse的时候刚好cancel网络请求，那么它会在onResponse()方法中抛出java.net.SocketException: Socket closed
     */
    private static Consumer<Throwable> disposeFailureInfo(Throwable t) {
        return throwable -> {
            String throwablesStr = t.toString();
            if (throwablesStr.contains("GaiException") ||
                    throwablesStr.contains("SocketTimeoutException") ||
                    throwablesStr.contains("UnknownHostException") ||
                    throwablesStr.contains("SocketException")) {
                ToastUtil.showShort("网络问题");
            }
            Log.e(TAG, t.getMessage());
        };
    }


    public static GankApi getGankApi() {
        if (gankApi == null) {
            synchronized (NetworkService.class) {
                if (gankApi == null) {
                    gankApi = sRetrofit.create(GankApi.class);
                }
            }
        }
        return gankApi;
    }

}