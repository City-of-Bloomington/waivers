package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 *
 */

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class FilesImport{

    boolean debug = true;
    static Logger logger = LogManager.getLogger(FilesImport.class);
    static final long serialVersionUID = 220L;			
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Map<String, String> map = new HashMap<>();
    FileUpload fup = new FileUpload();
    public FilesImport(){

    }
    public String doImport(String fileName){
	//
	prepareMap();
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	//
	String path="c:\\data\\waivers\\files\\2018\\"; // PC path
	// String path=/srv/data/waivers/files/2018/"; // server path
				
	String qq = "insert into attachments "+
	    " (id,waiver_id,file_name,old_file_name,type,date,notes,user_id) "+
	    " values(?,?,?,?,?, now(),'imported',1)";
	String back = "";
	Reader reader = null;
	int jj=1; // use for address aa well
	try{
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB";
		return back;
	    }
	    pstmt = con.prepareStatement(qq);
	    reader = new FileReader(fileName);
	    BufferedReader buffer = new BufferedReader(reader);
	    String line;
	    while ((line = buffer.readLine()) != null) {
		String type = "", waiver_id="",name="", old_name=""; 
		String waiver_num = "", str="", id="";								
		if(line.length() > 5){
		    type = "Recorded Waiver";
		    id=""; waiver_id="";
		    waiver_num=""; name=""; old_name="";
		    str = line.substring(0,line.indexOf(".")).trim();
		    if(str.indexOf("-") > 0){
			type = "Other";
			waiver_num = str.substring(0,str.indexOf("-"));
		    }
		    else if(str.indexOf(' ') > 0){
			type = "Other";
			waiver_num = str.substring(0,str.indexOf(' '));
		    }
		    else{
			waiver_num = str;
		    }
		    if(map.containsKey(waiver_num)){
			waiver_id = map.get(waiver_num);
		    }
		    if(!waiver_id.equals("")){
			id = ""+jj;
			old_name = line;
			// new name
			name = "waiver_"+waiver_num+"_"+id+".pdf";
			pstmt.setString(1, id);
			pstmt.setString(2, waiver_id);
			pstmt.setString(3, name);
			if(line.indexOf(' ') > 0){
			    line = line.replaceAll("\\s","_");
			}
			pstmt.setString(4, line); // old_file_name
			pstmt.setString(5, type);
			pstmt.executeUpdate();
			jj++;
			File oldfile =new File(path+old_name);
			File newfile =new File(path+name);
			if(!oldfile.renameTo(newfile)){
			    System.out.println("Rename Failed");
			    System.err.println("Old "+old_name);
			    System.err.println("New "+name);
			}
		    }
		    // if(jj > 500) break;
		}
		System.err.println("id: "+id+", waiver_id:"+waiver_id+" w_num:"+waiver_num+" line: "+line+", str: "+str);
	    }
	    reader.close();
	}
	catch(Exception ex){
	    System.err.println(ex);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);									
	}
	return back;
    }
    public String prepareMap(){
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String back = "", qq = "select id,waiver_num from waivers";
	try{
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB";
		return back;
	    }
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
		String str = rs.getString(1);
		String str2 = rs.getString(2);
		if(str2 != null && !str2.equals("")){
		    map.put(str2, str);
		}
	    }
	}
	catch(Exception ex){
	    System.err.println(ex);
	    back += ex;
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);									
	}
	return back;
    }
    /**
       vid = 38 // city
       insert into sales values(0,'Trash',38,now(),5796001,5829000,6600,'Billed','voided',null,null,0,0,null,null,0,2,1);
       insert into sales values(0,'Trash',38,now(),5785501, 5796000,2100,'Billed','voided',null,null,0,0,null,null,0,2,1);

       insert into sales values(0,'Yard',38,now(),867001,900000,6600,'Billed','voided',null,null,0,0,null,null,0,2,1);

       insert into sales values(0,'Yard',38,now(),861001,867000,1200,'Billed','voided',null,null,0,0,null,null,0,2,1);





    */
}
