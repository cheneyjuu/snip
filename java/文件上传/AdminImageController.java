package com.pufeng.portal.web.admin;

import com.pufeng.portal.entity.Content;
import com.pufeng.portal.entity.Image;
import com.pufeng.portal.service.admin.ContentService;
import com.pufeng.portal.service.admin.ImageService;
import com.pufeng.portal.utility.HandlerUpload;
import com.pufeng.portal.utility.ImageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Juchen
 * Date: 13-7-8
 * Time: 上午9:47
 */
@Controller
@RequestMapping (value = "/admin/image")
public class AdminImageController {

    @Autowired
    private ContentService contentService;
    @Autowired
    private ImageService imageService;

    /**
     * 上传主题图片
     * @param file
     * @param request
     * @return
     */
    @RequestMapping (value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,
                         HttpServletRequest request,Model model){
        HandlerUpload handlerUpload = new HandlerUpload();
        Map<String, String> fileNameAndPathMap = null;
        try {
            InputStream iis = file.getInputStream();
            BufferedImage bi= ImageIO.read(iis);
            int originHeight = bi.getHeight();//高
            int originWidth = bi.getWidth();//宽
            //  4*4缩略图宽高
            int middleWidth = bi.getWidth()>200?200:bi.getWidth();
            int middleHeight = bi.getHeight()>220?220:bi.getHeight();
            //  画廊缩略图
            int bigThumbWidth = bi.getWidth()>900?900:bi.getWidth();
            int bigThumbHeight = bi.getHeight()>600?600:bi.getHeight();
            int smallThumbWidth = bi.getWidth()>200?200:bi.getWidth();
            int smallThumbHeight = bi.getHeight()>150?150:bi.getHeight();

            fileNameAndPathMap = handlerUpload.uploadFile(file,request);
            //  真实路径
            String realPath = request.getSession().getServletContext().getRealPath(fileNameAndPathMap.get("filePath"))+"/";
            //  定义文件名
            File inFile = new File(realPath+fileNameAndPathMap.get("fileName"));
            File middleOutFile = new File(realPath+"m_"+fileNameAndPathMap.get("fileName"));
            File bigThumbOutFile = new File(realPath+"bt_"+fileNameAndPathMap.get("fileName"));
            File smallThumOutFile = new File(realPath+"st_"+fileNameAndPathMap.get("fileName"));

            ImageTools.convert(inFile, middleOutFile, middleWidth, middleHeight, 90);
            ImageTools.convert(inFile,bigThumbOutFile,bigThumbWidth,bigThumbHeight,90);
            ImageTools.convert(inFile,smallThumOutFile,smallThumbWidth,smallThumbHeight,90);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  fileNameAndPathMap.get("filePath")+fileNameAndPathMap.get("fileName");
    }

    @RequestMapping (value = "/singleUpload/{contentId}")
    public String upload(@Valid @ModelAttribute("perloadImage") Image image,
                         @PathVariable (value = "contentId") Long contentId,
                               @RequestParam("file") MultipartFile[] files,
                               HttpServletRequest request){
        Content content = contentService.findById(contentId);
        List<Image> imageList = new ArrayList<Image>();
        HandlerUpload handlerUpload = new HandlerUpload();
        Map<String, String> fileNameAndPathMap = null;
        try {
            for(MultipartFile file : files){
                InputStream iis = file.getInputStream();
                BufferedImage bi = ImageIO.read(iis);
                int originHeight = bi.getHeight();//高
                int originWidth = bi.getWidth();//宽
                //  4*4缩略图宽高
                int middleWidth = bi.getWidth()>200?200:bi.getWidth();
                int middleHeight = bi.getHeight()>220?220:bi.getHeight();
                //  画廊缩略图
                int bigThumbWidth = bi.getWidth()>900?900:bi.getWidth();
                int bigThumbHeight = bi.getHeight()>600?600:bi.getHeight();
                int smallThumbWidth = bi.getWidth()>200?200:bi.getWidth();
                int smallThumbHeight = bi.getHeight()>150?150:bi.getHeight();

                fileNameAndPathMap = handlerUpload.uploadFile(file,request);
                //  真实路径
                String realPath = request.getSession().getServletContext().getRealPath(fileNameAndPathMap.get("filePath"))+"/";
                //  定义文件名
                File inFile = new File(realPath+fileNameAndPathMap.get("fileName"));
                File middleOutFile = new File(realPath+"m_"+fileNameAndPathMap.get("fileName"));
                File bigThumbOutFile = new File(realPath+"bt_"+fileNameAndPathMap.get("fileName"));
                File smallThumOutFile = new File(realPath+"st_"+fileNameAndPathMap.get("fileName"));
                ImageTools.convert(inFile, middleOutFile, middleWidth, middleHeight, 90);
                ImageTools.convert(inFile,bigThumbOutFile,bigThumbWidth,bigThumbHeight,90);
                ImageTools.convert(inFile,smallThumOutFile,smallThumbWidth,smallThumbHeight,90);

                image.setImageName(fileNameAndPathMap.get("fileName"));
                image.setMiddleName("m_"+fileNameAndPathMap.get("fileName"));
                image.setBigName("bt_"+fileNameAndPathMap.get("fileName"));
                image.setSmallName("st_"+fileNameAndPathMap.get("fileName"));
                imageList.add(image);
            }
            contentService.save(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/image/list/"+contentId.toString();
    }

    @RequestMapping (value = "/delete/{contentId}/{imageId}")
    public String delete(@PathVariable (value = "contentId") Long contentId,
                         @PathVariable (value = "imageId") Long imageId){
        imageService.delete(imageId);
        return "redirect:/admin/image/list/"+contentId.toString();
    }

    @RequestMapping (value = "/delete/{imageId}")
    @ResponseBody
    public String delete(@PathVariable (value = "imageId") Long imageId){
        imageService.delete(imageId);
        return null;
    }

    @RequestMapping (value = "/list/{contentId}")
    public String list(@PathVariable (value = "contentId") Long contentId,
                       Model model){
        List<Image> imageList = contentService.findById(contentId).getImageList();
        model.addAttribute("imageList", imageList);
        model.addAttribute("contentId", contentId);
        return "admin/imageList";
    }

    @ModelAttribute ("perloadImage")
    public Image getImage(@RequestParam (value = "id", required = false) Long id){
        if (id!=null){
            return imageService.findById(id);
        }
        return null;
    }
}
