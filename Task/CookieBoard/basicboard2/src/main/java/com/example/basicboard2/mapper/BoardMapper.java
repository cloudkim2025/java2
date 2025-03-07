package com.example.basicboard2.mapper;

import com.example.basicboard2.model.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    void saveArticle(Article article);
}
