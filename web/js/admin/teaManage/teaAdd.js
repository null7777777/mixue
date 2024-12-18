$(function(){
	var form=$("#teaAddForm").Validform({
		tiptype:2,//validform初始化
	});
	
	form.addRule([
		{
			ele:"#teaName",
		    datatype:"*2-20",
		    ajaxurl:"jsp/admin/TeaManageServlet?action=find",
		    nullmsg:"请输入商品名称！",
		    errormsg:"商品名称至少2个字符,最多20个字符！" 
		},
		{ 
			ele:"#catalog",
			datatype:"*",
			nullmsg:"请选择商品分类！",
			errormsg:"请选择商品分类！"
		},
		{
			ele:"#price",
			datatype:"/^[0-9]{1,}([.][0-9]{1,2})?$/",
			mullmsg:"价格不能为空！",
			errormsg:"价格只能为数字"
		},
		
		{
			ele:"#teaImg",
		    datatype:"*",
		    nullmsg:"请上传商品图片！",
		    errormsg:"请上传图书图片！"
		}
	
	]);
	
});

