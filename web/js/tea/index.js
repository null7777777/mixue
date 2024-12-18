$.ajax({
	url:"ShopIndex",
	dataType:"json",
	async:true,
	data:{},
	type:"POST",
	success:function(data){
		datalist(data);
	}
		
})

function datalist(data){
	
	
	//推荐商品
	if(data.recTeas!=null){
		$.each(data.recTeas,function(i,n){
			var tag="<li class='col-md-3'><div class='list'>" +
			"<a href='teadetail?teaId="+n.teaId+"'><img class='img-responsive' src='"+n.upLoadImg.imgSrc+"'/></a>"+
			"<div class='proinfo'><h2><a class='text-center' href='teadetail?teaId="+n.teaId+"'>"+n.teaName+"</a></h2>"+
			"<p><i>"+n.price+"</i><a class='btn btn-danger btn-xs' href='javascript:void(0)' onclick='addToCart("+n.teaId+")' " +
				"data-toggle='modal' data-target='.bs-example-modal-sm'>ADD</a></p></div></div></li>";
			
			$("#recTeas ul").append(tag);
		})
	}
	
	//新增加的书
	if(data.newTeas!=null){
		$.each(data.newTeas,function(i,n){
			var tag="<li class='col-md-3'><div class='list'>" +
			"<a href='teadetail?teaId="+n.teaId+"'><img class='img-responsive' src='"+n.upLoadImg.imgSrc+"'/></a>"+
			"<div class='proinfo'><h2><a class='text-center' href='teadetail?teaId="+n.teaId+"'>"+n.teaName+"</a></h2>"+
			"<p><i>"+n.price+"</i><a class='btn btn-danger btn-xs' href='javascript:void(0)' onclick='addToCart("+n.teaId+")' " +
				"data-toggle='modal' data-target='.bs-example-modal-sm'>ADD</a></p></div></div></li>";
			
			$("#newTeas ul").append(tag);
			
		})
	}
	
	
}
