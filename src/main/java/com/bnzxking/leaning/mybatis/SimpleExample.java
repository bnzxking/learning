package com.bnzxking.leaning.mybatis;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by bnzxking on 2018/4/20.
 */
public class SimpleExample {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://10.140.99.196/aaa";

    public static void main(String[] arg) throws ClassNotFoundException, SQLException, IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        Configuration configuration = sqlSessionFactory.getConfiguration();
//        configuration.addMapper(BlogMapper.class);
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);
        ids.add(5);
        Blog blog = mapper.selectBlog(ids);
        System.out.println(blog);
    }



}
