package com.netease.ps.demo.service;

import com.netease.ps.demo.domain.Data;
import com.netease.ps.demo.domain.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by hzzhangxuan on 2017/9/26.
 */
public interface DataRepository extends JpaRepository<Data,Integer> {
    Data findById(int id);
    Data[] findAllByUdidAndNameOrderByTime(String udid,String name);
    Page<Data> findAllByUdidAndNameOrderByTimeDesc(String udid, String name, Pageable pageable);

    @Query(value="select distinct d.udid from data d", nativeQuery = true)
    List<Object> findAllUdid();

    @Query(value="select distinct d.name from data d where d.udid = ?1", nativeQuery = true)
    List<Object> findAllMemberByUdid(String udid);

    @Query(value="select distinct d.udid from data d where d.time>?1", nativeQuery = true)
    List<Object> findAllUdidRecently(Date time);

    @Query(value="select distinct d.udid from data d where d.time>?1 and d.time<?2", nativeQuery = true)
    List<Object> findAllUdidRecently(Date time,Date end);

    @Query(value="select distinct d.name from data d where d.udid = ?1 and d.time>?2", nativeQuery = true)
    List<Object> findAllMemberByUdidRecently(String udid,Date time);

    @Query(value="select distinct d.name from data d where d.udid = ?1 and d.time>?2 and d.time<?3", nativeQuery = true)
    List<Object> findAllMemberByUdidRecently(String udid,Date time,Date end);

    @Query(value="select d.time,d.value from data d where d.udid = ?1 and d.name=?2 and d.time>?3 and d.time<?4", nativeQuery = true)
    List<Object[]> findAllPosinByRange(String udid, String name,Date time, Date end);

}
