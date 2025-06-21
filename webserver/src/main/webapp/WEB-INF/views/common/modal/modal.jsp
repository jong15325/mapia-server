<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Scrollable Modal -->
<div class="modal fade" id="termsModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
     role="dialog" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable modal-dialog-centered">
    <div class="modal-content">
      <%-- 모달 헤더 --%>
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
        <%--<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>--%>
      </div>

      <%-- 모달 바디 --%>
      <div class="modal-body">
        <h5 class="fs-16">Tooltips in a Modal</h5>
        <p class="text-muted">You only need to know a little to make a big
          <a href="javascript:void(0);"
             class="popover-test fw-medium text-decoration-underline link-success"
             data-bs-toggle="popover"
             title="Common Types of Fonts"
             data-bs-content="They're a good choice for more traditional projects."
             data-bs-container="body"
             data-bs-placement="bottom"
             data-bs-original-title="Popover Title">
            Popover on Click
          </a>

          you do every day. So let's get started. First, some common types of fonts and what you need to know about them triggers a popover on click.</p>
      </div>

      <%-- 모달 푸터 --%>
      <div class="modal-footer">
        <div class="mx-auto">
          <button type="button" class="btn btn-light" data-bs-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary ">Save Changes</button>
        </div>
      </div>
    </div>
  </div>
</div>