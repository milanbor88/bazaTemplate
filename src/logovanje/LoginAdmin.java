package logovanje;


import java.io.IOException;
import java.sql.ResultSet;






import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import baza.DBKonekcija;


public class LoginAdmin {
	
	private String dbadminName;
	private String dbpass;
	private String adminName;
	private String password;
	private boolean isLogin=false;

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public String getDbadminName() {
		return dbadminName;
	}

	public void setDbadminName(String dbadminName) {
		this.dbadminName = dbadminName;
	}

	public String getDbpass() {
		return dbpass;
	}

	public void setDbpass(String dbpass) {
		this.dbpass = dbpass;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

/*	Connection con;
	Statement stmt;
	ResultSet rs;*/
	
	
	public void getAdmin (String adminName) {
		
		String sql_str="SELECT adminName,password FROM admin WHERE adminName = '"+adminName+"'";
		
		DBKonekcija.getConnection();
		try 
		{
	/*		Class.forName("com.mysql.jdbc.Driver");
			con=
			stmt=con.createStatement();
			rs=stmt.executeQuery(sql_str);*/
			/*if(rs !=null) {*/
			
			/*Connection con=DBKonekcija.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql_str)*/;
			
			ResultSet rs=DBKonekcija.getResultS(sql_str);
			
			rs.next();
			
			dbadminName=rs.getString(1).toString();
			System.out.println("adminName is " + dbadminName);
			dbpass=rs.getString(2).toString();
			System.out.println("password is" + dbpass); 
			rs.close();
		/*	} */
			 
		} catch (Exception e) {
			System.err.println("nema korisnika sa tim imenom " + e.getMessage());
		} 
		
	}
	
	//checking username and password and return invalid and valid
	public String proveraSifreAdmina() {
		
		getAdmin(adminName);
		System.out.println("ide");
		if(this.adminName.equals(dbadminName) && this.password.equals(dbpass)) {
			System.out.println("get valid");
			isLogin=true;
			return "valid";
		} else {
			System.out.println("get invalid");
			FacesMessage msg=new FacesMessage("Pogreska, netacan unos podataka!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
			
		}

	}
	
	private void doRedirect(String url){  
		  try {  
		   FacesContext context=FacesContext.getCurrentInstance();  
		   context.getExternalContext().redirect(url);  
		  } catch (IOException e) {  
			  FacesMessage msg=new FacesMessage(e.getMessage());
			  FacesContext.getCurrentInstance().addMessage(null, msg);  
		  }  
		 }  
	
	public void verifyLogin(ComponentSystemEvent event) {
		if(!isLogin) {
			doRedirect("loginAdmin.xhtml");
		}
	}
	
	

}
