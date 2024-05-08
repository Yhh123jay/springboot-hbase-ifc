package com.yhh.springboot_hbase_ifc.controller.FileSystem;

import com.yhh.springboot_hbase_ifc.FileSystem.HadoopClient;
import com.yhh.springboot_hbase_ifc.FileSystem.util.FileUtil;
import com.yhh.springboot_hbase_ifc.model.vo.Result;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("file")
@AllArgsConstructor
public class FileController {

    private HadoopClient hadoopClient;

    /**
     * 上传文件
     */
    @PostMapping("upload")
    public Result<?> upload(@RequestParam String uploadPath, MultipartFile file) {
        hadoopClient.copyFileToHDFS(false, true, FileUtil.multipartFileToFile(file).getPath(), uploadPath);
        return Result.build(null, 200, "上传成功");
    }

    /**
     * 下载文件
     */
    @GetMapping("download")
    public void download(@RequestParam String path, @RequestParam String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/force-download");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
        OutputStream os;
        try {
            os = response.getOutputStream();
            hadoopClient.download(path, fileName, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     */
    @PostMapping("mkdir")
    public Result<Boolean> mkdir(@RequestParam String folderPath) {
        boolean result = false;
        if (StringUtils.isNotEmpty(folderPath)) {
            result = hadoopClient.mkdir(folderPath, true);
        }
        return Result.build(result, 200, "创建目录成功");
    }

    /**
     * 目录信息
     */
    @GetMapping("getPathInfo")
    public Result<List<Map<String, Object>>> getPathInfo(@RequestParam String path) {
        return Result.build(hadoopClient.getPathInfo(path), 200, "获取目录信息成功");
    }

    /**
     * 获取目录下文件列表
     */
    @GetMapping("getFileList")
    public Result<List<Map<String, String>>> getFileList(@RequestParam String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        return Result.build(hadoopClient.getFileList(path), 200, "获取目录下文件列表成功");
    }

    /**
     * 删除文件或文件夹
     */
    @PostMapping("rmdir")
    public Result<?> rmdir(@RequestParam String path, @RequestParam(required = false) String fileName) {
        hadoopClient.rmdir(path, fileName);
        return Result.build(null, 200, "删除成功");
    }

    /**
     * 读取文件内容
     */
    @GetMapping("readFile")
    public Result<Object> readFile(@RequestParam String filePath) {
        return Result.build((Object) hadoopClient.readFile(filePath), 200, "读取成功");
    }

    /**
     * 文件或文件夹重命名
     */
    @PostMapping("renameFile")
    public Result<Boolean> renameFile(@RequestParam String oldName, @RequestParam String newName) {
        return Result.build(hadoopClient.renameFile(oldName, newName), 200, "重命名成功");
    }

    /**
     * 上传本地文件
     */
    @PostMapping("uploadFileFromLocal")
    public Result<?> uploadFileFromLocal(@RequestParam String path, @RequestParam String uploadPath) {
        hadoopClient.copyFileToHDFS(false, true, path, uploadPath);
        return Result.build(null, 200, "上传成功");
    }

    /**
     * 下载文件到本地
     */
    @PostMapping("downloadFileFromLocal")
    public Result<?> downloadFileFromLocal(@RequestParam String path, @RequestParam String downloadPath) {
        hadoopClient.downloadFileFromLocal(path, downloadPath);
        return Result.build(null, 200, "下载成功");
    }

    /**
     * 复制文件
     */
    @PostMapping("copyFile")
    public Result<?> copyFile(String sourcePath, String targetPath) {
        hadoopClient.copyFile(sourcePath, targetPath);
        return Result.build(null, 200, "复制成功");
    }
}
