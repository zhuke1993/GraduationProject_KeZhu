package com.zhuke.svmclassifier.util;

import java.io.*;

/**
 * 此类是Android手机文件相关选项的工具类，初始化应用的相关文件以及文件夹
 *
 * @author Administrator
 */
public class FileUtil {

    /**
     * 将字符串添加到文件末尾
     *
     * @param filePath 添加到的文件的文件路径
     * @param addStr   待添加的字符串值
     */
    public static void addStr2File(String filePath, String addStr) {
        try {
            BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath), true)));
            bufw.append(addStr);
            bufw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化应用所需的相关文件以及文件夹
     *
     * @return
     */
    public static boolean iniFile() {


        return true;
    }

}
