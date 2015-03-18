package logovanje;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import baza.DBKonekcija;

@ManagedBean(name="loginUser")
@SessionScoped
public class LoginUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String username;
    private String password;
    private boolean loggedIn;
    ResultSet rs=null;
    
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
    
	public String proveraUsera() {
		FacesMessage msg=null;
		
		String dbuser="";
		String dbpass="";
		
		String sql="SELECT username, password FROM user WHERE username = '"+username+"'";
		
		DBKonekcija.getConnection();
		
		try {
			rs=DBKonekcija.getResultS(sql);
			
			rs.next();
			
			dbuser=rs.getString(1).toString();
			
			dbpass=rs.getString(2).toString();
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(username.equals(dbuser) && password.equals(dbpass)) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome",
				        username);
				 FacesContext.getCurrentInstance().addMessage(null, msg);
				loggedIn = true;
				System.out.println("password is " + password); 
				System.out.println("adminName is " + username);
				return "/userpages/welcome.xhtml?faces-redirect=true";
		} else {
			 msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error",
	                    "Invalid User Name or Password");
			 FacesContext.getCurrentInstance().addMessage(null, msg);
	            loggedIn = false;
	            return null;
		}
	}
	
	public String logout() {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
	             .getSession(false)).invalidate();
	        loggedIn = false;
	       return "/loginUser.xhtml?faces-redirect=true";
	}
	
/*	public void logout(ActionEvent event) throws IOException {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false)).invalidate();
           loggedIn = false;
           FacesContext.getCurrentInstance().getExternalContext().redirect("/loginUser.xhtml");
    }*/
	
/*	public void proveraUsera(ActionEvent event) {
		FacesMessage msg=null;
		
		String dbuser="";
		String dbpass="";
		
		String sql="SELECT username, password FROM user WHERE username = '"+username+"'";
		
		DBKonekcija.getConnection();
		
		try {
			rs=DBKonekcija.getResultS(sql);
			
			rs.next();
			
			dbuser=rs.getString(1).toString();
			
			dbpass=rs.getString(2).toString();
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(username.equals(dbuser) && password.equals(dbpass)) {
			try {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome",
				        username);
				FacesContext.getCurrentInstance().getExternalContext()
				        .redirect("/userpages/welcome.xhtml");
				 FacesContext.getCurrentInstance().addMessage(null, msg);
				loggedIn = true;
				System.out.println("password is " + password); 
				System.out.println("adminName is " + username);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			 msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error",
	                    "Invalid User Name or Password");
			 FacesContext.getCurrentInstance().addMessage(null, msg);
	            loggedIn = false;
	      
		}
		
	}*/
    

}
