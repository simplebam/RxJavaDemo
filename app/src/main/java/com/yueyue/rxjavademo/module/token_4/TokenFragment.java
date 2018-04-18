package com.yueyue.rxjavademo.module.token_4;

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
import com.yueyue.rxjavademo.network.NetworkService;
import com.yueyue.rxjavademo.network.api.FakeApi;
import com.yueyue.rxjavademo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/4/18 19:41
 * desc   :
 */
public class TokenFragment extends BaseFragment {

    @BindView(R.id.tv_token)
    TextView mTvToken;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;


    @OnClick(R.id.btn_request)
    void upload() {
        changeSwipeRefreshState(true);
        unsubscribe();
        final FakeApi fakeApi = NetworkService.getFakeApi();
        Disposable disposable = fakeApi.getFakeToken("fake_auth_code")
                .flatMap(fakeToken -> fakeApi.getFakeData(fakeToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        fakeThing -> {
                            changeSwipeRefreshState(false);
                            mTvToken.setText(getString(R.string.got_data, fakeThing.id, fakeThing.name));
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
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        ButterKnife.bind(this, view);
        initSwipeRefresh();
        return view;
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefresh.setEnabled(false);
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
        return R.layout.dialog_token;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token;
    }
}
