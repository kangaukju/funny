<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<script type="text/javascript">
	var ws = new WebSocket("ws://10.10.200.155:8080/TailServlet");
	
	// 로그 출력 라인을 maxLine 만큼만 유지 (메모리 사용량 조절)
	var maxLine = 1000;
	
    ws.onmessage = function(message){
    	// maxLine 만큼만 로그 유지
    	// 동적으로 로그 추가/삭제 
    	var table = document.getElementById("logTable");
    	
    	if (table.rows.length > maxLine) {
    		table.deleteRow(0);
    	}
    	var row = table.insertRow(table.rows.length);
    	var cell = row.insertCell(0);
    	cell.innerHTML = message.data;    	
    };
    function postToServer(){
        ws.send(document.getElementById("msg").value);
        document.getElementById("msg").value = "";
    }
    function closeConnect(){
        ws.close();
    }
</script>
</head>
<body>
	<table id="logTable"></table>
	<input id="msg" type="text" />
	<button type="submit" id="sendButton" onClick="postToServer()">Send!</button>
	<button type="submit" id="sendButton" onClick="closeConnect()">End</button>
</body>
</html>