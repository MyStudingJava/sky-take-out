package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.SelmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐管理
 */
@RestController("userSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SelmealService SelmealService;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result list(Long categoryId){
        List<Setmeal> list = SelmealService.list(categoryId);

        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param setmealId
     * @return
     */
    @GetMapping("/dish/{setmealId}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result getBySetmealId(Long setmealId){
        SetmealVO setmealVO = SelmealService.getBySetmealId(setmealId);

        return Result.success(setmealVO);
    }



}
