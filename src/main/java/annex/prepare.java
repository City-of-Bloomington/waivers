import java.util.*;
import java.sql.*;
import java.io.*;
import java.io.File;
import java.nio.file.*;
import java.text.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.Reader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
public class prepare{

				final static String[] types = {"LLC","INC.","CORP","LLP","CO.","LTD.","TRUST","ENTERPRISE","CONSTRUCTION"};

    //
    // New database connect string (Oracle 8.2)
    //   
    Connection con, con2;
    Statement stmt, stmt2;
    final int baseNumber = 1; 
    ResultSet rs, rs2;
    boolean flag = true, debug = true;
    int  errorInsert = 0;
    int errorCount = 0;
    int recordCount = 0;
    int defCount = 0;
    String message = "", close_case="", case_types="";
		String dbStr = "";
		String seasonArr[] = {"","Summer","Fall/Winter","Winter/Spring","Ongoing"};
    public prepare(){
				try{
						dbStr = "jdbc:mysql://outlaw.bloomington.in.gov:3306/waivers?user=waivers&password="+java.net.URLEncoder.encode("h++pd", "UTF-8");

						// databaseConnect();
						// testQuartz();
						// copySponsorsAndContacts();
						// databaseDisconnect();
						// testSqlite();
						// testIndexOf();
						// testFiles();
						// testCalendarChange();
						// findMimeType();
						// doImport();
						checkTypes();
						// doEndocdeDecode();
				}
				catch(Exception ex){
						System.err.println(ex);
				}
    }
		public void checkTypes(){
				String strs[] = {"Delta Inc.", "Delata Llc","Data interprise co."};
				for(String val:strs){
						String val2 = val.toUpperCase();
						boolean found = false;
						for(String str:types){
								if(val2.indexOf(str) > -1){
										System.err.println("Bus "+val);
										found = true;
								}
						}
						if(!found)
								System.err.println("Not "+val);
				}
		}
		/**
			 cleanup for next import
			 
			 delete from attachments;
			 delete from reviews;
			 delete from addresses;
			 delete from tasks;
			 delete from owner_waivers;delete from owners;delete from waivers;
			 
			 ALTER TABLE owners AUTO_INCREMENT = 1;
			 ALTER TABLE reviews AUTO_INCREMENT = 1;
			 ALTER TABLE addresses AUTO_INCREMENT = 1;
			 
			 // after import do the following
			 insert into addresses (id,street_address,waiver_id) select 0,hookup_address,id from waivers where hookup_address is not null;				 
			 insert into reviews (id,waiver_id) select 0,id from waivers where date > '2014-01-01';
			 //
			 we need to set expire date for approved waivers after 2017 ? need check			 
			 
		 */
		public void doImport(){
				//
				/* // this is the excel header
					 WAIVER_ID, 1
					 Misc BOOK, 2
					 Misc PAGE, 3
					 OWNER 1 at Signing, 4
					 OWNER 2 at Signing, 5
					 IN/OUT,             6
					 Date Signed,        7
					 Date Recorded,      8
					 Tax ID #,           9
					 Acreage,           10
					 Acreage2,          11
					 sec-twp-range-dir, 12
					 Development,       13
					 Lot,               14
					 ADDRESS OF HOOK-UP, 15
					 Deed BOOK (Instrument #), 16
					 Deed PAGE,                17
					 Recorder ID & Notes,      18
					 Paper Copy of Waiver Verified, 19
					 Notes,                         20
					 INGIS,                         21
					 GIS Mapping Notes              22
				*/
				String back = "";
				String fileName = "c:\\webapps\\waivers\\docs\\Waiver_GIS20160816.csv";
				Reader in = null;
				try{
						in = new FileReader(fileName);
						//
						// if you want to define the header 
						// Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("ID", "Last Name", "First Name").parse(in);
						//
						// header auto detection
						// Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
						Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
						int jj=1;
						for (CSVRecord record : records) {
								String id = record.get(0);// ("WAIVER_ID"); // 1
								String owner1 = record.get(3);// ("OWNER 1 at Signing"); // 4
								String owner2 = record.get(4);// ("OWNER 2 at Signing"); // 5
								String in_out = record.get(5);// ("IN/OUT");    // 6
								String date_signed = record.get(6);// ("Date Signed"); // 7
								String date_recorded = record.get(7);// ("Date Recorded"); // 8
								String tax_id = record.get(8);// ("Tax ID #");						 // 9
								String hookup_addr = record.get(14);// ("ADDRESS OF HOOK-UP"); // 15
								String instrumnet_num = record.get(15);// ("Deed BOOK (Instrument #)"); // 16
								String recorder_id = record.get(17);// ("Recorder ID & Notes"); //18
								String notes = record.get(19);// ("Notes"); // 20										
								String ingis = record.get(20);// ("INGIS"); // 21
								String gis_notes = record.get(21);// ("GIS Mapping Notes"); // 22
								//
								String recorder_notes = "";
								
								// System.err.println(" id: "+id);
								if(id.equals("")) continue;
								System.err.println(" owner1: "+owner1);
								// System.err.println(" owner2: "+owner2);
								// System.err.print(" date signed: "+date_signed);
								// System.err.println(" date record: "+date_recorded);
								// System.err.println(" tax_id: "+tax_id);								
								// System.err.println(" addr: "+hookup_addr);
								// System.err.println(" instrum #: "+instrumnet_num);

								
								if(recorder_id.length() > 10){
										recorder_notes = recorder_id;
										recorder_id = "";
								}
								// System.err.println(id+" reocrder id notes:"+recorder_id_notes);
								// System.err.print(" notes: "+notes);
								// System.err.print(id+" ingis: "+ingis);
								// System.err.println(" gis notes: "+gis_notes);
								if(id.equals("2386")) break; // last record in this file
								// if(jj > 50) break;
						}
						if(in != null){
								in.close();
						}
				}
				catch(Exception ex){
						System.err.println(ex);
						back += ex;
				}
		}
		/*
		void findMimeType(){
				Map<String, String> map = new HashMap<>();
				map.put("image/gif","gif");
				map.put("image/jpeg","jpg");
				String fileName = "c:/webapps/ROOT/images/tomcat_gif.tmp";
				File file = new File(fileName);
				Tika tika = new Tika();
				try{
						String ext = "";
						String mimeType = tika.detect(file);
						if(map.containsKey(mimeType)){
								ext = map.get(mimeType);
						}
						System.err.println(" type "+mimeType+" "+ext);
				}
				catch(Exception ex){
						System.err.println(ex);
				}

		}
		*/
		void testCalendarChange(){
				String date="01/01/2000", new_date="", type="year";
				int cnt = 15;
				
				String[] arr = date.split("/");
				System.err.println(" arr "+arr.length);
				if(arr != null && arr.length == 3){
						int[] mmddyy = {0,0,0}; // mm dd yyyy
						try{
								int jj=0;
								for(String str:arr){
										mmddyy[jj++] = Integer.parseInt(str);
								}
								Calendar cal = Calendar.getInstance();
								cal.set(Calendar.MONTH, mmddyy[0]-1);
								cal.set(Calendar.DATE, mmddyy[1]);
								cal.set(Calendar.YEAR, mmddyy[2]);
								if(type.equals("year"))
										cal.add(Calendar.YEAR, cnt);
								else if(type.equals("month"))
										cal.add(Calendar.MONTH, cnt);
								else
										cal.add(Calendar.DATE, cnt);
								new_date = cal.get(Calendar.MONTH+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
								System.err.println(" new date "+new_date);
								
						}catch(Exception ex){
								System.err.println(ex);
						}
				}

		}
		void doEndocdeDecode(){
				String str = "Invoice Sept 12 2016.pdf";
				try{
						String str2 = URLEncoder.encode(str, "UTF-8");
						String str3 = URLDecoder.decode(str2, "UTF-8");						
						System.err.println(str + " ==> "+str2+" "+str3);
				}
				catch(Exception ex){
						System.err.println(ex);
				}
		}
		String getFileExtension(File file) {
				String name = file.getName();
				try {
						if(name.indexOf(".") > -1){
								return name.substring(name.lastIndexOf(".") + 1);
						}
				} catch (Exception e) {
						System.err.println(e);
				}
				return "";
		}		
		void testFiles(){
				// application/pdf
				String str = "C:/webapps/assettrack/WEB-INF/files/cbu_data.pdf";
				// image/jpeg
				String str2 = "C:/webapps/assettrack/WEB-INF/files/help.jpg";
				// application/vnd.ms-excel
				String str3 = "C:/webapps/assettrack/WEB-INF/files/rental.csv";
				// text/xml
				String str34 = "C:/webapps/assettrack/WEB-INF/files/rental.xml";
				//
				// video/mpeg
				String str35 = "C:/webapps/assettrack/WEB-INF/files/rental.mpg";
				// video/mpeg
				String str36 = "C:/webapps/assettrack/WEB-INF/files/rental.mp4";
				// video/avi
				String str37 = "C:/webapps/assettrack/WEB-INF/files/rental.avi";
				// video/quicktime
				String str38 = "C:/webapps/assettrack/WEB-INF/files/rental.qt";
				// video/quicktime
				String str39 = "C:/webapps/assettrack/WEB-INF/files/rental.wmv";
				
				
				// text/html
				String str4 = "C:/webapps/assettrack/WEB-INF/files/rental.html";				
				
				// image/gif
				String str5 = "C:/webapps/assettrack/WEB-INF/files/tomcat.gif";
				//docx application/vnd.openxmlformats-officedocument.wordprocessingml.document
				//doc application/msword
				String str6 = "C:/webapps/assettrack/WEB-INF/files/supervisor_approve.doc";
				//xlsx application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
				String str7 = "C:/webapps/assettrack/WEB-INF/files/workarea.xlsx";
				
				
				try{
						File file = new File(str39);
						String name = file.getName();
						System.err.println(" name "+name);
						String file_ext = getFileExtension(file);
						// System.err.println(" ext "+file_ext);
						String pp = file.getAbsolutePath();
						Path path = Paths.get(pp);
						String fileType = Files.probeContentType(path);
						System.err.println(" ext "+file_ext+" "+fileType);
				}catch(Exception ex){
						System.err.println(ex);
				}
		}
		/**
			 
				create table users(
					id       tinyint unsigned not null auto_increment,
					username varchar(10) not null unique,
					full_name     varchar(128),
					dept     varchar(128),
					role     enum('user','admin'),
					inactive char(1),
					primary key(id)
			 )engine=InnoDB;

			 create table groups(
			   id  tinyint unsigned not null auto_increment,
			   name    varchar(128),
			   inactive char(1),
			   primary key(id)
			 )engine=InnoDB;

				insert into groups (id,name) values(1,'Utilities'),(2,'Legal'),(3,'Controller'),(4,'ITS');

				create table user_groups(
				user_id  tinyint unsigned,
				group_id tinyint unsigned,
				primary key(user_id,group_id),
				foreign key(user_id) references users(id),
				foreign key(group_id) references groups(id)
				)engine=InnoDB;

				create table steps(                                                             	id  tinyint unsigned not null auto_increment,                                  	name varchar(128),                                                            	field_name varchar(80),                                                        	field2_name varchar(80),                                                       	part_name varchar(80),				                                               	  require_upload  char(1),                                                        inactive char(1),                                                              	primary key(id)                                                                )engine=InnoDB;
;;
;; 
				insert into steps(id,name) values(10,'Start'), ;;  utility
				(20,'Request Warranty Deed'),   ;; Utility
				(30,'Send Warrany Deed to Legal'), ;; Legal
				(40,'Prepare a Waiver),    ;; legal
				(50,'Notify customer for appointment to sign the waiver'), ;; legal
				(60,'Signing the waiver by customer'),                     ;; legal
				(70,'Email Uitilities that the waiver is ready to be recorded'), ;; Legal
				(80,'Print email, file and inform service connection to proceed'), ;; Utilities
				(90,'Record waiver with Courthouse Recorder's Office'), ;; legal
				(100,'Send recorded waiver hardcopy to Controller\'s Office'),;; legal
				(110,'File hardcopy'),// controller
				(120,'Inform GIS about the waiver'),        ;; Controller
				(130,'Add property to Waivers of Annexation GIS Layer'),;; ITS GIS
				(140,'Completed');; //

				;; // All
				update steps set field2_name='Date';
				update steps set field3_name='Notes';
				;; Legal
				update steps set field2_name='Signed Date' where id=60;
				;; legal recording
				update steps set field2_name='Recorded Date' where id=90;
				update steps set field3_name='Recorder ID Notes' where id=90;
				;; // Controller
				update steps set field3_name='Controller Notes' where id=110;
				update steps set field2_name='Paper Verified Date' where id=110;
				;; // GIS
				update steps set field2_name='Date Mapped' where id=130;								
				update steps set field3_name='GIS Notes' where id=130;
				;;
				create table work_flows(                                                         id tinyint unsigned not null auto_increment,                                    step_id tinyint unsigned not null,                                              next_step_id tinyint unsigned not null,                                         primary key(id),                                                                foreign key(step_id) references steps(id),                                      foreign key(next_step_id) references steps(id)                                  )engine=InnoDB;
				
				insert into work_flows
				values(0,10,20), // start
				(0,20,30),
				(0,30,40),
				(0,40,50),
				(0,50,60),
				(0,60,70),
				(0,70,80), ;; cuncurrent with 90
				(0,70,90),
				(0,90,100),
				(0,100,110),
				(0,110,120),
				(0,120,130),
				(0,130,140), ;; GIS completed
				(0,80,140);  ;; Utility completed
				
				
				create table group_steps(                                                         id tinyint unsigned not null auto_increment,                                    step_id  tinyint unsigned,                                                      group_id tinyint unsigned,                                                      primary key(id),                                                                foreign key(group_id) references groups(id),                                    foreign key(step_id) references steps(id),                                      unique(step_id,group_id)                                                      )engine=InnoDB;
			 
			 insert into group_steps values(0,10,1),(0,20,1); ;; utilities
			 insert into group_steps values(0,30,2),(0,40,2),(0,50,2),(0,60,2),(0,70,2);  ;; legal
			 insert into group_steps values(0,80,1);    ;; utilities
			 insert into group_steps values(0,90,2),(0,100,2);  ;; legal
			 insert into group_steps values(0,110,3),(0,120,3);  ;; controller
			 insert into group_steps values(0,130,4),(0,140,4); ;; ITS GIS

;;			 
;; expire_date is 15 years from date, but for waivers approved after 6/2015
;;
      create table waivers(                                                              id int unsigned not null auto_increment,                                        instrumnet_num        varchar(256),                                               legal_description     varchar(256),                                             parcel_tax_id         varchar(512),                                              hookup_address        varchar(1024),				                                     notes                 varchar(512),                                             signed_date           date,                                                     recorder_id           varchar(20),                                              recorder_date         date,                                                     recorder_notes         varchar(512),                                            paper_verified_date    date,                                                    controller_notes       varchar(512),                                            in_out_city           enum('IN','OUT'),                                         in_gis                char(1),                                                  mapped_date           date,                                                     gis_notes             varchar(512),                                             imported              char(1),                                                  added_by              tinyint unsigned,                                         invalid_addr          char(1),                                                  status                enum('Open','Closed','Completed'),                        date                  date,                                                     expire_date           date,                                                     closed_by             tinyint unsigned,                                         closed_date           date,                                                     isBusiness            char(1),                                                  primary key(id),                                                                foreign key(added_by) references users(id),                                     foreign key(closed_by) references users(id)                                      )engine=InnoDB;

				 // 
				 // Note: in action class and actions table
				 // we are using action_id instead of id because in the
				 // code we are extending action class from task class and
				 // we need task id to stay id so that we benefit from task
				 // class functions such as finding next task, group, etc,
				 //
				 // replaced by works table because of naming conflict
				 //
				 create table tasks(                                                             task_id             int unsigned not null auto_increment,                       waiver_id      int unsigned,				                                             step_id        tinyint unsigned,                                                start_date     date,                                                            completed_date date,                                                            claimed_by     tinyint unsigned,                                                field_value    date,                                                            field2_value   varchar(512),                                                     primary key(task_id),                                                           foreign key(step_id) references steps(id),                                      foreign key(waiver_id) references waivers(id),                                  foreign key(claimed_by) references users(id)                                  )engine=InnoDB;

				create table attachment_seq(                                                      id int unsigned not null auto_increment,                                        primary key(id)                                                               )engine=InnoDB;
				
				create table attachments(                                                         id int unsigned not null,                                                       obj_id           int unsigned,                                                  obj_type         enum('Waiver','Task'),                                         file_name           varchar(128),                                               old_file_name       varchar(255),                                               hardcopy_location   varchar(255),                                               date                date,                                                       notes               varchar(510),                                               user_id             tinyint unsigned,                                           primary key(id),                                                                foreign key(id) references attachment_seq(id),                                  foreign key(user_id) references users(id)                                      )engine=InnoDB;
								
				//

				create table reviews(
				id int unsigned not null auto_increment primary key,
				waiver_id int unsigned not null unique,
				date date,
				verified char(1),
				notes varchar(256),
				reviewed_by tinyint(3) unsigned,
				foreign key(waiver_id) references waivers(id),
				foreign key(reviewed_by) references users(id)
				)engine=InnoDB;

				create table addresses(                                                         id int unsigned not null auto_increment primary key,                            street_address varchar(160),                                                     waiver_id int unsigned,                                                         ma_address_id int,                                                              ma_unit_id int,                                                                 invalid char(1),                                                                foreign key(waiver_id) references waivers(id)                                   )engine=InnoDB;

			 insert into addresses (id,street_address,waiver_id) select 0,hookup_address,id from waivers where hookup_address is not null;

			 				create table owners(                                                         id int unsigned not null auto_increment primary key,                            first_name varchar(80),                                                         last_name varchar(80),                                                          business_name varchar(120),                                                     title  varchar(30)                                                             )engine=InnoDB;

					 create table owner_waivers(                                                        owner_id int unsigned,                                                          waiver_id int unsigned,                                                         foreign key(waiver_id) references waivers(id),                                  foreign key(owenr_id) references owners(id)                                   )engine=InnoDB;

					 //
					 // after import do the following
					 //
					 insert into addresses (id,street_address,waiver_id) select 0,hookup_address,id from waivers where hookup_address is not null;				 
					 insert into reviews (id,waiver_id) select 0,id from waivers where date > '2014-01-01';
					 //
					 // we need to set expire date for approved waivers after 2017 ? need check
					 
					 
					 
		 */
		void testIndexOf(){
				String str = "sibow@bloomington.in.gov";
				try{
						String str2 = str.substring(0,str.indexOf("@"));
						System.err.println(str2);
				}catch(Exception ex){
						System.err.println(ex);
				}
		}
		void doCleanQuartz(){
				
				/*
					
					// apps
							 delete from QRTZ_CRON_TRIGGERS;delete from QRTZ_SIMPLE_TRIGGERS;delete from QRTZ_FIRED_TRIGGERS;delete from QRTZ_TRIGGERS;delete from QRTZ_JOB_DETAILS;
					
					// outlaw
							 delete from qrtz_cron_triggers;delete from qrtz_simple_triggers;delete from qrtz_fired_triggers;delete from qrtz_triggers;delete from qrtz_job_details;
				*/
		}
	
		void testDate(){
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
				SimpleDateFormat ft = new SimpleDateFormat ("MM/dd/yyyy");
		
				String str="Jul 4, 2011";
				String str2="7/11/11";
				java.util.Date date = null;
				try{
						date = df.parse(str);
						System.err.println(df.format(date));
						date = ft.parse(str2);
						System.err.println(ft.format(date));			
				}
				catch(Exception ex){
						System.err.println(ex);
				}
		
		}
		void testSub(){
				String str = "51-57";
				String str2 = str.substring(0,str.indexOf("-"));
				System.err.println(str2);
		}
		//
    String initCap(String inStr){
	
				if(inStr == null) return inStr;
				inStr = inStr.toLowerCase();
				String strArr[] = inStr.split("\\s");
				StringBuilder sb = new StringBuilder();
				for(int i=0;i<strArr.length;i++){
						if(i > 0) sb.append(" ");
						String str = strArr[i].substring(0,1).toUpperCase();
						if(strArr[i].length() > 1)
								str += strArr[i].substring(1);
						sb.append(str);
				}
				return sb.toString();
    }
		//
    final static String escapeIt(String s) {

				StringBuffer safe = new StringBuffer(s);
				int len = s.length();
				int c = 0;
				while (c < len) {                           
						if (safe.charAt(c) == '\'' ||
								safe.charAt(c) == '"' 
								){
								safe.insert(c, '\\');
								c += 2;
								len = safe.length();
						}
						else {
								c++;
						}
				}
				return safe.toString();
    }
    //

    void testExp(){
				String str = "1233", str2="12.12",str3="12ab";
				if(str.matches("[0-9]")) System.err.println("match 1");
				if(str.matches("\\d")) System.err.println("match 2");
				if(str.matches("\\d+")) System.err.println("match 3");
				if(str.matches("[0-9]+")) System.err.println("match 4");

				if(str.matches("\\p{Digit}")) System.err.println("match 5");
				if(str2.matches("\\p{Digit}")) System.err.println("match 6");
				if(str3.matches("\\p{Digit}")) System.err.println("match 7");
				if(str.matches("\\p{Digit}+")) System.err.println("match 8");
				if(str2.matches("\\p{Digit}+")) System.err.println("match 9");
				if(str3.matches("\\p{Digit}+")) System.err.println("match 10");

    }

    // 
    void getTableInfo(){

				try{
						rs = stmt.executeQuery("select * from mytablename");
						System.err.println("Current fields");
						if(rs.next()){
								int clcnt = rs.getMetaData().getColumnCount();
								System.err.println(clcnt);
								for(int i=0; i<clcnt; i++){
										String str = rs.getMetaData().getColumnName(i+1);
										//   String str2 = rs.getMetaData().getColumnType(i+1);
										System.err.println(" "+i+" "+str);
								}
						}
	    
				}catch(Exception ex){
						System.err.println(ex);
				}
    }

    public void databaseConnect(){
				//
				try {
						Class.forName("com.mysql.jdbc.Driver").newInstance();
						//
						// mysql
						con = DriverManager.getConnection(dbStr);
						if(con == null){
								System.err.println("Could not connect");
						}
						else{
								stmt = con.createStatement();
								stmt2 = con.createStatement();
						}
				}
				catch (Exception sqle){
						System.err.println(sqle);

				}
    }

    public void databaseDisconnect(){

				try {
						if(con != null)
								con.close();
						if(con2 != null)
								con2.close();
				}
				catch (Exception e) {
						System.err.println(e);
				}
    }
    public static void main(String[] args){
	
				new prepare();

    }

}





































