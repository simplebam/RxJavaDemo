package com.yueyue.rxjavademo.network.api;

import com.yueyue.rxjavademo.model.GankBeautyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * author : yueyue on 2018/4/17 22:04
 * desc   :
 */

public interface GankApi {
    //http://gank.io/api/data/福利/50/1
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
