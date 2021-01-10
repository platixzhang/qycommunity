package qyServer0;


import java.util.*;

public class usersOp {
	private encrypt cry;
	public void createTable(JDBCKonnekt jk) throws Exception {
		String sql = "create table qyuser"
                + "("
                + "username varchar(10) not null,"
                + "email varchar(30) not null,"
                + "password varchar(256) not null,"
                + "primary key (username)"					//��������
                + ")";
        jk.executeSql(sql);
        //���ܺ�������Ǹ���ϣֵ��
    }
	public void removeTable(JDBCKonnekt jk)
	{
		String sql = "drop table qyuser;";
		jk.executeSql(sql);
	}
	public void addUser(users u,JDBCKonnekt jk) throws Exception {
		String sql = "insert into qyuser(username,email,password) values('"
				+u.getusername()+"','"+u.getemail()+"','"+u.getpassword()+"');";
	jk.executeSql(sql);
	}
	public int delUserViaID(String username,JDBCKonnekt jk) throws Exception {
		int ans = 1;
		List<Map<String,Object>> uList;
		String sql1 = "select * from qyuser where username='"+username+"';";
		uList=jk.query(sql1);
		if(uList.isEmpty())
		{
			System.out.println("No User Found!");
			return 0;
		}
        String sql = "delete from qyuser where username like '"+username+"';";
        jk.executeSql(sql);
        return ans;
	}
	public List<users> getUsers(JDBCKonnekt jk){
		String sql = "select * from qyuser;";
		List<Map<String,Object>> list = jk.query(sql);	//ʵ���Ǹ���ѯ��
		
		List<users> uList = new ArrayList<users>();		//�洢�Ľ����
		users user = null;
		for(Map<String,Object> map:list){
            user = new users((String)map.get("username"),(String)map.get("email"),(String)map.get("password"));
            uList.add(user);
        }
		return uList;
	}
	public List<users> getUserViaID(String username,JDBCKonnekt jk)
	{
		String sql = "select * from qyuser where username like '"+username+"';";
		List<Map<String,Object>> list = jk.query(sql);
		List<users> uList = new ArrayList<users>();
		users user = null;
		for(Map<String,Object> map:list){
            user = new users((String)map.get("username"),(String)map.get("email"),(String)map.get("password"));
            uList.add(user);
        }
		user = uList.stream().findFirst().orElse(null);
        return uList;
	}
	public List<users> getUserViaEmail(String email,JDBCKonnekt jk)
	{
		String sql = "select * from qyuser where email like '"+email+"';";
		List<Map<String,Object>> list = jk.query(sql);
		List<users> uList = new ArrayList<users>();
		users user = null;
		for(Map<String,Object> map:list){
            user = new users((String)map.get("username"),(String)map.get("email"),(String)map.get("password"));
            uList.add(user);
        }
		user = uList.stream().findFirst().orElse(null);
        return uList;
	}
	public boolean checkUserViaID(String username,JDBCKonnekt jk)
	{
		List<users> uList;
		//System.out.println("checkUserViaID:"+username);
		uList = getUserViaID(username,jk);
		if(uList.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public boolean checkUserViaEmail(String email,JDBCKonnekt jk)
	{
		List<users> uList;
		uList = getUserViaEmail(email,jk);
		if(uList.isEmpty())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public int modifyUser(String username,String password,JDBCKonnekt jk)
	{
		List<Map<String,Object>> uList;
		String sql1 = "select * from qyuser where username='"+username+"';";
		uList=jk.query(sql1);
		if(uList.isEmpty())
		{
			System.out.println("No User Found!");
			return 0;
		}
		else
		{
			String sql2 = "update qyuser set password='"+cry.getSHA256(password)+"' where username='"+username+"';";
			jk.executeSql(sql2);
			return 1;
		}
	}
}