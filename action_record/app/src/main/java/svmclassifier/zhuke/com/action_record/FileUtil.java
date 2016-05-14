package svmclassifier.zhuke.com.action_record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 此类是Android手机文件相关选项的工具类，初始化应用的相关文件以及文件夹
 *
 * @author ZHUKE
 *
 */
public class FileUtil {

    /**
     * 将字符串添加到文件末尾
     *
     * @param filePath
     *            添加到的文件的文件路径
     * @param addStr
     *            待添加的字符串值
     */
    public static void addStr2File(String filePath, String addStr) {
        try {
            BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath),true)));
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

        File tempFile = new File(SVMConfig.action_f);
        try {
            // 初始化应用文件夹
            File dirFile = new File(SVMConfig.appDir);
            // 如果这是一个文件夹且不存在的话，则进行新建文件夹
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
