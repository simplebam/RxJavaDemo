package com.yueyue.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author : yueyue on 2018/4/18 21:27
 * desc   :
 */
public class YingPicResult {

    @SerializedName("images")
    public List<YingPic> images;
}
