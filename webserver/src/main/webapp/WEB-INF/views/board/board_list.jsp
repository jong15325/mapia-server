<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="ko" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">
<head>

    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>

</head>

<body>

    <!-- Begin page -->
    <div id="layout-wrapper">
        <%@ include file="../common/layout/topbar.jsp" %>
        <%@ include file="../common/layout/sidebar.jsp" %>

        <!-- ============================================================== -->
        <!-- Start right Content here -->
        <!-- ============================================================== -->
        <div class="main-content">

            <div class="page-content">
                <div class="container-fluid">

                    <%@ include file="../common/layout/page_title.jsp" %>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="card" id="applicationList">
                                <form id="schFrm" action="/board/list" method="get">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <%-- 게시판 카테고리 타입 --%>
                                    <input type="hidden" name="ctgType" value="${search.ctgType}"/>
                                    <%-- 게시판 페이지 --%>
                                    <input type="hidden" id="pageNum" name="pageNum" value="${search.pageNum}"/>

                                    <div class="card-header  border-0">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <h5 class="card-title mb-0">자유게시판</h5>
                                            <div class="flex-shrink-0">
                                                <div class="d-flex gap-1 flex-wrap">
                                                    <button type="button" class="btn btn-success add-btn" onclick="changePostSubmit('/board/create')">
                                                        <i class="ri-edit-fill align-bottom me-1"></i> 게시글 작성
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-body border border-dashed border-end-0 border-start-0">
                                        <div class="row g-3 d-flex justify-content-end">
                                            <!--end col-->
                                            <div class="col-xxl-3 col-sm-3">
                                                <div>
                                                    <input type="text" name="searchDate" class="form-control" data-provider="flatpickr"
                                                           data-date-format="Y-m-d" data-range-date="true" id="demo-datepicker" value="${search.searchDate}" placeholder="날짜선택">
                                                </div>
                                            </div>
                                            <!--end col-->
                                            <div class="col-xxl-3 col-sm-3">
                                                <div>
                                                    <select class="form-select" name="searchType">
                                                        <option value="" ${search.searchType == "" ? "selected" : ""}>제목+내용</option>
                                                        <option value="title" ${search.searchType == "title" ? "selected" : ""}>제목</option>
                                                        <option value="content" ${search.searchType == "content" ? "selected" : ""}>내용</option>
                                                        <option value="writer" ${search.searchType == "writer" ? "selected" : ""}>글작성자</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <!--end col-->
                                            <div class="col-xxl-5 col-sm-5">
                                                <div class="input-group">
                                                    <input type="text" class="form-control" name="searchKeyword" value="${search.searchKeyword}" placeholder="조회할 검색어를 입력해주세요">
                                                    <button class="btn btn-outline-success" type="submit" id="button-addon2">조회</button>
                                                </div>
                                            </div>
                                        </div>
                                        <!--end row-->
                                    </div>
                                </form>
                                <div class="card-body">
                                    <div class="table-responsive table-card">
                                        <table class="table table-hover align-middle table-nowrap table-striped-columns mb-0">
                                            <colgroup>
                                                <col style="width:3%">
                                                <col style="width:5%">
                                                <col>
                                                <col style="width:15%">
                                                <col style="width:15%">
                                                <col style="width:10%">
                                            </colgroup>
                                            <thead class="table-light">
                                            <tr>
                                                <th scope="col">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" value="" id="cardtableCheck">
                                                        <label class="form-check-label" for="cardtableCheck"></label>
                                                    </div>
                                                </th>
                                                <th scope="col" class="text-center">말머리</th>
                                                <th scope="col" class="text-center">제목</th>
                                                <th scope="col" class="text-center">작성자</th>
                                                <th scope="col" class="text-center">작성일</th>
                                                <th scope="col" class="text-center">조회</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="boardList" items="${boardList}">
                                                    <tr style="cursor: pointer;">
                                                        <td class="text-center">
                                                            <div class="form-check">
                                                                <input class="form-check-input" type="checkbox" value="" id="cardtableCheck01">
                                                                <label class="form-check-label" for="cardtableCheck01"></label>
                                                            </div>
                                                        </td>
                                                        <td class="text-center">
                                                           <c:choose>
                                                                <c:when test="${not empty boardList.boardTag.boardTagNm}">
                                                                    <span class="badge bg-${boardList.boardTag.boardTagColor}-subtle text-${boardList.boardTag.boardTagColor}">
                                                                            ${boardList.boardTag.boardTagNm}
                                                                    </span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    -
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td><a class="link-body-emphasis" href="/board/read?searchIdx=${boardList.boardIdx}">${boardList.boardTitle} </a></td>
                                                        <td class="text-center">
                                                            <div class="dropdown">
                                                                <a class="dropdown-btn link-body-emphasis" href="javascript:void(0);" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${boardList.createdId} </a>
                                                               <div class="dropdown-menu dropdown-menu-start" style="">
                                                                    <a class="dropdown-item" href="#">게시글보기</a>
                                                                    <a class="dropdown-item" href="#">1:1 채팅</a>
                                                                    <a class="dropdown-item" href="#">쪽지보내기</a>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td class="text-center">${boardList.getSmartDateFormat("HH:ss", "yyyy-MM-dd")}</td>
                                                        <td class="text-center"><fmt:formatNumber value="${boardList.boardViewCnt}" type="number" groupingUsed="true"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <div class="d-flex justify-content-center">
                                        <ul class="pagination pagination-separated">
                                            <c:if test="${pageInfo.hasPreviousPage}">
                                                <li class="page-item">
                                                    <a href="javascript:void(0);" class="page-link" onclick="movePage(pageNum=${pageInfo.pageNum - 5 > 1 ? pageInfo.pageNum - 5 : 1})">
                                                        <i class="ri-arrow-left-fill"></i>
                                                    </a>
                                                </li>
                                            </c:if>
                                            <c:forEach var="pageNum" items="${pageInfo.navigatepageNums}">
                                                <li class="page-item ${pageNum == pageInfo.pageNum ? 'active' : ''}">
                                                    <a href="javascript:void(0);" class="page-link" onclick="movePage(pageNum=${pageNum})">${pageNum}</a>
                                                </li>
                                            </c:forEach>
                                            <c:if test="${pageInfo.hasNextPage}">
                                                <li class="page-item">
                                                    <a href="javascript:void(0);" class="page-link" onclick="movePage(pageNum=${pageInfo.pageNum + 5 <= pageInfo.pages ? pageInfo.pageNum + 5 : pageInfo.pages})">
                                                        <i class="ri-arrow-right-fill"></i>
                                                    </a>
                                                </li>
                                            </c:if>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- container-fluid -->
            </div>
            <!-- End Page-content -->

            <%@ include file="../common/layout/footer.jsp" %>

        </div>
        <!-- end main content-->

    </div>
    <!-- END layout-wrapper -->

    <%--<%@ include file="../common/layout/custom.jsp" %>--%>
    <%@ include file="../common/script/script_footer.jsp" %>

    <!-- flatpickr.js -->
    <script type='text/javascript' src='/assets/libs/flatpickr/flatpickr.min.js'></script>

    <!-- app js -->
    <script src="/assets/js/app.js"></script>

</body>
</html>