<%--
  Created by IntelliJ IDEA.
  User: jianzhiping
  Date: 2020/5/6
  Time: 9:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <title>分销查询</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="分销查询">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <meta name="viewport" content=" maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vant@2.0/lib/index.css">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <script>(function(a,d){var b=a.documentElement,e="orientationchange"in window?"orientationchange":"resize",c=function(){var a=b.clientWidth;a&&(b.style.fontSize=640<a?"40px":a/640*40+"px")};a.addEventListener&&(d.addEventListener(e,c,!1),a.addEventListener("DOMContentLoaded",c,!1))})(document,window);</script>
    <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="./js/moment.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>

    <link rel="stylesheet" href="css/xinguansign.css">
        <style>
            .van-cell{
                height:280px;
            }
        </style>
</head>
<body>
<block name="main-content"></block>
<br>
<div class="form-group">
    <h1>分销查询</h1>
    <%--<div class='datatimepicker'>
        <div class="form-group">
            <!--指定 date标记-->
            <div class='input-group date' id='datetimepicker2'>
                <input type='date' class="form-control" id="date" onchange="search()" />
            </div>
        </div>
    </div>
    <select name="select" id="select" class="form-control" onchange="select()">
        <option value="">全部采样点</option>
    </select>--%>
</div>
<div id="app" >
    <form action="/">
        <van-search
                onfocus="hide()"
                onblur="show()"
                v-model="value"
                placeholder="请输入合作机构名称或销售姓名"
        ></van-search>
    </form>
    <van-cell-group>
        <van-cell title="" value="" label="" ></van-cell>
    </van-cell-group>
    <van-button type="primary" id="pre" onclick="prev()" style="display: inline-block;margin-left:20px;">上一页</van-button>
    <%--<van-button type="primary" onclick="search()" id="sear" style="position: absolute;right:20px;bottom:10px;display:none;" >搜索</van-button>--%>
    <van-button type="primary" id="next" onclick="next()" style="display: inline-block;float: right;margin-right:20px;">下一页</van-button>
    <div id="page"  style="display: inline-block;font-size:30px">
        <ul class="list-inline" style="display: inline-block;margin-left:30px;">
            <li>1</li>
        </ul>
        <p style="display: inline-block;text-align: right">共<span style="color:green" id="totalpage">0</span>条</p>
    </div>
</div>
<div class="modal fade" id="loadingModal" data-backdrop="static" data-keyboard="false">
    <div style="width: 500px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 35%; top: 50%;margin-left:-100px;margin-top:-10px">
        <div class="progress progress-striped active" style="margin-bottom: 0;">
            <div class="progress-bar" style="width: 100%;"></div>
        </div>
        <h5 style="color:#fff;font-size:50px"> <strong>正在查询，请稍等！</strong> </h5>
    </div>
</div>
<div class="modal fade" id="pageModal" style="width: 100%;height:100%; z-index: 20000;margin-left:-100px;margin-top:-10px;font-size:30px;">
    <ul class="list-inline" style="display: inline-block;padding:30px 10px;background-color: #fff; position: absolute; text-align: center; left: 35%; top: 70%;">
        <li>1</li>
    </ul>
</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vant@2.0/lib/vant.min.js"></script>
<script>


    var valuea="";
    new Vue({
        el: '#app',
        data() {
            return {
                value: valuea,
            }
        },
        watch: {
            'value': function(newVal){
                valuea = this.value
            }
        }

    });
    // vant.value="cc"
</script>
<script>
    //分页所需参数
    var total=0;
    var start=0;
    var limit=10;
    var pagesize=1;
    var pageno=1;

    $(function () {

        $('#datetimepicker2').datetimepicker({
            format: 'YYYY-MM-DD',
            locale: moment.locale('zh-cn'),
            defaultDate: new Date(),
        });
        $.post({
            url:"sign!getdidianAll.action",
            success:function (data) {
                // data=JSON.parse(data);
                for (var i=0;i<data.length;i++){
                    var ht="<option value='"+data[i]+"'>"+data[i]+"</option>";
                    $("#select").append(ht);
                }
            }
        })
        init();
    })

    /*
        初始化与查询加载函数
    */
    function init() {
        //显示等待条
        $("#loadingModal").modal('show');
        $.post({
            url:"sign!salelist.action",
            data:{"start":start,"limit":limit,"name":valuea},
            success:function (data){
                //隐藏等待条 与页码选择框
                $("#loadingModal").modal('hide');
                $("#pageModal").modal('hide');
                $(".van-cell-group").html("");
                $("#page ul").html("");
                $("#pageModal ul").html("");
                //赋值条数 页码等信息
                total=data.total;
                pagesize=data.pagesize;
                pageno=data.pageno;
                $("#totalpage").text(total);
                var ht="";
                //如果总页数小于4页则直接显示
                if(pagesize<=4){
                    for(var i=0;i<pagesize;i++){
                        ht="<li onclick='liclick(this)'>"+(i+1)+"</li>";
                        $("#page ul").append(ht);
                    }
                    $("#page ul li:eq("+(pageno-1)+")").addClass("active");
                }else{
                    //总页数大于4页则使用省略号省略页码
                    if(pageno>=(pagesize-1)){
                        ht="<li onclick='liclick(this)'>"+1+"</li>";
                        ht+="<li onclick='dian()'>...</li>";
                        ht+="<li onclick='liclick(this)'>"+(pagesize-1)+"</li>";
                        ht+="<li onclick='liclick(this)'>"+pagesize+"</li>";
                        $("#page ul").append(ht);
                        if(pageno==(pagesize-1)){
                            $("#page ul li:eq(2)").addClass("active");
                        }else{
                            $("#page ul li:eq(3)").addClass("active");
                        }
                    }else{
                        ht="<li onclick='liclick(this)'>"+pageno+"</li>";
                        ht+="<li onclick='liclick(this)'>"+(pageno+1)+"</li>";
                        ht+="<li onclick='dian()'>...</li>";
                        ht+="<li onclick='liclick(this)'>"+pagesize+"</li>";
                        $("#page ul").append(ht);
                        $("#page ul li:eq(0)").addClass("active");
                    }
                }
                //页码选择框加载页码
                for(var i=0;i<pagesize;i++){
                    ht="<li onclick='liclick(this)'>"+(i+1)+"</li>";
                    $("#pageModal ul").append(ht);
                }
                $("#pageModal ul li:eq("+(pageno-1)+")").addClass("active");
                data=data.rows;
                //无数据显示无数据
                if(data.length<1){
                    var html="<div class=\"van-cell\"><div class=\"van-cell__title\"><span>无</span><div class=\"van-cell__label\">暂无数据</div></div><div class=\"van-cell__value\"><span>暂无数据</span></div></div>";
                    $(".van-cell-group").append(html);
                    return;
                }
                //循环加载数据
                for (var i=0;i<data.length;i++){
                    var html="<div class=\"van-cell\" onclick=\"hf('sign!customer.action?cooperationstr="+data[i].cooperationstr+"')\"><div class=\"van-cell__title\"><span>销售姓名："+data[i].salename+"<br/>业务员："+data[i].businessmanager+"</span><div class=\"van-cell__label\">合作机构："+data[i].cooperationcompanyname+"<br/><br/><br/>分支机构："+data[i].cooperationsubcompanyname+"</div></div><div class=\"van-cell__value\"><span>"+data[i].renshu+"人<br/>"+(data[i].yuyuenum==null?'':data[i].yuyuenum)+"</span></div></div>";
                    $(".van-cell-group").append(html);
                }
                //如果只有一个合作机构直接跳转到客户信息界面
                if(data.length==1){
                    hf("sign!customer.action?cooperationstr="+data[0].cooperationstr+"")
                }
            },error:function () {
                alert("错误，请联系管理员！");
            }
        })
    }
    function hf(hf){
        location.href=hf;
    }
    //
    // function hide(){
    //     $("#next").hide();
    //     $("#pre").hide();
    //     $("#sear").show();
    // }
    // function show(){
    //     $("#next").show();
    //     $("#pre").show();
    //     $("#sear").hide()
    //     init();
    // }
    /*
    搜索函数
    */
    function search() {
        pageno=1;
        start=pageno*limit-limit;
        init();
        // show();
    }
    function selecttitle() {
        window.location.href=$("#titleselect").val();
    }
    /*
    采样点下拉框选择函数
    */
    function select() {
        pageno=1;
        start=pageno*limit-limit;
        init()
    }
    /*
    页码点击事件
    */
    function liclick(ts) {
        if(pageno==$(ts).text()){
            return;
        }
        pageno=$(ts).text();
        start=pageno*limit-limit;
        init();
    }

    /*
    上一页
    */
    function prev() {
        start=pageno*limit-limit*2;
        if(pageno-1<1){
            return;
        }
        init();
    }

    /*
    下一页
    */
    function next() {
        start=pageno*limit;
        if(pageno+1>pagesize){
            return;
        }
        init();
    }
    /*
    多页码选择框
    */
    function dian(){
        $("#pageModal").modal('show');
    }

    /*
    监控回车事件提交搜索
    */
    document.onkeydown = function (e) { // 回车提交表单
        // 兼容FF和IE和Opera
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
        if (code == 13) {
            // $("#sear").trigger("click");
            search();
        }
    }
</script>
</html>
