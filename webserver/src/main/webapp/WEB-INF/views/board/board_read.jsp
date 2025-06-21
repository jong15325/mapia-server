<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html lang="ko" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">
<head>
    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>

</head>
<body>
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
                            <div class="tab-content text-muted">
                                <div class="tab-pane fade show active" id="project-overview" role="tabpanel">
                                    <div class="card">
                                        <div class="card-header d-flex flex-column py-3">
                                            <div class="mb-2">
                                                <span class="badge bg-${boardDTO.boardTag.boardTagColor}-subtle text-${boardDTO.boardTag.boardTagColor}">
                                                    ${boardDTO.boardTag.boardTagNm}
                                                </span>
                                                <c:if test="${boardDTO.cmtIsAllow != 'Y'}">
                                                    <span class="badge bg-info-subtle text-primary">
                                                        댓글 비허용
                                                    </span>
                                                </c:if>
                                            </div>
                                            <h5 class="card-title mb-2">${boardDTO.boardTitle}</h5>
                                            <div class="d-flex align-items-center text-muted">
                                                <div class="me-auto">
                                                    <i class="ri-user-3-line me-1"></i> ${boardDTO.createdId}
                                                </div>
                                                <div class="me-3">
                                                    <i class="ri-time-line me-1"></i> ${boardDTO.formatCreatedAt("yyyy-MM-dd HH:mm")}
                                                </div>
                                                <div>
                                                    <i class="ri-eye-line me-1"></i> <fmt:formatNumber value="${boardDTO.boardViewCnt}" type="number" groupingUsed="true"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-body border-0 align-items-center">
                                            <div class="">
                                                <div>
                                                    ${boardDTO.boardCont}
                                                </div>
                                                <div class="pt-3 border-top border-top-dashed mt-4">
                                                    <h6 class="mb-3 fw-semibold text-uppercase">첨부파일</h6>
                                                    <div class="row g-3">
                                                        <c:if test="${empty fileList}">
                                                            <div class="col-12">
                                                                <p class="text-muted mb-0">첨부파일이 없습니다.</p>
                                                            </div>
                                                        </c:if>
                                                        <c:forEach var="file" items="${fileList}" varStatus="status">
                                                            <div class="col-xxl-4 col-lg-6">
                                                                <div class="border rounded border-dashed p-2">
                                                                    <div class="d-flex align-items-center">
                                                                        <div class="flex-shrink-0 me-3">
                                                                            <div class="rounded d-flex align-items-center justify-content-center fs-24 file-icon" data-extension="${file.fileExtension}">
                                                                                <i class="ri-file-line"></i>
                                                                            </div>
                                                                        </div>
                                                                        <div class="flex-grow-1 overflow-hidden">
                                                                                <h5 class="fs-13 mb-1"><a href="javascript:void(0);" onclick="downloadFile('${file.fileKey}')" class="text-body text-truncate d-block">${file.orgFileName}</a></h5>
                                                                                <div class="file-size" data-size="${file.fileSize}"></div>
                                                                        </div>
                                                                        <div class="flex-shrink-0 ms-2">
                                                                            <div class="d-flex gap-1">
                                                                                <button type="button" onclick="downloadFile('${file.fileKey}')" class="btn btn-icon text-muted btn-sm fs-18">
                                                                                    <i class="ri-download-2-line"></i>
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:forEach>
                                                        <!-- end col -->
                                                    </div>
                                                    <!-- end row -->
                                                </div>
                                            </div>
                                        </div>
                                        <!-- end card body -->
                                    </div>
                                    <!-- end card -->
                                    <c:if test="${boardDTO.cmtIsAllow == 'Y'}">
                                        <div class="card">
                                            <div class="card-header align-items-center d-flex">
                                                <h4 class="card-title mb-0 flex-grow-1">댓글</h4>
                                            </div><!-- end card header -->

                                            <div class="card-body">
                                                <div id="cmtArea" class="px-3 mx-n3 mb-2" style="max-height: 700px; overflow-y: auto;">
                                                    <c:if test="${empty cmtList}">
                                                        <p id="noCmtMsg" class="text-muted text-center">댓글이 없습니다.</p>
                                                    </c:if>
                                                    <c:forEach var="cmt" items="${cmtList}">
                                                        <c:if test="${empty cmt.rootCmtIdx}">
                                                            <%-- 최상위 댓글 그룹 시작 --%>
                                                            <div id="root_cmt_${cmt.boardCmtIdx}" class="comment-group mb-4">
                                                                <%-- 최상위 댓글 --%>
                                                                <div id="cmt_${cmt.boardCmtIdx}" class="cmt d-flex mb-3">
                                                                    <div class="flex-shrink-0">
                                                                        <img src="${not empty cmt.memberProfilePath ? cmt.memberProfilePath : '/img/user/default_profile.png'}" alt="" class="avatar-sm rounded-circle material-shadow" />
                                                                    </div>
                                                                    <div class="flex-grow-1 ms-3">
                                                                        <h5 class="fs-13">${cmt.createdId} <small class="text-muted ms-2">${cmt.getSmartDateFormat("HH:ss", "yyyy-MM-dd")}</small></h5>
                                                                        <p class="text-muted">${cmt.htmlCont}</p>
                                                                        <a href="javascript:void(0);" class="badge text-muted bg-light" onclick="addReplyForm('${cmt.boardCmtIdx}')">
                                                                            <i class="mdi mdi-reply"></i> 답글
                                                                        </a>
                                                                    </div>
                                                                </div>

                                                                <%-- 해당 최상위 댓글의 답글들 --%>
                                                                <c:forEach var="reply" items="${cmtList}">
                                                                    <c:if test="${reply.rootCmtIdx == cmt.boardCmtIdx}">
                                                                        <div id="cmt_${reply.boardCmtIdx}" class="cmt d-flex mb-3 ms-5">
                                                                            <div class="flex-shrink-0">
                                                                                <img src="${not empty reply.memberProfilePath ? reply.memberProfilePath : '/img/user/default_profile.png'}" alt="" class="avatar-sm rounded-circle material-shadow" />
                                                                            </div>
                                                                            <div class="flex-grow-1 ms-3">
                                                                                <h5 class="fs-13">${reply.createdId} <small class="text-muted ms-2">${reply.getSmartDateFormat("HH:ss", "yyyy-MM-dd")}</small></h5>
                                                                                <p class="text-muted">
                                                                                    <div class="d-flex align-items-start gap-2 flex-wrap">
                                                                                        <div class="dropdown">
                                                                                            <a class="fw-semibold text-info dropdown-btn link-body-emphasis" href="javascript:void(0);" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${reply.boardCmtToId} </a>
                                                                                            <div class="dropdown-menu dropdown-menu-start">
                                                                                                <a class="dropdown-item" href="#">게시글보기</a>
                                                                                                <a class="dropdown-item" href="#">1:1 채팅</a>
                                                                                                <a class="dropdown-item" href="#">쪽지보내기</a>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="flex-grow-1">
                                                                                            ${reply.htmlCont}
                                                                                        </div>
                                                                                    </div>
                                                                                </p>
                                                                                <a href="javascript:void(0);" class="badge text-muted bg-light" onclick="addReplyForm('${reply.boardCmtIdx}')">
                                                                                    <i class="mdi mdi-reply"></i> 답글
                                                                                </a>
                                                                            </div>
                                                                        </div>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </div>
                                                            <%-- 최상위 댓글 그룹 끝 --%>
                                                        </c:if>
                                                    </c:forEach>
                                                </div>
                                                <form id="cmtForm" class="mt-4">
                                                    <input type="hidden" id="boardIdx" name="boardIdx" value="${boardDTO.boardIdx}">
                                                    <div class="row g-3">
                                                        <div class="col-12">
                                                            <label for="cmtCont"></label><textarea class="form-control bg-light border-light" id="cmtCont" name="boardCmtCont" rows="3"
                                                            placeholder="댓글을 남겨보세요" style="resize: none;" oninput="autoResizeTextarea(this)" onpaste="setTimeout(() => autoResizeTextarea(this), 0)"></textarea>
                                                        </div>
                                                        <div class="col-12 text-end">
                                                            <a href="javascript:void(0);" id="cmtRegBtn" class="cmtRegBtn btn btn-success">등록</a>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                            <!-- end card body -->
                                        </div>
                                        <!-- end card -->
                                    </c:if>
                                </div>
                                <!-- end tab pane -->
                            </div>
                        </div>
                        <!-- end col -->
                    </div>
                    <!-- end row -->
                </div>
                <!-- container-fluid -->
            </div>
            <!-- End Page-content -->

            <%@ include file="../common/layout/footer.jsp" %>
        </div>
        <!-- end main content-->
    </div>
    <!-- END layout-wrapper -->

    <%@ include file="../common/script/script_footer.jsp" %>

    <script src="/assets/js/pages/project-overview.init.js"></script>
    <script src="/assets/js/app.js"></script>

    <script src="/js/board/board_read.js"></script>
</body>
</html>
