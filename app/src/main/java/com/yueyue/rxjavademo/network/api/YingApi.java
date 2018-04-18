package com.yueyue.rxjavademo.network.api;

import com.yueyue.rxjavademo.model.YingPicResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author : yueyue on 2018/4/17 22:04
 * desc   :
 */

public interface YingApi {

    String HOST = "http://cn.bing.com/";


    //http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1
    @GET("HPImageArchive.aspx")
    Observable<YingPicResult> getimages(@Query("format") String format,
                                          @Query("idx") int idx,
                                          @Query("n") int num);
}
