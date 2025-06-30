document.addEventListener('DOMContentLoaded', function() {
    connectWebSocket();
});

function connectWebSocket() {
    const token = "asdfmklasmdlkfmaskldfmklasdmf";
    const ws = new WebSocket(`ws://localhost:8081/game-socket?token=${token}`);

    socket = new WebSocket(ws);

    ws.send(JSON.stringify({
        type: "ROOM",
        action: "JOIN_ROOM",
        data: { roomId: "room123" }
    }));
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