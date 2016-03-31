package com.zhuke.svmclassifier.util;

import java.util.StringTokenizer;

/**
 * 数据预处理
 * Created by ZHUKE on 2016/3/31.
 */
public class DataProcessing {


    /**
     * 对某时刻手环的状态信息进行格式化和归一化处理
     *
     * @param s 手环的状态信息，数据为一个字符串，格式为-0.00308228 -0.00506592 -0.309479 0.349579 0.159607 0.083252
     * @return 将各动作量进行归一化处理后，存进一个double数组中，返回该double数组
     */
    public static double[] normalization(String s) {
        // 数据以空格进行分割
        StringTokenizer str = new StringTokenizer(s, " ");
        int len = str.countTokens();
        double[] d = new double[len];
        if (s != null) {
            // 按照数据格式进行个格式化处理
            for (int i = 0; i < len; i++) {
                double temp = Double.parseDouble(str.nextToken());
                //将数据进行归一化处理
                temp = (temp + 32768) / 32768 - 1;
                d[i] = temp;
            }
        }
        return d;

    }


    /**
     * 数据预处理
     *
     * @param input 传入的数据矩阵
     * @param r     行
     * @param c     列
     * @return
     */
    public double[] dataProcessing(double[][] input, int r, int c) {

        // 二维数组的前三列为角度，后三列为加速度
        double[] d = new double[12];

        // 进行加速度提取
        double[] d1 = acceleration(input, r, c, 2);

        // 进行角度提取
        double[] d2 = angle(input, r, 3);

        // 进行rms提取
        double[] d3 = rms(input, r, c);

        // rms+angle+acc
        for (int i = 0; i < 12; i++) {
            int j, k, l;
            for (j = 0; j < d3.length; j++) {
                d[i] = d3[j];
                i++;
            }
            for (k = 0; k < d2.length; k++) {
                d[i] = d2[k];
                i++;
            }
            for (l = 0; l < d1.length; l++) {
                d[i] = d1[l];
                i++;
            }
        }

        return d;
    }

    /**
     * 加速度处理
     *
     * @param input 传入的数据矩阵
     * @param r     行
     * @param c     列
     * @return
     */
    public double[] acceleration(double[][] input, int r, int c, int beginC) {

        double[] d = new double[c];
        for (int i = 0; i < c; i++) {
            double temp = 0;
            for (int j = beginC; j < r; j++) {
                temp = temp + input[j][i]
                        * (Math.pow(((double) (j + 1)) / 10, 2));
            }
            d[i] = temp;
        }
        return d;
    }


    /**
     * 角度处理
     *
     * @param input 传入的数据矩阵
     * @param r     行
     * @param c     列
     * @return
     */
    public double[] angle(double[][] input, int r, int c) {

        double[] d = new double[c];
        for (int i = 0; i < c; i++) {
            double temp = 0;
            for (int j = 0; j < r; j++) {
                temp = temp + input[j][i];
            }
            d[i] = temp;
        }
        return d;
    }


    /**
     * 均方根
     *
     * @param input 传入的数据矩阵
     * @param r     行
     * @param c     列
     * @return
     */
    public double[] rms(double[][] input, int r, int c) {

        double[] d = new double[c];
        for (int i = 0; i < c; i++) {
            double temp = 0;
            for (int j = 0; j < r; j++) {
                temp = temp + input[j][i] * input[j][i];
            }
            d[i] = Math.sqrt(temp / r);
        }
        return d;
    }
}
