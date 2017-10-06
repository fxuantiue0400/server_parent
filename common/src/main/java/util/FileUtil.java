package util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2017/9/10/010.
 */
public class FileUtil {

    /**
     * 小文件直接装载到内存
     *
     * 内部使用了try with resurce
     *
     * @param file
     * @return
     */
    public static byte[] readFileBytes(String file){
        Path path = Paths.get(file);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readFileStringList(String file){
        Path path = Paths.get(file);
        try {
            return Files.readAllLines(path,Charset.forName("GBK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        String file = "d:\\aaa.txt";
        byte[] bytes = readFileBytes(file);


        Optional.of(new String(bytes, Charset.forName("GBK"))).ifPresent(System.out::println);

        Optional.of("===============").ifPresent(System.out::println);
        List<String> stringLines = readFileStringList(file);
        stringLines.forEach(System.out::println);

        Path path = Paths.get(file);
        Path target = Paths.get("e:\\");

        try {
            Files.createTempFile(target,"t","bak");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
