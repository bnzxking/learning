package com.bnzxking.learning.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bnzxking on 2018/5/2.
 */
public interface BlogMapper {
    Blog selectBlog(@Param("ids")List<Integer> ids);
}
