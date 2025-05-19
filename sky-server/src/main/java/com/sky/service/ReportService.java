package com.sky.service;

import com.sky.dto.ReportDTO;
import com.sky.vo.TurnoverReportVO;

public interface ReportService {
    /**
     * 营业额统计
     * @param reportDTO
     * @return
     */
    TurnoverReportVO getTurnoverReport(ReportDTO reportDTO);
}
