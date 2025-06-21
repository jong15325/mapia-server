const isLocal = window.location.hostname === "localhost" || window.location.hostname.startsWith("127.") || window.location.hostname.startsWith("192.168.");

function logMessage(...args) {
    if (isLocal) {
        console.log(...args);
    }
}