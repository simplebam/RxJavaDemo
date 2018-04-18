package com.yueyue.rxjavademo.model;

/**
 * author : yueyue on 2018/4/18 19:48
 * desc   :
 */
public class FakeToken {
    public String token;
    public boolean expired;

    public FakeToken() {
    }

    public FakeToken(boolean expired) {
        this.expired = expired;
    }
}
