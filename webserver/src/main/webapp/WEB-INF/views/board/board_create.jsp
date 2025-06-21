<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">
<head>

    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>

    <!-- Plugins css -->
    <link href="/assets/libs/dropzone/dropzone.css" rel="stylesheet" type="text/css" />

    <script>
        let fileConfig = JSON.parse('${fileConfig}');
    </script>
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

                    <form id="boardForm" method="POST" enctype="multipart/form-data" onsubmit="return false;">
                        <input type="hidden" name="boardCtg.boardCtgType" value="${search.ctgType}"/>
                        <div class="row">
                            <div class="card">
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div class="mb-4">
                                                <label class="form-label" for="board-tag">말머리</label>
                                                <select name="boardTag.boardTagIdx" id="board-tag" class="form-select">
                                                    <c:forEach var="boardTagList" items="${boardTagList}">
                                                        <option value="${boardTagList.boardTagIdx}">${boardTagList.boardTagNm}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-md-9">
                                            <div class="mb-4">
                                                <label class="form-label" for="project-title-input">제목</label>
                                                <input name="boardTitle" type="text" class="form-control" id="project-title-input" placeholder="제목을 입력해주세요">
                                            </div>
                                        </div>
                                    </div>

                                    <%--<div class="mb-3">
                                        <label class="form-label">작성자</label>
                                        <div>ddd</div>
                                        &lt;%&ndash;<input class="form-control" id="project-thumbnail-img" type="file" accept="image/png, image/gif, image/jpeg">&ndash;%&gt;
                                    </div>--%>

                                    <div class="mb-3">
                                        <label class="form-label">내용</label>
                                        <div id="ckeditor-classic">
                                        </div>
                                        <input type="hidden" name="boardCont" id="boardCont">
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-6">
                                            <div class="mb-3 mb-lg-0">
                                                <label for="choices-status-input" class="form-label">공개 여부</label>
                                                <select name="isActive" class="form-select" data-choices data-choices-search-false id="choices-status-input">
                                                    <option value="Y" selected>공개</option>
                                                    <option value="N">비공개</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-lg-6">
                                            <div class="mb-3 mb-lg-0">
                                                <label for="choices-priority-input" class="form-label">댓글 허용</label>
                                                <select name="cmtIsAllow" class="form-select" data-choices data-choices-search-false id="choices-priority-input">
                                                    <option value="Y" selected>허용</option>
                                                    <option value="N">비허용</option>
                                                </select>
                                            </div>
                                        </div>
                                        <%--<div class="col-lg-4">
                                            <div>
                                                <label for="datepicker-deadline-input" class="form-label">내용 공개일</label>
                                                <input type="text" class="form-control" id="datepicker-deadline-input" placeholder="Enter due date" data-provider="flatpickr">
                                            </div>
                                        </div>--%>
                                    </div>
                                </div>
                                <!-- end card body -->
                            </div>
                            <!-- end card -->

                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title mb-0">파일 업로드</h5>
                                </div>
                                <div class="card-body">
                                    <div>
                                        <p class="text-muted">파일을 추가해주세요</p>

                                        <div class="dropzone">
                                            <div class="fallback">
                                                <input name="file" type="file" multiple="multiple">
                                            </div>
                                            <div class="dz-message needsclick">
                                                <div class="mb-3">
                                                    <i class="display-4 text-muted ri-upload-cloud-2-fill"></i>
                                                </div>

                                                <h5>파일을 이곳에 드랍해서 업로드도 가능합니다</h5>
                                            </div>
                                        </div>

                                        <ul class="list-unstyled mb-0" id="dropzone-preview">
                                            <li class="mt-2" id="dropzone-preview-list">
                                                <!-- This is used as the file preview template -->
                                                <div class="border rounded">
                                                    <div class="d-flex p-2">
                                                        <div class="flex-shrink-0 me-3">
                                                            <div class="avatar-sm bg-light rounded">
                                                                <img src="#" alt="Project-Image" data-dz-thumbnail class="img-fluid rounded d-block" />
                                                            </div>
                                                        </div>
                                                        <div class="flex-grow-1">
                                                            <div class="pt-1">
                                                                <h5 class="fs-14 mb-1" data-dz-name>&nbsp;</h5>
                                                                <p class="fs-13 text-muted mb-0" data-dz-size></p>
                                                                <strong class="error text-danger" data-dz-errormessage></strong>
                                                            </div>
                                                        </div>
                                                        <div class="flex-shrink-0 ms-3">
                                                            <button data-dz-remove class="btn btn-sm btn-danger">삭제</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                        <!-- end dropzon-preview -->
                                    </div>
                                </div>
                            </div>
                            <!-- end card -->
                            <div class="text-end mb-4">
                                <%--<button type="submit" class="btn btn-danger w-sm">삭제</button>--%>
                                <%--<button type="submit" class="btn btn-secondary w-sm">수정</button>--%>
                                <button type="submit" class="btn btn-success w-sm" id="createBtn">등록</button>
                            </div>
                        </div>
                        <!-- end row -->
                    </form>
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

    <!-- ckeditor -->
    <script src="/assets/libs/@ckeditor/ckeditor5-build-classic/build/ckeditor.js"></script>

    <!-- dropzone js -->
    <script src="/assets/libs/dropzone/dropzone-min.js"></script>
    <!-- project-create init -->
    <%--<script src="/assets/js/pages/project-create.init.js"></script>--%>

    <!-- App js -->
    <script src="/assets/js/app.js"></script>

    <script src="/js/board/board_create.js"></script>
</body>

</html>