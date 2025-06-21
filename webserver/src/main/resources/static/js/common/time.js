const TimeUtil = (function() {

    /**
     * 날짜 오브젝트가 오늘 날짜인지 확인
     * @param {Date} date - 확인할 날짜
     * @returns {boolean} 오늘이면 true, 아니면 false
     */
    function isToday(date) {
        const today = new Date();
        return date.getFullYear() === today.getFullYear() &&
            date.getMonth() === today.getMonth() &&
            date.getDate() === today.getDate();
    }

    /**
     * 날짜 포맷팅 함수
     * @param {Date} date - 포맷할 날짜
     * @param {string} format - 포맷 패턴 (예: HH:mm, yyyy-MM-dd)
     * @returns {string} 포맷된 날짜 문자열
     */
    function formatDate(date, format) {
        const tokens = {
            'yyyy': date.getFullYear(),
            'MM': String(date.getMonth() + 1).padStart(2, '0'),
            'dd': String(date.getDate()).padStart(2, '0'),
            'HH': String(date.getHours()).padStart(2, '0'),
            'mm': String(date.getMinutes()).padStart(2, '0'),
            'ss': String(date.getSeconds()).padStart(2, '0')
        };

        let result = format;
        for (const [token, value] of Object.entries(tokens)) {
            result = result.replace(token, value);
        }
        return result;
    }

    /**
     * 상황에 맞는 스마트 날짜 포맷팅 (오늘이면 시간, 아니면 날짜 표시)
     * @param {Date|string} dateObj - 포맷할 날짜 (Date 객체 또는 날짜 문자열)
     * @param {string} timePattern - 오늘일 경우 사용할 시간 포맷 (기본값: "HH:mm")
     * @param {string} datePattern - 다른 날일 경우 사용할 날짜 포맷 (기본값: "yyyy-MM-dd")
     * @returns {string} 포맷된 날짜/시간 문자열
     */
    function getSmartDateFormat(dateObj, timePattern, datePattern) {
        // 기본값 설정
        timePattern = timePattern || 'HH:mm';
        datePattern = datePattern || 'yyyy-MM-dd';

        // 문자열이면 Date 객체로 변환
        const date = dateObj instanceof Date ? dateObj : new Date(dateObj);

        // 유효하지 않은 날짜면 빈 문자열 반환
        if (isNaN(date.getTime())) {
            return '';
        }

        // 오늘이면 시간 포맷, 아니면 날짜 포맷 사용
        if (isToday(date)) {
            return formatDate(date, timePattern);
        } else {
            return formatDate(date, datePattern);
        }
    }

    // 공개 API
    return {
        getSmartDateFormat
    };
})();