/**
 * 파일 관련 공통 기능 모듈
 */
const FileUtil = (function() {

    const fileTypes = {
        image: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg'],
        pdf: ['pdf'],
        word: ['doc', 'docx'],
        excel: ['xls', 'xlsx'],
        powerpoint: ['ppt', 'pptx'],
        archive: ['zip', 'rar', '7z', 'tar', 'gz'],
        audio: ['mp3', 'wav', 'ogg', 'flac', 'aac'],
        video: ['mp4', 'avi', 'mov', 'wmv', 'mkv', 'flv', 'webm'],
        code: ['js', 'html', 'css', 'java', 'py', 'php', 'xml', 'json', 'sql']
    };

    const iconClasses = {
        image: 'ri-image-line',
        pdf: 'ri-file-pdf-line',
        word: 'ri-file-word-line',
        excel: 'ri-file-excel-line',
        powerpoint: 'ri-file-ppt-line',
        archive: 'ri-archive-line',
        audio: 'ri-file-music-line',
        video: 'ri-video-line',
        code: 'ri-code-line',
        default: 'ri-file-text-line'
    };

    const bgColorClasses = {
        image: 'bg-soft-success text-success',
        pdf: 'bg-soft-danger text-danger',
        word: 'bg-soft-info text-info',
        excel: 'bg-soft-success text-success',
        powerpoint: 'bg-soft-warning text-warning',
        archive: 'bg-soft-primary text-primary',
        audio: 'bg-soft-info text-info',
        video: 'bg-soft-warning text-warning',
        code: 'bg-soft-secondary text-secondary',
        default: 'bg-soft-primary text-primary'
    };

    function getFileType(extension) {
        for (const type in fileTypes) {
            if (fileTypes[type].includes(extension)) {
                return type;
            }
        }
        return 'default';
    }

    function getIconClass(fileType) {
        return iconClasses[fileType] || iconClasses.default;
    }

    function getBgColorClass(fileType) {
        return bgColorClasses[fileType] || bgColorClasses.default;
    }

    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';

        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];

        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
    }

    //파일 요소에 아이콘 적용
    function applyFileIcon(element, extension) {
        const fileType = getFileType(extension.toLowerCase());
        const iconClass = getIconClass(fileType);
        const bgColorClass = getBgColorClass(fileType);

        // 기존 클래스 제거 및 새 배경색 클래스 추가
        element.classList.remove('bg-light', 'text-secondary');
        bgColorClass.split(' ').forEach(cls => element.classList.add(cls));

        // 아이콘 요소 업데이트
        const iconElement = element.querySelector('i') || document.createElement('i');
        iconElement.className = iconClass;

        if (!element.contains(iconElement)) {
            element.appendChild(iconElement);
        }
    }

    return {
        getFileType,
        getIconClass,
        getBgColorClass,
        formatFileSize,
        applyFileIcon,
        fileTypes
    };
})();