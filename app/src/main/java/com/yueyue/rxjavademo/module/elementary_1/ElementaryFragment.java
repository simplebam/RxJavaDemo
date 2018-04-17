package com.yueyue.rxjavademo.module.elementary_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yueyue.rxjavademo.BaseFragment;
import com.yueyue.rxjavademo.R;

import butterknife.ButterKnife;

/**
 * author : yueyue on 2018/4/17 20:32
 * desc   :
 */
public class ElementaryFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }
}
