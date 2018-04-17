package com.yueyue.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author : yueyue on 2018/4/17 22:06
 * desc   :
 */
public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results")
    List<GankBeauty> beauties;
}