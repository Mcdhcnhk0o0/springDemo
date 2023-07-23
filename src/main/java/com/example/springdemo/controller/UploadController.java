package com.example.springdemo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UploadController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    @PostMapping("/upload")
    public String upload(MultipartFile multipartfile, HttpServletRequest request){
        String realPath = request.getSession().getServletContext().getRealPath("/update/");
        String format=sdf.format(new Date());
        File folder=new File(realPath+format);
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        String oldname = multipartfile.getOriginalFilename();
        String newname = UUID.randomUUID().toString()+oldname.substring(oldname.lastIndexOf("."),oldname.length());
        try {
            multipartfile.transferTo(new File(folder, newname));
            System.out.println(new File(folder, newname).getAbsolutePath());//输出（上传文件）保存的绝对路径
            String filePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/update/"+format+newname;
            return filePath+"上传成功";
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "上传失败!";
    }

}
