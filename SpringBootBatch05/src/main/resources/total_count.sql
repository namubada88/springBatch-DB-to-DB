drop table total_count;
create table total_count(
create_date date,
count int);
insert into total_count(create_date, count) values(sysdate,(select count(num) from user_registration));