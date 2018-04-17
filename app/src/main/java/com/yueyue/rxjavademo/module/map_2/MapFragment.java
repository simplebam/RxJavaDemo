package com.yueyue.rxjavademo.module.map_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yueyue.rxjavademo.BaseFragment;
import com.yueyue.rxjavademo.R;
import com.yueyue.rxjavademo.adapter.ItemListAdapter;
import com.yueyue.rxjavademo.network.NetworkService;
import com.yueyue.rxjavademo.utils.GankBeautyResultToItemsMapper;
import com.yueyue.rxjavademo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author : yueyue on 2018/4/17 21:25
 * desc   :
 */
public class MapFragment extends BaseFragment {

    private int page = 0;

    @BindView(R.id.tv_page)
    TextView mTvPage;
    @BindView(R.id.acb_pre_page)
    Button mAcbPrePage;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ItemListAdapter mAdapter;

    @OnClick(R.id.acb_pre_page)
    void previousPage() {
        loadPage(--page);
        if (page <= 1) {
            mAcbPrePage.setEnabled(false);
        }
    }

    @OnClick(R.id.acb_next_page)
    void nextPage() {
        loadPage(++page);
        if (page > 1) {
            mAcbPrePage.setEnabled(true);
        }
    }

    private void loadPage(int page) {
        changeSwipeRefreshState();
        unsubscribe();
        Disposable disposable = NetworkService.getGankApi()
                .getBeauties(10, page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    changeSwipeRefreshState();
                    mTvPage.setText(getString(R.string.page_with_number, MapFragment.this.page));
                    mAdapter.setItems(items);
                }, throwable -> {
                    changeSwipeRefreshState();
                    ToastUtil.showShort(R.string.loading_failed);
                });

        mCompositeDisposable.add(disposable);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        initSwipeRefresh();
        initRecyclerView();
        return view;
    }

    private void initSwipeRefresh() {
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
    }

    private void changeSwipeRefreshState() {
        boolean refreshing = mSwipeRefresh.isRefreshing();
        mSwipeRefresh.setRefreshing(!refreshing);
    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mSwipeRefresh.setEnabled(false);
        mAdapter = new ItemListAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }
}
