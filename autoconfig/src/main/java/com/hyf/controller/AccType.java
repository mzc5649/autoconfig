package com.hyf.controller;

import com.hyf.entity.ConfigDataEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author hyf
 * @create 2021/10/21 11:46
 */
@Component
public class AccType {
    /*public static void main(String[] args) {
        List<String> list = readLastNLine(new File("E:\\cointiger\\exchange-commons\\src\\main\\resources\\AccType.properties"));
     list.forEach(s -> {
         System.out.println(s);
     });
    }*/
    /**
     * 读取最后n行内容
     * 根据换行符判断当前的行数，
     * 使用统计来判断当前读取第N行
     *
     * @param file
     * @return
     */
    public List<String> readLastNLine(File file) {
        List<String> result = new ArrayList<>();
        List<String> coins = new ArrayList<String>();
        List<String> coin = new ArrayList<String>();

        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        //使用随机存取
        RandomAccessFile fileRead = null;
        try {
            fileRead = new RandomAccessFile(file, "r");
            long length = fileRead.length();
            long pos = length - 1;
            while (pos > 0) {
                pos--;
                //开始读取
                fileRead.seek(pos);
                if (fileRead.readByte() == '\n') {
                    //使用readLine获取当前行
                    String line = fileRead.readLine();
                    if (!line.equals("")) {
                        //设置编码
                        line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                        line = ascii2native(line);
                        if (line.contains("交易对")) {
                            if (coins.size() < 2) {
                                coins.add(line);
                            }
                        } else {
                            if (coin.size() < 8) {
                                coin.add(line);
                            }
                        }
                        if (coins.size() == 2 && coin.size() == 8) {
                            break;
                        }
                    }
                }
            }
            Collections.reverse(coin);
            Collections.reverse(coins);
            result.addAll(coins);
            result.addAll(coin);
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

    /**
     * 向AccType配置文件中写入数据
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

    public List<String> readEigSeqNinSeq(File file,ConfigDataEntity entity) {
        List<String> result = new ArrayList<>();

        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        //使用随机存取
        RandomAccessFile fileRead = null;
        try {
            fileRead = new RandomAccessFile(file, "r");
            long length = fileRead.length();
            long pos = length - 1;
            while (pos > 0) {
                pos--;
                //开始读取
                fileRead.seek(pos);
                if (fileRead.readByte() == '\n') {
                    //使用readLine获取当前行
                    String line = fileRead.readLine();
                    if (!line.equals("")) {
                        //设置编码
                        line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                        line = ascii2native(line);
                        if (line.contains(entity.getTicker().toUpperCase()+"_LOCK")||line.contains(entity.getTicker().toUpperCase()+"_NORMAL")) {
                               result.add(line);
                        }
                        if (result.size()==2) {
                            break;
                        }
                    }
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
}
