package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查询当前用户的所有地址信息
     */
    List<AddressBook> list();

    /**
     * 根据id设置默认地址
     * @param id
     * @return
     */
    void setDefault(Integer id);
}
