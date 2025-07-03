<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg" data-sidebar-image="none" data-preloader="disable">

<head>

    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>

    <script type="text/javascript">
        window.gameConfig = {
            serverUrl: '${apiHost}',
            webSocketUrl: 'ws://'+'${apiHost}'+'/game-socket', // WebSocket URL
            accessToken: '${gameAccessToken}',
            tokenExpiryMinutes: ${expAt}
        };

        //console.log('[GAME CONFIG] 게임 설정 초기화:', window.gameConfig);
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

                    <!-- 연결 상태 및 제어 패널 -->
                    <div class="row mb-3">
                        <div class="col-md-12">
                            <div class="alert alert-info d-flex align-items-center" id="connectionStatus">
                                <i class="ri-wifi-line me-2"></i>
                                <span id="connectionText">게임서버 연결 중...</span>
                            </div>
                        </div>
                        <%--<div class="col-md-4">
                            <div class="d-flex gap-2">
                                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="checkConnection()">
                                    <i class="ri-refresh-line me-1"></i> 연결 확인
                                </button>
                                <button type="button" class="btn btn-outline-info btn-sm" onclick="manualRefreshToken()">
                                    <i class="ri-key-line me-1"></i> 토큰 갱신
                                </button>
                            </div>
                        </div>--%>
                    </div>

                    <!-- 방 생성 버튼 -->
                    <div class="row mb-3">
                        <div class="col-12">
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createRoomModal">
                                <i class="ri-add-line me-1"></i> 방 만들기
                            </button>
                            <button type="button" class="btn btn-danger" onclick="deleteAllRooms();">
                                <i class="ri-subtract-line me-1"></i> 전체 방 삭제
                            </button>
                            <small class="text-muted ms-2">
                                현재 ${rooms.size()}개의 방이 있습니다.
                            </small>
                        </div>
                    </div>

                    <!-- 룸 리스트 -->
                    <div class="row" id="roomList">
                        <c:choose>
                            <c:when test="${not empty rooms}">
                                <c:forEach var="room" items="${rooms}" varStatus="status">
                                    <div class="col-xl-3 col-lg-4 col-md-6 mb-3">
                                        <div class="card">
                                            <div class="card-body">
                                                <div class="d-flex align-items-center mb-3">
                                                    <div class="avatar-sm rounded bg-light d-flex align-items-center justify-content-center me-3">
                                                        <i class="ri-group-line text-primary fs-16"></i>
                                                    </div>
                                                    <div class="flex-grow-1">
                                                        <h6 class="mb-1">${room.roomTitle}</h6>
                                                        <small class="text-muted">
                                                            ${room.roomPlayerNum}/${room.roomMaxPlayerNum}명
                                                            <c:if test="${not empty room.roomPwd}">
                                                                <i class="ri-lock-line ms-1" title="비밀방"></i>
                                                            </c:if>
                                                        </small>
                                                    </div>
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <span class="badge bg-success">${room.roomStatus}</span>
                                                    <button type="button" class="btn btn-sm btn-outline-primary"
                                                            onclick="joinRoom('${room.roomId}', '${room.roomPwd}')">
                                                        참가하기
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="col-12">
                                    <div class="text-center py-5">
                                        <i class="ri-chat-3-line display-4 text-muted"></i>
                                        <h5 class="mt-3 text-muted">생성된 방이 없습니다</h5>
                                        <p class="text-muted">새로운 방을 만들어 게임을 시작해보세요!</p>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        <!-- end main content-->
    </div>
    <!-- END layout-wrapper -->

    <!-- 방 생성 모달 -->
    <div class="modal fade zoomIn" id="createRoomModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content border-0">
                <div class="modal-header p-3 bg-info-subtle">
                    <h5 class="modal-title" id="exampleModalLabel">방 만들기</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" id="close-modal"></button>
                </div>
                <div class="modal-body">
                    <form id="createRoomForm">
                        <div class="mb-3">
                            <label for="roomTitle" class="form-label">방 제목</label>
                            <input type="text" class="form-control" id="roomTitle" name="roomTitle"
                                   placeholder="방 제목을 입력하세요" required maxlength="30">
                            <div class="form-text">최대 30자까지 입력 가능합니다.</div>
                        </div>
                        <div class="mb-3">
                            <label for="roomPwd" class="form-label">비밀번호 (선택사항)</label>
                            <input type="password" class="form-control" id="roomPwd" name="roomPwd"
                                   placeholder="비밀번호를 설정하면 비밀방이 됩니다" maxlength="20">
                            <div class="form-text">비밀번호를 설정하지 않으면 공개방으로 생성됩니다.</div>
                        </div>
                        <div class="mb-3">
                            <label for="roomMaxPlayerNum" class="form-label">최대 인원</label>
                            <select class="form-select" id="roomMaxPlayerNum" name="roomMaxPlayerNum">
                                <option value="6">6명</option>
                                <option value="8" selected>8명</option>
                                <option value="12">12명</option>
                            </select>
                        </div>
                    </form>
                    <div class="modal-footer">
                        <div class="hstack gap-2 justify-content-end">
                            <button type="button" class="btn btn-light" id="close-modal" data-bs-dismiss="modal">닫기</button>
                            <button type="submit" class="btn btn-success" id="add-btn" onclick="createRoom();">방 만들기</button>
                            <!-- <button type="button" class="btn btn-success" id="edit-btn">Update Task</button> -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--end modal-->

    <%@ include file="../common/script/script_footer.jsp" %>

    <!-- project list init -->
    <script src="/assets/js/pages/project-list.init.js"></script>

    <!-- App js -->
    <script src="/assets/js/app.js"></script>

    <%-- GAME --%>
    <script src="/js/game/common.js"></script>
    <script src="/js/game/handlers.js"></script>
    <script src="/js/game/room_list.js"></script>
</body>

</html>