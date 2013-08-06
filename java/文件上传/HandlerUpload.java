package com.pufeng.portal.utility;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: chen
 * Date: 13-1-13
 * Time: 下午4:32
 */
public class HandlerUpload {

    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/");
        /**构建图片保存的目录**/
        String logoPathDir = "/upload/files"+ dateformat.format(new Date());
        /**得到图片保存目录的真实路径**/
        String logoRealPathDir = request.getSession().getServletContext().getRealPath(logoPathDir)+"/";
        /**根据真实路径创建目录**/
        File logoSaveFile = new File(logoRealPathDir);
        if(!logoSaveFile.exists())
            logoSaveFile.mkdirs();
        String fileName = null;

        if (!file.isEmpty()) {
            try {
                fileName = generateFileName(file.getOriginalFilename());
                byte[] files = file.getBytes();
                // 处理上传
                this.copyFile(file.getInputStream(), fileName, logoRealPathDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> fileNameAndPathMap = new HashMap<String, String>();
        fileNameAndPathMap.put("fileName",fileName);
        fileNameAndPathMap.put("filePath",logoPathDir);
        return fileNameAndPathMap;
    }

    /**
     * 写文件到本地
     * @param in
     * @param fileName
     * @throws java.io.IOException
     */
    private void copyFile(InputStream in,String fileName, String filePath) throws IOException {
        FileOutputStream fs = new FileOutputStream(filePath
                + fileName);
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = in.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        in.close();
    }

    /**
     * <h2>为上传文件自动分配文件名称，避免重复</h2>
     *
     * @param fileName
     *            <tt>要重命名的文件名</tt>
     * @return
     */
    private String generateFileName(String fileName) {
        // 获得当前时间
        DateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        // 转换为字符串
        String formatDate = format.format(new Date());
        // 随机生成文件编号
        int random = new Random().nextInt(10000);
        // 获得文件后缀名称
        int position = fileName.lastIndexOf(".");
        String extension = fileName.substring(position);
        // 组成一个新的文件名称
        return formatDate + random + extension;
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    boolean flag;
    File file;
    public boolean DeleteFolder(String sPath) {
        flag = false;
        file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}
