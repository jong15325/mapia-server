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
                                    <p class="text-muted">회원가입</p>
                                </div>
                                <div class="p-2 mt-4">
                                    <form class="needs-validation mb-4" novalidate method="POST" onsubmit="return formCheck(event)">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                                        <div class="row mb-3">
                                            <label for="emailId" class="form-label">아이디(이메일) <span class="text-danger">*</span></label>
                                            <div class="col-lg-5 mb-3">
                                                <input type="text" class="form-control" id="emailId" placeholder="이메일 아이디를 입력해주세요"
                                                       oninput="isAlphaNumeric(this)" required>
                                                <div class="invalid-feedback">
                                                    이메일을 아이디를 입력해주세요
                                                </div>
                                            </div>
                                            <div class="col-lg-7">
                                                <div class="input-group">
                                                    <div class="input-group-text">@</div>
                                                    <select class="form-select" id="emailDomain" aria-label="Default select example" required>
                                                        <option value="" selected>이메일 도메인을 선택해주세요</option>
                                                        <c:forEach var="enumEmail" items="${enumEmail}">
                                                            <option value="${enumEmail.domain}">${enumEmail.domain}</option>
                                                        </c:forEach>
                                                    </select>
                                                    <div class="invalid-feedback">
                                                        도메인을 선택해주세요
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label" for="password-input">비밀번호</label>
                                            <div class="position-relative auth-pass-inputgroup">
                                                <input type="password" class="form-control pe-5 password-input" onpaste="return false" placeholder="비밀번호를 입력해주세요" id="password-input" name="memberPwd" aria-describedby="passwordInput" required>
                                                <button class="btn btn-link position-absolute end-0 top-0 text-decoration-none text-muted password-addon material-shadow-none me-3" type="button" id="password-addon"><i class="ri-eye-fill align-middle"></i></button>
                                                <div class="invalid-feedback">
                                                    비밀번호를 입력해주세요
                                                </div>
                                            </div>
                                        </div>

                                        <div id="password-contain" class="p-3 bg-light mb-2 rounded">
                                            <h5 class="fs-13">Password must contain:</h5>
                                            <p id="pass-eng" class="invalid fs-12 mb-2"><b>영문</b> 대/소문자 포함</p>
                                            <p id="pass-number" class="invalid fs-12 mb-2"><b>숫자</b> 포함</p>
                                            <p id="pass-length" class="invalid fs-12 mb-2"><b>최소 8자</b> 이상</p>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label" for="confirm-password-input">비밀번호 확인</label>
                                            <div class="position-relative auth-pass-inputgroup">
                                                <input type="password" class="form-control pe-5 password-input" onpaste="return false" placeholder="비밀번호를 한번 더 입력해주세요" id="confirm-password-input" aria-describedby="confirmPasswordInput" required>
                                                <button class="btn btn-link position-absolute end-0 top-0 text-decoration-none text-muted password-addon material-shadow-none me-3" type="button" id="password-addon2"><i class="ri-eye-fill align-middle"></i></button>
                                                <div class="invalid-feedback">
                                                    비밀번호를 한번 더 입력해주세요
                                                </div>
                                            </div>
                                        </div>

                                        <div>
                                            <div class="accordion" id="termsContent">
                                                <div class="accordion-item material-shadow">
                                                    <div class="form-check-success accordion-button collapsed" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne" id="headingOne">
                                                        <input class="form-check-input me-2" type="checkbox" value="" id="termsCheck" required>
                                                        <label for="termsCheck" class="mb-0 flex-grow-1">이용약관 및 개인정보처리방침 동의</label>
                                                    </div>
                                                    <div id="collapseOne" class="accordion-collapse collapse" aria-labelledby="headingOne" data-bs-parent="#termsContent">
                                                        <div class="accordion-body p-3 bg-light rounded" style="max-height: 200px; overflow-y: auto;">
                                                            <h5 class="text-primary"> 이용약관</h5>
                                                            <p class="small">
                                                                본 약관은 회원 가입 및 서비스 이용과 관련하여, 회사와 이용자의 권리 및 의무 사항을 규정합니다.<br>
                                                                1. 회원은 본 약관에 동의한 후 서비스를 이용할 수 있습니다.<br>
                                                                2. 회원은 가입 시 허위 정보를 제공해서는 안 됩니다.<br>
                                                                3. 회사는 서비스의 원활한 운영을 위해 필요한 경우 사전 고지 후 서비스를 변경할 수 있습니다.<br>
                                                                4. 회원은 본 약관을 준수해야 하며, 서비스 이용 중 불법적인 행위를 해서는 안 됩니다.
                                                            </p>

                                                            <h5 class="text-primary mt-3"> 개인정보처리방침</h5>
                                                            <p class="small">
                                                                본 개인정보처리방침은 이용자의 개인정보 보호를 위해 적용됩니다.<br>
                                                                1. 수집하는 개인정보: 이메일, 비밀번호, 이름 등 회원가입 시 입력한 정보.<br>
                                                                2. 개인정보 이용 목적: 서비스 제공 및 고객 지원.<br>
                                                                3. 개인정보 보관 기간: 회원 탈퇴 후 30일 이내 삭제.<br>
                                                                4. 개인정보 보호 정책에 대한 자세한 사항은 고객센터를 통해 확인할 수 있습니다.
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mt-4">
                                            <button class="btn btn-success w-100" type="submit" id="signupBtn">가입</button>
                                        </div>

                                        <div class="mt-4 text-center">
                                            <div class="signin-other-title">
                                                <h5 class="fs-13 mb-4 title">간편 회원가입</h5>
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
                                        <p class="mb-0">아이디가 있으신가요 ? <a href="/auth/login" class="fw-semibold text-primary text-decoration-underline"> 로그인하기 </a> </p>
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
    <!-- validation init -->
    <script src="/assets/js/pages/form-validation.init.js"></script>
    <!-- password create init -->
    <script src="/assets/js/pages/passowrd-create.init.js"></script>

    <script src="/js/auth/signup.js"></script>
</body>

</html>