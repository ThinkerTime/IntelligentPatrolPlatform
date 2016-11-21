package com.techstar.intelligentpatrolplatform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.utils.CommonUtils;

/**
 * author lrzg on 16/11/17.
 * 描述：射频标签
 */

public class FridFragment extends BaseFragment{

    public static FridFragment newInstance(String action) {
        FridFragment fragment = new FridFragment();
        Bundle args = new Bundle();
        args.putString(CommonUtils.ACTION, action);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_frid,container,false);
        return view;
    }

    @Override
    public void initData() {
        hideLoading();
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onRefresh() {

    }
}
