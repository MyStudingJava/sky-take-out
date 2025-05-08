package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {

    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询当前用户的所有地址信息
     * @return
     */
    @Override
    public List<AddressBook> list() {
        return addressBookMapper.list(BaseContext.getCurrentId());
    }

    /**
     * 设置默认地址
     * @param id
     */
    @Override
    public void setDefault(Integer id) {
        AddressBook addressBook = AddressBook.builder()
                .isDefault(1)
                .id(id)
                .build();

        addressBookMapper.update(addressBook);
    }
}
