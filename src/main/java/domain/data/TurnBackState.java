package domain.data;

import lombok.Data;

/**
 * @auther 朱振霆~
 */
public class TurnBackState extends Station{
    /**
     * 折返开始时间
     */
    private int startTime;
    /**
     * 折返结束时间
     */
    private int endTime;


    public TurnBackState(String metroNum, String stationName, int startTime, int endTime) {
        super(metroNum,stationName);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
