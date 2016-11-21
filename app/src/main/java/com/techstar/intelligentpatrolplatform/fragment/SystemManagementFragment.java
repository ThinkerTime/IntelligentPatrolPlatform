package com.techstar.intelligentpatrolplatform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.utils.CommonUtils;

/**
 * author lrzg on 16/11/10.
 * 描述：系统设置
 */

public class SystemManagementFragment extends BaseFragment{

    public static SystemManagementFragment newInstance(String action) {
        SystemManagementFragment fragment = new SystemManagementFragment();
        Bundle args = new Bundle();
        args.putString(CommonUtils.ACTION, action);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
        }
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_system_management, container, false);
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
