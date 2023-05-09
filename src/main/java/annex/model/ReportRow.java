/**
 * @copyright Copyright (C) 2014-2015 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
package annex.model;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import annex.list.*;
import annex.utils.*;

public class ReportRow implements java.io.Serializable{

    String row[]={"",""};
    boolean debug = false;
    int size = 2;
    static final long serialVersionUID = 190L;		
    static Logger logger = LogManager.getLogger(ReportRow.class);
    public ReportRow(){
    }
    public ReportRow(boolean deb){
	debug = deb;
    }	
    public ReportRow(boolean deb, int size){
	debug = deb;
	setSize(size);
    }
    //
    //
    public String getRowItem(int jj){
	if(jj < 0 || jj >= size) return null;
	return row[jj];
    }
    public int getSize(){
	return size;
    }
    public String getFirst(){
	return row[0];
    }
    public String getSecond(){
	return row[1];
    }
    public String getThird(){
	if(2 < size)
	    return row[2];
	return "";
    }
    public String getForth(){
	if(3 < size)
	    return row[3];
	return "";
    }	
    //
    // setters
    //
    /**
     * setting array size, can not be less than 2
     * @param size int value
     */
    public void setSize(int size){
	if(size > this.size){
	    row = new String[size];
	    for(int jj=0;jj<size;jj++){
		row[jj] = "0"; // initialize;
	    }
	}
	this.size = size;
    }
    public void setRow(int jj, String val){
	if(val != null && jj < size)
	    row[jj] = val;
    }
    /**
     * since many report will be two columns
     */
    public void setRow(String val, String val2){
	if(val != null){
	    row[0] = val;
	}
	if(val2 != null){
	    row[1] = val2;
	}		
    }
	
    /**
     * we may have some report with three columns 
     */
    public void setRow(String val, String val2, String val3){
	if(val != null){
	    row[0] = val;
	}
	if(val2 != null){
	    row[1] = val2;
	}
	if(val3 != null && size > 2){
	    row[2] = val3;
	}		
    }
    //
    public void setRow(String ... vals){
	if(vals != null && vals.length == size){
	    row = vals;
	}
    }
    //
    public String toString(){
	String ret = "";
	for(int jj=0;jj<size;jj++){
	    if(!ret.equals("")) ret += ", ";
	    ret += row[jj];
	}
	return "["+ret+"]";
    }
	
}
