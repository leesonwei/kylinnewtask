
drop table if exists tb_kylin;

create table tb_kylin(
id Int(16),
userid varchar(64),
protocol varchar(10),
host varchar(32),
port Int(8),
username varchar(16),
password varchar(16),
is_connect boolean
);

drop table if exists tb_kylin_job;
create table tb_kylin_job(
kylin_id int(16),
uuid varchar(64),
is_resume boolean,
resume_times int(2)
);

drop table if exists QRTZ_CALENDARS;
CREATE TABLE QRTZ_CALENDARS (
SCHED_NAME VARCHAR(120),
CALENDAR_NAME VARCHAR(200),
CALENDAR BLOB);