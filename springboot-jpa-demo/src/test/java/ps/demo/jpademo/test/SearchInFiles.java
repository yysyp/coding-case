package ps.demo.jpademo.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchInFiles {


    private static Map<String, File> treeMap = new TreeMap();

    public static void main(String[] args) throws Exception {
        String prjPath = "./";
        String suffix = "java";
        String regex = "\"\\s*[^\u0000-\u007F]+\\s*\"";
        String outPath = "result.properties";
        Pattern pt = Pattern.compile(regex);

        Collection<File> listFiles = FileUtils.listFiles(new File(prjPath), FileFilterUtils.suffixFileFilter(suffix), DirectoryFileFilter.INSTANCE);
        for (File file : listFiles) {
            inFile(file, pt);
        }

        //System.out.print(treeMap.keySet().size());

        FileUtils.writeLines(new File(outPath), treeMap.keySet());
    }


    public static void inFile(File file, Pattern pt) throws IOException {
        String content = FileUtils.readFileToString(file);
        Matcher mt = pt.matcher(content);
        while (mt.find()) {
            treeMap.put(content.substring(mt.start(), mt.end()), file);
        }
    }

    /*
    public static void print(Map<String, File> treeMap) {

    }
    */

}
