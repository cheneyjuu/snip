<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>新增内容</title>
    <script type="text/javascript" src="${ctx}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="${ctx}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript">
        $(function(){
            window.UEDITOR_HOME_URL = "${ctx}/static/ueditor/";
        });
    </script>
</head>
<body>
<form method="post">
    <div class="control-group" id="areaDiv">
        <label class="control-label" for="myEditor">内容</label>
        <div class="controls">
            <textarea id="myEditor" name="content"></textarea>
        </div>
    </div>
</form>
<script src="${ctx}/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript">

    UE.getEditor('myEditor');

</script>
</body>
</html>