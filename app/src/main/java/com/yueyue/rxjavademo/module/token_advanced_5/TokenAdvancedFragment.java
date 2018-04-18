package com.yueyue.rxjavademo.module.token_advanced_5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yueyue.rxjavademo.BaseFragment;
import com.yueyue.rxjavademo.R;
import com.yueyue.rxjavademo.model.FakeToken;
import com.yueyue.rxjavademo.network.NetworkService;
import com.yueyue.rxjavademo.network.api.FakeApi;
import com.yueyue.rxjavademo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/4/18 20:07
 * desc   :
 */
public class TokenAdvancedFragment extends BaseFragment {
    @BindView(R.id.tv_token)
    TextView mTvToken;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    final FakeToken cachedFakeToken = new FakeToken(true);
    boolean tokenUpdated;

    @OnClick(R.id.btn_invalidate_token)
    void invalidateToken() {
        cachedFakeToken.expired = true;
        ToastUtil.showShort(R.string.token_destroyed);
    }

    @OnClick(R.id.btn_request)
    void upload() {
        tokenUpdated = false;
        changeSwipeRefreshState(true);
        unsubscribe();
        final FakeApi fakeApi = NetworkService.getFakeApi();
        //All RxJava - 为 Retrofit 添加重试 - Android - 掘金
        //           https://juejin.im/entry/5937a12cac502e0068ccfdc5
        Disposable disposable = Observable.just(1)
                .flatMap(o -> cachedFakeToken.token == null
                        ? Observable.error(new NullPointerException("Token is null!"))
                        : fakeApi.getFakeData(cachedFakeToken))
                .retryWhen(observable -> observable.flatMap(throwable -> {
                            if (throwable instanceof IllegalArgumentException ||
                                    throwable instanceof NullPointerException) {
                                return fakeApi.getFakeToken("fake_auth_code")
                                        .doOnNext(fakeToken -> {
                                            tokenUpdated = true;
                                            cachedFakeToken.token = fakeToken.token;
                                            cachedFakeToken.expired = fakeToken.expired;
                                        });
                            }
                            return Observable.error(throwable);
                        })
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        fakeData -> {
                            changeSwipeRefreshState(false);

                            String token = cachedFakeToken.token;
                            token = tokenUpdated ? token + "(" + getString(R.string.updated) + ")" : token;

                            mTvToken.setText(getString(R.string.got_token_and_data, token, fakeData.id, fakeData.name));
                        },
                        throwable -> {
                            changeSwipeRefreshState(false);
                            ToastUtil.showShort(R.string.loading_failed);
                        });

        mCompositeDisposable.add(disposable);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_advanced, container, false);
        ButterKnife.bind(this, view);
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefresh.setEnabled(false);
        return view;
    }

    private void changeSwipeRefreshState(boolean refreshing) {
        boolean currRefreshing = mSwipeRefresh.isRefreshing();
        if (refreshing == currRefreshing) {
            return;
        }

        mSwipeRefresh.setRefreshing(refreshing);
    }


    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }
}
