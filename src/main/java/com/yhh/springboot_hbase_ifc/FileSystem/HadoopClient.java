package com.yhh.springboot_hbase_ifc.FileSystem;

import com.yhh.springboot_hbase_ifc.FileSystem.config.HadoopProperties;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class HadoopClient {

    private FileSystem fs;
    private HadoopProperties hadoopProperties;

    /**
     * 初始化时创建目录，不存在才创建
     */
    @PostConstruct
    public void init() {
        mkdir(hadoopProperties.getDirectoryPath(), true);
    }

    /**
     * 拼接文件路径地址
     *
     * @param folder 文件夹路径名称
     * @return {@link String}
     */
    private String spliceFolderPath(String folder) {
        if (StringUtils.isNotEmpty(folder)) {
            return hadoopProperties.getPath() + folder;
        } else {
            return hadoopProperties.getPath();
        }
    }

    /**
     * 创建目录
     *
     * @param folderPath 文件夹路径名称
     * @param create     不存在是否新建目录
     * @return {@link boolean}
     */
    public boolean mkdir(String folderPath, boolean create) {
        log.info("【开始创建目录】 文件夹路径名称: {}", folderPath);
        boolean flag = false;
        if (StringUtils.isEmpty(folderPath)) {
            throw new IllegalArgumentException("folder不能为空");
        }
        try {
            Path path = new Path(folderPath);
            if (create) {
                if (!fs.exists(path)) {
                    fs.mkdirs(path);
                }
            }
            if (fs.getFileStatus(path).isDirectory()) {
                flag = true;
            }
        } catch (Exception e) {
            log.error("【创建目录失败】", e);
        }
        return flag;
    }

    /**
     * 文件上传
     *
     * @param delSrc    指是否删除源文件，true为删除，默认为false
     * @param overwrite 是否覆盖
     * @param srcFile   源文件，上传文件路径
     * @param destPath  fs的目标路径
     */
    public void copyFileToHDFS(boolean delSrc, boolean overwrite, String srcFile, String destPath) {
        log.info("【文件上传】 开始上传, 上传文件路径: {}", destPath);
        Path srcPath = new Path(srcFile);
        // 目标路径
        Path dstPath = new Path(destPath);
        try {
            // 文件上传
            fs.copyFromLocalFile(delSrc, overwrite, srcPath, dstPath);
        } catch (IOException e) {
            log.error("【文件上传失败】", e);
        }
    }


    /**
     * 删除文件或者文件目录
     *
     * @param path     文件目录路径
     * @param fileName 文件名称
     */
    public void rmdir(String path, String fileName) {
        log.info("【删除文件】 开始删除, 删除文件目录的路径: {}, 文件目录: {}", path, fileName);
        try {
            // 返回FileSystem对象
            if (StringUtils.isNotBlank(fileName)) {
                path = path + "/" + fileName;
            }
            // 删除文件或者文件目录  delete(Path f) 此方法已经弃用
            fs.delete(new Path(path), true);
        } catch (IllegalArgumentException | IOException e) {
            log.error("【删除文件失败】", e);
        }
    }

    /**
     * 下载文件
     *
     * @param path         路径
     * @param fileName     文件名称
     * @param outputStream 输出流
     * @throws IOException 流异常
     */
    public void download(String path, String fileName, OutputStream outputStream) throws IOException {
        log.info("【下载文件】 开始下载, 下载文件名称: {}", fileName);
        @Cleanup InputStream is = fs.open(new Path(path + fileName));
        IOUtils.copyBytes(is, outputStream, 4096, true);
    }

    /**
     * 下载文件到本地
     *
     * @param path         文件路径
     * @param downloadPath 本地下载路径
     */
    public void downloadFileFromLocal(String path, String downloadPath) {
        log.info("【下载文件到本地】 开始下载, 文件路径: {}, 本地下载路径: {}", path, downloadPath);
        // 上传路径
        Path clientPath = new Path(path);
        // 目标路径
        Path serverPath = new Path(downloadPath);
        try {
            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
            fs.copyToLocalFile(false, clientPath, serverPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取目录信息
     *
     * @param path 目录路径
     * @return {@link List}
     */
    public List<Map<String, Object>> getPathInfo(String path) {
        log.info("【获取目录信息】 开始获取, 目录路径: {}", path);
        FileStatus[] statusList;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            statusList = fs.listStatus(new Path(path));
            if (null != statusList && statusList.length > 0) {
                for (FileStatus fileStatus : statusList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("filePath", fileStatus.getPath());
                    map.put("fileStatus", fileStatus.toString());
                    list.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取目录下文件列表
     *
     * @param path 目录路径
     * @return {@link List}
     */
    public List<Map<String, String>> getFileList(String path) {
        log.info("【获取目录下文件列表】 开始获取, 目录路径: {}", path);
        List<Map<String, String>> list = new ArrayList<>();
        try {
            // 递归找到所有文件
            RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(new Path(path), true);
            while (filesList.hasNext()) {
                LocatedFileStatus next = filesList.next();
                String fileName = next.getPath().getName();
                Path filePath = next.getPath();
                Map<String, String> map = new HashMap<>();
                map.put("fileName", fileName);
                map.put("filePath", filePath.toString());
                list.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     */
    public String readFile(String filePath) {
        log.info("【读取文件内容】 开始读取, 文件路径: {}", filePath);
        Path newPath = new Path(filePath);
        InputStream in = null;
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        try {
            in = fs.open(newPath);
            String line; // 用来保存每行读取的内容
            // 设置字符编码，防止中文乱码
            reader = new BufferedReader(new InputStreamReader(in, "GBK"));
            // 读取第一行
            line = reader.readLine();
            // 如果 line 为空说明读完了
            while (line != null) {
                // 将读到的内容添加到 buffer 中
                buffer.append(line);
                // 添加换行符
                buffer.append("\n");
                // 读取下一行
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            IOUtils.closeStream(in);
        }
        return buffer.toString();
    }

    /**
     * 文件或文件夹重命名
     *
     * @param oldName 旧文件或旧文件夹名称
     * @param newName 新文件或新文件夹名称
     * @return 是否更改成功 true: 成功/false: 失败
     */
    public boolean renameFile(String oldName, String newName) {
        log.info("【文件或文件夹重命名】 开始重命名, 旧文件或旧文件夹名称: {}, 新文件或新文件夹名称: {} ", oldName, newName);
        boolean isOk = false;
        Path oldPath = new Path(oldName);
        Path newPath = new Path(newName);
        try {
            // 更改名称
            isOk = fs.rename(oldPath, newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 复制文件
     *
     * @param sourcePath 复制路径
     * @param targetPath 目标路径
     */
    public void copyFile(String sourcePath, String targetPath) {
        log.info("【复制文件】 开始复制, 复制路径: {}, 目标路径: {}", sourcePath, targetPath);
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);
        FSDataInputStream inputStream;
        FSDataOutputStream outputStream;
        try {
            inputStream = fs.open(oldPath);
            outputStream = fs.create(newPath);
            IOUtils.copyBytes(inputStream, outputStream, 1024 * 1024 * 64, false);
            IOUtils.closeStreams(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
