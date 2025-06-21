/* 이메일 검증 */
const EmailValidator = {
    isValidEmailId: (emailId) => {
        if (!emailId || typeof emailId !== "string") return false;
        const trimmed = emailId.trim();
        return /^[a-zA-Z0-9]+$/.test(trimmed);
    },

    isValidEmailDomain: (emailDomain) => {
        if (!emailDomain || typeof emailDomain !== "string") return false;
        const trimmed = emailDomain.trim();
        return /^(?!.*\.\.)[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(trimmed);
    },

    isValidFullEmail: (emailId, emailDomain) => {
        return EmailValidator.isValidEmailId(emailId) && EmailValidator.isValidEmailDomain(emailDomain);
    },

    isValidFullEmailAddress: (fullEmail) => {
        if (!fullEmail || typeof fullEmail !== "string") return false;
        const trimmed = fullEmail.trim();
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(trimmed);
    },

    getFullEmail: (emailId, emailDomain) => {
        if (EmailValidator.isValidFullEmail(emailId, emailDomain)) {
            return emailId.trim() + "@" + emailDomain.trim();
        }
        return null;
    }
};

/* 비밀번호 검증 */
const PasswordValidator = {
    isValidPassword : (password) => /^(?=.*[A-Za-z])(?=.*\d).{8,}$/.test(password),
    isPasswordMatch : (password, confirmPassword) => password === confirmPassword,
};

/* 입력값 검증 (null, 빈 값 체크) */
const InputValidator = {
    isEmpty: (value) => {
        return value == null || (typeof value === "string" && value.trim() === "");
    },

    isNotEmpty: (value) => {
        return !InputValidator.isEmpty(value);
    },

    areAllEmpty: (...values) => {
        return values.every(value => InputValidator.isEmpty(value));
    },

    isAnyNotEmpty: (...values) => {
        return values.some(value => InputValidator.isNotEmpty(value));
    }
};

//클로저 패턴?이라고함