package com.hyf.controller;

import com.hyf.entity.ConfigDataEntity;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hyf
 * @create 2021/10/21 18:41
 */
@Controller
public class AutoConfigController {

    @Autowired
    private AccType accType;
    @Autowired
    private Trade trade;
    @Autowired
    private ExchangeSql exchangeSql;
    @Autowired
    private Matching matching;
    @Autowired
    private Enums enums;

    @Value("${AccType_path}")
    private String accTypePath;
    @Value("${AccType_tem_path}")
    private String accTypeTemPath;
    @Value("${trade_tem_path}")
    private String tradeTemPath;
    @Value("${trade_awsDev_path}")
    private String tradeAwsDevPath;
    @Value("${trade_awsLive_path}")
    private String tradeAwsLivePath;
    @Value("${trade_dev_path}")
    private String tradeDevPath;
    @Value("${trade_live_path}")
    private String tradeLivePath;
    @Value("${two_path}")
    private String twoPath;
    @Value("${api_awsDev_path}")
    private String apiAwsDevPath;
    @Value("${api_awsLive_path}")
    private String apiAwsLivePath;
    @Value("${api_awsLiveTwo_path}")
    private String apiAwsLiveTwoPath;
    @Value("${api_dev_path}")
    private String apiDevPath;
    @Value("${api_live_path}")
    private String apiLivePath;
    @Value("${sql_tem_path1}")
    private String sqlTemPath1;
    @Value("${sql_tem_path2}")
    private String sqlTemPath2;
    @Value("${sql_tem_path3}")
    private String sqlTemPath3;
    @Value("${sql_tem2_path}")
    private String sqlTem2Path;
    @Value("${matching_path}")
    private String matchingPath;
    @Value("${coinAddressUrlType1}")
    private String coinAddressUrlType1;
    @Value("${coinSymbol1}")
    private String coinSymbol1;
    @Value("${coinSymbolDepth1}")
    private String coinSymbolDepth1;
    @Value("${config1}")
    private String config1;
    @Value("${symbol1}")
    private String symbol1;
    @Value("${coinAddressUrlType2}")
    private String coinAddressUrlType2;
    @Value("${coinSymbol2}")
    private String coinSymbol2;
    @Value("${coinSymbolDepth2}")
    private String coinSymbolDepth2;
    @Value("${config2}")
    private String config2;
    @Value("${symbol2}")
    private String symbol2;
    private Map<String,Object> map=new HashMap<>();
    @RequestMapping("/insetData")
    @ResponseBody
    public String insetData(MultipartFile file) throws Exception {
        List<String> message = checkExcel(file);
        if (message.size()!=0) {
            return "数据错误";
        }
        Workbook workBook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workBook.getSheetAt(0);
        int rowNumber = sheet.getPhysicalNumberOfRows();
        System.out.println("本次操作的行数为"+rowNumber+"行");
        for (int i = 1; i < rowNumber; i++) {
            Row row = sheet.getRow(i);
            ConfigDataEntity configDataEntity=getEntity(row);
            //枚举类中的配置
            if (configDataEntity.getConfigType().equals("0")){
                File temAddressUrl=new File(coinAddressUrlType1);
                File temCoinSymbol=new File(coinSymbol1);
                File temDepth=new File(coinSymbolDepth1);
                File temConfig=new File(config1);
                File temSymbol=new File(symbol1);
                List<String> temDepthenums=enums.readLastNLine(temDepth);
                List<String> temSymbolenums=enums.readLastNLine(temSymbol);
                List<String> temDepthData=enums.getSymbolDepth(temDepthenums,configDataEntity);
                List<String> temSymbolData=enums.getSymbol(temSymbolenums,configDataEntity);
                enums.writeData(coinSymbolDepth2,temDepthData);
                enums.writeData(symbol2,temSymbolData);
                List<String> temCoinAddressenums=enums.readLastNLine(temAddressUrl);
                List<String> CoinAddressData=enums.getCoinAddress(temCoinAddressenums,configDataEntity);
                enums.writeData(coinAddressUrlType2,CoinAddressData);
                List<String> temCoinSymbolenums=enums.readLastNLine(temCoinSymbol);
                List<String> temConfigenums=enums.readLastNLine(temConfig);
                List<String> temCoinSymbolData=enums.getCoinSymbol(temCoinSymbolenums,configDataEntity);
                List<String> temConfigData=enums.getConfig(temConfigenums,configDataEntity);
                enums.writeData(coinSymbol2,temCoinSymbolData);
                enums.writeData(config2,temConfigData);
            }
            if (configDataEntity.getConfigType().equals("2")){
                File temAddressUrl=new File(coinAddressUrlType1);
                List<String> temCoinAddressenums=enums.readLastNLine(temAddressUrl);
                List<String> CoinAddressData=enums.getCoinAddress(temCoinAddressenums,configDataEntity);
                enums.writeData(coinAddressUrlType2,CoinAddressData);
                File temCoinSymbol=new File(coinSymbol1);
                File temConfig=new File(config1);
                List<String> temCoinSymbolenums=enums.readLastNLine(temCoinSymbol);
                List<String> temConfigenums=enums.readLastNLine(temConfig);
                List<String> temCoinSymbolData=enums.getCoinSymbol(temCoinSymbolenums,configDataEntity);
                List<String> temConfigData=enums.getConfig(temConfigenums,configDataEntity);
                enums.writeData(coinSymbol2,temCoinSymbolData);
                enums.writeData(config2,temConfigData);
            }
            if (configDataEntity.getConfigType().equals("1")){
                File temDepth=new File(coinSymbolDepth1);
                File temSymbol=new File(symbol1);
                List<String> temDepthenums=enums.readLastNLine(temDepth);
                List<String> temSymbolenums=enums.readLastNLine(temSymbol);
                List<String> temDepthData=enums.getSymbolDepth(temDepthenums,configDataEntity);
                List<String> temSymbolData=enums.getSymbol(temSymbolenums,configDataEntity);
                enums.writeData(coinSymbolDepth2,temDepthData);
                enums.writeData(symbol2,temSymbolData);
            }
            //读取AccType.properties文件最后添加的内容
            File fileAccType=new File(accTypePath);
            //读取模板文件
            File temAccType=new File(accTypeTemPath);
            List<String> dataList=accType.readLastNLine(fileAccType);
            List<String> temList=accType.readLastNLine(temAccType);
            //获取这次需要添加的AccType配置内容
            List<String> acctypeData = getAccType(dataList, temList, configDataEntity,fileAccType);
            if (acctypeData!=null){
                accType.writeData(accTypePath,acctypeData);
            }else {
                return "数据错误";
            }
            //获取exchange_trade文件的模板
            List<String> temTrade=trade.readLastNLine(new File(tradeTemPath),configDataEntity);
            //这次的trade文件数据
            List<String> tradeData=trade.getTrade(temTrade,configDataEntity);
            //往trade文件种写入数据
            trade.writeData(tradeAwsDevPath,tradeData);
            trade.writeData(tradeAwsLivePath,tradeData);
            trade.writeData(tradeDevPath,tradeData);
            trade.writeData(tradeLivePath,tradeData);
            //往 2.0中插入数据
            trade.writeData(twoPath,tradeData);
            //往trade-api中插入数据
            trade.writeData(apiAwsDevPath,tradeData);
            trade.writeData(apiAwsLivePath,tradeData);
            trade.writeData(apiAwsLiveTwoPath,tradeData);
            trade.writeData(apiDevPath,tradeData);
            trade.writeData(apiLivePath,tradeData);
            //在exchange-sql中创建目录和文件
            SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
            String date = sf.format(new Date());
            String dirName1="D:\\project\\exchange-sql\\NewCoinSql\\2021\\"+date.substring(0,6);
            String dirName2="D:\\project\\exchange-sql\\NewCoinSql\\2021\\"+date.substring(0,6)+"\\"+date;
            String dirName3="D:\\project\\exchange-sql\\NewCoinSql\\2021\\"+date.substring(0,6)+"\\"+date+"\\"+configDataEntity.getTicker().toLowerCase();
            String dirName4="D:\\project\\exchange-sql\\NewCoinSql\\2021\\"+date.substring(0,6)+"\\"+date+"\\"+configDataEntity.getTicker().toLowerCase()+"\\"+"day2";
             createDir(dirName1);
             createDir(dirName2);
             createDir(dirName3);
             //创建文件
             String fileName1=dirName3+"\\"+"01NewCoin02-add-exchange.FeeAccount.sql";
            String fileName2="";
             if (!configDataEntity.getConfigType().equals("2")) {
                 createDir(dirName4);
                 fileName2 = dirName4 + "\\" + "02OpenTrade-add-exchange.fee.sql";

                 //第二个文件
                 List<String> temSql2=exchangeSql.readLastNLine(new File(sqlTem2Path));
                 List<String> sqlData2=exchangeSql.getExchangeSql2(temSql2,configDataEntity);
                 exchangeSql.writeData(fileName2,sqlData2);
             }
             //往文件中写入数据
            //第一个文件
            List<String> temSql1;
            if (configDataEntity.getConfigType().equals("0")){
                temSql1= exchangeSql.readLastNLine(new File(sqlTemPath1));
            }else if (configDataEntity.getConfigType().equals("2")){
                temSql1= exchangeSql.readLastNLine(new File(sqlTemPath2));
            }else {
                temSql1= exchangeSql.readLastNLine(new File(sqlTemPath3));
            }
            List<String> sqlData1=exchangeSql.getExchangeSql1(temSql1,configDataEntity,map);

            exchangeSql.writeData(fileName1,sqlData1);

            //本次的matching模板
            if (!configDataEntity.getConfigType().equals("2")) {
                List<String> temMatch = matching.readLastNLine(new File(matchingPath));
                List<String> matchData = matching.getMatching(temMatch, configDataEntity, map);
                String matchDir1 = "D:\\project\\matching-engine-only-config\\src\\main\\resources\\aws-live-new\\2021\\" + date.substring(0, 6);
                String matchDir2 = "D:\\project\\matching-engine-only-config\\src\\main\\resources\\aws-live-new\\2021\\" + date.substring(0, 6) + "\\" + date;
                createDir(matchDir1);
                createDir(matchDir2);
                String name = configDataEntity.getTicker().toLowerCase() + configDataEntity.getJiaoyiqu().toLowerCase();
                matching.writeData(matchDir2 + "\\" + name + ".conf.properties", matchData);
            }
        }
        return "配置添加成功";
    }

   public boolean createDir(String dirName){
       File dir=new File(dirName);
       if (dir.exists()){
           return false;
       }else {
          return dir.mkdirs();
       }
    }
    public static ConfigDataEntity getEntity(Row row){
        ConfigDataEntity entity = new ConfigDataEntity();
        row.getCell(2).setCellType(CellType.STRING);
        row.getCell(3).setCellType(CellType.STRING);
        row.getCell(4).setCellType(CellType.STRING);
        row.getCell(5).setCellType(CellType.STRING);
        row.getCell(6).setCellType(CellType.STRING);
        row.getCell(7).setCellType(CellType.STRING);
        row.getCell(8).setCellType(CellType.STRING);
        row.getCell(9).setCellType(CellType.STRING);
        row.getCell(10).setCellType(CellType.STRING);
        row.getCell(11).setCellType(CellType.STRING);
        row.getCell(12).setCellType(CellType.STRING);
        row.getCell(13).setCellType(CellType.STRING);
        row.getCell(14).setCellType(CellType.STRING);
        row.getCell(15).setCellType(CellType.STRING);
        row.getCell(16).setCellType(CellType.STRING);
        row.getCell(17).setCellType(CellType.STRING);
        row.getCell(18).setCellType(CellType.STRING);
        row.getCell(21).setCellType(CellType.STRING);

        if (!isNullOrEmpty(row.getCell(0).getStringCellValue())){
            entity.setTicker(row.getCell(0).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(1).getStringCellValue())){
            entity.setLeixing(row.getCell(1).getStringCellValue());
            if (!"erc20".equals(entity.getLeixing())){
                entity.setRealname(entity.getTicker().toLowerCase()+"_"+entity.getLeixing().toLowerCase());
            }
            if ("BEP20".equalsIgnoreCase(entity.getLeixing())){
                entity.setAddUrl("https://bscscan.com/address/");
                entity.setTxUrl("https://bscscan.com/tx/");
            }else if ("erc20".equalsIgnoreCase(entity.getLeixing())){
                entity.setAddUrl("https://etherscan.io/address/");
                entity.setTxUrl("https://etherscan.com/tx/");
                entity.setRealname(entity.getTicker().toLowerCase());
            }else if ("TRC20".equalsIgnoreCase(entity.getLeixing())){
                entity.setAddUrl("https://tronscan.io/#/address/");
                entity.setTxUrl("https://tronscan.io/#/transaction/");
            }else {
                entity.setAddUrl(row.getCell(19).getStringCellValue());
                entity.setTxUrl(row.getCell(20).getStringCellValue());
            }
        }
        if (!isNullOrEmpty(row.getCell(2).getStringCellValue())){
            entity.setBigNum(row.getCell(2).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(3).getStringCellValue())){
            entity.setDepMin(row.getCell(3).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(4).getStringCellValue())){
            entity.setQuerenshu(row.getCell(4).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(5).getStringCellValue())){
            entity.setWithdrawNum(row.getCell(5).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(6).getStringCellValue())){
            entity.setWithdrawMin(row.getCell(6).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(7).getStringCellValue())){
            entity.setWithdrawMax(row.getCell(7).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(8).getStringCellValue())){
            entity.setFeeMin(row.getCell(8).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(9).getStringCellValue())){
            entity.setFeeMax(row.getCell(9).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(10).getStringCellValue())){
            entity.setCountNum(row.getCell(10).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(11).getStringCellValue())){
            entity.setUsdtNum(row.getCell(11).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(12).getStringCellValue())){
            entity.setXuhao(row.getCell(12).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(13).getStringCellValue())){
            entity.setJiaoyiqu(row.getCell(13).getStringCellValue());

            if (entity.getJiaoyiqu().equals("usdt")){
                entity.setJyqNor("201142");
                entity.setJyqLock("202142");
            }else if(entity.getJiaoyiqu().equals("susdt")){
                entity.setJyqNor("201634");
                entity.setJyqLock("202634");
            }else if(entity.getJiaoyiqu().equals("btc")){
                entity.setJyqNor("201101");
                entity.setJyqLock("202101");
            }else if(entity.getJiaoyiqu().equals("tch")){
                entity.setJyqNor("201108");
                entity.setJyqLock("202108");
            }else if (entity.getJiaoyiqu().equals("eth")){
                entity.setJyqNor("201102");
                entity.setJyqLock("202102");
            }
        }
        if (!isNullOrEmpty(row.getCell(14).getStringCellValue())){
            entity.setPriceLen(row.getCell(14).getStringCellValue());
            //进行价格小数位的设置
            Integer priceLen=Integer.parseInt(entity.getPriceLen());
            if (priceLen==8||priceLen==10){
                entity.setPriceLen2("6");
                entity.setPriceLen3("4");
                BigDecimal bigDecimal1=new BigDecimal(1/Math.pow(10,priceLen));
                BigDecimal bigDecimal2=new BigDecimal(1/Math.pow(10,6));
                BigDecimal bigDecimal3=new BigDecimal(1/Math.pow(10,4));
                entity.setShendu1(String.format("%."+priceLen+"f",bigDecimal1));
                entity.setShendu2(String.format("%.6f",bigDecimal2));
                entity.setShendu3(String.format("%.4f",bigDecimal3));
            }else if (priceLen==6){
                entity.setPriceLen2("4");
                entity.setPriceLen3("2");
                BigDecimal bigDecimal1=new BigDecimal(1/Math.pow(10,6));
                BigDecimal bigDecimal2=new BigDecimal(1/Math.pow(10,4));
                BigDecimal bigDecimal3=new BigDecimal(1/Math.pow(10,2));
                entity.setShendu1(String.format("%.6f",bigDecimal1));
                entity.setShendu2(String.format("%.4f",bigDecimal2));
                entity.setShendu3(String.format("%.2f",bigDecimal3));
            }else if (priceLen==4){
                entity.setPriceLen2("2");
                entity.setPriceLen3("1");
                BigDecimal bigDecimal1=new BigDecimal(1/Math.pow(10,4));
                BigDecimal bigDecimal2=new BigDecimal(1/Math.pow(10,2));
                BigDecimal bigDecimal3=new BigDecimal(1/Math.pow(10,1));
                entity.setShendu1(String.format("%.4f",bigDecimal1));
                entity.setShendu2(String.format("%.2f",bigDecimal2));
                entity.setShendu3(String.format("%.1f",bigDecimal3));
            }else if (priceLen==2){
                entity.setPriceLen2("1");
                entity.setPriceLen3("0");
                BigDecimal bigDecimal1=new BigDecimal(1/Math.pow(10,2));
                BigDecimal bigDecimal2=new BigDecimal(1/Math.pow(10,1));
                entity.setShendu1(String.format("%.2f",bigDecimal1));
                entity.setShendu2(String.format("%.1f",bigDecimal2));
                entity.setShendu3("0");
            }else{
                entity.setPriceLen2("8");
                entity.setPriceLen3("6");
                BigDecimal bigDecimal1=new BigDecimal(1/Math.pow(10,12));
                BigDecimal bigDecimal2=new BigDecimal(1/Math.pow(10,8));
                BigDecimal bigDecimal3=new BigDecimal(1/Math.pow(10,6));
                entity.setShendu1(String.format("%.12f",bigDecimal1));
                entity.setShendu2(String.format("%.8f",bigDecimal2));
                entity.setShendu3(String.format("%.6f",bigDecimal3));
            }
        }
        if (!isNullOrEmpty(row.getCell(15).getStringCellValue())){
            Integer volume=Integer.parseInt(row.getCell(15).getStringCellValue());
            if (volume!=0) {
                BigDecimal bigDecimal = new BigDecimal(1 / Math.pow(10, volume));
                entity.setVolumeLen(String.format("%." + volume + "f", bigDecimal));
            }else{
                entity.setVolumeLen("0");
            }
        }
        if (!isNullOrEmpty(row.getCell(16).getStringCellValue())){
            entity.setDealMinNum(row.getCell(16).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(17).getStringCellValue())){
            entity.setDealMinLim(row.getCell(17).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(18).getStringCellValue())){
            entity.setYinying(row.getCell(18).getStringCellValue());
        }
        if (!isNullOrEmpty(row.getCell(21).getStringCellValue())){
            entity.setConfigType(row.getCell(21).getStringCellValue());
        }
        return entity;
    }

    public List<String> checkExcel(MultipartFile file) throws Exception {
        List<String> list = new ArrayList<>();
        //错误返回
        List error = new ArrayList();
        //读取工作簿
        Workbook workBook = WorkbookFactory.create(file.getInputStream());
        //读取工作表
        Sheet sheet = workBook.getSheetAt(0);
        int rowNumber = sheet.getPhysicalNumberOfRows();
        //校验是否填写内容
        if (rowNumber <= 1) {
            error.add("文件无内容，请检查");
            return error;
        }
        workBook.close();
        return error;
    }

    //对于AccType添加内容的处理
    public List<String> getAccType(List<String> dataList, List<String> temList, ConfigDataEntity configDataEntity,File fileAccType){

        List<String> accTypeData=new ArrayList<>();
        accTypeData.add("");
        Integer firSeq=Integer.valueOf(dataList.get(0).substring(dataList.get(0).indexOf("=")+1,dataList.get(0).indexOf(",")))+2;
        Integer secSeq=Integer.valueOf(dataList.get(1).substring(dataList.get(1).indexOf("=")+1,dataList.get(1).indexOf(",")))+2;
        Integer thirSeq=Integer.valueOf(dataList.get(2).substring(dataList.get(2).indexOf("=")+1,dataList.get(2).indexOf(",")))+1;
        Integer forSeq=Integer.valueOf(dataList.get(3).substring(dataList.get(3).indexOf("=")+1,dataList.get(3).indexOf(",")))+1;
        Integer fifSeq=Integer.valueOf(dataList.get(4).substring(dataList.get(4).indexOf("=")+1,dataList.get(4).indexOf(",")))+1;
        Integer sixSeq=Integer.valueOf(dataList.get(5).substring(dataList.get(5).indexOf("=")+1,dataList.get(5).indexOf(",")))+1;
        Integer sevSeq=Integer.valueOf(dataList.get(6).substring(dataList.get(6).indexOf("=")+1,dataList.get(6).indexOf(",")))+1;
        Integer eigSeq=Integer.valueOf(dataList.get(7).substring(dataList.get(7).indexOf("=")+1,dataList.get(7).indexOf(",")))+1;
        Integer ninSeq=Integer.valueOf(dataList.get(8).substring(dataList.get(8).indexOf("=")+1,dataList.get(8).indexOf(",")))+1;
        Integer tenSeq=Integer.valueOf(dataList.get(9).substring(dataList.get(9).indexOf("=")+1,dataList.get(9).indexOf(",")))+1;
        map.put("firSeq",firSeq);
        map.put("secSeq",secSeq);
        map.put("thirSeq",thirSeq);
        map.put("forSeq",forSeq);
        map.put("fifSeq",fifSeq);
        map.put("sixSeq",sixSeq);
        map.put("sevSeq",sevSeq);
        map.put("eigSeq",eigSeq);
        map.put("ninSeq",ninSeq);
        map.put("tenSeq",tenSeq);
        if (configDataEntity.getConfigType().equals("0")){
            //币种和币对都有
            temList.forEach(s -> {
               String s1=s.replaceAll("TICKER",configDataEntity.getTicker().toUpperCase().contains("$")?
                       configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toUpperCase():
                       configDataEntity.getTicker().toUpperCase())
                .replaceAll("RDS_CHAR_DOLLAR","\\$")
                .replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                        configDataEntity.getTicker().toLowerCase())
                .replaceAll("rds_char_dollar","\\$")
                .replaceAll("jiaoyiqu",configDataEntity.getJiaoyiqu().toLowerCase())
                .replaceAll("JIAOYIQU",configDataEntity.getJiaoyiqu().toUpperCase())
                .replaceAll("firSeq",firSeq.toString())
                .replaceAll("secSeq",secSeq.toString())
                .replaceAll("thirSeq",thirSeq.toString())
                .replaceAll("forSeq",forSeq.toString())
                .replaceAll("fifSeq",fifSeq.toString())
                .replaceAll("sixSeq",sixSeq.toString())
                .replaceAll("sevSeq",sevSeq.toString())
                .replaceAll("eigSeq",eigSeq.toString())
                .replaceAll("ninSeq",ninSeq.toString())
                .replaceAll("tenSeq",tenSeq.toString());
                accTypeData.add(s1);
            });
        }else if (configDataEntity.getConfigType().equals("2")){
            //只有币种没有币对
            for (int i = 2; i < temList.size(); i++) {
                String s= temList.get(i).replaceAll("TICKER",configDataEntity.getTicker().toUpperCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toUpperCase():
                        configDataEntity.getTicker().toUpperCase())
                .replaceAll("RDS_CHAR_DOLLAR","\\$")
                .replaceAll("thirSeq",thirSeq.toString())
                .replaceAll("forSeq",forSeq.toString())
                .replaceAll("fifSeq",fifSeq.toString())
                .replaceAll("sixSeq",sixSeq.toString())
                .replaceAll("sevSeq",sevSeq.toString())
                .replaceAll("eigSeq",eigSeq.toString())
                .replaceAll("ninSeq",ninSeq.toString())
                .replaceAll("tenSeq",tenSeq.toString());
                accTypeData.add(s);
            }
        }else if (configDataEntity.getConfigType().equals("1")){
            //更新一下map的eigSeq和ninSeq
           List<String> dataList2= accType.readEigSeqNinSeq(fileAccType,configDataEntity);
            Integer ninSeq2=Integer.valueOf(dataList2.get(0).substring(dataList2.get(0).indexOf("=")+1,dataList2.get(0).indexOf(",")));
            Integer eigSeq2=Integer.valueOf(dataList2.get(1).substring(dataList2.get(1).indexOf("=")+1,dataList2.get(1).indexOf(",")));
            map.put("eigSeq",eigSeq2);
            map.put("ninSeq",ninSeq2);
            //只有币对没有币种
            for (int i = 0; i < 2; i++) {
                String s=temList.get(i).replaceAll("TICKER",configDataEntity.getTicker().toUpperCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toUpperCase():
                        configDataEntity.getTicker().toUpperCase())
                .replaceAll("RDS_CHAR_DOLLAR","\\$")
                .replaceAll("ticker",configDataEntity.getTicker().toLowerCase().contains("$")?
                        configDataEntity.getTicker().replaceAll("\\$","RDS_CHAR_DOLLAR").toLowerCase():
                        configDataEntity.getTicker().toLowerCase())
                .replaceAll("rds_char_dollar","\\$")
                .replaceAll("jiaoyiqu",configDataEntity.getJiaoyiqu().toLowerCase())
                .replaceAll("JIAOYIQU",configDataEntity.getJiaoyiqu().toUpperCase())
                .replaceAll("firSeq",firSeq.toString())
                .replaceAll("secSeq",secSeq.toString());
                accTypeData.add(s);
            }
        }else {
            return null;
        }
        return accTypeData;
    }

    public static boolean isNullOrEmpty(String s){
        if( s==null || s.trim().length()==0 ){
            return true;
        }
        if (compareString(s, "null")) {
            return true;
        }
        return false;
    }
    public static boolean compareString(String str1, String str2) {
        if (null == str1) {
            str1 = "";
        }
        if (null == str2) {
            str2 = "";
        }
        if (str1.trim().equals(str2.trim())) {
            return true;
        }
        return false;
    }
}
