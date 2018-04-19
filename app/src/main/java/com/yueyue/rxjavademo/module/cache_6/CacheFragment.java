package com.yueyue.rxjavademo.module.cache_6;

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
import android.widget.TextView;

import com.yueyue.rxjavademo.BaseFragment;
import com.yueyue.rxjavademo.R;
import com.yueyue.rxjavademo.adapter.ItemListAdapter;
import com.yueyue.rxjavademo.module.cache_6.data.Data;
import com.yueyue.rxjavademo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * author : yueyue on 2018/4/19 08:56
 * desc   :
 */
public class CacheFragment extends BaseFragment {

    @BindView(R.id.atv_loading_time)
    TextView aTvLoadingTime;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private long startingTime;
    private ItemListAdapter mAdapter;

    @OnClick(R.id.btn_load)
    void load() {
        changeSwipeRefreshState(true);
        startingTime = System.currentTimeMillis();
        unsubscribe();
        Disposable disposable = Data.getInstance()
                .subscribeData(
                        items -> {
                            changeSwipeRefreshState(false);

                            int loadingTime = (int) (System.currentTimeMillis() - startingTime);
                            aTvLoadingTime.setText(getString(R.string.loading_time_and_source,
                                    loadingTime, Data.getInstance().getDataSourceText()));

                            mAdapter.setItems(items);
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            changeSwipeRefreshState(false);
                            ToastUtil.showShort(R.string.loading_failed);
                        });
        mCompositeDisposable.add(disposable);
    }

    @OnClick(R.id.abt_clear_memory_cache)
    void clearMemoryCache() {
        Data.getInstance().clearMemoryCache();
        mAdapter.setItems(null);
        ToastUtil.showShort(R.string.memory_cache_cleared);
    }

    @OnClick(R.id.abt_clear_memory_and_disk_cache)
    void clearMemoryAndDiskCache() {
        //这里还不是真正意义上的清空图片磁盘(因为Glide还有图片缓存的)
        Data.getInstance().clearMemoryAndDiskCache();
        mAdapter.setItems(null);
        ToastUtil.showShort(R.string.memory_and_disk_cache_cleared);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        ButterKnife.bind(this, view);
        initSwipeRefresh();
        initAdapter();
        return view;
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeColors(Color.GREEN,Color.BLUE,Color.RED);
        mSwipeRefresh.setEnabled(false);
    }

    private void initAdapter() {
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
        return R.layout.dialog_cache;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }
}
