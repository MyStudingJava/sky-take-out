package com.sky.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端穿过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程的id" + Thread.currentThread().getId());

        // 需要将DTO转换为entity
        // 因为 DTO是service层,controller层是接受和往外传的,所以里面只能包含需要的数据
        Employee employee = new Employee();
        // 不用一个一个去set,用对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置账号状态,默认正常状态
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码,默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置创建时间、更新时间
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());

        // 设置创建人id、更新人id
        // employee.setCreateUser(BaseContext.getCurrentId());
        // employee.setUpdateUser(BaseContext.getCurrentId());

        // 使用持久层map插入
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 比如查询第一页
        // select * from employee limit 0,10
        // 这里使用MyBatis的分页插件 --- 动态sql进行拼接分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        // 根据员工id进行修改状态
        // update employee set status = ? where id = ?

        /*Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/

        // 第二种运用构建器
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    // 使用JsonIgnore注解,忽略密码
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        // employee.setPassword("******");
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        // 需要将DTO转换为entity
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }
}
