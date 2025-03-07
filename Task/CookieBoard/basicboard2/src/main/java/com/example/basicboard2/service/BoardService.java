package com.example.basicboard2.service;

import com.example.basicboard2.mapper.BoardMapper;
import com.example.basicboard2.model.Article;
import com.example.basicboard2.model.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ✅ 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service // Spring의 Service 계층으로 등록
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입을 자동화
public class BoardService {

    private final BoardMapper boardMapper; // 게시글 관련 데이터베이스 매퍼
    private final FileService fileService; // 파일 업로드 서비스

    /**
     * ✅ 게시글 목록을 조회하는 메서드 (페이징 처리 포함)
     * @param page 요청한 페이지 번호
     * @param size 페이지당 표시할 게시글 개수
     * @return 게시글 목록
     */
    public List<Article> getBoardArticles(int page, int size) {
        int offset = (page - 1) * size; // 페이지는 1부터 시작하므로 offset 계산
        return boardMapper.getArticles(
                Paging.builder()
                        .offset(offset)
                        .size(size)
                        .build()
        );
    }

    /**
     * ✅ 새로운 게시글을 저장하는 메서드 (파일 업로드 포함)
     * @param userId 작성자 ID
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param file 첨부 파일 (선택 사항)
     */
    @Transactional // 트랜잭션 적용 (데이터 일관성 유지)
    public void saveArticle(String userId, String title, String content, MultipartFile file) {
        String path = null;
        if (!file.isEmpty()) { // 파일이 존재하는 경우 파일 업로드 수행
            path = fileService.fileUpLoad(file);
        }

        // 게시글 저장
        boardMapper.saveArticle(
                Article.builder()
                        .title(title)
                        .content(content)
                        .userId(userId)
                        .filePath(path) // 업로드된 파일 경로 저장
                        .build()
        );
    }

    /**
     * ✅ 총 게시글 개수를 조회하는 메서드
     * @return 게시글 개수
     */
    public int getTotalArticleCnt() {
        return boardMapper.getArticleCnt();
    }
}
