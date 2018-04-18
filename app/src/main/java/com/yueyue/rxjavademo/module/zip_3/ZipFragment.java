package com.yueyue.rxjavademo.module.zip_3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yueyue.rxjavademo.BaseFragment;
import com.yueyue.rxjavademo.Constant;
import com.yueyue.rxjavademo.R;
import com.yueyue.rxjavademo.adapter.ItemListAdapter;
import com.yueyue.rxjavademo.model.HuaBanBeauty;
import com.yueyue.rxjavademo.model.Item;
import com.yueyue.rxjavademo.network.NetworkService;
import com.yueyue.rxjavademo.utils.GankBeautyResultToItemsMapper;
import com.yueyue.rxjavademo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/4/18 09:22
 * desc   :
 */
public class ZipFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ItemListAdapter mAdapter;


    @OnClick(R.id.btn_zipLoad)
    void load() {
        changeSwipeRefreshState(true);
        unsubscribe();
        Disposable disposable = Observable.zip(
                NetworkService.getGankApi().getBeauties(10, 1).map(GankBeautyResultToItemsMapper.getInstance()),
                NetworkService.getHuaBanApi().getBeauties(10, 1, Constant.SHOWAPI_TYPE, Constant.SHOWAPI_APPID, Constant.SHOWAPI_SIGN),
                (gankItems, huabanItems) -> {
                    List<Item> items = new ArrayList<>(gankItems);

                    List<HuaBanBeauty> huaBanBeauties = huabanItems.bean2List();
                    for (int i = 0; i < huaBanBeauties.size(); i++) {
                        HuaBanBeauty beauty = huaBanBeauties.get(i);
                        Item huaBanItem = new Item();

                        huaBanItem.description = beauty.title;
                        huaBanItem.imageUrl = beauty.thumb;
                        items.add(huaBanItem);
                    }

                    return items;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        items -> {
                            changeSwipeRefreshState(false);
                            mAdapter.setItems(items);
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
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        ButterKnife.bind(this, view);

        initSwipeRefresh();
        initRecyclerView();

        return view;
    }


    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefresh.setEnabled(false);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ItemListAdapter();
        mRecyclerView.setAdapter(mAdapter);
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
        return R.layout.dialog_zip;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }
}
