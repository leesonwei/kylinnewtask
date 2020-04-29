
drop table if exists tb_kylin;

create table tb_kylin(
id Int(16),
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
)