package com.sky.controller.admin;

import com.sky.dto.ReportDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    /**
     * 订单统计
     * @param reportDTO
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计接口")
    public Result<OrderReportVO> getOrderReport(ReportDTO reportDTO){
        OrderReportVO orderReportVO = reportService.getOrderReport(reportDTO);
        return Result.success(orderReportVO);
    }

    /**
     * 销量top10
     * @param reportDTO
     * @return
     */
    @GetMapping("/top10")
    @ApiOperation("销量top10接口")
    public Result<SalesTop10ReportVO> getSalesTop10Report(ReportDTO reportDTO){
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10Report(reportDTO);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出运营数据报表
     * @param response
     * @return
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表接口")
    // 响应结果为文件流 -- 用HttpServletResponse response
    public void exportBusinessData(HttpServletResponse response) {
        reportService.exportBusinessData(response);
    }
}
