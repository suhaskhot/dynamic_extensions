alter table EAV_ATTRIBUTE_RULE drop constraint FKA0BF02844D87D1BE;
alter table EAV_ATTRIBUTE drop constraint FK90607FB779F466F7;
alter table EAV_ATTRIBUTE_RULE_PARAMETER drop constraint FK4F286B8EF555D27B;
drop table CAELMIR_ADDRESS cascade constraints;
drop table EAV_ATTRIBUTE_RULE cascade constraints;
drop table EAV_ATTRIBUTE cascade constraints;
drop table EAV_ENTITY cascade constraints;
drop table EAV_ATTRIBUTE_RULE_PARAMETER cascade constraints;
drop sequence EAV_ENTITY_SEQ;
drop sequence EAV_ATTRULE_PARAM_SEQ;
drop sequence CAELMIR_ADDRESS_SEQ;
drop sequence EAV_ATTRIBUTE_SEQ;
drop sequence EAV_ATTRIBUTE_RULE_SEQ;
drop sequence CATISSUE_QUERY_TABLE_DATA_SEQ;
drop sequence CATISSUE_TABLE_RELATION_SEQ;
drop sequence CATISSUE_INTF_COLUMN_DATA_SEQ;

create table CAELMIR_ADDRESS (
   IDENTIFIER number(19,0) not null,
   STREET varchar2(50),
   CITY varchar2(50),
   STATE varchar2(50),
   COUNTRY varchar2(50),
   ZIPCODE varchar2(30),
   PHONE_NUMBER varchar2(50),
   FAX_NUMBER varchar2(50),
   primary key (IDENTIFIER)
);
create table EAV_ATTRIBUTE_RULE (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50),
   ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table EAV_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   DATA_TYPE varchar2(50),
   DEFAULT_VALUE varchar2(50),
   DESCRIPTION varchar2(100),
   DISPLAY_CHOICE char(1),
   DISPLAY_UNITS varchar2(50),
   FORMAT varchar2(50),
   NAME varchar2(50),
   REF_VALUES varchar2(100),
   SCALE number(10,0),
   VALIDATION_RULE varchar2(100),
   ENTITY_ID number(19,0),
   ATTRIBUTE_SIZE number(19,0),
   DISPLAY_SEQUENCE_NUMBER number(10,0),
   primary key (IDENTIFIER)
);
create table EAV_ENTITY (
   IDENTIFIER number(19,0) not null,
   DESCRIPTION varchar2(100),
   NAME varchar2(50),
   primary key (IDENTIFIER)
);
create table EAV_ATTRIBUTE_RULE_PARAMETER (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50),
   VALUE varchar2(50),
   ATTRIBUTE_RULE_ID number(19,0),
   primary key (IDENTIFIER)
);
alter table EAV_ATTRIBUTE_RULE add constraint FKA0BF02844D87D1BE foreign key (ATTRIBUTE_ID) references EAV_ATTRIBUTE;
alter table EAV_ATTRIBUTE add constraint FK90607FB779F466F7 foreign key (ENTITY_ID) references EAV_ENTITY;
alter table EAV_ATTRIBUTE_RULE_PARAMETER add constraint FK4F286B8EF555D27B foreign key (ATTRIBUTE_RULE_ID) references EAV_ATTRIBUTE_RULE;
create sequence EAV_ENTITY_SEQ;
create sequence EAV_ATTRULE_PARAM_SEQ;
create sequence CAELMIR_ADDRESS_SEQ;
create sequence EAV_ATTRIBUTE_SEQ;
create sequence EAV_ATTRIBUTE_RULE_SEQ;
create sequence CATISSUE_QUERY_TABLE_DATA_SEQ;
create sequence CATISSUE_TABLE_RELATION_SEQ;
create sequence CATISSUE_INTF_COLUMN_DATA_SEQ;
