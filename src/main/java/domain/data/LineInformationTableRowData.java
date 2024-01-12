package domain.data;

/**
 * @auther 朱振霆~
 */
public class LineInformationTableRowData extends Station{

    /**
     * 以鱼洞站为起点的距离
     */
    private int distance;
    /**
     * 车站序号
     */
    private int stationNum;
    /**
     * 是否连接车辆段
     */
    private boolean isConnectingDepot;
    /**
     * 连接车辆段名
     */
    private String connectingDepotName;
    /**
     * 是否是上行轮乘点
     */
    private boolean isUpRotationPoint;
    /**
     * 是否是下行轮乘点
     */
    private boolean isDownRotationPoint;
    /**
     * 是否为折返站
     */
    private boolean isTurnBackStation;

    /**
     * 表格中为1，返回true
     */
    private boolean strToBoolean(String param) {
        return "1".equals(param);
    }

    public LineInformationTableRowData() {
        this.isConnectingDepot = false;
        this.isUpRotationPoint = false;
        this.isDownRotationPoint = false;
        this.isTurnBackStation = false;
    }

    public LineInformationTableRowData(String stationName, int distance, int stationNum, boolean isConnectingDepot, boolean isUpRotationPoint, boolean isDownRotationPoint, boolean isTurnBackStation) {
        this.setStationName(stationName);
        this.distance = distance;
        this.stationNum = stationNum;
        this.isConnectingDepot = isConnectingDepot;
        this.isUpRotationPoint = isUpRotationPoint;
        this.isDownRotationPoint = isDownRotationPoint;
        this.isTurnBackStation = isTurnBackStation;
    }



    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getStationNum() {
        return stationNum;
    }

    public void setStationNum(int stationNum) {
        this.stationNum = stationNum;
    }

    public boolean getConnectingDepot() {
        return isConnectingDepot;
    }

    public void setConnectingDepot(boolean connectingDepot) {
        isConnectingDepot = connectingDepot;
    }

    public boolean getUpRotationPoint() {
        return isUpRotationPoint;
    }

    public void setUpRotationPoint(String upRotationPoint) {
        isUpRotationPoint = strToBoolean(upRotationPoint);
    }

    public boolean getDownRotationPoint() {
        return isDownRotationPoint;
    }

    public void setDownRotationPoint(String downRotationPoint) {
        isDownRotationPoint = strToBoolean(downRotationPoint);
    }

    public boolean isTurnBackStation() {
        return isTurnBackStation;
    }

    public void setTurnBackStation(String turnBackStation) {
        isTurnBackStation = strToBoolean(turnBackStation);
    }

    public String getConnectingDepotName() {
        return connectingDepotName;
    }

    public void setConnectingDepotName(String connectingDepotName) {
        this.connectingDepotName = connectingDepotName;
        processConnectingDepotName();
    }
    private void processConnectingDepotName() {
        if (connectingDepotName != null && !"".equals(connectingDepotName)) {
            this.isConnectingDepot = true;
        }
    }


//    public void setConnectingDepotName(String connectingDepotName) {
//        if (connectingDepotName != null && !"".equals(connectingDepotName)){
//            this.connectingDepotName = connectingDepotName;
//            this.isConnectingDepot = true;
//        }
//    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
