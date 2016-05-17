package com.zhuke.svmclassifier.util;

/**
 * Created by ZHUKE on 2016/4/18.
 */
public class ArrayUtil {
    /*public static void updateToPredictArray() {
        if (SVMConfig.TEMP_STATE < SVMConfig.R - SVMConfig.L + SVMConfig.NOISE) {
            //此时需要取历史数据
            int count = 0;
            for (int i = 0, j = SVMConfig.TEMP_STATE; j >= SVMConfig.NOISE; i++, j--) {
                SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                count++;
            }
            for (int i = SVMConfig.ACTION_TO_RECORD, j = count; count < SVMConfig.R - SVMConfig.L; i--, j++) {
                SVMConfig.TO_PERDICT[count] = SVMConfig.ACTION_TEMP[i - 1];
                count++;
            }
        } else {
            //直接存取
            for (int i = 0, j = SVMConfig.TEMP_STATE; i < SVMConfig.R - SVMConfig.L; i++, j--) {
                SVMConfig.TO_PERDICT[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
            }
        }
    }

    public static void updateToLearnArray() {
        if (SVMConfig.TEMP_STATE < SVMConfig.R - SVMConfig.L + SVMConfig.NOISE) {
            //此时需要取历史数据
            int count = 0;
            for (int i = 0, j = SVMConfig.TEMP_STATE; j >= SVMConfig.NOISE; i++, j--) {
                SVMConfig.TO_LEARN[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
                count++;
            }
            for (int i = SVMConfig.ACTION_TO_RECORD, j = count; count < SVMConfig.R - SVMConfig.L; i--, j++) {
                SVMConfig.TO_LEARN[count] = SVMConfig.ACTION_TEMP[i - 1];
                count++;
            }
        } else {
            //直接存取
            for (int i = 0, j = SVMConfig.TEMP_STATE; i < SVMConfig.R - SVMConfig.L; i++, j--) {
                SVMConfig.TO_LEARN[i] = SVMConfig.ACTION_TEMP[j - SVMConfig.NOISE];
            }
        }
    }*/

    public static boolean isZero(double[] d) {
        for (int i = 0; i < d.length; i++) {
            if (d[i] != 0.0) {
                return false;
            }
        }
        return true;
    }


    public static double[] subArray(double[] minuend, double[] meiosis) throws Exception {
        if (minuend.length != meiosis.length) {
            throw new Exception("数组格式错误");
        }
        double[] result = new double[minuend.length];
        for (int i = 0; i < minuend.length; i++) {
            result[i] = minuend[i] - meiosis[i];
        }
        return result;
    }

    public static double[] plusArray(double[] d1, double[] d2) throws Exception {
        if (d1.length != d2.length) {
            throw new Exception("数组格式错误");
        }
        double[] result = new double[d1.length];
        for (int i = 0; i < d1.length; i++) {
            result[i] = d1[i] + d2[i];
        }
        return result;
    }

    public static double[] muiltiArray(double d, double[] array) throws Exception {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] * d;
        }
        return result;
    }
}
