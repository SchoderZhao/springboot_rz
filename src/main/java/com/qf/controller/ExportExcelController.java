package com.qf.controller;


import com.qf.service.SysUserService;
import com.qf.utils.Lg;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @auther ZhaoXingLei
 * @date 2019/04/02  14:23
 */
@Controller
public class ExportExcelController {
    @Resource
    private SysUserService sysUserService;
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response){
        try{
            response.setContentType("application/octet-stream");//xxx.*
            String filename = "千锋员工信息.xls";
            filename = URLEncoder.encode(filename,"utf-8");//编码
            response.setHeader("content-disposition","attachment;filename="+filename);
            List<Map<String,Object>>  list = sysUserService.exportExcel();
            //list--->excel
            Workbook workbook = new HSSFWorkbook();//空的excel文件
            Sheet sheet =  workbook.createSheet("千锋集团员工信息");
            String titles = "userId,username,email,mobile,createTime,sex";
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i);//行
                String t[] = titles.split(",");
                Map<String,Object> map = list.get(i);

                for (int j = 0; j < t.length; j++) {
                    //单元格
                    Cell cell = row.createCell(j);
                    cell.setCellValue(map.get(t[j])+"");//给单元格赋值

                }

            }

            OutputStream os =  response.getOutputStream();
            workbook.write(os);//把excel文件响应到客户端
            os.flush();
            Lg.log("导出成功");
        }catch(Exception e){
            e.printStackTrace();
            Lg.log("导出失败");
        }

    }
}
