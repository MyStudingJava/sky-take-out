package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    @Insert("insert into address_book (user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default, create_time, update_time, create_user, update_user) " +
             "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(AddressBook addressBook);

    /**
     * 根据id查询
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> list(Long userId);
}
