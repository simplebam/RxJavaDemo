package com.yueyue.rxjavademo.module.complete_1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yueyue.rxjavademo.BaseFragment;
import com.yueyue.rxjavademo.Constant;
import com.yueyue.rxjavademo.R;
import com.yueyue.rxjavademo.network.NetworkService;
import com.yueyue.rxjavademo.network.api.YingApi;
import com.yueyue.rxjavademo.utils.ToastUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/4/17 20:32
 * desc   :
 */
public class CompleteFragment extends BaseFragment {

    @BindView(R.id.iv_image)
    ImageView mIvImage;

    @OnClick(R.id.btn_request)
    void load() {
        unsubscribe();
        int random = new Random().nextInt(5) + 1;//离目前最新一次图片更新的日期天数
        Disposable disposable = NetworkService.getYingApi().getimages(Constant.YING_FORMAT, random, 1)
                .filter(yingPicResult -> yingPicResult.images != null && yingPicResult.images.size() > 0)
                .map(yingPicResult -> yingPicResult.images.get(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        yingPic -> {
                            Glide.with(getContext()).load(YingApi.HOST + yingPic.url)
                                    .into(mIvImage);
                        },
                        throwable -> ToastUtil.showShort(R.string.loading_failed));
        mCompositeDisposable.add(disposable);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_complete;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_complete;
    }
}
