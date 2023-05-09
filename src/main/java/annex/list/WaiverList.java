package annex.list;
/**
 * @copyright Copyright (C) 2014-2016 City of Bloomington, Indiana. All rights reserved.
 * @license http://www.gnu.org/copyleft/gpl.html GNU/GPL, see LICENSE.txt
 * @author W. Sibo <sibow@bloomington.in.gov>
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import annex.model.*;
import annex.utils.*;

public class WaiverList extends CommonInc{

    static Logger logger = LogManager.getLogger(WaiverList.class);
    static final long serialVersionUID = 320L;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    String hookup_address="", parcel_tax_id="", instrumnet_num="";
    String id="", status = "", date_from="", date_to="", name="", in_gis="",
	imported="",type="", development_subdivision="",
	legal_description="", waiver_num="",
	which_date="w.date", limit = " limit 30";
    String in_out_option = ""; // All, In, Out, noInOut
    boolean showAll = false, hasNoMappedDate=false;
    List<Waiver> waivers = null;
    String year = "";
    public WaiverList(){
    }
    public WaiverList(boolean deb){
	super(deb);
    }
    public List<Waiver> getWaivers(){
	return waivers;
    }
    public void setId(String val){
	if(val != null)
	    id = val;
    }
    public void setParcelTaxId(String val){
	if(val != null)
	    parcel_tax_id = val.trim();
    }
    public void setHookupAddress(String val){
	if(val != null)
	    hookup_address = val.trim();
    }		
    public void setStatus(String val){
	if(val != null && !val.equals("-1"))
	    status = val;
    }
    public void setName(String val){
	if(val != null)
	    name = val.trim();
    }
    public void setWaiverNum(String val){
	if(val != null)
	    waiver_num = val.trim();
    }		
    public void setLegalDescription(String val){
	if(val != null)
	    legal_description = val.trim();
    }
    public void setDevelopmentSubdivision(String val){
	if(val != null)
	    development_subdivision = val.trim();
    }		
    public void setInstrumentNum(String val){
	if(val != null)
	    instrumnet_num = val.trim();
    }		
    public void setWhichDate(String val){
	if(val != null)
	    which_date = val;
    }
    public void setDateFrom(String val){
	if(val != null)
	    date_from = val;
    }
    public void setDateTo(String val){
	if(val != null)
	    date_to = val;
    }
    public void setInGis(String val){
	if(val != null && !val.equals("-1"))
	    in_gis = val; // y, n
    }
    public void setImported(String val){
	if(val != null && !val.equals("-1"))
	    imported = val; // y, n
    }
    public void setType(String val){
	if(val != null && !val.equals("-1"))
	    type = val; // business, trust, individual
    }
    public void setYear(String val){
	if(val != null && !val.equals("-1"))
	    year = val; 
    }		
    public void setShowAll(boolean val){
	if(val){
	    showAll = true;
	    setNoLimit();
	}
    }
    public void setNoMappedDate(boolean val){
	if(val){
	    setNoLimit();
	    hasNoMappedDate = true;
	}
    }
    public void setInOutOption(String val){
	if(val != null && !val.equals("-1")){
	    in_out_option = val;
	}
    }
    public String getYear(){
	if(year.isEmpty()){
	    return "-1";
	}
	return year;
    }
    public void setNoLimit(){
	limit = "";
    }
    public String getId(){
	return id;
    }
    public String getName(){
	return name;
    }
    public String getWaiverNum(){
	return waiver_num;
    }		
    public String getInstrumentNum(){
	return instrumnet_num;
    }
    public String getHookupAddress(){
	return hookup_address;
    }
    public String getParcelTaxId(){
	return parcel_tax_id;
    }
    public String getWhichDate(){
	return which_date;
    }
    public boolean getShowAll(){
	return showAll;
    }
    public boolean getNoMappedDate(){
	return hasNoMappedDate;
    }
    public String getStatus(){
	if(status.isEmpty())
	    return "-1";
	return status;
    }
    public String getInGis(){
	if(in_gis.isEmpty())
	    return "-1";
	return in_gis;
    }
    public String getImported(){
	if(imported.isEmpty())
	    return "-1";
	return imported;
    }
    public String getType(){
	if(type.isEmpty())
	    return "-1";
	return type;
    }		
    public String getDateFrom(){
	return date_from;
    }
    public String getDateTo(){
	return date_to;
    }
    public String getDevelopmentSubdivision(){
	return development_subdivision;
    }
    public String getLegalDescription(){
	return legal_description;
    }
    public String getInOutOption(){
	if(in_out_option.isEmpty())
	    return "-1";
	return in_out_option;
    }
		
    public String find(){
		
	String back = "";
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection con = Helper.getConnection();
	String qq = "select w.id,"+
	    "w.waiver_num,"+
	    "w.waiver_instrument_num,"+
	    "w.deed_book,"+
	    "w.deed_page,"+
						
	    "w.waiver_book,"+
	    "w.waiver_page,"+
	    "w.parcel_pin,"+
	    "w.legal_description,"+
	    "w.parcel_tax_id,"+
						
	    "w.deed_instrument_num,"+
	    "w.acrage,"+
	    "w.sec_twp_range_dir,"+
	    "w.development_subdivision,"+
	    "w.lot,"+
						
	    "w.notes,"+
	    "date_format(w.signed_date,'%m/%d/%Y'),"+
	    "date_format(w.scanned_date,'%m/%d/%Y'),"+
	    "date_format(w.recorder_date,'%m/%d/%Y'),"+
	    "date_format(w.paper_verified_date,'%m/%d/%Y'),"+
						
	    "w.in_out_city,"+
	    "date_format(w.mapped_date,'%m/%d/%Y'),"+
	    "w.gis_notes, "+
	    "date_format(w.expire_date,'%m/%d/%Y'), "+
	    "w.status,"+
						
	    "date_format(w.date,'%m/%d/%Y'), "+
	    "w.closed_by,"+ 
	    "date_format(w.closed_date,'%m/%d/%Y'),"+
	    "w.added_by, "+  
	    "w.imported "+
						
	    "from waivers w ";
				
				
	if(con == null){
	    back = "Could not connect to DB";
	    addError(back);
	    return back;
	}
	String qw = "";
	boolean entityTbl = false;

	if(!id.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.id = ? ";
	}
	else if(!waiver_num.isEmpty()){
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " w.waiver_num = ? ";
	}
	else{
	    if(!status.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " w.status = ? ";
	    }
	    if(hasNoMappedDate){
		if(!qw.isEmpty()) qw += " and ";
		qw += " w.mapped_date is null ";
	    }
	    if(!imported.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		if(imported.equals("n"))
		    qw += " w.imported is null ";
		else
		    qw += " w.imported is not null ";										
	    }
	    if(!type.isEmpty()){
		entityTbl = true;
		if(!qw.isEmpty()) qw += " and ";
		if(type.equals("business"))
		    qw += " e.is_business is not null ";
		else if(type.equals("trust"))
		    qw += " e.is_trust is not null ";
		else
		    qw += " e.is_business is null and e.is_trust is null";
	    }						
	    if(!name.isEmpty()){
		entityTbl = true;
		if(!qw.isEmpty()) qw += " and ";
		qw += " e.name like ?";
	    }
	    if(!hookup_address.isEmpty()){
		qq += ", addresses a ";
		if(!qw.isEmpty()) qw += " and ";
		qw += " a.waiver_id=w.id ";
		qw += " and a.street_address like ? ";
	    }
	    if(!development_subdivision.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " w.development_subdivision like ? ";
	    }
	    if(!legal_description.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " w.legal_description like ? ";
	    }
	    if(!in_out_option.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		if(in_out_option.equals("noInOut")){
		    qw +=" w.in_out_city is null ";
		}
		else{
		    qw +=" w.in_out_city = ? ";
		}
	    }
	    if(!parcel_tax_id.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (w.parcel_tax_id like ?  or w.parcel_pin like ? )";
	    }
	    if(!instrumnet_num.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += " (w.deed_instrument_num = ? or w.waiver_instrument_num=?)";
	    }
	    if(!year.isEmpty()){
		if(!qw.isEmpty()) qw += " and ";
		qw += "year("+which_date +")= ? ";
	    }
	    else{
		if(!date_from.isEmpty()){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += which_date +" >= ? ";
		}
		if(!date_to.isEmpty()){
		    if(!qw.isEmpty()) qw += " and ";
		    qw += which_date +" <= ? ";
		}
	    }
	}
	if(entityTbl){
	    qq += ", entities e, entity_waivers ew ";
	    if(!qw.isEmpty()) qw += " and ";
	    qw += " e.id = ew.entity_id and w.id=ew.waiver_id ";
	}
	if(!qw.isEmpty()){
	    qq += " where "+qw;
	}
	// System.err.println(qq);
						
	qq += " order by w.id desc "+limit;
	logger.debug(qq);
	try{						
	    pstmt = con.prepareStatement(qq);
	    int jj=1;
	    if(!id.isEmpty()){
		pstmt.setString(jj++,id);
	    }
	    else if(!waiver_num.isEmpty()){
		pstmt.setString(jj++,waiver_num);
	    }
	    else {
		if(!status.isEmpty()){
		    pstmt.setString(jj++,status);
		}
		if(!name.isEmpty()){
		    pstmt.setString(jj++,"%"+name+"%");
		}
		if(!hookup_address.isEmpty()){
		    pstmt.setString(jj++, "%"+hookup_address+"%");
		}
		if(!development_subdivision.isEmpty()){
		    pstmt.setString(jj++, "%"+development_subdivision+"%");
		}
		if(!legal_description.isEmpty()){
		    pstmt.setString(jj++, "%"+legal_description+"%");
		}
		if(!in_out_option.isEmpty()){
		    if(!in_out_option.equals("noInOut")){
			pstmt.setString(jj++, in_out_option);
		    }
		}
		if(!parcel_tax_id.isEmpty()){
		    pstmt.setString(jj++, "%"+parcel_tax_id+"%");
		    pstmt.setString(jj++, "%"+parcel_tax_id+"%");								
		}
		if(!instrumnet_num.isEmpty()){
		    pstmt.setString(jj++, instrumnet_num);
		    pstmt.setString(jj++, instrumnet_num);								
		}
		if(!year.isEmpty()){
		    pstmt.setString(jj++, year);
		}
		else{
		    if(!date_from.isEmpty()){
			java.util.Date dateTmp = df.parse(date_from);
			pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));	
		    }
		    if(!date_to.isEmpty()){
			java.util.Date dateTmp = df.parse(date_to);
			pstmt.setDate(jj++, new java.sql.Date(dateTmp.getTime()));	
		    }
		}
	    }
	    rs = pstmt.executeQuery();
	    if(waivers == null)
		waivers = new ArrayList<Waiver>();
	    while(rs.next()){
		Waiver one =
		    new Waiver(debug,
			       rs.getString(1),
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
								
		if(!waivers.contains(one))
		    waivers.add(one);
	    }
	}
	catch(Exception ex){
	    back += ex+" : "+qq;
	    logger.error(back);
	    addError(back);
	}
	finally{
	    Helper.databaseDisconnect(con, pstmt, rs);
	}
	return back;
    }
}






















































