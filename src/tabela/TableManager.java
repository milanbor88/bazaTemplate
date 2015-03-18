package tabela;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import baza.DBKonekcija;



public class TableManager {

	private String imeTab;
	private Connection con=null;
	private PreparedStatement ps=null;
	private String tabele;
	PreparedStatement psUser=null;
	Connection conUser=null;
	

	public String getTabele() {
		return tabele;
	}

	public void setTabele(String tabele) {
		this.tabele = tabele;
	}

	public PreparedStatement getPs() {
		return ps;
	}

	public void setPs(PreparedStatement ps) {
		this.ps = ps;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public String getImeTab() {
		return imeTab;
	}

	public void setImeTab(String imeTab) {
		this.imeTab = imeTab;
	}
	
	
	public List<SelectItem> getTabeleVred() {
		List<SelectItem> sveTabele=new ArrayList<SelectItem>();
		List<String> tabele=new ArrayList<String>();
		
		con=DBKonekcija.getConnection();
		ResultSet rsTables=null;
		
		try {
			
			DatabaseMetaData metadata=con.getMetaData();
			String[] tableTypes={"TABLE"};
			rsTables=metadata.getTables(null, "%", "%" , tableTypes);
			
			while(rsTables.next()) {
				
				
				tabele.add(rsTables.getString("TABLE_NAME"));
				if(rsTables.getString("TABLE_NAME").equals("admin")) {
					tabele.remove(rsTables.getString("TABLE_NAME"));
				}
				
				if(rsTables.getString("TABLE_NAME").equals("tabela")) {
					tabele.remove(rsTables.getString("TABLE_NAME"));
				}
				
				if(rsTables.getString("TABLE_NAME").equals("user")) {
					tabele.remove(rsTables.getString("TABLE_NAME"));
				}
				
				if(rsTables.getString("TABLE_NAME").equals("kortable")) {
					tabele.remove(rsTables.getString("TABLE_NAME"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Exception in getKorisniciVred::"+e.getMessage());
		}
		
		
		for(int i=0;i<tabele.size(); i++) {
			sveTabele.add(new SelectItem(tabele.get(i)));
	
		}
		
		return sveTabele;
		
	}
	
	
	public void dodajTabOpis(String novaTab, String noviOpis) {

		String sql="INSERT INTO tabela (imeTab,opisTab) VALUES (?,?)";
		
		con=DBKonekcija.getConnection();
		
		try {
			ps= con.prepareStatement(sql);
			ps.setString(1, novaTab);
			ps.setString(2, noviOpis);
			int affected = ps.executeUpdate();
			if( affected ==1) {
				FacesMessage msg=new FacesMessage("Nova tabela uspesno je uneta u tabelu!!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				FacesMessage msg=new FacesMessage("Desila se greska !!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (SQLException e) {
			FacesMessage msg=new FacesMessage(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {
			
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
		}
		
	}
	
	public void dodajKol(ActionEvent event) {
		
	}
	
	public String novaTab() {
		return "dodTab";
	}
	
public String dodajTab(int brojElem) {
		
	
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		String novaTab=request.getParameter("formKolone:novaTab");
		String noviOpis=request.getParameter("formKolone:noviOpis");
		
		
		String[] novaKol=new String[brojElem];
		String[] noviTip=new String[brojElem];
		/*String[] noviNull=new String[brojElem];*/
	
		for(int i=0; i<brojElem; i++) {
			novaKol[i]=request.getParameter("formKolone:vrednost"+i);
			noviTip[i]=request.getParameter("formKolone:menu"+i);
		/*	noviNull[i]=request.getParameter("formKolone:checkBox"+i);*/
		}
		
	/*	for (int y=0; y<novoIme.length; y++) {
	
		FacesMessage msg=new FacesMessage(novoIme[y] +" "+ noviTip[y]);
		FacesContext.getCurrentInstance().addMessage(null, msg); 
		}*/
		
		//pokusaj za setovanje vrednosti checkboxa i posle koristiti u sql-u
/*		for(int t=0;t<noviNull.length; t++) {
			if(noviNull[t].equals("true")) {
				noviNull[t]="NULL";
			} else {
				noviNull[t]="NOT NULL";
			}
		}*/
		
		String sql="CREATE TABLE "+ novaTab +"  (";
		for(int d=0; d<novaKol.length; d++) {
			if(d==(novaKol.length-1)) {
				sql+=novaKol[d] + " " + noviTip[d];
			} else {
			sql+=novaKol[d] + " " + noviTip[d] + ",";
			}
		}
		sql+=");";
		
/*		String sql="CREATE TABLE "+ novaTab +"  (";
		for(int d=0; d<novaKol.length; d++) {
			if(d==(novaKol.length-1)) {
				sql+=novaKol[d] + " " + noviTip[d] + " " + noviNull[d];
			} else {
			sql+=novaKol[d] + " " + noviTip[d] + " " + noviNull[d] + ", ";
			}
		}
		sql+=");";*/
		
		
		System.out.println(sql);
		System.out.println(brojElem);
		System.out.println(novaTab);
		System.out.println(noviOpis);
/*		for(int h=0; h<brojElem; h++) {
		System.out.println(noviNull[h]);
		}*/
		con=DBKonekcija.getConnection();
		
		try {
			ps= con.prepareStatement(sql);
			ps.executeUpdate();
				
			FacesMessage msg=new FacesMessage("Nova tabela uspesno je uneta u tabelu!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
		} catch (SQLException e) {
			FacesMessage msg=new FacesMessage(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}finally {
			
			try {
				con.close();
				ps.close();
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
		}
		
		dodajTabOpis(novaTab, noviOpis );
		
		return null;
	}

	public void poveziKor(ActionEvent evt) {
		
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String noviKor=request.getParameter("tabForm:korisnik");
		String novaTab=request.getParameter("tabForm:tabela");
		
		String sql="INSERT INTO kortable (username, imeTab) VALUES (?,?)";
		con=DBKonekcija.getConnection();
	
			try {
			ps= con.prepareStatement(sql);
			ps.setString(1, noviKor);
			ps.setString(2, novaTab);
			int dodato = ps.executeUpdate();
			if( dodato ==1) {
				
				conUser=DBKonekcija.getConnection();
				
				String sql_Veza="GRANT INSERT, DELETE, UPDATE ON administrator."+novaTab+" TO '"+noviKor+"'@'localhost';";

				System.out.println(sql_Veza);
				try {
					psUser=conUser.prepareStatement(sql_Veza);
		
					psUser.executeUpdate();
					FacesMessage msg=new FacesMessage("Dodato u bazu nova veza !!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				
				} catch (Exception e) {
						
					FacesMessage msg=new FacesMessage(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					
				}finally {
					try {
						psUser.close();
						conUser.close();
					} catch (SQLException e) {
						FacesMessage msg=new FacesMessage(e.getMessage());
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
				}
				
			} else {
				FacesMessage msg=new FacesMessage("Desila se greska, veza nije dodata !!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			
			}
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					FacesMessage msg=new FacesMessage(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
	}
	
}
