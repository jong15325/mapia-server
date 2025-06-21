package me.jjh.mapia.webserver.dto.board;

import lombok.*;

/**
 * packageName    : me.jjh.mapia.dto
 * fileName       : BoardSearchDTO
 * author         : JJH
 * date           : 2025-01-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-16        JJH       최초 생성
 */

@NoArgsConstructor
@Getter
@Setter
public class BoardSearchDTO {
    private int pageNum = 0;       // 현재 페이지 번호
    private int pageSize = 8;      // 페이지당 게시글 수
    private int navigatePages = 5;    // 네비게이션에 보여줄 페이지 수

    /* 검색 및 필터 */
    private String searchKeyword;      // 검색어
    private String searchType;   // 검색 유형 (title, content, author)
    private String searchTag; // 태그 검색
    private String searchDate; //날짜 검색
    private String sortBy = "BOARD_IDX";  // 정렬 기준
    private String sortOrder = "DESC";    // 정렬 순서
    private String ctgType; // 게시판 타입
    private Long searchIdx;

    /* 날짜 분기 */
    private String startDate;             // 자동 분기 처리된 시작일
    private String endDate;               // 자동 분기 처리된 종료일

    /* 날짜 자동 분기 처리 */
    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;

        if (searchDate != null && !searchDate.isEmpty()) {
            if (searchDate.contains("~")) {
                // 범위 값 처리
                String[] dates = searchDate.split("~");
                this.startDate = dates[0].trim();
                this.endDate = dates[1].trim();
            }
        }
    }
}
