package com.yhh.springboot_hbase_ifc.FileSystem.util;

import org.apache.hadoop.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class FileUtil {
    /**
     * 文件写入输入流
     *
     * @param in   输入流
     * @param file 文件
     */
    public static void inputStreamToFile(InputStream in, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            IOUtils.closeStreams(os, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MultipartFile 转 File
     *
     * @param file 上传的文件
     * @return {@link File}
     */
    public static File multipartFileToFile(MultipartFile file) {
        File f = null;
        try {
            if (file != null && file.getSize() > 0) {
                InputStream in = file.getInputStream();
                f = new File(Objects.requireNonNull(file.getOriginalFilename()));
                inputStreamToFile(in, f);
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        }
    }
}
