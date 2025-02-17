<%@ page language="java" pageEncoding="gb2312"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- ����3��meta��ǩ*����*������ǰ�棬�κ��������ݶ�*����*������� -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="./img/asset-favico.ico">
    <title>������Ʊ</title>
    <link rel="stylesheet" href="./css/bkfp.css" />
    <link rel="stylesheet" href="./plugins/elementui/index.css" />
    <script src="./plugins/jquery/dist/jquery.min.js"></script>
    <script src="./plugins/healthmobile.js"></script>
    <script src="./plugins/datapicker/datePicker.js"></script>
    <script src="./plugins/vue/vue.js"></script>
    <script src="./plugins/vue/axios-0.18.0.js"></script>
    <script src="./plugins/elementui/index.js"></script>
    <script>
        var samplenum = getUrlParam("samplenum");//�ӵ�ַ����̬��ȡ�������
        var outtradeno = getUrlParam("outtradeno");//�̻����
        var isxianshang = getUrlParam("isxianshang");//����
        var sfz =getUrlParam("sfz");
        var name = getUrlParam("name");
        var yuyueid= getUrlParam("yuyueid");


    </script>
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div id="app" class="app">
    <!-- ҳ��ͷ�� -->
    <div class="top-header">
        <span class="f-left">

            <h2><a href="http://xinguan.scisoon.cn/xinguan/index.php/Appointment/table"><</a></h2>

        </span>
    </div>
    <!-- ҳ������ -->
    <div class="contentBox">
        <div class="form-info">
            <div class="info-title">
                <span class="name"></span>
            </div>
            <form class="info-form">
                <div class="input-row">
                    <label>�������</label>
                    <%--<p>{{samplenum}}</p>--%>
                    <input v-model="orderInfo.samplenum" type="text" class="input-clear" readonly="readonly">
                </div>


                    <%--<p>{{samplenum}}</p>--%>
                    <input v-model="orderInfo.yuyueid" type="hidden" class="input-clear" readonly="readonly" >

                <%--<div class="input-row">--%>
                    <%--<label>����</label>--%>
                    <%--<input v-model="orderInfo.name" type="text" class="input-clear" placeholder="����������">--%>
                <%--</div>--%>
                <%--<div class="input-row">--%>
                    <%--<label>���֤��</label>--%>
                    <%--<input v-model="orderInfo.sfz" type="text" class="input-clear" placeholder="���������֤��">--%>
                <%--</div>--%>
                <div class="input-row" v-cloak>
                    <label>�̻�����</label>
                    <input v-model="orderInfo.outtradeno" type="text" class="input-clear"  placeholder="�̻�����">

                </div>
                <%--<div class="input-row">--%>
                    <%--<input v-model="orderInfo.xs"  class="input-clear" type="hidden" disabled="disabled">--%>
                <%--</div>--%>
                <div class="input-row">
                    <label>�ֻ���</label>
                    <input v-model="orderInfo.telephone" type="text" class="input-clear" placeholder="�������ֻ���">
                    <input style="font-size: x-small;" id="validateCodeButton"  @click="sendValidateCode()" type="button" value="������֤��">
                </div>
                <div class="input-row">
                    <label>��֤��</label>
                    <input v-model="orderInfo.validate" type="text" class="input-clear" placeholder="��������֤��">
                </div>

                <template>
                    <el-tabs >
                        <el-tab-pane label="���˷�Ʊ" v-model="orderInfo.isfapiao" name="first">

                            <div class="input-row">
                                <label>��Ʊ̧ͷ</label>
                                <input v-model="orderInfo.fapiaotaitou"  value="fptt1"  type="text" class="input-clear" placeholder="�����뷢Ʊ̧ͷ">
                            </div>
                            <div class="radio-list1">
                                <div class="radio1">
                                    <input v-model="orderInfo.fapiaotype" id="item1" type="radio" value="1" checked>
                                    <label for="item1"></label>
                                    <span>���ӷ�Ʊ</span>
                                </div>
                            </div>
                            <div class="input-row">
                                <label>��������</label>
                                <input v-model="orderInfo.accessemail" type="text" class="input-clear" placeholder="���������������">
                            </div>
                        </el-tab-pane>
                        <el-tab-pane  label="��λ��Ʊ" name="second"  v-model="orderInfo.isfapiao">



                            <div class="input-row">
                                <label>��Ʊ̧ͷ</label>
                                <input v-model="orderInfo.fapiaotaitou" value="fptt"  type="text" class="input-clear" placeholder="�����뷢Ʊ̧ͷ">
                            </div>
                            <div class="input-row">
                                <label>˰��</label>
                                <input v-model="orderInfo.shuihao" type="text" class="input-clear" placeholder="������˰��">
                            </div>
                            <div class="input-row">
                                <label>ע���ַ</label>
                                <input v-model="orderInfo.zhucedizhi" type="text" class="input-clear" placeholder="������ע���ַ">
                            </div>
                            <div class="input-row">
                                <label>ע��绰</label>
                                <input v-model="orderInfo.zhucedianhua" type="text" class="input-clear" placeholder="������ע��绰">
                            </div>
                            <div class="input-row">
                                <label>��������</label>
                                <input v-model="orderInfo.kaihuyinhang" type="text" class="input-clear" placeholder="�����뿪������">
                            </div>
                            <div class="input-row">
                                <label>�����˺�</label>
                                <input v-model="orderInfo.yinhangzhanghao" type="text" class="input-clear" placeholder="�����������˺�">
                            </div>
                            <div class="radio-list1">
                                <div class="radio1">
                                    <input v-model="orderInfo.fapiaotype" id="item2" type="radio" value="1" checked>
                                    <label for="item1"></label>
                                    <span>���ӷ�Ʊ</span>
                                </div>
                            </div>
                            <div class="input-row">
                                <label>��������</label>
                                <input v-model="orderInfo.accessemail" type="text" class="input-clear" placeholder="�������������">
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </template>
            </form>
            <div class="box-button">
                <button @click="submitOrder()" type="button" class="btn order-btn">���뿪Ʊ</button>
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
                outtradeno:outtradeno,//�̻�����
                xs:isxianshang,   //�Ƿ�����
                sfz:sfz,         //���֤
                name:name,      //����
                samplenum:samplenum,
                yuyueid:yuyueid,
            }
        },
        methods:{//
            //������֤��
            sendValidateCode(){
                //��ȡ�û�������ֻ���
                var telephone = this.orderInfo.telephone;
                //���û�������ֻ��Ž���У��
                if(!checkTelephone(telephone)){
                    this.$message.error('��������ȷ���ֻ���');
                    //У�鲻ͨ��
                    return ;
                }
                validateCodeButton = $("#validateCodeButton")[0];
                //������ʱ����������30�뵹��ʱЧ��
                clock = window.setInterval(doLoop,1000);
                //����ajax�����ں�̨ϵͳΪ�û��ֻ��ŷ�����֤��
                $.get("bkfp!send4Order.action",{"telephone":telephone}).then((response) => {
                    if(!response.data.flag){
                        //��֤�뷢��ʧ��
                        this.$message.error('��֤�뷢��ʧ�ܣ������ֻ��������Ƿ���ȷ');
                    }
                });
            },

            //�ύ��Ʊ
            submitOrder(){


                //����ajax�����ύ������
                $.post("bkfp!save.action",this.orderInfo).then((response) => {

                    if(response.o == '��'){

                        //��Ʊ�ɹ���ת��ҳ
                        window.location.href="http://xinguan.scisoon.cn/xinguan/index.php/Appointment/table";
                    }else{
                        //ʧ��
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