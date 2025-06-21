<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">

<head>

    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>

    <script>
        let memberId = "${memberId}"; // 서버에서 넘겨준 사용자 아이디
        let verifyToken = "${verifyToken}"; // 서버에서 넘겨준 인증 토큰
        let verifyExpAt = "${verifyExpAt}"; // 서버에서 넘겨준 만료 시간 (밀리초)
    </script>
</head>

<body>

    <div class="auth-page-wrapper pt-5">
        <!-- auth page bg -->
        <div class="auth-one-bg-position auth-one-bg" id="auth-particles">
            <div class="bg-overlay"></div>
        </div>

        <!-- auth page content -->
        <div class="auth-page-content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="text-center mt-sm-5 mb-4 text-white-50">
                            <div>
                                <a href="/" class="d-inline-block auth-logo">
                                    <img src="/img/common/main_con.png" alt="" height="20" style="height: 7.5rem;width: 9.5rem;">
                                </a>
                            </div>
                            <%--<p class="mt-3 fs-15 fw-medium">Premium Admin & Dashboard Template</p>--%>
                        </div>
                    </div>
                </div>
                <!-- end row -->

                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-6 col-xl-5">
                        <div class="card mt-4 card-bg-fill">

                            <div class="card-body p-4">
                                <div class="mb-4">
                                    <div class="avatar-lg mx-auto">
                                        <div class="avatar-title bg-light text-primary display-5 rounded-circle">
                                            <i class="ri-mail-line"></i>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center mt-3">
                                    <p id="countdownTimer" style="font-size: 16px; font-weight: bold; color: red;"></p>
                                </div>
                                <div class="p-2 mt-4">
                                    <div class="text-muted text-center mb-4 mx-lg-3">
                                        <h4>인증코드를 확인해주세요</h4>
                                        <p>아래에 인증 코드 4자리를 입력해주세요</p>
                                    </div>

                                    <form id="verifyCodeFrm" autocomplete="off">
                                        <div class="row">
                                            <div class="col-3">
                                                <div class="mb-3">
                                                    <label for="digit1-input" class="visually-hidden">Digit 1</label>
                                                    <input type="text" name="verifyCode" class="form-control form-control-lg bg-warning-subtle border-light text-center" onkeyup="moveToNext(1, event)" maxLength="1" id="digit1-input" required>
                                                </div>
                                            </div><!-- end col -->

                                            <div class="col-3">
                                                <div class="mb-3">
                                                    <label for="digit2-input" class="visually-hidden">Digit 2</label>
                                                    <input type="text" name="verifyCode" class="form-control form-control-lg bg-warning-subtle border-light text-center" onkeyup="moveToNext(2, event)" maxLength="1" id="digit2-input" required>
                                                </div>
                                            </div><!-- end col -->

                                            <div class="col-3">
                                                <div class="mb-3">
                                                    <label for="digit3-input" class="visually-hidden">Digit 3</label>
                                                    <input type="text" name="verifyCode" class="form-control form-control-lg bg-warning-subtle border-light text-center" onkeyup="moveToNext(3, event)" maxLength="1" id="digit3-input" required>
                                                </div>
                                            </div><!-- end col -->

                                            <div class="col-3">
                                                <div class="mb-3">
                                                    <label for="digit4-input" class="visually-hidden">Digit 4</label>
                                                    <input type="text" name="verifyCode" class="form-control form-control-lg bg-warning-subtle border-light text-center" onkeyup="moveToNext(4, event)" maxLength="1" id="digit4-input" required>
                                                </div>
                                            </div><!-- end col -->
                                        </div>
                                    </form><!-- end form -->

                                    <div class="mt-3">
                                        <button type="button" id="signupVerifyBtn" class="btn btn-success w-100" onclick="verifyCodeSend();">확인</button>
                                    </div>
                                </div>
                            </div>
                            <!-- end card body -->
                        </div>
                        <!-- end card -->

                        <div class="mt-4 text-center">
                            <p class="mb-0">인증 코드를 못받으셨나요 ? <a href="javascript:void(0)" class="fw-semibold text-primary text-decoration-underline" onclick="verifyReSend();">재전송</a> </p>
                        </div>

                    </div>
                </div>
                <!-- end row -->
            </div>
            <!-- end container -->
        </div>
        <!-- end auth page content -->
    </div>
    <!-- end auth-page-wrapper -->

    <%@ include file="../common/layout/custom.jsp" %>
    <%@ include file="../common/script/script_footer.jsp" %>

    <!-- particles js -->
    <script src="/assets/libs/particles.js/particles.js"></script>
    <!-- particles app js -->
    <script src="/assets/js/pages/particles.app.js"></script>
    <!-- two-step-verification js -->
    <script src="/assets/js/pages/two-step-verification.init.js"></script>

    <script src="/js/auth/signupVerify.js"></script>
</body>

</html>