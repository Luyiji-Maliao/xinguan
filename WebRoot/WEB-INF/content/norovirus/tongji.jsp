<%--
  Created by IntelliJ IDEA.
  User: jianzhiping
  Date: 2020/5/12
  Time: 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <title>新冠统计信息</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="新冠统计信息">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <meta name="viewport" content=" maximum-scale=1.0, user-scalable=0">

    <link rel="stylesheet" href="css/xinguansign.css">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <%--<link href="https://cdn.bootcdn.net/ajax/libs/multiple.js/0.0.1/multiple.css" rel="stylesheet">--%>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/echarts@4.5.0/dist/echarts.min.js"></script>
    <script src="https://gw.alipayobjects.com/os/antv/assets/f2/3.4.2/f2.min.js"></script>
    <%--<script src="https://cdn.bootcdn.net/ajax/libs/multiple.js/0.0.1/multiple.min.js"></script>--%>
    <script src="js/slider.js"></script>
    <style>
        .echartdiv{
            font-size:40px;
            height: 600px;
            margin-top:20px;
            margin-bottom:20px;
        }
        .div{
            margin: 40px 20px;
            padding: 30px;
            border-radius: 30px;
            background-color: #fff;
        }
        .echartdiv2{
            height: 750px;
        }
        .echartdiv3{
            height: 450px;
            background: #fff;
        }
        h1{
            font-size:40px;
            font-weight: bold;
        }
        .inputdate{
            margin: 0px 10px;
            height:80px;
            color:#D1D6DC;
            width:285px;
            border: 0px;
            text-align:center;
            background-color: #fff;
            border-bottom:1px solid #D1D6DC;
        }
        button.btn{
            font-size:40px;    height: 100px;
            width: 100px;
            padding: 0px 20px;
            border-radius: 10px;
            margin: 5px;
            background-color: #0045C7;
        }
        .info{
            font-size:32px;
        }
        #table{
            margin-top:30px;
            border-radius: 30px;
        }
        #table tr{
            height: 90px;
            border: 0px;
            border-bottom: 2px solid #D1D6DC;
        }
        thead tr{
            background-color: #ECF6FF;
            color: #0142C4;
            font-size: 42px;
        }
        #table thead tr th{
            min-width: 130px;
            font-size: 32px;
        }
        #table tr td{
            text-align: center;
            line-height: 100px;
        }
        hr{
            border-width:5px;
        }
        #geshu{
            font-size:40px;
            width: 100%;
            margin-top:20px;
            margin-bottom:20px;
        }
        #geshu li{
            list-style: none;
            color:#fff;
            background: #0045C7;
            border-radius: 10px;
            margin: 20px 0px;
            height: 80px;
            line-height: 80px;
            padding:0px 20px;
        }
        #geshu li .personnum{
            position: absolute;
            right: 70px;
        }
        .multiple-mobile-content {
            height: 100%;
            z-index: 50;
            position: relative;
            left:30px;
            /* display: inline-block; */
        }
        .multiple-mobile{
            z-index: 1;
        }

        #bannerNav{
            display: none;
        }
        #banner,#banner3,#banner4 {
            margin-bottom:-10px;
            margin-left:-120px;
        }
        #carousel,#carouse3,#carouse3 {
            position:relative;
            z-index:2;
            margin-top:20px;
            transform-style:preserve-3d;
            perspective:800px
        }
        #carousel div,#carouse3>div,#carouse4>div{
            position:absolute;
            left:45%;
            top:50%;
            margin-left:-252px;
            transition:transform .5s ease-in-out;
            box-shadow:8px 8px 20px rgba(0,0,0,.2);
            cursor:pointer;
            background-color:#0141C5;
        }
        #bannerNav ul li {
            cursor:pointer;
            overflow:hidden;
            display:inline-block;
            width:22px;
            margin:0 2px
        }
        #bannerNav ul li a {
            margin:0 auto;
            display:block;
            width:6px;
            height:6px;
            vertical-align:top;
            border-radius:3px;
            background:#5e6671;
            font-size:0
        }
        #bannerNav ul li.on a,#bannerNav ul li:hover a {
            background:#00aeff
        }
        #bannerNav ul li.on a {
            width:20px
        }
        #carousel div,#carouse3>div,#carouse4>div {
            width:700px;
            border-radius: 10px;
            /*opacity: 0;*/
            color:#fff;
            padding: 20px 40px;
            margin-bottom: 80px;
        }
        li{
            list-style: none;
        }
        #carousel:after,#carouse3:after,#carouse4:after{/*伪元素是行内元素 正常浏览器清除浮动方法*/
            content: "";
            display: block;
            height: 0;
            clear:both;
            visibility: hidden;
        }
    </style>
</head>
<body >
<%--使用大div包裹所有元素，因ios在body上设置样式不太兼容--%>
<div style="font-size:32px;background-color:#F4F4F4;width: 100%;overflow-x: hidden;">
    <%--标题--%>
<p style="font-size:70px;text-align: center;color:#fff;height: 328px;line-height: 328px;
    font-family: cursive;width:100%;background: url('img/phone_header.jpg') no-repeat;background-size: cover;">新冠采样点信息统计</p>

<%--各渠道人数总计 start--%>
<div class="div" style="height: 800px;" id="gequdao">
        <h1>各渠道每日人数概览</h1>
        <input type="date" name="startdate" id="startdate2" min="2020-04-18" class="inputdate"><span style="color:#D1D6DC;">—</span>
        <input type="date" name="enddate" id="enddate2" min="2020-04-18" class="inputdate">
        <button type="button" class="btn btn-primary btn-lg" onclick="query2()">
            <span class="glyphicon glyphicon-search" style="color:#fff;" aria-hidden="true"></span>
        </button>
        <div id="banner">
            <div id="carousel">
            </div>
        </div>
    </div>
<%--各渠道人数总计 end--%>

<%--各渠道人数总计 start--%>
<div class="div" style="height: 800px;" id="hejiechart">
            <h1>各渠道每日预约人数</h1>
            <input type="date" name="startdate" id="startdate3" min="2020-04-18" class="inputdate"><span style="color:#D1D6DC;">—</span>
            <input type="date" name="enddate" id="enddate3" min="2020-04-18" class="inputdate">
            <button type="button" class="btn btn-primary btn-lg" onclick="query3()">
                <span class="glyphicon glyphicon-search" style="color:#fff;" aria-hidden="true"></span>
            </button>
            <div id="banner3">
                <div id="carouse3">
                    <div>
                        <h2>全部渠道</h2>
                        <hr>
                    <div id="heji" class="echartdiv3"></div>
                    </div>
                    <div>
                        <h2>线上预约</h2>
                        <hr>
                    <div id="xianshang" class="echartdiv3" ></div>
                    </div>
                    <div>
                        <h2>团体</h2>
                        <hr>
                    <div id="tuantiqudao" class="echartdiv3" ></div>
                    </div>
                    <div>
                        <h2>京东</h2>
                        <hr>
                    <div id="jingdongqudao" class="echartdiv3"></div>
                    </div>
                    <div>
                        <h2>华大合作</h2>
                        <hr>
                    <div id="huadaqudao" class="echartdiv3"></div>
                    </div>
                </div>
            </div>
        </div>
<%--各渠道人数总计 end--%>

<%--各渠道人数实际 start--%>
<div class="div" style="height: 800px;" id="shijiechart">
            <h1>各渠道每日实际采样人数</h1>
            <input type="date" name="startdate" id="startdate4" min="2020-04-18" class="inputdate"><span style="color:#D1D6DC;">—</span>
            <input type="date" name="enddate" id="enddate4" min="2020-04-18" class="inputdate">
            <button type="button" class="btn btn-primary btn-lg" onclick="query4()">
                <span class="glyphicon glyphicon-search" style="color:#fff;" aria-hidden="true"></span>
            </button>
            <div id="banner4">
                <div id="carouse4" style="position: relative;">
                    <div>
                        <h2>全部渠道</h2>
                        <hr>
                        <div id="heji1" class="echartdiv3" ></div>
                    </div>
                    <div>
                        <h2>线上预约</h2>
                        <hr>
                        <div id="xianshang1" class="echartdiv3"></div>
                    </div>
                    <div>
                        <h2>团体</h2>
                        <hr>
                        <div id="tuantiqudao1" class="echartdiv3"></div>
                    </div>
                    <div>
                        <h2>京东</h2>
                        <hr>
                        <div id="jingdongqudao1" class="echartdiv3"></div>
                    </div>
                    <div>
                        <h2>华大合作</h2>
                        <hr>
                        <div id="huadaqudao1" class="echartdiv3"></div>
                    </div>
                </div>
            </div>
        </div>
<%--各渠道人数实际 end--%>

<%--每日总预约数  start--%>
<div class="div">
    <h1>每日总预约数</h1>
    <div id="zong" class="echartdiv"></div>
</div>
<%--每日总预约数  end--%>

<%--各采样点总人数 start--%>
<div class="div">
<h1>各采样点总人数</h1>
    <div id="geshu">
        <ul>
        </ul>
    </div>
</div>
<%--各采样点总人数 end--%>

<%--各采样点人数统计 start--%>
<div class="div">
        <h1 >各采样点人数统计</h1>
        <from>
            <input type="date" name="startdate" id="startdate" min="2020-04-18" class="inputdate"><span style="color:#D1D6DC;">—</span>
            <input type="date" name="enddate" id="enddate" min="2020-04-18" class="inputdate">
            <button type="button" class="btn btn-primary btn-lg" onclick="query()">
                <span class="glyphicon glyphicon-search" style="color:#fff;" aria-hidden="true"></span>
            </button>
        </from>
        <%--<button class="btn btn-info" style="width:100%;">查看全部</button>--%>
        <table class="table" id="table">
            <thead>
            <th>采样点</th>
            <th>现场</th>
            <th>线上</th>
            <th>合计</th>
            <th>实际</th>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
<%--各采样点人数统计 end--%>
<%--底部优讯logo start--%>
<img src="img/uscilogo.png" alt="usci" width="303px" height="94px" style="display: block;margin: 40px auto;">
<%--底部优讯logo end--%>
</div>
</body>
<script>
    // var ua = navigator.userAgent.toLowerCase();
    // var isWeixin = ua.indexOf('micromessenger') != -1;
    // if (isWeixin) {
    //     alert("微信");
    // }else{
    //     alert("不是");
    // }
    var day1 = new Date();
    day1.setTime(day1.getTime()-24*60*60*1000);
    var mh=(day1.getMonth()+1)+"";
    if(mh.length<2){
        mh="0"+mh;
    }
    var s1 = day1.getFullYear()+"-" + mh + "-" + (day1.getDate().toString().length<2?"0"+day1.getDate().toString():day1.getDate());
        var date=new Date();
        $("#startdate").val(date.toJSON().substr(0,10));
        $("#enddate").val(date.toJSON().substr(0,10));
        $("#startdate").attr("max",date.toJSON().substr(0,10));
        $("#enddate").attr("max",date.toJSON().substr(0,10));
    var startdate=$("#startdate").val();
    var enddate=$("#enddate").val();

    function query(){
        startdate=$("#startdate").val();
        enddate=$("#enddate").val();
        gettable();
    }

    gettable();
    function gettable(){
        if(startdate>enddate){
            alert("起始时间请不要大于结束时间");
            return;
        }
        $.post({
            url:'sign!getcaiyangren.action',
            data:{"startdate":startdate,"enddate":enddate},
            success:function (data) {
                $("#table tbody").html("");
                var didian=[];
                var xianchang=[];
                var xianshang=[];
                var heji=[];
                var shiji=[];
                var zongchang=0;
                var zongxian=0;
                var zonghe=0;
                var zongshi=0;
                //合计
                for(var i=0;i<data.shijilist.length;i++){
                    didian[i]=data.shijilist[i].caiyangdian;
                    if(data.shijilist[i].qudao=="团体"){
                        didian[i]+="（团体）";
                    }
                    shiji[i]=data.shijilist[i].renshu;
                }
                //现场   根据采样点循环匹配到对应的下标然后填入数据
                for(var i=0;i<data.xianchanglist.length;i++){
                    var biao2=didian.indexOf(data.xianchanglist[i]==null?-1:data.xianchanglist[i].caiyangdian);
                    if(biao2!=-1){
                        xianchang[biao2]=data.xianchanglist[i].renshu;
                    }
                }
                //线上预约  根据采样点循环匹配到对应的下标然后填入数据
                for(var i=0;i<data.xianshanglist.length;i++){
                    var biao=didian.indexOf(data.xianshanglist[i]==null?-1:data.xianshanglist[i].caiyangdian);
                    if(biao!=-1){
                        xianshang[biao]=data.xianshanglist[i].renshu;
                    }
                }
                //合计  根据采样点循环匹配到对应的下标然后填入数据
                for(var i=0;i<data.hejilist.length;i++){
                    var biao3=didian.indexOf(data.hejilist[i]==null?-1:data.hejilist[i].caiyangdian);
                    if(biao3!=-1){
                        heji[biao3]=data.hejilist[i].renshu;
                    }
                }
                //计算总计人数，并将为匹配到采样点的数据修改为0  并将数据添加到页面
                for (var i=0;i<didian.length;i++){
                    if(didian[i] && didian[i].toString().indexOf("(")>-1){
                        didian[i]=didian[i].substring(0,didian[i].indexOf("("));
                    }
                    if(didian[i] && didian[i].indexOf("优迅医学")>-1){
                        didian[i]="优迅医学检验实验室"
                    }
                    if(!xianchang[i]) {
                        xianchang[i] = 0;
                    }
                    if(!xianshang[i]){
                        xianshang[i]=0;
                    }
                    if(!heji[i]){
                        heji[i]=0;
                    }
                    zongchang+=xianchang[i];
                    zongxian+=xianshang[i];
                    zonghe+=heji[i];
                    zongshi+=shiji[i];
                    $("#table tbody").append("<tr><td style='text-align:left;'>"+didian[i]+"</td><td  >"+xianchang[i]+"</td><td >"+xianshang[i]+"</td><td  >"+heji[i]+"</td><td>"+shiji[i]+"</td></tr>");
                }

                $("#table tbody").append("<tr class='info' ><td style='text-align:left;color:#FF6801;'>总计：</td><td>"+zongchang+"</td><td>"+zongxian+"</td><td>"+zonghe+"</td><td>"+zongshi+"</td></tr>");
            }
        })
    }
    /*每日总预约数          strat*/
    var zong = echarts.init(document.getElementById('zong'));
    zong.showLoading();
    $.post({
        url:'sign!tongjiall.action',
        success:function (data) {
            var series=[]
            var renshuarr=[];
            var names=[];
            for(var i=0;i<data.data.length;i++){
                renshuarr[i]=data.data[i].renshu;
            }

            zong.hideLoading();
            zongji={
                "data": renshuarr,
                "delta": [
                    null
                ],
                "names": data.datelist
            };

            var start = zongji.data.length - 15
            var end = zongji.data.length - 1

            option = {
                color:['#417CE0'],
                tooltip : {
                    trigger: 'item',
                    "textStyle": {
                        "fontSize": 40
                    }
                },
                legend: {
                    data: "总计",
                    textStyle: {
                        "fontSize": 35
                    },
                    "left": "left",
                },
                grid: {
                    top: '5%',
                    left: '1%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type : 'category',
                        data : zongji.names,
                        axisLabel:{textStyle: {
                                "fontSize": 35
                            },}
                    }
                ],
                yAxis: [
                    {
                        type : 'value',
                        axisLabel: {
                            textStyle: {
                                "fontSize": 30
                            },
                            formatter: function (a) {
                                a = +a;
                                return isFinite(a)
                                    ? echarts.format.addCommas(+a)
                                    : '';
                            }
                        }
                    }
                ],
                dataZoom: [
                    {
                        type: 'slider',
                        show: true,
                        startValue: start,
                        endValue: end,
                        handleSize: 8,
                    },
                    {
                        type: 'inside',
                        start: 94,
                        end: 100,
                    }
                ],
                series : {
                    name: '总计',
                    type: 'bar',
                    label: {
                        show: true,
                        position: 'inside',
                        "textStyle": {
                            "fontSize": 28
                        }
                    },
                    data:  zongji.data
                }
            };

            zong.setOption(option);

        }
    });
    /*每日总预约数          strat*/

    /*各采样点每日人数信息     start*/
    $("#startdate2").val(s1);
    $("#enddate2").val(s1);
    $("#startdate2").attr("max",date.toJSON().substr(0,10));
    $("#enddate2").attr("max",date.toJSON().substr(0,10));
    var startdate2=$("#startdate2").val();
    var enddate2=$("#enddate2").val();
    function query2(){
        startdate2=$("#startdate2").val();
        enddate2=$("#enddate2").val();
        cydnum();
    }
    function cydnum(){
        $.post({
            url:'sign!gediannum.action',
            data:{"startdate":startdate2,"enddate":enddate2},
            success:function (data) {
                $("#carousel").html("");
                var allarr=[];
                var alldilist=[];
                var allhtml="<div>" +
                    "      <h2>全部渠道</h2><h2>"+startdate2+"—"+enddate2+"</h2>" +
                    "      <hr>" +
                    "      <table class='table'>"+
                    "      <tr><td>采样点</td><td>线上</td><td>现场</td><td>合计</td><td>实际</td></tr>";
                //全部渠道采样点人数
                //先循环出实际的人数与采样地址
                for(var i=0;i<data.allscydlist.length;i++){
                    alldilist[i]=data.allscydlist[i].caiyangdian;
                    allarr[i]={
                        shiji:data.allscydlist[i].shiji,
                        heji:0,
                        xianchang:0,
                        xianshang:0
                    }
                }
                //然后把预约的人数根据采样点匹配放到同一个数组中
                for(var i=0;i<data.allcydlist.length;i++){
                    var ix=alldilist.indexOf(data.allcydlist[i].caiyangdian);
                    if(ix==-1){
                        continue;
                    }
                    allarr[ix].heji=data.allcydlist[i].heji
                    allarr[ix].xianchang=data.allcydlist[i].xianchang;
                    allarr[ix].xianshang=data.allcydlist[i].xianshang;
                }
                //最后循环拼接一个html文本对象
                var xianshangzong=0;
                var xianchangzong=0;
                var hejizong=0;
                var shijizong=0;
                for (var i=0;i<alldilist.length;i++){
                    if(alldilist[i] && alldilist[i].indexOf("(")>-1){
                        alldilist[i]=alldilist[i].substr(0,alldilist[i].indexOf("("));
                    }
                    if(alldilist[i]&&alldilist[i].length>10){
                        alldilist[i]=alldilist[i].substr(0,7);
                    }
                    xianshangzong+=allarr[i].xianshang;
                    xianchangzong+=allarr[i].xianchang;
                    hejizong+=allarr[i].heji;
                    shijizong+=allarr[i].shiji;
                    allhtml+="<tr><td>"+alldilist[i]+"</td><td>"+allarr[i].xianshang+"</td><td>"+allarr[i].xianchang+"</td><td>"+allarr[i].heji+"</td><td>"+allarr[i].shiji+"</td></tr>";
                }

                allhtml+="<tr><td>合计：</td><td>"+xianshangzong+"</td><td>"+xianchangzong+"</td><td>"+hejizong+"</td><td>"+shijizong+"</td></tr> " +
                    "     </table>" +
                    "     </div>";
                $("#carousel").append(allhtml)


                //线上预约渠道
                var xsarr=[];
                var xsdilist=[];
                var xshtml="<div>" +
                    "      <h2>线上预约</h2><h2>"+startdate2+"—"+enddate2+"</h2>" +
                    "      <hr>" +
                    "      <table class='table'>"+
                    "      <tr><td>采样点</td><td>线上</td><td>现场</td><td>合计</td><td>实际</td></tr>";
                for(var i=0;i<data.xsscydlist.length;i++){
                    xsdilist[i]=data.xsscydlist[i].caiyangdian;
                    xsarr[i]={
                        shiji:data.xsscydlist[i].shiji,
                        heji:0,
                        xianchang:0,
                        xianshang:0
                    }
                }
                for(var i=0;i<data.xscydlist.length;i++){
                    var ix=xsdilist.indexOf(data.xscydlist[i].caiyangdian);
                    if(ix==-1){
                        continue;
                    }
                    xsarr[ix].heji=data.xscydlist[i].heji
                    xsarr[ix].xianchang=data.xscydlist[i].xianchang;
                    xsarr[ix].xianshang=data.xscydlist[i].xianshang;
                }
                xianshangzong=0;
                xianchangzong=0;
                hejizong=0;
                shijizong=0;
                for (var i=0;i<xsdilist.length;i++){
                    if(xsdilist[i] && xsdilist[i].indexOf("(")>-1){
                        xsdilist[i]=xsdilist[i].substr(0,xsdilist[i].indexOf("("));
                    }
                    if(xsdilist[i]&&xsdilist[i].length>10){
                        xsdilist[i]=xsdilist[i].substr(0,7);
                    }
                    xianshangzong+=xsarr[i].xianshang;
                    xianchangzong+=xsarr[i].xianchang;
                    hejizong+=xsarr[i].heji;
                    shijizong+=xsarr[i].shiji;
                    xshtml+="<tr><td>"+xsdilist[i]+"</td><td>"+xsarr[i].xianshang+"</td><td>"+xsarr[i].xianchang+"</td><td>"+xsarr[i].heji+"</td><td>"+xsarr[i].shiji+"</td></tr>";
                }
                xshtml+="<tr><td>合计：</td><td>"+xianshangzong+"</td><td>"+xianchangzong+"</td><td>"+hejizong+"</td><td>"+shijizong+"</td></tr>" +
                    "      </table>" +
                    "     </div>";
                $("#carousel").append(xshtml);

                //团体渠道
                var ttarr=[];
                var ttdilist=[];
                var tthtml="<div>" +
                    "      <h2>团体</h2><h2>"+startdate2+"—"+enddate2+"</h2>" +
                    "      <hr>" +
                    "      <table class='table'>"+
                    "      <tr><td>采样点</td><td>线上</td><td>现场</td><td>合计</td><td>实际</td></tr>";
                for(var i=0;i<data.ttscydlist.length;i++){
                    ttdilist[i]=data.ttscydlist[i].caiyangdian;
                    ttarr[i]={
                        shiji:data.ttscydlist[i].shiji,
                        heji:0,
                        xianchang:0,
                        xianshang:0
                    }
                }
                for(var i=0;i<data.ttcydlist.length;i++){
                    var ix=ttdilist.indexOf(data.ttcydlist[i].caiyangdian);
                    if(ix==-1){
                        continue;
                    }
                    ttarr[ix].heji=data.ttcydlist[i].heji
                    ttarr[ix].xianchang=data.ttcydlist[i].xianchang;
                    ttarr[ix].xianshang=data.ttcydlist[i].xianshang;
                }
                xianshangzong=0;
                xianchangzong=0;
                hejizong=0;
                shijizong=0;
                for (var i=0;i<ttdilist.length;i++){
                    if(ttdilist[i] && ttdilist[i].indexOf("(")>-1){
                        ttdilist[i]=ttdilist[i].substr(0,ttdilist[i].indexOf("("));
                    }
                    if(ttdilist[i]&&ttdilist[i].length>10){
                        ttdilist[i]=ttdilist[i].substr(0,7);
                    }
                    xianshangzong+=ttarr[i].xianshang;
                    xianchangzong+=ttarr[i].xianchang;
                    hejizong+=ttarr[i].heji;
                    shijizong+=ttarr[i].shiji;
                    tthtml+="<tr><td>"+ttdilist[i]+"</td><td>"+ttarr[i].xianshang+"</td><td>"+ttarr[i].xianchang+"</td><td>"+ttarr[i].heji+"</td><td>"+ttarr[i].shiji+"</td></tr>";
                }
                tthtml+="<tr><td>合计：</td><td>"+xianshangzong+"</td><td>"+xianchangzong+"</td><td>"+hejizong+"</td><td>"+shijizong+"</td></tr>" +
                    "  </table>" +
                    "     </div>";
                $("#carousel").append(tthtml);


                //京东预约渠道
                var jdarr=[];
                var jddilist=[];
                var jdhtml="<div>" +
                    "      <h2>京东合作</h2><h2>"+startdate2+"—"+enddate2+"</h2>" +
                    "      <hr>" +
                    "      <table class='table'>" +
                    "       <tr><td>采样点</td><td>线上</td><td>现场</td><td>合计</td><td>实际</td></tr>";
                for(var i=0;i<data.jdscydlist.length;i++){
                    jddilist[i]=data.jdscydlist[i].caiyangdian;
                    jdarr[i]={
                        shiji:data.jdscydlist[i].shiji,
                        heji:0,
                        xianchang:0,
                        xianshang:0
                    }
                }
                for(var i=0;i<data.jdcydlist.length;i++){
                    var ix=jddilist.indexOf(data.jdcydlist[i].caiyangdian);
                    if(ix==-1){
                        continue;
                    }
                    jdarr[ix].heji=data.jdcydlist[i].heji
                    jdarr[ix].xianchang=data.jdcydlist[i].xianchang;
                    jdarr[ix].xianshang=data.jdcydlist[i].xianshang;
                }

                xianshangzong=0;
                xianchangzong=0;
                hejizong=0;
                shijizong=0;
                for (var i=0;i<jddilist.length;i++){
                    if(jddilist[i] && jddilist[i].indexOf("(")>-1){
                        jddilist[i]=jddilist[i].substr(0,jddilist[i].indexOf("("));
                    }
                    if(jddilist[i]&&jddilist[i].length>10){
                        jddilist[i]=jddilist[i].substr(0,7);
                    }
                    xianshangzong+=jdarr[i].xianshang;
                    xianchangzong+=jdarr[i].xianchang;
                    hejizong+=jdarr[i].heji;
                    shijizong+=jdarr[i].shiji;
                    jdhtml+="<tr><td>"+jddilist[i]+"</td><td>"+jdarr[i].xianshang+"</td><td>"+jdarr[i].xianchang+"</td><td>"+jdarr[i].heji+"</td><td>"+jdarr[i].shiji+"</td></tr>";
                }
                jdhtml+="<tr><td>合计：</td><td>"+xianshangzong+"</td><td>"+xianchangzong+"</td><td>"+hejizong+"</td><td>"+shijizong+"</td></tr>" +
                    "      </table>" +
                    "     </div>";
                $("#carousel").append(jdhtml);

                //华大渠道
                var hdarr=[];
                var hddilist=[];
                var hdhtml="<div>" +
                    "      <h2>华大合作</h2><h2>"+startdate2+"—"+enddate2+"</h2>" +
                    "      <hr>" +
                    "      <table class='table'>"+
                    "      <tr><td>采样点</td><td>线上</td><td>现场</td><td>合计</td><td>实际</td></tr>";
                for(var i=0;i<data.hdscydlist.length;i++){
                    hddilist[i]=data.hdscydlist[i].caiyangdian;
                    hdarr[i]={
                        shiji:data.hdscydlist[i].shiji,
                        heji:0,
                        xianchang:0,
                        xianshang:0
                    }
                }
                for(var i=0;i<data.hdcydlist.length;i++){
                    var ix=hddilist.indexOf(data.hdcydlist[i].caiyangdian);
                    if(ix==-1){
                        continue;
                    }
                    hdarr[ix].heji=data.hdcydlist[i].heji
                    hdarr[ix].xianchang=data.hdcydlist[i].xianchang;
                    hdarr[ix].xianshang=data.hdcydlist[i].xianshang;
                }
                xianshangzong=0;
                xianchangzong=0;
                hejizong=0;
                shijizong=0;
                for (var i=0;i<hddilist.length;i++){
                    if(hddilist[i] && hddilist[i].indexOf("(")>-1){
                        hddilist[i]=hddilist[i].substr(0,hddilist[i].indexOf("("));
                    }
                    if(hddilist[i]&&hddilist[i].length>10){
                        hddilist[i]=hddilist[i].substr(0,7);
                    }
                    xianshangzong+=hdarr[i].xianshang;
                    xianchangzong+=hdarr[i].xianchang;
                    hejizong+=hdarr[i].heji;
                    shijizong+=hdarr[i].shiji;
                    hdhtml+="<tr><td>"+hddilist[i]+"</td><td>"+hdarr[i].xianshang+"</td><td>"+hdarr[i].xianchang+"</td><td>"+hdarr[i].heji+"</td><td>"+hdarr[i].shiji+"</td></tr>";
                }
                hdhtml+="<tr><td>合计：</td><td>"+xianshangzong+"</td><td>"+xianchangzong+"</td><td>"+hejizong+"</td><td>"+shijizong+"</td></tr>" +
                    "      </table>" +
                    "     </div>";
                $("#carousel").append(hdhtml);


                $('#carousel').carousel({
                    curDisplay: 0, //默认索引
                    autoPlay: false, //是否自动播放
                    interval: 3000 //间隔时间
                });
                var hg=0;
                for(var i=0;i<$("#carousel div").length;i++){
                    if($("#carousel div:eq("+i+")").height()>hg){
                        hg=$("#carousel div:eq("+i+")").height();
                    }
                }
                $("#gequdao").height(hg+400);
            }
        });
    }
    cydnum();
    /*各采样点每日人数信息     end*/

    /*各采样点总人数          strat*/
    var c=10;
    function chakangengduo(){
        $(".liitem").slice(c,c+=10).show();
        if($("#geshu ul p").text()=="收起"){
            c=10;
            $(".liitem:gt(9)").hide();
            $("#geshu ul p").text("查看更多");
        }
        if($(".liitem").length<=c){
            $("#geshu ul p").text("收起");
        }
    }
    $.post({
        url:'sign!caiyangall.action',
        success:function (data) {
            var ff="";
            for(var i=0;i<data.data.length;i++){
                if(data.data[i].caiyangdian && data.data[i].caiyangdian.indexOf("(")>-1){
                    data.data[i].caiyangdian=data.data[i].caiyangdian.substr(0,data.data[i].caiyangdian.indexOf("("));
                }
                if(data.data[i].caiyangdian.length>10){
                    data.data[i].caiyangdian=data.data[i].caiyangdian.substr(0,7);
                }
                if(data.data[i].qudao=="团体"){
                    data.data[i].caiyangdian+="（团体）";
                }
                ff+="<li class='liitem'><span>"+(i+1)+"</span> <span>"+data.data[i].caiyangdian+"</span> <span class=\"personnum\">"+data.data[i].renshu+"</span><div></div></li>";
            }
            $("#geshu ul").append(ff);
            $("#geshu ul").append("<p style='margin:0px auto;text-align:center;' onclick='chakangengduo()'>查看更多</p>")

            $(".liitem:gt(9)").hide();

            // var multiple = new Multiple({
            //     selector: '.liitem',
            //     background: 'linear-gradient(#0141C5, #CBD9F3)'
            // });
        }
    });
    /*各采样点总人数          end*/

    /*根据渠道划分每日总人数         */
    // $("#startdate3").val(s1);
    // $("#enddate3").val(s1);
    $("#startdate3").attr("max",date.toJSON().substr(0,10));
    $("#enddate3").attr("max",date.toJSON().substr(0,10));
    var startdate3=$("#startdate3").val();
    var enddate3=$("#enddate3").val();
    var heji = echarts.init(document.getElementById('heji'));
    heji.showLoading();
    var xianshang = echarts.init(document.getElementById('xianshang'));
    xianshang.showLoading();
    var tuantiqudao = echarts.init(document.getElementById('tuantiqudao'));
    tuantiqudao.showLoading();
    var jingdongqudao = echarts.init(document.getElementById('jingdongqudao'));
    jingdongqudao.showLoading();
    var huadaqudao = echarts.init(document.getElementById('huadaqudao'));
    huadaqudao.showLoading();
    function query3(){
        startdate3=$("#startdate3").val();
        enddate3=$("#enddate3").val();
        yuyuenumbyqudao();
    }
    function yuyuenumbyqudao(){
        $.post({
            url:'sign!yuyuenumbyqudao.action',
            data:{"startdate":startdate3,"enddate":enddate3},
            success:function (data) {
                var hejiarr=[];
                var xianshangarr=[];
                var tuantiarr=[];
                var jingdongarr=[];
                var huadaarr=[];
                var datelist=[];
                var start=0;
                var end=0;
                for(var i=0;i<data.data.length;i++){
                    hejiarr[i]=data.data[i].heji;
                    xianshangarr[i]=data.data[i].xianshang;
                    tuantiarr[i]=data.data[i].tuanti;
                    jingdongarr[i]=data.data[i].jingdong;
                    huadaarr[i]=data.data[i].huada;
                    datelist[i]=data.data[i].yuyuedate;
                }
                //全部渠道表格
                heji.hideLoading();
                hejiarrdata={
                    "data": hejiarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = hejiarrdata.data.length - 15
                end = hejiarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : hejiarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  hejiarrdata.data
                    }
                };
                heji.setOption(option);
                //线上表格
                xianshang.hideLoading();
                xianshangarrdata={
                    "data": xianshangarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = xianshangarrdata.data.length - 15
                end = xianshangarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : xianshangarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  xianshangarrdata.data
                    }
                };
                xianshang.setOption(option);
                //团体渠道图表
                tuantiqudao.hideLoading();
                tuantiarrdata={
                    "data": tuantiarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = tuantiarrdata.data.length - 15
                end = tuantiarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : tuantiarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  tuantiarrdata.data
                    }
                };
                tuantiqudao.setOption(option);
                //京东渠道图表
                jingdongqudao.hideLoading();
                jingdongarrdata={
                    "data": jingdongarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = jingdongarrdata.data.length - 15
                end = jingdongarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : jingdongarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  jingdongarrdata.data
                    }
                };
                jingdongqudao.setOption(option);
                //华大合作渠道
                huadaqudao.hideLoading();
                jingdongarrdata={
                    "data": jingdongarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = jingdongarrdata.data.length - 15
                end = jingdongarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : jingdongarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  jingdongarrdata.data
                    }
                };
                huadaqudao.setOption(option);

                $('#carouse3').carousel({
                    curDisplay: 0, //默认索引
                    autoPlay: false, //是否自动播放
                    interval: 3000 //间隔时间
                });
                var hg=0;
                for(var i=0;i<$("#carouse3 div").length;i++){
                    if($("#carouse3>div:eq("+i+")").height()>hg){
                        hg=$("#carouse3>div:eq("+i+")").height();
                    }
                }
                $("#hejiechart").height(hg+400);

            }
        });
    }
    yuyuenumbyqudao();
    /*根据渠道划分每日总人数          end*/


    /*根据渠道划分每日实际采样人数         */
    // $("#startdate4").val(s1);
    // $("#enddate4").val(s1);
    $("#startdate4").attr("max",date.toJSON().substr(0,10));
    $("#enddate4").attr("max",date.toJSON().substr(0,10));
    var startdate4=$("#startdate4").val();
    var enddate4=$("#enddate4").val();
    var heji1 = echarts.init(document.getElementById('heji1'));
    heji1.showLoading();
    var xianshang1 = echarts.init(document.getElementById('xianshang1'));
    xianshang1.showLoading();
    var tuantiqudao1 = echarts.init(document.getElementById('tuantiqudao1'));
    tuantiqudao1.showLoading();
    var jingdongqudao1 = echarts.init(document.getElementById('jingdongqudao1'));
    jingdongqudao1.showLoading();
    var huadaqudao1 = echarts.init(document.getElementById('huadaqudao1'));
    huadaqudao1.showLoading();
    function query4(){
        startdate4=$("#startdate4").val();
        enddate4=$("#enddate4").val();
        shijinumbyqudao();
    }
    function shijinumbyqudao(){
        $.post({
            url:'sign!shijinumby.action',
            data:{"startdate":startdate4,"enddate":enddate4},
            success:function (data) {
                var hejiarr=[];
                var xianshangarr=[];
                var tuantiarr=[];
                var jingdongarr=[];
                var huadaarr=[];
                var datelist=[];
                var start=0;
                var end=0;
                for(var i=0;i<data.length;i++){
                    hejiarr[i]=data[i].heji;
                    xianshangarr[i]=data[i].xianshang;
                    tuantiarr[i]=data[i].tuanti;
                    jingdongarr[i]=data[i].jingdong;
                    huadaarr[i]=data[i].huada;
                    datelist[i]=data[i].samplebindtime;
                }
                //全部渠道表格
                heji1.hideLoading();
                hejiarrdata={
                    "data": hejiarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = hejiarrdata.data.length - 15
                end = hejiarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : hejiarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  hejiarrdata.data
                    }
                };
                heji1.setOption(option);
                //线上表格
                xianshang1.hideLoading();
                xianshangarrdata={
                    "data": xianshangarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = xianshangarrdata.data.length - 15
                end = xianshangarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : xianshangarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  xianshangarrdata.data
                    }
                };
                xianshang1.setOption(option);
                //团体渠道图表
                tuantiqudao1.hideLoading();
                tuantiarrdata={
                    "data": tuantiarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = tuantiarrdata.data.length - 15
                end = tuantiarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : tuantiarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  tuantiarrdata.data
                    }
                };
                tuantiqudao1.setOption(option);
                //京东渠道图表
                jingdongqudao1.hideLoading();
                jingdongarrdata={
                    "data": jingdongarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = jingdongarrdata.data.length - 15
                end = jingdongarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : jingdongarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  jingdongarrdata.data
                    }
                };
                jingdongqudao1.setOption(option);
                //华大合作渠道
                huadaqudao1.hideLoading();
                jingdongarrdata={
                    "data": jingdongarr,
                    "delta": [
                        null
                    ],
                    "names": datelist
                };
                start = jingdongarrdata.data.length - 15
                end = jingdongarrdata.data.length - 1
                option = {
                    color:['#417CE0'],
                    tooltip : {
                        trigger: 'item',
                        "textStyle": {
                            "fontSize": 40
                        }
                    },
                    legend: {
                        data: "总计",
                        textStyle: {
                            "fontSize": 35
                        },
                        "left": "left",
                    },
                    grid: {
                        top: '5%',
                        left: '1%',
                        containLabel: true
                    },
                    xAxis: [
                        {
                            type : 'category',
                            data : jingdongarrdata.names,
                            axisLabel:{textStyle: {
                                    "fontSize": 35
                                },}
                        }
                    ],
                    yAxis: [
                        {
                            type : 'value',
                            axisLabel: {
                                textStyle: {
                                    "fontSize": 30
                                },
                                formatter: function (a) {
                                    a = +a;
                                    return isFinite(a)
                                        ? echarts.format.addCommas(+a)
                                        : '';
                                }
                            }
                        }
                    ],
                    dataZoom: [
                        {
                            type: 'slider',
                            show: true,
                            startValue: start,
                            endValue: end,
                            handleSize: 8,
                        },
                        {
                            type: 'inside',
                            start: 94,
                            end: 100,
                        }
                    ],
                    series : {
                        name: '总计',
                        type: 'bar',
                        label: {
                            show: true,
                            position: 'inside',
                            "textStyle": {
                                "fontSize": 28
                            }
                        },
                        data:  jingdongarrdata.data
                    }
                };
                huadaqudao1.setOption(option);

                $('#carouse4').carousel({
                    curDisplay: 0, //默认索引
                    autoPlay: false, //是否自动播放
                    interval: 3000 //间隔时间
                });
                var hg=0;
                for(var i=0;i<$("#carouse4 div").length;i++){
                    if($("#carouse4>div:eq("+i+")").height()>hg){
                        hg=$("#carouse4>div:eq("+i+")").height();
                    }
                }

                $("#shijiechart").height(hg+400);

            }
        });
    }
    shijinumbyqudao();
    /*根据渠道划分每日实际采样人数          end*/






</script>
</html>
