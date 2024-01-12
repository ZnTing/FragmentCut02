package domain.constraint;

import java.util.ArrayList;
import java.util.List;

public class OnAndOffDutyStation {

    private List<String> startWorkStation;

    private List<String> endWorkStation;

    private Integer maxDriverNum; // 最大司机数

    public OnAndOffDutyStation() {
        startWorkStation = new ArrayList<>();
        endWorkStation = new ArrayList<>();
    }

    public List<String> getStartWorkStation() {
        return startWorkStation;
    }

    public void setStartWorkStation(List<String> startWorkStation) {
        this.startWorkStation = startWorkStation;
    }

    public List<String> getEndWorkStation() {
        return endWorkStation;
    }

    public void setEndWorkStation(List<String> endWorkStation) {
        this.endWorkStation = endWorkStation;
    }

    public Integer getMaxDriverNum() {
        return maxDriverNum;
    }

    public void setMaxDriverNum(Integer maxDriverNum) {
        this.maxDriverNum = maxDriverNum;
    }
}
