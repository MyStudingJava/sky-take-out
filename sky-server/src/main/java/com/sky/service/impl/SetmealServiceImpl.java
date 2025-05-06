package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CommonService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private CommonService commonService;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 向套餐表插入1条数据，获取插入之后的id
        setmealMapper.insert(setmeal);

        // 获取insert套餐的id
        Long setmealId = setmeal.getId();

        // 向套餐菜品关系表插入多条数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealId);
            }

            // 批量插入
            setmealDishMapper.insertBatch(setmealDishes);
        }

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        // 根据id查询套餐数据
        Setmeal setmeal = setmealMapper.getById(id);

        // 根据id查询套餐菜品关系数据
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        // 将查询到的数据组装封装到VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        // 修改套餐基表的基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.update(setmeal);

        // 删除原有套餐关联的菜品数据
        setmealDishMapper.deleteById(setmealDTO.getId());

        // 重新插入套餐关联的菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }

            setmealDishMapper.insertBatch(setmealDishes);
        }


    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void delectBatch(List<Long> ids) {
        // 判断当前套餐是否能否删除 -- 是否关联了菜品 ??
        List<Long> setmealIdsByDishIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIdsByDishIds != null && setmealIdsByDishIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 判断当前套餐是否能否删除 -- 是否存在起售中的套餐 ??
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE) {
                // 存在起售中的套餐，不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }

            // 删除图片
            commonService.deleteFile(setmeal.getImage());
        }

        // 删除套餐
        setmealMapper.deleteByIds(ids);

        // 删除套餐关联的菜品数据
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 开启或关闭套餐
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();

        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long setmealId) {
        return setmealMapper.getDishItemById(setmealId);
    }
}
