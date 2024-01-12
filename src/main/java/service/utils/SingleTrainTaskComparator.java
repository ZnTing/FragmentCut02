package service.utils;

import domain.data.SingleTrainTask;

import java.util.Comparator;

/**
 * @auther 朱振霆~
 */
public class SingleTrainTaskComparator implements Comparator<SingleTrainTask> {

    @Override
    public int compare(SingleTrainTask o1, SingleTrainTask o2) {
        int trainNum1 = Integer.parseInt(o1.getTrainNum());
        int trainNum2 = Integer.parseInt(o2.getTrainNum());
        return trainNum1 - trainNum2;
    }

}
