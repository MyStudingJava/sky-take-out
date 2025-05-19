package com.sky.controller.admin;

import com.sky.dto.ReportDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计接口")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param reportDTO
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计接口")
    public Result<TurnoverReportVO> getTurnoverReport(ReportDTO reportDTO) {
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverReport(reportDTO);
        return Result.success(turnoverReportVO);
    }

    /**
     * 用户统计
     * @param reportDTO
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计接口")
    public Result<UserReportVO> getUserReport(ReportDTO reportDTO){
        UserReportVO userReportVO = reportService.getUserReport(reportDTO);
        return Result.success(userReportVO);
    }
}
