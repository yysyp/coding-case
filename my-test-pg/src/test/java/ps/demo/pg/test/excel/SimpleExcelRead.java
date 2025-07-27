package ps.demo.pg.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import ps.demo.commonlibx.common.FileUtilTool;

public class SimpleExcelRead {
    public static void main(String[] args) {
        String fileName = "simpleWrite.xlsx";

        EasyExcel.read(FileUtilTool.getFileInHomeDir(fileName), DemoData.class, new DemoDataListener())
                .sheet()
                .doRead();
    }

    public static class DemoDataListener implements ReadListener<DemoData> {
        @Override
        public void invoke(DemoData data, AnalysisContext context) {
            System.out.println("读取到数据: " + data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("所有数据解析完成");
        }
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