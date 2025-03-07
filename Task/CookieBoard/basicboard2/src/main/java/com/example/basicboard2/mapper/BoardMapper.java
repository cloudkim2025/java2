package com.example.basicboard2.mapper;

import com.example.basicboard2.model.Article;
import com.example.basicboard2.model.Paging;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    void saveArticle(Article article);
    List<Article> getArticles(Paging page);
    int getArticleCnt();
}