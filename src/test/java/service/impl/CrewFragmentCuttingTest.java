package service.impl;

import dao.GetExcelData;
import domain.constraint.Constraints;
import domain.data.LineInformationTableRowData;
import domain.data.MetroNumTask;
import org.junit.jupiter.api.Test;
import service.utils.Utils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @auther 朱振霆~
 */
class CrewFragmentCuttingTest {
    @Test
    void testCrewFragmentCutting() throws IOException {
        String timeData = "E:\\实习文件\\重庆数据\\187号图时刻表 22-08-2（删环北）(删750-753).xls";
        String lineInfo = "E:\\实习文件\\重庆数据\\3号线线路信息.xlsx";
        GetExcelData getExcelDate = new GetExcelData();
        Constraints constraint = new Constraints();
        constraint.setOnAndOffDutyStations(Utils.tempOnAndOffDutyStation());//出退勤地点
        constraint.setInOutDepotTime(Utils.tempInAndOutTrainDepot());//出入段时间
        String trainDiagramNo = getExcelDate.getTrainDiagramNo(timeData);//输入时刻表名称号
        List<LineInformationTableRowData> lineInfoData = getExcelDate.getLineInfoData(lineInfo);//线路信息
        List<MetroNumTask> trainTimetableData = getExcelDate.getTrainTimetableData(timeData);//时刻表
        CrewFragmentCutting crewFragmentCutting = new CrewFragmentCutting(trainDiagramNo, trainTimetableData, lineInfoData, constraint);

    }
}