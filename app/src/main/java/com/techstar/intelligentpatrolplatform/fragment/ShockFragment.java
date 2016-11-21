package com.techstar.intelligentpatrolplatform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.activity.MainActivity;
import com.techstar.intelligentpatrolplatform.utils.ChartUtils;
import com.techstar.intelligentpatrolplatform.utils.CommonUtils;
import com.techstar.intelligentpatrolplatform.utils.LogUtils;

import org.achartengine.GraphicalView;

import java.util.ArrayList;

/**
 * author lrzg on 16/11/10.
 * 描述：震动
 */

public class ShockFragment extends BaseFragment implements View.OnClickListener {

    private TextView mShock_tv;
    private RadioGroup mShock_gp;
    private RadioButton mVhf_rb, mLow_rb;
    private LinearLayout mShock_ll;
    private ChartUtils mChartUtils;
    private ImageButton mBarLeft_img, mBarRight_img;
    private boolean isStart = true;
    ArrayList<Float> datas = new ArrayList<Float>();

    public static ShockFragment newInstance(String action) {
        ShockFragment fragment = new ShockFragment();
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
        view = inflater.inflate(R.layout.fragment_shock, container, false);

        mShock_ll = (LinearLayout) view.findViewById(R.id.ll_shock_chart);
        mBarLeft_img = (ImageButton) view.findViewById(R.id.img_bar_left);
        mBarRight_img = (ImageButton) view.findViewById(R.id.img_bar_right);

        mShock_tv = (TextView) view.findViewById(R.id.tv_shock);
        mShock_gp = (RadioGroup) view.findViewById(R.id.gp_shock);
        mVhf_rb = (RadioButton) view.findViewById(R.id.rb_vhf);
        mLow_rb = (RadioButton) view.findViewById(R.id.rb_low);

        return view;
    }

    @Override
    public void initData() {
        hideLoading();
        mChartUtils = new ChartUtils(getActivity(), "", 0, 1000, -30, 30, R.color.wathet, R.color.wathet);
        GraphicalView mChart = mChartUtils.getChart();
        mShock_ll.addView(mChart);
    }

    @Override
    public void initEvent() {
        mShock_gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mVhf_rb.getId()) {
                    LogUtils.e("高频");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.onShockVHF();
                } else if (checkedId == mLow_rb.getId()) {
                    LogUtils.e("低频");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.onShockLOW();
                }
            }
        });

        mBarLeft_img.setOnClickListener(this);
        mBarRight_img.setOnClickListener(this);

    }

    @Override
    protected void onRefresh() {

    }

    /**
     * 设置温度
     *
     * @param temp
     */
    public void setData(final float temp) {
        LogUtils.i("temp:" + temp);
        if(datas.size() > 1000){
            mChartUtils.rightUpdateCharts(datas);
            datas.clear();
        }else {
            datas.add(temp);
        }

//        mChartUtils.updateChart(temp);
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mShock_tv.setText("" + temp);
                    }
                });
    }

    public void setListData(ArrayList<Float> listData){


        mChartUtils.rightUpdateCharts(listData);
        int size = listData.size() - 1;
        final Float temp = listData.get(size);
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mShock_tv.setText("" + temp);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_bar_left:
                if (isStart) {
                    isStart = false;
                    mBarLeft_img.setImageResource(R.mipmap.stop);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.onStartCollectingShock();
                } else {
                    isStart = true;
                    mBarLeft_img.setImageResource(R.mipmap.start);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.onStopCollectingShock();
                }

                break;
            case R.id.img_bar_right:
                LogUtils.e("右边");
                break;
        }
    }
}
