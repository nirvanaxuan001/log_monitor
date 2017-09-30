package com.netease.ps.demo.controller;

import com.netease.ps.demo.domain.Data;
import com.netease.ps.demo.service.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzzhangxuan on 2017/9/27.
 */
@Controller
public class ViewChangeController {
    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @RequestMapping("/index")
    public String indexView(){
        return "index";
    }
    @RequestMapping("/log")
    public String logView(){
        return "log";
    }
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    public String uploadFile(@RequestParam(value="parse_file") MultipartFile parse, @RequestParam(value="log_file") MultipartFile log,@RequestParam(value="input_udid") String udid){
        //read parse
        try {
            InputStream parseFile= parse.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(parseFile));
            String timeFormat = br.readLine();
//            timeFormat=timeFormat.substring(0,timeFormat.length()-1);
            StringBuilder sb = new StringBuilder(timeFormat);
            sb.insert(1,"^");
            String startWithTime =sb.toString();
            String line ="";
            Map<String,String> patternMap = new HashMap<String,String>();
            while((line=br.readLine())!=null){
                String[] temp = line.split(" ");
                String key = temp[0];
                String value = temp[1];
                patternMap.put(key,startWithTime+value);
            }
        //read log
            Long fileLengthLong = log.getSize();
            byte[] fileContent = new byte[fileLengthLong.intValue()];
            InputStream logFile= log.getInputStream();
            int size =logFile.read(fileContent);
            String allLines = new String(fileContent);
            Pattern p =Pattern.compile(timeFormat);
            Matcher m = p.matcher(allLines);
            String[] lines =allLines.split(timeFormat);
            if(lines.length > 0)
            {
                int count = 0;
                while(count < lines.length-1)
                {
                    if(m.find())
                    {
                        lines[count] = m.group()+lines[count+1];
                    }
                    count++;
                }
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int entityCount = 50;
            int batchSize = 25;

            EntityManager entityManager = null;
            EntityTransaction transaction = null;

            try {
                entityManager = entityManagerFactory
                        .createEntityManager();

                transaction = entityManager.getTransaction();
                transaction.begin();

                for(String aLine :lines){
                    for(Map.Entry<String,String> entry:patternMap.entrySet()){
                        String name= entry.getKey();
                        String value = entry.getValue();
                        p=Pattern.compile(value);
                        m=p.matcher(aLine);
                        if(m.find()){
                            try {
//                                dataRepository.save(new Data(df.parse(m.group(1)),udid,name,Double.parseDouble(m.group(2))));
                                entityManager.persist( new Data(df.parse(m.group(1)),udid,name,Double.parseDouble(m.group(2))) );
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                transaction.commit();
            } catch (RuntimeException e) {
                if ( transaction != null &&
                        transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
            }
            } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "log";
    }
}
