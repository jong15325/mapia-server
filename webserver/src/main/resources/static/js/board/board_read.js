let isSubmit = false; // 중복 제출 여부 확인 변수
let isReplySubmit = false; // 중복 제출 여부 확인 변수

document.addEventListener('DOMContentLoaded', function() {
    // 파일 아이콘 설정
    initializeFileIcons();

    // 댓글 등록
    initCmtForm();

});

function downloadFile(fileKey) {
    window.location.href = "/file/download/" + fileKey;
}

function initializeFileIcons() {
    const fileIcons = document.querySelectorAll('.file-icon');

    fileIcons.forEach(function(iconElement) {
        const extension = iconElement.getAttribute('data-extension').toLowerCase();

        FileUtil.applyFileIcon(iconElement, extension);
    });

    const fileSizeElements = document.querySelectorAll('.file-size');

    fileSizeElements.forEach(function(element) {
        const sizeInBytes = parseInt(element.getAttribute('data-size'));
        element.textContent = FileUtil.formatFileSize(sizeInBytes);
    });
}

function initCmtForm() {
    const submitBtn = document.querySelector('#cmtRegBtn');
    if (submitBtn) {
        submitBtn.addEventListener('click', formCheck);
    }
}

function cmtContCheck() {
    const cmtContEl = document.getElementById('cmtCont');
    let cmtCont = cmtContEl.value.trim();

    if (InputValidator.isEmpty(cmtCont)) {
        showMessage("WARNING", "댓글 내용을 입력해주세요", "", focusClose(cmtContEl));
        return false;
    }

    return true;
}

function replyContCheck() {
    const replyContEl = document.getElementById('replyCont');
    let replyCont = replyContEl.value.trim();

    if (InputValidator.isEmpty(replyCont)) {
        showMessage("WARNING", "댓글 내용을 입력해주세요", "", focusClose(replyContEl));
        return false;
    }

    return true;
}

/* 폼 비활성화 */
function submitDisabled(disabled) {
    const btn = document.querySelector('#cmtRegBtn');
    btn.disabled = disabled;
    isSubmit = disabled;
}

function formCheck() {

    if(!isSubmit) {
        submitDisabled(true);

        const validFunctions = [
            { validate: cmtContCheck, element: document.getElementById("cmtCont") },
        ];

        for (let item of validFunctions) {
            if (!item.validate()) {
                submitDisabled(false);
                return false;
            }
        }

        // FormData 생성
        const form = document.getElementById('cmtForm');
        const formData = new FormData(form);

        // AJAX 요청
        apiRequest('/board/cmt/createProc', "POST", formData)
            .then(response =>
            {
                logMessage("OK", response);
                addNewCmt(response.data);

            })
            .catch(error =>
            {
                logMessage("ERROR", error);
                showMessage("WARNING", "댓글 등록 중 오류가 발생했습니다.", "문제가 지속되면 관리자에게 문의해주세요");
                submitDisabled(false);
            })
            .finally(() => {
                submitDisabled(false);
            });
        }
}

function addNewCmt(cmt) {

    const textarea = document.getElementById('cmtCont');
    if (textarea)
        textarea.value = '';

    cancelReply();

    const cmtArea = document.getElementById('cmtArea');

    const noCmtMsg = cmtArea.querySelector('#noCmtMsg');
    if (noCmtMsg)
        noCmtMsg.remove();

    const rootCmtIdv = document.createElement('div');
    rootCmtIdv.id = 'root_cmt_' + cmt.boardCmtIdx;
    rootCmtIdv.className = 'comment-group mb-4';

    const cmtDiv = document.createElement('div');
    cmtDiv.id = 'cmt_' + cmt.boardCmtIdx;
    cmtDiv.className = 'cmt d-flex mb-4';

    const profilePath = cmt.memberProfilePath || '/img/user/default_profile.png';
    const formattedDate = TimeUtil.getSmartDateFormat(cmt.createdAt, 'HH:mm', 'yyyy-MM-dd HH:mm');

    cmtDiv.innerHTML = `
        <div class="flex-shrink-0">
            <img src="${profilePath}" alt="" class="avatar-sm rounded-circle material-shadow" />
        </div>
        <div class="flex-grow-1 ms-3">
            <h5 class="fs-13">${cmt.createdId} <small class="text-muted ms-2">${formattedDate}</small></h5>
            <p class="text-muted">${TextUtil.getSafeHTML(cmt.boardCmtCont)}</p>
            <a href="javascript:void(0);" class="badge text-muted bg-light" onclick="addReplyForm('${cmt.boardCmtIdx}', this)">
                <i class="mdi mdi-reply"></i> 답글
            </a>
        </div>
    `;

    rootCmtIdv.append(cmtDiv);
    cmtArea.append(rootCmtIdv);
}

function addReplyForm(cmtIdx) {

    const extReplyArea = document.querySelector('.replyArea');
    if (extReplyArea)
        extReplyArea.remove();

    const cmtContainer = document.getElementById('cmt_'+cmtIdx);

    const replyDiv = document.createElement('div');
    replyDiv.className = 'replyArea';

    replyDiv.innerHTML = `
        <form id="replyForm" class="mt-4">
            <input type="hidden" id="parentCmtIdx" name="parentCmtIdx" value="${cmtIdx}">
            <div class="row g-3">
                <div class="col-12">
                    <label for="replyCont"></label>
                    <textarea class="form-control bg-light border-light" 
                    id="replyCont" name="replyCont" rows="3"
                    placeholder="댓글을 남겨보세요" style="resize: none;" 
                    onInput="autoResizeTextarea(this)"
                    onPaste="setTimeout(() => autoResizeTextarea(this), 0)"></textarea>
                </div>
                <div class="col-12 text-end">
                    <a href="javascript:void(0);" class="btn btn-danger" onclick="cancelReply()">취소</a>
                    <a href="javascript:void(0);" class="replyRegBtn btn btn-success" onclick="addReply('${cmtIdx}')">등록</a>
                </div>
            </div>
        </form>
    `;

    cmtContainer.insertAdjacentElement('afterend', replyDiv);
    const replyCont = document.getElementById('replyCont');
    replyCont.focus();
}

function addNewReply(reply) {
    cancelReply();

    const cmtArea = document.getElementById('root_cmt_' + reply.rootCmtIdx);

    const replyDiv = document.createElement('div');
    replyDiv.id = 'cmt_' + reply.boardCmtIdx;
    replyDiv.className = 'cmt d-flex mb-4 ms-5';

    const profilePath = reply.memberProfilePath || '/img/user/default_profile.png';
    const formattedDate = TimeUtil.getSmartDateFormat(reply.createdAt, 'HH:mm', 'yyyy-MM-dd HH:mm');

    replyDiv.innerHTML = `
        <div class="flex-shrink-0">
            <img src="${profilePath}" alt="" class="avatar-sm rounded-circle material-shadow" />
        </div>
        <div class="flex-grow-1 ms-3">
            <h5 class="fs-13">${reply.createdId} <small class="text-muted ms-2">${formattedDate}</small></h5>
            <p class="text-muted">
                <div class="d-flex align-items-start gap-2 flex-wrap">
                    <div class="dropdown">
                        <a class="fw-semibold text-info dropdown-btn link-body-emphasis" href="javascript:void(0);" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${reply.boardCmtToId} </a>
                        <div class="dropdown-menu dropdown-menu-start">
                            <a class="dropdown-item" href="#">게시글보기</a>
                            <a class="dropdown-item" href="#">1:1 채팅</a>
                            <a class="dropdown-item" href="#">쪽지보내기</a>
                        </div>
                    </div>
                    <div class="flex-grow-1">
                        ${TextUtil.getSafeHTML(reply.boardCmtCont)}
                    </div>
                </div>
            </p>
            <a href="javascript:void(0);" class="badge text-muted bg-light" onclick="addReplyForm('${reply.boardCmtIdx}')">
                <i class="mdi mdi-reply"></i> 답글
            </a>
        </div>
    `;

    cmtArea.append(replyDiv);
}

function cancelReply() {
    const replyArea = document.querySelector('.replyArea');
    if(replyArea)
        replyArea.remove();
}

function addReply(cmtIdx) {
    replyFormCheck(cmtIdx);
}

function replyFormCheck(cmtIdx) {
    if(!isReplySubmit) {

        const validFunctions = [
            { validate: replyContCheck, element: document.getElementById("replyCont") },
        ];

        for (let item of validFunctions) {
            if (!item.validate()) {
                return false;
            }
        }

        // FormData 생성
        const form = document.getElementById('replyForm');
        const formData = new FormData(form);
        formData.append('boardCmtCont', formData.get('replyCont'));

        // AJAX 요청
        apiRequest('/board/reply/createProc', "POST", formData)
            .then(response =>
            {
                logMessage("OK", response);
                addNewReply(response.data);
            })
            .catch(error =>
            {
                logMessage("ERROR", error);
                showMessage("WARNING", "댓글 등록 중 오류가 발생했습니다.", "문제가 지속되면 관리자에게 문의해주세요");
                submitDisabled(false);
            })
            .finally(() => {
                submitDisabled(false);
            });
    }
}


function autoResizeTextarea(textarea) {
    textarea.style.height = 'auto';

    const minHeight = 60;
    const maxHeight = 200;

    let newHeight = Math.max(minHeight, textarea.scrollHeight);
    newHeight = Math.min(newHeight, maxHeight);

    // 높이 적용
    textarea.style.height = newHeight + 'px';

    // 최대 높이 초과시 스크롤 표시
    if (textarea.scrollHeight > maxHeight) {
        textarea.style.overflowY = 'auto';
    } else {
        textarea.style.overflowY = 'hidden';
    }
}