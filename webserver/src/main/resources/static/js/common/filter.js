const InputFilter = {
    isOnlyNumbers: (value) => /^[0-9]+$/.test(value), // 숫자만 허용
    isOnlyLetters: (value) => /^[A-Za-z]+$/.test(value), // 영문자만 허용
    isAlphaNumeric: (value) => /^[A-Za-z0-9]+$/.test(value), // 영문자 + 숫자 허용
    isAlphaNumericWithSpecial: (value) => /^[A-Za-z0-9@$!%*?&]+$/.test(value), // 영문자 + 숫자 + 특수문자 허용

    hasMinLength: (value, minLength) => value.length > minLength,
    hasMaxLength: (value, maxLength) => value.length < maxLength,
    isWithinLength: (value, min, max) => value.length > min && value.length < max,
};

function isOnlyNumbers(obj) {
    obj.value = obj.value.replace(/[^0-9]/g, ""); // 숫자만 허용
}

function isOnlyLetters(obj) {
    obj.value = obj.value.replace(/[^A-Za-z]/g, ""); // 영문만 허용
}

function isAlphaNumeric(obj) {
    obj.value = obj.value.replace(/[^A-Za-z0-9]/g, ""); // 영문 + 숫자만 입력 가능
}

function isAlphaNumericWithSpecial(obj) {
    obj.value = obj.value.replace(/[^A-Za-z0-9@$!%*?&]/g, ""); // 영문자 + 숫자 + 특정 특수문자 허용
}

function hasMaxLength(obj, maxLength) {
    obj.value = obj.value.substring(0, maxLength);
}