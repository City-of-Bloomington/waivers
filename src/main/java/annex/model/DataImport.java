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
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class DataImport{

    boolean debug = true;
    static Logger logger = LogManager.getLogger(DataImport.class);
    static final long serialVersionUID = 220L;			
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Map<String, Entity> map = new HashMap<>();
    public DataImport(){

    }
    public String doImport(String fileName){
	//
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qq = "insert into waivers "+
	    " (id,"+
	    " waiver_num,"+
	    " waiver_instrument_num,"+
	    " deed_book,"+
	    " deed_page,"+
						
	    " parcel_tax_id,"+
	    " legal_description,"+
	    " sec_twp_range_dir,"+
	    " development_subdivision,"+
	    " notes,"+
						
	    " in_out_city,"+
	    " gis_notes,"+
	    " signed_date,"+
	    " recorder_date,"+
	    " acrage,"+
						
	    " lot,"+
	    " deed_instrument_num,"+
	    " date,"+
	    " waiver_book,"+
	    " waiver_page,"+

	    " scanned_date,"+
	    " expire_date,"+
	    " imported,"+
	    " status, "+
	    " parcel_pin, "+
	    " mapped_date "+
	    " ) "+
						
	    " values(?,?,?,?,?, ?,?,?,?,?, "+
	    " ?,?,?,?,?, ?,?,?,?,?, "+
	    " ?,?, 'y', 'Completed',?, ?)";				
	String qq2 = "insert into entity_waivers values(?,?)";
	String qq3 = "insert into addresses (id, waiver_id,street_address) values(?,?,?)";
	/*
	  clean up for import
	  delete from entity_waivers;delete from entities;delete from tasks; delete from addresses; delete from email_logs;delete from attachments; delete from waivers;
					
	  // this is the excel header
	  row,  // 0
	  Waiver ID,
	  isBusiness, // y
	  isTrust, // y
	  OWNER 1 at Signing,
	  OWNER 2 at Signing, // 5
					 
	  Owner 3 at Signing, // 
	  SERVICE ADDRESS (ADDRESS OF HOOK-UP),
	  Date WAIVER Signed,  
	  Date WAIVER Recorded,
	  Expire Date,  // new 10
					 
	  waiver book,  // 
	  waiver page,  
	  Waiver Instrument # (Recorder's ID), 
	  Current Parcel Number, // parcel_PIN
	  Former Parcel number, // parcel_tax_id 15
					 
	  Acrage,
	  Bad acrage,  // ignore 
	  legal description,
	  sec-twp-range-dir,  
	  Development/Subdivision, // 20
					 
	  lot,
	  Deed instrument num, 
	  Deed book,
	  Deed page,
	  Waiver scanned, // Y,N 25
					 
	  Notes,
	  In/out city,   // I/O  
	  In GIS, // Y, N
	  Mapped date,
	  GIS notes  // 30

	*/
	String back = "";
	Reader in = null;
	int jj=1; // use for address aa well
	try{
	    con = Helper.getConnection();
	    if(con == null){
		back = "Could not connect to DB";
		return back;
	    }
	    pstmt = con.prepareStatement(qq);
	    pstmt2 = con.prepareStatement(qq2);
	    pstmt3 = con.prepareStatement(qq3);
	    in = new FileReader(fileName);
	    //
	    // if you want to define the header 
	    // Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ID", "Last Name", "First Name").parse(in);
	    //
	    // header auto detection
	    // Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
	    Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
	    //
	    // dups
	    for (CSVRecord record : records) {
		String id = ""+jj;
		String date = "";
		boolean is_business = false, is_trust=false;
		// reocrd.get(0) row  we ignore
		String waiver_id = record.get(1);// ("WAIVER_ID"); // 1
		// skip the first line
		if(waiver_id.startsWith("waiver")) continue;
		String isBusiness =record.get(2);
		if(!isBusiness.equals(""))
		    is_business = true;
		String isTrust =record.get(3);
		if(!isTrust.equals(""))
		    is_trust = true;								
		String owner1 = record.get(4);// ("OWNER 1 at Signing"); // 4
		String owner2 = record.get(5);// ("OWNER 2 at Signing"); // 5
		String owner3 = record.get(6);
								
		String hookup_addr = record.get(7);// 
		String date_signed = record.get(8);// ("Date Signed"); 
		String date_recorded = record.get(9);// ("Date Recorded");
		String expire_date = record.get(10);// 
										
		String waiver_book = record.get(11);
		String waiver_page = record.get(12);								
		String waiver_instrument_num = record.get(13);
		String parcel_pin = record.get(14);
		String parcel_tax_id = record.get(15);
								
		String acrage = record.get(16);
		// ignore 17; // bad acrage
		String legal_description = record.get(18);
		String sec_twp_range_dir = record.get(19);//sec-twp-range-dir
		String development_subdivision = record.get(20);
		String lot = record.get(21);
		String deed_instrument_num = record.get(22);

		String deed_book = record.get(23);
		String deed_page = record.get(24);
		String waiver_scanned = record.get(25); // not used
		String notes = record.get(26);// ("Notes"); 
		String in_out = record.get(27);// ("I/O");   
								
		String inGis = record.get(28);// ("INGIS"); // 21
		String mapped_date = record.get(29);
		String gis_notes = record.get(30);// ("GIS Mapping Notes"); 
		/*
		  if(!old_tax_id.equals("")){
		  if(!tax_id.equals("")) tax_id += ", ";
		  tax_id += old_tax_id;
		  }
		*/
		if(waiver_id.equals("")) continue; // empty record
		else if(waiver_id.startsWith("Waiver")) continue; // first row
		if(!date_signed.equals("")){
		    date = date_signed;
		}
		else if(!date_recorded.equals("")){
		    date = date_recorded;
		}
		//
		// String recorder_notes = recorder_id;
		System.err.println(" id: "+id+" "+waiver_id);
		System.err.print(" owner1: "+owner1);
		System.err.println(" owner2: "+owner2);
		System.err.print(" date signed: "+date_signed);
		System.err.println(" date record: "+date_recorded);
		System.err.println(" pin: "+parcel_pin);								
		System.err.println(" addr: "+hookup_addr);
		System.err.println(" instrum #: "+deed_instrument_num);
		System.err.print(" notes: "+notes);
		System.err.print("in_out: "+in_out);
		System.err.println(" gis notes: "+gis_notes);
		System.err.println(" legal desc: "+legal_description);
		int npos = -1, npos2=-1;
		Waiver waiver = new Waiver(id,
					   waiver_id, // waiver_num
					   waiver_instrument_num,
					   deed_book,
					   deed_page,
																					 
					   parcel_pin,
					   legal_description,
					   sec_twp_range_dir,
					   development_subdivision,
					   notes,
																					 
					   in_out,
					   gis_notes,
					   date_signed,
					   date_recorded,
					   acrage,
																					 
					   lot,
					   deed_instrument_num,
					   date,
					   waiver_book,
					   waiver_page,
																					 
					   expire_date,
					   parcel_tax_id,
					   mapped_date
					   );
		back = waiver.doSaveForImport(pstmt);
		if(!back.equals("")){
		    break;
		}
		back = addAddress(id, hookup_addr, pstmt3);
		//
		// if business owner1 will be business name
		//
		if(owner1 != null && !owner1.equals("")){
		    if(is_business){
			handleBusiness(waiver, pstmt2, owner1);
		    }
		    else if(is_trust){
			handleTrust(waiver, pstmt2, owner1);
		    }
		    else{
			handleOwner(waiver, pstmt2, owner1);
		    }
		}
		if(owner2 != null && !owner2.equals("")){
		    handleOwner(waiver, pstmt2, owner2);
		}
		if(owner3 != null && !owner3.equals("")){
		    handleOwner(waiver, pstmt2, owner3);
		}								
		jj++;								
		// if(jj > 500) break; // last record in this file
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
    public String addAddress(String id,
		      String address,
		      PreparedStatement pstmt){
	String back = "";
				
	if(address != null){
	    try{
		pstmt.setString(1, id);
		pstmt.setString(2, id);
		pstmt.setString(3, address);
		pstmt.executeUpdate();
	    }catch(Exception ex){
		back += ex;
		System.err.println(ex);
	    }
	}
	return back;
    }		
    public void handleOwner(Waiver waiver,
		     PreparedStatement pstmt,
		     String owner){
	if(owner != null && !owner.trim().equals("")){
	    // we may use ; instead for 2 or more
	    if(owner.indexOf(";") > 0){
		String[] names = owner.split(";");
		if(names.length > 0){
		    for(String name:names){
			addOwners(waiver, pstmt, name);
		    }
		}
	    }
	    else{
		addOwners(waiver, pstmt, owner);
	    }
	}
    }
    void addOwners(Waiver waiver,
		   PreparedStatement pstmt,
		   String name){
	if(name.trim().equals("")) return;
	if(map.containsKey(name)){
	    Entity owner = map.get(name);
	    String owner_id = owner.getId();
	    waiver.setAddEntityId(owner_id);
	    waiver.doAddEntity(pstmt);
	}
	else {
	    Entity owner = new Entity();
	    owner.setWaiver_id(waiver.getId());
	    owner.setName(name);
	    String back = owner.doSave();
	    if(back.equals("")){
		map.put(name, owner);
	    }
	}
    }
		
    void handleBusiness(Waiver waiver,
			PreparedStatement pstmt,
			String owner){
	if(owner != null && !owner.equals("")){
	    addBusiness(waiver, pstmt, owner);
	}
    }		
    void addBusiness(Waiver waiver,
		     PreparedStatement pstmt,
		     String name){
	if(name.trim().equals("")) return;
	if(map.containsKey(name)){
	    Entity owner = map.get(name);
	    String owner_id = owner.getId();
	    waiver.setAddEntityId(owner_id);
	    waiver.doAddEntity(pstmt);
	}
	else {
	    Entity owner = new Entity();
	    owner.setWaiver_id(waiver.getId());
	    owner.setName(name);
	    owner.setIsBusiness(true);
	    String back = owner.doSave();
	    if(back.equals("")){
		String owner_id = owner.getId();
		if(!owner_id.equals("")){
		    map.put(name, owner);
		}
	    }
	}
    }
    void handleTrust(Waiver waiver,
		     PreparedStatement pstmt,
		     String owner){
	if(owner != null && !owner.equals("")){
	    addTrust(waiver, pstmt, owner);
	}
    }		
    void addTrust(Waiver waiver,
		  PreparedStatement pstmt,
		  String name){
	if(name.trim().equals("")) return;
	if(map.containsKey(name)){
	    Entity owner = map.get(name);
	    String owner_id = owner.getId();
	    waiver.setAddEntityId(owner_id);
	    waiver.doAddEntity(pstmt);
	}
	else {
	    Entity owner = new Entity();
	    owner.setWaiver_id(waiver.getId());
	    owner.setName(name);
	    owner.setIsTrust(true);
	    String back = owner.doSave();
	    if(back.equals("")){
		String owner_id = owner.getId();
		if(!owner_id.equals("")){
		    map.put(name, owner);
		}
	    }
	}
    }		
}
