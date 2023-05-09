/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.model;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import javax.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import annex.list.*;
import annex.utils.*;

public class ReportStats{
	
    static Logger logger = LogManager.getLogger(ReportStats.class);
    static final long serialVersionUID = 174L;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    NumberFormat currFormat = NumberFormat.getCurrencyInstance();	
    String year = "",date_from="",date_to="", signed_date="";
    String title = "", which_date="",by="", prev_year="", next_year="";
    boolean debug = false;
    boolean signed=true, allWaivers=false;
    List<List<ReportRow>> all = new ArrayList<List<ReportRow>>();
    Hashtable<String, ReportRow> all2 = new Hashtable<String, ReportRow>(4);
    DecimalFormat decFormat = new DecimalFormat("###,###.##");
    List<ReportRow> rows = null; 
    ReportRow columnTitle = null;
    //
    int totalIndex = 2; // DB index for row with 2 items
    public ReportStats(){

    }

    public void setYear(String val){
	if(val != null && !val.equals("-1"))
	    year = val;
    }
    public void setPrev_year(String val){
	if(val != null && !val.equals("-1"))
	    prev_year = val;
    }
    public void setNext_year(String val){
	if(val != null && !val.equals("-1"))
	    next_year = val;
    }	

    public void setDate_from(String val){
	if(val != null)
	    date_from = val;
    }	
    public void setDate_to(String val){
	if(val != null)
	    date_to = val;
    }
    public void setSigned_date(String val){
	if(val != null)
	    signed_date = val;
    }		
    public void setBy(String val){
	if(val != null)
	    by = val;
    }
    public void setSigned(Boolean val){
	signed = val;
    }
    public void setAllWaivers(Boolean val){ // all
	allWaivers = val;
    }	
    //
    // getters
    //
    public String getYear(){
	return year;
    }
    public String getPrev_year(){
	return prev_year;
    }
    public String getNext_year(){
	return next_year;
    }	

    public String getDate_from(){
	return date_from ;
    }	
    public String getDate_to(){
	return date_to ;
    }
    public String getSigned_date(){
	return signed_date ;
    }		
    public String getBy(){
	return by ;
    }

    public boolean getSigned(){
	return signed;
    }	
    public boolean getAllWaivers(){
	return allWaivers;
    }
    public String getTitle(){
	return title;
    }	
    public List<ReportRow> getRows(){
	return rows;
    }

    public List<List<ReportRow>> getAll(){
	return all;
    }


    public ReportRow getColumnTitle(){
	return columnTitle;
    }
    public String find(){
	String msg = "";
	if(signed){
	    msg += signedStats();
	}
	return msg;
    }
    void setTitle(){
	if(!year.equals("")){
	    title +=" "+year;
	}
	else {
	    if(!date_from.equals("")){
		title += " "+date_from;
	    }
	    if(!date_to.equals("")){
		if(!date_from.equals(date_to)){
		    title += " - "+date_to;
		}
	    }
	}
    }
    /**
     * signed stats
     */
    public String signedStats(){
		
	Connection con = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;				
	ResultSet rs = null;

	String msg = "";
	String which_date = "";
	String qq = "", qq2="", qq3="", qq4="";
	title = "Signed Waivers ";
	setTitle();
	rows = new ArrayList<ReportRow>();		
	ReportRow one = new ReportRow(debug, 2);
	one.setRow("Title", title);
	rows.add(one);
	one = new ReportRow(debug, 3);
	one.setRow("Title","Count","Percent(%)");
	rows.add(one);
	//
	// Permits
	//
	qq = " select count(*) from waivers w where w.in_out_city='OUT' ";
	qq2 = " select count(*) from waivers w where w.in_out_city='OUT' ";
	qq3 = " select count(*) from waivers w where signed_date is not null ";
	qq4 = " select count(*) from waivers w where signed_date is not null ";
	//
	if(!signed_date.equals("")){
	    qq2 += " and w.signed_date <= ? ";
	    qq4 += " and w.signed_date <= ? ";						
	}

	logger.debug(qq);
	con = Helper.getConnection();
	if(con == null){
	    msg = "Could not connect ";
	    return msg;
	}
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    pstmt4 = con.prepareStatement(qq4);
	    int jj=1;
	    if(!signed_date.isEmpty()){
		pstmt2.setDate(jj, new java.sql.Date(dateFormat.parse(signed_date).getTime()));
		jj++;
	    }
	    rs = pstmt.executeQuery();
	    int total = 0;
	    if(rs.next()){
		total += rs.getInt(1);
	    }
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		int cnt = rs.getInt(1);
		one = new ReportRow(debug, 3);			
		one.setRow("Before or ="+signed_date,""+cnt, decFormat.format((cnt/(total+0.0))*100));
		rows.add(one);
		// all.add(rows);
		one = new ReportRow(debug, 3);			
		one.setRow("After "+signed_date,""+(total-cnt), decFormat.format(((total - cnt)/(total+0.0))*100));
		rows.add(one);
	    }
	    one = new ReportRow(debug, 3);
	    one.setRow("Total",
		       ""+total,
		       "100.00");
	    rows.add(one);
	    all.add(rows);
	    //
	    // second table
	    rows = new ArrayList<ReportRow>();		
	    one = new ReportRow(debug, 2);
	    title = "Signed Waivers without IN/OUT City";
	    one.setRow("Title", title);
	    rows.add(one);
	    one = new ReportRow(debug, 3);
	    one.setRow("Title","Count","Percent(%)");
	    rows.add(one);
	    jj=1;
	    if(!signed_date.isEmpty()){
		pstmt4.setDate(jj, new java.sql.Date(dateFormat.parse(signed_date).getTime()));
		jj++;
	    }
	    rs = pstmt3.executeQuery();
	    total = 0;
	    if(rs.next()){
		total += rs.getInt(1);
	    }
	    rs = pstmt4.executeQuery();
	    if(rs.next()){
		int cnt = rs.getInt(1);
		one = new ReportRow(debug, 3);			
		one.setRow("Before or ="+signed_date,""+cnt, decFormat.format((cnt/(total+0.0))*100));
		rows.add(one);
		// all.add(rows);
		one = new ReportRow(debug, 3);			
		one.setRow("After "+signed_date,""+(total-cnt), decFormat.format(((total - cnt)/(total+0.0))*100));
		rows.add(one);
	    }
	    one = new ReportRow(debug, 3);
	    one.setRow("Total",
		       ""+total,
		       "100.00");
	    rows.add(one);
	    all.add(rows);
	}catch(Exception e){
	    msg += e+":"+qq;
	    logger.error(msg);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4);
	}		
	return msg;
    }
		
}






















































