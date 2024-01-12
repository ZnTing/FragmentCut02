package service.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.constant.Constant;
import domain.constraint.OnAndOffDutyStation;
//import domain.constraint.OnAndOffDutyStation;
//import domain.constraint.RestTimeConstraint;
//import domain.constraint.ShiftConstraint;
//import domain.constraint.StaticTimeConstraints;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * int时间转String时间
     * @param intTime 时间（秒）
     * @return 时间（HH:mm:ss）
     */
    public static String intToStrTime(int intTime) {
        int oneDay = 24 * 60 * 60;
        String strTime;
        intTime = Math.abs(intTime);
        if (intTime > oneDay) {
            strTime = (intTime - oneDay) / 3600
                    + ":" + (((intTime - oneDay) % 3600) / 60 < 10 ? ("0" + (((intTime - oneDay) % 3600) / 60)) : (((intTime - oneDay) % 3600) / 60))
                    + ":" + (((intTime - oneDay) % 60) == 0 ? "00" : (((intTime - oneDay) % 60) < 10 ? ("0" + ((intTime - oneDay) % 60)) : ((intTime - oneDay) % 60)));
        } else {
            strTime = intTime / 3600
                    + ":" + ((intTime % 3600) / 60 < 10 ? ("0" + ((intTime % 3600) / 60)) : ((intTime % 3600) / 60))
                    + ":" + ((intTime % 60) == 0 ? "00" : ((intTime % 60) < 10 ? ("0" + (intTime % 60)) : (intTime % 60)));
        }
        return strTime;
    }

    /**
     * str时间转int时间
     * @param strEnterTime HH:mm:ss
     * @return 时间（秒）
     */
    public static int strToIntTime(String strEnterTime) {
        int temp;
        String[] splitTime = strEnterTime.split(":");
        if (splitTime.length == 1) {
            temp = Integer.parseInt(splitTime[0]) * 3600;
        } else if (splitTime.length == 2) {
            temp = Integer.parseInt(splitTime[0]) * 3600 + Integer.parseInt(splitTime[1]) * 60;
        } else {
            temp = Integer.parseInt(splitTime[0]) * 3600 + Integer.parseInt(splitTime[1]) * 60 + Integer.parseInt(splitTime[2]);
        }
        if (temp < 3 * 3600) {
            return temp + 24 * 60 * 60;
        } else {
            return temp;
        }
    }

    /**
     * 小时分钟转秒
     */
    public static int numToIntTime(int hours, int minutes) {
        return hours * 3600 + minutes * 60;
    }

    /**
     * 计算距离（输入单位：米  输出单位：千米）
     */
    public static double calculateDistance(int a, int b) {
        BigDecimal t = new BigDecimal(Math.abs(a - b) / 1000);
        return t.setScale(0, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * int时间转String时间（导出轮乘卡用）
     * @param intTime 时间（秒）
     * @return 时间（HH:mm:ss）
     */
    public static String intToStrTimeSp(int intTime) {
        int oneDay = 24 * 60 * 60;
        intTime = Math.abs(intTime);
        String H, M;
        int h, m, t;
        if (intTime > oneDay) {
            t = intTime - oneDay;
        } else {
            t = intTime;
        }
        h = t / 3600;
        m = (t % 3600) / 60;
        if (h >= 10) {
            H = "" + h;
        } else {
            H = "0" + h;
        }
        if (m >= 10) {
            M = "" + m;
        } else {
            M = "0" + m;
        }
        return H + M;
    }

    private static String isOddOrEven(int i) {
        // 判断一个数是奇数还是偶数
        if (i % 2 == 0) {
            return "偶数";
        } else {
            return "奇数";
        }
    }

    public static String checkUpOrDown(String metroNo) {
        int i = Integer.parseInt(metroNo);
        switch (isOddOrEven(i)) {
            case "偶数":
                return "上行";
            case "奇数":
                return "下行";
        }
        return "";
    }

    public static String transUpOrDown(String metroNo) {
        String s = checkUpOrDown(metroNo);
        switch (s) {
            case "上行":
                return "20" + metroNo;
            case "下行":
                return "10" + metroNo;
        }
        return "";
    }

    /**
     * 给派班数据前面加0
     *
     * @param time H:mm:ss
     * @return HH:mm:ss
     */
    public static String handleTimeFormat_Dispatch(String time) {
        String[] splitTime = time.split(":");
        if (Integer.parseInt(splitTime[0]) < 10) {
            return "0" + time;
        } else {
            return time;
        }
    }

    /**
     * 对象的深拷贝
     *
     * @param t     被复制的对象
     * @param clazz 对象类型
     * @param <T>   泛型
     * @return 复制的对象
     */
    public static <T> T deepClone(T t, Class<T> clazz) {
        String jsonStr = JSON.toJSONString(t, SerializerFeature.WriteClassName);
        return JSON.parseObject(jsonStr, clazz);
    }

    /**
     * 对象的深拷贝
     *
     * @param <T>   泛型
     * @param t     被复制的对象
     * @param clazz 对象类型
     * @return 复制的对象
     */
    public static <T> List<T> deepCloneArr(List<T> t, Class<T> clazz) {
        String jsonStr = JSON.toJSONString(t, SerializerFeature.DisableCircularReferenceDetect);
        return JSON.parseArray(jsonStr, clazz);
    }

    /**
     * 在运行图名称中查找其编号字符串，并去除无关字符
     *
     * @param diagramsName 完整运行图名称
     * @return 运行图编号
     */
    public static String sSubstring(String diagramsName) {
        if (diagramsName == null || "".equals(diagramsName)) {
            throw new RuntimeException("运行图名称错误！");
        }
        diagramsName = getString(diagramsName, "[\\s]+[0-9]+[号]");
        StringBuilder stringBuilder = new StringBuilder(diagramsName);
        stringBuilder.deleteCharAt(stringBuilder.indexOf(" "));
        stringBuilder.deleteCharAt(stringBuilder.indexOf("号"));
        return stringBuilder.toString();
    }

    /**
     * 根据正则截取字符串
     */
    public static String getString(String str, String regex) {
        StringBuilder stringBuilder = new StringBuilder();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            stringBuilder.append(m.group());
        }
        return stringBuilder.toString();
    }


//    public static List<String> sort_cq_no_3_all = Stream
//            .of(Constant.CC_TJYZ, Constant.STATION_LTSZ, Constant.CC_DJ, Constant.STATION_JGLZ, Constant.CC_HCBL)
//            .collect(Collectors.toList());

    public static Comparator<String> getStringOrderCompare(List<String> sort) {
        return (o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            for (String s : sort) {
                if (o1.equals(s)) {
                    return -1;
                }
                if (o2.equals(s)) {
                    return 1;
                }
            }
            return 1;
        };
    }

    public static String cq_no_3_lts = Constant.CC_TJYZ + Constant.STATION_LTSZ;
    public static String cq_no_3_jgl = Constant.CC_DJ + Constant.STATION_JGLZ;

//    /**
//     * 先按照开始地点排序，再按照时间排序
//     * @param sort 地点顺序
//     */
//    public static Comparator<DriverCrewTask> getDriverTaskListStartStationOrderCompare(List<String> sort) {
//        return (o1, o2) -> {
//            for (String s : sort) {
//                boolean o1First = s.contains(o1.getStartStation());
//                boolean o2First = s.contains(o2.getStartStation());
//                boolean sameName = o1.getStartStation().equals(o2.getStartStation());
//                if (sameName || (o1First && o2First)) {
//                    return Integer.compare(o1.getIntOffDutyTime(), o2.getIntOffDutyTime());
//                } else {
//                    if (o1First) {
//                        return -1;
//                    }
//                    if (o2First) {
//                        return 1;
//                    }
//                }
//            }
//            return Integer.compare(o1.getIntOffDutyTime(), o2.getIntOffDutyTime());
//        };
//    }
//
//    /**
//     * 先按照开始地点排序，再按照时间排序
//     * @param sort 地点顺序
//     */
//    public static Comparator<DriverCrewTask> getDriverTaskListEndStationOrderCompare(List<String> sort) {
//        return (o1, o2) -> {
//            for (String s : sort) {
//                boolean o1First = s.contains(o1.getEndStation());
//                boolean o2First = s.contains(o2.getEndStation());
//                boolean sameName = o1.getEndStation().equals(o2.getEndStation());
//                if (sameName || (o1First && o2First)) {
//                    return Integer.compare(o1.getIntOffDutyTime(), o2.getIntOffDutyTime());
//                } else {
//                    if (o1First) {
//                        return -1;
//                    }
//                    if (o2First) {
//                        return 1;
//                    }
//                }
//            }
//            return Integer.compare(o1.getIntOffDutyTime(), o2.getIntOffDutyTime());
//        };
//    }



    /**
     * 将汉字转为数字
     *
     * @param taskType 班次类型
     */
    public static Integer taskType2Int(String taskType) {
        if (taskType == null) {
            return 0;
        }
        switch (taskType) {
            case Constant.MORNING:
                return 1;
            case Constant.DAY:
                return 2;
            case Constant.NIGHT:
                return 3;
            default:
                return 0;
        }
    }


    public static String int2TaskType(Integer i) {
        if (i == null) {
            return null;
        }
        switch (i) {
            case 1:
                return Constant.MORNING;
            case 2:
                return Constant.DAY;
            case 3:
                return Constant.NIGHT;
            default:
                return null;
        }
    }
//    /**
//     * 静态变量  有了
//     */
//    public static StaticTimeConstraints tempStaticTimeConstraints() {
//        return new StaticTimeConstraints(numToIntTime(6, 30), // 早班交车时间（6:30）
//                numToIntTime(3, 30),// 夜-早班累积工作时长（9:30小时）
//                numToIntTime(8, 0), // 白班累积工作时长（8小时）
//                numToIntTime(7, 0),
//                numToIntTime(2, 0), // 连续工作时长（2小时）
//                numToIntTime(0, 35), // 检车时长（出库基班出勤时间 = 基班开始时间 - 检车时长）
//                numToIntTime(0, 20), // 正线出勤准备时长
//                numToIntTime(0, 45), // 普通接续时最大允许休息时间
//                numToIntTime(0, 10)); // 特殊，单边车休息时间
//    }

    /**
     * 临时匹配出退勤地点（需要注意顺序）
     */
    public static List<OnAndOffDutyStation> tempOnAndOffDutyStation() {

        List<OnAndOffDutyStation> onAndOffDutyStations = new ArrayList<>();

        OnAndOffDutyStation temp1 = new OnAndOffDutyStation();
        OnAndOffDutyStation temp2 = new OnAndOffDutyStation();
        OnAndOffDutyStation temp3 = new OnAndOffDutyStation();
        OnAndOffDutyStation temp4 = new OnAndOffDutyStation();
        OnAndOffDutyStation temp5 = new OnAndOffDutyStation();
        OnAndOffDutyStation temp6 = new OnAndOffDutyStation();
        OnAndOffDutyStation temp7 = new OnAndOffDutyStation();
//
        temp1.getStartWorkStation().add(Constant.CC_TJYZ);
        temp1.getStartWorkStation().add(Constant.STATION_LTSZ);
        temp1.getEndWorkStation().add(Constant.CC_TJYZ);
        temp1.getEndWorkStation().add(Constant.STATION_LTSZ);

        temp2.getStartWorkStation().add(Constant.CC_DJ);
        temp2.getStartWorkStation().add(Constant.STATION_JGLZ);
        temp2.getEndWorkStation().add(Constant.CC_DJ);
        temp2.getEndWorkStation().add(Constant.STATION_JGLZ);

        temp3.getStartWorkStation().add(Constant.CC_HCBL);
        temp3.getEndWorkStation().add(Constant.STATION_LTSZ);
        temp3.getEndWorkStation().add(Constant.STATION_JGLZ);

//        temp4.getStartWorkStation().add(Constant.STATION_LTSZ);
//        temp4.getStartWorkStation().add(Constant.STATION_JGLZ);
//        temp4.getEndWorkStation().add(Constant.STATION_HCBLCC);

        temp6.getStartWorkStation().add(Constant.STATION_LTSZ);
        temp6.getEndWorkStation().add(Constant.CC_HCBL);

        temp7.getStartWorkStation().add(Constant.STATION_JGLZ);
        temp7.getEndWorkStation().add(Constant.CC_HCBL);

        temp5.getStartWorkStation().add(Constant.CC_HCBL);
        temp5.getStartWorkStation().add(Constant.STATION_JRBZ);
        temp5.getEndWorkStation().add(Constant.CC_HCBL);
        temp5.getEndWorkStation().add(Constant.STATION_JRBZ);

        onAndOffDutyStations.add(temp1);
        onAndOffDutyStations.add(temp2);
        onAndOffDutyStations.add(temp3);
//        onAndOffDutyStations.add(temp4);
        onAndOffDutyStations.add(temp5);
        onAndOffDutyStations.add(temp6);
        onAndOffDutyStations.add(temp7);
        return onAndOffDutyStations;
    }


        /**
         * 临时匹配出入段时间
         */
    public static Map<String, Integer> tempInAndOutTrainDepot() {
        Map<String, Integer> tempMap = new HashMap<>(8);
        tempMap.put(Constant.CC_TJYZ, 15 * 60);
        tempMap.put(Constant.CC_DJ, 10 * 60);
        tempMap.put(Constant.CC_HCBL, 15 * 60);
        return tempMap;
    }
    /**
     * 通过字符串匹配不同班次的不同时段休息时间
     */
//    public static Map<String, ShiftConstraint> makeStringShiftConstraintMap() {
//        Map<String, ShiftConstraint> temp = new HashMap<>(8);
//
//        ShiftConstraint morning = new ShiftConstraint(
//                false,
//                numToIntTime(5, 0),
//                numToIntTime(8, 20),
//                numToIntTime(6, 50), // 这个参数对计算结果影响很大
//                numToIntTime(10, 0),
//                numToIntTime(10, 30),
//                4,
//                numToIntTime(4, 0));
//        RestTimeConstraint x = new RestTimeConstraint(
//                numToIntTime(5, 0),
//                numToIntTime(10, 30),
//                numToIntTime(0, 15),
//                numToIntTime(0, 30));
//        morning.getRestTimeConstraints().add(x);
//        temp.put("早班", morning);
//
//        ShiftConstraint day = new ShiftConstraint(
//                false,
//                numToIntTime(8, 20),
//                numToIntTime(16, 20),
//                numToIntTime(15, 20),
//                numToIntTime(18, 0),
//                numToIntTime(18, 30),
//                6,
//                numToIntTime(9, 0));
//        RestTimeConstraint a = new RestTimeConstraint(
//                numToIntTime(8, 20),
//                numToIntTime(11, 0),
//                numToIntTime(0, 15),
//                numToIntTime(0, 30));
//        day.getRestTimeConstraints().add(a);
//        RestTimeConstraint b = new RestTimeConstraint(
//                numToIntTime(11, 0),
//                numToIntTime(13, 0),
//                numToIntTime(0, 15),
//                numToIntTime(0, 40));
//        day.getRestTimeConstraints().add(b);
//        RestTimeConstraint c = new RestTimeConstraint(
//                numToIntTime(13, 0),
//                numToIntTime(16, 20),
//                numToIntTime(0, 15),
//                numToIntTime(0, 45));
//        day.getRestTimeConstraints().add(c);
//        RestTimeConstraint d = new RestTimeConstraint(
//                numToIntTime(16, 20),
//                numToIntTime(18, 30),
//                numToIntTime(0, 15),
//                numToIntTime(0, 30));
//        day.getRestTimeConstraints().add(d);
//        // 特殊情况，限定在童家院子白班入库后休息一段时间后可再接车
//        RestTimeConstraint t = new RestTimeConstraint(
//                Constant.CC_TJYZ,
//                0,
//                0,
//                numToIntTime(0, 80),
//                numToIntTime(0, 100));
//        day.getRestTimeConstraints().add(t); // 特殊情况
//
//        temp.put("白班", day);
//
//        ShiftConstraint night = new ShiftConstraint(
//                true,
//                -numToIntTime(25, 0), // 开始接车时间
//                -numToIntTime(19, 0), // 接车结束时间 不填
//                -numToIntTime(19, 30), // 退勤片段划分界限1848 不填
//                -numToIntTime(16, 20), // 一般退勤时间 不填
//                -numToIntTime(16, 20), // 最晚退勤时间
//                99,
//                numToIntTime(5, 0));
//
//        RestTimeConstraint special = new RestTimeConstraint(
//                "特殊",
//                numToIntTime(13, 0),
//                numToIntTime(16, 20),
//                numToIntTime(0, 15),
//                numToIntTime(0, 40));
//        night.getRestTimeConstraints().add(special);
//
//        RestTimeConstraint e = new RestTimeConstraint(
//                numToIntTime(16, 20),
//                numToIntTime(17, 20),
//                numToIntTime(0, 15),
//                numToIntTime(0, 40));
//        night.getRestTimeConstraints().add(e);
//        RestTimeConstraint f = new RestTimeConstraint(
//                numToIntTime(17, 20),
//                numToIntTime(19, 20),
//                numToIntTime(0, 15),
//                numToIntTime(0, 40));
//        night.getRestTimeConstraints().add(f);
//        RestTimeConstraint g = new RestTimeConstraint(
//                numToIntTime(19, 20),
//                numToIntTime(25, 0),
//                numToIntTime(0, 15),
//                numToIntTime(0, 45));
//        night.getRestTimeConstraints().add(g);
//        temp.put("夜班", night);
//        return temp;
//    }

}

