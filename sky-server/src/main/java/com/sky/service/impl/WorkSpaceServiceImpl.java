package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 获取今日营业数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);

        HashMap hashMap = new HashMap();
        hashMap.put("begin", beginTime);
        hashMap.put("end", endTime);
        // 今日新增用户
        Integer newUser = userMapper.countByMap(hashMap);

        // 今日总订单
        Integer orderCount = orderMapper.countByMap(hashMap);

        // 今日有效订单
        hashMap.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.countByMap(hashMap);

        // 今日营业额
        Double turnover = orderMapper.sumByMap(hashMap);
        turnover = turnover == null ? 0.0 : turnover;

        // 今日订单完成率
        Double orderCompletionRate = validOrderCount == 0 ? 0.0 : Double.valueOf(validOrderCount) / orderCount;

        // 今日平均客单价
        Double unitPrice = turnover == 0.0 ? 0.0 : Double.valueOf(turnover) / validOrderCount;

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUser)
                .build();
    }

    /**
     * 获取订单统计数据
     * @return
     */
    @Override
    public OrderOverViewVO getOverViewOrders() {
        // 全部订单
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setPage(1);
        ordersPageQueryDTO.setPageSize(20);

        Page<Orders> ordersPageQuery = orderMapper.pageQuery(ordersPageQueryDTO);
        Integer allOrders = (int) ordersPageQuery.getTotal();

        // 已取消数量
        ordersPageQueryDTO.setStatus(Orders.CANCELLED);
        Integer cancelledOrders = (int) orderMapper.pageQuery(ordersPageQueryDTO).getTotal();

        // 已完成数量
        ordersPageQueryDTO.setStatus(Orders.COMPLETED);
        Integer completedOrders = (int) orderMapper.pageQuery(ordersPageQueryDTO).getTotal();


        // 待接单数量
        ordersPageQueryDTO.setStatus(Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = (int) orderMapper.pageQuery(ordersPageQueryDTO).getTotal();

        // 待派单数量
        ordersPageQueryDTO.setStatus(Orders.CONFIRMED);
        Integer deliveredOrders = (int)orderMapper.pageQuery(ordersPageQueryDTO).getTotal();

        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelledOrders)
                .completedOrders(completedOrders)
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .build();
    }

    /**
     * 获取菜品总览数据
     * @return
     */
    @Override
    public DishOverViewVO getOverViewDishes() {
        DishPageQueryDTO dishPageQueryDTO = new DishPageQueryDTO();

        // 已停售菜品数量
        dishPageQueryDTO.setPage(0);
        Integer discontinued = (int)dishMapper.pageQuery(dishPageQueryDTO).getTotal();

        // 已起售菜品数量
        dishPageQueryDTO.setStatus(1);
        Integer sold = (int)dishMapper.pageQuery(dishPageQueryDTO).getTotal();

        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }

    /**
     * 获取套餐总览数据
     * @return
     */
    @Override
    public DishOverViewVO getOverViewSetmeals() {
        SetmealPageQueryDTO setmealPageQueryDTO = new SetmealPageQueryDTO();

        // 已停售套餐数量
        setmealPageQueryDTO.setStatus(0);
        Integer discontinued = (int) setmealMapper.pageQuery(setmealPageQueryDTO).getTotal();

        // 已起售套餐数量
        setmealPageQueryDTO.setStatus(1);
        Integer sold = (int) setmealMapper.pageQuery(setmealPageQueryDTO).getTotal();

        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }
}
