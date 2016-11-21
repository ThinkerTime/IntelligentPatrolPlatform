package com.techstar.intelligentpatrolplatform.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.fragment.FridFragment;
import com.techstar.intelligentpatrolplatform.fragment.InfraredSpectrumFragment;
import com.techstar.intelligentpatrolplatform.fragment.ShockFragment;
import com.techstar.intelligentpatrolplatform.fragment.SystemManagementFragment;
import com.techstar.intelligentpatrolplatform.fragment.TemperatureFragment;
import com.techstar.intelligentpatrolplatform.utils.CommonUtils;
import com.techstar.intelligentpatrolplatform.utils.ErrorType;
import com.techstar.intelligentpatrolplatform.utils.LogUtils;
import com.techstar.intelligentpatrolplatform.utils.SPUtils;
import com.techstar.intelligentpatrolplatform.utils.StrUtils;
import com.techstar.intelligentpatrolplatform.utils.UsbDevicesUtils;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mTemperatureTab, mShockTab, mInfraredSpectrumTab,mFridTab, mSystemManagementTab;
    private ImageView mTemperature_img, mShock_img, mInfraredSpectrum_img,mFrid_img, mSystemManagement_img;
    private TextView mTemperature_text, mShock_text, mInfraredSpectrum_text,mFrid_text, mSystemManagement_text;
    private TemperatureFragment mTemperatureFragment;
    private ShockFragment mShockFragment;
    private InfraredSpectrumFragment mInfraredSpectrumFragment;
    private FridFragment mFridFragment;
    private SystemManagementFragment mSystemManagementFragment;

    private static final int TAB_TEMPERATURE = 0;
    private static final int TAB_SHOCK = 1;
    private static final int TAB_INFRARED_SPECTRUM = 2;
    private static final int TAB_FRID = 3;
    private static final int TAB_SYSTEM_MANAGEMENT = 4;

    private int mCurrentPosition;
    private Timer timer = new Timer();
    private long mExitTime = 0;

    private UsbManager mUsbManager;
    UsbDevice mydevice;         //usb设备对象
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private int usb_used = 0;

    UsbMsgThread mUsbMsgThread;
    private UsbDevicesUtils mUsbDevicesUtils;
    private boolean isDeviceState = false;//设备打开状态

    private PendingIntent mPermissionIntent;
    ArrayList<Float> datas = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        initData();
        setSelected(TAB_TEMPERATURE);

        initUsb();
    }

    private void initView() {
        mTemperatureTab = (LinearLayout) findViewById(R.id.ll_tab_temperature);
        mShockTab = (LinearLayout) findViewById(R.id.ll_tab_shock);
        mInfraredSpectrumTab = (LinearLayout) findViewById(R.id.ll_tab_infrared_spectrum);
        mFridTab = (LinearLayout)  findViewById(R.id.ll_tab_frid);
        mSystemManagementTab = (LinearLayout) findViewById(R.id.ll_tab_system_management);

        mTemperature_img = (ImageView) findViewById(R.id.img_tab_temperature);
        mShock_img = (ImageView) findViewById(R.id.img_tab_shock);
        mInfraredSpectrum_img = (ImageView) findViewById(R.id.img_tab_infrared_spectrum);
        mFrid_img = (ImageView) findViewById(R.id.img_tab_frid);
        mSystemManagement_img = (ImageView) findViewById(R.id.img_tab_system_management);


        mTemperature_text = (TextView) findViewById(R.id.text_tab_temperature);
        mShock_text = (TextView) findViewById(R.id.text_tab_shock);
        mInfraredSpectrum_text = (TextView) findViewById(R.id.text_tab_infrared_spectrum);
        mFrid_text = (TextView)findViewById(R.id.text_tab_frid);
        mSystemManagement_text = (TextView) findViewById(R.id.text_tab_system_management);

    }

    private void initEvent() {
        mTemperatureTab.setOnClickListener(this);
        mShockTab.setOnClickListener(this);
        mInfraredSpectrumTab.setOnClickListener(this);
        mFridTab.setOnClickListener(this);
        mSystemManagementTab.setOnClickListener(this);
    }


    /**
     * 初始化USB
     */
    private void initUsb() {

        //设置广播接收器
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE); //获取usb服务
        if (mUsbManager == null) {
            Toast.makeText(MainActivity.this, "获取USB服务失败", Toast.LENGTH_LONG).show();
            return;
        }
        //搜索所有的usb设备

        mUsbDevicesUtils = new UsbDevicesUtils(MainActivity.this, mUsbManager, mPermissionIntent);
        mydevice = mUsbDevicesUtils.ScanDevices();
        if (mydevice != null) {
            Toast.makeText(MainActivity.this, "发现设备", Toast.LENGTH_LONG).show();
            getUsbInfo(mydevice);
        } else {
            Toast.makeText(getApplicationContext(), "mydevice = null,未找到点巡检设备", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    /**
     * 获取usb相关信息
     *
     * @param mydevice
     * @return
     */
    public void getUsbInfo(UsbDevice mydevice) {

        int ret = mUsbDevicesUtils.OpenDevice(mydevice);
        if (ret != ErrorType.ERR_OPEN_ENDPOINT_SUCCESS) {
            //失败
            isDeviceState = false;
            Toast.makeText(getApplicationContext(), "USB设备打开失败", Toast.LENGTH_SHORT).show();
        } else {
            //成功
            isDeviceState = true;
            Toast.makeText(getApplicationContext(), "USB设备打开成功", Toast.LENGTH_SHORT).show();
        }

        //创建线程 读取usb返回信息
        mUsbMsgThread = new UsbMsgThread(mHandler);  //创建 usb数据 接收线程
        mUsbMsgThread.start();
        usb_used = 1;


    }

    /**
     * 开启线程接受消息
     */
    class UsbMsgThread extends Thread {           //usb消息 接收线程
        private Handler msgHandler;                   //Handler

        public UsbMsgThread(Handler mHandler) {   //构造函数，获得mmInStream和msgHandler对象
            this.msgHandler = mHandler;
        }

        public void run() {
            LogUtils.d("run运行");

            byte[] InBuffer = new byte[4 * 1024];           //创建 缓冲区,1次传输 8个字节
            int length = InBuffer.length;
            int timeout = 5000;
            while (!Thread.interrupted()) {

                int cnt = mUsbDevicesUtils.USBReadData(InBuffer, length, timeout);//接收bulk数据
                if (cnt < 0) {                        //没有接收到数据，则继续循环
                    continue;
                }

                Message msg = new Message();          //定义一个消息,并填充数据
                msg.what = 0x1234;
                msg.arg1 = cnt;
                msg.obj = InBuffer;
                msgHandler.sendMessage(msg);          //通过handler发送消息
            }


        }
    }


    public void OnOpen() {
        byte buffer[] = new byte[8];
        buffer[0] = (byte) 0x81;
        buffer[1] = 1;  //开灯
        buffer[2] = 0;  //命令

        buffer[3] = 0;  //参数长度
        buffer[4] = 0;  //参数长度
        buffer[5] = 0;  //参数长度
        buffer[6] = 0;  //参数长度
        buffer[7] = 0;  //参数长度

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "开灯命令发送成功 : " + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "开灯命令发送失败", Toast.LENGTH_LONG).show();
        }


    }

    public void OnStop() {
        byte buffer[] = new byte[8];
        buffer[0] = (byte) 0x81;
        buffer[1] = 3;  //关灯
        buffer[2] = 0;  //命令

        buffer[3] = 0;  //参数长度
        buffer[4] = 0;  //参数长度
        buffer[5] = 0;  //参数长度
        buffer[6] = 0;  //参数长度
        buffer[7] = 0;  //参数长度

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "开灯命令发送成功 : " + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "开灯命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_tab_temperature:
                if (mCurrentPosition != TAB_TEMPERATURE) {
                    mCurrentPosition = TAB_TEMPERATURE;
                    setSelected(TAB_TEMPERATURE);
                }
                LogUtils.i("温度");
                break;
            case R.id.ll_tab_shock:
                if (mCurrentPosition != TAB_SHOCK) {
                    mCurrentPosition = TAB_SHOCK;
                    setSelected(TAB_SHOCK);
                }

                LogUtils.i("震动");
                break;
            case R.id.ll_tab_infrared_spectrum:
                if (mCurrentPosition != TAB_INFRARED_SPECTRUM) {
                    mCurrentPosition = TAB_INFRARED_SPECTRUM;
                    setSelected(TAB_INFRARED_SPECTRUM);
                }
                LogUtils.i("红外图谱");
                break;
            case R.id.ll_tab_frid:
                if (mCurrentPosition != TAB_FRID) {
                    mCurrentPosition = TAB_FRID;
                    setSelected(TAB_FRID);
                }
                LogUtils.i("射频");
                break;
            case R.id.ll_tab_system_management:
                if (mCurrentPosition != TAB_SYSTEM_MANAGEMENT) {
                    mCurrentPosition = TAB_SYSTEM_MANAGEMENT;
                    setSelected(TAB_SYSTEM_MANAGEMENT);
                }
                LogUtils.i("系统设置");
                break;
        }

    }

    public void setSelected(int tab) {
        SPUtils.saveIntSP(CommonUtils.CURRENT_TAB, tab);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        resetTab();
        switch (tab) {
            case TAB_TEMPERATURE:
                if (mTemperatureFragment == null) {

                    mTemperatureFragment = TemperatureFragment.newInstance("");
                    transaction.add(R.id.fl_content, mTemperatureFragment, mTemperatureFragment.getClass().getName());
                } else {
                    transaction.show(mTemperatureFragment);
                }
                mTemperature_img.setImageResource(R.mipmap.temperature_sel_icon_tab);
                mTemperature_text.setTextColor(getResources().getColor(R.color.wathet));
                break;
            case TAB_SHOCK:
                if (mShockFragment == null) {
                    mShockFragment = ShockFragment.newInstance("");
                    transaction.add(R.id.fl_content, mShockFragment, mShockFragment.getClass().getName());
                } else {
                    transaction.show(mShockFragment);
                }
                mShock_img.setImageResource(R.mipmap.shock_sel_icon_tab);
                mShock_text.setTextColor(getResources().getColor(R.color.wathet));

                break;
            case TAB_INFRARED_SPECTRUM:
                if (mInfraredSpectrumFragment == null) {
                    mInfraredSpectrumFragment = InfraredSpectrumFragment.newInstance("");
                    transaction.add(R.id.fl_content, mInfraredSpectrumFragment, mInfraredSpectrumFragment.getClass().getName());
                } else {
                    transaction.show(mInfraredSpectrumFragment);
                }
                mInfraredSpectrum_img.setImageResource(R.mipmap.infraredspectrum_sel_icon_tab);
                mInfraredSpectrum_text.setTextColor(getResources().getColor(R.color.wathet));

                break;
            case TAB_FRID:
                if (mFridFragment == null) {
                    mFridFragment = FridFragment.newInstance("");
                    transaction.add(R.id.fl_content, mFridFragment, mFridFragment.getClass().getName());
                } else {
                    transaction.show(mFridFragment);
                }
                mFrid_img.setImageResource(R.mipmap.frid_sel_icon_tab);
                mFrid_text.setTextColor(getResources().getColor(R.color.wathet));
                break;
            case TAB_SYSTEM_MANAGEMENT:

                if (mSystemManagementFragment == null) {
                    mSystemManagementFragment = SystemManagementFragment.newInstance("");
                    transaction.add(R.id.fl_content, mSystemManagementFragment, mSystemManagementFragment.getClass().getName());
                } else {
                    transaction.show(mSystemManagementFragment);
                }
                mSystemManagement_img.setImageResource(R.mipmap.systemmanagement_sel_icon_tab);
                mSystemManagement_text.setTextColor(getResources().getColor(R.color.wathet));

                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void resetTab() {
        mTemperature_img.setImageResource(R.mipmap.temperature_icon_tab);
        mShock_img.setImageResource(R.mipmap.shock_icon_tab);
        mInfraredSpectrum_img.setImageResource(R.mipmap.infraredspectrum_icon_tab);
        mFrid_img.setImageResource(R.mipmap.frid_icon_tab);
        mSystemManagement_img.setImageResource(R.mipmap.systemmanagement_icon_tab);

        mTemperature_text.setTextColor(getResources().getColor(R.color.bg_back_img));
        mShock_text.setTextColor(getResources().getColor(R.color.bg_back_img));
        mInfraredSpectrum_text.setTextColor(getResources().getColor(R.color.bg_back_img));
        mFrid_text.setTextColor(getResources().getColor(R.color.bg_back_img));
        mSystemManagement_text.setTextColor(getResources().getColor(R.color.bg_back_img));
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTemperatureFragment != null) {
            transaction.hide(mTemperatureFragment);
        }
        if (mShockFragment != null) {
            transaction.hide(mShockFragment);
        }
        if (mInfraredSpectrumFragment != null) {
            transaction.hide(mInfraredSpectrumFragment);
        }
        if(mFridFragment != null){
            transaction.hide(mFridFragment);
        }
        if (mSystemManagementFragment != null) {
            transaction.hide(mSystemManagementFragment);
        }
    }

    public void initData() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    for (int i = 0; i < 1000; i++) {
//                        java.util.Random random = new java.util.Random();// 定义随机类
//                        int result = random.nextInt(51);// 返回[0,51)集合中的整数，注意不包括51
//
//                        Message message = new Message();
//                        message.what = result;
//                        mHandler.sendMessage(message);
//                        Thread.sleep(100);
//
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();

//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Random random = new Random();// 定义随机类
//                int result = random.nextInt(51);// 返回[0,51)集合中的整数，注意不包括51
//                Message message = new Message();
//                message.what = result;
//                mHandler.sendMessage(message);
//                temperatureFragment.setData(result);
//            }
//        };
//        timer.schedule(timerTask, 2000, 1000);

    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0x1234) {//如果消息是 0x1234,则是从 线程中 传输过来的数据
//                Toast.makeText(MainActivity.this,""+msg.arg1,Toast.LENGTH_LONG).show();
                showResult((byte[]) msg.obj, msg.arg1);//将 缓冲区的数据显示到 UI
            }

            super.handleMessage(msg);
        }
    };

    /**
     * 显示 下位机 通过 usb 接口传输过来的数据 【bulk方式】
     *
     * @param buffer
     * @param count
     */
    private void showResult(byte[] buffer, int count) {
        StringBuffer msg = new StringBuffer();//创建缓冲区
        LogUtils.e("count:" + count);

        if (buffer[0] != (byte) 0xBB) {
            return;
        }
        //温度结果返回
        if (buffer[1] == (byte) 0x01 && buffer[2] == (byte) 0x04 && buffer[count - 1] == (byte) 0x99) {
            final TemperatureFragment temperatureFragment = (TemperatureFragment) getSupportFragmentManager().findFragmentByTag(mTemperatureFragment.getClass().getName());
            String s = StrUtils.toHexString(buffer);
            //截取数据域
            final String substring = s.substring(5 * 2, count * 2 - 2 * 2);
            LogUtils.i("substring:" + substring);
            final int length = substring.length() / 8;
            for (int i = 0; i < length; i++) {
                String substring1 = substring.substring(i * 8, (i + 1) * 8);
                Float value = Float.intBitsToFloat(Integer.valueOf(substring1, 16));
                temperatureFragment.setData(value);
            }

//
//            for (int i = 3; i < (count - 1); i++) {//循环 加入 数据，16进制 格式
//                msg.append(String.format("0x%x ", buffer[i]));
//            }
//            LogUtils.i("测温结果:"+msg);
        }
        //温度界面打开n
        if (buffer[1] == (byte) 0x01 && buffer[2] == (byte) 0x03 && buffer[count - 1] == (byte) 0x99) {
            setSelected(TAB_TEMPERATURE);
        }

        //震动结果返回
        if (buffer[1] == (byte) 0x02 && buffer[2] == (byte) 0x08 && buffer[count - 1] == (byte) 0x99) {
            final ShockFragment shockFragment = (ShockFragment) getSupportFragmentManager().findFragmentByTag(mShockFragment.getClass().getName());
            String s = StrUtils.toHexString(buffer);
            //截取数据域
            final String substring = s.substring(5 * 2, count * 2 - 2 * 2);
            final int length = substring.length() / 8;
            for (int i = 0; i < length; i++) {
                String substring1 = substring.substring(i * 8, (i + 1) * 8);
                Float value = Float.intBitsToFloat(Integer.valueOf(substring1, 16));

                datas.add(value);
                if (i == (length - 1)) {
                    shockFragment.setListData(datas);
                    datas.clear();
                }
            }


        }
        //震动界面打开
        if (buffer[1] == (byte) 0x02 && buffer[2] == (byte) 0x07 && buffer[count - 1] == (byte) 0x99) {
            setSelected(TAB_SHOCK);
        }


    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        //usb 设备操作 授权
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            Toast.makeText(getApplicationContext(), "设备有运行权限 ", Toast.LENGTH_LONG).show();
                            mydevice = device;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "设备被拒绝的权限 ", Toast.LENGTH_LONG).show();
                        return;
                    }


                }
            }
        }
    };

    /**
     * 开始采集温度
     */

    public void onStartCollectingTemperature() {
        byte buffer[] = new byte[7];
        buffer[0] = (byte) 0xBB;//帧头
        buffer[1] = (byte) 0x01;  //标识码
        buffer[2] = (byte) 0x01;  //控制码
        buffer[3] = (byte) 0x00;  //数据长度
        buffer[4] = (byte) 0x00;  //帧尾
        buffer[5] = (byte) 0xBD;  //校检码
        buffer[6] = (byte) 0x99;  //帧尾

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "开始采集命令发送成功" + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "开始采集命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 停止采集温度
     */
    public void onStopCollectingTemperature() {
        byte buffer[] = new byte[7];
        buffer[0] = (byte) 0xBB;//帧头
        buffer[1] = (byte) 0x01;  //标识码
        buffer[2] = (byte) 0x02;  //控制码
        buffer[3] = (byte) 0x00;  //数据长度
        buffer[4] = (byte) 0x00;  //帧尾
        buffer[5] = (byte) 0xBE;  //校检码
        buffer[6] = (byte) 0x99;  //帧尾

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "停止采集命令发送成功" + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "停止采集命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 开始采集振动
     */
    public void onStartCollectingShock() {
        byte buffer[] = new byte[7];
        buffer[0] = (byte) 0xBB;//帧头
        buffer[1] = (byte) 0x02;  //标识码
        buffer[2] = (byte) 0x05;  //控制码
        buffer[3] = (byte) 0x00;  //数据长度
        buffer[4] = (byte) 0x00;  //帧尾
        buffer[5] = (byte) 0xC2;  //校检码
        buffer[6] = (byte) 0x99;  //帧尾

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "开始采集命令发送成功" + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "开始采集命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 停止采集振动
     */

    public void onStopCollectingShock() {
        byte buffer[] = new byte[7];
        buffer[0] = (byte) 0xBB;//帧头
        buffer[1] = (byte) 0x02;  //标识码
        buffer[2] = (byte) 0x06;  //控制码
        buffer[3] = (byte) 0x00;  //数据长度
        buffer[4] = (byte) 0x00;  //帧尾
        buffer[5] = (byte) 0xC3;  //校检码
        buffer[6] = (byte) 0x99;  //帧尾

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "停止采集命令发送成功" + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "停止采集命令发送失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 设置振动高频
     */
    public void onShockVHF() {
        byte buffer[] = new byte[8];
        buffer[0] = (byte) 0xBB;//帧头
        buffer[1] = (byte) 0x04;  //标识码
        buffer[2] = (byte) 0x12;  //控制码
        buffer[3] = (byte) 0x00;  //数据长度
        buffer[4] = (byte) 0x01;  //帧尾
        buffer[5] = (byte) 0x02;  //数据域
        buffer[6] = (byte) 0xCE;  //校检码
        buffer[7] = (byte) 0x99;  //帧尾

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "高频设置成功" + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "高频设置失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 设置振动低频
     */
    public void onShockLOW() {
        byte buffer[] = new byte[8];
        buffer[0] = (byte) 0xBB;//帧头
        buffer[1] = (byte) 0x04;  //标识码
        buffer[2] = (byte) 0x12;  //控制码
        buffer[3] = (byte) 0x00;  //数据长度
        buffer[4] = (byte) 0x01;  //帧尾
        buffer[5] = (byte) 0x01;  //数据域
        buffer[6] = (byte) 0xCD;  //校检码
        buffer[7] = (byte) 0x99;  //帧尾

        int length = buffer.length;
        int timeout = 5000;

        int cnt = mUsbDevicesUtils.USBWriteData(buffer, length, timeout);
        if (cnt >= 0) {
            Toast.makeText(MainActivity.this, "低频设置成功" + cnt, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "低频设置失败", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        timer.cancel();
        if (usb_used > 0) {
            mUsbMsgThread.interrupt();           //结束 线程
        }

        unregisterReceiver(mUsbReceiver);

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)// 程序按返回键退出处理
    {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // 双击退出
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


}
