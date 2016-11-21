package com.techstar.intelligentpatrolplatform.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.activity.MainActivity;
import com.techstar.intelligentpatrolplatform.utils.ChartUtils;
import com.techstar.intelligentpatrolplatform.utils.CommonUtils;
import com.techstar.intelligentpatrolplatform.utils.LogUtils;

import org.achartengine.GraphicalView;

/**
 * author lrzg on 16/11/10.
 * 描述：温度
 */

public class TemperatureFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mTempChart_ll;
    private TextView mTemperature_tv;
    private ChartUtils mChartUtils;
    private ImageButton mBarLeft_img, mBarRight_img;
    private boolean isStart = true;

    public static TemperatureFragment newInstance(String action) {
        TemperatureFragment fragment = new TemperatureFragment();
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
        view = inflater.inflate(R.layout.fragment_temperature, container, false);
        mTemperature_tv = (TextView) view.findViewById(R.id.tv_temperature);

        mTempChart_ll = (LinearLayout) view.findViewById(R.id.ll_temp_chart);
        mBarLeft_img = (ImageButton) view.findViewById(R.id.img_bar_left);
        mBarRight_img = (ImageButton) view.findViewById(R.id.img_bar_right);

        return view;
    }


    @Override
    public void initData() {
        hideLoading();

        mChartUtils = new ChartUtils(getActivity(), "", 0, 60, 0, 160, R.color.wathet, R.color.wathet);
        GraphicalView mChart = mChartUtils.getChart();
        mTempChart_ll.addView(mChart);
    }


    /**
     * 设置温度
     *
     * @param temp
     */
    public void setData(final float temp) {
        LogUtils.i("temp:"+temp);
        mChartUtils.updateChart(temp);

        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mTemperature_tv.setText("" + temp);
                    }
                });
    }


    @Override
    public void initEvent() {
        LogUtils.i("温度Fragment:");
        mBarLeft_img.setOnClickListener(this);
        mBarRight_img.setOnClickListener(this);
    }

    @Override
    protected void onRefresh() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_bar_left:
                if (isStart) {
                    isStart = false;
                    mBarLeft_img.setImageResource(R.mipmap.stop);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.onStartCollectingTemperature();
//                    mainActivity.OnOpen();
                } else {
                    isStart = true;
                    mBarLeft_img.setImageResource(R.mipmap.start);
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.onStopCollectingTemperature();
//                    mainActivity.OnStop();
                }

                break;
            case R.id.img_bar_right:
                LogUtils.e("右边");
                break;

        }
    }
}
