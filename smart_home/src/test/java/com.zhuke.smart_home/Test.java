package com.zhuke.smart_home;

import gnu.io.SerialPort;

import java.util.Observer;
import java.util.*;

public class Test implements Observer {

    SerialReader sr = new SerialReader();

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        openSerialPort("COM4", "38400", ""); //打开串口。
    }

    public void update(Observable o, Object arg) {
        String mt = new String((byte[]) arg);
        System.out.println("得到串口数据\n" + mt); //串口数据
        System.out.println("-----------------"+mt.length());
    }

    /**
     * 往串口发送数据,实现双向通讯.
     */
    public void send(String message) {
        Test test = new Test();
        test.openSerialPort("COM4", "38400", message);
    }

    /**
     * 打开串口
     */
    public void openSerialPort(String port, String rate, String message) {
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();
        String dataBit = "" + SerialPort.DATABITS_8;
        String stopBit = "" + SerialPort.STOPBITS_1;
        String parity = "" + SerialPort.PARITY_NONE;
        int parityInt = SerialPort.PARITY_NONE;
        params.put(SerialReader.PARAMS_PORT, port); // 端口名称
        params.put(SerialReader.PARAMS_RATE, rate); // 波特率
        params.put(SerialReader.PARAMS_DATABITS, dataBit); // 数据位
        params.put(SerialReader.PARAMS_STOPBITS, stopBit); // 停止位
        params.put(SerialReader.PARAMS_PARITY, parityInt); // 无奇偶校验
        params.put(SerialReader.PARAMS_TIMEOUT, 100); // 设备超时时间 1秒
        params.put(SerialReader.PARAMS_DELAY, 100); // 端口数据准备时间 1秒
        try {
            sr.open(params);
            sr.addObserver(this);
            if (message != null && message.length() != 0) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String Bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }


    public String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }
} 
 
