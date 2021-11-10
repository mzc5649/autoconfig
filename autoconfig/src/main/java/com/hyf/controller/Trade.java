package com.hyf.controller;

import com.hyf.entity.ConfigDataEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hyf
 * @create 2021/10/22 18:34
 */
@Component
public class Trade {

    /*public static void main(String[] args) {
        ConfigDataEntity configDataEntity=new ConfigDataEntity();
        configDataEntity.setConfigType("1");
       List<String> result= readLastNLine(new File("E:\\myproject\\autoconfig\\src\\main\\resources\\static\\exchange_trade.txt"),configDataEntity);
       result.forEach(s -> {
           System.out.println(s);
       });
    }
*/
    public List<String> readLastNLine(File file,ConfigDataEntity entity) {
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
                    if (!line.equals("")) {
                        //设置编码
                        line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                        line = ascii2native(line);
                        result.add(line);
                    }
                }
                pos++;
            }
            if (entity.getConfigType().equals("2")){
                for (int i = 0; i < 9; i++) {
                    result.remove(result.size()-1);
                }
            }else if (entity.getConfigType().equals("1")){
                for (int i = 0; i < 12; i++) {
                    result.remove(0);
                }
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
     * 向配置文件中写入数据
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

    public List<String> getTrade(List<String> temList, ConfigDataEntity configDataEntity){
        List<String> tradeData=new ArrayList<>();
        if (configDataEntity.getConfigType().equals("1")){
            temList.forEach(tem->{
                String tem1=tem.replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                        configDataEntity.getTicker().toLowerCase())
                        .replaceAll("rds_char_dollar","\\$")
                    .replaceAll("jiaoyiqu",configDataEntity.getJiaoyiqu().toLowerCase())
                    .replaceAll("volumeLen",configDataEntity.getVolumeLen())
                    .replaceAll("dealMinLim",configDataEntity.getDealMinLim())
                    .replaceAll("shendu1",configDataEntity.getShendu1())
                    .replaceAll("shendu2",configDataEntity.getShendu2())
                    .replaceAll("shendu3",configDataEntity.getShendu3())
                    .replaceAll("yinying",configDataEntity.getYinying());
                tradeData.add(tem1);
            });
        }else if (configDataEntity.getConfigType().equals("2")){
            temList.forEach(tem->{
                String tem1=tem.replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                        configDataEntity.getTicker().toLowerCase())
                        .replaceAll("rds_char_dollar","\\$")
                    .replaceAll("withdrawMin",configDataEntity.getWithdrawMin())
                    .replaceAll("withdrawMax",configDataEntity.getWithdrawMax())
                    .replaceAll("feeMax",configDataEntity.getFeeMax())
                    .replaceAll("feeMin",configDataEntity.getFeeMin())
                    .replaceAll("withdrawNum",configDataEntity.getWithdrawNum());
                tradeData.add(tem1);
            });
        }else {
            temList.forEach(tem->{
                String tem1=tem.replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                        configDataEntity.getTicker().toLowerCase())
                        .replaceAll("rds_char_dollar","\\$")
                        .replaceAll("withdrawMin",configDataEntity.getWithdrawMin())
                        .replaceAll("withdrawMax",configDataEntity.getWithdrawMax())
                        .replaceAll("feeMax",configDataEntity.getFeeMax())
                        .replaceAll("feeMin",configDataEntity.getFeeMin())
                        .replaceAll("withdrawNum",configDataEntity.getWithdrawNum())
                        .replaceAll("jiaoyiqu",configDataEntity.getJiaoyiqu().toLowerCase())
                        .replaceAll("volumeLen",configDataEntity.getVolumeLen())
                        .replaceAll("dealMinLim",configDataEntity.getDealMinLim())
                        .replaceAll("shendu1",configDataEntity.getShendu1())
                        .replaceAll("shendu2",configDataEntity.getShendu2())
                        .replaceAll("shendu3",configDataEntity.getShendu3())
                        .replaceAll("yinying",configDataEntity.getYinying());
                tradeData.add(tem1);
            });
        }
        return tradeData;
    }

}
