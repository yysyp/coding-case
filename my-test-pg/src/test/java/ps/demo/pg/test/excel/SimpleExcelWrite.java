package ps.demo.pg.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import ps.demo.commonlibx.common.FileUtilTool;

import java.util.ArrayList;
import java.util.List;

public class SimpleExcelWrite {
    public static void main(String[] args) {
        String fileName = "simpleWrite.xlsx";

        // 准备数据
        List<DemoData> data = new ArrayList<>();
        data.add(new DemoData("张三", 25, "zhangsan@example.com"));
        data.add(new DemoData("李四", 30, "lisi@example.com"));
        data.add(new DemoData("王五", 28, "wangwu@example.com"));

        // 写入Excel
        EasyExcel.write(FileUtilTool.getFileInHomeDir(fileName), DemoData.class)
                .sheet("用户信息")
                .doWrite(data);
    }

    public static class DemoData {
        @ExcelProperty("姓名")
        private String name;

        @ExcelProperty("年龄")
        private Integer age;

        @ExcelProperty("邮箱")
        private String email;

        // 构造方法、getter和setter
        public DemoData(String name, Integer age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
    }
}
