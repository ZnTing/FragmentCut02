package service.impl;

import com.sun.deploy.cache.CacheEntry;
import domain.constraint.Constraints;
import domain.data.*;
import service.utils.SingleTrainTaskComparator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @auther 朱振霆~
 */
public class CrewFragmentCutting {
    private int crewFragmentTaskNum = 0;
    private List<CrewFragmentTask> crewFragmentTasks;//乘务片段列表
    private List<LineInformationTableRowData> lineInfoData; //线路信息列表
    private String trainDiagramNo;
    private List<MetroNumTask> metroNumTasks;
    private Constraints constraint;
    //折返站的Map
    private final Map<Integer, TurnBackState> turnBackStateMap1 = new HashMap<>(400);
//    private List<TurnBackState> turnBackStateList = new ArrayList<>();

    /**
     * 通过数据库导入数据的构造器
     *
     * @param trainDiagramNo
     * @param lineInfoData
     * @param metroNumTasks
     * @param constraint
     */
    public CrewFragmentCutting(String trainDiagramNo, List<MetroNumTask> metroNumTasks, List<LineInformationTableRowData> lineInfoData, Constraints constraint) {
        this.lineInfoData = lineInfoData;
        this.trainDiagramNo = trainDiagramNo;
        this.constraint = constraint;
        this.crewFragmentTasks = generateCrewFragmentList(metroNumTasks, lineInfoData);
        //测试完整性验证
        CheckMethod checkMethod = new CheckMethod();
        crewFragmentTasks = checkMethod.deleteCrewFragment(crewFragmentTasks);
        checkMethod.integrityTesting(crewFragmentTasks);
    }


    public List<CrewFragmentTask> generateCrewFragmentList(List<MetroNumTask> metroNumTasks, List<LineInformationTableRowData> lineInfoData) {
        //出入段对应所需时间
        Map<String, Integer> inOutDepotTime = constraint.getInOutDepotTime();
        //线路信息
        Map<String, LineInformationTableRowData> lineInfoMap = lineInfoData.stream().collect(Collectors.toMap(LineInformationTableRowData::getStationName, (e) -> e));
        // 连接车辆段的车站名->连接的车辆段名
        Map<String, String> stationToDepotName = lineInfoData.stream().filter(LineInformationTableRowData::getConnectingDepot).collect(Collectors.toMap(LineInformationTableRowData::getStationName, LineInformationTableRowData::getConnectingDepotName));
        //轮乘点
        List<LineInformationTableRowData> upRotationPoint = lineInfoData.stream().filter(LineInformationTableRowData::getUpRotationPoint).collect(Collectors.toList());
        List<LineInformationTableRowData> downRotationPoint = lineInfoData.stream().filter(LineInformationTableRowData::getDownRotationPoint).collect(Collectors.toList());
        Map<String, LineInformationTableRowData> rotationPoint = lineInfoData.stream().filter((e) -> e.getUpRotationPoint() || e.getDownRotationPoint()).collect(Collectors.toMap(LineInformationTableRowData::getStationName, (e) -> e));
        //时刻表数据,按照车底号分类
        Map<String, List<MetroNumTask>> trainToMetroNumTasksMap = metroNumTasks.stream().collect(Collectors.groupingBy(MetroNumTask::getTrainNum));

        //实现按车底分类
        SingleTrainTask singleTrainTask;
        List<SingleTrainTask> singleTrainTasks = new ArrayList<>();
        for (Map.Entry<String, List<MetroNumTask>> entry : trainToMetroNumTasksMap.entrySet()) {
            String k = entry.getKey();
            List<MetroNumTask> v = entry.getValue();
            singleTrainTask = new SingleTrainTask();
            singleTrainTask.setTrainNum(k);
            singleTrainTask.getMetroNumTasks().addAll(v);
            singleTrainTasks.add(singleTrainTask);
        }
        //singleTrainTasks 列表中的每个 SingleTrainTask 对象执行 init() 方法
        for (SingleTrainTask task : singleTrainTasks) {
            task.init01();//实现按车底分类
        }
        // 添加元素到List中，按照车底大小排序
        Collections.sort(singleTrainTasks, new SingleTrainTaskComparator());
        //乘务片段划分
        CrewFragmentTask crewFragmentTask;
        crewFragmentTasks = new ArrayList<>();
        int rotationPointCount;
        int startIndex;
        int endIndex;
        int nextIndex;
        boolean inAndOutDepotFlag;
        for (SingleTrainTask trainTask : singleTrainTasks) {
            rotationPointCount = 0;
            startIndex = 0;
            inAndOutDepotFlag = true;
            //for (ArrivalStationTime timeList : trainTask.getArrivalSchedule()) {
            //每个车底对应的时刻表
            List<ArrivalStationTime> arrivalStationTimeList = trainTask.getArrivalSchedule();
            // 定义标签名为outer:
            for (int i = 0; i < arrivalStationTimeList.size() - 1; i++) {
                nextIndex = i + 1;
                //防止空指针
                int j = 0;
                if (arrivalStationTimeList.get(nextIndex) != null) {
                    j = nextIndex;
                }
                int end = 0;
                if (arrivalStationTimeList.get(arrivalStationTimeList.size() - 1) != null) {
                    end = arrivalStationTimeList.size() - 1;
                }
                //是否是轮乘点
                boolean isRotationPoint = rotationPoint.containsKey(arrivalStationTimeList.get(i).getStationName());
                //首车站是否包含车辆段名
                boolean isStationToDepotName = stationToDepotName.containsKey(arrivalStationTimeList.get(startIndex).getStationName());//是否是轮乘点
                //尾车站是否包含车辆段名
                boolean isEndStationToDepotName = stationToDepotName.containsKey(arrivalStationTimeList.get(end).getStationName());//是否是轮乘点
                //=======出段===========
                if (inAndOutDepotFlag && isStationToDepotName) {
                    if (!isRotationPoint) {
                        continue;
                    }
                    String tempDepotName = stationToDepotName.get(arrivalStationTimeList.get(startIndex).getStationName());
                    int tempStartTime = arrivalStationTimeList.get(startIndex).getIntEnterTime() - inOutDepotTime.get(tempDepotName);
                    crewFragmentTask = new CrewFragmentTask(constraint);
                    crewFragmentTask.setCrewFragmentTaskNum(crewFragmentTaskNum++);
                    crewFragmentTask.setTrainNum(trainTask.getTrainNum()); // 车底号
                    crewFragmentTask.setMetroNum(arrivalStationTimeList.get(i).getMetroNum());
                    crewFragmentTask.setStartStation(tempDepotName);
                    crewFragmentTask.setIntStartTime(tempStartTime);
                    crewFragmentTask.setEndStation(arrivalStationTimeList.get(i).getStationName());
                    crewFragmentTask.setIntEndTime(arrivalStationTimeList.get(i).getIntEnterTime());
                    crewFragmentTask.setDuration(crewFragmentTask.getIntEndTime() - crewFragmentTask.getIntStartTime());
                    crewFragmentTask.setOutDepot(true);
                    crewFragmentTasks.add(crewFragmentTask);
                    rotationPointCount++;
                    startIndex = i;
                    inAndOutDepotFlag = false;
                }
                //=========上行=========
                if (lineInfoMap.get(arrivalStationTimeList.get(i).getStationName()).getStationNum() > lineInfoMap.get(arrivalStationTimeList.get(j).getStationName()).getStationNum()) {
                    for (LineInformationTableRowData up : upRotationPoint) {
                        if (!up.getStationName().equals(arrivalStationTimeList.get(j).getStationName())) {
                            continue;
                        }
                        endIndex = j;
                        crewFragmentTask = getCrewFragmentTask(startIndex, trainTask, arrivalStationTimeList, startIndex);
                        crewFragmentTask.setEndStation(arrivalStationTimeList.get(endIndex).getStationName());
                        crewFragmentTask.setIntEndTime(arrivalStationTimeList.get(endIndex).getIntEnterTime());
                        crewFragmentTask.setDuration(crewFragmentTask.getIntEndTime() - crewFragmentTask.getIntStartTime());
                        crewFragmentTasks.add(crewFragmentTask);
                        startIndex = endIndex;
                    }
                }
                //=========下行=========
                if (lineInfoMap.get(arrivalStationTimeList.get(i).getStationName()).getStationNum() < lineInfoMap.get(arrivalStationTimeList.get(j).getStationName()).getStationNum()) {
                    for (LineInformationTableRowData down : downRotationPoint) {
                        if (!down.getStationName().equals(arrivalStationTimeList.get(j).getStationName())) {
                            continue;
                        }
                        endIndex = j;
                        crewFragmentTask = getCrewFragmentTask(startIndex, trainTask, arrivalStationTimeList, startIndex);
                        crewFragmentTask.setEndStation(arrivalStationTimeList.get(endIndex).getStationName());
                        crewFragmentTask.setIntEndTime(arrivalStationTimeList.get(endIndex).getIntEnterTime());
                        crewFragmentTask.setDuration(crewFragmentTask.getIntEndTime() - crewFragmentTask.getIntStartTime());
                        crewFragmentTasks.add(crewFragmentTask);
                        startIndex = endIndex;
                    }
                }
                //=======入段===========
                if (!inAndOutDepotFlag && isEndStationToDepotName && j == end) {
                    String tempDepotName = stationToDepotName.get(arrivalStationTimeList.get(end).getStationName());
                    int tempEndTime = arrivalStationTimeList.get(end).getIntEnterTime() + inOutDepotTime.get(tempDepotName);
                    crewFragmentTask = getCrewFragmentTask(startIndex, trainTask, arrivalStationTimeList, i);
                    crewFragmentTask.setEndStation(tempDepotName);
                    crewFragmentTask.setIntEndTime(tempEndTime);
                    crewFragmentTask.setDuration(crewFragmentTask.getIntEndTime() - crewFragmentTask.getIntStartTime());
                    crewFragmentTask.setInDepot(true);
                    crewFragmentTasks.add(crewFragmentTask);
                }
                //=========折返站==========
                if (arrivalStationTimeList.get(i).getStationName().equals(arrivalStationTimeList.get(j).getStationName())
                        && arrivalStationTimeList.get(i).getIntEnterTime() != arrivalStationTimeList.get(j).getIntEnterTime()) {
//                    turnBackStateList.add(new TurnBackState(arrivalStationTimeList.get(j).getMetroNum(),
//                            arrivalStationTimeList.get(j).getStationName(),
//                            arrivalStationTimeList.get(i).getIntEnterTime(),
//                            arrivalStationTimeList.get(j).getIntExitTime()));
//                    for (CrewFragmentTask task : crewFragmentTasks) {
//                        if (task.getMetroNum().equals(arrivalStationTimeList.get(j).getMetroNum())) {
//                            task.setMetroNumAfterTurnBackStation(arrivalStationTimeList.get(j).getStationName());
//                            task.setTurnBackStation(arrivalStationTimeList.get(j).getMetroNum());
//                            task.setIntTurnBackStartTime(arrivalStationTimeList.get(i).getIntEnterTime());
//                            task.setIntTurnBackEndTime(arrivalStationTimeList.get(j).getIntExitTime());
//                        }
//                    }
                    turnBackStateMap1.put(crewFragmentTasks.size(),
                            new TurnBackState(arrivalStationTimeList.get(j).getMetroNum(),
                                    arrivalStationTimeList.get(j).getStationName(),
                                    arrivalStationTimeList.get(i).getIntEnterTime(),
                                    arrivalStationTimeList.get(j).getIntExitTime()));

//                    // 把记录中得折返站设置到片段内
//                    for (Map.Entry<String, TurnBackState> turnBackStateEntry : turnBackStateMap1.entrySet()) {
//                        String turnBackKey = turnBackStateEntry.getKey();
//                        TurnBackState turnBackValue = turnBackStateEntry.getValue();
//                        crewFragmentTasks.get(i).setMetroNumAfterTurnBackStation(turnBackStateMap1.get(turnBackKey).getStationName());
//                        crewFragmentTasks.get(i).setTurnBackStation(turnBackStateMap1.get(turnBackKey).getMetroNum());
//                        crewFragmentTasks.get(i).setIntTurnBackStartTime(turnBackStateMap1.get(turnBackKey).getStartTime());
//                        crewFragmentTasks.get(i).setIntTurnBackEndTime(turnBackStateMap1.get(turnBackKey).getEndTime());
//                    }
                }
            }
        }
        for (Map.Entry<Integer, TurnBackState> entry : turnBackStateMap1.entrySet()) {
            Integer key = entry.getKey();
            TurnBackState value = entry.getValue();
            CrewFragmentTask task = crewFragmentTasks.get(key);
            task.setMetroNumAfterTurnBackStation(value.getStationName());
            task.setTurnBackStation(value.getMetroNum());
            task.setIntTurnBackStartTime(value.getStartTime());
            task.setIntTurnBackEndTime(value.getEndTime());
        }
        return crewFragmentTasks;
    }

    /**
     * 划分片段
     *
     * @param startIndex
     * @param trainTask
     * @param arrivalStationTimeList
     * @param i
     * @return
     */
    private CrewFragmentTask getCrewFragmentTask(int startIndex, SingleTrainTask trainTask, List<ArrivalStationTime> arrivalStationTimeList, int i) {
        CrewFragmentTask crewFragmentTask;
        crewFragmentTask = new CrewFragmentTask(constraint);
        crewFragmentTask.setCrewFragmentTaskNum(crewFragmentTaskNum++);
        crewFragmentTask.setTrainNum(trainTask.getTrainNum()); // 车底号
        crewFragmentTask.setMetroNum(arrivalStationTimeList.get(i).getMetroNum());
        crewFragmentTask.setStartStation(arrivalStationTimeList.get(startIndex).getStationName());
        crewFragmentTask.setIntStartTime(arrivalStationTimeList.get(startIndex).getIntExitTime());
        return crewFragmentTask;
    }
}