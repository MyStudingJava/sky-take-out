package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.shoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ShoppingCartImpl implements shoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart cart = buildCartFromDTO(shoppingCartDTO);

        // 查询是否已存在该商品
        Optional<ShoppingCart> existingCartOpt = findCartInDB(cart);

        if (existingCartOpt.isPresent()) {
            ShoppingCart existingCart = existingCartOpt.get();
            existingCart.setNumber(existingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(existingCart);
        } else {
            // 如果不存在，设置默认信息并插入
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                Dish dish = dishMapper.getById(dishId);
                cart.setName(dish.getName());
                cart.setImage(dish.getImage());
                cart.setAmount(dish.getPrice());
            } else {
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                cart.setName(setmeal.getName());
                cart.setImage(setmeal.getImage());
                cart.setAmount(setmeal.getPrice());
            }

            cart.setNumber(1);
            cart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(cart);
        }
    }

    /**
     * 查看购物车
     */
    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(getCurrentUserCartTemplate());
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.deleteShoppingCart(getCurrentUserCartTemplate());
    }

    /**
     * 删除购物车中的某个商品（数量减一或删除）
     */
    @Override
    public void sub(ShoppingCartDTO dto) {
        ShoppingCart cart = buildCartFromDTO(dto);
        Optional<ShoppingCart> cartOpt = findCartInDB(cart);

        if (cartOpt.isPresent()) {
            ShoppingCart cartInDB = cartOpt.get();
            if (cartInDB.getNumber() > 1) {
                cartInDB.setNumber(cartInDB.getNumber() - 1);
                shoppingCartMapper.updateNumberById(cartInDB);
            } else {
                shoppingCartMapper.deleteShoppingCart(cartInDB);
            }
        } else {
            throw new RuntimeException("购物车中无此商品");
        }
    }

    /**
     * 获取当前用户的购物车对象模板（仅含 userId）
     */
    private ShoppingCart getCurrentUserCartTemplate() {
        return ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
    }

    /**
     * 根据 DTO 构建完整的购物车对象（包含 dishId 或 setmealId）
     */
    private ShoppingCart buildCartFromDTO(ShoppingCartDTO dto) {
        return ShoppingCart.builder()
                .dishId(dto.getDishId())
                .setmealId(dto.getSetmealId())
                .userId(BaseContext.getCurrentId())
                .build();
    }

    /**
     * 查询当前用户下的匹配购物车记录
     */
    private Optional<ShoppingCart> findCartInDB(ShoppingCart cart) {
        List<ShoppingCart> list = shoppingCartMapper.list(cart);
        return list != null && !list.isEmpty() ? Optional.of(list.get(0)) : Optional.empty();
    }
}
