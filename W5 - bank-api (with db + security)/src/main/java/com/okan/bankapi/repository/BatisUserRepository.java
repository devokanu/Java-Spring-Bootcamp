package com.okan.bankapi.repository;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.okan.bankapi.model.UserModel;

@Mapper
public class BatisUserRepository implements UserRepository {

	Reader reader;

	private SqlSessionFactory init(Reader reader) {
		try {
			reader = Resources.getResourceAsReader("myBatis_conf.xml");
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			return sqlSessionFactory;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserModel loadUserByUsername(String username) {

		SqlSession session = init(reader).openSession();
		UserModel user = null;

		if (session != null) {
			user = session.selectOne("user-mapper.loadUserByUsername", username);
		}
		return user;
	}

}
