function addToCart(teaId){
	$.ajax({
		url:"CartServlet?action=add",
		dataType:"json",
		async:true,
		data:{"teaId":teaId},
		type:"POST",
		success:function(data){
			$("#cart .num").html(data);
		}
			
	})
}



