package korisnik;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;


import javax.servlet.http.HttpServletRequest;

import elements.NoviElementi;
import baza.DBKonekcija;

@ManagedBean(name="korMan")
@SessionScoped
public class KorisnikManager {
	
	@ManagedProperty(value="#{noviElem}")
	private NoviElementi novElem;

	public NoviElementi getNovElem() {
		return novElem;
	}
	public void setNovElem(NoviElementi novElem) {
		this.novElem = novElem;
	}



	ResultSet rs=null;
	PreparedStatement ps=null;
	Connection con=null;
	
	private String imePrveKol;
	private String imeTab;
	
	public String getImePrveKol() {
		return imePrveKol;
	}
	public void setImePrveKol(String imePrveKol) {
		this.imePrveKol = imePrveKol;
	}
	public String getImeTab() {
		return imeTab;
	}
	public void setImeTab(String imeTab) {
		this.imeTab = imeTab;
	}



	private String imeKolone;
	private String valueSel;
	
	public String getImeKolone() {
		return imeKolone;
	}
	public void setImeKolone(String imeKolone) {
		this.imeKolone = imeKolone;
	}
	public String getValueSel() {
		return valueSel;
	}
	public void setValueSel(String valueSel) {
		this.valueSel = valueSel;
	}
	
	private String kolone;
	
	public String getKolone() {
		return kolone;
	}
	public void setKolone(String kolone) {
		this.kolone = kolone;
	}

	private List<List<String>> dynList;
	
	public List<List<String>> getDynList() {
		return dynList;
	}
	public void setDynList(List<List<String>> dynList) {
		this.dynList = dynList;
	}



	private HtmlPanelGrid dynamicRow;
	
	public HtmlPanelGrid getDynamicRow() {
		if(dynamicRow==null) {
			populateOnLoad();
			populateDynamicRow();
		}
		return dynamicRow;
	}
	public void setDynamicRow(HtmlPanelGrid dynamicRow) {
		this.dynamicRow = dynamicRow;
	}



	private List<String> listRow;
	
	public List<String> getListRow() {
		return listRow;
	}
	public void setListRow(List<String> listRow) {
		this.listRow = listRow;
	}
	
	private HtmlPanelGrid dynInputs;
	
	public HtmlPanelGrid getDynInputs() {
		
		if(dynInputs==null) {
			populateDynInputs();
		}
		
		return dynInputs;
	}
	public void setDynInputs(HtmlPanelGrid dynInputs) {
		this.dynInputs = dynInputs;
	}
	
	private String inputVals;
	
	public String getInputVals() {
		return inputVals;
	}
	public void setInputVals(String inputVals) {
		this.inputVals = inputVals;
	}
	
	
	public List<SelectItem> getTabeleVred() {
		
		List<SelectItem> sveVred=new ArrayList<SelectItem>();
		List<String> vred=new ArrayList<String>();
		
		imePrveKol=getNovElem().getDynamicHeaders()[0];
		imeTab=getNovElem().izabranaTab;
		System.out.println("imePrveKol : " + imePrveKol + " imeTab : " + imeTab );
		
		String sql="SELECT "+ imePrveKol +" FROM "+ imeTab +"";
		System.out.println("sql za dobijanje vred iz prve kolone : " + sql);
		
		DBKonekcija.getConnection();
		
		rs=DBKonekcija.getResultS(sql);
		
		try {
			while(rs.next()) {
				
				vred.add(rs.getString(imePrveKol));
		
			}
		} catch (SQLException e) {
			System.out.println("nije nesto dobro u getTabeleVred " + e.getMessage());
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println("nije se lepo zatvorilo rs " + e.getMessage());
			}
		}
		
		for(int i=0; i<vred.size(); i++) {
			
			sveVred.add(new SelectItem(vred.get(i)));
		
		}
		
		return sveVred;
		
	}
	
	public void promenaVred(ValueChangeEvent event) {
	
		valueSel=event.getNewValue().toString();
		System.out.println("selektovana vred : " + valueSel);
		String kolone="";
		
		for(int t=0; t<getNovElem().dynamicHeaders.length; t++) {
			if(t==getNovElem().dynamicHeaders.length-1) {
				kolone+=getNovElem().dynamicHeaders[t];
			}else {
				kolone+=getNovElem().dynamicHeaders[t] + ",";
			}
		}
		
		System.out.println(" kolone koje treba prikazati : " + kolone );
		
		String sql_str="SELECT "+ kolone +" FROM "+ imeTab +" WHERE "+ imePrveKol +" = "+ valueSel +"";
		System.out.println("sql za dobijanje vrednosti za odredjeni red : " + sql_str);
		
		DBKonekcija.getConnection();
		
		try {
			
			ResultSet rs=DBKonekcija.getResultS(sql_str);
			
			rs.next();
			
			listRow=new ArrayList<String>();
			
			for(int i=1; i<=getNovElem().getBrojKol();i++) {
				listRow.add(rs.getString(i).toString());
			}
			
			System.out.println("listRow je : " + listRow);
			
			dynList=new ArrayList<List<String>>();
			dynList.add(listRow);
			
		} catch (SQLException e) {
			System.out.println("Exception in promenaVred::"+e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println("nije se zatvorila rs in promenaVred::"+e.getMessage());
			}
			
		}
		
		
		System.out.println("iz select menu vred je : " + imeKolone);
		
		populateDynamicRow();
	}

	public void populateDynamicRow() {
		
		HtmlDataTable dynTable=new HtmlDataTable();
		dynTable.setValueExpression("value", 
				 createValueExpression("#{korMan.dynList}", List.class));
		dynTable.setVar("dynItem");
		 
		for(int i=0;i<dynList.get(0).size(); i++) {
			
			 HtmlColumn column = new HtmlColumn();
			 dynTable.getChildren().add(column);
			 
			 
			/* HtmlOutputText header=new HtmlOutputText();
			 header.setValue(getNovElem().dynamicHeaders[i]);
			 column.setHeader(header);*/
			 
			 HtmlOutputText output = new HtmlOutputText();
			 output.setValueExpression("value", 
					 createValueExpression("#{dynItem["+ i +"]}", String.class));
			 
			 column.getChildren().add(output);
		} 

		
	
		dynamicRow = new HtmlPanelGrid();
		dynamicRow.getChildren().add(dynTable);
		

		
		
	}
	
	private ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
            facesContext.getELContext(), valueExpression, valueType);
    }
	
	
	private void populateOnLoad() {
		
		imePrveKol=getNovElem().getDynamicHeaders()[0];
		imeTab=getNovElem().izabranaTab;
		
		String prvaVred=getNovElem().getDynamicMF().get(0).get(0);
		System.out.println("prva vrednost iz prvog reda iz izabrene tabele : " + prvaVred);
		
		String kolone="";
		
		for(int t=0; t<getNovElem().dynamicHeaders.length; t++) {
			if(t==getNovElem().dynamicHeaders.length-1) {
				kolone+=getNovElem().dynamicHeaders[t];
			}else {
				kolone+=getNovElem().dynamicHeaders[t] + ",";
			}
		}
		
		System.out.println(" kolone koje treba prikazati : " + kolone );
		
		String sql_str="SELECT "+ kolone +" FROM "+ imeTab +" WHERE "+ imePrveKol +" = "+ prvaVred +"";
		System.out.println("sql za dobijanje vrednosti za odredjeni red : " + sql_str);
		
		DBKonekcija.getConnection();
		
		try {
			
			rs=DBKonekcija.getResultS(sql_str);
			
			rs.next();
			
			listRow=new ArrayList<String>();
			
			for(int i=1; i<=getNovElem().getBrojKol();i++) {
				listRow.add(rs.getString(i).toString());
			}
			
			System.out.println("listRow je : " + listRow);
			
			dynList=new ArrayList<List<String>>();
			dynList.add(listRow);
			
			System.out.println("dynList je : " + dynList);
			
		} catch (SQLException e) {
			System.out.println("Exception in promenaVred::"+e.getMessage());
		}		
	}
	
	
	public void populateDynInputs() {
		
		dynInputs=new HtmlPanelGrid();
		
		HtmlPanelGroup panel=new HtmlPanelGroup();
		
		for(int j=0; j<getNovElem().dynamicHeaders.length; j++) {
			
			HtmlOutputText kolBr=new HtmlOutputText();
			kolBr.setValue("Kolona#"+(j+1));
			panel.getChildren().add(kolBr);
			
			
			HtmlInputText inputData=new HtmlInputText();
			inputData.setValue(inputVals);
			inputData.setId("ulVred" + j);
	
			panel.getChildren().add(inputData);
			
		}
		
		dynInputs.getChildren().add(panel);
	}
	
/*	public void dodajRed(ActionEvent evt) {
		
		HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String noviRed[]=new String[getNovElem().dynamicHeaders.length];
		
		String vrednosti="";
		
		for(int i=0;i<getNovElem().dynamicHeaders.length; i++) {
			noviRed[i]=request.getParameter("tabKor:ulVred"+i);
		}
		
		for(int j=0;j<noviRed.length;j++) {
			
			System.out.println("noviRed["+j+"] je : " + noviRed[j]);
		}
		
		for(int t=0;t<noviRed.length; t++) {
			if(t==noviRed.length-1) {
				vrednosti+=noviRed[t];
			} else {
				vrednosti+=noviRed[t] + ",";
			}
		}
		
		System.out.println("string vrednosti je : " + vrednosti);
		
		String sql="INSERT INTO "+ imeTab +" ("+ kolone +") VALUES("+ vrednosti +") ";
		
		
		for(int k=0; k<noviRed.length;k++) {
			if(noviRed[k]=="") {
				FacesMessage msg=new FacesMessage("Mora se uneti podatak za kolonu["+ k +"] !!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		
		con=DBKonekcija.getConnection();
		
		try {
			ps=con.prepareStatement(sql);
			int dodat=ps.executeUpdate();
			if(dodat==1) {
				FacesMessage msg=new FacesMessage("Dodato u tabelu novi red !!!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (SQLException e) {
			FacesMessage msg=new FacesMessage(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("Desila se graska priliko zatvaranja konekcije rs i con");
			}
		}
		
	}*/
	
	/*@ManagedProperty(value="#{loginUser}")
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
		String ime="zorance";
		
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
	
	}*/

	
/*	public String radSaTabLink() {
		return "/userpages/radTabela.xhtml?faces-redirect=true";
	}
	
	public String radSaTab() {
		return "/userpages/proba.xhtml?faces-redirect=true";
	}
	
	public String radSaTabProbas() {
		return "/userpages/probas.xhtml?faces-redirect=true";
	}*/
	
}
