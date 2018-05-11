package com.bnzxking.leaning.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by bnzxking on 2018/5/2.
 */
public interface BlogMapper {
    Blog selectBlog(@Param("ids")List<Integer> ids);
}
