package com.yueyue.rxjavademo;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;

import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * author : yueyue on 2018/4/17 20:16
 * desc   :
 */
public abstract class BaseFragment extends Fragment {

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @OnClick(R.id.btn_tip)
    void tip() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }


    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
