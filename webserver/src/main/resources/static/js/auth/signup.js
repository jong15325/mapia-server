let isSubmit = false; // 중복 제출 여부 확인 변수

document.addEventListener("DOMContentLoaded", function() {
    // memberId가 존재하고 이메일 형식(@을 포함)이면 처리
    if(memberId && memberId.includes('@')) {
        // 이메일을 @ 기준으로 분리
        const [emailId, emailDomain] = memberId.split('@');

        // 아이디 부분 설정
        const emailIdField = document.getElementById("emailId");
        if(emailIdField) {
            emailIdField.value = emailId;
        }

        // 도메인 부분 설정
        const emailDomainField = document.getElementById("emailDomain");
        if(emailDomainField) {
            // select 옵션 중에서 일치하는 도메인 찾기
            const options = emailDomainField.options;
            let domainFound = false;

            for(let i = 0; i < options.length; i++) {
                if(options[i].value === emailDomain) {
                    emailDomainField.selectedIndex = i;
                    domainFound = true;
                    break;
                }
            }
        }
    }
});

/* 이메일 검증*/
function emailCheck() {
    let emailIdEl = document.getElementById("emailId");
    let emailId = emailIdEl.value.trim();
    let emailDomainEl = document.getElementById("emailDomain");
    let emailDomain = emailDomainEl.value.trim();

    // 이메일 아이디 검증
    if (!EmailValidator.isValidEmailId(emailId)) {
        showMessage("WARNING", "이메일 아이디는 영문과 숫자만 입력 가능합니다", "", focusClose(emailIdEl));
        return false;
    }

    // 이메일 도메인 검증
    if (!EmailValidator.isValidEmailDomain(emailDomain)) {
        showMessage("WARNING", "잘못된 이메일 도메인입니다", "", focusClose(emailDomainEl));
        return false;
    }

    // 전체 검증
    if (!EmailValidator.getFullEmail(emailId, emailDomain)) {
        showMessage("WARNING", "잘못된 이메일 아이디입니다", "", focusClose(emailId));
        return false;
    }

    return true;
}

/* 비밀번호 검증 */
function passwordCheck() {
    //const = 요소 그 자체, 절대 불변
    //let = 재선언 불가능, 재할당 가능
    //var = 재선언 가능, 재할당 가능 -> 지양
    //하단에 요소 자체를 불러올 시 const 선언하면 좋지만 .value를 가져오고있어 let으로 선언해야함
    let passwordEl = document.getElementById("password-input");
    let password = passwordEl.value;

    // 비밀번호 검증
    if (!PasswordValidator.isValidPassword(password)) {
        showMessage("WARNING", "비밀번호는 최소 8자리 이상이며, 영문과 숫자를 조합해야 합니다", "", focusClose(passwordEl));
        return false;
    }

    return true; // 모든 검증 통과 시 폼 제출 허용
}

function ConfirmPasswordCheck() {
    let password = document.getElementById("password-input").value;
    let confirmPasswordEl = document.getElementById("confirm-password-input");
    let confirmPassword = confirmPasswordEl.value;

    // 비밀번호 확인 체크
    if (!PasswordValidator.isPasswordMatch(password, confirmPassword)) {
        showMessage("WARNING", "입력된 비밀번호가 일치하지 않습니다", "", focusClose(confirmPasswordEl));
        return false;
    }

    return true;
}

/* 약관 동의 검증*/
function termsCheck() {
    let termsCheckbox = document.getElementById("termsCheck");
    if (!termsCheckbox.checked) {
        showMessage("WARNING", "약관에 동의해야 회원가입이 가능합니다", "", focusClose(termsCheckbox));
        return false;
    }
    return true;
}

/* 폼 비활성화 */
function submitDisabled(disabled) {
    const btn = document.getElementById("signupBtn");
    btn.disabled = disabled;
    isSubmit = disabled;
}

/* 전체 폼 검증*/
function formCheck(event) {
    if(!isSubmit) {
        submitDisabled(true);

        const validFunctions = [
            { validate: emailCheck, element: document.getElementById("emailId") },
            { validate: passwordCheck, element: document.getElementById("password-input") },
            { validate: ConfirmPasswordCheck, element: document.getElementById("confirm-password-input") },
            { validate: termsCheck, element: document.getElementById("termsCheck") }
        ];
        for (let item of validFunctions) {
            if (!item.validate()) {
                submitDisabled(false);

                return false;
            }
        }


        const formData = {
            memberId:
                EmailValidator.getFullEmail(
                    document.getElementById("emailId").value.trim(),
                    document.getElementById("emailDomain").value,
                ),

            memberPwd: document.getElementById("password-input").value
        };

        apiRequest("/auth/signupProc", "POST", formData)
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
                    showMessage("WARNING", "오류가 발생했습니다", "문제가 지속되면 관리자에게 문의해주세요");
                }
            ).finally(()=>{
                submitDisabled(false);
            })


        return false;
    }
}