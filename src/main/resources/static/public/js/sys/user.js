$(function(){
    var option = {
        url: '../sys/user/list',
        pagination: true,	//显示分页条
        sidePagination: 'server',//服务器端分页
        showRefresh: true,  //显示刷新按钮
        search: true,
        toolbar: '#toolbar',
        striped : true,     //设置为true会有隔行变色效果
        //idField: 'userId',
        columns: [
            {
                field: 'userId',
                title: '序号',
                width: 40,
                formatter: function(value, row, index) {
                    var pageSize = $('#table').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#table').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {checkbox:true},
            { title: '用户ID', field: 'userId',sortable:true},
            { title: '用户名', field: 'username'},
            { title: '密码', field: 'password',formatter: function(value){
                    return '******';
                }},
            { title: '邮箱', field: 'email'},
            { title: '手机号', field: 'mobile'},
            { title: '状态', field: 'status',formatter: function(value){
                    if(value === 0){
                        return '<span class="label label-primary">冻结</span>';
                    }
                    if(value === 1){
                        return '<span class="label label-success">正常</span>';
                    }

                }

            }
        ]};
    $('#table').bootstrapTable(option);
});
var ztree;

var vm = new Vue({
	el:'#dtapp',
    data:{
        showList: true,
        title: null,
        user:{}
    },
    methods:{

        del: function(){
            var rows = getSelectedRows();
            if(rows == null){
                return ;
            }
            var id = 'userId';
            //提示确认框
            layer.confirm('您确定要删除所选数据吗？', {
                btn: ['确定', '取消'] //可以无限个按钮
            }, function(index, layero){
                var ids = new Array();
                //遍历所有选择的行数据，取每条数据对应的ID
                $.each(rows, function(i, row) {
                    ids[i] = row[id];
                });

                $.ajax({
                    type: "POST",
                    url: "user/del",
                    data: JSON.stringify(ids),
                    success : function(r) {
                        if(r.code === 0){
                            layer.alert('删除成功');
                            $('#table').bootstrapTable('refresh');
                        }else{
                            layer.alert(r.msg);
                        }
                    },
                    error : function() {
                        layer.alert('服务器没有返回数据，可能服务器忙，请重试');
                    }
                });
            });
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.user = {parentName:null,parentId:0,type:1,orderNum:0};
            vm.getMenu();
        },
        update: function (event) {
            var id = 'userId';
            var userId = getSelectedRow()[id];
            if(userId == null){
                return ;
            }

            $.get("../sys/user/info/"+userId, function(r){
                vm.showList = false;
                vm.title = "修改";
                vm.user = r.user;

                vm.getMenu();
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.user.userId == null ? "../sys/user/save" : "../sys/user/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.user),
                success: function(r){
                    if(r.code === 0){
                        layer.alert('操作成功', function(index){
                            layer.close(index);
                            vm.reload();
                        });
                    }else{
                        layer.alert(r.msg);
                    }
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            $("#table").bootstrapTable('refresh');
        },
        getMenu: function(userId){

            var setting = {
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "userId",
                        pIdKey: "parentId",
                        rootPId: -1
                    },
                    key: {
                        url:"nourl"
                    }
                }
            };


        },
        exports: function(){
            console.log("导出！");
            location.href="http://127.0.0.1:8080/exportExcel";
        }
    }
});