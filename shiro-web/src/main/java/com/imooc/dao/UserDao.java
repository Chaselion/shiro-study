package com.imooc.dao;

import java.util.List;

import com.imooc.vo.User;

public interface UserDao {

	User getUserByUserName(String userName);

	List<String> queryRolesByUserName(String userName);

}
