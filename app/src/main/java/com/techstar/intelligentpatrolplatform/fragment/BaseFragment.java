package com.techstar.intelligentpatrolplatform.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.utils.UIUtils;

/**
 * author lrzg on 16/11/10.
 * 描述：
 */

public abstract class BaseFragment extends Fragment{

    public View view;
    public View loadingView;
    private ImageView mGif;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "Android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = initView(inflater, container);

        loadingView = View.inflate(getActivity(), R.layout.loading_page, null);
        FrameLayout mFrameLayout = new FrameLayout(getActivity());
        mFrameLayout.addView(view);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = UIUtils.dip2px(50, getActivity());

        mFrameLayout.addView(loadingView, layoutParams);
        showLoading();
        return mFrameLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * xml--->view  最终显示在界面上的方法
     *
     * @return view
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 数据填充UI操作，请求网络等
     */
    public abstract void initData();

    /**
     * 设置各种点击事件
     */
    public abstract void initEvent();



    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        if (mGif == null) {
            mGif = (ImageView) loadingView.findViewById(R.id.img_gif);
        }

    }

    protected abstract void onRefresh();

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
