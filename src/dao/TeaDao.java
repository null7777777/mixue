package dao;

import java.util.List;

import bean.Tea;

import bean.PageBean;


public interface TeaDao {
	// 获取商品总数
	long teaReadCount();
	
	//按商品名获取商品总数
	long teaReadCount(String teaname);
	
	public List<Tea> teaList(PageBean pageBean, String teaname);

	// 获取商品分页列表(视图)
	List<Tea> teaList(PageBean pageBean);

	// 按分类获取商品数量
	long teaReadCount(int catalogId);

	// 按分类获取商品分页列表(视图)
	List<Tea> teaList(PageBean pageBean, int catalogId);

	// 增加商品
	boolean teaAdd(Tea tea);

	// 根据商品id查找商品信息(视图)
	Tea findTeaById(int teaId);

	// 根据商品名称查找商品是否存在
	boolean findTeaByTeaName(String teaName);

	// 更新商品信息
	boolean teaUpdate(Tea tea);

	// 根据id删除商品
	boolean teaDelById(int teaId);

	// 根据id串查询图片id串
	String findimgIdByIds(String ids);

	// 批量删除商品
	boolean teaBatDelById(String ids);

	// 随机获取指定数量的商品
	List<Tea> teaList(int num);

	// 获取指定数量新添加的商品
	List<Tea> newTeas(int num);
	
	// 获取轮播图路径
	List<String> findRecommendTeaImages();

}
