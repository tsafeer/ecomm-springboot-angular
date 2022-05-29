drop table if exists region;
drop table if exists country;
drop table if exists Item_type;
drop table if exists Sales_Channel;
drop table if exists orders;


create or replace table orders (
Order_ID number(13,0) PRIMARY KEY,
ordered_by varchar2(9),
Order_Date date,
Order_Priority varchar2(1),
Region_id number(3),
Country_id number(3),
Item_Type_id number(3),
Sales_Channel_id number(3),
Ship_Date date,
Units_Sold number(10,0),
Unit_Price number(13,2),
Unit_Cost number(13,2),
Total_Revenue number(13,2),
Total_Cost number(13,2),
Total_Profit number(13,2)
);

create table if not exists region (
region_id IDENTITY NOT NULL PRIMARY KEY ,
region_name varchar2(50)
);

create table if not exists country (
country_id IDENTITY NOT NULL PRIMARY KEY ,
country_name varchar2(50)
);


create table if not exists Item_Type (
Item_Type_id IDENTITY NOT NULL PRIMARY KEY ,
Item_Type_name varchar2(50)
);

create table if not exists Sales_Channel (
Sales_Channel_id IDENTITY NOT NULL PRIMARY KEY ,
Sales_Channel_name varchar2(50)
);