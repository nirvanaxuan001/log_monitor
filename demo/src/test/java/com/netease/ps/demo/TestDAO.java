package com.netease.ps.demo;

import com.netease.ps.demo.domain.Data;
import com.netease.ps.demo.service.DataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hzzhangxuan on 2017/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestDAO {

    @Autowired
    private DataRepository dataRepository;

    @Test
    public void test() throws Exception {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        String formattedDate = dateFormat.format(date);

        dataRepository.save(new Data(new Date(),"udid_b","name_b",1));
        Data[] datas =dataRepository.findAllByUdidAndNameOrderByTime("udid_a","name");
        for(Data data : datas){
            System.out.println(data.getTime());
        }
//        Assert.assertEquals(1, dataRepository.findAll().size());
//        dataRepository.delete(dataRepository.findById(1));
    }
}
