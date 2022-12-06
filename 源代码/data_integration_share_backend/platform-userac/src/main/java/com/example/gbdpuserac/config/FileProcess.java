package com.example.gbdpuserac.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.gbdpbootcore.core.ProjectConstant.FILE_BASE_PATH;
import static com.example.gbdpuserac.core.ProjectConstant.*;

public class FileProcess {
    public static boolean saveFile(MultipartFile file,String destDir) {
        File dest = new File(destDir);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        if(!dest.exists()) {
            try {
                dest.createNewFile();
                dest = new File(dest.getAbsolutePath());
                file.transferTo(dest);
                return true;
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return false;
    }

    public static String[] getNewFileDir(String fileName){
        String[] two =new String[2];
        String tmp = DateProcess.format_year_month.format(System.currentTimeMillis())+
                "/"+System.currentTimeMillis()+"_"+fileName;
        two[0] = file_prefix+tmp;
        two[1] = file_prefix_url+tmp;
        return two;
    }

    public static int getStrIndex(String[] arrays,String str){
        for(int i=0;i<arrays.length;i++){
            if(str.equals(arrays[i]))
                return i;
        }
        return -1;
    }
    public static String getOSName(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return XPDF_DIR+"win/"+XPDF_PIC_WIN;
        }else{
            return XPDF_PIC_LINUX;
        }
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
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

    public static void transPDFToPic(String PDFDir,String picDir){
        if(PDFDir==null||PDFDir.isEmpty())
            return;
        String[] houzui = PDFDir.split("\\.");
        if (houzui.length<=0||!houzui[houzui.length-1].equals("pdf")){
            return;
        }
        if(PDFDir==null||PDFDir.trim().isEmpty())
            return;
        PDFDir = FILE_BASE_PATH+"/"+PDFDir.split("_")[0]+"/"+PDFDir;
        picDir = XPDF_PIC_RESULT_DIR + picDir;
        File result = new File(picDir);
        if(!result.exists()){
            result.mkdirs();
        }else{
            if(deleteDirectory(picDir)){
                result.mkdirs();
            }
        }
        picDir = picDir + File.separator;
        String[] cmd = {getOSName(),"-f","1","-l","7","-r","100",PDFDir,picDir};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getImgList(String src,String url) {
        File dir = new File(src);
        List<String> pics = new ArrayList<>();
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        String[] files = dir.list();
        int tmplen = 6;
        if(files.length<=tmplen&&files.length>2){
            tmplen = files.length-2;
        }
        if(files.length<=2)
            tmplen=0;
        for (int i = 0; i < tmplen; i++) {
            File file = new File(dir, "-00000"+String.valueOf(i+1)+".png");
            if (file.isFile()) {// 如果文件
                pics.add(url + file.getName());
            }
        }
        return pics;

    }

    public static boolean validImg(String src,String filename){
        File dir = new File(src);
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        String[] files = dir.list();
        int tmplen = 6;
        if(files.length<=tmplen&&files.length>2){
            tmplen = files.length-2;
        }
        if(files.length<=2)
            tmplen=0;
        for (int i = 0; i < tmplen; i++) {
            String tmpmm = "-00000"+String.valueOf(i+1)+".png";
            if(tmpmm.equals(filename))
                return true;
        }
        return false;
    }


}
