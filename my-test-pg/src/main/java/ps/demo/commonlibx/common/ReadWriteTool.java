package ps.demo.commonlibx.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class ReadWriteTool {

    public static void writeProperties(File file, Properties prop, boolean append) {
        Writer writer = null;
        try {
            writer = new FileWriter(file, append);
            prop.store(writer, TimeTool.getNowStr());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static Properties readProperties(File file) {
        Properties prop = new Properties();
        FileReader fileReader = null;
        //InputStream inStream=null;
        try {
            fileReader = new FileReader(file);
            prop.load(fileReader);
            //inStream = new FileInputStream(file);
            //prop.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                }
            }
        }
        return prop;
    }

    public static void writeObjectToFileTsInHomeDir(Object content) {
        writeObjectToFile(FileUtilTool.getFileTsInHomeDir(".log"), content);
    }

    public static void writeObjectToFile(File file, Object content) {
        writeObjectToFile(file, content, StandardCharsets.UTF_8, true);
    }

    public static void writeObjectToFile(File file, Object content, Charset charsetName, boolean append) {
        try {
            String lineEnding = System.lineSeparator();
            FileUtils.writeStringToFile(file, JsonTool.object2JsonString(content) + lineEnding, charsetName, append);
        } catch (IOException e) {
            throw new RuntimeException("Write to File failed!", e);
        }
    }

    public static <T> T readObjectFromFile(File file, Class<T> c) {
        try {
            return JsonTool.jsonString2Object(FileUtils.readFileToString(file, StandardCharsets.UTF_8), c);
        } catch (IOException e) {
            throw new RuntimeException("Read from File failed!", e);
        }
    }

    public static <T> void writeObjectsToFile(File file, List<T> lists) {
        writeObjectsToFile(file, lists, true);
    }

    public static <T> void writeObjectsToFile(File file, List<T> lists, boolean append) {
        try {
            List<String> stringList = JsonTool.listObject2ListJson(lists);
            FileUtils.writeLines(file, stringList, append);
        } catch (IOException e) {
            throw new RuntimeException("Write to File failed!", e);
        }
    }

    public static <T> List<T> readObjectsFromFile(File file, Class<T> c) {
        return readObjectsFromFile(file, StandardCharsets.UTF_8, c);
    }

    public static <T> List<T> readObjectsFromFile(File file, Charset charset, Class<T> c) {
        try {
            List<String> lines = FileUtils.readLines(file, charset);
            return lines.stream().map(s -> JsonTool.jsonString2Object(s, c)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Read from File failed!", e);
        }
    }


    public final static int countLines(File file) {
        try (LineNumberReader rf = new LineNumberReader(new FileReader(file))) {
            long fileLength = file.length();
            rf.skip(fileLength);
            return rf.getLineNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public final static List<String> lines(File file) {
        List<String> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static String readFileContent(File file) {
        return readFileContent(file, "UTF-8");
    }

    public static String readFileContent(File file, String encoding) {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String content = "";
            StringBuilder sb = new StringBuilder();
            while ((content = bf.readLine()) != null) {
                sb.append(content);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static void writeFileContent(File file, String content) {
        writeFileContent(file, content, "UTF-8");
    }

    public static void writeFileContent(File file, String content, String encoding) {
        writeFileContent(file, content, encoding, false);
    }

    public static void writeFileContent(File file, String content, String encoding, boolean append) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), encoding));
            bw.write(content);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static void closeAll(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
