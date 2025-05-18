package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单分页查询")
    public Result<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 各状态订单数量统计
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各状态订单统计数量")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();

        return Result.success(orderStatisticsVO);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> getById(@PathVariable Long id){
        OrderVO orderVO = orderService.getById(id);

        return Result.success(orderVO);
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 接单
     * @param id
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirmOrder (@RequestBody Long id){
        // 更改状态
        orderService.changeOrderStatus(id);
        return Result.success();
    }

    /**
     * 拒绝订单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/reject")
    @ApiOperation("拒绝订单")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 派送订单
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result deliveryOrder(@PathVariable Long id){
        orderService.changeOrderStatus(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result completeOrder(@PathVariable Long id){
        orderService.changeOrderStatus(id);
        return Result.success();
    }
}
