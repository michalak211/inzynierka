//package pl.mrozek.inzynierka.temp;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//import pl.pwpw.demo.KWN.Entity.KWN;
//import pl.pwpw.demo.KWN.Entity.Task;
//import pl.pwpw.demo.KWN.Mapper.Mapper;
//import pl.pwpw.demo.KWN.Service.KWNService;
//import pl.pwpw.demo.ogolne.Entity.PC;
//import pl.pwpw.demo.ogolne.Entity.Status;
//import pl.pwpw.demo.ogolne.Entity.Zlecenia;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Locale;
//
//@Service
//public class ExcelgeneralGenerator {
//
//    @Autowired
//    KWNService kwnService;
//
//    @Autowired
//    Mapper mapper;
//
//    private XSSFWorkbook workbook;
//    private CellStyle stylBialy;
//    private CellStyle stylCzerwony;
//    private CellStyle stylZielony;
//    private CellStyle stylZolty;
//
//
//    private String url;
//
//    private void setUrl(String url) {
//        this.url = url;
//    }
//
//
//    public static void flushExcel(HttpServletResponse response, XSSFWorkbook workbook) {
//        try {
//            ServletOutputStream out = response.getOutputStream();
//            workbook.write(out);
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static XSSFWorkbook getTemplateSource(String url) throws Exception {
//        FileInputStream file;
//        file = new FileInputStream(new File(url));
//        return new XSSFWorkbook(file);
//    }
//
//    public void fillKWNForm(
//            long id, HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//
//
//        KWN kwn = kwnService.getKwn(id);
//        String fileName = kwn.getNrKwn() + ".xlsx";
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//        Resource resource = new ClassPathResource("kwn.xlsx");
//        File file = resource.getFile();
//        setUrl(file.getPath());
//        try {
//            if (this.url != null) {
//                workbook = getTemplateSource(this.url);
//            } else {
//                workbook = new XSSFWorkbook();
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        XSSFSheet sheet = workbook.getSheetAt(1);
//        XSSFRow row;
//
//        String pattern = "dd-MM-YYYY";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("pl", "PL"));
//
//        getCell(sheet, 0, 7).setCellValue("NR " + kwn.getNrKwn());
//        getCell(sheet, 1, 8).setCellValue(kwn.getObszar());
//        getCell(sheet, 1, 2).setCellValue(kwn.getNazwaWyrobu());
//        getCell(sheet, 3, 2).setCellValue(kwn.getIlosc());
//        getCell(sheet, 3, 8).setCellValue(kwn.getTypilosci());
//        getCell(sheet, 5, 2).setCellValue(kwn.getZglaszajacyNiezgodnosc());
//        getCell(sheet, 5, 6).setCellValue(kwn.getKomOrg());
//        getCell(sheet, 5, 8).setCellValue(simpleDateFormat.format(kwn.getDataZgloszenia()));
//
//        StringBuilder zlecenia = new StringBuilder();
//        for (Zlecenia nr : kwn.getNrZlecenia()) {
//            zlecenia.append(nr.getNrZlecenia()).append(",");
//        }
//
//        getCell(sheet, 7, 2).setCellValue(zlecenia.toString());
//        getCell(sheet, 7, 8).setCellValue(kwn.getDzialWydzial());
//        getCell(sheet, 9, 2).setCellValue(kwn.getWykryteNaEtapie());
//        getCell(sheet, 9, 8).setCellValue(kwn.getNrPtotOdb());
//        getCell(sheet, 12, 5).setCellValue(kwn.getNiezgodnoscDotyczy());
//        getCell(sheet, 14, 2).setCellValue(kwn.getCecha());
//        getCell(sheet, 14, 7).setCellValue(kwn.getOpisNiezgodnosci());
//
//
//        getCell(sheet, 17, 1).setCellValue(kwn.getDodatkowyOpisNiezgodnosci());
//        short temp=calculateRowHeight(100,kwn.getDodatkowyOpisNiezgodnosci(),20);
//        sheet.getRow(17).setHeightInPoints(temp);
//        getCell(sheet, 19, 5).setCellValue(kwn.getRodzajPrzyczyny());
//        getCell(sheet, 22, 1).setCellValue(kwn.getPotencjalnaPrzyczyna());
//        sheet.getRow(22).setHeightInPoints(calculateRowHeight(100,kwn.getPotencjalnaPrzyczyna(),20));
//        getCell(sheet, 24, 2).setCellValue(simpleDateFormat.format(kwn.getDataSpotkaniaZ()));
//        getCell(sheet, 25, 5).setCellValue(kwn.getDecyzjaZKWN());
//        getCell(sheet, 28, 1).setCellValue(kwn.getUzasadnienie());
//        getCell(sheet, 34, 7).setCellValue(kwn.getZzreklamowac());
//        getCell(sheet, 36, 7).setCellValue(kwn.getZzuruchomic());
//        if (kwn.getZzuruchomic().equals("tak")){
//            getCell(sheet,36,8).setCellValue(kwn.getOsobaOdp().getFirstAndLastName());
//        }
//
//        getCell(sheet, 38, 7).setCellValue(kwn.getZzotworzyc());
//        getCell(sheet, 42, 1).setCellValue(kwn.getUwagi());
//        getCell(sheet, 45, 1).setCellValue(kwn.getModerator().getImieiNazwisko());
//        getCell(sheet, 45, 4).setCellValue(kwn.getModerator().getDzial().split(" ")[0]);
//
//
//        short heightPodpis = sheet.getRow(52).getHeight();
//        short heightUwagi = sheet.getRow(42).getHeight();
//        short heightStandard = sheet.getRow(45).getHeight();
//
//        int rowIter1 = 0;
//        int rowIter2 = 0;
//        int i = 0;
//        int startingRow;
//
//
//        startingRow = 47;
//        for (PC osoba : kwn.getPcList()) {
//
//            if (i >=1) {
//                try {
//
//                    sheet.shiftRows(startingRow + i, sheet.getLastRowNum(), 1);
//                    row = copyRow(sheet, startingRow, startingRow + i);
//
//                    row.getCell(1).setCellValue(osoba.getImieNazwisko());
//                    row.getCell(4).setCellValue(osoba.getSymbolDzialu().split(" ")[0]);
//
//                    rowIter1++;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                sheet.getRow(startingRow).getCell(1).setCellValue(osoba.getImieNazwisko());
//                sheet.getRow(startingRow).getCell(4).setCellValue(osoba.getSymbolDzialu().split(" ")[0]);
//            }
//
//            i++;
//        }
//        startingRow = 31;
//        i = 0;
//        for (Task task : kwn.getTaskList()) {
//
//            if (i >=1) {
//                try {
//                    sheet.shiftRows(startingRow + i, sheet.getLastRowNum(), 1);
//                    row = copyRow(sheet, startingRow, startingRow + i);
//
//                    row.getCell(1).setCellValue(task.getContent());
//                    if (task.getDeadLine() != null) {
//                        row.getCell(7).setCellValue(simpleDateFormat.format(task.getDeadLine()));
////                        row.getCell(7).setCellValue(simpleDateFormat.format(osoba.getDeadLine()));
//                    }else {
//                        row.getCell(7).setCellValue((String) null);
//
//                    }
//                    row.getCell(8).setCellValue(task.getEmployee().getFirstAndLastName());
//                    row.setHeightInPoints(calculateRowHeight(60,task.getContent(),20));
//
//
//                    rowIter2++;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//
//                sheet.getRow(startingRow + i).getCell(1).setCellValue(task.getContent());
//                if (task.getDeadLine() != null) {
//                    sheet.getRow(startingRow + i).getCell(7).setCellValue(simpleDateFormat.format(task.getDeadLine()));
//                }else {
//                    sheet.getRow(startingRow + i).getCell(7).setCellValue((String) null);
//
//                }
//                sheet.getRow(startingRow + i).getCell(8).setCellValue(task.getEmployee().getFirstAndLastName());
//                sheet.getRow(startingRow+i).setHeightInPoints(calculateRowHeight(60,task.getContent(),20));
//            }
//            i++;
//        }
//
//
//        sheet.getRow(45 + rowIter2).setHeight(heightStandard);
//
//        if (rowIter2 >= 1) {
//            for (int j = 0; j < rowIter1+1; j++) {
//                sheet.getRow(47 + j + rowIter2).setHeight(heightStandard);
//            }
//        }
//
//        sheet.getRow(52 + rowIter1 + rowIter2).setHeight(heightPodpis);
//        sheet.getRow(42 + rowIter2).setHeight(heightUwagi);
//
//        int rowBreak = sheet.getRowBreaks()[0];
//        sheet.removeRowBreak(rowBreak);
//        sheet.setRowBreak(rowBreak + rowIter2);
//        flushExcel(response, workbook);
//    }
//
//    public static short calculateRowHeight(int rowCapacity, String content,int czcionka){
//
//        int rows=0;
//        String[] parts= content.split("\n",-1);
//        for (String part:parts){
//            rows++;
//            rows+=part.length()/rowCapacity;
//        }
//        short ret= (short) (rows*(czcionka));
//        return ret;
//    }
//
//
//    public void createExcelKWN(List<KWN> list, HttpServletRequest request,
//                               HttpServletResponse response) throws Exception {
//
//
//        String fileName = "BazaEffectKWN.xlsx";
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//        Resource resource = new ClassPathResource("BazaKwn.xlsx");
//        File file = resource.getFile();
//        setUrl(file.getPath());
//
//
//        if (this.url != null) {
//            workbook = getTemplateSource(this.url);
//        } else {
//            workbook = new XSSFWorkbook();
//        }
//
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        getCell(sheet, 2, 2).getCellStyle();
//        stylBialy = getCell(sheet, 2, 3).getCellStyle();
//        stylCzerwony = getCell(sheet, 2, 2).getCellStyle();
//        stylZielony = getCell(sheet, 2, 1).getCellStyle();
//        stylZolty = getCell(sheet, 2, 0).getCellStyle();
//
//        int rowIterator = 2;
//        int Lp = 1;
//
//
//
//        for (int j=0;j<list.size();j++) {
//            KWN kwn=list.get(j);
//
//            if (kwn.getTaskList().size() > 0) {
//
//                boolean fill1=true;
//                for (Task task : kwn.getTaskList()) {
//                    Row row = copyRow(sheet, 1, rowIterator);
//                    if (fill1){
//                        rowFill(row, kwn);
//                        fill1=false;
//                    }
//                    row.getCell(0).setCellValue(Lp);
//                    fillTaskRow(row,task);
//                    rowIterator++;
//                }
//            } else {
//                Row row = copyRow(sheet, 1, rowIterator);
//                row.getCell(0).setCellValue(Lp);
//                rowFill(row, kwn);
//                rowIterator++;
//            }
//
//            //scalanie komorek do zadań
//            if (kwn.getTaskList().size() > 0) {
//                for (int i = 0; i <= 30; i++) {
//                    if (i != 22 && i != 23 && i != 24&& i!=25) {
//                        sheet.addMergedRegion(new CellRangeAddress(rowIterator - kwn.getTaskList().size(), rowIterator - 1, i, i));
//                    }
//                }
//            }
//            Lp++;
//        }
//
////        for (int i = 0; i <= 30; i++) {
////            sheet.autoSizeColumn(i);
////        }
//
//        sheet.removeRow(sheet.getRow(1));
//        sheet.shiftRows(2, sheet.getLastRowNum(), -1);
//
//        flushExcel(response, workbook);
//    }
//
//
//    private void rowFill(Row row, KWN kwn) {
//
//        String pattern = "dd-MM-YYYY";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//
//        row.getCell(1).setCellValue(kwn.getNrKwn());
//        row.getCell(2).setCellValue(kwn.getNazwaWyrobu());
//        row.getCell(3).setCellValue(kwn.getObszar());
//        row.getCell(4).setCellValue(kwn.getIlosc());
//        row.getCell(5).setCellValue(kwn.getTypilosci());
//        row.getCell(6).setCellValue(simpleDateFormat.format(kwn.getDataZgloszenia()));
//        row.getCell(7).setCellValue(kwn.getZglaszajacyNiezgodnosc());
//        row.getCell(8).setCellValue(kwn.getKomOrg());
//        row.getCell(9).setCellValue(kwn.getDzialWydzial());
//        row.getCell(10).setCellValue(kwn.getNrPtotOdb());
//        row.getCell(11).setCellValue(kwn.getWykryteNaEtapie());
//
//        StringBuilder temp = new StringBuilder();
//        for (Zlecenia zl : kwn.getNrZlecenia()) {
//            temp.append(zl.getNrZlecenia()).append(",");
//        }
//        row.getCell(12).setCellValue(temp.toString());
//        row.getCell(13).setCellValue(kwn.getNiezgodnoscDotyczy());
//        row.getCell(14).setCellValue(kwn.getCecha());
//        row.getCell(15).setCellValue(kwn.getOpisNiezgodnosci());
//        row.getCell(16).setCellValue(kwn.getDodatkowyOpisNiezgodnosci());
//        row.getCell(17).setCellValue(kwn.getRodzajPrzyczyny());
//        row.getCell(18).setCellValue(kwn.getPotencjalnaPrzyczyna());
//        row.getCell(19).setCellValue(simpleDateFormat.format(kwn.getDataSpotkaniaZ()));
//        row.getCell(20).setCellValue(kwn.getDecyzjaZKWN());
//        row.getCell(21).setCellValue(kwn.getUzasadnienie());
//        row.getCell(26).setCellValue(kwn.getZzreklamowac());
//        row.getCell(27).setCellValue(kwn.getZzuruchomic());
//        row.getCell(28).setCellValue(kwn.getZzotworzyc());
//        row.getCell(29).setCellValue(kwn.getUwagi());
//        row.getCell(30).setCellValue(kwn.getStatus().toString());
//
//        if (kwn.getStatus().equals(Status.ZAKONCZONY)) {
//            row.getCell(30).setCellStyle(stylZielony);
//        } else if (kwn.getStatus().equals(Status.AKTYWNY)) {
//            row.getCell(30).setCellStyle(stylZolty);
//        }
//    }
//    private void fillTaskRow(Row row, Task task){
//        String pattern = "dd-MM-YYYY";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//
//
//
//        if (task.getContent() != null) {
//            row.getCell(22).setCellValue(task.getContent());
//            if (task.getDeadLine() != null) {
//                row.getCell(23).setCellValue(simpleDateFormat.format(task.getDeadLine()));
//            }
//            row.getCell(24).setCellValue(task.getEmployee().getFirstAndLastName());
//            row.getCell(25).setCellValue(task.getStatus().toString());
//            switch (task.getStatus()){
//                case ZAKONCZONY: {row.getCell(25).setCellStyle(stylZielony);
//                break;}
//                case KONIEC_JEST_BLISKI: {row.getCell(25).setCellStyle(stylZolty);
//                break;}
//                case OPOZNIONY: {row.getCell(25).setCellStyle(stylCzerwony);
//                break;}
//            }
//        }
//
//    }
//
//
//    public static XSSFCell getCell(XSSFSheet sheet, int row, int col) {
//        XSSFRow sheetRow = sheet.getRow(row);
//        if (sheetRow == null) {
//            sheetRow = sheet.createRow(row);
//        }
//        XSSFCell cell = sheetRow.getCell(col);
//        if (cell == null) {
//            cell = sheetRow.createCell(col);
//        }
//        return cell;
//    }
//
//    private void colorRow(Row row, CellStyle kolor) {
//
//        for (int i = 0; i <= 29; i++) {
//            row.getCell(i).setCellStyle(kolor);
//        }
//    }
//
//    private void cloneCell(int j, int row, XSSFSheet sheet) {
//        //XSSFCell oldCell = sheet.getRow(1).getCell(j+1);
//        //XSSFCell newCell = sheet.getRow(1).createCell(j);
//
//
//        sheet.getRow(row).getCell(j).setCellComment(sheet.getRow(row).getCell(j + 1).getCellComment());
//        sheet.getRow(row).getCell(j).setCellStyle(sheet.getRow(row).getCell(j + 1).getCellStyle());
//        sheet.setColumnWidth(j, sheet.getColumnWidth(j + 1));
//
//
//        switch (sheet.getRow(row).getCell(j + 1).getCellType()) {
//            case Cell.CELL_TYPE_BOOLEAN: {
//                sheet.getRow(row).getCell(j).setCellValue(sheet.getRow(row).getCell(j + 1).getBooleanCellValue());
//                break;
//            }
//            case Cell.CELL_TYPE_NUMERIC: {
//                sheet.getRow(row).getCell(j).setCellValue(sheet.getRow(row).getCell(j + 1).getNumericCellValue());
//                break;
//            }
//            case Cell.CELL_TYPE_STRING: {
//                sheet.getRow(row).getCell(j).setCellValue(sheet.getRow(row).getCell(j + 1).getStringCellValue());
//                break;
//            }
//            case Cell.CELL_TYPE_ERROR: {
//                sheet.getRow(row).getCell(j).setCellValue(sheet.getRow(row).getCell(j + 1).getErrorCellValue());
//                break;
//            }
//            case Cell.CELL_TYPE_FORMULA: {
//                sheet.getRow(row).getCell(j).setCellFormula(sheet.getRow(row).getCell(j + 1).getCellFormula());
//                break;
//            }
//        }
//
//
//        //return sheet.getRow(1).getCell(j);
//    }
//
//
//    public static XSSFRow copyRow(XSSFSheet sheet, int sourceRowNum, int destinationRowNum) {
//        // pobieranie rzędu
//
//        XSSFRow sourceRow = sheet.getRow(sourceRowNum);
//        XSSFRow newRow = sheet.createRow(destinationRowNum);
//        XSSFWorkbook workbook = sheet.getWorkbook();
//
//        newRow.setHeight(sourceRow.getHeight());
//
//        XSSFCell newCell;
//        XSSFCell oldCell;
//
//        // tworzenieodpowiedniej ilości komoórek
//        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
//            // kopiowanie komorki
//            oldCell = sourceRow.getCell(i);
//            newCell = newRow.createCell(i);
//
//            // obsluga pustych komorek nie wiem po co, ale jest
//            if (oldCell == null) {
//                newCell = null;
//                continue;
//            }
//
//            // kopiowanie stylu
//            XSSFCellStyle newCellStyle = workbook.createCellStyle();
//            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
//            newCell.setCellStyle(newCellStyle);
//
//            // kopiowanie komentarzy
//            if (oldCell.getCellComment() != null) {
//                newCell.setCellComment(oldCell.getCellComment());
//            }
//
//            // kopiowanie hiperłączy
//            if (oldCell.getHyperlink() != null) {
//                newCell.setHyperlink(oldCell.getHyperlink());
//            }
//
//            // Ustawianie typu danych komorki
//            //newCell.setCellType(oldCell.getCellType());
//            //newCell.setCellType(oldCell.getCellTypeEnum());
//
//            switch (oldCell.getCellType()) {
//                case Cell.CELL_TYPE_BLANK:
//                    newCell.setCellValue(oldCell.getStringCellValue());
//                    break;
//                case Cell.CELL_TYPE_BOOLEAN:
//                    newCell.setCellValue(oldCell.getBooleanCellValue());
//                    break;
//                case Cell.CELL_TYPE_ERROR:
//                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
//                    break;
//                case Cell.CELL_TYPE_FORMULA:
//                    newCell.setCellFormula(oldCell.getCellFormula());
//                    break;
//                case Cell.CELL_TYPE_NUMERIC:
//                    newCell.setCellValue(oldCell.getNumericCellValue());
//                    break;
//                case Cell.CELL_TYPE_STRING:
//                    newCell.setCellValue(oldCell.getRichStringCellValue());
//                    break;
//            }
//        }
//
//        // scalanie komorek
//        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
//            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
//            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
//                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
//                        (newRow.getRowNum() +
//                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
//                                )),
//                        cellRangeAddress.getFirstColumn(),
//                        cellRangeAddress.getLastColumn());
//                sheet.addMergedRegion(newCellRangeAddress);
//            }
//        }
//
//        return newRow;
//    }
//
//
//}
