/* 페이지 이동*/
function movePage(pageNum) {
    document.getElementById("pageNum").value = pageNum;  // 페이지 번호 세팅
    document.getElementById("schFrm").submit();          // 폼 제출
}

/* 이전 페이지로 이동 */
function historyBack() {
    if (window.history.length > 1) {
        history.back();
    } else {
        window.location.href = "/";  // 이전 페이지가 없으면 홈으로 이동
    }
}

/* 폼 POST 변경 제출 */
function changePostSubmit(url) {
    const frm = document.getElementById("schFrm");
    frm.action = url;  // 폼의 액션을 변경
    frm.method = "POST";
    frm.submit();      // 폼 제출
}