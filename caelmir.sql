alter table DYEXTN_COLUMN_PROPERTIES drop constraint FK8FCE2B3FBC7298A9;
alter table DYEXTN_ENTITY_GROUP_REL drop constraint FK5A0D835A992A67D7;
alter table DYEXTN_ENTITY_GROUP_REL drop constraint FK5A0D835A79F466F7;
alter table DYEXTN_CONSTRAINT_PROPERTIES drop constraint FK82886CD8BC7298A9;
alter table DYEXTN_COMBO_BOX drop constraint FKCBDD2BDDBC7298A9;
alter table DYEXTN_RULE drop constraint FKC27E0994D87D1BE;
alter table DYEXTN_BYTE_ARRAY_ATTRIBUTE drop constraint FKE1A0925CBC7298A9;
alter table DYEXTN_CHECK_BOX drop constraint FK4EFF9257BC7298A9;
alter table DYEXTN_DATE_PICKER drop constraint FKFCCCC602BC7298A9;
alter table DYEXTN_TABLE_PROPERTIES drop constraint FKE608E081BC7298A9;
alter table DYEXTN_TEXT_AREA drop constraint FKFB0CA0E2BC7298A9;
alter table DYEXTN_TEXT_FIELD drop constraint FK66C9DB25BC7298A9;
alter table DYEXTN_ENTITY_GROUP drop constraint FK105DE7A0BC7298A9;
alter table DYEXTN_ENTITY_GROUP drop constraint FK105DE7A08458C1F7;
alter table DYEXTN_STRING_ATTRIBUTE drop constraint FKBA4AB5EBBC7298A9;
alter table DYEXTN_LIST_BOX drop constraint FK208395A7BC7298A9;
alter table DYEXTN_RADIO_BUTTON drop constraint FKA57864B3BC7298A9;
alter table DYEXTN_BOOLEAN_ATTRIBUTE drop constraint FK9063888BC7298A9;
alter table DYEXTN_CONTROL drop constraint FK70FB5E809C6A9B9;
alter table DYEXTN_CONTROL drop constraint FK70FB5E804D87D1BE;
alter table DYEXTN_ATTRIBUTE drop constraint FK37F1E2FF4D87D1BE;
alter table DYEXTN_ATTRIBUTE drop constraint FK37F1E2FFBC7298A9;
alter table DYEXTN_RULE_PARAMETER drop constraint FK22567363871AAD3E;
alter table DYEXTN_ENTITY drop constraint FK8B243640EBABE0B4;
alter table DYEXTN_ENTITY drop constraint FK8B243640BC7298A9;
alter table DYEXTN_DATA_GRID drop constraint FK233EB73EBC7298A9;
alter table DYEXTN_DATE_ATTRIBUTE drop constraint FKEFCF0C88BC7298A9;
alter table DYEXTN_CONTAINER drop constraint FK1EAB84E479F466F7;
alter table DYEXTN_CONTAINER drop constraint FK1EAB84E445DEFCF5;
alter table DYEXTN_PRIMITIVE_ATTRIBUTE drop constraint FKA9F765C7BC7298A9;
alter table DYEXTN_PRIMITIVE_ATTRIBUTE drop constraint FKA9F765C76B8F285C;
alter table DYEXTN_ASSOCIATION drop constraint FK104684243AC5160;
alter table DYEXTN_ASSOCIATION drop constraint FK10468424565FD063;
alter table DYEXTN_ASSOCIATION drop constraint FK10468424F60C84D6;
alter table DYEXTN_ASSOCIATION drop constraint FK104684246315C5C9;
alter table DYEXTN_ASSOCIATION drop constraint FK1046842487F497D3;
alter table DYEXTN_ASSOCIATION drop constraint FK10468424BC7298A9;
drop table DYEXTN_COLUMN_PROPERTIES cascade constraints;
drop table DYEXTN_ENTITY_GROUP_REL cascade constraints;
drop table DYEXTN_DATABASE_PROPERTIES cascade constraints;
drop table DYEXTN_CONSTRAINT_PROPERTIES cascade constraints;
drop table DYEXTN_COMBO_BOX cascade constraints;
drop table DYEXTN_ROLE cascade constraints;
drop table DYEXTN_RULE cascade constraints;
drop table DYEXTN_BYTE_ARRAY_ATTRIBUTE cascade constraints;
drop table DYEXTN_CHECK_BOX cascade constraints;
drop table DYEXTN_DATE_PICKER cascade constraints;
drop table DYEXTN_TABLE_PROPERTIES cascade constraints;
drop table DYEXTN_TEXT_AREA cascade constraints;
drop table DYEXTN_TEXT_FIELD cascade constraints;
drop table DYEXTN_ENTITY_GROUP cascade constraints;
drop table DYEXTN_STRING_ATTRIBUTE cascade constraints;
drop table DYEXTN_LIST_BOX cascade constraints;
drop table DYEXTN_RADIO_BUTTON cascade constraints;
drop table DYEXTN_BOOLEAN_ATTRIBUTE cascade constraints;
drop table DYEXTN_CONTROL cascade constraints;
drop table DYEXTN_ATTRIBUTE cascade constraints;
drop table DYEXTN_RULE_PARAMETER cascade constraints;
drop table DYEXTN_ABSTRACT_METADATA cascade constraints;
drop table DYEXTN_VIEW cascade constraints;
drop table DYEXTN_SEMANTIC_PROPERTY cascade constraints;
drop table DYEXTN_ENTITY cascade constraints;
drop table DYEXTN_DATA_GRID cascade constraints;
drop table DYEXTN_DATE_ATTRIBUTE cascade constraints;
drop table DYEXTN_CONTAINER cascade constraints;
drop table DYEXTN_PRIMITIVE_ATTRIBUTE cascade constraints;
drop table DYEXTN_ASSOCIATION cascade constraints;
drop sequence DYEXTN_SEMANTIC_PROPERTY_SEQ;
drop sequence DYEXTN_CONTAINER_SEQ;
drop sequence DYEXTN_RULE_PARAMETER_SEQ;
drop sequence DYEXTN_ABSTRACT_METADATA_SEQ;
drop sequence DYEXTN_DATABASE_PROPERTIES_SEQ;
drop sequence DYEXTN_ROLE_SEQ;
drop sequence DYEXTN_VIEW_SEQ;
drop sequence DYEXTN_CONTROL_SEQ;
drop sequence DYEXTN_RULE_SEQ;
create table DYEXTN_COLUMN_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP_REL (
   ENTITY_GROUP_ID number(19,0) not null,
   ENTITY_ID number(19,0) not null,
   primary key (ENTITY_ID, ENTITY_GROUP_ID)
);
create table DYEXTN_DATABASE_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CONSTRAINT_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   SOURCE_ENTITY_KEY varchar2(255),
   TARGET_ENTITY_KEY varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_COMBO_BOX (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_ROLE (
   IDENTIFIER number(19,0) not null,
   ASSOCIATION_TYPE varchar2(255),
   MAX_CARDINALITY number(10,0),
   MIN_CARDINALITY number(10,0),
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_BYTE_ARRAY_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   CONTENT_TYPE varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CHECK_BOX (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_PICKER (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_TABLE_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXT_AREA (
   IDENTIFIER number(19,0) not null,
   COLUMNS number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXT_FIELD (
   IDENTIFIER number(19,0) not null,
   COLUMNS number(10,0),
   PASSWORD number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP (
   IDENTIFIER number(19,0) not null,
   ROOT_NODE_ENTITY_ID number(19,0),
   ENTITY_GROUP_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   DEFAULT_VALUE varchar2(255),
   SIZE number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_LIST_BOX (
   IDENTIFIER number(19,0) not null,
   MULTISELECT number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_RADIO_BUTTON (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   DEFAULT_VALUE number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTROL (
   IDENTIFIER number(19,0) not null,
   ATTRIBUTE_ID number(19,0),
   CAPTION varchar2(255),
   CSS_CLASS varchar2(255),
   HIDDEN number(1,0),
   NAME varchar2(255),
   SEQUENCE_NUMBER number(10,0),
   TOOL_TIP varchar2(255),
   CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE_PARAMETER (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   VALUE varchar2(255),
   RULE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ABSTRACT_METADATA (
   IDENTIFIER number(19,0) not null,
   CREATED_DATE date,
   DESCRIPTION varchar2(255),
   LAST_UPDATED date,
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_VIEW (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_SEMANTIC_PROPERTY (
   IDENTIFIER number(19,0) not null,
   CONCEPT_CODE varchar2(255),
   TERM varchar2(255),
   THESAURAS_NAME varchar2(255),
   ABSTRACT_METADATA_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY (
   IDENTIFIER number(19,0) not null,
   TABLE_PROPERTY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_GRID (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   DEFAULT_VALUE number(19,0),
   MEASUREMENT_UNIT varchar2(255),
   SCALE number(10,0),
   SIZE number(10,0),
   FORMAT varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINER (
   IDENTIFIER number(19,0) not null,
   BUTTON_CSS varchar2(255),
   CAPTION varchar2(255),
   ENTITY_ID number(19,0),
   MAIN_TABLE_CSS varchar2(255),
   REQUIRED_FIELD_INDICATOR varchar2(255),
   REQUIRED_FIELD_WARNING_MESSAGE varchar2(255),
   TITLE_CSS varchar2(255),
   VIEW_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_PRIMITIVE_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   COLUMN_PROPERTY_ID number(19,0),
   IS_COLLECTION number(1,0),
   IS_IDENTIFIED number(1,0),
   IS_PRIMARY_KEY number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   CONSTRAINT_PROPERTY_ID number(19,0),
   DIRECTION varchar2(255),
   SOURCE_ENTITY_ID number(19,0),
   SOURCE_ROLE_ID number(19,0),
   TARGET_ENTITY_ID number(19,0),
   TARGET_ROLE_ID number(19,0),
   primary key (IDENTIFIER)
);
alter table DYEXTN_COLUMN_PROPERTIES add constraint FK8FCE2B3FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES;
alter table DYEXTN_ENTITY_GROUP_REL add constraint FK5A0D835A992A67D7 foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP;
alter table DYEXTN_ENTITY_GROUP_REL add constraint FK5A0D835A79F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_CONSTRAINT_PROPERTIES add constraint FK82886CD8BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES;
alter table DYEXTN_COMBO_BOX add constraint FKCBDD2BDDBC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_RULE add constraint FKC27E0994D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_BYTE_ARRAY_ATTRIBUTE add constraint FKE1A0925CBC7298A9 foreign key (IDENTIFIER) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_CHECK_BOX add constraint FK4EFF9257BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_DATE_PICKER add constraint FKFCCCC602BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_TABLE_PROPERTIES add constraint FKE608E081BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES;
alter table DYEXTN_TEXT_AREA add constraint FKFB0CA0E2BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_TEXT_FIELD add constraint FK66C9DB25BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_ENTITY_GROUP add constraint FK105DE7A0BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_ENTITY_GROUP add constraint FK105DE7A08458C1F7 foreign key (ROOT_NODE_ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_STRING_ATTRIBUTE add constraint FKBA4AB5EBBC7298A9 foreign key (IDENTIFIER) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_LIST_BOX add constraint FK208395A7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_RADIO_BUTTON add constraint FKA57864B3BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_BOOLEAN_ATTRIBUTE add constraint FK9063888BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_CONTROL add constraint FK70FB5E809C6A9B9 foreign key (CONTAINER_ID) references DYEXTN_CONTAINER;
alter table DYEXTN_CONTROL add constraint FK70FB5E804D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_ATTRIBUTE add constraint FK37F1E2FF4D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ATTRIBUTE add constraint FK37F1E2FFBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_RULE_PARAMETER add constraint FK22567363871AAD3E foreign key (RULE_ID) references DYEXTN_RULE;
alter table DYEXTN_ENTITY add constraint FK8B243640EBABE0B4 foreign key (TABLE_PROPERTY_ID) references DYEXTN_TABLE_PROPERTIES;
alter table DYEXTN_ENTITY add constraint FK8B243640BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_DATA_GRID add constraint FK233EB73EBC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_DATE_ATTRIBUTE add constraint FKEFCF0C88BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_CONTAINER add constraint FK1EAB84E479F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_CONTAINER add constraint FK1EAB84E445DEFCF5 foreign key (VIEW_ID) references DYEXTN_VIEW;
alter table DYEXTN_PRIMITIVE_ATTRIBUTE add constraint FKA9F765C7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_PRIMITIVE_ATTRIBUTE add constraint FKA9F765C76B8F285C foreign key (COLUMN_PROPERTY_ID) references DYEXTN_COLUMN_PROPERTIES;
alter table DYEXTN_ASSOCIATION add constraint FK104684243AC5160 foreign key (SOURCE_ROLE_ID) references DYEXTN_ROLE;
alter table DYEXTN_ASSOCIATION add constraint FK10468424565FD063 foreign key (CONSTRAINT_PROPERTY_ID) references DYEXTN_CONSTRAINT_PROPERTIES;
alter table DYEXTN_ASSOCIATION add constraint FK10468424F60C84D6 foreign key (TARGET_ROLE_ID) references DYEXTN_ROLE;
alter table DYEXTN_ASSOCIATION add constraint FK104684246315C5C9 foreign key (TARGET_ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ASSOCIATION add constraint FK1046842487F497D3 foreign key (SOURCE_ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ASSOCIATION add constraint FK10468424BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE;
create sequence DYEXTN_SEMANTIC_PROPERTY_SEQ;
create sequence DYEXTN_CONTAINER_SEQ;
create sequence DYEXTN_RULE_PARAMETER_SEQ;
create sequence DYEXTN_ABSTRACT_METADATA_SEQ;
create sequence DYEXTN_DATABASE_PROPERTIES_SEQ;
create sequence DYEXTN_ROLE_SEQ;
create sequence DYEXTN_VIEW_SEQ;
create sequence DYEXTN_CONTROL_SEQ;
create sequence DYEXTN_RULE_SEQ;
