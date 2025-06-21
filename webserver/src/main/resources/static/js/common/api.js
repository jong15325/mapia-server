async function apiRequest(url, method = "GET", data = {}, headers = {}) {
    try {
        // CSRF 토큰 가져오기 (JSP의 meta 태그 활용)
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

        // 기본 헤더 설정 (JSON 요청 기본값)
        const defaultHeaders = {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "X-requested-With": "XMLHttpRequest",
        };

        // CSRF 헤더 추가 (토큰이 존재하는 경우)
        if (csrfToken && csrfHeader)
            defaultHeaders[csrfHeader] = csrfToken;

        // 사용자 지정 헤더와 병합
        const finalHeaders = { ...defaultHeaders, ...headers };

        // 요청 옵션 설정
        const options = {
            method,
            headers: finalHeaders,
            credentials: "same-origin", // 세션 쿠키 포함
        };

        // 요청 본문 추가 (GET 요청은 body 없음)
        if (data) {
            if (data instanceof FormData) {
                delete finalHeaders["Content-Type"]; // FormData 사용 시 Content-Type 자동 처리
                options.body = data;
            } else {
                options.body = JSON.stringify(data);
            }
        }

        // Fetch API 요청
        const response = await fetch(url, options);
        // HTTP 응답 코드 검사 (200~299는 성공)
        /*if (!response.ok) {
            throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
        }*/

        // 응답이 존재하면 JSON으로 변환, 아니면 빈 값 반환
        const contentType = response.headers.get("Content-Type");

        return contentType && contentType.includes("application/json") ? await response.json() : await response.text();

    } catch (error) {
        logMessage("요청 실패 : ", error.message);
        showMessage("warning", "오류가 발생했습니다", "관리자에게 문의해주세요");
        throw error;
    }
}

function getRequest(url, data = {}) {
    const queryString = new URLSearchParams(data).toString();
    window.location.href = url + (queryString ? `?${queryString}` : "");
}

function postRequest(url, data = {}) {
    const form = document.createElement("form");
    form.method = "POST";
    form.action = url;
    form.style.display = "none";

    // HashMap 데이터를 form의 hidden input 요소로 변환
    Object.entries(data).forEach(([key, value]) => {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = key;
        input.value = value;
        form.appendChild(input);
    });

    document.body.appendChild(form);
    form.submit();
}

async function gameApiRequest(url, method = "GET", data = {}) {
    try {
        const options = {
            method,
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            credentials: "include"
        };

        if (method !== "GET" && data) {
            options.body = JSON.stringify(data);
        }

        console.log('Game server request:', { url, method, data });

        const response = await fetch(url, options);

        console.log('Game server response:', response.status, response.statusText);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const contentType = response.headers.get("Content-Type");
        return contentType && contentType.includes("application/json")
            ? await response.json()
            : await response.text();

    } catch (error) {
        console.error("Game server request failed:", error);
        throw error;
    }
}