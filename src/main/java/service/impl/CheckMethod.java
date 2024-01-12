package service.impl;

import domain.data.CrewFragmentTask;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auther 朱振霆~
 */
public class CheckMethod {
    /**
     * 测试完整性验证
     * @param crewFragmentTask
     * @return
     */
    public List<CrewFragmentTask> deleteCrewFragment(List<CrewFragmentTask> crewFragmentTask) {
        Map<String, List<CrewFragmentTask>> trainNumMap = crewFragmentTask.stream().collect(Collectors.groupingBy(CrewFragmentTask::getTrainNum));
        trainNumMap.values().forEach(tasks -> tasks.sort(Comparator.comparing(CrewFragmentTask::getIntStartTime)
                                .thenComparing(CrewFragmentTask::getIntEndTime)));
//        for (Map.Entry<String, List<CrewFragmentTask>> entry : trainNumMap.entrySet()) {
//            String key = entry.getKey();
//            List<CrewFragmentTask> tasks = entry.getValue();
//            tasks.remove(0);
//            //tasks.remove(crewFragmentTask.get(crewFragmentTask.size()-1));
//        }
//        CrewFragmentTask removeCrewFragmentTasks = trainNumMap.get("70").remove(0);
//        List<CrewFragmentTask> values = trainNumMap.get("70");
//        if (values != null) {
//            values.remove(0);
//        }
//        删除出入段
        //crewFragmentTask.remove(crewFragmentTask.get(0));
        //crewFragmentTask.remove(crewFragmentTask.get(crewFragmentTask.size()-1));

        //crewFragmentTask.remove(crewFragmentTask.get(85));//九公里——龙头寺
        //重复
        //crewFragmentTask.add(85, crewFragmentTask.get(85));
        //crewFragmentTask.remove(crewFragmentTask.get(86));
        return  crewFragmentTask;
    }
    /**
     * 完整性验证 todo 完整性验证
     */
    public void integrityTesting(List<CrewFragmentTask> crewFragmentTaskList) {
        //多或者少---有问题的车底
        Map<String, CrewFragmentTask> falseTrainNumMap = new HashMap<>();
        //按车底分类
        Map<String, List<CrewFragmentTask>> trainNumMap = crewFragmentTaskList.stream().collect(Collectors.groupingBy(CrewFragmentTask::getTrainNum));
        //使用 forEach 方法遍历每个任务列表，并对每个任务列表执行排序操作
        //通过 Comparator.comparing 方法指定按照任务的开始时间进行比较。
        //使用 thenComparing 方法指定当开始时间相同时，再按照结束时间进行比较
        trainNumMap.values().forEach(tasks -> tasks.sort(Comparator.comparing(CrewFragmentTask::getIntStartTime)
                .thenComparing(CrewFragmentTask::getIntEndTime)));
        //遍历所有车底
        for (Map.Entry<String, List<CrewFragmentTask>> entry : trainNumMap.entrySet()) {
            String key = entry.getKey();
            List<CrewFragmentTask> tasks = entry.getValue();
            /*
            测试重复
            CrewFragmentTask crewFragmentTask = tasks.get(3);
            tasks.add(3,crewFragmentTask);
             */
            //出入段
            if (!tasks.get(0).getOutDepot()) {
                falseTrainNumMap.put(key, tasks.get(0));
                throw new RuntimeException("出段存在错误的车底");
            }
            if (!tasks.get(tasks.size() - 1).getInDepot()) {
                falseTrainNumMap.put(key, tasks.get(tasks.size() - 1));
                throw new RuntimeException("入段存在错误的车底");
            }
            //遍历同一车底下的所有value
            for (int i = 0; i < tasks.size() - 1; i++) {
                //下一个索引
                int j = i + 1;
                CrewFragmentTask currentTask = tasks.get(i);
                CrewFragmentTask nextTask = tasks.get(j);
                //若重复
                if (currentTask.getStartStation().equals(nextTask.getStartStation())
                        && currentTask.getIntStartTime() == nextTask.getIntStartTime()) {
                    falseTrainNumMap.put(key, currentTask);
                    throw new RuntimeException("存在重复的车底");
                }
                //多或者少
                if (!(currentTask.getEndStation().equals(nextTask.getStartStation())
                        && currentTask.getIntEndTime() <= nextTask.getIntStartTime())) {
                    //发现缺失或额外的片段，记录车底，和当前有问题的对象
                    falseTrainNumMap.put(key, currentTask);
                    throw new RuntimeException("存在少或多划分车底");
                }
            }
        }
    }
}
