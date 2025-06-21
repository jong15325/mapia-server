const AlertConfig = {
    category: {
        INFO: { icon: "info" },
        SUCCESS: { icon: "success" },
        ERROR: { icon: "error" },
        WARNING: { icon: "warning" },
        QUESTION: { icon: "question" },
    },

    default: {
        /* 버튼 기본 스타일 */
        buttonsStyling: true,

        /* 확인 버튼 */
        showConfirmButton: true,
        confirmButtonText: "확인",

        customClass: {
            confirmButton: "btn btn-primary w-xs me-2 mt-2",
            htmlContainer: 'text-muted fs-5',
        },

        /* 닫기 버튼 */
        showCloseButton: false,

        /* 다른 영역 클릭 시 닫기 */
        allowOutsideClick: false,
        backdrop: true,
    },

    confirm: {
        /* 취소 버튼 */
        showCancelButton: true,
        cancelButtonText: "취소",
        cancelButtonColor: "#C1505D",

        customClass: {
            cancelButton: "btn text-light w-xs mt-2",
        },
    },

    input: {
        default: {
            //inputPlaceholder: "여기에 입력하세요...",
            showCancelButton: true,
            confirmButtonText: "확인",
            cancelButtonText: "취소",
        },

        email: {
            icon: "question",
            title: "이메일 입력",
            input: "email",
            inputPlaceholder: "example@example.com",
            confirmButtonText: "확인",
            cancelButtonText: "취소",
        },

        password: {
            icon: "question",
            title: "비밀번호 입력",
            input: "password",
            inputPlaceholder: "비밀번호를 입력하세요",
            confirmButtonText: "확인",
            cancelButtonText: "취소",
        },
    },
};

function showMessage(category="WARNING", title="", content = "", addOptions = {}) {
    const config = AlertConfig.category[category];
    return Swal.fire({
        ...config,
        ...AlertConfig.default,
        title: title ? title : content,
        text: content ? " ※ " + content : "",
        ...addOptions
    });
}

function showConfirm(category="WARNING", title="", content = "", onConfirm, onCancel = () => {}) {
    const config = AlertConfig.category[category];
    Swal.fire({
        ...config,
        ...AlertConfig.default,
        ...AlertConfig.confirm,
        customClass: {
            ...AlertConfig.default.customClass,
            ...AlertConfig.confirm.customClass,
        },
        title: title ? title : content,
        text: content ? " ※ " + content : "",
    }).then((result) => {
        console.log(result);
        if (result.isConfirmed) {
            onConfirm();
        } else {
            onCancel();
        }
    });
}

function backMessage(category="WARNING", title="", content = "", addOptions = {}) {
    const config = AlertConfig.category[category];
    return Swal.fire({
        ...AlertConfig.default,
        ...config,
        title: title ? title : content,
        text: content ? " ※ " + content : "",
        ...addOptions
    }).then(() =>{
        history.back();
    });
}

function closeMessage(category="WARNING", title="", content = "", addOptions = {}) {
    const config = AlertConfig.category[category];
    return Swal.fire({
        ...AlertConfig.default,
        ...config,
        title: title ? title : content,
        text: content ? " ※ " + content : "",
        ...addOptions
    }).then(() =>{
        if (window.opener) {
            window.opener.location.reload(true);
        }

        window.close();
    });
}

function moveMessage(category="WARNING", title="", content = "", move="", addOptions = {}) {
    const config = AlertConfig.category[category];
    return Swal.fire({
        ...AlertConfig.default,
        ...config,
        title: title ? title : content,
        text: content ? " ※ " + content : "",
        ...addOptions,
        willClose: () => {
            if (move) {
                window.location.href = move;
            }
        }
    });
}

function focusClose(element, additionalOptions = {}) {
    return {
        ...additionalOptions,
        didClose: () => {
            if (element) {
                element.focus();
            }
        }
    };
}
