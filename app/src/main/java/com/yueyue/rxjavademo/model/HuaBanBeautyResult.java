package com.yueyue.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yueyue on 2018/4/18 10:13
 * desc   :
 */
public class HuaBanBeautyResult {

    @SerializedName("showapi_res_body")
    public HuaBanBodyBean huaBanBodyBean;
    @SerializedName("showapi_res_code")
    public int resCode;
    @SerializedName("showapi_res_error")
    public String resError;

    public List<HuaBanBeauty> huaBanBeauties;

    public static class HuaBanBodyBean {

        @SerializedName("0")
        public HuaBanBeauty beauty0;
        @SerializedName("1")
        public HuaBanBeauty beauty1;
        @SerializedName("2")
        public HuaBanBeauty beauty2;
        @SerializedName("3")
        public HuaBanBeauty beauty3;
        @SerializedName("4")
        public HuaBanBeauty beauty4;
        @SerializedName("5")
        public HuaBanBeauty beauty5;
        @SerializedName("6")
        public HuaBanBeauty beauty6;
        @SerializedName("7")
        public HuaBanBeauty beauty7;
        @SerializedName("8")
        public HuaBanBeauty beauty8;
        @SerializedName("9")
        public HuaBanBeauty beauty9;

        @SerializedName("ret_code")
        public int retCode;

    }

    public List<HuaBanBeauty> bean2List() {
        huaBanBeauties = new ArrayList<>(10);
        huaBanBeauties.add(huaBanBodyBean.beauty0);
        huaBanBeauties.add(huaBanBodyBean.beauty1);
        huaBanBeauties.add(huaBanBodyBean.beauty2);
        huaBanBeauties.add(huaBanBodyBean.beauty3);
        huaBanBeauties.add(huaBanBodyBean.beauty4);
        huaBanBeauties.add(huaBanBodyBean.beauty5);
        huaBanBeauties.add(huaBanBodyBean.beauty6);
        huaBanBeauties.add(huaBanBodyBean.beauty7);
        huaBanBeauties.add(huaBanBodyBean.beauty8);
        huaBanBeauties.add(huaBanBodyBean.beauty9);
        return huaBanBeauties;

    }

}
