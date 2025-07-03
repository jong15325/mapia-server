document.addEventListener('DOMContentLoaded', function() {

    // 게임 공통 기능 초기화
    if (!GameCommon.initialize()) {
        return;
    }

    registerHandlers();

});

function createRoom() {
    const form = document.getElementById('createRoomForm');
    const formData = new FormData(form);

    const roomData = {
        roomTitle: formData.get('roomTitle'),
        roomPwd: formData.get('roomPwd'),
        roomMaxPlayerNum: parseInt(formData.get('roomMaxPlayerNum'))
    };

    // AJAX 요청
    apiRequest('/game/rooms/create', "POST", roomData)
        .then(response =>
        {
            logMessage("OK", response);

            if (response.status === "OK") {
                showMessage('SUCCESS', '방 생성', '방이 성공적으로 생성되었습니다.');

                // 모달 닫기
                const modal = bootstrap.Modal.getInstance(document.getElementById('createRoomModal'));
                if (modal) {
                    modal.hide();
                }
                form.reset();
            } else {
                showMessage('ERROR', '방 생성 실패', '방 생성에 실패했습니다.');
            }
        })
        .catch(error =>
        {
            logMessage("ERROR : ", error);
            showMessage("WARNING", "방 생성 실패", "문제가 지속되면 관리자에게 문의해주세요");
        })
        .finally(() => {
            logMessage("OK", "완료");
        });
}

function deleteAllRooms() {

    // AJAX 요청
    apiRequest('/game/rooms/deleteAllRooms', "POST")
        .then(response =>
        {
            logMessage("OK", response);

            if (response.status === "OK") {
                showMessage('SUCCESS', '모든 방 삭제', '모든 방이 성공적으로 삭제되었습니다.');
            } else {
                showMessage('ERROR', '모든 방 삭제 실패', '모든 방 삭제에 실패했습니다.');
            }
        })
        .catch(error =>
        {
            logMessage("ERROR : ", error);
            showMessage("WARNING", "모든 방 삭제 중 오류가 발생했습니다.", "문제가 지속되면 관리자에게 문의해주세요");
        })
        .finally(() => {
            logMessage("OK", "완료");
        });
}

function handleRoomCreated() {
    showMessage('SUCCESS', '방 생성', '방이 생성되었습니다.');
}

function joinRoom(roomId, roomPwd) {
    let password = roomPwd;

    // 비밀방인 경우 비밀번호 입력
    if (roomPwd && roomPwd.trim().length > 0) {
        password = prompt('방 비밀번호를 입력하세요:');
        if (!password) {
            return;
        }
    }

    const joinData = {
        roomId: roomId,
        roomPwd: password || ''
    };
}