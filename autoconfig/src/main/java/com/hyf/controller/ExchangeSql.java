package com.hyf.controller;

import com.hyf.entity.ConfigDataEntity;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @create 2021/10/22 19:05
 */
@Component
public class ExchangeSql {

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
                    if (line!=null){
                        line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                        line = ascii2native(line);
                    }
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

   //往exchange-sql的文件中插入的数据
    public List<String> getExchangeSql1(List<String> temSql, ConfigDataEntity configDataEntity, Map<String,Object> map){
        List<String> sqlData=new ArrayList<>();
        if (configDataEntity.getConfigType().equals("0")) {
            temSql.forEach(s -> {
                String s1 = s.replaceAll("eigSeq", map.get("eigSeq").toString())
                        .replaceAll("ninSeq", map.get("ninSeq").toString())
                        .replaceAll("tenSeq", map.get("tenSeq").toString())
                        .replaceAll("firSeq", map.get("firSeq").toString())
                        .replaceAll("secSeq", map.get("secSeq").toString())
                        .replaceAll("thirSeq", map.get("thirSeq").toString())
                        .replaceAll("forSeq", map.get("forSeq").toString())
                        .replaceAll("fifSeq", map.get("fifSeq").toString())
                        .replaceAll("sixSeq", map.get("sixSeq").toString())
                        .replaceAll("sevSeq", map.get("sevSeq").toString())
                        .replaceAll("TICKER", configDataEntity.getTicker().toUpperCase().contains("$")?
                                configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toUpperCase():
                                configDataEntity.getTicker().toUpperCase())
                        .replaceAll("RDS_CHAR_DOLLAR","\\$")
                        .replaceAll("JIAOYIQU", configDataEntity.getJiaoyiqu().toUpperCase())
                        .replaceAll("ticker", configDataEntity.getTicker().toLowerCase().contains("$")?
                                configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                                configDataEntity.getTicker().toLowerCase())
                        .replaceAll("rds_char_dollar","\\$")
                        .replaceAll("jiaoyiqu", configDataEntity.getJiaoyiqu().toLowerCase())
                        .replaceAll("xuhao", configDataEntity.getXuhao())
                        .replaceAll("leixing", configDataEntity.getLeixing().toLowerCase())
                        .replaceAll("realname", configDataEntity.getRealname().contains("$")?
                                configDataEntity.getRealname().replaceAll("\\$","RDS_CHAR_DOLLAR"):
                                configDataEntity.getRealname())
                        .replaceAll("RDS_CHAR_DOLLAR","\\$")
                        .replaceAll("querenshu", configDataEntity.getQuerenshu())
                        .replaceAll("addUrl", configDataEntity.getAddUrl())
                        .replaceAll("txUrl", configDataEntity.getTxUrl())
                        .replaceAll("bigNum", configDataEntity.getBigNum())
                        .replaceAll("withdrawMin", configDataEntity.getWithdrawMin())
                        .replaceAll("withdrawMax", configDataEntity.getWithdrawMax())
                        .replaceAll("feeMin", configDataEntity.getFeeMin())
                        .replaceAll("feeMax", configDataEntity.getFeeMax())
                        .replaceAll("withdrawNum", configDataEntity.getWithdrawNum())
                        .replaceAll("countNum", configDataEntity.getCountNum())
                        .replaceAll("usdtNum", configDataEntity.getUsdtNum());
                sqlData.add(s1);
            });
        }else if (configDataEntity.getConfigType().equals("1")){
            temSql.forEach(s -> {
                String s1 = s
                        .replaceAll("firSeq", map.get("firSeq").toString())
                        .replaceAll("secSeq", map.get("secSeq").toString())
                        .replaceAll("TICKER", configDataEntity.getTicker().toUpperCase().contains("$")?
                                configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toUpperCase():
                                configDataEntity.getTicker().toUpperCase())
                        .replaceAll("RDS_CHAR_DOLLAR","\\$")
                        .replaceAll("JIAOYIQU", configDataEntity.getJiaoyiqu().toUpperCase())
                        .replaceAll("ticker", configDataEntity.getTicker().toLowerCase().contains("$")?
                                configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                                configDataEntity.getTicker().toLowerCase())
                        .replaceAll("rds_char_dollar","\\$")
                        .replaceAll("jiaoyiqu", configDataEntity.getJiaoyiqu().toLowerCase())
                        .replaceAll("xuhao", configDataEntity.getXuhao());
                sqlData.add(s1);
            });
        }else {
            temSql.forEach(s -> {
                String s1 = s.replaceAll("eigSeq", map.get("eigSeq").toString())
                        .replaceAll("ninSeq", map.get("ninSeq").toString())
                        .replaceAll("tenSeq", map.get("tenSeq").toString())
                        .replaceAll("thirSeq", map.get("thirSeq").toString())
                        .replaceAll("forSeq", map.get("forSeq").toString())
                        .replaceAll("fifSeq", map.get("fifSeq").toString())
                        .replaceAll("sixSeq", map.get("sixSeq").toString())
                        .replaceAll("sevSeq", map.get("sevSeq").toString())
                        .replaceAll("TICKER", configDataEntity.getTicker().toUpperCase().contains("$")?
                                configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toUpperCase():
                                configDataEntity.getTicker().toUpperCase())
                        .replaceAll("RDS_CHAR_DOLLAR","\\$")
                        .replaceAll("ticker", configDataEntity.getTicker().toLowerCase().contains("$")?
                                configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                                configDataEntity.getTicker().toLowerCase())
                        .replaceAll("rds_char_dollar","\\$")
                        .replaceAll("leixing", configDataEntity.getLeixing().toLowerCase())
                        .replaceAll("realname", configDataEntity.getRealname())
                        .replaceAll("querenshu", configDataEntity.getQuerenshu())
                        .replaceAll("addUrl", configDataEntity.getAddUrl())
                        .replaceAll("txUrl", configDataEntity.getTxUrl())
                        .replaceAll("bigNum", configDataEntity.getBigNum())
                        .replaceAll("withdrawMin", configDataEntity.getWithdrawMin())
                        .replaceAll("withdrawMax", configDataEntity.getWithdrawMax())
                        .replaceAll("feeMin", configDataEntity.getFeeMin())
                        .replaceAll("feeMax", configDataEntity.getFeeMax())
                        .replaceAll("withdrawNum", configDataEntity.getWithdrawNum())
                        .replaceAll("countNum", configDataEntity.getCountNum())
                        .replaceAll("usdtNum", configDataEntity.getUsdtNum());
                sqlData.add(s1);
            });
        }
        return sqlData;
    }

    public List<String> getExchangeSql2(List<String> temSql,ConfigDataEntity configDataEntity){
        List<String> sqlData=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        temSql.forEach(s -> {
            String s1= s.replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                    configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                    configDataEntity.getTicker().toLowerCase())
                    .replaceAll("rds_char_dollar","\\$")
             .replaceAll("jiaoyiqu",configDataEntity.getJiaoyiqu().toLowerCase())
             .replaceAll("riqi",sdf.format(new Date()));
             sqlData.add(s1);
        });
        return sqlData;
    }
}
