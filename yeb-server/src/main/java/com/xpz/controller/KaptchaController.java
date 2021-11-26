package com.xpz.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码
 *
 * @Author: Catherine
 */
@RestController
public class KaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码")
    @GetMapping(value = "/kaptcha", produces = "image/jpeg")
    public void kaptcha(HttpServletRequest request, HttpServletResponse response){
        //定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        //Set standard HTTP/1.1 no-cache headers.
        response.addHeader("Cache-Control", "post-check=0,pre-check=0");
        //Set standard HTTP/1.0 no-chche header.
        response.setHeader("Pragma", "no-cache");
        //return a jpeg
        response.setContentType("image/jpeg");

        //-------------生成验证码 begin  -------------
        String text = defaultKaptcha.createText();
        System.out.println("验证码内容： " + text);
        //将验证码文本内容放入session
        request.getSession().setAttribute("kaptcha", text);
        //创建图片验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片，格式为jpeg
            ImageIO.write(image,"jpeg",outputStream);
            System.out.println("已输出图片");
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != outputStream){
                try{
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        //-------------生成验证码 end  ---------------
    }
}
