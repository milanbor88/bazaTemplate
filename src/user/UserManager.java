package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*import java.sql.Statement;*/
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;








import baza.DBKonekcija;
import user.User;
public class UserManager {
	
	private String korisnici;
	private String ime;
	private String sifra;
	PreparedStatement ps=null;
	Connection con=null;
	PreparedStatement psUser=null;
	Connection conUser=null;

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(String korisnici) {
		this.korisnici = korisnici;
	}


	public List<SelectItem> getKorisniciVred() {
		List<SelectItem> sviKorisnici=new ArrayList<SelectItem>();
		List<User> korisnici=new ArrayList<User>();
		String sql_str="SELECT username FROM user";
		
		DBKonekcija.getConnection();
		
		try {
			/*Connection con=DBKonekcija.getConnection();*/
			
			/*Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql_str);*/
			ResultSet rs=DBKonekcija.getResultS(sql_str);
			while(rs.next()) {
				
				User kor=new User();
				kor.setUsername(rs.getString("username"));
				korisnici.add(kor);
				
			}
		} catch (SQLException e) {
			System.out.println("Exception in getKorisniciVred::"+e.getMessage());
		}
		
		
		for(int i=0;i<korisnici.size(); i++) {
			sviKorisnici.add(new SelectItem(korisnici.get(i).getUsername()));
		}
		
		return sviKorisnici;
		
	}
	
	
	public void promenaKor(ValueChangeEvent event) {
		ime=event.getNewValue().toString();
		String sql_str="SELECT username,password FROM user WHERE username = '"+ime+"'";
		
		DBKonekcija.getConnection();
		
		try {
			/*Connection con=DBKonekcija.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql_str);*/
			
			ResultSet rs=DBKonekcija.getResultS(sql_str);
			
			rs.next();
			
			ime=rs.getString("username").toString();
			sifra=rs.getString("password").toString();
		} catch (SQLException e) {
			System.out.println("Exception in getAllCustomer::"+e.getMessage());
		}
		
		
	}



	public List<User> getPrikazikorisnika() {
		List<User> infoKor=new ArrayList<User>();
		/*String sql_str="SELECT username,password FROM user WHERE username = '"+user+"'";*/
		
		String sql_str="SELECT username,password FROM user";
		
		DBKonekcija.getConnection();
		ResultSet rs=DBKonekcija.getResultS(sql_str);
		
		try {

			while(rs.next()) {
			User kor=new User();
			kor.setUsername(rs.getString("username"));
			kor.setPassword(rs.getString("password"));
			infoKor.add(kor);
			}
		} catch (SQLException e) {
			System.out.println("Exception in getPrikaziKorisnika::"+e.getMessage());
		}
		
		return infoKor;
	}

	public void dodajKor(ActionEvent event) {
		
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String novaSif=request.getParameter("adminForm:novSif");
		String noviKor=request.getParameter("adminForm:novIme");
		
		/*String sql = "INSERT into user (username, password) " +
				"VALUES ('"+noviKor+"', '"+novaSif+"')";*/
		String sql="INSERT INTO user (username,password) VALUES (?,?)";
	
		
		if(novaSif=="" && noviKor!="") {
			FacesMessage msg=new FacesMessage("Mora se uneti sifra za novog korisnika !!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else if(noviKor=="" && novaSif!=""){
			FacesMessage msg=new FacesMessage("Mora se uneti ime za novog korisnika !!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else if(noviKor!="" && novaSif!="") {
			
			con=DBKonekcija.getConnection();
		
			try {
			ps= con.prepareStatement(sql);
			ps.setString(1, noviKor);
			ps.setString(2, novaSif);
			int dodato = ps.executeUpdate();
			if( dodato !=1) {
				FacesMessage msg=new FacesMessage("Desila se greska, nije dodat novi korisnik !!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				
				String sql_User="CREATE USER '"+noviKor+"'@'localhost' IDENTIFIED BY '"+novaSif+"';";
				conUser=DBKonekcija.getConnection();
				System.out.println(sql_User);
				try {
					psUser=conUser.prepareStatement(sql_User);
		
					psUser.executeUpdate();
						FacesMessage msg=new FacesMessage("Dodato u bazupodataka nov korisnik !!!");
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
				
			}
			
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
			} finally {
				try {
					ps.close();
					con.close();
				} catch (SQLException e) {
					FacesMessage msg=new FacesMessage(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} else {
			FacesMessage msg=new FacesMessage("Ime korisnika i sifra se moraju uneti!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void obrisiKor(ActionEvent event) {
		
		String sql = "DELETE FROM user WHERE username = ?";
		con=DBKonekcija.getConnection();
		
		try {
			
			ps=con.prepareStatement(sql);
			ps.setString(1, ime);
			int affected = ps.executeUpdate();
			
			if(affected == 1) {
				
				conUser=DBKonekcija.getConnection();
				
				String sql_User="DROP USER '"+ime+"'@'localhost';";

				System.out.println(sql_User);
				try {
					psUser=conUser.prepareStatement(sql_User);
		
					psUser.executeUpdate();
						FacesMessage msg=new FacesMessage("Izbrisano iz baze podatakaka !!!");
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
				FacesMessage msg=new FacesMessage("Korisnik nije obrisan !!!");
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
	
	public void izmeniKor(ActionEvent event) {
		String sql_all="UPDATE user SET " +
				"username = ?, password = ? " +
				"WHERE username = '"+ime+"'";
		String sql_name="UPDATE user SET username= ? WHERE username='"+ime+"'";
		String sql_pass="UPDATE user SET password = ? WHERE username='"+ime+"'";
		
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String novaSif=request.getParameter("adminForm:novSif");
		String noviKor=request.getParameter("adminForm:novIme");
		
	/*	if(noviKor=="" && novaSif=="") {
			FacesMessage msg=new FacesMessage("Mora se uneti barem jedan podatak za promenu!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		}*/
		con=DBKonekcija.getConnection();
		
		if(noviKor=="" && novaSif!="" ) {
			
			try {
				ps=con.prepareStatement(sql_pass);
				ps.setString(1, novaSif);
				int affected=ps.executeUpdate();
				if(affected == 1) {
					FacesMessage msg=new FacesMessage("Sifra korisnika je promenjena!!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg=new FacesMessage("Sifra nije promenjena !!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					FacesMessage msg2=new FacesMessage(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg2);
				}
			}
		} else if(novaSif=="" && noviKor!="") {
			try {
				ps=con.prepareStatement(sql_name);
				ps.setString(1, noviKor);
				int affected=ps.executeUpdate();
				if(affected == 1) {
					FacesMessage msg=new FacesMessage("Korisnicko ime je promenjeno!!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg=new FacesMessage("Korisnicko ime nije promenjeno !!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					FacesMessage msg2=new FacesMessage(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg2);
				}
			}
		} else if(noviKor!="" && novaSif!="") {
			try {
				ps=con.prepareStatement(sql_all);
				ps.setString(1, noviKor);
				ps.setString(2, novaSif);
				int affected=ps.executeUpdate();
				if(affected == 1) {
					FacesMessage msg=new FacesMessage("Sifra i korisnicko ime su promenjeni!!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					FacesMessage msg=new FacesMessage("Neuspesna promena !!!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			} catch (SQLException e) {
				FacesMessage msg=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					FacesMessage msg2=new FacesMessage(e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg2);
				}
			}
		} else {
			FacesMessage msg=new FacesMessage("Mora se uneti barem jedan podatak za promenu!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			try {
				con.close();
			} catch (SQLException e) {
				FacesMessage msg2=new FacesMessage(e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg2);
			}
			
		}
	}
	
}
