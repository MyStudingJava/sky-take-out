package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端-地址簿接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result<String> add(@RequestBody AddressBook addressBook){
        addressBookService.save(addressBook);

        return Result.success();
    }


    /**
     *
     * @param id
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result<String> setDefault(@RequestBody Integer id){
        addressBookService.setDefault(id);

        return Result.success();
    }

    /**
     * 查询当前用户的所有地址信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户的所有地址信息")
    public Result<List<AddressBook>> list(){
        List<AddressBook> list =  addressBookService.list();

        return Result.success(list);
    }
}
