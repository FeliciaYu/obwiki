package com.example.obwiki.mapper;

import com.example.obwiki.entity.Ebook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 电子书 Mapper 接口
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@Mapper
public interface EbookMapper extends BaseMapper<Ebook> {

    void increaseViewCount(Long ebookId);
}
