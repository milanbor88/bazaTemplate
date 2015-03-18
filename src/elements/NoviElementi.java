package elements;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import baza.DBKonekcija;


public class NoviElementi {

/*	private HtmlPanelGroup dataGroup;*/
	
	
	

/*	proba za din table data*/
	
	public String izabranaTab;


	private HtmlPanelGroup dynamicDataTableGroup;
	
	public List<List<String>> dynamicMF;
	
	
	public List<List<String>> getDynamicMF() {
		return dynamicMF;
	}

	public void setDynamicMF(List<List<String>> dynamicMF) {
		this.dynamicMF = dynamicMF;
	}
	
	
	public int countRow;
	
	public int getCountRow() {
		return countRow;
	}

	public void setCountRow(int countRow) {
		this.countRow = countRow;
	}
	
	public String[] dynamicHeaders;
	

	public String[] getDynamicHeaders() {
		return dynamicHeaders;
	}

	public void setDynamicHeaders(String[] dynamicHeaders) {
		this.dynamicHeaders = dynamicHeaders;
	}
	
	
	public List<String>  dynamicList;
	
	public List<String> getDynamicList() {
		return dynamicList;
	}

	public void setDynamicList(List<String> dynamicList) {
		this.dynamicList = dynamicList;
	}
	

	public HtmlPanelGroup getDynamicDataTableGroup() {
		
		if(dynamicDataTableGroup==null) {
			loadDynamicList();
			populateDynamicDataTable();
		}
		return dynamicDataTableGroup;
	}

	public void setDynamicDataTableGroup(HtmlPanelGroup dynamicDataTableGroup) {
		this.dynamicDataTableGroup = dynamicDataTableGroup;
	}
	
/*	---------------------------------------------------*/

	private HtmlPanelGrid dataGrid;


	private int brojElem;

	private String select;
	private int brojKol;
	private int brojRed;

	public int getBrojKol() {
		return brojKol;
	}

	public void setBrojKol(int brojKol) {
		this.brojKol = brojKol;
	}

	public int getBrojRed() {
		return brojRed;
	}

	public void setBrojRed(int brojRed) {
		this.brojRed = brojRed;
	}

	public int getBrojElem() {
		return brojElem;
	}

	public void setBrojElem(int brojElem) {
		this.brojElem = brojElem;
	}
	
	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	private String input;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public HtmlPanelGrid getDataGrid() {
		
		if(dataGrid==null) {
			populateGrid();
		}
		
		return dataGrid;
	}

	public void setDataGrid(HtmlPanelGrid dataGrid) {
		this.dataGrid = dataGrid;
	}



	private ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
            facesContext.getELContext(), valueExpression, valueType);
    }
    
	//TREBA
	public void populateGrid() {

		Application app=FacesContext.getCurrentInstance().getApplication();

		dataGrid=new HtmlPanelGrid();
		
		 HtmlPanelGroup panel=(HtmlPanelGroup) app.createComponent(HtmlPanelGroup.COMPONENT_TYPE);
	
		 for(int i=0; i<brojElem; i++) {

		/* HtmlPanelGroup panel=(HtmlPanelGroup) app.createComponent(HtmlPanelGroup.COMPONENT_TYPE);*/
			   
		  HtmlInputText dataInput=(HtmlInputText) app.createComponent(HtmlInputText.COMPONENT_TYPE);
	       dataInput.setValueExpression("value", createValueExpression("#{noviElem.input}", String.class));
	       dataInput.setId("vrednost"+i);
	       
	      HtmlOutputText dataOutput=(HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
	       dataOutput.setValue("Ime tabele"+i);
	       dataOutput.setId("imeTab"+i);
	       
	       HtmlSelectOneMenu dataSelect=(HtmlSelectOneMenu) app.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
	       dataSelect.setValueExpression("value", createValueExpression("#{noviElem.select}", String.class));
	       dataSelect.setId("menu"+i);
	       
	       UISelectItems dataItems=(UISelectItems) app.createComponent(UISelectItems.COMPONENT_TYPE);
	       dataItems.setValueExpression("value", createValueExpression("#{noviElem.dataType}", ArrayList.class));
	       dataItems.setId("item"+i);
	       
	       List<UIComponent> menuChildList= dataSelect.getChildren();
	       menuChildList.add(dataItems);
	       
/*	       HtmlSelectBooleanCheckbox dataCheck=(HtmlSelectBooleanCheckbox) app.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
	       dataCheck.setValue("true");
	       dataCheck.setId("checkBox"+i);
	       dataCheck.setTitle("Oznaceno je true");
	       
	       HtmlOutputText dataOutputNull=(HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
	       dataOutputNull.setValue("Null");
	       dataOutputNull.setId("jelNull"+i);*/

	    /*   dataSelect.getChildren().add(dataItems);*/

	       panel.getChildren().add(dataOutput);
	       panel.getChildren().add(dataInput);
	       panel.getChildren().add(dataSelect);
	       
	/*       panel.getChildren().add(dataOutputNull);
	       panel.getChildren().add(dataCheck)*/;
	       
	   /*    dataGrid.getChildren().add(panel);*/
	       
	    
/*	       dataGroup.getChildren().add(dataOutput);
	       dataGroup.getChildren().add(dataInput);*/
	       
		   }
		 
		 dataGrid.getChildren().add(panel);
	}
	
	///////////////////////////////////////////
    
	//funkcija za vranje tipova podataka prilikom odabira za novu kolonu
	public List<SelectItem> getDataType() {
		
		List<SelectItem> sviTipovi=new ArrayList<SelectItem>();
		List<String> tipovi=new ArrayList<String>();
		
		tipovi.add("INT");
		tipovi.add("FLOAT");
		tipovi.add("VARCHAR");

		for(int i=0;i<tipovi.size(); i++) {
			sviTipovi.add(new SelectItem(tipovi.get(i)));
		}
		
		return  sviTipovi;
	}
	
	
	
	public String vrati() {
		
		setDataGrid(null);
		return "tabele";
		
	}

	public void brojKR(String tab) {
		
		Connection con=DBKonekcija.getConnection();
		ResultSet rs=null;
		String sql="SELECT * FROM "+izabranaTab+"";
		System.out.println("za broj kolone i reda sql upit : " + sql);
		try {
			Statement stmt =con.createStatement();
			rs=stmt.executeQuery(sql);
			ResultSetMetaData data=rs.getMetaData();
			brojKol=data.getColumnCount();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
		/*		con.close();*/
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
		
		
		try {
			int brojac=0;
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(sql);
			while(rs.next()) {
				brojac++;
			}
			brojRed=brojac;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
	}
	

	
/*	METHODE ZA dinTABLU*/
	
private void loadDynamicList() {
		
		brojKR(izabranaTab);
		
		
		String sqlTab="SELECT * FROM "+ izabranaTab +"";
		
		Connection con=DBKonekcija.getConnection();
		ResultSet rs=null;


		try {
			
			
			rs=DBKonekcija.getResultS(sqlTab);
			
			System.out.println("query za headerse : " + sqlTab );
			
			ResultSetMetaData data=rs.getMetaData();
			
			System.out.println("brojKol je : " + brojKol);
		
			dynamicHeaders=new String[brojKol];
			
			//t<colNum
			for(int t=0;t<brojKol; t++) {
				String colName=data.getColumnName(t+1);
				System.out.println("Iz colName " + colName);

				dynamicHeaders[t]=colName;
				System.out.println("Iz dynamicHeaders " + dynamicHeaders[t]);
			}
			
			//j<colNum 
			for(int j=0;j<brojKol; j++) {
				System.out.println("provera za niz dynamicHeaders " + dynamicHeaders[j] );
				System.out.println(j);
			}
			
/*			String kuku="";
			
			for(int t=0;t<dynamicHeaders.length; t++) {
				if(t==dynamicHeaders.length-1) {
					kuku+=dynamicHeaders[t];
				} else {
				kuku+=  dynamicHeaders[t] + ",";
				}
			}
			
			System.out.println("String kuku je : " + kuku);*/
			
		} catch (SQLException e) {

			System.out.println("Za probas, za headerDinamic" + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		

		
		try {
	
			rs=DBKonekcija.getResultS(sqlTab);
			
			dynamicMF=new ArrayList<List<String>>();
			
			while(rs.next()) {
				
				dynamicList=new ArrayList<String>();
				countRow++;
		
				System.out.println("countRow" + countRow);
				
				for(int i=1;i<=brojKol; i++) {

					dynamicList.add(rs.getString(i).toString());
					
					System.out.println("iz dynamicList rs.getString(i) " + rs.getString(i));
					System.out.println("iz dynamicList[" + countRow +"]" + dynamicList);
				
				}
				
				System.out.println(dynamicList);

				dynamicMF.add(dynamicList);
				
				System.out.println("dynamicMF " + dynamicMF);
	
			}
		
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				rs.close();
				con.close();
			} catch (SQLException e) {
			System.out.println(e.getMessage());
			}
		}
	
	}

	
	 private void populateDynamicDataTable(){
		 
		 HtmlDataTable dynamicDataTable=new HtmlDataTable();
		 
		 dynamicDataTable.setValueExpression("value", 
				 createValueExpression("#{noviElem.dynamicMF}", List.class));
		 
		 dynamicDataTable.setVar("dynamicItem");
		 

		 
		 for(int i=0; i< dynamicMF.get(0).size(); i++) {
			 HtmlColumn column = new HtmlColumn();
			 dynamicDataTable.getChildren().add(column);
			 
			 System.out.println("kad se pravi dynamicHeaders : " + dynamicHeaders[i]);
			 
			 HtmlOutputText header=new HtmlOutputText();
			 header.setValue(dynamicHeaders[i]);
			 column.setHeader(header);
			 
			 HtmlOutputText output = new HtmlOutputText();
			 output.setValueExpression("value", 
					 createValueExpression("#{dynamicItem["+ i +"]}", String.class));
			 column.getChildren().add(output);
			 

			 
		 }
		 
		 dynamicDataTableGroup = new HtmlPanelGroup();
	     dynamicDataTableGroup.getChildren().add(dynamicDataTable);

		
	 }
	 

		
		public void radSaTabProbas(ActionEvent evt) {
			
			HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			izabranaTab=request.getParameter("formSelTab:tablica");
			
			System.out.println("izabranaTab : " + izabranaTab);
			
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("probas.xhtml");
			} catch (IOException e) {
				System.err.println("Greska prilikom redirekta " + e.getMessage());
			}
			
			
		}
	 
	
}
