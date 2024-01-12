package dao;

import domain.data.ArrivalStationTime;
import domain.data.LineInformationTableRowData;
import domain.data.MetroNumTask;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther 朱振霆~
 */
public class GetExcelData {


    public String getTrainDiagramNo(String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        Workbook workbook = null;
        if (path.endsWith(".xls")) {
            workbook = new HSSFWorkbook(in);
        }
        if (path.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(in);
        }
        if (workbook == null || workbook.getSheetAt(0) == null) {
            throw new RuntimeException("运行图文件错误");
        }
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(0);
        return cell == null ? null : getCellValue(cell);
    }

    /**
     * 获取列车时刻表数据
     * @param path 文件路径
     * @return 列车时刻表数据
     * @throws IOException
     */
    public List<MetroNumTask> getTrainTimetableData(String path) throws IOException {

        MetroNumTask rowData; // 行数据
        List<ArrivalStationTime> arrivalStationTimes; // 对于每一个
        ArrivalStationTime arrivalStationTime;
        List<MetroNumTask> trainTimetableRowData = new ArrayList<>(); // 行数据集合

        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        Workbook workbook = null;
        if (path.endsWith(".xls")) {
            workbook = new HSSFWorkbook(in);
        }
        if (path.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(in);
        }
        if (workbook == null || workbook.getSheetAt(0) == null) {
            throw new RuntimeException("运行图文件错误");
        }
        Sheet sheet = workbook.getSheetAt(0);
        //在第一行获取车站名及其索引
        Row stationNameRow = sheet.getRow(1);
        int stationNameColumnCount = stationNameRow.getLastCellNum() == 0 ? 0 : stationNameRow.getLastCellNum();
        String[] stationName = new String[stationNameColumnCount];
        for (int i = 0; i < stationNameColumnCount; i++) {
            Cell cell = stationNameRow.getCell(i); // 获取单元格数据
            String value = cell == null ? null : getCellValue(cell);
            stationName[i] = value;
        }
        // 从第三行开始获取数据
        int rowCount = sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum();//最后一行的索引
        for (int r = 3; r <= rowCount; r++) {
            //获取每一行的最大列数
            Row row = sheet.getRow(r);
            int columnCount = row.getLastCellNum() == 0 ? 0 : row.getLastCellNum();
            rowData = new MetroNumTask();
            arrivalStationTimes = new ArrayList<>();
            // -1避免空指针
            for (int j = 0; j < columnCount - 1; j++) {
                Cell cell1 = row.getCell(j); // 获取单元格数据
                String value1 = cell1 == null ? null : getCellValue(cell1);
                // 前两列单独指定
                if (j == 0) {
                    rowData.setTrainNum(value1);
                    continue;
                }
                if (j == 1) {
                    rowData.setMetroNum(value1);
                    continue;
                }
                Cell cell2 = row.getCell(j + 1); // 获取下一个单元格数据
                String value2 = cell2 == null ? null : getCellValue(cell2);
                if (j % 2 == 0 && value1 != null && !"".equals(value1) && value2 != null && !"".equals(value2)) {
                    arrivalStationTime = new ArrivalStationTime();
                    arrivalStationTime.setStationName(stationName[j]);
                    arrivalStationTime.startAndIntEnterTime(value1);
                    arrivalStationTime.startAndIntExitTime(value2);
                    arrivalStationTimes.add(arrivalStationTime);
                }
            }
            rowData.setArrivalSchedule(arrivalStationTimes);
            rowData.init();
            trainTimetableRowData.add(rowData);
        }
        return trainTimetableRowData;
    }

    /**
     * 获取线路信息数据
     * @param path 文件路径
     * @return 线路信息表数据
     * @throws IOException
     */
    public List<LineInformationTableRowData> getLineInfoData(String path) throws IOException {
        LineInformationTableRowData lineInformationTableRowData;
        List<LineInformationTableRowData> list = new ArrayList<>();

        FileInputStream in = new FileInputStream(path);
        Workbook workbook = null;
        if (path.endsWith(".xls")) {
            workbook = new HSSFWorkbook(in);
        }
        if (path.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(in);
        }
        if (workbook == null || workbook.getSheetAt(0) == null) {
            throw new RuntimeException("线路信息错误");
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum(); //最后一行的索引
        for (int r = 1; r <= rowCount; r++) {
            Row row = sheet.getRow(r);
            lineInformationTableRowData = new LineInformationTableRowData();
            //获取每一行的最大列数
            int columnCount = row.getLastCellNum() == 0 ? 0 : row.getLastCellNum();
            // 临时存放数据容器
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < columnCount; j++) {
                Cell cell = row.getCell(j);
                String value = cell == null ? null : getCellValue(cell);
                temp.add(value);
            }
            lineInformationTableRowData.setStationName(temp.get(0));
            lineInformationTableRowData.setDistance(Integer.parseInt(temp.get(1)));
            lineInformationTableRowData.setStationNum(Integer.parseInt(temp.get(2)));
            lineInformationTableRowData.setConnectingDepotName(temp.get(3));
            lineInformationTableRowData.setUpRotationPoint(temp.get(4));
            lineInformationTableRowData.setDownRotationPoint(temp.get(5));
            lineInformationTableRowData.setTurnBackStation(temp.get(6));
            list.add(lineInformationTableRowData);
        }
        return list;
    }

    //单元格内容类型校验
    private String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        String value = "";
        switch (cellType) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    DateFormat dateFormat = new SimpleDateFormat("H:mm:ss");
                    value = dateFormat.format(date);
//                    value = new DateTime(date).toString("H:mm:ss"); // 时间会偏移5分43秒
                } else {
                    cell.setCellType(CellType.STRING);
                    value = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                boolean booleanCellValue = cell.getBooleanCellValue();
                value = String.valueOf(booleanCellValue);
                break;
            case BLANK:
            default:
                break;
        }
        return value;
    }
}