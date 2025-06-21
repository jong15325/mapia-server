<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg"
      data-sidebar-image="none" data-preloader="disable" data-theme="default" data-theme-colors="default">
<head>
    <%@ include file="../../common/layout/meta.jsp" %>
    <%@ include file="../../common/script/script_header.jsp" %>
    <script>
        document.addEventListener("DOMContentLoaded", function () {

            const alertRes = {
                code: "<c:out value='${alertResponse.enumCode.code}' />",
                type: "<c:out value='${alertType}' />",
                category: "<c:out value='${alertResponse.category}' />",
                successMethod: "<c:out value='${alertResponse.successMethod}' />",
                successUrl: "<c:out value='${alertResponse.successUrl}' />",
                successData:  ${empty alertResponse.successData ? "{}" : alertResponse.successData},
                cancelMethod: "<c:out value='${alertResponse.cancelMethod}' />",
                cancelUrl: "<c:out value='${alertResponse.cancelUrl}' />",
                cancelData:  ${empty alertResponse.cancelData ? "{}" : alertResponse.cancelData},
                message: "<c:out value='${alertResponse.enumCode.message}' />".trim() || null,
                addMessage: "<c:out value='${alertResponse.enumCode.addMessage}' />".trim() || null,
            };

            logMessage("성공", alertRes);

            alertRes.message = alertRes.message ? alertRes.message : alertRes.addMessage;

            if(alertRes.code) {
                switch (alertRes.type) {
                    case "SHOW":
                        showMessage(alertRes.category, alertRes.message, alertRes.addMessage);
                        break;
                    case "MOVE":
                        moveMessage(alertRes.category, alertRes.message, alertRes.addMessage, alertRes.successUrl);
                        break;
                    case "BACK":
                        backMessage(alertRes.category, alertRes.message, alertRes.addMessage);
                        break;
                    case "CLOSE":
                        closeMessage(alertRes.category, alertRes.message, alertRes.addMessage);
                        break;
                    case "CONFIRM":
                        showConfirm(alertRes.category, alertRes.message, alertRes.addMessage,
                            ()=> {
                                if(alertRes.successUrl) {
                                    if (alertRes.successMethod === "GET") {
                                        getRequest(alertRes.successUrl, alertRes.successData);
                                    } else if (alertRes.successMethod === "POST") {
                                        postRequest(alertRes.successUrl, alertRes.successData);
                                    } else if (alertRes.successMethod === "AJAX") {
                                        apiRequest(alertRes.successUrl, "POST", alertRes.successData)
                                            .then(response =>
                                            {
                                                logMessage("OK", response);
                                                if(response.moveUrl) {
                                                    moveMessage("INFO", response.message, response.addMessage, response.moveUrl);
                                                } else {
                                                    showMessage("INFO", response.message, response.addMessage);
                                                }

                                            })
                                            .catch(error =>
                                            {
                                                logMessage("ERROR", error);
                                                backMessage("ERROR", alertRes.message, alertRes.addMessage);
                                            })
                                    }
                                }
                            },
                            ()=> {
                                if(alertRes.cancelUrl) {
                                    if (alertRes.cancelMethod === "GET") {
                                        getRequest(alertRes.cancelUrl, alertRes.cancelData);
                                    } else if (alertRes.cancelMethod === "POST") {
                                        postRequest(alertRes.cancelUrl, alertRes.cancelData);
                                    } else if (alertRes.cancelMethod === "AJAX") {
                                        apiRequest(alertRes.cancelUrl, "POST", alertRes.cancelData)
                                            .then(response =>
                                            {
                                                logMessage("OK", response);
                                                if(response.moveUrl) {
                                                    moveMessage("INFO", response.message, response.addMessage, response.moveUrl);
                                                } else {
                                                    history.back();
                                                }
                                            })
                                            .catch(error =>
                                            {
                                                logMessage("ERROR", error);
                                                backMessage("ERROR", alertRes.message, alertRes.addMessage);
                                            })
                                    }
                                }
                            });
                        break;
                    default:
                        showMessage(alertRes.category, alertRes.message, alertRes.addMessage);
                        break;
                }
            }
        });
    </script>

</head>
<body>
  <%@ include file="../../common/layout/custom.jsp" %>
  <%@ include file="../../common/script/script_footer.jsp" %>
</body>
</html>
