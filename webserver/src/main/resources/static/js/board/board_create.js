var previewTemplate, dropzone;
var ckeditorClassic = document.querySelector("#ckeditor-classic");
var dropzonePreviewNode = document.querySelector("#dropzone-preview-list");
var uploadedFiles = []; // 업로드된 파일 정보 저장 배열
var totalFiles = 0; // 전역 변수로 선언
var filesProcessed = 0; // 전역 변수로 선언

// Initialize CKEditor
if (ckeditorClassic) {
    ClassicEditor.create(document.querySelector("#ckeditor-classic"))
        .then(function(editor) {
            editor.ui.view.editable.element.style.height = "200px";

            editor.model.document.on('change:data', () => {
                document.getElementById('boardCont').value = editor.getData();
            });
        })
        .catch(function(error) {
            console.error(error);
        });
}

// Initialize Dropzone
if (dropzonePreviewNode) {
    // Remove the ID from the preview template
    dropzonePreviewNode.id = "";

    // Get the preview template HTML
    previewTemplate = dropzonePreviewNode.parentNode.innerHTML;

    // Remove the preview template from the DOM
    dropzonePreviewNode.parentNode.removeChild(dropzonePreviewNode);

    Dropzone.autoDiscover = false;  // deprecated 된 옵션. false로 해놓는걸 공식문서에서 명시

    // Initialize Dropzone with configuration
    dropzone = new Dropzone(".dropzone", {
        url: "/file/upload", // URL to send files to
        method: "post",                  // HTTP method
        previewTemplate: previewTemplate,       // Custom preview template
        previewsContainer: "#dropzone-preview",  // Container for file previews
        autoProcessQueue: false,  // 자동 업로드를 비활성화
        uploadMultiple: false, // 한 번에 하나씩 업로드
        parallelUploads: 1, // 병렬 업로드 수
        autoQueue: true, // 이 부분을 true로 변경
        clickable: true,
        maxFilesize: fileConfig.maxFileSize / (1024 * 1024), // 바이트를 MB로 변환
        maxFiles: fileConfig.maxCountPerPost,
        acceptedFiles: fileConfig.allowedExtensions ?
            fileConfig.allowedExtensions.map(ext => `.${ext}`).join(',') :
            ".jpg,.jpeg,.png,.gif,.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.zip,.rar",
        dictFileTooBig: `파일이 너무 큽니다 (최대: ${fileConfig.maxFileSize / (1024 * 1024)}MB)`,
        dictInvalidFileType: "이 파일 형식은 업로드할 수 없습니다",
        dictMaxFilesExceeded: `최대 ${fileConfig.maxCountPerPost}개까지만 업로드 가능합니다`,
        dictRemoveFile: "삭제",
        timeout: 300000, //커넥션 타임아웃 설정 -> 데이터가 클 경우 꼭 넉넉히 설정해주자
        headers: {
            'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
        },
        init: function() {
            let myDropzone = this; // closure 변수 (화살표 함수 쓰지않게 주의)

            // 등록 버튼에 이벤트 리스너 추가
            document.querySelector('#createBtn').addEventListener('click', function (e) {
                e.preventDefault(); // 버튼 기본 동작 방지

                formCheck();
            });

            // 파일 추가 시 로깅
            myDropzone.on("addedfile", function(file) {
                console.log("파일 추가됨: " + file.name + " (" + file.size + " bytes)");

                // 파일 크기 검증
                if (file.size > fileConfig.maxFileSize) {
                    console.error("파일 크기 초과: " + file.name + " (" + file.size + " bytes)");
                    this.removeFile(file);
                    showMessage("WARNING", "파일 크기 초과", "\"" + file.name + "\" 파일이 최대 용량(" + (fileConfig.maxFileSize / (1024 * 1024)) + "MB)을 초과합니다.");
                    return;
                }

                // 최소 파일 크기 검증 추가
                if (file.size < fileConfig.minFileSize) {
                    console.error("파일 크기 미달: " + file.name + " (" + file.size + " bytes)");
                    this.removeFile(file);
                    showMessage("WARNING", "파일 크기 미달", "\"" + file.name + "\" 파일이 최소 용량(" + (fileConfig.minFileSize / 1024) + "KB)보다 작습니다.");
                    return;
                }

                // 파일 형식 검증
                const fileExtension = file.name.split('.').pop().toLowerCase();
                if (!fileConfig.allowedExtensions.includes(fileExtension)) {
                    console.error("파일 형식 오류: " + file.name + " (" + fileExtension + ")");
                    this.removeFile(file);
                    showMessage("WARNING", "파일 형식 오류", "\"" + file.name + "\" 파일은 허용되지 않는 형식입니다. 허용되는 형식: " + fileConfig.allowedExtensions.join(', '));
                }

                // 파일 유형에 따른 아이콘 설정
                const fileType = FileUtil.getFileType(fileExtension);
                if (fileConfig.typeIcons && fileConfig.typeIcons[fileType]) {
                    const previewElement = file.previewElement;
                    const imgContainer = previewElement.querySelector(".avatar-sm");

                    if (imgContainer) {
                        const imgElement = previewElement.querySelector("[data-dz-thumbnail]");
                        if (imgElement) {
                            imgElement.remove();
                        }

                        // 아이콘 클래스와 배경색 가져오기
                        const iconClass = FileUtil.getIconClass(fileType);
                        const bgColorClass = FileUtil.getBgColorClass(fileType);

                        // 아이콘 요소 생성 및 추가
                        const iconElement = document.createElement("i");
                        iconElement.className = iconClass + " display-4"; // RemixIcon 클래스 + 크기

                        // 컨테이너에 클래스 추가
                        imgContainer.className = "avatar-sm rounded d-flex align-items-center justify-content-center " + bgColorClass;

                        // 아이콘 추가
                        imgContainer.appendChild(iconElement);

                        // 컨테이너에 클래스 추가
                        imgContainer.className = "avatar-sm rounded d-flex align-items-center justify-content-center " + bgColorClass;

                        // 아이콘 추가
                        imgContainer.appendChild(iconElement);
                    }
                }
            });

            myDropzone.on("maxfilesexceeded", function(file) {
                // 초과된 파일 자동 제거
                myDropzone.removeFile(file);

                // 사용자에게 알림
                showMessage("WARNING", "파일 수 초과", "최대 10개의 파일만 업로드할 수 있습니다.");
            });

        }
    });
}

let isSubmit = false; // 중복 제출 여부 확인 변수

// boardTitle 체크 함수 추가
function boardTitleCheck() {
    let boardTitleEl = document.getElementById("project-title-input");
    let boardTitle = boardTitleEl.value.trim();

    if (InputValidator.isEmpty(boardTitle)) {
        showMessage("WARNING", "제목을 입력해주세요", "", focusClose(boardTitleEl));
        return false;
    }

    return true;
}

// boardCont 체크 함수 추가
function boardContCheck() {
    const editor = document.querySelector('.ck-editor__editable').ckeditorInstance;
    const content = editor.getData();

    if (InputValidator.isEmpty(content)) {
        showMessage("WARNING", "내용을 입력해주세요", "", focusClose(editor));
        return false;
    }

    return true;
}

/* 폼 비활성화 */
function submitDisabled(disabled) {
    const btn = document.getElementById("createBtn");
    btn.disabled = disabled;
    isSubmit = disabled;
}

function handleFileUpload(response) {
    const boardIdx = response.data.boardIdx;
    const ctgType = response.data.ctgType;

    // 파일이 있는 경우 업로드 진행
    const myDropzone = Dropzone.forElement('.dropzone');
    if (myDropzone.files.length > 0) {
        console.log("파일 업로드 시작 - 게시글 ID: " + response.data.boardIdx + ", 파일 수: " + myDropzone.files.length);

        totalFiles = myDropzone.files.length;
        filesProcessed = 0;

        myDropzone.options.params = {
            boardIdx: boardIdx
        };

        // 파일 전송 전 처리
        myDropzone.on("sending", function(file, xhr, formData) {
            console.log("파일 전송 시작: " + file.name);
            formData.append('_csrf', document.querySelector('input[name="_csrf"]').value);
            formData.append('boardIdx', boardIdx);
            formData.append('fileType', "board");
            formData.append('order', myDropzone.files.indexOf(file));
        });

        // 파일 업로드 진행률 표시
        myDropzone.on("uploadprogress", function(file, progress) {
            if (progress % 25 === 0 || progress === 100) { // 25% 단위로만 로그 출력
                console.log(`파일 "${file.name}" 업로드 진행률: ${progress.toFixed(0)}%`);
            }
        });

        // 게시글 ID를 dropzone 설정에 추가
        myDropzone.options.url = `/file/upload`;
        myDropzone.processQueue();

        // 서버로 파일이 성공적으로 전송되면 실행
        myDropzone.on('success', function (file, response) {
            console.log('파일 업로드 성공: ' + file.name);

            // 서버 응답 확인
            if(response.status === "OK") {
                // 업로드된 파일 정보 저장
                uploadedFiles.push(response.fileInfo);

                filesProcessed++;

                // 마지막 파일이거나 25% 단위로만 로그 출력
                if (filesProcessed === totalFiles || filesProcessed % Math.ceil(totalFiles/4) === 0) {
                    console.log(`파일 처리 진행률: ${filesProcessed}/${totalFiles} (${Math.round(filesProcessed/totalFiles*100)}%)`);
                }

                // 다음 파일이 있으면 처리
                if (filesProcessed < totalFiles) {
                    myDropzone.processQueue();
                } else {
                    console.log('모든 파일 업로드 완료');
                }
            } else {
                // 업로드 실패 처리
                console.error('파일 업로드 실패:', response.message);
                showMessage("WARNING", "파일 업로드에 실패했습니다", response.message);
                submitDisabled(false);
            }
        });

        // 파일 업로드 완료 후 페이지 이동 처리를 위해 이벤트 리스너 등록
        myDropzone.on('queuecomplete', function() {
            console.log('파일 업로드 큐 완료, 게시글 페이지로 이동');
            moveMessage("SUCCESS", response.message, response.addMessage, "/board/list?ctgType=" + ctgType);
        });

        // 업로드 에러 처리
        myDropzone.on('error', function (file, errorMessage) {
            console.error(`파일 업로드 오류 (${file.name}): ${errorMessage}`);
            showMessage("WARNING", "파일 업로드 오류", `${file.name}: ${errorMessage}`);
            submitDisabled(false);
        });
    } else {
        // 파일이 없으면 바로 페이지 이동
        console.log('첨부 파일 없음, 게시글 페이지로 이동');
        moveMessage("SUCCESS", response.message, response.addMessage, "/board/list?ctgType=" + ctgType);
    }
}

function formCheck() {

    if(!isSubmit) {
        submitDisabled(true);

        const editor = document.querySelector('.ck-editor__editable').ckeditorInstance;
        document.getElementById('boardCont').value = editor.getData();

        const validFunctions = [
            { validate: boardTitleCheck, element: document.getElementById("project-title-input") },
            { validate: boardContCheck, element: document.querySelector('.ck-editor__editable') }
        ];

        for (let item of validFunctions) {
            if (!item.validate()) {
                submitDisabled(false);
                return false;
            }
        }

        // 폼 데이터 생성
        const form = document.getElementById('boardForm');
        const formData = new FormData(form);

        apiRequest("/board/createProc", "POST", formData)
            .then(response =>
                {
                    logMessage("OK", response);
                    if (response.status === "OK") {

                        handleFileUpload(response);
                    } else {
                        console.error('게시글 생성 실패:', response.message);
                        showMessage("WARNING", response.message, response.addMessage);
                        submitDisabled(false);
                    }
                }
            )
            .catch(error =>
                {
                    logMessage("ERROR", error);
                    showMessage("WARNING", "게시글 등록 중 오류가 발생했습니다.", "문제가 지속되면 관리자에게 문의해주세요");
                    submitDisabled(false);
                }
            ).finally(()=>
            {
                submitDisabled(false);
            }
        );

        return false;
    }
}