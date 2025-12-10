package ps.poc.paimon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.paimon.catalog.Catalog;
import org.apache.paimon.catalog.CatalogContext;
import org.apache.paimon.catalog.CatalogFactory;
import org.apache.paimon.fs.Path;
import org.apache.paimon.options.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateCatalog {

//    public static Catalog createFilesystemCatalog() {
//        CatalogContext context = CatalogContext.create(new Path("..."));
//        return CatalogFactory.createCatalog(context);
//    }

    public static Catalog createFilesystemCatalog() throws RuntimeException {
        // 使用绝对路径或者适当的相对路径
        Options options = new Options();

        String warehousePath = "file:///" + System.getProperty("user.dir") + "/target/paimon_warehouse2";
        warehousePath = warehousePath.replace("\\", "/");
        System.out.println("===>>warehousePath: " + warehousePath);
//        String fsPath = StringUtils.substringAfter(warehousePath, "file:///");
//        if (Files.exists(Paths.get(fsPath))) {
//            System.out.println("===>>warehousePath clean up: " + warehousePath);
//            try {
//                FileUtils.deleteDirectory(new File(fsPath));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        options.set("warehouse", warehousePath); // 本地文件系统
        // 或者使用 HDFS 路径
        // options.set("warehouse", "hdfs://namenode:port/path/to/warehouse");

        options.set("fs.defaultFS", "file:///");

        CatalogContext context = CatalogContext.create(options);
        return CatalogFactory.createCatalog(context);
    }

    public static Catalog createHiveCatalog() {
        // Paimon Hive catalog relies on Hive jars
        // You should add hive classpath or hive bundled jar.
        Options options = new Options();
        options.set("warehouse", "...");
        options.set("metastore", "hive");
        options.set("uri", "...");
        options.set("hive-conf-dir", "...");
        options.set("hadoop-conf-dir", "...");
        CatalogContext context = CatalogContext.create(options);
        return CatalogFactory.createCatalog(context);
    }
}