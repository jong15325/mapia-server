document.addEventListener('DOMContentLoaded', function() {
    //connectWebSocket();
});

function connectWebSocket() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}:8081/ws/rooms`;

    socket = new WebSocket(wsUrl);

    socket.onopen = function() {
        console.log('WebSocket connected');
    };

    socket.onmessage = function(event) {
        try {
            const room = JSON.parse(event.data);
            rooms.set(room.roomId, room);
            renderRooms();
        } catch (error) {
            console.error('Failed to parse room update:', error);
        }
    };

    socket.onclose = function() {
        console.log('WebSocket disconnected');
        // 재연결 로직
        setTimeout(connectWebSocket, 3000);
    };

    socket.onerror = function(error) {
        console.error('WebSocket error:', error);
    };
}

function createRoom() {
    const roomTitle = document.getElementById('roomTitle').value;
    const roomPwd = document.getElementById('roomPwd').value;

    const requestData = {
        roomTitle: roomTitle,
        roomPwd: roomPwd
    };

    // AJAX 요청
    apiRequest('/game/rooms/create', "POST", requestData)
        .then(response =>
        {
            logMessage("OK", response);

        })
        .catch(error =>
        {
            logMessage("ERROR : ", error);
            showMessage("WARNING", "댓글 등록 중 오류가 발생했습니다.", "문제가 지속되면 관리자에게 문의해주세요");
        })
        .finally(() => {
            logMessage("OK", "완료");
        });
}