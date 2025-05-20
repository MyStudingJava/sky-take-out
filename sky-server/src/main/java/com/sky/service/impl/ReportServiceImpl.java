package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.ReportDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     * @param reportDTO
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverReport(ReportDTO reportDTO) {
        LocalDate begin = reportDTO.getBegin();
        LocalDate end = reportDTO.getEnd();
//        if (begin == null || end == null) {
//            // 如果为空, 默认查询最近7天的数据
//            end = LocalDate.now();
//            begin = end.minusDays(6);
//        }

        // 计算日期, 开始日期到结束日期区间
        ArrayList<LocalDate> dateList = new ArrayList<>();
        while (begin.compareTo(end) <= 0) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }

        // 查询相关日期的营业额--->已完成订单
        ArrayList<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 获取当天对应的营业额
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59

            // select sum(amount) from orders where order_time > ? and order_time < ? and status = 5
            HashMap hashMap = new HashMap();
            hashMap.put("begin", beginTime);
            hashMap.put("end", endTime);
            // status = 5 / Orders.COMPLETED 表示订单已完成
            hashMap.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.sumByMap(hashMap);
            turnover  = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     * @param reportDTO
     * @return
     */
    @Override
    public UserReportVO getUserReport(ReportDTO reportDTO) {
        LocalDate begin = reportDTO.getBegin();
        LocalDate end = reportDTO.getEnd();

        // 计算日期, 开始日期到结束日期区间
        ArrayList<LocalDate> dateList = new ArrayList<>();
        while (begin.compareTo(end) <= 0) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }

        // 查询每天新增用户数量
        ArrayList<Integer> newUserList = new ArrayList<>();
        // 查询每天总用户数量
        ArrayList<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            HashMap hashMap = new HashMap();
            hashMap.put("end", endTime); // 只加end 结束时间,自动匹配统计总数的sql

            // 总用户数
            // select count(id) from user where create_time > ?
            Integer totalUser = userMapper.countByMap(hashMap);
            totalUserList.add(totalUser);

            hashMap.put("begin", beginTime); // 加begin 开始时间,自动匹配新增用户的sql
            // 新增用户数
            // select count(id) from user where create_time > ? and create_time < ?
            Integer newUser = userMapper.countByMap(hashMap);
            newUserList.add(newUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 订单统计
     * @param reportDTO
     * @return
     */
    @Override
    public OrderReportVO getOrderReport(ReportDTO reportDTO) {
        LocalDate begin = reportDTO.getBegin();
        LocalDate end = reportDTO.getEnd();

        // 计算日期, 开始日期到结束日期区间
        ArrayList<LocalDate> dateList = new ArrayList<>();
        while (begin.compareTo(end) <= 0) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }


        // 计算每日订单总数
        ArrayList<Integer> orderCountList = new ArrayList<>();

        // 计算每日有效订单数--就是营业额
        ArrayList<Integer> validOrderCountList = new ArrayList<>();

        // 订单总数
        Integer totalOrdersCount = 0;
        // 有效订单总数
        Integer validOrdersCount = 0;

        for (LocalDate date :dateList ) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // 00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // 23:59:59

            // select count(id) from orders where order_time > ? and order_time < ? and status = ?
            HashMap hashMap = new HashMap();
            hashMap.put("begin", beginTime);
            hashMap.put("end", endTime);
            Integer orderCount = orderMapper.countByMap(hashMap);
            orderCount = orderCount == null ? 0 : orderCount;
            orderCountList.add(orderCount);

            // status = 5 / Orders.COMPLETED 表示订单已完成
            hashMap.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(hashMap);
            validOrderCount = validOrderCount == null ? 0 : validOrderCount;
            validOrderCountList.add(validOrderCount);

            totalOrdersCount += orderCount;

            validOrdersCount += validOrderCount;
        }

        Double orderCompletionRate = totalOrdersCount == 0 ? 0.0 :validOrdersCount.doubleValue() / totalOrdersCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrdersCount)
                .validOrderCount(validOrdersCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 销量排名
     * @param reportDTO
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10Report(ReportDTO reportDTO) {
        LocalDate begin = reportDTO.getBegin();
        LocalDate end = reportDTO.getEnd();

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);


        // 查询总订单的top10
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");


        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }
}
