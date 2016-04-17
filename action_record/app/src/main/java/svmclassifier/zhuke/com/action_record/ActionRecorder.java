package svmclassifier.zhuke.com.action_record;

/**
 * 动作记录
 * Created by ZHUKE on 2016/4/17.
 */
public class ActionRecorder {
    public static void main(String[] args) {
        System.out.println(ActionRecorder.getCurrentAction());
    }

    private static float ax;
    private static float ay;
    private static float az;

    private static float oy;
    private static float oz;

    private static float gx;
    private static float gy;
    private static float gz;

    public static String getCurrentAction() {
        return ax + "," + ay + "," + az + "," + oy + "," + oz + "," + gx + "," + gy + "," + gz;
    }

    public static float getAx() {
        return ax;
    }

    public static void setAx(float ax) {
        ActionRecorder.ax = ax;
    }

    public static float getAy() {
        return ay;
    }

    public static void setAy(float ay) {
        ActionRecorder.ay = ay;
    }

    public static float getAz() {
        return az;
    }

    public static void setAz(float az) {
        ActionRecorder.az = az;
    }

    public static float getOy() {
        return oy;
    }

    public static void setOy(float oy) {
        ActionRecorder.oy = oy;
    }

    public static float getOz() {
        return oz;
    }

    public static void setOz(float oz) {
        ActionRecorder.oz = oz;
    }

    public static float getGx() {
        return gx;
    }

    public static void setGx(float gx) {
        ActionRecorder.gx = gx;
    }

    public static float getGy() {
        return gy;
    }

    public static void setGy(float gy) {
        ActionRecorder.gy = gy;
    }

    public static float getGz() {
        return gz;
    }

    public static void setGz(float gz) {
        ActionRecorder.gz = gz;
    }
}
