set global max_allowed_packet = 2*1024*1024*10;

CREATE DATABASE IF NOT EXISTS testdb;
CREATE TABLE IF NOT EXISTS testdb.student_details (
 roll_no int(10) NOT NULL,
 registration_no int(10) NOT NULL AUTO_INCREMENT,
 first_name varchar(50) NOT NULL DEFAULT '',
 last_name varchar(50) DEFAULT '',
 class varchar(10) NOT NULL DEFAULT '',
 marks int(3) NOT NULL DEFAULT '0',
 gender varchar(6) NOT NULL DEFAULT 'male',
 PRIMARY KEY(registration_no)
) ENGINE=InnoDB;

INSERT INTO testdb.`student_details` (`roll_no`,`registration_no`,`first_name`,`last_name`,`class`,`marks`,`gender`) VALUES (1,100,'aaa1','s','1',1,'1');
INSERT INTO testdb.`student_details` (`roll_no`,`registration_no`,`first_name`,`last_name`,`class`,`marks`,`gender`) VALUES (2,101,'bbb2','x','1',2,'1');

commit;
