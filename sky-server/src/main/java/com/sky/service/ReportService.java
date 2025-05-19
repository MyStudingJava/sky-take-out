package com.sky.service;

import com.sky.dto.ReportDTO;
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
}
