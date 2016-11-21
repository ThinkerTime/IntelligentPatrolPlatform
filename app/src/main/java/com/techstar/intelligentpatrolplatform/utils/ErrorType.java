package com.techstar.intelligentpatrolplatform.utils;

/**
 * author lrzg on 16/11/11.
 * 描述：USB类型错误列表
 */

public class ErrorType {
    public static int ERR_OPEN_DEVICE = 0;//USB设备打开失败
    public static int ERR_INTERFACE_NUM = 1;//USB设备获取接口数量失败
    public static int ERR_OPEN_INTERFACE = 2;//USB设备获取接口失败
    public static int ERR_ENDPOINT_COUNT = 3;//获取端点数数量
    public static int ERR_OPEN_SUCCESS = 4;//USB设备打开成功
    public static int ERR_OPEN_ENDPOINT = 5;//端点打开失败
    public static int ERR_OPEN_ENDPOINT_SUCCESS = 6;//端点打开成功
    public static int ERR_EPOUT_NULL = -1;//输出端口为空
    public static int ERR_EPIN_NULL = -2;//输入端口为空


    public static int ERR_INPUT_DATA_TOO_MUCH = -2;
    public static int ERR_INPUT_DATA_TOO_LESS = -3;
    public static int ERR_INPUT_DATA_ILLEGALITY = -4;
    public static int ERR_USB_WRITE_DATA = -5;
    public static int ERR_USB_READ_DATA = -6;
    public static int ERR_READ_NO_DATA = -7;
    public static int ERR_CLOSE_DEVICE = -9;
    public static int ERR_EXECUTE_CMD = -10;
    public static int ERR_SELECT_DEVICE = -11;
    public static int ERR_DEVICE_OPENED = -12;
    public static int ERR_DEVICE_NOTOPEN = -13;
    public static int ERR_BUFFER_OVERFLOW = -14;
    public static int ERR_DEVICE_NOTEXIST = -15;
    public static int ERR_LOAD_KERNELDLL = -16;
    public static int ERR_CMD_FAILED = -17;
    public static int ERR_BUFFER_CREATE = -18;
    public static int ERR_NO_PERMISSIONS = -19;
}
