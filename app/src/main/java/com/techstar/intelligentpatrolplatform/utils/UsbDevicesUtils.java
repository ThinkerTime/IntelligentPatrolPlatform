package com.techstar.intelligentpatrolplatform.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

/**
 * author lrzg on 16/11/11.
 * 描述：USB工具类
 */

public class UsbDevicesUtils {

    private Context mContext;
    private UsbManager mUsbManager;
    private PendingIntent pendingIntent;
    private UsbDevice usbDevice;
    private UsbDeviceConnection connection;    //usb设备连接对象
    private UsbInterface intf;//usb接口对象
    private UsbEndpoint epOut, epIn;    //输入、输出 端点 对象


    public UsbDevicesUtils(Context context, UsbManager usbManager, PendingIntent pendingIntent) {
        this.mContext = context;
        this.mUsbManager = usbManager;
        this.pendingIntent = pendingIntent;

    }

    /**
     * 扫描USB设备
     *
     * @return UsbDevice
     */
    public UsbDevice ScanDevices() {
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
//            if (device.getVendorId() == 17224 && device.getProductId() == 21815) {
            if (device.getVendorId() == 0x0483 && device.getProductId() == 0x5710) {
                if (mUsbManager.hasPermission(device)) {  //判断是否有权限 使用usb设备
                    Toast.makeText(mContext, "有权限", Toast.LENGTH_LONG).show();
                } else {
                    //没有权限询问用户是否授予权限
                    Toast.makeText(mContext, "没有权限", Toast.LENGTH_LONG).show();
                    mUsbManager.requestPermission(device, pendingIntent); //该代码执行后，系统弹出一个对话框，
                    //询问用户是否授予程序操作USB设备的权限
                }
                return this.usbDevice = device;
            }
        }

        return null;
    }

    public int OpenDevice(UsbDevice mydevice) {

        connection = mUsbManager.openDevice(mydevice);//打开usb设备
        //USB设备打开失败
        if (connection == null) {
            return ErrorType.ERR_OPEN_DEVICE;
        } else {
            //USB设备获取接口数量失败
            if (mydevice.getInterfaceCount() != 1) {
                return ErrorType.ERR_INTERFACE_NUM;
            }
            //USB设备获取接口失败
            intf = mydevice.getInterface(0);
            if (intf == null) {
                return ErrorType.ERR_OPEN_INTERFACE;
            }

            //独占接口
            connection.claimInterface(intf, true);//独占接口

            //获取 endpoint
            int cnt = intf.getEndpointCount();//获取端点数
            if (cnt < 1) {
                return ErrorType.ERR_ENDPOINT_COUNT;
            }
//            Toast.makeText(mContext, "cnt:"+cnt, Toast.LENGTH_LONG).show();
            for (int index = 0; index < cnt; index++) {
                UsbEndpoint ep = intf.getEndpoint(index);
                if ((ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) && (ep.getDirection() == UsbConstants.USB_DIR_OUT)) {
                    epOut = ep;    //针对主机而言，从主机 输出到设备,获取到 bulk的输出端点 对象
//                    Toast.makeText(mContext, "epOut:"+epOut.getEndpointNumber(), Toast.LENGTH_LONG).show();
                }
                if ((ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) && (ep.getDirection() == UsbConstants.USB_DIR_IN)) {
                    epIn = ep;    //针对主机而言，从设备 输出到主机 ,获取到 bulk的输入端点 对象
//                    Toast.makeText(mContext, "epIn:"+epIn.getEndpointNumber(), Toast.LENGTH_LONG).show();
                }
            }

            if (epOut != null && epIn != null) {

                return ErrorType.ERR_OPEN_ENDPOINT_SUCCESS;
            } else {
                return ErrorType.ERR_OPEN_ENDPOINT;
            }

        }
    }

    public int USBWriteData( byte[] Outbuffer, int length, int timeout) {
        if (epOut == null) {
            return ErrorType.ERR_EPOUT_NULL;
        }
        return connection.bulkTransfer(epOut, Outbuffer, length, timeout); //发送bulk 数据给下位机
    }

    public int USBReadData(byte[] InBuffer, int length, int timeout) {
        if (epOut == null) {
            return ErrorType.ERR_EPIN_NULL;
        }
        return connection.bulkTransfer(epIn, InBuffer, length, timeout);//接收bulk数据
    }


}
