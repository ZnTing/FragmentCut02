package domain.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther 朱振霆~
 */
public class MetroNumTask extends TrainNumAndMetroNum{

    /**
     * 到站时刻表
     */
    List<ArrivalStationTime> arrivalSchedule;

    public MetroNumTask(String metroNum) {
        super(metroNum);
    }

    public MetroNumTask() {

    }

    public void init(){
        arrivalSchedule.forEach((e) -> e.setMetroNum(getMetroNum()));
    }

//    public MetroNumTask(String trainNum, String metroNum, List<ArrivalStationTime> arrivalSchedule) {
//        this.setTrainNum(trainNum);
//        this.setMetroNum(metroNum);
//        this.arrivalSchedule = arrivalSchedule;
//    }

    public List<ArrivalStationTime> getArrivalSchedule() {
        return arrivalSchedule;
    }

    public void setArrivalSchedule(List<ArrivalStationTime> arrivalSchedule) {
        this.arrivalSchedule = arrivalSchedule;
    }
}
