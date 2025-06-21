let isSubmit = false; // 중복 제출 여부 확인 변수

/* 이메일 검증*/
function emailCheck() {
    let emailIdEl = document.getElementById("emailId");
    let emailId = emailIdEl.value.trim();

    //입력값 검증
    if (InputValidator.isEmpty(emailId)) {
        showMessage("WARNING", "이메일을 입력해주세요", "", focusClose(emailIdEl));
        return false;
    }

    // 전체 검증
    if (!EmailValidator.isValidFullEmailAddress(emailId)) {
        showMessage("WARNING", "잘못된 이메일 아이디입니다", "", focusClose(emailIdEl));
        return false;
    }

    return true;
}

/* 비밀번호 검증 */
function passwordCheck() {
    let passwordEl = document.getElementById("password-input");
    let password = passwordEl.value;

    //입력값 검증
    if (InputValidator.isEmpty(password)) {
        showMessage("WARNING", "비밀번호를 입력해주세요", "", focusClose(passwordEl));
        return false;
    }

    return true;
}

/* 폼 비활성화 */
function submitDisabled(disabled) {
    const btn = document.getElementById("loginBtn");
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
        ];

        for (let item of validFunctions) {
            if (!item.validate()) {
                submitDisabled(false);

                return false;
            }
        }

        return true;
    }
}