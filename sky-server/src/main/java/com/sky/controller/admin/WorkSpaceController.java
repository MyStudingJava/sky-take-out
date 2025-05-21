package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作台相关接口
 */
@RestController()
@RequestMapping("/admin/workspace")
@Api(tags = "工作台相关接口")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 获取今日营业数据
     * @return
     */
    @RequestMapping("/businessData")
    @ApiOperation("获取工作台统计数据")
    public Result<BusinessDataVO> getBusinessData() {
        BusinessDataVO businessData =  workSpaceService.getBusinessData();
        return Result.success(businessData);
    }


    /**
     * 获取订单统计数据
     * @return
     */
    @RequestMapping("/overviewOrders")
    @ApiOperation("获取订单统计数据")
    public Result<OrderOverViewVO> getOverViewOrders() {
        OrderOverViewVO orderOverView= workSpaceService.getOverViewOrders();
        return Result.success(orderOverView);
    }

    /**
     * 获取菜品统计数据
     * @return
     */
    @RequestMapping("/overviewDishes")
    @ApiOperation("获取菜品统计数据")
    public Result<DishOverViewVO> getOverViewDishes(){
        DishOverViewVO dishOverViewVO = workSpaceService.getOverViewDishes();
        return Result.success(dishOverViewVO);
    }

    /**
     * 获取套餐统计数据
     * @return
     */
    @RequestMapping("/overviewSetmeals")
    @ApiOperation("获取套餐统计数据")
    public Result<DishOverViewVO> getOverViewSetmeals(){
        DishOverViewVO dishOverViewVO = workSpaceService.getOverViewSetmeals();
        return Result.success(dishOverViewVO);
    }
}
