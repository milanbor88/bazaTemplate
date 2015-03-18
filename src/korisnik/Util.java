package korisnik;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import logovanje.LoginUser;
import baza.DBKonekcija;


@ManagedBean(name="util")
@RequestScoped
public class Util {
	
	@ManagedProperty(value="#{loginUser}")
	private LoginUser userLog;

	public LoginUser getUserLog() {
		return userLog;
	}

	public void setUserLog(LoginUser userLog) {
		this.userLog = userLog;
	}
	
	private String tabela;
	ResultSet rs=null;

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}
	
	public List<SelectItem> getVrednostiTab() {
		
		List<SelectItem> sveTabele=new ArrayList<SelectItem>();
		List<String> tabele=new ArrayList<String>();
		
		String ime=getUserLog().getUsername();
	/*	String ime="zorance";*/
		
		String sql="SELECT imeTab FROM kortable WHERE username = '"+ ime +"'";
		
		DBKonekcija.getConnection();
		
		rs=DBKonekcija.getResultS(sql);
		
		try {
			while(rs.next()) {
				tabele.add(rs.getString("imeTab"));
			}
		} catch (SQLException e) {
			System.out.println("Exception in getTabelaVred::"+e.getMessage());
		}
		
		for(int i=0;i<tabele.size(); i++) {
			sveTabele.add(new SelectItem(tabele.get(i)));
	
		}
		
		return sveTabele;
	
	}
	

}
