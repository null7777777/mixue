package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.Tea;
import  bean.PageBean;
import  dao.TeaDao;
import  util.DateUtil;
import  util.DbUtil;

public class TeaDaoImpl implements TeaDao {

	//分页查询商品的数据集
	@Override
	public List<Tea> teaList(PageBean pageBean) {
		List<Tea> list = new ArrayList<>();

		String sql = "select * from view_tea limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, (pageBean.getCurPage() - 1) * pageBean.getMaxSize(),
				pageBean.getMaxSize());

		// 把查询的tea结果由List<Map<String, Object>>转换为List<Tea>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Tea tea = new Tea(map);
				list.add(tea);
			}
		}
		return list;
	}

	//商品总数的统计
	@Override
	public long teaReadCount() {
		String sql = "select count(*) as count from s_tea";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}

	//添加商品
	@Override
	public boolean teaAdd(Tea tea) {
		String sql = "insert into s_tea(teaName,catalogId,price,description,imgId,addTime) values(?,?,?,?,?,?)";

		int i = DbUtil.excuteUpdate(sql, tea.getTeaName(), tea.getCatalog().getCatalogId(),
				tea.getPrice(), tea.getDescription(), tea.getUpLoadImg().getImgId(),
				DateUtil.getTimestamp());

		return i > 0 ? true : false;
	}

	//通过商品id查找商品
	@Override
	public Tea findTeaById(int teaId) {
		String sql = "select * from view_tea where teaId=?";
		Tea tea = null;
		List<Map<String, Object>> list = DbUtil.executeQuery(sql, teaId);
		if (list.size() > 0) {
			tea = new Tea(list.get(0));
		}
		return tea;
	}

	//通过商品名查找商品
	@Override
	public boolean findTeaByTeaName(String teaName) {
		String sql = "select * from s_tea where teaName=?";
		List<Map<String, Object>> list = DbUtil.executeQuery(sql, teaName);
		return list.size() > 0 ? true : false;
	}

	@Override
    public boolean teaUpdate(Tea tea) {
        // 检查 teaId 是否有效
        if (tea.getTeaId() == 0) {
            System.out.println("teaId is required and cannot be 0.");
            return false;
        }

        // 构建动态 SQL 和参数列表
        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("UPDATE s_tea SET ");
        boolean hasUpdates = false;

        // 只有当 catalogId 不为 0 时才添加到 SQL 中
        if (tea.getCatalogId() != 0) {
            sqlBuilder.append("catalogId=?, ");
            params.add(tea.getCatalogId());
            hasUpdates = true;
        }

        // 只有当 price 不为 0.0 时才添加到 SQL 中
        if (tea.getPrice() != 0.0) {
            sqlBuilder.append("price=?, ");
            params.add(tea.getPrice());
            hasUpdates = true;
        }

        // 只有当 description 不为 null 或空字符串时才添加到 SQL 中
        if (tea.getDescription() != null && !tea.getDescription().trim().isEmpty()) {
            sqlBuilder.append("description=?, ");
            params.add(tea.getDescription().trim());
            hasUpdates = true;
        }

        // 只有当 recommend 不为 null 时才添加到 SQL 中
        if (tea.isRecommend() == true || tea.isRecommend() == false) { // 假设 recommend 是 Boolean 类型
            sqlBuilder.append("recommend=?, ");
            params.add(tea.isRecommend());
            hasUpdates = true;
        }

        // 如果没有需要更新的字段，直接返回 false
        if (!hasUpdates) {
            System.out.println("No fields to update for teaId: " + tea.getTeaId());
            return false;
        }

        // 移除最后一个多余的逗号和空格
        sqlBuilder.setLength(sqlBuilder.length() - 2);

        // 添加 WHERE 子句
        sqlBuilder.append(" WHERE teaId=?");
        params.add(tea.getTeaId());
        
        // 执行更新操作
        String sql = sqlBuilder.toString();
        
        
        System.out.println("Executing SQL: " + sql);
        for (int i = 0; i < params.size(); i++) {
            System.out.println("Parameter " + (i + 1) + ": " + params.get(i));
        }

        int rowsAffected = DbUtil.excuteUpdate(sql, params.toArray());

        // 返回是否成功更新
        return rowsAffected > 0;
    }
	
	

	//商品的删除操作
	@Override
	public boolean teaDelById(int teaId) {
		String sql = "delete from s_tea where teaId=?";
		int i = DbUtil.excuteUpdate(sql, teaId);
		return i > 0 ? true : false;
	}

	//商品的批量查询
	@Override
	public String findimgIdByIds(String ids) {
		String imgIds = "";
		String sql = "select imgId from s_tea where teaId in(" + ids + ")";
		List<Map<String, Object>> list = DbUtil.executeQuery(sql);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i != list.size() - 1) {
					imgIds += list.get(i).get("imgId") + ",";
				} else {
					imgIds += list.get(i).get("imgId");
				}
			}
		}
		return imgIds;
	}

	//批量删除
	@Override
	public boolean teaBatDelById(String ids) {
		String sql = "delete from s_tea where teaId in(" + ids + ")";
		int i = DbUtil.excuteUpdate(sql);
		return i > 0 ? true : false;
	}

	// 随机查询一定数量的商品
	@Override
	public List<Tea> teaList(int num) {
		List<Tea> list = new ArrayList<>();
		String sql = "select * from view_tea order by rand() LIMIT ?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, num);
		// 把查询的tea结果由List<Map<String, Object>>转换为List<Tea>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Tea tea = new Tea(map);
				list.add(tea);
			}
		}
		return list;
	}

	//查询指定数量的商品
	@Override
	public List<Tea> newTeas(int num) {
		List<Tea> list = new ArrayList<>();
		String sql = "SELECT * FROM view_tea ORDER BY addTime desc limit 0,?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, num);
		// 把查询的tea结果由List<Map<String, Object>>转换为List<Tea>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Tea tea = new Tea(map);
				list.add(tea);
			}
		}
		return list;
	}

	//按分类id统计商品数量
	@Override
	public long teaReadCount(int catalogId) {
		String sql = "select count(*) as count from s_tea where catalogId=?";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, catalogId);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}

	//按分类id获取商品列表
	@Override
	public List<Tea> teaList(PageBean pageBean, int catalogId) {
		List<Tea> list = new ArrayList<>();
		String sql = "select * from view_tea where catalogId=? limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, catalogId,
				(pageBean.getCurPage() - 1) * pageBean.getMaxSize(), pageBean.getMaxSize());
		// 把查询的tea结果由List<Map<String, Object>>转换为List<Tea>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Tea tea = new Tea(map);
				list.add(tea);
			}
		}
		return list;
	}
	
	//按分类id和商品名获取商品列表
	@Override
	public List<Tea> teaList(PageBean pageBean, String teaname) {
		List<Tea> list = new ArrayList<>();

		String sql = "select * from view_tea where teaName like '%"+teaname+"%' limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql,
				(pageBean.getCurPage() - 1) * pageBean.getMaxSize(), pageBean.getMaxSize());

		// 把查询的tea结果由List<Map<String, Object>>转换为List<Tea>
		if (lm.size() > 0) {
			for (Map<String, Object> map : lm) {
				Tea tea = new Tea(map);
				list.add(tea);
			}
		}
		return list;
	}
	//根据商品名统计商品数量
	@Override
	public long teaReadCount(String teaname) {
		String sql = "select count(*) as count from s_tea where teaName like '%"+teaname+"%'";
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql);
		return lm.size() > 0 ? (long) lm.get(0).get("count") : 0;
	}
	
	// 轮播图路径
	@Override
    public List<String> findRecommendTeaImages() {
        String sql = "SELECT i.imgSrc " +
                     "FROM ((SELECT imgId FROM s_tea " +
                     "       WHERE recommend = true " +
                     "       ORDER BY updateTime DESC) " +
                     "      UNION " +
                     "      (SELECT imgId FROM s_tea " +
                     "       WHERE recommend = false " +
                     "       ORDER BY updateTime DESC " +
                     "       LIMIT 3) " +
                     "      LIMIT 3) AS t " +
                     "JOIN s_uploadimg i ON t.imgId = i.imgId";
        
        List<Map<String, Object>> list = null;
        list = DbUtil.executeQuery(sql);
        
        // Extract the imgSrc from the result set
        List<String> imagePaths = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> row : list) {
                imagePaths.add((String) row.get("imgSrc"));
            }
        }
        
        return imagePaths;
    }

}
