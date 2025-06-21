<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">
<head>

    <%@ include file="../common/layout/meta.jsp" %>
    <%@ include file="../common/script/script_header.jsp" %>
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
                            <%-- <p class="mt-3 fs-24 fw-bold">M A P I A</p>--%>
                        </div>
                    </div>
                </div>
                <!-- end row -->

                <div class="row justify-content-center">
                    <div class="col-md-8 col-lg-7 col-xl-6">
                        <div class="card mt-4 card-bg-fill">

                            <div class="card-body p-4">
                                <div class="text-center mt-2">
                                    <h5 class="text-primary fs-24">M A P I A</h5>
                                    <p class="text-muted">로그인</p>
                                </div>
                                <div class="p-2 mt-4">
                                    <form class="needs-validation mb-4" novalidate action="/auth/login" method="POST" onsubmit="return formCheck(event)">
                                        <!-- spring security 제공하는 CSRF 토큰을 자동으로 JSP에 포함 -->
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                                        <div class="mb-3 form-floating">
                                            <input type="email" class="form-control" id="emailId" name="memberId" value="${memberId}" placeholder="이메일" required>
                                            <label class="form-label" for="emailId">이메일</label>
                                            <div class="invalid-feedback">
                                                이메일을 입력해주세요
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <div class="position-relative auth-pass-inputgroup form-floating mb-3">
                                                <input type="password" class="form-control pe-5 password-input" id="password-input" name="memberPwd" placeholder="비밀번호" required>
                                                <button class="btn btn-link position-absolute end-0 top-0 text-decoration-none text-muted password-addon material-shadow-none mt-2 me-3" type="button"
                                                        id="password-addon"><i class="ri-eye-fill align-middle"></i></button>
                                                <label class="form-label" for="password-input">비밀번호</label>
                                                <div class="invalid-feedback">
                                                    비밀번호를 입력해주세요
                                                </div>
                                            </div>
                                        </div>

                                        <div class="d-flex justify-content-between align-items-center">
                                          <%--  <div class="form-check-success">
                                                <input class="form-check-input" type="checkbox" id="auth-remember-check">
                                                <label class="" for="auth-remember-check">자동로그인</label>
                                            </div>

                                            <div>
                                                <a href="/" class="text-muted">비밀번호 찾기</a>
                                            </div>--%>
                                        </div>

                                        <div class="mt-4">
                                            <button class="btn btn-success w-100" type="submit" id="loginBtn">로그인</button>
                                        </div>

                                        <div class="mt-4 text-center">
                                            <div class="signin-other-title">
                                                <h5 class="fs-13 mb-4 title">간편 로그인</h5>
                                            </div>
                                            <div class="row align-items-center">
                                                <div class="col-sm-4 col-md-6">
                                                    <a class="btn btn-sm d-block w-100" href="/oauth2/authorization/naver">
                                                        <img src="/img/button/login/naver_btnG.png" alt="네이버 로그인" style="height: 40px;width:150px;">
                                                    </a>
                                                </div>
                                                <div class="col-sm-4 col-md-6">
                                                    <a class="btn btn-sm d-block w-100" href="/oauth2/authorization/kakao">
                                                        <img src="/img/button/login/kakao_btnG.png" alt="카카오 로그인" style="height: 40px;width:150px;">
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="mt-4 text-center">
                                        <p class="mb-0">아이디가 없으신가요? <a href="/auth/signup" class="fw-semibold text-primary text-decoration-underline"> 회원가입하기 </a></p>
                                    </div>
                                </div>
                            </div>
                            <!-- end card body -->
                        </div>
                        <!-- end card -->
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
    <!-- password-addon init -->
    <script src="/assets/js/pages/password-addon.init.js"></script>
    <!-- validation init -->
    <script src="/assets/js/pages/form-validation.init.js"></script>

    <script src="/js/auth/login.js"></script>
</body>

</html>