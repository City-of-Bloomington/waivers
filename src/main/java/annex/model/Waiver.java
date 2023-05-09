package annex.model;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.sql.*;
import java.util.List;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.list.*;
import annex.utils.*;

public class Waiver extends CommonInc implements java.io.Serializable{

    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yy"); // paper_verified_date in mm/dd/yy format from excel		
    static final long serialVersionUID = 310L;	
    static Logger logger = LogManager.getLogger(Waiver.class);		
    String id="",
	waiver_num="",
	deed_book="", deed_page="",parcel_pin="",
	waiver_book="", waiver_page="",
	deed_instrument_num="", acreage="",sec_twp_range_dir="",
	development_subdivision="",lot="",
	date="", expire_date="",

	waiver_instrument_num="", 
	business_name="", legal_description="", parcel_tax_id="",
	gis_notes="", mapped_date="", signed_date="", 
	recorder_date="", paper_verified_date="",
	scanned_date="", notes="",
	hookup_address="", 
				
	in_out_city="",imported="", added_by="",status="",
	closed_by="", closed_date="",  invalid_addr="", is_business="",
	is_trust="";
    boolean reviewUpdate = false;
    String user_id = "", add_entity_id="", del_entity="", entity_name="";
    List<Task> completedTasks = null;
    List<Entity> entities = null;
    List<FileUpload> uploads = null;
    List<Address> addresses = null;
    String entity_id="", address_id=""; // for remove
    String add_addr_ids = ""; // string of address_ids
    String add_entity_ids = "";
    //
    // uncompleted tasks
    //
    List<Task> tasks = null;
		
    //
    public Waiver(){
	super();
    }	
    public Waiver(boolean deb, String val){
	super(deb);
	setId(val);
    }
    public Waiver(boolean deb, String val, String val2){
	super(deb);
	setId(val);
	setWaiverNum(val2);
    }		
    public Waiver(boolean deb,
		  String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5,
									
		  String val6,
		  String val7,
		  String val8,
		  String val9,
		  String val10,
									
		  String val11,
		  String val12,
		  String val13,
		  String val14,
		  String val15,

		  String val16,									
		  String val17,
		  String val18,
		  String val19,
		  String val20,
									
		  String val21,
		  String val22,
		  String val23,
		  String val24,
		  String val25,
									
		  String val26,
		  String val27,
		  String val28,
		  String val29,
		  boolean val30

		  ){
	super(deb);
	setValues(val,
		  val2,
		  val3,
		  val4,
		  val5,
									
		  val6,
		  val7,
		  val8,
		  val9,
		  val10,
									
		  val11,
		  val12,
		  val13,
		  val14,
		  val15,
									
		  val16,
		  val17,
		  val18,
		  val19,
		  val20,
									
		  val21,
		  val22,
		  val23,									
		  val24,
		  val25,
									
		  val26,
		  val27,
		  val28,
		  val29,
		  val30
		  );
    }
    // this is used for import Need fix
    public Waiver(String val,
		  String val2,
		  String val3,
		  String val4,
		  String val5,
									
		  String val6,
		  String val7,
		  String val8,
		  String val9,
		  String val10,
									
		  String val11,
		  String val12,
		  String val13,
		  String val14,
		  String val15,
									
		  String val16,
		  String val17,
		  String val18,
		  String val19,
		  String val20,

		  String val21,
		  String val22,
		  String val23
		  ){
	setId(val);
	setWaiverNum(val2);
	setWaiverInstrumentNum(val3);
	setDeedBook(val4);
	setDeedPage(val5); // 5
				
	setParcelPin(val6);
	setLegalDescription(val7);
	setSecTwpRangeDir(val8);
	setDevelopmentSubdivision(val9);
	setNotes(val10); // 10
				
	setInOutCity(val11);
	setGisNotes(val12);				
	setSignedDate(val13);
	setRecorderDate(val14);
	setAcreage(val15);
				
	setLot(val16);
	setDeedInstrumentNum(val17);
	setDate(val18);
	setWaiverBook(val19);
	setWaiverPage(val20);
				
	setExpireDate(val21);
	setParcelTaxId(val22);
	setMappedDate(val23);
	//
    }

    void setValues(
		   String val,
		   String val2,
		   String val3,
		   String val4,
		   String val5,
									 
		   String val6,
		   String val7,
		   String val8,
		   String val9,
		   String val10,
									 
		   String val11,
		   String val12,
		   String val13,
		   String val14,
		   String val15,
									 
		   String val16,
		   String val17,
		   String val18,
		   String val19,
		   String val20,
									 
		   String val21,
		   String val22,
		   String val23,
		   String val24,
		   String val25,
									 
		   String val26,
		   String val27,
		   String val28,
		   String val29,
		   boolean val30

		   ){
	setId(val);
	setWaiverNum(val2);
	setWaiverInstrumentNum(val3);
	setDeedBook(val4);
	setDeedPage(val5);

	setWaiverBook(val6);
	setWaiverPage(val7);
	setParcelPin(val8);
	setLegalDescription(val9);
	setParcelTaxId(val10);
				
	setDeedInstrumentNum(val11);
	setAcreage(val12);
	setSecTwpRangeDir(val13);
	setDevelopmentSubdivision(val14);
	setLot(val15);

	setNotes(val16);
	setSignedDate(val17);				
	setScannedDate(val18);
	setRecorderDate(val19);
	setPaperVerifiedDate(val20);
				
	setInOutCity(val21);
	setMappedDate(val22);
	setGisNotes(val23);
	setExpireDate(val24);
	setStatus(val25);
				
	setDate(val26);
	setClosedBy(val27);
	setClosedDate(val28);
	setAddedBy(val29);
	setImported(val30); // bool


    }
    public boolean equals(Object obj){
	if(obj instanceof Waiver){
	    Waiver one =(Waiver)obj;
	    return id.equals(one.getId());
	}
	return false;				
    }
    public int hashCode(){
	int seed = 37;
	if(!id.equals("")){
	    try{
		seed += Integer.parseInt(id);
	    }catch(Exception ex){
	    }
	}
	return seed;
    }
    //
    // getters
    //
    public String getId(){
	return id;
    }
    public String getWaiverNum(){
	if(id.equals("")){
	    findNextWaiverNum();
	}
	return waiver_num;
    }		
    public String getDeedInstrumentNum(){
	return
	    deed_instrument_num ;
    }
    public String getWaiverInstrumentNum(){
	return
	    waiver_instrument_num ;
    }		
    public String getAddEntityId(){
	return
	    add_entity_id ;
    }
    public String getDeedBook(){
	return deed_book;
    }
    public String getDeedPage(){
	return deed_page;
    }
    public String getWaiverBook(){
	return waiver_book;
    }
    public String getWaiverPage(){
	return waiver_page;
    }		
    public String getParcelPin(){
	return parcel_pin;
    }
    public String getAcrage(){
	return acreage;
    }
    public String getAcreage(){
	return acreage;
    }
    public String getSecTwpRangeDir(){
	return sec_twp_range_dir;
    }
    public String getDevelopmentSubdivision(){
	return development_subdivision;
    }
    public String getLot(){
	return lot;
    }

    public String getEntityName(){
	return ""; // auto_complete 
    }
    public String getLegalDescription(){
	return
	    legal_description ;
    }
    public String getParcelTaxId(){
	return
	    parcel_tax_id ;
    }
		
    public String getMappedDate(){
	return
	    mapped_date ;
    }
    public String getGisNotes(){
	return
	    gis_notes;
    }
    public String getNotes(){
	return
	    notes;
    }		
		
    public String getHookupAddress(){
	return
	    hookup_address ;
    }
    public String getRecorderDate(){
	return
	    recorder_date;
    }
    public String getScannedDate(){
	return
	    scanned_date;
    }		
    public String getInOutCity(){
	return
	    in_out_city ;
    }
    public String getPaperVerifiedDate(){
	return
	    paper_verified_date;
    }

		
    public boolean getImported(){
	return
	    !imported.equals("") ;
    }
    // needed to know if no printing is needed anymore
    public boolean isSigned(){
	return !signed_date.equals("");
    }
    public String getAddedBy(){
	return
	    added_by ;
    }
    public String getClosedBy(){
	return
	    closed_by ;
    }
    public String getClosedDate(){
	return
	    closed_date ;
    }		
    public String getStatus(){
	return status;
    }
    public boolean isOpen(){
	return status.equals("Open") || status.isEmpty();
    }
    public boolean isClosed(){
	return status.equals("Closed");
    }		
    // added date, for old data we use signed_date
    public String getDate(){
	return date;
    }
    public String getSignedDate(){
	return signed_date;
    }		
    public String getExpireDate(){
	return expire_date;
    }
    public String getEntitiesInfo(){
	String ret = "";
	if(entities == null)
	    getEntities();
	if(entities != null){
	    for(Entity one:entities){
		if(!ret.equals("")) ret += ", ";
		ret += one;
	    }
	}
	return ret;
    }
    public String getWaiverBookPage(){
	String ret = waiver_book.trim();
	if(!waiver_page.trim().equals("")){
	    if(!ret.equals("")){
		ret += "/";
	    }
	    ret += waiver_page;
	}
	return ret;
    }
    public String getDeedBookPage(){
	String ret = deed_book.trim();
	if(!deed_page.trim().equals("")){
	    if(!ret.equals("")){
		ret += "/";
	    }
	    ret += deed_page;
	}
	return ret;
    }
    public String getLotAcreage(){
	String ret = lot.trim();
	if(!acreage.trim().equals("")){
	    if(!ret.equals("")){
		ret += "/";
	    }
	    ret += acreage;
	}
	return ret;
    }		
    public String getLotAcrage(){
	return getLotAcreage();
    }
		
    public boolean hasGisNotes(){
	return !gis_notes.equals("");
    }
    //
    // setters
    //
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setWaiverNum(String val){
	if(val != null)
	    waiver_num = val;
    }
    public void setDeedBook(String val){
	if(val != null)
	    deed_book = val;
    }
    public void setDeedPage(String val){
	if(val != null)
	    deed_page = val;
    }
    public void setWaiverBook(String val){
	if(val != null)
	    waiver_book = val;
    }
    public void setWaiverPage(String val){
	if(val != null)
	    waiver_page = val;
    }
		
    public void setParcelPin(String val){
	if(val != null)
	    parcel_pin = val;
    }
    public void setParcelPin2(String val){
	// 
    }		
    public void setDeedInstrumentNum(String val){
	if(val != null)
	    deed_instrument_num = val;
    }
    public void setWaiverInstrumentNum(String val){
	if(val != null)
	    waiver_instrument_num = val;
    }
    public void setAcreage(String val){
	if(val != null)
	    acreage = val;
    }
    public void setAcrage(String val){
	setAcreage(val);
    }		
    public void setSecTwpRangeDir(String val){
	if(val != null)
	    sec_twp_range_dir = val;
    }
    public void setDevelopmentSubdivision(String val){
	if(val != null)
	    development_subdivision = val;
    }
    public void setLot(String val){
	if(val != null)
	    lot = val;
    }
    public void setAddEntityId(String val){
	if(val != null)
	    add_entity_id = val;
    }
    public void setEntityName(String val){
	// for auto_complete 
    }

    public void setLegalDescription(String val){
	if(val != null)
	    legal_description = val;
    }
    public void setParcelTaxId(String val){
	if(val != null)
	    parcel_tax_id = val;
    }
    // not needed just for interface
    public void setParcelTaxId2(String val){

    }		
    public void setHookupAddress(String val){
	if(val != null)
	    hookup_address = val;
    }
    public void setNotes(String val){
	if(val != null)
	    notes = val;
    }
    public void setGisNotes(String val){
	if(val != null)
	    gis_notes = val;
    }
    public void setRecorderDate(String val){
	if(val != null)
	    recorder_date = val;
    }
    public void setScannedDate(String val){
	if(val != null)
	    scanned_date = val;
    }		
    public void setInOutCity(String val){
	if(val != null)
	    in_out_city = val;
    }

    public void setInvalidAddr(boolean val){
	if(val)
	    invalid_addr = "y";
    }
    public void setImported(boolean val){
	if(val)
	    imported = "y";
    }
    public void setAddedBy(String val){
	if(val != null)
	    added_by = val;
    }
    public void setClosedBy(String val){
	if(val != null)
	    closed_by = val;
    }
    public void setClosedDate(String val){
	if(val != null)
	    closed_date = val;
    }		
    public void setStatus(String val){
	if(val != null)
	    status = val;
    }
    public void setDate(String val){
	if(val != null)
	    date = val;
    }
    public void setMappedDate(String val){
	if(val != null)
	    mapped_date = val;
    }		
    public void setSignedDate(String val){
	if(val != null)
	    signed_date = val;
    }
    public void setPaperVerifiedDate(String val){
	if(val != null)
	    paper_verified_date = val;
    }		
    //
    // expire_date equals 15 years from date
    // but only for waivers after 06/2015
    //
    public void setExpireDate(String val){
	if(val != null)
	    expire_date = val;
    }		
    public void setUserId(String val){
	if(val != null)
	    user_id = val;
    }
    public void setDelEntity(String val){
	if(val != null)
	    del_entity = val;
    }
    public void setAddAddrIds(String val){
	add_addr_ids = val;
    }
    public void setAddEntityIds(String val){
	add_entity_ids = val;
    }		
    public boolean hasAddedBy(){
	return !added_by.equals("");
    }
    public boolean canBePrinted(){
	if(isOpen()
	   && hasEntities()
	   && hasMoreTasks()
	   && signed_date.equals("")){
	    if(tasks.size() > 0){
		for(Task task:tasks){
		    // if this is prepare a waiver, then we can print
		    if(task.getName().startsWith("Prepare")){
			return true;
		    }
		}
	    }
	    // even if prepared task was done but still can be printed
	    // 
	    if(hasCompletedTasks()){
		for(Task task:completedTasks){
		    // if this is prepare a waiver, then we can print
		    if(task.getName().startsWith("Prepare")){
			return true;
		    }
		}
	    }
	}
	return false;
    }
    //
    // Expire date is only required when a waiver is recorded
    //
    public boolean needExpireDate(){
	return !expire_date.equals("") || !expire_date.equals("");
    }
    public void setEntityId(String val){
	if(val != null)
	    entity_id = val;
    }
    public void setAddressId(String val){
	if(val != null)
	    address_id = val;
    }		
    public User getAddedByUser(){
	if(!added_by.equals("")){
	    User one = new User(debug, added_by);
	    String back = one.doSelect();
	    if(back.equals(""))
		return one;
	}
	return null;
    }
    public User getClosedByUser(){
	if(!closed_by.equals("")){
	    User one = new User(debug, closed_by);
	    String back = one.doSelect();
	    if(back.equals(""))
		return one;
	}
	return null;
    }		
    public String toString(){
	return id;
    }
    public List<Task> getCompletedTasks(){
	if(completedTasks == null){
	    findTasks();
	}
	return completedTasks;
    }
    public boolean hasMoreTasks(){
	if(!id.equals("") && status.equals("Open")){
	    findTasks();
	    return tasks != null;
	}
	return false;
    }
    public boolean hasCompletedTasks(){
	if(!id.equals("")){
	    if(completedTasks == null){
		findTasks();
	    }
	}
	return completedTasks != null && completedTasks.size() > 0;
    }
    public List<Task> getTasks(){
	if(tasks == null && !id.equals("")){
	    findTasks();
	}
	return tasks;
    }
    public List<FileUpload> getUploads(){
	logger.debug(" get uploads ");
	if(uploads == null){
	    FileUploadList ful = new FileUploadList();
	    ful.setWaiver_id(id);
	    String back = ful.find();
	    if(back.equals("")){
		List<FileUpload> ones = ful.getUploads();
		if(ones != null && ones.size() > 0);
		uploads = ones;
	    }
	    else{
		logger.error(back);
	    }
	}
	return uploads;
    }
    public String getUploadCount(){
	getUploads();
	if(uploads != null)
	    return ""+uploads.size();
	return "";
				
    }
    public List<Address> getAddresses(){
	logger.debug(" get addresses ");
	if(addresses == null){
	    AddressList adl = new AddressList(debug, id);
	    String back = adl.find();
	    if(back.equals("")){
		List<Address> ones = adl.getAddresses();
		if(ones != null && ones.size() > 0);
		addresses = ones;
	    }
	}
	return addresses;
    }
    public boolean hasAddresses(){
	getAddresses();
	return addresses != null && addresses.size() > 0;
    }
    public String getAddressInfo(){
	logger.debug(" address info ");
	String ret = "";
	if(hasAddresses()){
	    for(Address one:addresses){
		if(!ret.equals("")) ret += ", ";
		ret +=  one;
	    }
	}
	return ret;
    }
    public String getBasicInfo(){
	String ret = getAddressInfo();
	return ret;
    }
    public String getBasicInfo2(){
	String ret = getEntitiesInfo();
	return ret;
    }
    public String getBasicInfo3(){
	String ret = "";		
	if(!legal_description.equals("")){
	    ret += legal_description;
	}
	if(!notes.equals("")){
	    if(!ret.equals("")) ret += ", "; 
	    ret += notes;
	}
	return ret;
    }
    public boolean hasUploads(){
	getUploads();
	return uploads != null && uploads.size() > 0;
    }
    private void findTasks(){
	logger.debug(" find tasks ");
	if(!id.equals("")){
	    if(completedTasks == null){
		TaskList al = new TaskList(debug, id);
		al.setCompleted();
		String back = al.find();
		if(back.equals("")){
		    List<Task> ones = al.getTasks();
		    if(ones != null && ones.size() > 0){
			completedTasks = ones;
		    }
		}
		else{
		    logger.error(back);
		}
	    }
	    if(tasks == null){
		TaskList al = new TaskList(debug, id);
		al.setUncompleted();
		String back = al.find();
		if(back.equals("")){
		    List<Task> ones = al.getTasks();
		    if(ones != null && ones.size() > 0){
			tasks = ones;
		    }
		}
		else{
		    logger.error(back);
		}
	    }
	}
    }
    public List<Entity> getEntities(){
	if(entities == null && !id.equals("")){
	    EntityList ol = new EntityList();
	    ol.setWaiver_id(id);
	    String back = ol.findForWaiver();
	    if(back.equals("")){
		List<Entity> ones = ol.getEntities();
		if(ones != null && ones.size() > 0)
		    entities = ones;
	    }
	    else{
		logger.error(back);
	    }
	}
	return entities;
    }
    public boolean hasEntities(){
	getEntities();
	return entities != null && entities.size() > 0;
    }
    public boolean isBusiness(){
	if(hasEntities()){
	    for(Entity one:entities){
		if(one.getIsBusiness())
		    return true;
	    }
	}
	return false;
    }
    public boolean isTrust(){
	if(hasEntities()){
	    for(Entity one:entities){
		if(one.getIsTrust())
		    return true;
	    }
	}
	return false;
    }
    public boolean hasNotes(){
	return !notes.equals("");
    }
    /**
     * when a new waiver is created we create with it the startTask
     * which needs a startTask and the next actiom as well
     */
    private String createStartTask(){
	//
	// find the first task to start with
	//
	logger.debug(" create start task");
	String back = "";
	Step step = null;
	StepList tl = new StepList(debug);
	Step one = tl.findStartStep();
	if(one != null){
	    step = one;
	}
	if(step != null && !id.equals("")){
	    Task task2 = new Task(debug,
				  step.getId(),
				  step.getName(),
				  step.getAlias(),
				  false,
				  step.getField_name(),
				  step.getField2_name(),
				  step.getPart_name(),
				  step.getRequire_upload(),
				  step.getSuggested_upload_type(),
																	
				  null, // task_id
				  id, // waiver_id
				  Helper.getToday(), // start_date now()
				  Helper.getToday(), // completed_date
				  added_by, // claimed_by
				  null, // field values are empty for startTask
				  null); 
	    back = task2.doSave();
	    if(back.equals("")){
		task2.createNextTasks();
	    }
	    else{
		logger.error(back);
	    }
	}
	return back;
    }
    /**
     * using concat('',waiver_num*1)=waiver_num to get numeric values only
     * and ignore appended numbers with alaphabets such as 2012A
     */
    public String findNextWaiverNum(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select "+
	    "max(waiver_num)+1 "+
	    "from waivers where concat('',waiver_num*1)=waiver_num ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		waiver_num = ""+rs.getInt(1);
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;				
    }
    //
    public String doSelect(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "select id,"+
	    "waiver_num,"+
	    "waiver_instrument_num,"+
	    "deed_book,"+
	    "deed_page,"+
						
	    "waiver_book,"+
	    "waiver_page,"+
	    "parcel_pin,"+
	    "legal_description,"+
	    "parcel_tax_id,"+
						
	    "deed_instrument_num,"+
	    "acrage,"+
	    "sec_twp_range_dir,"+
	    "development_subdivision,"+
	    "lot,"+
						
	    "notes,"+
	    "date_format(signed_date,'%m/%d/%Y'),"+
	    "date_format(scanned_date,'%m/%d/%Y'),"+						
	    "date_format(recorder_date,'%m/%d/%Y'),"+
	    "date_format(paper_verified_date,'%m/%d/%Y'),"+
						
	    "in_out_city,"+
	    "date_format(mapped_date,'%m/%d/%Y'),"+
	    "gis_notes, "+
	    "date_format(expire_date,'%m/%d/%Y'), "+
	    "status,"+
						
	    "date_format(date,'%m/%d/%Y'), "+
	    "closed_by,"+
	    "date_format(closed_date,'%m/%d/%Y'),"+
	    "added_by, "+
	    "imported from waivers where ";
	if(!id.isEmpty()){
	    qq += " id=?";
	}
	else if(!waiver_num.isEmpty()){
	    qq += " waiver_num = ?";
	}
	else{
	    back = "No waiver ID or Number is provided ";
	    addError(back);
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    if(!id.isEmpty()){
		pstmt.setString(1,id);
	    }
	    else{
		pstmt.setString(1,waiver_num);
	    }
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		setValues(rs.getString(1),
			  rs.getString(2),
			  rs.getString(3),
			  rs.getString(4),
			  rs.getString(5),
													
			  rs.getString(6),
			  rs.getString(7),
			  rs.getString(8),
			  rs.getString(9),
			  rs.getString(10),
													
			  rs.getString(11),
			  rs.getString(12),
			  rs.getString(13),
			  rs.getString(14),
			  rs.getString(15),
													
			  rs.getString(16),
			  rs.getString(17),
			  rs.getString(18),
			  rs.getString(19),
			  rs.getString(20),
													
			  rs.getString(21),
			  rs.getString(22),
			  rs.getString(23),
			  rs.getString(24),
			  rs.getString(25),
													
			  rs.getString(26),
			  rs.getString(27),
			  rs.getString(28),
			  rs.getString(29),
			  rs.getString(30) != null
			  );													
	    }
	    else{
		back ="Record "+id+" Not found";
		message = back;
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);			
	}
	return back;
    }

    public String doSave(){
		
	String back = "";
		
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null,
	    pstmt4=null, pstmt5=null;
	if(waiver_num.isEmpty()){
	    back = " Waiver number is required ";
	    addError(back);
	    return back;
	}
	ResultSet rs = null;
	if(date.equals("")){
	    date = Helper.getToday();
	}
	status = "Open";
	String qq = "insert into waivers "+
	    "(id,"+
	    "waiver_num,"+
	    "waiver_instrument_num,"+
	    "waiver_book,"+
	    "waiver_page,"+
						
	    "parcel_pin,"+
	    "legal_description,"+
	    "parcel_tax_id,"+
	    "acrage, "+
	    "sec_twp_range_dir, "+
						
	    "development_subdivision,"+
	    "lot, "+
	    "deed_book,"+
	    "deed_page, "+
	    "scanned_date,"+
						
	    "notes,"+
	    "signed_date,"+
	    "deed_instrument_num,"+
	    "recorder_date,"+
	    "paper_verified_date,"+
						
	    "in_out_city,"+
	    "mapped_date,"+
	    "gis_notes,"+
	    "expire_date,"+
	    "status,"+
						
	    "date,"+
	    "added_by) "+
						
	    "values "+
	    "(0,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+
	    "?,?,?,?,?,"+
						
	    "?,?,?,?,'Open',"+
	    "now(),?)";
				
	String qq2 = "insert into entity_waivers values(?,?)";
	String qq3 = "update addresses set waiver_id=? where id in ";
	if(!add_addr_ids.equals("")){
	    qq3 += "("+add_addr_ids+")";
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    back = setParamsForSave(pstmt);
	    pstmt.executeUpdate();
	    //
	    // get the id of the new record
	    //
	    qq = "select LAST_INSERT_ID() ";
	    if(debug){
		logger.debug(qq);
	    }
	    pstmt2 = con.prepareStatement(qq);				
	    rs = pstmt2.executeQuery();
	    if(rs.next()){
		id = rs.getString(1);
	    }
	    if(!add_entity_id.equals("")){
		qq = qq2;
		if(debug){
		    logger.debug(qq);
		}						
		pstmt3 = con.prepareStatement(qq);
		pstmt3.setString(1, add_entity_id);								
		pstmt3.setString(2, id);
		pstmt3.executeUpdate();
	    }
	    if(!add_entity_ids.equals("")){
		qq = qq2;
		pstmt4 = con.prepareStatement(qq);
		String[] e_ids = add_entity_ids.split(",");
		if(e_ids != null){
		    for(String str:e_ids){
			pstmt4.setString(1, str);								
			pstmt4.setString(2, id);
			pstmt4.executeUpdate();
		    }
		}
	    }
	    if(!add_addr_ids.equals("")){
		qq = qq3;
		if(debug){
		    logger.debug(qq);
		}						
		pstmt5 = con.prepareStatement(qq);
		pstmt5.setString(1, id);
		pstmt5.executeUpdate();
	    }
	    if(!id.equals("")){
		back = createStartTask();
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3, pstmt4, pstmt5);
	}
	return back;

    }
    //
    // needed for data import
    //
    public String doRemoveEntity(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " delete from entity_waivers where entity_id=? and waiver_id=? ";
	if(id.equals("")){
	    back = " Waiver ID not provided ";
	    return back;
	}
	if(entity_id.equals("")){
	    back = " Entity ID not provided ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, entity_id);
	    pstmt.setString(2, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;						

    }
    public String doRemoveAddress(){
	String back = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = " update addresses set waiver_id=null where waiver_id=? and id=? ";
	if(id.equals("")){
	    back = " Waiver ID not provided ";
	    return back;
	}
	if(address_id.equals("")){
	    back = " Address ID not provided ";
	    return back;
	}
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	logger.debug(qq);				
	try{
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.setString(2, address_id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(ex);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;						

    }				
    public String doAddEntity(PreparedStatement pstmt){
	String back = "";
	try{
	    if(!add_entity_id.equals("")){
		pstmt.setString(1, add_entity_id);								
		pstmt.setString(2, id);
		pstmt.executeUpdate();
	    }
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	    addError(back);
	}
	return back;
    }
    public String doSaveForImport(PreparedStatement pstmt){
		
	String back = "";
		
	ResultSet rs = null;
	if(date.equals("")){
	    date = Helper.getToday();
	}
	if(expire_date.equals("") && !recorder_date.equals("")){
	    if(Helper.checkIfDateIsGreaterThan(recorder_date, "7/1/2015"))
		expire_date = Helper.getDateFrom(recorder_date,"year", 15);
	}
	status = "Completed";
	scanned_date = "12/01/2017"; // scanned last year the default
	// this the sql in DataImport file
	/*
	  String qq = "insert into waivers "+
	  " (id,"+
	  " waiver_num,"+
	  " waiver_instrument_num,"+
	  " deed_book,'+
	  " deed_page,"+
						
	  " parcel_tax_id,"+
	  " legal_description,"+
	  " sec_twp_range_dir,"+
	  " development_subdivision,'+
	  " notes,"+
						
	  " in_out_city,"+
	  " gis_notes,"+
	  " signed_date,"+
	  " recorder_date,"+
	  " acrage,'+
						
	  " lot,"+
	  " deed_instrument_num,"+
	  " date,
	  " waiver_book,
	  " waiver_page,"+

	  " scanned_date,
	  " expire_date,
	  " imported,'+
	  " status) "+
						
	  " values(?,?,?,?,?, ?,?,?,?,? ,?,?,?,?,?, ?,?,?,?,?,"+
	  "?,?,'y','Completed')";
						

	  String qq2 = "insert into entity_waivers values(?,?)";
	*/
	int jj=1;
	try{
	    if(id.equals("")) return back;
	    pstmt.setString(jj++, id); // jj for DataImport seq
	    pstmt.setString(jj++, waiver_num);
	    if(waiver_instrument_num.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, waiver_instrument_num);
	    }
	    if(deed_book.equals(""))
		pstmt.setNull(jj++, Types.VARCHAR);								
	    else
		pstmt.setString(jj++, deed_book);
	    if(deed_page.equals(""))
		pstmt.setNull(jj++, Types.VARCHAR);								
	    else
		pstmt.setString(jj++, deed_page); // 5
						
	    if(parcel_tax_id.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, parcel_tax_id);
	    }
	    if(legal_description.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, legal_description);
	    }
	    if(sec_twp_range_dir.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, sec_twp_range_dir);
	    }
	    if(development_subdivision.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, development_subdivision);
	    }
	    if(notes.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, notes); // 10
	    }						  
	    if(in_out_city.equals("")){
		pstmt.setNull(jj++, Types.INTEGER);
	    }
	    else{
		if(in_out_city.startsWith("O"))
		    in_out_city = "OUT";
		else
		    in_out_city = "IN";
		pstmt.setString(jj++, in_out_city);
	    }
	    if(gis_notes.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++, gis_notes);
	    }
						
	    if(signed_date.equals("")){
		pstmt.setNull(jj++, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(signed_date);						
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }  // 15
						
	    if(recorder_date.equals("")){
		pstmt.setNull(jj++, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(recorder_date);						
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(acreage.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++,acreage);
	    }
	    if(lot.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++,lot); 
	    }
	    if(deed_instrument_num.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++,deed_instrument_num); 
	    }						
	    if(date.equals("")){
		pstmt.setNull(jj++, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(date);						
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(waiver_book.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++,waiver_book);
	    }
	    if(waiver_page.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++,waiver_page); 
	    }
	    if(scanned_date.equals("")){
		pstmt.setNull(jj++, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(scanned_date);						
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(expire_date.equals("")){
		pstmt.setNull(jj++, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(expire_date);						
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(parcel_pin.equals("")){
		pstmt.setNull(jj++, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(jj++,parcel_pin); 
	    }
	    if(mapped_date.equals("")){
		pstmt.setNull(jj++, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(mapped_date);						
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }						
	    pstmt.executeUpdate();
	    //
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	}
	return back;
    }		
    public String updateRelatedPart(String part_name){
	String back = "";
	if(part_name != null && !part_name.equals("")){
	    if(part_name.indexOf("legal") > -1){
		back = doLegalUpdate();
	    }
	    else if(part_name.indexOf("controller") > -1){
		back = doControllerUpdate();
	    }
	    else if(part_name.indexOf("gis") > -1){
		back = doGisUpdate();
	    }
	    else if(part_name.indexOf("recorder") > -1){
		back = doRecorderUpdate();
	    }
	    else{
		back = "Unknown part "+part_name;
	    }
	}
	return back;
    }
    private String setParamsForSave(PreparedStatement pstmt){
	String back = "";
	int jj=1;
	try{
	    if(waiver_num.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_num);
	    if(waiver_instrument_num.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_instrument_num);
	    if(waiver_book.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_book);
	    if(waiver_page.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_page);
						
	    if(parcel_pin.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,parcel_pin);						
	    if(legal_description.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,legal_description);
	    if(parcel_tax_id.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,parcel_tax_id);
	    if(acreage.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,acreage);
	    if(sec_twp_range_dir.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,sec_twp_range_dir);
						
	    if(development_subdivision.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,development_subdivision);
	    if(lot.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,lot);
	    if(deed_book.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,deed_book);
	    if(deed_page.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,deed_page);
	    if(scanned_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(scanned_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(notes.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,notes);
	    if(signed_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(signed_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }						
	    if(deed_instrument_num.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,deed_instrument_num);
	    if(recorder_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(recorder_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
						
	    if(paper_verified_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(paper_verified_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }									
	    if(in_out_city.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,in_out_city);
	    if(mapped_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(mapped_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(gis_notes.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,gis_notes);
	    if(expire_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(expire_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }						
	    if(added_by.equals(""))
		pstmt.setNull(jj++,Types.INTEGER);
	    else
		pstmt.setString(jj++,added_by);

	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	    addError(back);
	}
	return back;
    }
    // ToDo 
    public String doUpdate(){
		
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null, pstmt2=null, pstmt3=null;
	ResultSet rs = null;
	String qq = "",qq2="";
	String qq3 = "update addresses set waiver_id=? where id in ";
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set "+
								
	    "waiver_num=?,"+
	    "waiver_instrument_num=?,"+
	    "waiver_book=?,"+
	    "waiver_page=?,"+
	    "parcel_pin=?,"+
						
	    "legal_description=?,"+
	    "parcel_tax_id=?,"+
	    "acrage=?,"+
	    "sec_twp_range_dir=?,"+
	    "development_subdivision=?,"+
						
	    "lot=?,"+
	    "deed_book=?,"+
	    "deed_page=?,"+
	    "scanned_date=?,"+ 
	    "notes=?,"+ // 15
						
	    "signed_date=?,"+
	    "deed_instrument_num=?,"+
	    "recorder_date=?,"+
	    "paper_verified_date=?,"+
	    "in_out_city=?,"+
						
	    "mapped_date=?,"+
	    "gis_notes=?,"+
	    "expire_date=?,"+
	    "date=? "+
						
	    " where id=? ";
	qq2 = " insert into entity_waivers values(?,?)";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    back = setParamsForUpdate(pstmt);
	    pstmt.setString(25, id);
	    pstmt.executeUpdate();
	    if(!add_entity_id.equals("")){
		qq = qq2;
		if(debug){
		    logger.debug(qq);
		}						
		pstmt2 = con.prepareStatement(qq);
		pstmt2.setString(1, add_entity_id);								
		pstmt2.setString(2, id);
		pstmt2.executeUpdate();
	    }
	    if(!add_addr_ids.equals("")){
		qq = qq3;								
		qq += "("+add_addr_ids+")";
		if(debug){
		    logger.debug(qq);
		}						
		pstmt3 = con.prepareStatement(qq);	
		pstmt3.setString(1, id);
		pstmt3.executeUpdate();								
	    }
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, rs, pstmt, pstmt2, pstmt3);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;

    }
    private String setParamsForUpdate(PreparedStatement pstmt){
	String back = "";
	int jj=1;
	try{
	    if(waiver_num.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_num);
	    if(waiver_instrument_num.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_instrument_num);
	    if(waiver_book.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_book);
	    if(waiver_page.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,waiver_page);
						
	    if(parcel_pin.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,parcel_pin);						
	    if(legal_description.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,legal_description);
	    if(parcel_tax_id.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,parcel_tax_id);
	    if(acreage.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,acreage);
	    if(sec_twp_range_dir.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,sec_twp_range_dir);
						
	    if(development_subdivision.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,development_subdivision);
	    if(lot.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,lot);
	    if(deed_book.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,deed_book);
	    if(deed_page.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,deed_page);
	    if(scanned_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(scanned_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(notes.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,notes);
	    if(signed_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(signed_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }						
	    if(deed_instrument_num.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,deed_instrument_num);
	    if(recorder_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(recorder_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
						
	    if(paper_verified_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(paper_verified_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }									
	    if(in_out_city.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,in_out_city);
	    if(mapped_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(mapped_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(gis_notes.equals(""))
		pstmt.setNull(jj++,Types.VARCHAR);
	    else
		pstmt.setString(jj++,gis_notes);
	    if(expire_date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(expire_date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }						
	    if(date.equals(""))
		pstmt.setNull(jj++,Types.DATE);
	    else{
		java.util.Date  dateTmp = df.parse(date);
		pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));
	    }		
	}
	catch(Exception ex){
	    back += ex;
	    logger.error(back);
	    addError(back);
	}
	return back;
    }		

    public String doClose(){
		
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(closed_by.equals("")){
	    back = " user not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set "+
	    "status=?,closed_by=?,closed_date=now() where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(1, "Closed");
	    pstmt.setString(2, closed_by);
	    pstmt.setString(3, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;
    }
    public String doComplete(){
		
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set status='Completed' where id=?";
	logger.debug(qq);
	try{						
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;
    }		
    public String doLegalUpdate(){
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(signed_date.equals("")){
	    back = " signed date is required ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set "+
	    "signed_date=?,notes=? where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    java.util.Date  dateTmp = df.parse(signed_date);						
	    pstmt.setDate(1, new java.sql.Date(dateTmp.getTime()));
	    if(notes.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, notes);
	    pstmt.setString(3, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;

    }
    public String doRecorderUpdate(){
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(recorder_date.equals("")){
	    back = " waiver recorded date is required ";
	    logger.error(back);
	    addError(back);
	    return back;
	}							
				
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set "+
	    "waiver_instrument_num=?,"+
	    "recorder_date=?,waiver_book=?,waiver_page=?,notes=? "+
	    "where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    if(waiver_instrument_num.equals(""))
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(1, waiver_instrument_num);
	    //
	    java.util.Date  dateTmp = df.parse(recorder_date);
	    pstmt.setDate(2, new java.sql.Date(dateTmp.getTime()));
	    //
	    if(waiver_book.equals(""))
		pstmt.setNull(3, Types.VARCHAR);
	    else
		pstmt.setString(3, waiver_book);
	    if(waiver_page.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, waiver_page);
	    if(notes.equals(""))
		pstmt.setNull(5, Types.VARCHAR);
	    else
		pstmt.setString(5, notes);						
	    pstmt.setString(6, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;

    }
    public String doControllerUpdate(){
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	if(paper_verified_date.equals("")){
	    paper_verified_date = Helper.getToday();
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set "+
	    "paper_verified_date=?,notes=? where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);
	    java.util.Date  dateTmp = df.parse(paper_verified_date);						
	    pstmt.setDate(1, new java.sql.Date(dateTmp.getTime()));
	    if(notes.equals(""))
		pstmt.setNull(2, Types.VARCHAR);
	    else
		pstmt.setString(2, notes);
	    pstmt.setString(3, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;

    }
    public String doGisUpdate(){
	String back = "";
	if(id.equals("")){
	    back = " id not set ";
	    logger.error(back);
	    addError(back);
	    return back;
	}
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qq = "";
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "update waivers set "+
	    "in_out_city=?,mapped_date=?,gis_notes=?,notes=? "+
	    "where id=?";
	logger.debug(qq);
	try{
	    pstmt = con.prepareStatement(qq);						
	    if(in_out_city.equals("")){
		pstmt.setNull(1, Types.VARCHAR);
	    }
	    else
		pstmt.setString(1, in_out_city);
	    if(mapped_date.equals("")){
		pstmt.setNull(2, Types.DATE);
	    }
	    else{
		java.util.Date  dateTmp = df.parse(mapped_date);						
		pstmt.setDate(2, new java.sql.Date(dateTmp.getTime()));
	    }
	    if(gis_notes.equals("")){
		pstmt.setNull(3, Types.VARCHAR);
	    }
	    else{
		pstmt.setString(3, gis_notes);
	    }
	    if(notes.equals(""))
		pstmt.setNull(4, Types.VARCHAR);
	    else
		pstmt.setString(4, notes);						
	    pstmt.setString(5, id);
	    pstmt.executeUpdate();
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	if(back.equals("")){
	    back = doSelect();
	}
	return back;
    }
		
    public String doDelete(){
		
	String back = "", qq = "";
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	con = Helper.getConnection();
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	qq = "delete from waivers where id=?";
	logger.debug(qq);
	try{						
	    pstmt = con.prepareStatement(qq);
	    pstmt.setString(1, id);
	    pstmt.executeUpdate();
	    message="Deleted Successfully";
	    id="";
	}
	catch(Exception ex){
	    back += ex+":"+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }

}
