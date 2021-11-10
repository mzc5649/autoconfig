package com.hyf.controller;

import com.hyf.entity.ConfigDataEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @create 2021/10/24 20:49
 */
@Component
public class Matching {


    public List<String> readLastNLine(File file) {
        List<String> result = new ArrayList<>();

        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        //使用随机存取
        RandomAccessFile fileRead = null;
        int pos=0;
        try {
            fileRead = new RandomAccessFile(file, "r");
            long length = fileRead.length();
            while (pos < length) {
                //开始读取
                fileRead.seek(pos);
                if (fileRead.readByte() == '\n') {
                    //使用readLine获取当前行
                    String line = fileRead.readLine();
                    //设置编码
                    line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                    line = ascii2native(line);
                    result.add(line);
                }
                pos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileRead != null) {
                try {
                    fileRead.close();
                } catch (Exception e) {
                }
            }
        }

        return result;
    }

    private static String ascii2native(String asciicode) {
        String[] asciis = asciicode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return asciicode;
        }
        return nativeValue;
    }

    /**
     * 向exchage_sql的配置文件中写入数据
     *
     * @return
     */
    public String writeData(String fileName, List<String> list) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName, true);
            list.forEach(s -> {
                try {
                    outputStream.write("\r\n".getBytes());
                    outputStream.write(s.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            outputStream.close();
            return "添加完成";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getMatching(List<String> temList, ConfigDataEntity configDataEntity, Map<String,Object> map){
        List<String> tradeData=new ArrayList<>();
        temList.forEach(tem->{
            String tem1=tem.replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                    configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                    configDataEntity.getTicker().toLowerCase())
            .replaceAll("rds_char_dollar","\\$")
            .replaceAll("jiaoyiqu",configDataEntity.getJiaoyiqu().toLowerCase())
            .replaceAll("jyqNor",configDataEntity.getJyqNor())
            .replaceAll("jyqLock",configDataEntity.getJyqLock())
            .replaceAll("eigSeq",map.get("eigSeq").toString())
            .replaceAll("ninSeq",map.get("ninSeq").toString())
            .replaceAll("firSeq",map.get("firSeq").toString())
            .replaceAll("secSeq",map.get("secSeq").toString());
            tradeData.add(tem1);
        });
        return tradeData;
    }
}
