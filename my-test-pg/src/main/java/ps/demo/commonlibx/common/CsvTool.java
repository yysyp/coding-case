package ps.demo.commonlibx.common;

import com.opencsv.CSVReader;
import com.opencsv.bean.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Data
 * public class User {
 *     @CsvBindByName(column = "name")
 *     @CsvBindByPosition(position = 0)
 *     private String name;
 *     @CsvBindByName(column = "age")
 *     @CsvBindByPosition(position = 1)
 *     private Integer age;
 *     @CsvBindByName(column = "sex")
 *     @CsvBindByPosition(position = 2)
 *     private String sex;
 * }
 */
public class CsvTool {

    public static <T> List<T> readCSVFile(String fileName, Class<T> clazz) {
        try (
                FileReader reader = new FileReader(fileName, Charset.forName("UTF-8"));
                CSVReader csvReader = new CSVReader(reader);) {

            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withType(clazz)
                    .withMappingStrategy(strategy)
                    .build();

            return csvToBean.parse();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void writeCSVFile(String fileName, List<T> list, boolean includeHeader, boolean append) {

        try (FileWriter writer = new FileWriter(fileName, Charset.forName("UTF-8"), append);) {
            StatefulBeanToCsvBuilder<T> beanToCsvBuilder = new StatefulBeanToCsvBuilder<>(writer);
            if (includeHeader) {
                var strategy = new CsvCustomColumnPositionStrategy<T>();
                Class<T> clazz = (Class<T>) list.get(0).getClass();
                strategy.setType(clazz);
                beanToCsvBuilder.withMappingStrategy(strategy);
            }
            beanToCsvBuilder.withOrderedResults(true);

            StatefulBeanToCsv<T> beanToCsv = beanToCsvBuilder.build();
            beanToCsv.write(list);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
