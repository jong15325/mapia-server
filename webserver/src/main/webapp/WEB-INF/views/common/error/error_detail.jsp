<%@ page contentType="text/html;charset=UTF-8" %>

<!doctype html>
<html lang="ko" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">

<head>
    <meta charset="utf-8" />
    <title>404</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- App favicon -->
    <link rel="shortcut icon" href="/assets/images/favicon.ico">

    <%@ include file="../../common/script/script_header.jsp" %>

</head>

<body>

<!-- auth-page wrapper -->
<div class="auth-page-wrapper auth-bg-cover py-5 d-flex justify-content-center align-items-center min-vh-100">
    <div class="bg-overlay"></div>
    <!-- auth-page content -->
    <div class="auth-page-content overflow-hidden pt-lg-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-xl-5">
                    <div class="card overflow-hidden card-bg-fill galaxy-border-none">
                        <div class="card-body p-4">
                            <div class="text-center">
                                <%--<lord-icon class="avatar-xl" src="https://cdn.lordicon.com/etwtznjn.json" trigger="loop" colors="primary:#405189,secondary:#0ab39c"></lord-icon>--%>
                                <p class="text-muted mb-4">${errors}</p>
                                <a href="/" class="btn btn-success"><i class="mdi mdi-home me-1"></i> 홈으로</a>
                                <a href="javascript:void(0);" class="btn btn-success" onclick="historyBack();"><i class="mdi mdi-keyboard-backspace me-1"></i> 이전 페이지로</a>
                            </div>
                        </div>
                    </div>
                    <!-- end card -->
                </div>
                <!-- end col -->

            </div>
            <!-- end row -->
        </div>
        <!-- end container -->
    </div>
    <!-- end auth page content -->
</div>
<!-- end auth-page-wrapper -->

<%@ include file="../../common/script/script_footer.jsp" %>

</body>

</html>