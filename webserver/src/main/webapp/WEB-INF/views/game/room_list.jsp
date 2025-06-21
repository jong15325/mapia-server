<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg" data-sidebar-image="none" data-preloader="disable">

<head>

    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>

    <script type="text/javascript">
        const API_HOST = '${apiHost}';
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

                    <div class="row g-4 mb-3">
                        <div class="col-sm-auto">
                            <div class="d-flex flex-wrap gap-2">
                                <button class="btn btn-danger add-btn" data-bs-toggle="modal" data-bs-target="#showModal"><i class="ri-add-line align-bottom me-1"></i> Create Task</button>
                                <button class="btn btn-soft-danger" id="remove-actions" onClick="deleteMultiple()"><i class="ri-delete-bin-2-line"></i></button>
                            </div>
                        </div>
                        <div class="col-sm">
                            <div class="d-flex justify-content-sm-end gap-2">
                                <div class="search-box ms-2">
                                    <input type="text" class="form-control" placeholder="Search...">
                                    <i class="ri-search-line search-icon"></i>
                                </div>

                                <select class="form-control w-md" data-choices data-choices-search-false>
                                    <option value="All" selected>전체</option>
                                    <option value="Today">방제</option>
                                    <option value="Yesterday">방장 아이디</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <c:forEach var="room" items="${rooms}">
                            <div class="col-xxl-3 col-sm-6 project-card">
                                <div class="card card-height-100">
                                    <div class="card-body">
                                        <div class="d-flex flex-column h-100">
                                            <div class="d-flex mb-2">
                                                <div class="flex-grow-1">
                                                    <h5 class="mb-0 fs-14"><a href="apps-projects-overview.html" class="text-body">${room.roomTitle}</a></h5>
                                                </div>
                                                <div class="flex-shrink-0">
                                                    <div class="d-flex gap-1 align-items-center">
                                                        <span class="badge bg-info-subtle text-black">
                                                            ${room.roomStatus}
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="d-flex mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <div class="avatar-sm">
                                                        <span class="avatar-title bg-warning-subtle rounded p-2">
                                                            <img src="/assets/images/brands/slack.png" alt="" class="img-fluid p-1">
                                                        </span>
                                                    </div>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h5 class="mb-1 fs-15"><a href="apps-projects-overview.html" class="text-body">ㅊㅊㅊㅊㅊ</a></h5>
                                                    <p class="text-muted text-truncate-two-lines mb-3">ㅁㅁㅁㅁㅁ</p>
                                                </div>
                                            </div>
                                            <div class="mt-auto">
                                                <div class="d-flex mb-2">
                                                    <div class="flex-grow-1">
                                                        <div>진행 (1일 밤)</div>
                                                    </div>
                                                    <div class="flex-shrink-0">
                                                        <div><i class="ri-list-check align-bottom me-1 text-muted"></i> 1/${room.roomMaxPlayerNum}</div>
                                                    </div>
                                                </div>
                                                <div class="progress progress-sm animated-progress">
                                                    <div class="progress-bar bg-success" role="progressbar" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100" style="width: 34%;"></div><!-- /.progress-bar -->
                                                </div><!-- /.progress -->
                                            </div>
                                        </div>
                                    </div>
                                    <!-- end card body -->
                                    <div class="card-footer bg-transparent border-top-dashed py-2">
                                        <div class="d-flex align-items-center">
                                            <div class="flex-grow-1">
                                                <div class="avatar-group">
                                                    <a href="javascript: void(0);" class="avatar-group-item" data-bs-toggle="tooltip" data-bs-trigger="hover" data-bs-placement="top" title="Darline Williams">
                                                        <div class="avatar-xxs">
                                                            <img src="/assets/images/users/avatar-2.jpg" alt="" class="rounded-circle img-fluid">
                                                        </div>
                                                    </a>
                                                   <%-- <a href="javascript: void(0);" class="avatar-group-item" data-bs-toggle="tooltip" data-bs-trigger="hover" data-bs-placement="top" title="Add Members">
                                                        <div class="avatar-xxs">
                                                            <div class="avatar-title fs-16 rounded-circle bg-light border-dashed border text-primary">
                                                                +
                                                            </div>
                                                        </div>
                                                    </a>--%>
                                                </div>
                                            </div>
                                            <div class="flex-shrink-0">
                                                <%--<div class="text-muted">
                                                    <i class="ri-calendar-event-fill me-1 align-bottom"></i> ${room.getSmartDateFormat("HH:ss", "yyyy-MM-dd")}
                                                </div>--%>
                                                <div>
                                                    <a href="apps-projects-create.html" class="btn btn-success"><i class="ri-add-line align-bottom me-1"></i> 입장</a>
                                                </div>
                                            </div>

                                        </div>

                                    </div>
                                    <!-- end card footer -->
                                </div>
                                <!-- end card -->
                            </div>
                            <!-- end col -->
                        </c:forEach>
                    </div>

                    <div class="row g-0 text-center text-sm-start align-items-center mb-4">
                        <div class="col-sm-6">
                            <div>
                                <p class="mb-sm-0 text-muted">Showing <span class="fw-semibold">1</span> to <span class="fw-semibold">10</span> of <span class="fw-semibold text-decoration-underline">12</span> entries</p>
                            </div>
                        </div>
                        <!-- end col -->
                        <div class="col-sm-6">
                            <ul class="pagination pagination-separated justify-content-center justify-content-sm-end mb-sm-0">
                                <li class="page-item disabled">
                                    <a href="#" class="page-link">Previous</a>
                                </li>
                                <li class="page-item active">
                                    <a href="#" class="page-link">1</a>
                                </li>
                                <li class="page-item ">
                                    <a href="#" class="page-link">2</a>
                                </li>
                                <li class="page-item">
                                    <a href="#" class="page-link">3</a>
                                </li>
                                <li class="page-item">
                                    <a href="#" class="page-link">4</a>
                                </li>
                                <li class="page-item">
                                    <a href="#" class="page-link">5</a>
                                </li>
                                <li class="page-item">
                                    <a href="#" class="page-link">Next</a>
                                </li>
                            </ul>
                        </div><!-- end col -->
                    </div><!-- end row -->
                </div>

                <div class="modal fade zoomIn" id="showModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-lg">
                        <div class="modal-content border-0">
                            <div class="modal-header p-3 bg-info-subtle">
                                <h5 class="modal-title" id="exampleModalLabel">방 만들기</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" id="close-modal"></button>
                            </div>
                            <div class="modal-body">
                                <div class="row g-3">
                                    <div class="col-lg-12">
                                        <label for="roomTitle" class="form-label">방제</label>
                                        <input type="text" id="roomTitle" name="roomTitle" class="form-control" placeholder="생성할 방의 제목을 입력해주세요" required />
                                    </div>
                                    <!--end col-->
                                    <div class="col-lg-12">
                                        <label for="roomPwd" class="form-label">비밀번호</label>
                                        <input type="text" id="roomPwd" name="roomPwd" class="form-control" placeholder="비밀방을 만드시려면 비밀번호를 입력해주세요" />
                                    </div>
                                    <!--end col-->
                                    <%--<div class="col-lg-6">
                                        <label for="ticket-status" class="form-label">모드</label>
                                        <select class="form-control" id="ticket-status">
                                            <option value="">1</option>
                                            <option value="New">2</option>
                                        </select>
                                    </div>--%>
                                    <!--end col-->
                                </div>
                                <!--end row-->
                            </div>
                            <div class="modal-footer">
                                <div class="hstack gap-2 justify-content-end">
                                    <button type="button" class="btn btn-light" id="close-modal" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-success" id="add-btn" onclick="createRoom();">Add Task</button>
                                    <!-- <button type="button" class="btn btn-success" id="edit-btn">Update Task</button> -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--end modal-->

                <!-- container-fluid -->
            </div>
            <!-- End Page-content -->
        </div>
        <!-- end main content-->
    </div>
    <!-- END layout-wrapper -->

    <%@ include file="../common/script/script_footer.jsp" %>

    <!-- project list init -->
    <script src="/assets/js/pages/project-list.init.js"></script>

    <!-- App js -->
    <script src="/assets/js/app.js"></script>

    <script src="/js/game/room_list.js"></script>

</body>

</html>