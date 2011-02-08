alter table dyextn_control add IS_PASTE_BUTTON_EANBLED boolean default 1;

alter table DYEXTN_USERDEFINED_DE add column ACTIVATION_DATE date;

alter table DYEXTN_CATEGORY_ATTRIBUTE add POPULATE_FROM_XML boolean default 0;

alter table DYEXTN_CATEGORY add POPULATE_FROM_XML boolean default 0;

create table DYEXTN_AUTO_LOADXPATH (IDENTIFIER bigint NOT NULL,XPATH varchar(4000),CATEGORY_ID bigint,PRIMARY KEY ("IDENTIFIER"));

alter table DYEXTN_AUTO_LOADXPATH add constraint FK42B764F339D13543 foreign key (CATEGORY_ID) references DYEXTN_Category (IDENTIFIER);

create table DYEXTN_CAT_XPATH_CONCEPTCODE (AUTO_LOAD_XPATH_ID bigint,CONCEPT_CODE varchar(4000));

alter table DYEXTN_CAT_XPATH_CONCEPTCODE add constraint FK42B764F339D12243 foreign key (AUTO_LOAD_XPATH_ID) references DYEXTN_AUTO_LOADXPATH (IDENTIFIER);

/* SQL for changes made for supporting Permissible Value in XML format and semantic codes*/
alter table DYEXTN_PERMISSIBLE_VALUE add NUMERIC_CODE integer;

alter table DYEXTN_SEMANTIC_PROPERTY add (CONCEPT_DEF_VALUE_ID integer);

create table DYEXTN_ABSTR_CATEGORY (IDENTIFIER bigint not null, PRIMARY KEY ("IDENTIFIER"));

create table DYEXTN_STATIC_CATEGORY (IDENTIFIER bigint not null, FORM_URL varchar(800), PRIMARY KEY ("IDENTIFIER"));

alter table DYEXTN_STATIC_CATEGORY add DataQuery varchar2(1800);

alter table DYEXTN_CATEGORY DROP CONSTRAINT fkd33de81b728b19be;

alter table DYEXTN_ABSTR_CATEGORY add constraint FK12E0EF6A728B19BE foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);;

alter table DYEXTN_STATIC_CATEGORY add constraint FKF69A71F290605B8D foreign key (IDENTIFIER) references DYEXTN_ABSTR_CATEGORY (IDENTIFIER);;

insert into DYEXTN_ABSTR_CATEGORY select IDENTIFIER FROM DYEXTN_CATEGORY;

alter table DYEXTN_CATEGORY add constraint FKD33DE81B90605B8D foreign key (IDENTIFIER) references DYEXTN_ABSTR_CATEGORY (IDENTIFIER);;

alter table DYEXTN_SEMANTIC_PROPERTY modify CONCEPT_DEFINITION varchar(4000);

alter table DYEXTN_CONTROL modify NAME varchar(400);

alter table DYEXTN_CONTROL modify HEADING varchar(800);

create table DYEXTN_USERDEF_DEF_PV_REL (USER_DEF_DE_ID bigint not null, PERMISSIBLE_VALUE_ID bigint not null, PRIMARY KEY ("USER_DEF_DE_ID", "PERMISSIBLE_VALUE_ID"));

alter table DYEXTN_USERDEF_DEF_PV_REL add constraint FK3EE69DCF49BDD78 foreign key (PERMISSIBLE_VALUE_ID) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);

alter table DYEXTN_USERDEF_DEF_PV_REL add constraint FK3EE69DCF5521B217 foreign key (USER_DEF_DE_ID) references DYEXTN_USERDEFINED_DE (IDENTIFIER);


/* Scripts for generating schema related to new Skip Logic Design */

create table DYEXTN_ACTION (ACTION_ID integer, CONTROL integer,
foreign key (CONTROL) references DYEXTN_CONTROL (identifier),
primary key (ACTION_ID));

create table DYEXTN_HIDE_ACTION (ACTION_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
primary key (ACTION_ID));

create table DYEXTN_SHOW_ACTION (ACTION_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
primary key (ACTION_ID));

create table DYEXTN_ENABLE_ACTION (ACTION_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
primary key (ACTION_ID));

create table DYEXTN_DISABLE_ACTION (ACTION_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
primary key (ACTION_ID));

create table DYEXTN_PERMISSIBLEVALUE_ACTION (ACTION_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
primary key (ACTION_ID));

create table DYEXTN_PV_ACTION_MAPPING (ACTION_ID integer, PERMISSIBLE_VALUE_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
foreign key (PERMISSIBLE_VALUE_ID) references DYEXTN_PERMISSIBLE_VALUE(identifier));

create table DYEXTN_CONDITION (CONDITION_ID integer, ACTION_ID integer,
foreign key (ACTION_ID) references DYEXTN_ACTION(ACTION_ID),
primary key (CONDITION_ID));

create table DYEXTN_GROUP_CONDITION (CONDITION_ID integer, LOGICAL_OPERATOR varchar(20), CHILD_CONDITION_ID integer,
foreign key (CONDITION_ID) references DYEXTN_CONDITION (CONDITION_ID),
foreign key (CHILD_CONDITION_ID) references DYEXTN_CONDITION (CONDITION_ID),
primary key (CONDITION_ID));

create table DYEXTN_PRIMITIVE_CONDITION (CONDITION_ID integer, CATEGORY_ATTRIBUTE integer, CONDITION_VALUE blob, CONDITION_OPERATOR varchar(20),
foreign key (CONDITION_ID) references DYEXTN_CONDITION (CONDITION_ID),
foreign key (CATEGORY_ATTRIBUTE) references DYEXTN_CATEGORY_ATTRIBUTE (identifier),
primary key (CONDITION_ID));

create table DYEXTN_CONDITION_STMT (CONDITION_STATEMENT_ID integer, CONTROL_ID integer,
foreign key (CONTROL_ID) references DYEXTN_CONTROL(identifier),
primary key (CONDITION_STATEMENT_ID));

alter table DYEXTN_CONDITION add (COND_STMT_ID integer references DYEXTN_CONDITION_STMT(CONDITION_STATEMENT_ID));

create table DYEXTN_SKIP_LOGIC (SKIPLOGIC_ID integer, CONTAINER_IDENTIFIER integer,
foreign key (CONTAINER_IDENTIFIER) references DYEXTN_CONTAINER(identifier),
primary key (SKIPLOGIC_ID));

alter table DYEXTN_CONDITION_STMT add (SKIP_LOGIC_ID integer,
foreign key (SKIP_LOGIC_ID) references DYEXTN_SKIP_LOGIC(SKIPLOGIC_ID));

/* Scripts for generating schema related to new Skip Logic Design Ends */
alter table DYEXTN_ABSTRACT_METADATA add ACTIVITY_STATUS varchar2(10) default 'ACTIVE';

-- Script for Supporting both ways sorting of permissible values Starts
alter table DYEXTN_USERDEFINED_DE add SORT varchar(20);
-- Script for Supporting both ways sorting of permissible values Ends

alter table DYEXTN_ACTION add SKIP_LOGIC_ID integer;