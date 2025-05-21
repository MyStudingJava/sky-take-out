package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;

public interface WorkSpaceService {
    /**
     * 获取今日营业数据
     * @return
     */
    BusinessDataVO getBusinessData();

    /**
     * 获取订单统计数据
     * @return
     */
    OrderOverViewVO getOverViewOrders();

    /**
     * 获取菜品统计数据
     * @return
     */
    DishOverViewVO getOverViewDishes();

    /**
     * 获取套餐统计数据
     * @return
     */
    DishOverViewVO getOverViewSetmeals();
}
