package qyServer0;


public class users {
	private String username;
	private String email;
	private String crypassword;
	private encrypt cry;
	//�������˸�SHA256�ļ���ģ�������ֹ���Ĵ洢���ݿ�й¶
	public users() {}
	public users(String username,String email,String temppassword)
	{
		this.username = username;
		this.email = email;
		if(temppassword.length()!=64)					//û���ܵ�
		this.crypassword = cry.getSHA256(temppassword);
		else
			this.crypassword=temppassword;
	}
	public String getusername(){return username;}
	public String getemail(){return email;}
	public String getpassword(){return crypassword;}
	public void setusername(String username) {this.username = username;}
	public void setemail(String email) {this.email = email;}
	public void setpassword(String password) {this.crypassword = cry.getSHA256(password);}
}