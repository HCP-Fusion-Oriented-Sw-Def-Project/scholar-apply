package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.core.ProjectConstant;
import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpuserac.config.DateProcess;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/uac/file")
@Slf4j
public class UacFileController {

    private final ResourceLoader resourceLoader;

    private static List<String> ACCEPT_IMAGE_TYPE_LIST = Arrays.asList("png", "jpeg", "jpg", "jpe", "ico", "tiff");

    private static Map<String, String> ACCEPT_IMAGE_CONTENT_TYPE_MAP = Maps.newHashMap();
    {
        ACCEPT_IMAGE_CONTENT_TYPE_MAP.put("png", "image/png");
        ACCEPT_IMAGE_CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        ACCEPT_IMAGE_CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        ACCEPT_IMAGE_CONTENT_TYPE_MAP.put("jpe", "image/jpeg");
        ACCEPT_IMAGE_CONTENT_TYPE_MAP.put("ico", "image/x-icon");
        ACCEPT_IMAGE_CONTENT_TYPE_MAP.put("tiff", "image/tiff");
    }

    @Autowired
    public UacFileController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
    @ApiOperation("预览图片文件接口")
    @GetMapping("/image/{filename:.+}")
    public Result getFile(@PathVariable String filename, HttpServletResponse response) {

        String[] split = filename.split("\\.");
        if (split.length == 0) {
            return ResultGenerator.genFailResult(ResultCode.ILLEGAL_PARAMETER, "该文件名称不合法！");
        }
        String fileType = split[split.length - 1];
        if (!ACCEPT_IMAGE_TYPE_LIST.contains(fileType)) {
            return ResultGenerator.genFailResult("该文件格式不支持预览！");
        }
        String monthPath = filename.split("_")[0];
        Resource resource = resourceLoader.getResource("file:" + Paths.get(ProjectConstant.FILE_BASE_PATH + "/" + monthPath, filename));
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        response.setContentType(ACCEPT_IMAGE_CONTENT_TYPE_MAP.get(fileType));

        fileLoad(response, resource);
        return ResultGenerator.genSuccessResult("文件预览成功！");
    }

    @ApiOperation("上传文件接口")
    @PostMapping("/upload")
    public Result handleFileUpload(@RequestParam("file") MultipartFile file) {
        String originalFilename = file == null ? "" : file.getOriginalFilename();
        if (file != null && !file.isEmpty()) {
            try {
                originalFilename = genFileName(file.getOriginalFilename());
                String monthPath = originalFilename.split("_")[0];
                File baseDir = new File(ProjectConstant.FILE_BASE_PATH + "/" + monthPath);
                if (!baseDir.getParentFile().exists()) {
                    baseDir.getParentFile().mkdirs();
                }
                if (!baseDir.exists()) {
                    baseDir.mkdirs();
                }
                Files.copy(file.getInputStream(), Paths.get(ProjectConstant.FILE_BASE_PATH + "/" + monthPath, originalFilename));
                return ResultGenerator.genSuccessResult(originalFilename);
            } catch (IOException | RuntimeException e) {
                return ResultGenerator.genFailResult("上传失败：" + originalFilename + "!");
            }
        }
        return ResultGenerator.genFailResult("上传失败：" + originalFilename + "，文件是空的!");
    }

    @ApiOperation("下载文件接口")
    @GetMapping("/download/{fileName:.+}")
    public Result fileDownload(HttpServletResponse response, @PathVariable String fileName) {
        String monthPath = fileName.split("_")[0];
        Resource resource = resourceLoader.getResource("file:" + Paths.get(ProjectConstant.FILE_BASE_PATH + "/" + monthPath, fileName));
        response.setContentType("multipart/form-data;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("GB2312"), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        fileLoad(response, resource);
        //成功后返回成功信息
        return ResultGenerator.genSuccessResult("文件下载成功！");
    }


    private void fileLoad(HttpServletResponse response, Resource resource) {
        //创建缓冲输入流
        BufferedInputStream bis = null;
        OutputStream outputStream = null;
        String filename = resource.getFilename();
        byte[] buff = new byte[1024];

        try {
            File file = resource.getFile();
            outputStream = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int read = bis.read(buff);
            //通过while循环写入到指定了的文件夹中
            while (read != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                read = bis.read(buff);
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            log.error("查看文件异常！！！ filename={},", filename, e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String genFileName(String fileName) {
        //使用下划线把UUID和文件名分割开来，后面可能会解析文件名的。
        return DateProcess.format_year_month.format(System.currentTimeMillis())+
                "_" + UUID.randomUUID().toString() + "_"+ fileName;
    }
}
