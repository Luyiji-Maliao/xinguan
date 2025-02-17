<%@ page language="java" pageEncoding="gb2312"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="./img/asset-favico.ico">
    <title>补开发票</title>
    <link rel="stylesheet" href="./css/bkfp.css" />
    <link rel="stylesheet" href="./plugins/elementui/index.css" />
    <script src="./plugins/jquery/dist/jquery.min.js"></script>
    <script src="./plugins/healthmobile.js"></script>
    <script src="./plugins/datapicker/datePicker.js"></script>
    <script src="./plugins/vue/vue.js"></script>
    <script src="./plugins/vue/axios-0.18.0.js"></script>
    <script src="./plugins/elementui/index.js"></script>
    <script>
        var samplenum = getUrlParam("samplenum");//从地址栏动态获取样本编号
        var outtradeno = getUrlParam("outtradeno");//商户编号
        var isxianshang = getUrlParam("isxianshang");//线上
        var sfz =getUrlParam("sfz");
        var name = getUrlParam("name");
        var yuyueid= getUrlParam("yuyueid");


    </script>
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div id="app" class="app">
    <!-- 页面头部 -->
    <div class="top-header">
        <span class="f-left">

            <h2><a href="http://xinguan.scisoon.cn/xinguan/index.php/Appointment/table"><</a></h2>

        </span>
    </div>
    <!-- 页面内容 -->
    <div class="contentBox">
        <div class="form-info">
            <div class="info-title">
                <span class="name"></span>
            </div>
            <form class="info-form">
                <div class="input-row">
                    <label>样本编号</label>
                    <%--<p>{{samplenum}}</p>--%>
                    <input v-model="orderInfo.samplenum" type="text" class="input-clear" readonly="readonly">
                </div>


                    <%--<p>{{samplenum}}</p>--%>
                    <input v-model="orderInfo.yuyueid" type="hidden" class="input-clear" readonly="readonly" >

                <%--<div class="input-row">--%>
                    <%--<label>姓名</label>--%>
                    <%--<input v-model="orderInfo.name" type="text" class="input-clear" placeholder="请输入姓名">--%>
                <%--</div>--%>
                <%--<div class="input-row">--%>
                    <%--<label>身份证号</label>--%>
                    <%--<input v-model="orderInfo.sfz" type="text" class="input-clear" placeholder="请输入身份证号">--%>
                <%--</div>--%>
                <div class="input-row" v-cloak>
                    <label>商户单号</label>
                    <input v-model="orderInfo.outtradeno" type="text" class="input-clear"  placeholder="商户单号">

                </div>
                <%--<div class="input-row">--%>
                    <%--<input v-model="orderInfo.xs"  class="input-clear" type="hidden" disabled="disabled">--%>
                <%--</div>--%>
                <div class="input-row">
                    <label>手机号</label>
                    <input v-model="orderInfo.telephone" type="text" class="input-clear" placeholder="请输入手机号">
                    <input style="font-size: x-small;" id="validateCodeButton"  @click="sendValidateCode()" type="button" value="发送验证码">
                </div>
                <div class="input-row">
                    <label>验证码</label>
                    <input v-model="orderInfo.validate" type="text" class="input-clear" placeholder="请输入验证码">
                </div>

                <template>
                    <el-tabs >
                        <el-tab-pane label="个人发票" v-model="orderInfo.isfapiao" name="first">

                            <div class="input-row">
                                <label>发票抬头</label>
                                <input v-model="orderInfo.fapiaotaitou"  value="fptt1"  type="text" class="input-clear" placeholder="请输入发票抬头">
                            </div>
                            <div class="radio-list1">
                                <div class="radio1">
                                    <input v-model="orderInfo.fapiaotype" id="item1" type="radio" value="1" checked>
                                    <label for="item1"></label>
                                    <span>电子发票</span>
                                </div>
                            </div>
                            <div class="input-row">
                                <label>接收邮箱</label>
                                <input v-model="orderInfo.accessemail" type="text" class="input-clear" placeholder="请输入身接收邮箱">
                            </div>
                        </el-tab-pane>
                        <el-tab-pane  label="单位发票" name="second"  v-model="orderInfo.isfapiao">



                            <div class="input-row">
                                <label>发票抬头</label>
                                <input v-model="orderInfo.fapiaotaitou" value="fptt"  type="text" class="input-clear" placeholder="请输入发票抬头">
                            </div>
                            <div class="input-row">
                                <label>税号</label>
                                <input v-model="orderInfo.shuihao" type="text" class="input-clear" placeholder="请输入税号">
                            </div>
                            <div class="input-row">
                                <label>注册地址</label>
                                <input v-model="orderInfo.zhucedizhi" type="text" class="input-clear" placeholder="请输入注册地址">
                            </div>
                            <div class="input-row">
                                <label>注册电话</label>
                                <input v-model="orderInfo.zhucedianhua" type="text" class="input-clear" placeholder="请输入注册电话">
                            </div>
                            <div class="input-row">
                                <label>开户银行</label>
                                <input v-model="orderInfo.kaihuyinhang" type="text" class="input-clear" placeholder="请输入开户银行">
                            </div>
                            <div class="input-row">
                                <label>银行账号</label>
                                <input v-model="orderInfo.yinhangzhanghao" type="text" class="input-clear" placeholder="请输入银行账号">
                            </div>
                            <div class="radio-list1">
                                <div class="radio1">
                                    <input v-model="orderInfo.fapiaotype" id="item2" type="radio" value="1" checked>
                                    <label for="item1"></label>
                                    <span>电子发票</span>
                                </div>
                            </div>
                            <div class="input-row">
                                <label>接收邮箱</label>
                                <input v-model="orderInfo.accessemail" type="text" class="input-clear" placeholder="请输入接收邮箱">
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </template>
            </form>
            <div class="box-button">
                <button @click="submitOrder()" type="button" class="btn order-btn">申请开票</button>
            </div>
        </div>
    </div>
</div>
<script>
    var vue = new Vue({
        el:'#app',
        data:{
            orderInfo:{
                fapiaotype:1,
                isfapiao:'1',
                outtradeno:outtradeno,//商户单号
                xs:isxianshang,   //是否线上
                sfz:sfz,         //身份证
                name:name,      //姓名
                samplenum:samplenum,
                yuyueid:yuyueid,
            }
        },
        methods:{//
            //发送验证码
            sendValidateCode(){
                //获取用户输入的手机号
                var telephone = this.orderInfo.telephone;
                //对用户输入的手机号进行校验
                if(!checkTelephone(telephone)){
                    this.$message.error('请输入正确的手机号');
                    //校验不通过
                    return ;
                }
                validateCodeButton = $("#validateCodeButton")[0];
                //创建定时器对象，用于30秒倒计时效果
                clock = window.setInterval(doLoop,1000);
                //发送ajax请求，在后台系统为用户手机号发送验证码
                $.get("bkfp!send4Order.action",{"telephone":telephone}).then((response) => {
                    if(!response.data.flag){
                        //验证码发送失败
                        this.$message.error('验证码发送失败，请检查手机号输入是否正确');
                    }
                });
            },

            //提交开票
            submitOrder(){


                //发送ajax请求，提交表单参数
                $.post("bkfp!save.action",this.orderInfo).then((response) => {

                    if(response.o == '是'){

                        //开票成功跳转首页
                        window.location.href="http://xinguan.scisoon.cn/xinguan/index.php/Appointment/table";
                    }else{
                        //失败
                        this.$message.error(response.msg);
                    }
                });

            }
        },

    });
</script>
<script>

</script>
</body>
</html>