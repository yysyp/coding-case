package pslab;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

public class TmlfTfMake {

    public static void main(String[] args) throws Exception {
        List<String> list = FileUtils.readLines(new File("tfsource/tmlfTfmakeReplacement.properties"), Charset.forName("UTF-8"));
        //"\\Q" + tobeEscapedRegex + "\\E"
        processFile(list, new File("tfsource/controller.tf"));
        processFile(list, new File("tfsource/dao.tf"));
        processFile(list, new File("tfsource/dto.tf"));
        processFile(list, new File("tfsource/entity.tf"));
        processFile(list, new File("tfsource/req.tf"));
        processFile(list, new File("tfsource/service.tf"));
        processFile(list, new File("tfsource/html-form.tf"));
        processFile(list, new File("tfsource/html-list.tf"));
        processFile(list, new File("tfsource/html-modify.tf"));
        processFile(list, new File("tfsource/html-view.tf"));
        System.out.println("done!");
    }

    private static void processFile(List<String> list, File file) throws IOException {
        String fileContent = FileUtils.readFileToString(file, "UTF-8");
        for (String rm : list) {
            String [] a = rm.split("=");
            //System.out.println("f="+a[0]+", r="+a[1]);
            fileContent = StringUtils.replace(fileContent, a[0], a[1]);
        }
        FileUtils.write(file, fileContent, "UTF-8");
    }
}
