<%--
  Created by IntelliJ IDEA.
  User: ZHUKE
  Date: 2016/3/31
  Time: 20:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>svm动作分类器</title>
    <script type="text/javascript" src="js/jquery-2.2.2.min.js"></script>
    <script type="text/javascript">
        function startPredict() {
            jQuery.ajax({
                url: '/core/predict/start.do',
                type: 'post',
                dataType: 'text',
                data: {
                },
                success: function (data) {
                    alert(data);
                }
            });
        }

        function stopPredict() {
            jQuery.ajax({
                url: '/core/predict/stop.do',
                type: 'post',
                dataType: 'text',
                data: {
                },
                success: function (data) {
                    alert(data);
                }
            });
        }


    </script>
</head>
<body>

<button style="width:200px;length:200px" onclick="startPredict()">开始预测</button>
<button style="width:200px;length:200px" onclick="stopPredict()">终止预测</button>
<br><br>
<p>学习列表：</p>
<button style="width:200px;length:200px">电灯1：开灯</button>
<br><br>
<button style="width:200px;length:200px">电灯1：灭灯</button>
<br><br>
<button style="width:200px;length:200px">电视1：开启</button>
<br><br>
<button style="width:200px;length:200px">电视1：关闭</button>
<br><br>
<button style="width:200px;length:200px">电视1：频道+</button>
<br><br>
<button style="width:200px;length:200px">电视1：频道-</button>
<br><br>
<button style="width:200px;length:200px">电视1：音量+</button>
<br><br>
<button style="width:200px;length:200px">电视1：音量-</button>
<br><br>
</body>


</html>