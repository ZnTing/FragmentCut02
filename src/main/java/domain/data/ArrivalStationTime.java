package domain.data;

import lombok.Data;
import service.utils.Utils;

/**
 * @auther 朱振霆~
 */

public class ArrivalStationTime extends Station{

    /**
     * 进站时间 int
     */
    private int intEnterTime;
    /**
     * 出站时间 int
     */
    private int intExitTime;
    /**
     * 进站时间 string
     */
    private String strEnterTime;
    /**
     * 出站时间 string
     */
    private String strExitTime;

    public ArrivalStationTime(String metroNum, String stationName) {
        super(metroNum, stationName);
    }

    public ArrivalStationTime() {

    }

    public int getIntEnterTime() {
        return intEnterTime;
    }

    public void setIntEnterTime(int intEnterTime) {
        this.intEnterTime = intEnterTime;
    }

    public int getIntExitTime() {
        return intExitTime;
    }

    public void setIntExitTime(int intExitTime) {
        this.intExitTime = intExitTime;
    }

    public String getStrEnterTime() {
        return strEnterTime;
    }

    public void setStrEnterTime(String strEnterTime) {
        this.strEnterTime = strEnterTime;
    }

    public String getStrExitTime() {
        return strExitTime;
    }

    public void setStrExitTime(String strExitTime) {
        this.strExitTime = strExitTime;
    }
    public void startAndIntEnterTime(String strEnterTime) {
        // 通过输入String类型的时间，同时初始化strEnterTime以及intEnterTime，小于凌晨2点的自动加一天
        this.strEnterTime = strEnterTime;
        this.intEnterTime = Utils.strToIntTime(strEnterTime);
    }
    public void startAndIntExitTime(String strExitTime) {
        this.strExitTime = strExitTime;
        this.intExitTime = Utils.strToIntTime(strExitTime);;
    }

}
