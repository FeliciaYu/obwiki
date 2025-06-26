package com.example.obwiki.mapper;

import com.example.obwiki.entity.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@Mapper
public interface DocMapper extends BaseMapper<Doc> {

    void increaseViewCount(Long id);

    void increaseVoteCount(Long id);
}
