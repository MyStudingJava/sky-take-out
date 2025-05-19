package com.sky.service.impl;

import com.sky.dto.ReportDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 营业额统计
     * @param reportDTO
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverReport(ReportDTO reportDTO) {
        LocalDate begin = reportDTO.getBegin();
        LocalDate end = reportDTO.getEnd();
        if (begin == null || end == null) {
            // 如果为空, 默认查询最近7天的数据
            end = LocalDate.now();
            begin = end.minusDays(6);
        }

        // 计算日期, 开始日期到结束日期区间
        ArrayList<LocalDate> dateList = new ArrayList<>();
        while (begin.compareTo(end) <= 0) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }

        // 查询相关日期的营业额--->已完成订单
        ArrayList<Object> turnoverList = new ArrayList<>();
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
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
}
