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
    <title>svm动作分类器模拟学习</title>
    <script type="text/javascript" src="js/jquery-2.2.2.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/sh.css">
    <script type="text/javascript">
        function startPredict() {
            jQuery.ajax({
                url: '/predict/start.do',
                type: 'post',
                dataType: 'text',
                data: {},
                success: function (data) {
                    alert(data);
                }
            });
        }

        function stopPredict() {
            jQuery.ajax({
                url: '/predict/stop.do',
                type: 'post',
                dataType: 'text',
                data: {},
                success: function (data) {
                    alert(data);
                }
            });
        }

        function learning(lable) {
            jQuery.ajax({
                url: '/learning.do',
                type: 'post',
                dataType: 'text',
                data: {
                    'lable': lable
                },
                success: function (data) {
                    alert(data);
                }
            });
        }


    </script>
</head>
<body>
<div style="width: 320px; height: 500px">
    <h3>控制选项</h3>
    <button class="button blue " onclick="startPredict()">开始预测</button>
    <br><br>
    <button class="button blue" onclick="stopPredict()">终止预测</button>
    <br><br>
    <h3>学习列表：</h3>
    <button class="button blue" onclick="learning('11')">电灯1：开灯</button>
    <br><br>
    <button class="button blue" onclick="learning('12')">电灯1：灭灯</button>
    <br><br>
    <button class="button blue" onclick="learning('21')">电灯2：开灯</button>
    <br><br>
    <button class="button blue" onclick="learning('22')">电灯2：灭灯</button>
    <br><br>
    <button class="button blue" onclick="learning('31')">电视1：开启</button>
    <br><br>
    <button class="button blue" onclick="learning('31')">电视1：关闭</button>
    <br><br>
    <br><br>
</div>

</body>


</html>