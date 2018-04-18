package com.yueyue.rxjavademo.network.api;

import com.yueyue.rxjavademo.model.HuaBanBeautyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author : yueyue on 2018/4/18 10:07
 * desc   :
 */
public interface HuaBanApi {

    String HOST = "https://route.showapi.com/";


    // type(可以不传) 默认调用最新 大胸妹=34 小清新=35  文艺范=36  性感妹=37  大长腿=38  黑丝袜=39 小翘臀=40
    // num(可以不传)  默认20,最大50
    // page(可以不传) 默认查询第1页
    // https://route.showapi.com/819-1?num=10&page=1&showapi_appid=62253&type=34&showapi_sign=f1aaabb545134c46a2fd459979cc47c2
    @GET("819-1")
    Observable<HuaBanBeautyResult> getBeauties(@Query("num") int num,
                                               @Query("page") int page,
                                               @Query("type") int type,
                                               @Query("showapi_appid") int appId,
                                               @Query("showapi_sign") String apiSign);
}
