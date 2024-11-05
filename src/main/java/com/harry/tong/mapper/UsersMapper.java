package com.harry.tong.mapper;

import com.harry.tong.entity.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UsersMapper  extends Mapper<User> {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectSave(String username);
}
