package com.sky.service;

import com.sky.dto.ReportDTO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

public interface ReportService {
    /**
     * 营业额统计
     * @param reportDTO
     * @return
     */
    TurnoverReportVO getTurnoverReport(ReportDTO reportDTO);

    /**
     * 用户统计
     * @param reportDTO
     * @return
     */
    UserReportVO getUserReport(ReportDTO reportDTO);

    /**
     * 订单统计
     * @param reportDTO
     * @return
     */
    OrderReportVO getOrderReport(ReportDTO reportDTO);

    /**
     * 销量排名
     * @param reportDTO
     * @return
     */
    SalesTop10ReportVO getSalesTop10Report(ReportDTO reportDTO);
}
