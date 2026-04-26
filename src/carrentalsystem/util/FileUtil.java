package carrentalsystem.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {
    public static String saveFile(File src, String folder) throws IOException {
        File destDir = new File(folder);
        if(!destDir.exists()) destDir.mkdirs();
        String destName = System.currentTimeMillis() + "_" + src.getName();
        File dest = new File(destDir, destName);
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return dest.getAbsolutePath();
    }
}
