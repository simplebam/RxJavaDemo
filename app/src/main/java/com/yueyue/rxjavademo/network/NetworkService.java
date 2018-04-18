package com.yueyue.rxjavademo.network;

import android.util.Log;

import com.yueyue.rxjavademo.network.api.FakeApi;
import com.yueyue.rxjavademo.network.api.GankApi;
import com.yueyue.rxjavademo.network.api.HuaBanApi;
import com.yueyue.rxjavademo.utils.ToastUtil;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author : yueyue on 2018/4/17 22:02
 * desc   :
 */
public class NetworkService {
    private static final String TAG = NetworkService.class.getSimpleName();

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    private static GankApi gankApi;
    private static HuaBanApi huaBanApi;
    private static FakeApi fakeApi;

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
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl(GankApi.HOST)
                            .addConverterFactory(gsonConverterFactory)
                            .addCallAdapterFactory(rxJavaCallAdapterFactory)
                            .build();
                    gankApi = retrofit.create(GankApi.class);
                }
            }
        }
        return gankApi;
    }


    public static HuaBanApi getHuaBanApi() {
        if (huaBanApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(HuaBanApi.HOST)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            huaBanApi = retrofit.create(HuaBanApi.class);
        }
        return huaBanApi;
    }

    public static FakeApi getFakeApi() {
        if (fakeApi == null) {
            synchronized (NetworkService.class) {
                if (fakeApi == null) {
                    fakeApi = new FakeApi();
                }
            }
        }
        return fakeApi;
    }

}
