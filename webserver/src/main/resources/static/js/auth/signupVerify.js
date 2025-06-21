let isSubmit = false;

document.addEventListener("DOMContentLoaded", function () {

    /* console.log("만료 시간:", verifyExpAt);
    console.log("사용자 아이디:", memberId);
    console.log("인증 토큰:", verifyToken);*/

    let expirationTime = verifyExpAt; // 서버에서 전달된 만료 시간 (밀리초)
    let currentTime = new Date().getTime(); // 현재 클라이언트 시간 (밀리초)
    let remainingTime = expirationTime - currentTime; // 남은 시간 계산

    function verifyCheck() {
        if (remainingTime <= 0) {
            document.getElementById("countdownTimer").innerHTML = "인증 시간이 만료되었습니다\n 회원가입 신청을 다시해주세요";
            setTimeout(function () {
                window.location.href = "/auth/login"; // 만료 후 자동 리디렉션 (선택)
                //moveMessage("success", response.message, response.addMessage, "/auth/login");

            }, 2000);
            return;
        }

        let seconds = Math.floor((remainingTime / 1000) % 60);
        let minutes = Math.floor((remainingTime / (1000 * 60)) % 60);
        let hours = Math.floor((remainingTime / (1000 * 60 * 60)) % 24);

        let formattedSeconds, formattedMinutes, formattedHours;

        if (hours > 0) {
            formattedHours = String(hours);
            formattedMinutes = String(minutes).padStart(2, '0');
            formattedSeconds = String(seconds).padStart(2, '0');
        } else if (minutes > 0) {
            formattedHours = "";
            formattedMinutes = String(minutes);
            formattedSeconds = String(seconds).padStart(2, '0');
        } else {
            formattedHours = "";
            formattedMinutes = "";
            formattedSeconds = String(seconds);
        }

        document.getElementById("countdownTimer").innerHTML =
            "남은 시간: " +
            (hours > 0 ? formattedHours + "시간 " : "") +
            (minutes > 0 ? formattedMinutes + "분 " : "") +
            formattedSeconds + "초";

        remainingTime -= 1000;
        setTimeout(verifyCheck, 1000);
    }

    verifyCheck();
});

function verifyReSend() {
    const formData = {
        verifyToken: verifyToken
    };

    apiRequest("/auth/signupVerifyResend", "POST", formData)
        .then(response =>
            {
                logMessage("OK", response);

                if(response.status === "OK") {
                    moveMessage("SUCCESS", response.message, response.addMessage, "/auth/signupVerify?verifyToken="+response.data.verifyToken);
                } else {
                    showMessage("WARNING", response.message, response.addMessage);
                }
            }
        )
        .catch(error =>
            {
                logMessage("ERROR", error);
                showMessage("ERROR", "오류가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요");
            }
        )
}

/* 폼 비활성화 */
function submitDisabled(disabled) {
    const btn = document.getElementById("signupVerifyBtn");
    btn.disabled = disabled;
    isSubmit = disabled;
}

function verifyCodeSend() {

    let verifyCodeFrm = document.forms["verifyCodeFrm"].elements["verifyCode"];

    let isFilled = Array.from(verifyCodeFrm).every(input => input.value.trim() !== "");

    if(!isFilled) {
        showMessage("WARNING", "인증 코드를 모두 입력해주세요", "");
        return;
    }

    let verifyCode = "";

    verifyCodeFrm.forEach(function (item) {
        logMessage(item.value);
        verifyCode += item.value;
    });

    const formData = {
        verifyToken: verifyToken,
        verifyCode: verifyCode
    };

    if(!isSubmit) {
        submitDisabled(true);

        apiRequest("/auth/signupVerifyProc", "POST", formData)
            .then(response => {
                logMessage("OK", response);

                if(response.status === "OK") {
                    moveMessage("SUCCESS", response.message, response.addMessage, "/auth/login");
                } else {
                    showMessage("WARNING", response.message, response.addMessage);
                }
            })
            .catch(error => {
                logMessage("ERROR", error);
                showMessage("ERROR", "오류가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요");
            }).finally(() => {
                submitDisabled(false);
            })
    }
}