package com.ssq.util;

import com.ssq.pojo.Blog;
import org.pegdown.PegDownProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public static String md2Html(String path) throws IOException {
        String html = null;
        //记录去除掉format_matter后的开始位置
        int html_start=0;
//        FileReader r = new FileReader(path);
//        BufferedReader buf=new BufferedReader(r);
//        //缓冲区过小会出现一定的问题
//        char[] cbuf = new char[1024*256];
//        while( buf.read(cbuf) != -1){
//            html += new String(cbuf);
//        }
//        buf.close();

        //使用readLine读取
        FileReader r = new FileReader(path);
        BufferedReader buf=new BufferedReader(r);
        String line;
        while( (line=buf.readLine()) != null){
            html +=line+'\n';
        }
        buf.close();

        String reg="---(.*?)---";
        Pattern patten = Pattern.compile(reg,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);//编译正则表达式
        Matcher matcher = patten.matcher(html);// 指定要匹配的字符串
        if (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            //获取第一个匹配到的偏移量
            html_start=matcher.end(0);
        }
        html=html.substring(html_start+1);
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        html = pdp.markdownToHtml(html);
        return html;
    }

    public static Map<String,Object> getMsgFromFrontMatter(String path) throws IOException {
        Map<String,Object> msgMap=new HashMap<>();
        //记录str类的html信息
        String html = null;
        //记录msg信息
        String msg=null;
        //读取文件
//        FileReader r = new FileReader(path);
//        char[] cbuf = new char[1024];
//        while( r.read(cbuf) != -1){
//            html += new String(cbuf);
//        }
//        r.close();

        //为了加快效率改成读取前10行
        FileReader r = new FileReader(path);
        BufferedReader buf=new BufferedReader(r);
        String line;
        int row=10;
        while( (line=buf.readLine()) != null &&row!=0){
            html +=line+'\n';
            --row;
        }
        buf.close();

        //正则匹配
        String reg="---(.*?)---";
        Pattern patten = Pattern.compile(reg,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);//编译正则表达式
        Matcher matcher = patten.matcher(html);// 指定要匹配的字符串
        if (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            msg=matcher.group();
        }
        //将信息存储进msgMap中
        //tags 用list存储
        //title date description 用String存储
        if(msg!=null)
        {
            msg=msg.substring(4,msg.length()-4);
            String []key_value=msg.split("\n");
            for(String msgMapItem:key_value)
            {
                String []tmp=msgMapItem.split(":");
                String key=tmp[0];
                String value=tmp[1];
                if(!key.equals("tags"))
                    msgMap.put(key.trim(),value.trim());
                else
                {
                    List<String> list=new LinkedList<>();
                    value=value.trim();
                    String []values=value.split(" ");
                    for(String t :values)
                    {
                        if(!t.equals(" "))
                            list.add(t);
                    }
                    msgMap.put(key,list);
                }
            }
        }
        return msgMap;
    }


    public static boolean saveFile(MultipartFile file, String filePath)
    {
        if(file==null||file.isEmpty())
            return false;
        try(InputStream inputStream=file.getInputStream())
        {
            Path uploadPath= Paths.get(filePath);
            if(!uploadPath.toFile().exists())
                uploadPath.toFile().mkdir();

            //如果文件存在则删除之前的文件
            deleteFile(file.getOriginalFilename(),filePath);
            Files.copy(inputStream, Paths.get(filePath).resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    public static boolean deleteFile(String filename,String filePath)
    {
        if(filename==null||filename.isEmpty())
            return false;
        try{
            File file=new File(String.valueOf(Paths.get(filePath).resolve(filename)));
            if(!file.exists())
            {
                return false;
            }
            else
                file.delete();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
}
