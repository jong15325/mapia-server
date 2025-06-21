/**
 * 텍스트 관련 공통 기능 모듈
 */
const TextUtil = (function() {
    'use strict';

    /**
     * HTML 특수 문자를 이스케이프하고 줄바꿈을 <br> 태그로 변환
     * @param {string} text - 변환할 텍스트
     * @returns {string} 안전하게 변환된 HTML 문자열
     */
    function getSafeHTML(text) {
        if (!text) return '';

        return text
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;')
            .replace(/\n/g, '<br>');
    }

    /**
     * 문자열이 특정 길이를 초과하면 말줄임표로 자르기
     * @param {string} text - 원본 텍스트
     * @param {number} maxLength - 최대 길이
     * @param {string} ellipsis - 말줄임표 문자 (기본값: '...')
     * @returns {string} 잘린 문자열
     */
    function truncateText(text, maxLength, ellipsis = '...') {
        if (!text || text.length <= maxLength) return text;
        return text.substring(0, maxLength) + ellipsis;
    }

    /**
     * URL, 이메일, 전화번호 등을 자동으로 링크로 변환
     * @param {string} text - 원본 텍스트
     * @returns {string} 링크가 포함된 HTML
     */
    function autoLink(text) {
        if (!text) return '';

        // 이미 안전한 HTML로 변환된 텍스트를 가정
        const safeText = text;

        // URL 패턴 변환
        const urlPattern = /https?:\/\/[^\s<]+[^<.,:;"')\]\s]/g;
        const withUrls = safeText.replace(urlPattern, url =>
            `<a href="${url}" target="_blank" rel="noopener noreferrer">${url}</a>`
        );

        // 이메일 패턴 변환
        const emailPattern = /[\w._%+-]+@[\w.-]+\.[a-zA-Z]{2,4}/g;
        return withUrls.replace(emailPattern, email =>
            `<a href="mailto:${email}">${email}</a>`
        );
    }

    /**
     * HTML 태그 제거
     * @param {string} html - HTML이 포함된 문자열
     * @returns {string} HTML 태그가 제거된 일반 텍스트
     */
    function stripTags(html) {
        if (!html) return '';
        return html.replace(/<\/?[^>]+(>|$)/g, '');
    }

    // 공개 API
    return {
        getSafeHTML,
        truncateText,
        autoLink,
        stripTags
    };
})();