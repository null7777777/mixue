package dao.impl;

import java.util.ArrayList;



import java.util.List;
import java.util.Map;

//import  bean.Admin;
import  bean.PageBean;
import  bean.User;
import  dao.UserDao;
import  util.DbUtil;


public class UserDaoImpl implements UserDao {

	//获取总用户记录数
	@Override
	public long teaReadCount() {
		long count=0;
		String sql="select count(*) as count from s_user";
		List<Map<String, Object>> lm=DbUtil.executeQuery(sql);
		if(lm.size()>0){
			count=(long) lm.get(0).get("count");
		}
		return count;
	}
	//根据用户名获取总记录数
	@Override
	public long teaReadCount(String username) {
		long count=0;
		String sql="select count(*) as count from s_user where userName like '%"+username+"%'";
		List<Map<String, Object>> lm=DbUtil.executeQuery(sql);
		if(lm.size()>0){
			count=(long) lm.get(0).get("count");
		}
		return count;
	}

	//分页获取用户列表
	@Override
	public List<User> userList(PageBean pageBean) {
		List<User> lu=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_user limit ?,?";
		
		list=DbUtil.executeQuery(sql,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				User u=new User(map);
				lu.add(u);
			}
		}
		return lu;
	}
	//根据用户名模糊查询获取用户列表（分页显示）
	@Override
	public List<User> userList(PageBean pageBean,String username) {
		List<User> lu=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_user where userName like '%"+username+"%' limit ?,?";
		
		list=DbUtil.executeQuery(sql,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				User u=new User(map);
				lu.add(u);
			}
		}
		return lu;
	}

	//根据用户名查找用户
	@Override
	public boolean findUser(String userName) {
		String sql="select * from s_user where userName=?";
		List<Map<String,Object>> list=DbUtil.executeQuery(sql, userName);
		return list.size()>0?true:false;
	}
	
	//用户的添加
	@Override
	public boolean userAdd(User user) {
		String sql="insert into s_user(userName,userPassWord,name,sex,age,tell,address,enabled) values(?,?,?,?,?,?,?,?)";
		
		int i= DbUtil.excuteUpdate(sql, user.getUserName(),user.getUserPassWord(),user.getName(),user.getSex(),user.getAge()
				,user.getTell(),user.getAddress(),user.getEnabled());
		
		return i>0?true:false;	
		
	}

	//查找指定id的用户信息
	@Override
	public User findUser(Integer id) {
		String sql="select * from s_user where userId=?";
		User u=null;
		List<Map<String,Object>> list=DbUtil.executeQuery(sql, id);
		if(list.size()>0) {
			u=new User(list.get(0));
		}
		return u;
	}

	//根据id更新用户信息
	@Override
	public boolean userUpdate(User user) {
		String sql="update s_user set userPassWord=?,name=?,sex=?,age=?,tell=?,address=?,enabled=? where userId =?";
		int i=DbUtil.excuteUpdate(sql,user.getUserPassWord(),user.getName(),user.getSex(),user.getAge()
				,user.getTell(),user.getAddress(),user.getEnabled(),user.getUserId());
		
		return i>0?true:false;
	}

	//根据id删除用户
	@Override
	public boolean delUser(int id) {
		String sql="delete from s_user where userId=?";
		int i=DbUtil.excuteUpdate(sql, id);
		return i>0?true:false;
	}

	//根据id集批量删除用户
	@Override
	public boolean batDelUser(String ids) {
		String sql="delete from s_user where userId in ("+ids+")";
		int i=DbUtil.excuteUpdate(sql);
		return i>0?true:false;
	}

	//用户登录
	@Override
	public User userLogin(User user) {
		User user1=null;
		String sql="select * from s_user where userName=? and userPassWord=?";
		List<Map<String, Object>> list = DbUtil.executeQuery(sql, user.getUserName(),user.getUserPassWord());
		if(list.size()>0) {
			Map<String, Object> map = list.get(0);
			user1=new User(map);
		}
		return user1;
	}
	
	@Override
	public User getUserByName(String userName) {
		String sql="select * from s_user where userName=? ";
		List<Map<String, Object>> resultMaps = DbUtil.executeQuery(sql, userName);

		User user1 = null;

		// 检查列表是否为空，以及是否至少有一个元素
		if (resultMaps != null && !resultMaps.isEmpty()) {
		    // 获取第一个 Map，因为我们使用了 LIMIT 1，所以最多只有一个元素
		    Map<String, Object> resultMap = resultMaps.get(0);
		    
		    // 假设 User 类有适当的构造函数或 setter 方法来设置属性
		    user1 = new User();
		    user1.setUserId((Integer) resultMap.get("userId"));
		    user1.setUserName((String) resultMap.get("userName"));
		    user1.setUserPassWord((String) resultMap.get("userPassWord"));
		    user1.setName((String) resultMap.get("name"));
		    user1.setSex((String) resultMap.get("sex"));
		    user1.setAge(((Number) resultMap.get("age")).intValue());
		    user1.setTell((String) resultMap.get("tell"));
		    user1.setAddress((String) resultMap.get("address"));
		    user1.setEnabled((String) resultMap.get("enabled"));
		}		

		return user1;	}
	

}
