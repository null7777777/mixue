package dao.impl;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import bean.Admin;
import bean.PageBean;
import dao.AdminDao;
import util.DateUtil;
import util.DbUtil;

public class AdminDaoImpl implements AdminDao {

	//用户登录，通过user传递，返回一个list集合
	@Override
	public boolean userLogin(Admin admin) {
		boolean flag=false;
		String sql="select * from s_admin where userName=? and passWord=?";
		String sql2="update s_admin set lastLoginTime=? where id=?";
		List<Map<String,Object>> list=DbUtil.executeQuery(sql, admin.getUserName(),admin.getPassWord());
		if(list.size()>0){
			flag=true;
			//这里需要name值传入对象中
			admin.setName((String)list.get(0).get("name"));
			//通过登录成功用户的id，更新最后登录时间
			DbUtil.excuteUpdate(sql2, DateUtil.getTimestamp(),list.get(0).get("id"));
		}
		return flag;
	}

	//用户列表的分页
	@Override
	public List<Admin> userList(PageBean pageBean) {
		List<Admin> lu=new ArrayList<>();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		
		String sql="select * from s_admin limit ?,?";
		
		list=DbUtil.executeQuery(sql,(pageBean.getCurPage()-1)*pageBean.getMaxSize(),pageBean.getMaxSize());
		
		if(list.size()>0) {
			for(Map<String,Object> map:list) {
				Admin u=new Admin(map);
				lu.add(u);
			}
		}
		
		return lu;
	}
	//用户的添加
	@Override
	public boolean userAdd(Admin user) {
		String sql="insert into s_admin(userName,password,name) values(?,?,?)";
		
		int i= DbUtil.excuteUpdate(sql, user.getUserName(),user.getPassWord(),user.getName());
		
		return i>0?true:false;	
		
	}

	//查找指定id用户信息
	@Override
	public Admin findUser(Integer id) {
		String sql="select * from s_admin where id=?";
		Admin admin=null;
		List<Map<String,Object>> list=DbUtil.executeQuery(sql, id);
		if(list.size()>0) {
			admin=new Admin(list.get(0));
		}
		return admin;
	}
	//查找用户名是否存在true存在
	@Override
	public boolean findUser(String userName) {
		String sql="select * from s_admin where userName=?";
		List<Map<String,Object>> list=DbUtil.executeQuery(sql, userName);
		return list.size()>0?true:false;
	}

	//根据用户的id更新用户信息
	@Override
	public boolean userUpdate(Admin admin) {
		String sql="update s_admin set password=? , name=? where id =?";
		int i=DbUtil.excuteUpdate(sql, admin.getPassWord(),admin.getName(),admin.getId());
		
		return i>0?true:false;
	}

	//根据id删除用户
	@Override
	public boolean delUser(int id) {
		String sql="delete from s_admin where id=?";
		int i=DbUtil.excuteUpdate(sql, id);
		return i>0?true:false;
	}

	//批量删除用户
	@Override
	public boolean batDelUser(String ids) {
		String sql="delete from s_admin where id in ("+ids+")";
		int i=DbUtil.excuteUpdate(sql);
		return i>0?true:false;
	}

	//用户数量的统计
	@Override
	public long teaReadCount() {
		long count=0;
		String sql="select count(*) as count from s_admin";
		List<Map<String, Object>> lm=DbUtil.executeQuery(sql);
		if(lm.size()>0){
			count=(long) lm.get(0).get("count");
		}
		return count;
	}

}
