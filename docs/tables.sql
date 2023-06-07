

;; Data desing for AnaxWaiver Project
;; ====================================

-- ;;			06/05/2023 
-- ;;			tax table added to merge old waivers data from socrata
			create table tax(
				annex_area 			varchar(50),
				waiver_num 			varchar(20),
				parcel_pin			varchar(1024),	
				owner 				varchar(255),
				owner_address 		varchar(255),
				owner_city 			varchar(255),
				property_address 	varchar(1024),
				property_city 		varchar(255),
				gross_tax_change	varchar(255),
				net_tax_change		varchar(255),
				)engine=InnoDB;

			create table users(
				id       tinyint unsigned not null auto_increment,
				username     varchar(20),
				full_name varchar(128),
				dept     enum('ITS','Legal','Utilities'),
				role     enum('View','Edit','Edit:Delete','Edit:Delete:Admin'),
				activeMail char(1),
				inactive char(1),
				primary key(id)
			 )engine=InnoDB;

			 create table groups(
			 id  tinyint unsigned not null auto_increment,
			 name    varchar(128),
			 inactive char(1),
			 primary key(id)
			 )engine=InnoDB;

				insert into groups (id,name) values(1,'Utilities'),
				(2,'Legal'),
				(3,'GIS');

				create table user_groups(
				user_id  tinyint unsigned,
				group_id tinyint unsigned,
				primary key(user_id,group_id),
				foreign key(user_id) references users(id),
				foreign key(group_id) references groups(id)
				)engine=InnoDB;

				create table steps(
				id  tinyint unsigned not null auto_increment,
				name varchar(128),
				alias varchar(20),
				field_name varchar(80),
				field2_name varchar(80),
				part_name varchar(80),				
				require_upload char(1),
				suggested_upload_type enum('Application','Deed','Recorded Waiver','Map'),
				inactive char(1),
				primary key(id)
				)engine=InnoDB;
;;
;; 
				insert into steps(id,name,alias,require_upload,suggested_upload_type) values(10,'Start','start',null,null), ;;  utility
				(20,'Application for Service','Application','y','Application'), ;; Utility
				(30,'Obtain Deed','Deed','y','Deed'),   ;; Utility
				(40,'Prepare a Waiver','Prepare Waiver',null,null),    ;; legal
				(50,'Notify customer for appointment to sign the waiver','Notify Customer',null,null), ;; legal
				(60,'Signing the waiver by customer','Customer Signs',null,null),                     ;; legal
				(90,'Record waiver with Courthouse Recorders Office','Record Waiver','y','Recorded Waiver')), ;; legal
				(110,'Add Waivers Parcels to GIS','Add to GIS',null,null);; ITS GIS
			

				;; // All
				update steps set field_name='Date';
				update steps set field2_name='Notes';
				update steps set require_upload = 'y' where id in (20,30,90)
			
				;;
				create table tasks(                                                             task_id int unsigned auto_increment primary key,                                waiver_id int unsigned,                                                         step_id tinyint unsigned,                                                       start_date date,                                                                completed_date date,                                                            claimed_by tinyint unsigned,                                                    field_value date,                                                               field2_value varchar(512),                                                      foreign key(waiver_id) references waivers(id),                                  foreign key(step_id) references steps(id),                                      foreign key(claimed_by) references users(id)                                    )engine=InnoDB;
				;;
				create table work_flows(
				id nt unsigned auto_increment primary key,
				step_id tinyint unsigned,
				next_step_id tinyint unsigned,
				foreign key(step_id) references steps(id),
			  foreign key(next_step_id) references steps(id)
				)engine=InnoDB;
				
				insert into work_flows
				values(0,10,20), // start
				(0,20,30),
				(0,30,40),
				(0,40,50),
				(0,50,60),
				(0,60,70),
				(0,70,90),
				(0,90,110);; completed

				
				
				create table group_steps(
				id tinyint unsigned not null auto_increment,
				step_id  tinyint unsigned,
				group_id tinyint unsigned,
				primary key(id),
				foreign key(group_id) references groups(id),
				foreign key(step_id) references steps(id),
				unique(step_id,group_id))engine=InnoDB;
			 
			 insert into group_steps values(0,10,1),(0,20,1),(0,30,1); ;; utilities
			 insert into group_steps values(0,40,2),(0,50,2),(0,60,2);  ;; legal
			 insert into group_steps values(0,70,2),(0,80,2),(0.90,2);    ;; legal
			 insert into group_steps values (0,100,2);  ;; legal
			 insert into group_steps values (0,110,3); ;; ITS GIS
			 
;;
;; 
      create table waivers(                                                            id int unsigned not null auto_increment,                                        waiver_num     varchar(20),                                                     waiver_instrument_num        varchar(80),                                       waiver_book             varchar(20),                                            waiver_page             varchar(20),                                            parcel_pin           varchar(256),                                              legal_description     varchar(512),                                             parcel_tax_id         varchar(512),                                             acrage                varchar(128),                                             sec_twp_range_dir    varchar(128),                                              development_subdivision  varchar(512),                                           lot                   varchar(512),                                             deed_book             varchar(256),                                             deed_page             varchar(256),                                             scanned_date          date,                                                     notes                 varchar(512),                                             signed_date           date,                                                     deed_instrument_num    varchar(128),                                            recorder_date         date,                                                     paper_verified_date   date,                                                     in_out_city           enum('IN','OUT'),                                         mapped_date           date,                                                     gis_notes             varchar(512),                                             expire_date           date,				                                              status                enum('Open','Closed','Completed'),                        date                  date,                                                     closed_by             tinyint unsigned,                                         closed_date           date,                                                     imported              char(1),									gross_tax_change      varchar(255), 									net_tax_change		varchar(255),                                                  added_by              tinyint unsigned,                                         primary key(id),                                                                foreign key(added_by) references users(id),                                     foreign key(closed_by) references users(id)						                      
	  )engine=InnoDB;

			create table entities(                                                             id int unsigned not null auto_increment primary key,                             name varchar(80),                                                               title varchar(80),                                                              is_business char(1),                                                            is_trust char(1)                                                              )engine=InnoDB;

				 
		 create table entity_waivers(                                                      entity_id int unsigned,                                                         waiver_id int unsigned,                                                         foreign key(entity_id) references entities(id),                                 foreign key(waiver_id) references waivers(id)                                  )engine=InnoDB;

		 

	 ;; some of waivers have multiple addresses
	 create table addresses(                                                          id int unsigned not null auto_increment primary key,                             street_address varchar(1024) not null,                                          waiver_id int unsigned,                                                         street_num varchar(50),                                                         street_name varchar(50),                                                        invalid char(1),                                                                foreign key(waiver_id) references waivers(id)                                  )engine=InnoDB;
				 				 
				 ;;
				 ;; in import we need to create the task to save the following date
				 ;;
				 reviewed              char(1),   
				 reviewed_date         date,
				 gis_notes             varchar(258),
				 //
				 // check tasks table

				create table attachments(                                                         id int unsigned not null auto_increment,                                        waiver_id           int unsigned,                                               task_id             int unsigned,                                               file_name           varchar(128),                                               old_file_name       varchar(255),                                               hardcopy_location   varchar(255),                                               type                enum('Application','Deed','Recorded Waiver','Map','Other'),                                                                                 date                date,                                                       notes               varchar(510),                                               user_id             tinyint unsigned,                                           primary key(id),                                                                foreign key(user_id) references users(id),                                      foreign key(waiver_id) references waivers(id)                                  )engine=InnoDB;
				
					create table email_logs(                                                        id int unsigned auto_increment primary key,                                     waiver_id        int unsigned,                                                  task_id          int unsigned,                                                  date             date,                                                          to_user          varchar(80),                                                   from_user        varchar(80),                                                   cc_users         varchar(1024),                                                 subject          varchar(80),                                                   msg              varchar(512),                                                  email_errors     varchar(1024),                                                 foreign key(waiver_id) references waivers(id),                                  foreign key(task_id) references tasks(task_id)                                 )engine=InnoDB;

;;
;; 02/19/2019
;; combine step 2 and 3 into one
;; update steps set name='Obtain Application and Deed' where id=20;
;; update steps set suggested_upload_type='Application' where id=20;
;; update steps set alias='Application and Deed' where id=20;
;; delete from group_steps where id=2;
;; delete from tasks where step_id=15;
;; update work_flows set next_step_id=20 where id=10;
;; delete from work_flows where id=20;
;; delete from steps where id=15;

;;
 create table group_notifications(                                                 id int unsigned auto_increment primary key,                                     group_id        tinyint unsigned,                                               completed_step_id    tinyint unsigned,                                          inactive       char(1),                                                         foreign key(group_id) references groups(id),                                    foreign key(completed_step_id) references steps(id)                            )engine=InnoDB;

 insert into group_notifications values(0,2,20,null),(0,3,60,null),(0,1,60,null);
alter table users add activeMail char(1) after role;
update users set activeMail='y';
update users set activeMail=null where id in (2,6,8,12);

;;
;; 3/4/2019
alter table email_logs add cc_users varchar(1024) after from_user;
	
