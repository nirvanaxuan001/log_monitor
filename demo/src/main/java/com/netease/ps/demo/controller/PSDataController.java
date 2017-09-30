package com.netease.ps.demo.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netease.ps.demo.domain.Data;
import com.netease.ps.demo.domain.Point;
import com.netease.ps.demo.domain.Value;
import com.netease.ps.demo.service.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzzhangxuan on 2017/9/25.
 */
@RestController
public class PSDataController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DataRepository dataRepository;

    @RequestMapping(value="/update",method= RequestMethod.POST)
    public Object updateRedisData(@RequestBody String body){
        try {
            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
            String udid = jsonObject.get("udid").getAsString();
            Number time = jsonObject.get("time").getAsNumber();
//            stringRedisTemplate.opsForSet().add("device", udid);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (!entry.getKey().equals("udid")&&!entry.getKey().equals("time")) {
                    String name = entry.getKey();
                    double value =entry.getValue().getAsDouble();
                    dataRepository.save(new Data(new Date(time.longValue()),udid,name,value));
                    String key = udid + "." + entry.getKey();
                    String val = String.valueOf(entry.getValue());
                    stringRedisTemplate.opsForValue().set(key,val,9, TimeUnit.SECONDS);
//                    stringRedisTemplate.opsForZSet().add(key,value,time.doubleValue());
                }
            }
        }catch (Exception e){
            return e.toString();
        }
        return null;
    }

    @RequestMapping(value="/getDevice",method= RequestMethod.GET)
    public List<Object> getDevice(){
        List<Object> udids= dataRepository.findAllUdid();
        return udids;
    }

    @RequestMapping(value="/getDeviceRecently",method= RequestMethod.GET)
    public List<Object> getDeviceRecently(@RequestParam(value="time") String time,@RequestParam(value="end",required = false) String end){
        Date date = new Date(Long.parseLong(time));
        if(end==null){
            List<Object> udids= dataRepository.findAllUdidRecently(date);
            return udids;
        }else {
            Date endDate = new Date(Long.parseLong(end));
            List<Object> udids= dataRepository.findAllUdidRecently(date,endDate);
            return udids;
        }
    }


    @RequestMapping(value="/getMemberByDevice",method= RequestMethod.GET)
    public List<Object> getMemberByDevice(@RequestParam(value="device") String device){
        List<Object> members = dataRepository.findAllMemberByUdid(device);
        return members;
    }

    @RequestMapping(value="/getMemberByDeviceRecently",method= RequestMethod.GET)
    public List<Object> getMemberByDevice(@RequestParam(value="device") String device,@RequestParam(value="time") String time,@RequestParam(value="end",required = false) String end){
        Date date = new Date(Long.parseLong(time));
        if(end==null) {
            List<Object> members = dataRepository.findAllMemberByUdidRecently(device, date);
            return members;
        }else{
            Date endDate = new Date(Long.parseLong(end));
            List<Object> members = dataRepository.findAllMemberByUdidRecently(device, date,endDate);
            return members;
        }
    }


    @RequestMapping(value="/getLatestData",method= RequestMethod.GET)
    public Value getData(@RequestParam(value="device") String udid, @RequestParam(value="member") String name){
        try {
            String value = stringRedisTemplate.opsForValue().get(udid + "." + name);
            Assert.notNull(value);
            return new Value(value);
        }catch (Exception e){
            int size =1;
            int page=0;
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pageable = new PageRequest(page, size, sort);
            Page<Data> datas= dataRepository.findAllByUdidAndNameOrderByTimeDesc(udid,name,pageable);
            List<Data> list =datas.getContent();
            Data data = list.get(0);
            String value = String.valueOf(data.getValue());
            long now = new Date().getTime();
            if(now-data.getTime().getTime()>300000){
                value = "0";
            }
            return new Value(value);
        }
    }
//    @RequestMapping(value="/getDataByRange",method = RequestMethod.POST)
//    public Page<Data> getDataByRange(@RequestBody String body){
//        try{
//            JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
//            String udid = jsonObject.get("udid").getAsString();
//            String name = jsonObject.get("name").getAsString();
//            int size = jsonObject.get("size").getAsInt();
//            int page=0;
//            Sort sort = new Sort(Sort.Direction.DESC, "id");
//            Pageable pageable = new PageRequest(page, size, sort);
//            Page<Data> datas= dataRepository.findAllByUdidAndNameOrderByTimeDesc(udid,name,pageable);
//            return datas;
//        }catch (Exception e){
//            return null;
//        }
//    }
        @RequestMapping(value="/getDataByRange",method = RequestMethod.POST)
        public List<Object[]> getDataByRange(@RequestBody String body){
            try {
                JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
                String udid = jsonObject.get("udid").getAsString();
                String name = jsonObject.get("name").getAsString();
                Date start = new Date(jsonObject.get("time").getAsLong());
                Date end = new Date(jsonObject.get("end").getAsLong());
                List<Object[]> points = dataRepository.findAllPosinByRange(udid,name,start,end);
                return points;
            }catch (Exception e){
                return null;
            }
        }
}
