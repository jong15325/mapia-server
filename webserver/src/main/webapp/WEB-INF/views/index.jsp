<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">

<head>

    <%@ include file="./common/layout/meta.jsp" %>
    <%@ include file="./common/script/script_header.jsp" %>

    <!-- jsvectormap css -->
    <link href="/assets/libs/jsvectormap/jsvectormap.min.css" rel="stylesheet" type="text/css"/>

    <!--Swiper slider css-->
    <link href="/assets/libs/swiper/swiper-bundle.min.css" rel="stylesheet" type="text/css"/>
</head>

<body>

<!-- Begin page -->
<div id="layout-wrapper">
    <%@ include file="./common/layout/topbar.jsp" %>
    <%@ include file="./common/layout/sidebar.jsp" %>

    <!-- ============================================================== -->
    <!-- Start right Content here -->
    <!-- ============================================================== -->
    <div class="main-content">

        <div class="page-content">
            <div class="container-fluid">

                <div class="row">
                    <div class="col">

                        <div class="h-100">
                            <div class="row">
                                <div class="col-xl-4">
                                    <div class="card card-height-100">
                                        <div class="card-header align-items-center d-flex">
                                            <h4 class="card-title mb-0 flex-grow-1">Store Visits by Source</h4>
                                            <div class="flex-shrink-0">
                                                <div class="dropdown card-header-dropdown">
                                                    <a class="text-reset dropdown-btn" href="#" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                        <span class="text-muted">Report<i class="mdi mdi-chevron-down ms-1"></i></span>
                                                    </a>
                                                    <div class="dropdown-menu dropdown-menu-end">
                                                        <a class="dropdown-item" href="#">Download Report</a>
                                                        <a class="dropdown-item" href="#">Export</a>
                                                        <a class="dropdown-item" href="#">Import</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div><!-- end card header -->

                                        <div class="card-body">
                                            <div id="store-visits-source" data-colors='["--vz-primary", "--vz-success", "--vz-warning", "--vz-danger", "--vz-info"]'
                                                 data-colors-minimal='["--vz-primary", "--vz-primary-rgb, 0.85", "--vz-primary-rgb, 0.70", "--vz-primary-rgb, 0.60", "--vz-primary-rgb, 0.45"]'
                                                 data-colors-interactive='["--vz-primary", "--vz-primary-rgb, 0.85", "--vz-primary-rgb, 0.70", "--vz-primary-rgb, 0.60", "--vz-primary-rgb, 0.45"]'
                                                 data-colors-galaxy='["--vz-primary", "--vz-primary-rgb, 0.85", "--vz-primary-rgb, 0.70", "--vz-primary-rgb, 0.60", "--vz-primary-rgb, 0.45"]'
                                                 class="apex-charts" dir="ltr"></div>
                                        </div>
                                    </div> <!-- .card-->
                                </div> <!-- .col-->

                                <div class="col-xl-8">
                                    <div class="card">
                                        <div class="card-header align-items-center d-flex">
                                            <h4 class="card-title mb-0 flex-grow-1">Recent Orders</h4>
                                            <div class="flex-shrink-0">
                                                <button type="button" class="btn btn-soft-info btn-sm material-shadow-none">
                                                    <i class="ri-file-list-3-line align-middle"></i> Generate Report
                                                </button>
                                            </div>
                                        </div><!-- end card header -->

                                        <div class="card-body">
                                            <div class="table-responsive table-card">
                                                <table class="table table-borderless table-centered align-middle table-nowrap mb-0">
                                                    <thead class="text-muted table-light">
                                                    <tr>
                                                        <th scope="col">Order ID</th>
                                                        <th scope="col">Customer</th>
                                                        <th scope="col">Product</th>
                                                        <th scope="col">Amount</th>
                                                        <th scope="col">Vendor</th>
                                                        <th scope="col">Status</th>
                                                        <th scope="col">Rating</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <a href="apps-ecommerce-order-details.html" class="fw-medium link-primary">#VZ2112</a>
                                                        </td>
                                                        <td>
                                                            <div class="d-flex align-items-center">
                                                                <div class="flex-shrink-0 me-2">
                                                                    <img src="/assets/images/users/avatar-1.jpg" alt="" class="avatar-xs rounded-circle material-shadow"/>
                                                                </div>
                                                                <div class="flex-grow-1">Alex Smith</div>
                                                            </div>
                                                        </td>
                                                        <td>Clothes</td>
                                                        <td>
                                                            <span class="text-success">$109.00</span>
                                                        </td>
                                                        <td>Zoetic Fashion</td>
                                                        <td>
                                                            <span class="badge bg-success-subtle text-success">Paid</span>
                                                        </td>
                                                        <td>
                                                            <h5 class="fs-14 fw-medium mb-0">5.0<span class="text-muted fs-11 ms-1">(61 votes)</span></h5>
                                                        </td>
                                                    </tr><!-- end tr -->
                                                    <tr>
                                                        <td>
                                                            <a href="apps-ecommerce-order-details.html" class="fw-medium link-primary">#VZ2111</a>
                                                        </td>
                                                        <td>
                                                            <div class="d-flex align-items-center">
                                                                <div class="flex-shrink-0 me-2">
                                                                    <img src="/assets/images/users/avatar-2.jpg" alt="" class="avatar-xs rounded-circle material-shadow"/>
                                                                </div>
                                                                <div class="flex-grow-1">Jansh Brown</div>
                                                            </div>
                                                        </td>
                                                        <td>Kitchen Storage</td>
                                                        <td>
                                                            <span class="text-success">$149.00</span>
                                                        </td>
                                                        <td>Micro Design</td>
                                                        <td>
                                                            <span class="badge bg-warning-subtle text-warning">Pending</span>
                                                        </td>
                                                        <td>
                                                            <h5 class="fs-14 fw-medium mb-0">4.5<span class="text-muted fs-11 ms-1">(61 votes)</span></h5>
                                                        </td>
                                                    </tr><!-- end tr -->
                                                    <tr>
                                                        <td>
                                                            <a href="apps-ecommerce-order-details.html" class="fw-medium link-primary">#VZ2109</a>
                                                        </td>
                                                        <td>
                                                            <div class="d-flex align-items-center">
                                                                <div class="flex-shrink-0 me-2">
                                                                    <img src="/assets/images/users/avatar-3.jpg" alt="" class="avatar-xs rounded-circle material-shadow"/>
                                                                </div>
                                                                <div class="flex-grow-1">Ayaan Bowen</div>
                                                            </div>
                                                        </td>
                                                        <td>Bike Accessories</td>
                                                        <td>
                                                            <span class="text-success">$215.00</span>
                                                        </td>
                                                        <td>Nesta Technologies</td>
                                                        <td>
                                                            <span class="badge bg-success-subtle text-success">Paid</span>
                                                        </td>
                                                        <td>
                                                            <h5 class="fs-14 fw-medium mb-0">4.9<span class="text-muted fs-11 ms-1">(89 votes)</span></h5>
                                                        </td>
                                                    </tr><!-- end tr -->
                                                    <tr>
                                                        <td>
                                                            <a href="apps-ecommerce-order-details.html" class="fw-medium link-primary">#VZ2108</a>
                                                        </td>
                                                        <td>
                                                            <div class="d-flex align-items-center">
                                                                <div class="flex-shrink-0 me-2">
                                                                    <img src="/assets/images/users/avatar-4.jpg" alt="" class="avatar-xs rounded-circle material-shadow"/>
                                                                </div>
                                                                <div class="flex-grow-1">Prezy Mark</div>
                                                            </div>
                                                        </td>
                                                        <td>Furniture</td>
                                                        <td>
                                                            <span class="text-success">$199.00</span>
                                                        </td>
                                                        <td>Syntyce Solutions</td>
                                                        <td>
                                                            <span class="badge bg-danger-subtle text-danger">Unpaid</span>
                                                        </td>
                                                        <td>
                                                            <h5 class="fs-14 fw-medium mb-0">4.3<span class="text-muted fs-11 ms-1">(47 votes)</span></h5>
                                                        </td>
                                                    </tr><!-- end tr -->
                                                    <tr>
                                                        <td>
                                                            <a href="apps-ecommerce-order-details.html" class="fw-medium link-primary">#VZ2107</a>
                                                        </td>
                                                        <td>
                                                            <div class="d-flex align-items-center">
                                                                <div class="flex-shrink-0 me-2">
                                                                    <img src="/assets/images/users/avatar-6.jpg" alt="" class="avatar-xs rounded-circle material-shadow"/>
                                                                </div>
                                                                <div class="flex-grow-1">Vihan Hudda</div>
                                                            </div>
                                                        </td>
                                                        <td>Bags and Wallets</td>
                                                        <td>
                                                            <span class="text-success">$330.00</span>
                                                        </td>
                                                        <td>iTest Factory</td>
                                                        <td>
                                                            <span class="badge bg-success-subtle text-success">Paid</span>
                                                        </td>
                                                        <td>
                                                            <h5 class="fs-14 fw-medium mb-0">4.7<span class="text-muted fs-11 ms-1">(161 votes)</span></h5>
                                                        </td>
                                                    </tr><!-- end tr -->
                                                    </tbody><!-- end tbody -->
                                                </table><!-- end table -->
                                            </div>
                                        </div>
                                    </div> <!-- .card-->
                                </div> <!-- .col-->
                            </div> <!-- end row-->

                        </div> <!-- end .h-100-->

                    </div> <!-- end col -->
                </div>

            </div>
            <!-- container-fluid -->
        </div>
        <!-- End Page-content -->

        <%@ include file="./common/layout/footer.jsp" %>
    </div>
    <!-- end main content-->

</div>
<!-- END layout-wrapper -->

<%--<%@ include file="./common/layout/custom.jsp" %>--%>
<%@ include file="./common/script/script_footer.jsp" %>

<!-- apexcharts -->
<script src="/assets/libs/apexcharts/apexcharts.min.js"></script>

<!-- Vector map-->
<script src="/assets/libs/jsvectormap/jsvectormap.min.js"></script>
<script src="/assets/libs/jsvectormap/maps/world-merc.js"></script>

<!--Swiper slider js-->
<script src="/assets/libs/swiper/swiper-bundle.min.js"></script>

<!-- Dashboard init -->
<script src="/assets/js/pages/dashboard-ecommerce.init.js"></script>

<!-- App js -->
<script src="/assets/js/app.js"></script>
</body>

</html>