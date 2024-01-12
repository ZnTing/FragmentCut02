package domain.data;

/**
 * @auther 朱振霆~
 */
public class TrainNumAndMetroNum {
    /**
     * 车底号
     */
    private String trainNum;
    /**
     * 车次号
     */
    private String metroNum;

    public TrainNumAndMetroNum() {
    }
    public TrainNumAndMetroNum(String metroNum) {
        this.metroNum = metroNum;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    public String getMetroNum() {
        return metroNum;
    }

    public void setMetroNum(String metroNum) {
        this.metroNum = metroNum;
    }
}
