// 룸 리스트 페이지용 핸들러 등록
function registerHandlers() {
    GameCommon.registerMessageHandler('ROOM_CREATED', handleRoomCreated);
}

function handleConnected(message) {
    console.log('[HANDLER] 서버 연결 완료:', message.data);
}

function handlePong(message) {
    console.log('[HANDLER] 연결 상태 정상');
}

function handleTokenExpired(message) {
    console.log('[HANDLER] 토큰 만료');
}