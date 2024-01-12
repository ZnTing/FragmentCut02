package domain.data;

import domain.constraint.Constraints;
import service.utils.Utils;

/**
 * @auther 朱振霆~
 */
public class CrewFragmentTask extends TrainNumAndMetroNum {
    private Constraints constraint;
    /**
     * 片段编号
     */
    private int crewFragmentTaskNum;
    /**
     * 默认按照时间顺序排序
     */
    private int order;

    /**
     * 片段运行里程
     */
    private double distance;
    /**
     * 任务开始时间
     */
    private int intStartTime;
    private String strStartTime;
    /**
     * 任务结束时间
     */
    private int intEndTime;
    private String strEndTime;
    /**
     * 持续时间（结束-开始）
     */
    private int duration;
    /**
     * 任务开始车站
     */
    private String startStation;
    /**
     * 任务结束车站
     */
    private String endStation;
    /**
     * 是否为出库片段
     */
    private boolean outDepot;
    /**
     * 是否为入库片段
     */
    private boolean inDepot;
    /**
     * 是否有折返站
     */
    private boolean turnBackTask;
    /**
     * 折返站之后的车次号
     */
    private String turnBackStation;
    /**
     * 折返的站名
     */
    private String metroNumAfterTurnBackStation;
    /**
     * 折返开始时间
     */
    private int intTurnBackStartTime;
    private String strTurnBackStartTime;
    /**
     * 折返结束时间
     */
    private int intTurnBackEndTime;
    private String strTurnBackEndTime;

    public CrewFragmentTask(Constraints constraint) {
        this.constraint = constraint;
        this.turnBackTask = false;
        this.outDepot = false;
        this.inDepot = false;
    }

    public Constraints getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraints constraint) {
        this.constraint = constraint;
    }

    public int getCrewFragmentTaskNum() {
        return crewFragmentTaskNum;
    }

    public void setCrewFragmentTaskNum(int crewFragmentTaskNum) {
        this.crewFragmentTaskNum = crewFragmentTaskNum;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIntStartTime() {
        return intStartTime;
    }

    public void setIntStartTime(int intStartTime) {
        this.intStartTime = intStartTime;
        this.strStartTime = Utils.intToStrTime(intStartTime);
    }

    public String getStrStartTime() {
        return strStartTime;
    }

    public void setStrStartTime(String strStartTime) {
        this.strStartTime = strStartTime;
    }

    public int getIntEndTime() {
        return intEndTime;
    }

    public void setIntEndTime(int intEndTime) {
        this.intEndTime = intEndTime;
        this.strEndTime = Utils.intToStrTime(intEndTime);
    }

    public String getStrEndTime() {
        return strEndTime;
    }

    public void setStrEndTime(String strEndTime) {
        this.strEndTime = strEndTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public boolean getOutDepot() {
        return outDepot;
    }

    public void setOutDepot(boolean outDepot) {
        this.outDepot = outDepot;
    }

    public boolean getInDepot() {
        return inDepot;
    }

    public void setInDepot(boolean inDepot) {
        this.inDepot = inDepot;
    }

    public boolean isTurnBackTask() {
        return turnBackTask;
    }

    public void setTurnBackTask(boolean turnBackTask) {
        this.turnBackTask = turnBackTask;
    }

    public String getTurnBackStation() {
        return turnBackStation;
    }

    public void setTurnBackStation(String turnBackStation) {
        this.turnBackStation = turnBackStation;
    }

    public String getMetroNumAfterTurnBackStation() {
        return metroNumAfterTurnBackStation;
    }

    public void setMetroNumAfterTurnBackStation(String metroNumAfterTurnBackStation) {
        this.metroNumAfterTurnBackStation = metroNumAfterTurnBackStation;
        if (metroNumAfterTurnBackStation != null) {
            this.turnBackTask = true;
        }
    }

    public int getIntTurnBackStartTime() {
        return intTurnBackStartTime;
    }

    public void setIntTurnBackStartTime(int intTurnBackStartTime) {
        this.intTurnBackStartTime = intTurnBackStartTime;
        this.strTurnBackStartTime = Utils.intToStrTime(intTurnBackStartTime);
    }

    public String getStrTurnBackStartTime() {
        return strTurnBackStartTime;
    }

    public void setStrTurnBackStartTime(String strTurnBackStartTime) {
        this.strTurnBackStartTime = strTurnBackStartTime;
    }

    public int getIntTurnBackEndTime() {
        return intTurnBackEndTime;
    }

    public void setIntTurnBackEndTime(int intTurnBackEndTime) {
        this.intTurnBackEndTime = intTurnBackEndTime;
        this.strTurnBackEndTime = Utils.intToStrTime(intTurnBackEndTime);
    }

    public String getStrTurnBackEndTime() {
        return strTurnBackEndTime;
    }

    public void setStrTurnBackEndTime(String strTurnBackEndTime) {
        this.strTurnBackEndTime = strTurnBackEndTime;
    }
}
