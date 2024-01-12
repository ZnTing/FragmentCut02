package domain.constraint;

import domain.constant.Constant;
import lombok.SneakyThrows;
import service.utils.Utils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Constraints implements Serializable {
    /**
     * 前端输入的动态约束（不同班次和休息约束）key-班次名 value-对应约束
     */
//    private Map<String, ShiftConstraint> shiftConstraintMap;
    /**
     * 需要前端输入的静态参数
     */
//    private StaticTimeConstraints staticTimeConstraints;
    /**
     * 出退勤地点
     */
    private List<OnAndOffDutyStation> onAndOffDutyStations;
    /**
     * 出入段时间
     */
    private Map<String, Integer> inOutDepotTime;

    public Constraints() {
    }

    public Constraints( List<OnAndOffDutyStation> onAndOffDutyStations, Map<String, Integer> inOutDepotTime) {
        this.onAndOffDutyStations = onAndOffDutyStations;
        this.inOutDepotTime = inOutDepotTime;
    }

    /**
     * 约束处理方法
     */
    @SneakyThrows
//    private void checkShiftConstraintValue(Map<String, ShiftConstraint> shiftConstraintMap) {
//        for (Map.Entry<String, ShiftConstraint> sce : shiftConstraintMap.entrySet()) {
//            ShiftConstraint value = sce.getValue();
//            String key = sce.getKey();
//            ShiftConstraint constraint = Utils.makeStringShiftConstraintMap().get(key);
//
//            value.setReverse(constraint.getReverse());
//            value.setOffDutyTaskLimit(constraint.getOffDutyTaskLimit());
//            value.setReferWorkDuring(constraint.getReferWorkDuring());
//
//            if ("夜班".equals(key)) {
//                Integer startPickUpTime = value.getStartPickUpTime();
//                if (startPickUpTime > 0){
//                    value.setStartPickUpTime(-value.getLatestOffDutyTime());
//                    value.setOffDutyTime(-startPickUpTime);
//                    value.setLatestOffDutyTime(-startPickUpTime);
//                    value.setNumOfOvertime(constraint.getNumOfOvertime());
//                    value.setEndPickUpTime(constraint.getEndPickUpTime());
//                    // 把一个特殊的超出夜班的休息时间放进夜班的约束里面
//                    value.getRestTimeConstraints().add(constraint.getRestTimeConstraints().stream()
//                            .filter(a -> "特殊".equals(a.getDepotName()))
//                            .findFirst()
//                            .orElse(null));
//                }
//            }
//        }
//    }

//    public Map<String, ShiftConstraint> getShiftConstraintMap() {
//        return shiftConstraintMap;
//    }
//
//    public void setShiftConstraintMap(Map<String, ShiftConstraint> shiftConstraintMap) {
//        if (shiftConstraintMap != null){
//            checkShiftConstraintValue(shiftConstraintMap);
//        }
//        this.shiftConstraintMap = shiftConstraintMap;
//    }
//
//    public StaticTimeConstraints getStaticTimeConstraints() {
//        return staticTimeConstraints;
//    }
//
//    public void setStaticTimeConstraints(StaticTimeConstraints staticTimeConstraints) {
//        this.staticTimeConstraints = staticTimeConstraints;
//    }

    public List<OnAndOffDutyStation> getOnAndOffDutyStations() {
        return onAndOffDutyStations;
    }

    public void setOnAndOffDutyStations(List<OnAndOffDutyStation> onAndOffDutyStations) {
        this.onAndOffDutyStations = onAndOffDutyStations;
        if (onAndOffDutyStations != null){
            this.onAndOffDutyStations.sort(Comparator.comparing(a -> a.getStartWorkStation().stream().noneMatch(b -> b.contains(Constant.CC))));
        }
    }

    public Map<String, Integer> getInOutDepotTime() {
        return inOutDepotTime;
    }

    public void setInOutDepotTime(Map<String, Integer> inOutDepotTime) {
        this.inOutDepotTime = inOutDepotTime;
    }
}
