const GameCommon = (function() {

    // 내부 변수
    let gameWebSocket = null;
    let messageHandlers = new Map();

    /**
     * 게임 설정 초기화 및 검증
     */
    function initialize() {
        if (!window.gameConfig) {
            console.error('[GAME COMMON] 게임 설정이 없습니다.');
            showMessage('ERROR', '설정 오류', '게임 설정을 불러올 수 없습니다.');
            return false;
        }

        console.log('[GAME COMMON] 게임 설정 로드 완료:', window.gameConfig);

        connectToGameServer();

        return true;
    }

    /**
     * 게임서버 WebSocket 연결
     */
    function connectToGameServer() {
        if (!window.gameConfig?.accessToken) {
            console.error('[GAME COMMON] JWT 토큰이 없습니다.');
            updateConnectionStatus(false, '토큰 없음');
            return;
        }

        try {
            const wsUrl = `${window.gameConfig.webSocketUrl}?token=${window.gameConfig.accessToken}`;
            console.log('[GAME COMMON] WebSocket 연결 시도:', wsUrl);

            gameWebSocket = new WebSocket(wsUrl);

            gameWebSocket.onopen = function(event) {
                console.log('[GAME COMMON] WebSocket 연결 성공');

                updateConnectionStatus(true, '연결됨');

            };

            gameWebSocket.onmessage = function(event) {
                try {
                    const message = JSON.parse(event.data);
                    handleGameMessage(message);
                } catch (error) {
                    console.error('[GAME COMMON] 메시지 파싱 오류:', error, event.data);
                }
            };

            gameWebSocket.onclose = function(event) {
                console.log('[GAME COMMON] WebSocket 연결 종료:', event.code, event.reason);
                updateConnectionStatus(false, 'WebSocket 연결 종료');
            };

            gameWebSocket.onerror = function(error) {
                console.error('[GAME COMMON] WebSocket 오류:', error);
                updateConnectionStatus(false, 'WebSocket 오류');
            };

        } catch (error) {
            console.error('[GAME COMMON] WebSocket 연결 중 오류:', error);
        }
    }

    function disconnectFromGameServer() {
        if (gameWebSocket) {
            gameWebSocket.close(1000, 'Client disconnect');
            gameWebSocket = null;
        }
    }

    function refreshGameToken() {
        console.log('[GAME COMMON] 토큰 갱신 시도');

        apiRequest('/game/token/refresh', 'POST', {})
            .then(response => {
               console.log(response);
            })
            .catch(error => {
                console.error('[GAME COMMON] 토큰 갱신 오류:', error);
                showMessage('ERROR', '토큰 갱신 오류', '토큰 갱신 중 오류가 발생했습니다.');
            });
    }

    function handleGameMessage(message) {
        console.log('[GAME COMMON] 메시지 수신:', message);

        // 등록된 핸들러가 있으면 실행
        if (messageHandlers.has(message.action)) {
            const handler = messageHandlers.get(message.action);
            if (typeof handler === 'function') {
                try {
                    handler(message);
                } catch (error) {
                    console.error(`[GAME COMMON] 핸들러 실행 오류 (${message.action}):`, error);
                }
            }
        } else {
            console.warn(`[GAME COMMON] 등록되지 않은 액션: ${message.action}`);
        }
    }

    function updateConnectionStatus(isConnected, statusText) {
        const statusElement = document.getElementById('connectionStatus');
        const textElement = document.getElementById('connectionText');

        if (!statusElement || !textElement) return;

        statusElement.className = `alert d-flex align-items-center ${isConnected ? 'alert-success' : 'alert-warning'}`;
        textElement.textContent = `게임서버: ${statusText}`;

        const icon = statusElement.querySelector('i');
        if (icon) {
            icon.className = isConnected ? 'ri-wifi-line me-2' : 'ri-wifi-off-line me-2';
        }
    }

    /**
     * 메시지 핸들러 등록
     */
    function registerMessageHandler(action, handler) {
        messageHandlers.set(action, handler);
    }

    /**
     * 메시지 핸들러 제거
     */
    function unregisterMessageHandler(action) {
        messageHandlers.delete(action);
    }

    // Public API
    return {
        initialize,
        registerMessageHandler,
        unregisterMessageHandler,
        connectToGameServer,
        disconnectFromGameServer,
        refreshGameToken,
        updateConnectionStatus
    };
})();