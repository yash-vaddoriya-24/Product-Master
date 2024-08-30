package com.example.productmaster.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ExcelController {

    @PostMapping("/api/makeExcel")
    public ResponseEntity<String> makeExcel(@RequestBody ActualData ad) {
        System.out.println(ad.header);
        System.out.println(ad.data.get(0));
        Path filePath = Paths.get("C:/Users/yashv/IdeaProjects/ProductMaster/product_master.xlsx");
        Workbook wb = excelmaker(ad);

        try (FileOutputStream fileOut = new FileOutputStream(filePath.toString())) {
            wb.write(fileOut);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while making excel file");
        }

        return ResponseEntity.ok("Excel file created successfully");
    }

    @GetMapping("/api/downloadExcel")
    public ResponseEntity<InputStreamResource> downloadExcel() {
        try {
            Path filePath = Paths.get("C:/Users/yashv/IdeaProjects/ProductMaster/product_master.xlsx");
            if (Files.exists(filePath)) {
                File file = filePath.toFile();
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product_master.xlsx");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(file.length())
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public Workbook excelmaker(ActualData ad) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        Row row = sheet.createRow(0);

        for (int k = 0; k < ad.header.size(); k++) {
            Cell cell = row.createCell(k);
            cell.setCellValue(ad.header.get(k));
        }
        for (int i = 0; i < ad.data.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            for (int j = 0; j < ad.data.get(i).size(); j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(ad.data.get(i).get(j));
            }
        }
        return wb;
    }

    public static class ActualData {
        private List<List<String>> data;
        private List<String> header;

        // Getters and Setters

        public List<List<String>> getData() {
            return data;
        }

        public void setData(List<List<String>> data) {
            this.data = data;
        }

        public List<String> getHeader() {
            return header;
        }

        public void setHeader(List<String> header) {
            this.header = header;
        }

        @Override
        public String toString() {
            return "ActualData{" +
                    "header=" + header + ", data=" + data +
                    '}';
        }
    }
}
