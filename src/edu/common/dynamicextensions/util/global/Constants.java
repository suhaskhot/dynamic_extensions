/**
 * <p>Title: Constants Class>
 * <p>Description:  This class stores the constants used in the operations in the application.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 16, 2005
 */

package edu.common.dynamicextensions.util.global;


/**
 * This class stores the constants used in the operations in the application.
 * @author gautam_shetty
 */

public class Constants extends edu.wustl.common.util.global.Constants
{	
 
    public static final String SESSION_DATA = "sessionData";
    
    public static final String AND_JOIN_CONDITION = "AND";
	public static final String OR_JOIN_CONDITION = "OR";
	//Sri: Changed the format for displaying in Specimen Event list (#463)
	public static final String TIMESTAMP_PATTERN = "MM-dd-yyyy HH:mm";
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String TIME_PATTERN_HH_MM_SS = "HH:mm:ss";
	
	// Mandar: Used for Date Validations in Validator Class
	public static final String DATE_SEPARATOR = "-";
	public static final String DATE_SEPARATOR_DOT = ".";
	public static final String MIN_YEAR = "1900";
	public static final String MAX_YEAR = "9999";
	
	//Mysql Database constants.
	/*public static final String MYSQL_DATE_PATTERN = "%m-%d-%Y";
	public static final String MYSQL_TIME_PATTERN = "%H:%i:%s";
	public static final String MYSQL_TIME_FORMAT_FUNCTION = "TIME_FORMAT";
	public static final String MYSQL_DATE_FORMAT_FUNCTION = "DATE_FORMAT";
	public static final String MYSQL_STR_TO_DATE_FUNCTION = "STR_TO_DATE";*/
	
	public static final String VIEW = "view";
	public static final String SEARCH = "search";
	public static final String DELETE = "delete";
	public static final String EXPORT = "export";
	public static final String SHOPPING_CART_ADD = "shoppingCartAdd";
	public static final String SHOPPING_CART_DELETE = "shoppingCartDelete";
	public static final String SHOPPING_CART_EXPORT = "shoppingCartExport";
	public static final String NEWUSERFORM = "newUserForm";
	public static final String ACCESS_DENIED = "access_denied";
	public static final String REDIRECT_TO_SPECIMEN = "specimenRedirect";
	public static final String CALLED_FROM = "calledFrom";
	
	//Constants required for Forgot Password
	public static final String FORGOT_PASSWORD = "forgotpassword";
	
	public static final String IDENTIFIER = "IDENTIFIER";
	public static final String LOGINNAME = "loginName";
	public static final String LASTNAME = "lastName";
	public static final String FIRSTNAME = "firstName";
	public static final String UPPER = "UPPER";
    public static final String ERROR_DETAIL = "Error Detail";
	public static final String INSTITUTION = "institution";
	public static final String EMAIL = "email";
	public static final String DEPARTMENT = "department";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String COUNTRY = "country";
	public static final String NEXT_CONTAINER_NO = "startNumber";
	public static final String CSM_USER_ID = "csmUserId";
	
	public static final String INSTITUTIONLIST = "institutionList";
	public static final String DEPARTMENTLIST = "departmentList";
	public static final String STATELIST = "stateList";
	public static final String COUNTRYLIST = "countryList";
	public static final String ROLELIST = "roleList";
	public static final String ROLEIDLIST = "roleIdList";
	public static final String CANCER_RESEARCH_GROUP_LIST = "cancerResearchGroupList";
	public static final String GENDER_LIST = "genderList";
	public static final String GENOTYPE_LIST = "genotypeList";
	public static final String ETHNICITY_LIST = "ethnicityList";
	public static final String PARTICIPANT_MEDICAL_RECORD_SOURCE_LIST = "participantMedicalRecordSourceList";
	public static final String RACELIST = "raceList";
	public static final String PARTICIPANT_LIST = "participantList";
	public static final String PARTICIPANT_ID_LIST = "participantIdList";
	public static final String PROTOCOL_LIST = "protocolList";
	public static final String TIMEHOURLIST = "timeHourList";
	public static final String TIMEMINUTESLIST = "timeMinutesList";
	public static final String TIMEAMPMLIST = "timeAMPMList";
	public static final String RECEIVEDBYLIST = "receivedByList";
	public static final String COLLECTEDBYLIST = "collectedByList";
	public static final String COLLECTIONSITELIST = "collectionSiteList";
	public static final String RECEIVEDSITELIST = "receivedSiteList";
	public static final String RECEIVEDMODELIST = "receivedModeList";
	public static final String ACTIVITYSTATUSLIST = "activityStatusList";
	public static final String USERLIST = "userList";
	public static final String SITETYPELIST = "siteTypeList";
	public static final String STORAGETYPELIST="storageTypeList";
	public static final String STORAGECONTAINERLIST="storageContainerList";
	public static final String SITELIST="siteList";
//	public static final String SITEIDLIST="siteIdList";
	public static final String USERIDLIST = "userIdList";
	public static final String STORAGETYPEIDLIST="storageTypeIdList";
	public static final String SPECIMENCOLLECTIONLIST="specimentCollectionList";
	public static final String PARTICIPANT_IDENTIFIER_IN_CPR = "participant";
	public static final String APPROVE_USER_STATUS_LIST = "statusList";
	public static final String EVENT_PARAMETERS_LIST = "eventParametersList";
		
	//New Specimen lists.
	public static final String SPECIMEN_COLLECTION_GROUP_LIST = "specimenCollectionGroupIdList";
	public static final String SPECIMEN_TYPE_LIST = "specimenTypeList";
	public static final String SPECIMEN_SUB_TYPE_LIST = "specimenSubTypeList";
	public static final String TISSUE_SITE_LIST = "tissueSiteList";
	public static final String TISSUE_SIDE_LIST = "tissueSideList";
	public static final String PATHOLOGICAL_STATUS_LIST = "pathologicalStatusList";
	public static final String BIOHAZARD_TYPE_LIST = "biohazardTypeList";
	public static final String BIOHAZARD_NAME_LIST = "biohazardNameList";
	public static final String BIOHAZARD_ID_LIST = "biohazardIdList";
	public static final String BIOHAZARD_TYPES_LIST = "biohazardTypesList";
	public static final String PARENT_SPECIMEN_ID_LIST = "parentSpecimenIdList";
	public static final String RECEIVED_QUALITY_LIST = "receivedQualityList";
	
	//SpecimenCollecionGroup lists.
	public static final String PROTOCOL_TITLE_LIST = "protocolTitleList";
	public static final String PARTICIPANT_NAME_LIST = "participantNameList";
	public static final String PROTOCOL_PARTICIPANT_NUMBER_LIST = "protocolParticipantNumberList";
	//public static final String PROTOCOL_PARTICIPANT_NUMBER_ID_LIST = "protocolParticipantNumberIdList";
	public static final String STUDY_CALENDAR_EVENT_POINT_LIST = "studyCalendarEventPointList";
   	//public static final String STUDY_CALENDAR_EVENT_POINT_ID_LIST="studyCalendarEventPointIdList";
	public static final String PARTICIPANT_MEDICAL_IDNETIFIER_LIST = "participantMedicalIdentifierArray";
	//public static final String PARTICIPANT_MEDICAL_IDNETIFIER_ID_LIST = "participantMedicalIdentifierIdArray";
	public static final String SPECIMEN_COLLECTION_GROUP_ID = "specimenCollectionGroupId";
	public static final String REQ_PATH = "redirectTo";
	
	public static final String CLINICAL_STATUS_LIST = "cinicalStatusList";
	public static final String SPECIMEN_CLASS_LIST = "specimenClassList";
	public static final String SPECIMEN_CLASS_ID_LIST = "specimenClassIdList";
	public static final String SPECIMEN_TYPE_MAP = "specimenTypeMap";
	//Simple Query Interface Lists
	public static final String OBJECT_NAME_LIST = "objectNameList";
	public static final String OBJECT_COMPLETE_NAME_LIST = "objectCompleteNameList";
	public static final String SIMPLE_QUERY_INTERFACE_TITLE = "simpleQueryInterfaceTitle";
    
	public static final String ATTRIBUTE_NAME_LIST = "attributeNameList";
	public static final String ATTRIBUTE_CONDITION_LIST = "attributeConditionList";
	
	//Constants for Storage Container.
	public static final String STORAGE_CONTAINER_TYPE = "storageType";
	public static final String STORAGE_CONTAINER_TO_BE_SELECTED = "storageToBeSelected";
	public static final String STORAGE_CONTAINER_POSITION = "position";
	
	public static final String STORAGE_CONTAINER_GRID_OBJECT = "storageContainerGridObject";
	public static final String STORAGE_CONTAINER_CHILDREN_STATUS = "storageContainerChildrenStatus";
	public static final String START_NUMBER = "startNumber";
	public static final String CHILD_CONTAINER_SYSTEM_IDENTIFIERS = "childContainerSystemIdentifiers";
	public static final int STORAGE_CONTAINER_FIRST_ROW = 1;
	public static final int STORAGE_CONTAINER_FIRST_COLUMN = 1;
	
	//event parameters lists
	public static final String METHOD_LIST = "methodList";
	public static final String HOUR_LIST = "hourList";
	public static final String MINUTES_LIST = "minutesList";
	public static final String EMBEDDING_MEDIUM_LIST = "embeddingMediumList";
	public static final String PROCEDURE_LIST = "procedureList";
	public static final String PROCEDUREIDLIST = "procedureIdList";
	public static final String CONTAINER_LIST = "containerList";
	public static final String CONTAINERIDLIST = "containerIdList";
	public static final String FROMCONTAINERLIST="fromContainerList";
	public static final String TOCONTAINERLIST="toContainerList";
	public static final String FIXATION_LIST = "fixationList";	
	public static final String FROM_SITE_LIST="fromsiteList";
	public static final String TO_SITE_LIST="tositeList";	
	public static final String ITEMLIST="itemList";
	public static final String DISTRIBUTIONPROTOCOLLIST="distributionProtocolList";
	public static final String TISSUE_SPECIMEN_ID_LIST="tissueSpecimenIdList";
	public static final String MOLECULAR_SPECIMEN_ID_LIST="molecularSpecimenIdList";
	public static final String CELL_SPECIMEN_ID_LIST="cellSpecimenIdList";
	public static final String FLUID_SPECIMEN_ID_LIST="fluidSpecimenIdList";
	public static final String STORAGE_STATUS_LIST="storageStatusList";
	public static final String CLINICAL_DIAGNOSIS_LIST = "clinicalDiagnosisList";
	public static final String HISTOLOGICAL_QUALITY_LIST="histologicalQualityList";
	
	//For Specimen Event Parameters.
	public static final String SPECIMEN_ID = "specimenId";
	public static final String FROM_POSITION_DATA = "fromPositionData";
	public static final String POS_ONE ="posOne";
	public static final String POS_TWO ="posTwo";
	public static final String STORAGE_CONTAINER_ID ="storContId";	
	public static final String IS_RNA = "isRNA";
	public static final String RNA = "RNA";
	
	//	New Participant Event Parameters
	public static final String PARTICIPANT_ID="participantId";
	
	//Constants required in User.jsp Page
	public static final String USER_SEARCH_ACTION = "UserSearch.do";
	public static final String USER_ADD_ACTION = "UserAdd.do";
	public static final String USER_EDIT_ACTION = "UserEdit.do";
	public static final String APPROVE_USER_ADD_ACTION = "ApproveUserAdd.do";
	public static final String APPROVE_USER_EDIT_ACTION = "ApproveUserEdit.do";
	public static final String SIGNUP_USER_ADD_ACTION = "SignUpUserAdd.do";
	public static final String USER_EDIT_PROFILE_ACTION = "UserEditProfile.do";
	public static final String UPDATE_PASSWORD_ACTION = "UpdatePassword.do";
	
	//Constants required in Accession.jsp Page
	public static final String ACCESSION_SEARCH_ACTION = "AccessionSearch.do";
	public static final String ACCESSION_ADD_ACTION = "AccessionAdd.do";
	public static final String ACCESSION_EDIT_ACTION = "AccessionEdit.do";
	
	//Constants required in StorageType.jsp Page
	public static final String STORAGE_TYPE_SEARCH_ACTION = "StorageTypeSearch.do";
	public static final String STORAGE_TYPE_ADD_ACTION = "StorageTypeAdd.do";
	public static final String STORAGE_TYPE_EDIT_ACTION = "StorageTypeEdit.do";
	
	//Constants required in StorageContainer.jsp Page
	public static final String STORAGE_CONTAINER_SEARCH_ACTION = "StorageContainerSearch.do";
	public static final String STORAGE_CONTAINER_ADD_ACTION = "StorageContainerAdd.do";
	public static final String STORAGE_CONTAINER_EDIT_ACTION = "StorageContainerEdit.do";
	
	public static final String SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION = "ShowStorageGridView.do";
	
	//Constants required in Site.jsp Page
	public static final String SITE_SEARCH_ACTION = "SiteSearch.do";
	public static final String SITE_ADD_ACTION = "SiteAdd.do";
	public static final String SITE_EDIT_ACTION = "SiteEdit.do";
	
	//Constants required in Site.jsp Page
	public static final String BIOHAZARD_SEARCH_ACTION = "BiohazardSearch.do";
	public static final String BIOHAZARD_ADD_ACTION = "BiohazardAdd.do";
	public static final String BIOHAZARD_EDIT_ACTION = "BiohazardEdit.do";
	
	//Constants required in Partcipant.jsp Page
	public static final String PARTICIPANT_SEARCH_ACTION = "ParticipantSearch.do";
	public static final String PARTICIPANT_ADD_ACTION = "ParticipantAdd.do";
	public static final String PARTICIPANT_EDIT_ACTION = "ParticipantEdit.do";

	//Constants required in Institution.jsp Page
	public static final String INSTITUTION_SEARCH_ACTION = "InstitutionSearch.do";
	public static final String INSTITUTION_ADD_ACTION = "InstitutionAdd.do";
	public static final String INSTITUTION_EDIT_ACTION = "InstitutionEdit.do";

	//Constants required in Department.jsp Page
	public static final String DEPARTMENT_SEARCH_ACTION = "DepartmentSearch.do";
	public static final String DEPARTMENT_ADD_ACTION = "DepartmentAdd.do";
	public static final String DEPARTMENT_EDIT_ACTION = "DepartmentEdit.do";
	
    //Constants required in CollectionProtocolRegistration.jsp Page
	public static final String COLLECTION_PROTOCOL_REGISTRATION_SEARCH_ACTION = "CollectionProtocolRegistrationSearch.do";
	public static final String COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION = "CollectionProtocolRegistrationAdd.do";
	public static final String COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION = "CollectionProtocolRegistrationEdit.do";
	
	//Constants required in CancerResearchGroup.jsp Page
	public static final String CANCER_RESEARCH_GROUP_SEARCH_ACTION = "CancerResearchGroupSearch.do";
	public static final String CANCER_RESEARCH_GROUP_ADD_ACTION = "CancerResearchGroupAdd.do";
	public static final String CANCER_RESEARCH_GROUP_EDIT_ACTION = "CancerResearchGroupEdit.do";
	
	//Constants required for Approve user
	public static final String USER_DETAILS_SHOW_ACTION = "UserDetailsShow.do";
	public static final String APPROVE_USER_SHOW_ACTION = "ApproveUserShow.do";
	
	//Reported Problem Constants
	public static final String REPORTED_PROBLEM_ADD_ACTION = "ReportedProblemAdd.do";
	public static final String REPORTED_PROBLEM_EDIT_ACTION = "ReportedProblemEdit.do";
	public static final String PROBLEM_DETAILS_ACTION = "ProblemDetails.do";
	public static final String REPORTED_PROBLEM_SHOW_ACTION = "ReportedProblemShow.do";
	
	//Query Results view Actions
	public static final String TREE_VIEW_ACTION = "TreeView.do";
	public static final String DATA_VIEW_FRAME_ACTION = "SpreadsheetView.do";
	public static final String SIMPLE_QUERY_INTERFACE_ACTION = "/SimpleQueryInterface.do";
	public static final String SEARCH_OBJECT_ACTION = "/SearchObject.do";
	public static final String SIMPLE_QUERY_INTERFACE_URL = "SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface&menuSelected=17";
	
	//New Specimen Data Actions.
	public static final String SPECIMEN_ADD_ACTION = "NewSpecimenAdd.do";
	public static final String SPECIMEN_EDIT_ACTION = "NewSpecimenEdit.do";
	public static final String SPECIMEN_SEARCH_ACTION = "NewSpecimenSearch.do";
	
	public static final String SPECIMEN_EVENT_PARAMETERS_ACTION = "ListSpecimenEventParameters.do";
	
	//Create Specimen Data Actions.
	public static final String CREATE_SPECIMEN_ADD_ACTION = "CreateSpecimenAdd.do";
	public static final String CREATE_SPECIMEN_EDIT_ACTION = "CreateSpecimenEdit.do";
	public static final String CREATE_SPECIMEN_SEARCH_ACTION = "CreateSpecimenSearch.do";
	
	//ShoppingCart Actions.
	public static final String SHOPPING_CART_OPERATION = "ShoppingCartOperation.do";

	public static final String SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION = "SpecimenCollectionGroup.do";
	public static final String SPECIMEN_COLLECTION_GROUP_ADD_ACTION = "SpecimenCollectionGroupAdd.do";
	public static final String SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "SpecimenCollectionGroupEdit.do";

	//Constants required in FrozenEventParameters.jsp Page
	public static final String FROZEN_EVENT_PARAMETERS_SEARCH_ACTION = "FrozenEventParametersSearch.do";
	public static final String FROZEN_EVENT_PARAMETERS_ADD_ACTION = "FrozenEventParametersAdd.do";
	public static final String FROZEN_EVENT_PARAMETERS_EDIT_ACTION = "FrozenEventParametersEdit.do";

	//Constants required in CheckInCheckOutEventParameters.jsp Page
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_SEARCH_ACTION = "CheckInCheckOutEventParametersSearch.do";
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_ADD_ACTION = "CheckInCheckOutEventParametersAdd.do";
	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETERS_EDIT_ACTION = "CheckInCheckOutEventParametersEdit.do";

	//Constants required in ReceivedEventParameters.jsp Page
	public static final String RECEIVED_EVENT_PARAMETERS_SEARCH_ACTION = "receivedEventParametersSearch.do";
	public static final String RECEIVED_EVENT_PARAMETERS_ADD_ACTION = "ReceivedEventParametersAdd.do";
	public static final String RECEIVED_EVENT_PARAMETERS_EDIT_ACTION = "receivedEventParametersEdit.do";

	//Constants required in FluidSpecimenReviewEventParameters.jsp Page
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION = "FluidSpecimenReviewEventParametersSearch.do";
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION = "FluidSpecimenReviewEventParametersAdd.do";
	public static final String FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION = "FluidSpecimenReviewEventParametersEdit.do";

	//Constants required in CELLSPECIMENREVIEWParameters.jsp Page
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION = "CellSpecimenReviewParametersSearch.do";
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION = "CellSpecimenReviewParametersAdd.do";
	public static final String CELL_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION = "CellSpecimenReviewParametersEdit.do";

	//Constants required in tissue SPECIMEN REVIEW event Parameters.jsp Page
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_SEARCH_ACTION = "TissueSpecimenReviewEventParametersSearch.do";
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION = "TissueSpecimenReviewEventParametersAdd.do";
	public static final String TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION = "TissueSpecimenReviewEventParametersEdit.do";

	//	Constants required in DisposalEventParameters.jsp Page	
	public static final String DISPOSAL_EVENT_PARAMETERS_SEARCH_ACTION = "DisposalEventParametersSearch.do";
	public static final String DISPOSAL_EVENT_PARAMETERS_ADD_ACTION = "DisposalEventParametersAdd.do";
	public static final String DISPOSAL_EVENT_PARAMETERS_EDIT_ACTION = "DisposalEventParametersEdit.do";
	
	//	Constants required in ThawEventParameters.jsp Page
	public static final String THAW_EVENT_PARAMETERS_SEARCH_ACTION = "ThawEventParametersSearch.do";
	public static final String THAW_EVENT_PARAMETERS_ADD_ACTION = "ThawEventParametersAdd.do";
	public static final String THAW_EVENT_PARAMETERS_EDIT_ACTION = "ThawEventParametersEdit.do";

	//	Constants required in MOLECULARSPECIMENREVIEWParameters.jsp Page
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_SEARCH_ACTION = "MolecularSpecimenReviewParametersSearch.do";
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION = "MolecularSpecimenReviewParametersAdd.do";
	public static final String MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION = "MolecularSpecimenReviewParametersEdit.do";

	//	Constants required in CollectionEventParameters.jsp Page
	public static final String COLLECTION_EVENT_PARAMETERS_SEARCH_ACTION = "CollectionEventParametersSearch.do";
	public static final String COLLECTION_EVENT_PARAMETERS_ADD_ACTION = "CollectionEventParametersAdd.do";
	public static final String COLLECTION_EVENT_PARAMETERS_EDIT_ACTION = "CollectionEventParametersEdit.do";
	
	//	Constants required in SpunEventParameters.jsp Page
	public static final String SPUN_EVENT_PARAMETERS_SEARCH_ACTION = "SpunEventParametersSearch.do";
	public static final String SPUN_EVENT_PARAMETERS_ADD_ACTION = "SpunEventParametersAdd.do";
	public static final String SPUN_EVENT_PARAMETERS_EDIT_ACTION = "SpunEventParametersEdit.do";
	
	//	Constants required in EmbeddedEventParameters.jsp Page
	public static final String EMBEDDED_EVENT_PARAMETERS_SEARCH_ACTION = "EmbeddedEventParametersSearch.do";
	public static final String EMBEDDED_EVENT_PARAMETERS_ADD_ACTION = "EmbeddedEventParametersAdd.do";
	public static final String EMBEDDED_EVENT_PARAMETERS_EDIT_ACTION = "EmbeddedEventParametersEdit.do";
	
	//	Constants required in TransferEventParameters.jsp Page
	public static final String TRANSFER_EVENT_PARAMETERS_SEARCH_ACTION = "TransferEventParametersSearch.do";
	public static final String TRANSFER_EVENT_PARAMETERS_ADD_ACTION = "TransferEventParametersAdd.do";
	public static final String TRANSFER_EVENT_PARAMETERS_EDIT_ACTION = "TransferEventParametersEdit.do";

//	Constants required in FixedEventParameters.jsp Page
	public static final String FIXED_EVENT_PARAMETERS_SEARCH_ACTION = "FixedEventParametersSearch.do";
	public static final String FIXED_EVENT_PARAMETERS_ADD_ACTION = "FixedEventParametersAdd.do";
	public static final String FIXED_EVENT_PARAMETERS_EDIT_ACTION = "FixedEventParametersEdit.do";

//	Constants required in ProcedureEventParameters.jsp Page
	public static final String PROCEDURE_EVENT_PARAMETERS_SEARCH_ACTION = "ProcedureEventParametersSearch.do";
	public static final String PROCEDURE_EVENT_PARAMETERS_ADD_ACTION = "ProcedureEventParametersAdd.do";
	public static final String PROCEDURE_EVENT_PARAMETERS_EDIT_ACTION = "ProcedureEventParametersEdit.do";
	
	//	Constants required in Distribution.jsp Page
	public static final String DISTRIBUTION_SEARCH_ACTION = "DistributionSearch.do";
	public static final String DISTRIBUTION_ADD_ACTION = "DistributionAdd.do";
	public static final String DISTRIBUTION_EDIT_ACTION = "DistributionEdit.do";
	
	//Spreadsheet Export Action
	public static final String SPREADSHEET_EXPORT_ACTION = "SpreadsheetExport.do";
	
	//Levels of nodes in query results tree.
	public static final int MAX_LEVEL = 5;
	public static final int MIN_LEVEL = 1;
	
	public static final String[] DEFAULT_TREE_SELECT_COLUMNS = {
	        
	};
	
	public static final String TABLE_DATA_TABLE_NAME = "CATISSUE_QUERY_TABLE_DATA";
	public static final String TABLE_DISPLAY_NAME_COLUMN = "DISPLAY_NAME";
	public static final String TABLE_ALIAS_NAME_COLUMN = "ALIAS_NAME";
	public static final String TABLE_FOR_SQI_COLUMN = "FOR_SQI";
	public static final String TABLE_ID_COLUMN = "TABLE_ID";
	public static final String TABLE_NAME_COLUMN = "TABLE_NAME";
	
	
	//Frame names in Query Results page.
	public static final String DATA_VIEW_FRAME = "myframe1";
	public static final String APPLET_VIEW_FRAME = "appletViewFrame";
	
	//NodeSelectionlistener - Query Results Tree node selection (For spreadsheet or individual view).
	public static final String DATA_VIEW_ACTION = "DataView.do?nodeName=";
	public static final String VIEW_TYPE = "viewType";
	
	//TissueSite Tree View Constants.
	public static final String PROPERTY_NAME = "propertyName";
	
	//Constants for type of query results view.
	public static final String SPREADSHEET_VIEW = "Spreadsheet View";
	public static final String OBJECT_VIEW = "Edit View";
	
	//Spreadsheet view Constants in DataViewAction.
	public static final String PARTICIPANT = "Participant";
	public static final String ACCESSION = "Accession";
	public static final String QUERY_PARTICIPANT_SEARCH_ACTION = "QueryParticipantSearch.do?systemIdentifier=";
	public static final String QUERY_PARTICIPANT_EDIT_ACTION = "QueryParticipantEdit.do";
	public static final String QUERY_COLLECTION_PROTOCOL_SEARCH_ACTION = "QueryCollectionProtocolSearch.do?systemIdentifier=";
	public static final String QUERY_COLLECTION_PROTOCOL_EDIT_ACTION = "QueryCollectionProtocolEdit.do";
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_SEARCH_ACTION = "QuerySpecimenCollectionGroupSearch.do?systemIdentifier=";
	public static final String QUERY_SPECIMEN_COLLECTION_GROUP_EDIT_ACTION = "QuerySpecimenCollectionGroupEdit.do";
	public static final String QUERY_SPECIMEN_SEARCH_ACTION = "QuerySpecimenSearch.do?systemIdentifier=";
	public static final String QUERY_SPECIMEN_EDIT_ACTION = "QuerySpecimenEdit.do";
	//public static final String QUERY_ACCESSION_SEARCH_ACTION = "QueryAccessionSearch.do?systemIdentifier=";
	
	//Individual view Constants in DataViewAction.
	public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";
	public static final String SPREADSHEET_DATA_LIST = "spreadsheetDataList";
	public static final String SELECT_COLUMN_LIST = "selectColumnList";
	public static final String CONFIGURED_SELECT_COLUMN_LIST = "configuredSelectColumnList";
	public static final String CONFIGURED_COLUMN_DISPLAY_NAMES = "configuredColumnDisplayNames";
	public static final String CONFIGURED_COLUMN_NAMES = "configuredColumnNames";
	public static final String SELECTED_NODE = "selectedNode";
	
	//Tree Data Action
	public static final String TREE_DATA_ACTION = "Data.do";
	
	public static final String SPECIMEN = "Specimen";
	public static final String SEGMENT = "Segment";
	public static final String SAMPLE = "Sample";
	public static final String COLLECTION_PROTOCOL_REGISTRATION = "CollectionProtocolRegistration";
	public static final String PARTICIPANT_ID_COLUMN = "PARTICIPANT_ID";
	public static final String ACCESSION_ID_COLUMN = "ACCESSION_ID";
	public static final String SPECIMEN_ID_COLUMN = "SPECIMEN_ID";
	public static final String SEGMENT_ID_COLUMN = "SEGMENT_ID";
	public static final String SAMPLE_ID_COLUMN = "SAMPLE_ID";
	
	//SimpleSearchAction
	public static final String SIMPLE_QUERY_NO_RESULTS = "noResults";
	public static final String SIMPLE_QUERY_SINGLE_RESULT = "singleResult";
	
	//For getting the tables for Simple Query and Fcon Query.
	public static final int SIMPLE_QUERY_TABLES = 1;
	public static final int ADVANCE_QUERY_TABLES = 2;
	
	//Identifiers for various Form beans
	public static final int USER_FORM_ID = 1;
	public static final int PARTICIPANT_FORM_ID = 2;
	public static final int ACCESSION_FORM_ID = 3;
	public static final int REPORTED_PROBLEM_FORM_ID = 4;
	public static final int INSTITUTION_FORM_ID = 5;
	public static final int APPROVE_USER_FORM_ID = 6;
	public static final int ACTIVITY_STATUS_FORM_ID = 7;
	public static final int DEPARTMENT_FORM_ID = 8;
	public static final int COLLECTION_PROTOCOL_FORM_ID = 9;
	public static final int DISTRIBUTIONPROTOCOL_FORM_ID = 10;
	public static final int STORAGE_CONTAINER_FORM_ID = 11;
	public static final int STORAGE_TYPE_FORM_ID = 12;
	public static final int SITE_FORM_ID = 13;
	public static final int CANCER_RESEARCH_GROUP_FORM_ID = 14;
	public static final int BIOHAZARD_FORM_ID = 15;
	public static final int FROZEN_EVENT_PARAMETERS_FORM_ID = 16;
	public static final int CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID = 17;
	public static final int RECEIVED_EVENT_PARAMETERS_FORM_ID = 18;
	public static final int COLLECTION_PROTOCOL_REGISTRATION_FORM_ID = 19;
	public static final int SPECIMEN_COLLECTION_GROUP_FORM_ID = 20;
	public static final int FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 21;
	public static final int NEW_SPECIMEN_FORM_ID = 22;
	public static final int CELL_SPECIMEN_REVIEW_PARAMETERS_FORM_ID =23;
	public static final int TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID = 24;
	public static final int DISPOSAL_EVENT_PARAMETERS_FORM_ID = 25;
	public static final int THAW_EVENT_PARAMETERS_FORM_ID = 26;
	public static final int MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_FORM_ID = 27;
	public static final int COLLECTION_EVENT_PARAMETERS_FORM_ID = 28;
	public static final int TRANSFER_EVENT_PARAMETERS_FORM_ID = 29;
	public static final int SPUN_EVENT_PARAMETERS_FORM_ID = 30;
	public static final int EMBEDDED_EVENT_PARAMETERS_FORM_ID = 31;
	public static final int FIXED_EVENT_PARAMETERS_FORM_ID = 32;	
	public static final int PROCEDURE_EVENT_PARAMETERS_FORM_ID = 33;
	public static final int CREATE_SPECIMEN_FORM_ID = 34;
	public static final int FORGOT_PASSWORD_FORM_ID = 35;
	public static final int SIGNUP_FORM_ID = 36;
	public static final int DISTRIBUTION_FORM_ID = 37;
	public static final int SPECIMEN_EVENT_PARAMETERS_FORM_ID = 38;
	public static final int SHOPPING_CART_FORM_ID = 39;
	public static final int SIMPLE_QUERY_INTERFACE_ID = 40;
	public static final int CONFIGURE_RESULT_VIEW_ID = 41;
	public static final int ADVANCE_QUERY_INTERFACE_ID = 42;
	public static final int QUERY_INTERFACE_ID = 43;
	
	//Misc
	public static final String SEPARATOR = " : ";
		
	//Status message key Constants
	public static final String STATUS_MESSAGE_KEY = "statusMessageKey";
	
	//Identifiers for JDBC DAO.
	public static final int QUERY_RESULT_TREE_JDBC_DAO = 1;
	
	//Activity Status values
	public static final String ACTIVITY_STATUS_APPROVE = "Approve";
	public static final String ACTIVITY_STATUS_REJECT = "Reject";
	public static final String ACTIVITY_STATUS_NEW = "New";
	public static final String ACTIVITY_STATUS_PENDING = "Pending";
	
	//Approve User status values.
	public static final String APPROVE_USER_APPROVE_STATUS = "Approve";
	public static final String APPROVE_USER_REJECT_STATUS = "Reject";
	public static final String APPROVE_USER_PENDING_STATUS = "Pending";
	
	//Approve User Constants
	public static final int ZERO = 0;
	public static final int START_PAGE = 1;
	public static final int NUMBER_RESULTS_PER_PAGE = 5;
	public static final String PAGE_NUMBER = "pageNum";
	public static final String RESULTS_PER_PAGE = "numResultsPerPage"; 
	public static final String TOTAL_RESULTS = "totalResults";
	public static final String PREVIOUS_PAGE = "prevpage";
	public static final String NEXT_PAGE = "nextPage";
	public static final String ORIGINAL_DOMAIN_OBJECT_LIST = "originalDomainObjectList";
	public static final String SHOW_DOMAIN_OBJECT_LIST = "showDomainObjectList";
	public static final String USER_DETAILS = "details";
	public static final String CURRENT_RECORD = "currentRecord";
	public static final String APPROVE_USER_EMAIL_SUBJECT = "Your membership status in caTISSUE Core.";
	
	//Edit Object Constants.
	public static final String TABLE_ALIAS_NAME = "aliasName"; 
	public static final String FIELD_TYPE_VARCHAR = "varchar";
	public static final String FIELD_TYPE_BIGINT = "bigint";
	public static final String FIELD_TYPE_DATE = "date";
	public static final String FIELD_TYPE_TIMESTAMP_TIME = "timestamptime";
	public static final String FIELD_TYPE_TIMESTAMP_DATE = "timestampdate";
	public static final String FIELD_TYPE_TEXT = "text";
	public static final String FIELD_TYPE_TINY_INT = "tinyint";
	
	public static final String CONDITION_VALUE_YES = "yes";
	public static final String TINY_INT_VALUE_ONE = "1";
	public static final String TINY_INT_VALUE_ZERO = "0";
	
	//Query Interface Results View Constants
	public static final String PAGEOF = "pageOf";
	public static final String QUERY = "query";
	public static final String PAGEOF_APPROVE_USER = "pageOfApproveUser";
	public static final String PAGEOF_SIGNUP = "pageOfSignUp";
	public static final String PAGEOF_USERADD = "pageOfUserAdd";
	public static final String PAGEOF_USER_ADMIN = "pageOfUserAdmin";
	public static final String PAGEOF_USER_PROFILE = "pageOfUserProfile";
	public static final String PAGEOF_CHANGE_PASSWORD = "pageOfChangePassword";
	
	//For Tree Applet
	public static final String PAGEOF_QUERY_RESULTS = "pageOfQueryResults";
	public static final String PAGEOF_STORAGE_LOCATION = "pageOfStorageLocation";
	public static final String PAGEOF_SPECIMEN = "pageOfSpecimen";
	public static final String PAGEOF_TISSUE_SITE = "pageOfTissueSite";
	
	public static final String CDE_NAME = "cdeName";

	//For Simple Query Interface and Edit.
	public static final String PAGEOF_SIMPLE_QUERY_INTERFACE = "pageOfSimpleQueryInterface";
	public static final String PAGEOF_EDIT_OBJECT = "pageOfEditObject";

	//Query results view temporary table name.
	public static final String QUERY_RESULTS_TABLE = "CATISSUE_QUERY_RESULTS";
	
	//Query results view temporary table columns.
	public static final String QUERY_RESULTS_PARTICIPANT_ID = "PARTICIPANT_ID";
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_ID = "COLLECTION_PROTOCOL_ID";
	public static final String QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID = "COLLECTION_PROTOCOL_EVENT_ID";
	public static final String QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID = "SPECIMEN_COLLECTION_GROUP_ID";
	public static final String QUERY_RESULTS_SPECIMEN_ID = "SPECIMEN_ID";
	public static final String QUERY_RESULTS_SPECIMEN_TYPE = "SPECIMEN_TYPE";
	
	// Assign Privilege Constants.
	public static final boolean PRIVILEGE_DEASSIGN = false;
	public static final String OPERATION_DISALLOW = "Disallow";
	
	//Constants for default column names to be shown for query result.
	public static final String[] DEFAULT_SPREADSHEET_COLUMNS = {
//	        	QUERY_RESULTS_PARTICIPANT_ID,QUERY_RESULTS_COLLECTION_PROTOCOL_ID,
//	        	QUERY_RESULTS_COLLECTION_PROTOCOL_EVENT_ID,QUERY_RESULTS_SPECIMEN_COLLECTION_GROUP_ID,
//	        	QUERY_RESULTS_SPECIMEN_ID,QUERY_RESULTS_SPECIMEN_TYPE
	        "IDENTIFIER","TYPE","ONE_DIMENSION_LABEL"
	};
	
	//Query results edit constants - MakeEditableAction.
	public static final String EDITABLE = "editable";
	
	//URL paths for Applet in TreeView.jsp
	public static final String QUERY_TREE_APPLET = "QueryTree.class";
	public static final String APPLET_CODEBASE = "Applet";
	public static final String TREE_APPLET_NAME = "treeApplet";
	
	//Shopping Cart
	public static final String SHOPPING_CART = "shoppingCart";
	
	public static final int SELECT_OPTION_VALUE = -1;
	
	
//	public static final String[] TISSUE_SITE_ARRAY = {
//	        SELECT_OPTION,"Sex","male","female",
//	        "Tissue","kidney","Left kidney","Right kidney"
//	};
	
	public static final String[] ATTRIBUTE_NAME_ARRAY = {
	        SELECT_OPTION
	};
	
	public static final String[] ATTRIBUTE_CONDITION_ARRAY = {
	        "=","<",">"
	};
	
	public static final String [] RECEIVEDMODEARRAY = {
	        SELECT_OPTION,
	        "by hand", "courier", "FedEX", "UPS"
	};
	public static final String [] GENDER_ARRAY = {
	        SELECT_OPTION,
	        "Male",
	        "Female"
	};
	
	public static final String [] GENOTYPE_ARRAY = {
	        SELECT_OPTION,
	        "XX",
	        "XY"
	};
	
	public static final String [] RACEARRAY = {
	        SELECT_OPTION,
	        "Asian",
	        "American"
	};
	
	public static final String [] PROTOCOLARRAY = {
	        SELECT_OPTION,
	        "aaaa",
	        "bbbb",
	        "cccc"
	};
	
	public static final String [] RECEIVEDBYARRAY = {
	        SELECT_OPTION,
	        "xxxx",
	        "yyyy",
	        "zzzz"
	};
	
	public static final String [] COLLECTEDBYARRAY = {
	        SELECT_OPTION,
	        "xxxx",
	        "yyyy",
	        "zzzz"
	};
	
	public static final String [] TIME_HOUR_ARRAY = {"1","2","3","4","5"};
	
	public static final String [] TIME_HOUR_AMPM_ARRAY = {"AM","PM"}; 
	
//	Constants required in CollectionProtocol.jsp Page
	public static final String COLLECTIONPROTOCOL_SEARCH_ACTION = "CollectionProtocolSearch.do";
	public static final String COLLECTIONPROTOCOL_ADD_ACTION = "CollectionProtocolAdd.do";
	public static final String COLLECTIONPROTOCOL_EDIT_ACTION = "CollectionProtocolEdit.do";

//	Constants required in DistributionProtocol.jsp Page
	public static final String DISTRIBUTIONPROTOCOL_SEARCH_ACTION = "DistributionProtocolSearch.do";
	public static final String DISTRIBUTIONPROTOCOL_ADD_ACTION = "DistributionProtocolAdd.do";
	public static final String DISTRIBUTIONPROTOCOL_EDIT_ACTION = "DistributionProtocolEdit.do";
	
	public static final String [] ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Closed",
			"Disabled"
	};

	public static final String [] SITE_ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Closed"
	};

	public static final String [] USER_ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Active",
	        "Closed"
	};
	public static final String [] COHORT_ACTIVITY_STATUS_VALUES = {
        SELECT_OPTION,
        "Active",
        "Disabled"
};
	public static final String [] APPROVE_USER_STATUS_VALUES = {
	        SELECT_OPTION,
	        APPROVE_USER_APPROVE_STATUS,
	        APPROVE_USER_REJECT_STATUS,
	        APPROVE_USER_PENDING_STATUS,
	};

	public static final String [] REPORTED_PROBLEM_ACTIVITY_STATUS_VALUES = {
	        SELECT_OPTION,
	        "Closed",
	        "Pending"
	};

	public static final String TISSUE = "Tissue";
	public static final String FLUID = "Fluid";
	public static final String CELL = "Cell";
	public static final String MOLECULAR = "Molecular";
	
	public static final String [] SPECIMEN_TYPE_VALUES = {
	        SELECT_OPTION,
	        TISSUE,
	        FLUID,
	        CELL,
			MOLECULAR
	};
	
	public static final String [] HOUR_ARRAY = {
			"00",
			"1",
	        "2",
	        "3",
	        "4",
	        "5",
	        "6",
	        "7",
	        "8",
	        "9",
	        "10",
	        "11",
	        "12",
	        "13",
	        "14",
	        "15",
	        "16",
	        "17",
	        "18",
	        "19",
	        "20",
	        "21",
	        "22",
	        "23"
	        
	};

	
	public static final String [] MINUTES_ARRAY = {
	 		"00",
			"01",
			"02",
			"03",
			"04",
			"05",
			"06",
			"07",
			"08",
			"09",
			"10",
			"11",
			"12",
			"13",
			"14",
			"15",
			"16",
			"17",
			"18",
			"19",
			"20",
			"21",
			"22",
			"23",
			"24",
			"25",
			"26",
			"27",
			"28",
			"29",
			"30",
			"31",
			"32",
			"33",
			"34",
			"35",
			"36",
			"37",
			"38",
			"39",
			"40",
			"41",
			"42",
			"43",
			"44",
			"45",
			"46",
			"47",
			"48",
			"49",
			"50",
			"51",
			"52",
			"53",
			"54",
			"55",
			"56",
			"57",
			"58",
			"59"
	};
	
	public static final String [] METHODARRAY = {
	        SELECT_OPTION,
			"LN2",
			"Dry Ice",
			"Iso pentane"
	};
	
	public static final String [] EMBEDDINGMEDIUMARRAY = {
				SELECT_OPTION,
				"Plastic",
				"Glass",
				
		};
	
	public static final String [] ITEMARRAY = {
			SELECT_OPTION,
			"Item1",
			"Item2",
	
	};
	
	public static final String UNIT_GM = "gm";
	public static final String UNIT_ML = "ml";
	public static final String UNIT_CC = "cell count";
	public static final String UNIT_MG = "�g";
	public static final String UNIT_CN = "count";
	
	public static final String [] PROCEDUREARRAY = {
	        SELECT_OPTION,
			"Procedure 1",
			"Procedure 2",
			"Procedure 3"
	};
	
	public static final String [] CONTAINERARRAY = {
	        SELECT_OPTION,
			"Container 1",
			"Container 2",
			"Container 3"
	};


	public static final String [] FIXATIONARRAY = {
	        SELECT_OPTION,
			"FIXATION 1",
			"FIXATION 2",
			"FIXATION 3"
	};
	
		
	public static final String CDE_NAME_TISSUE_SITE = "Tissue Site";
	public static final String CDE_NAME_CLINICAL_STATUS = "Clinical Status";
	public static final String CDE_NAME_GENDER = "Gender";
	public static final String CDE_NAME_GENOTYPE = "Genotype";
	public static final String CDE_NAME_SPECIMEN_CLASS = "Specimen";
	public static final String CDE_NAME_SPECIMEN_TYPE = "Specimen Type";
	public static final String CDE_NAME_TISSUE_SIDE = "Tissue Side";
	public static final String CDE_NAME_PATHOLOGICAL_STATUS = "Pathological Status";
	public static final String CDE_NAME_RECEIVED_QUALITY = "Received Quality";
	public static final String CDE_NAME_FIXATION_TYPE = "Fixation Type";
	public static final String CDE_NAME_COLLECTION_PROCEDURE = "Collection Procedure";
	public static final String CDE_NAME_CONTAINER = "Container";
	public static final String CDE_NAME_METHOD = "Method";
	public static final String CDE_NAME_EMBEDDING_MEDIUM = "Embedding Medium";
	public static final String CDE_NAME_BIOHAZARD = "Biohazard";
	public static final String CDE_NAME_ETHNICITY = "Ethnicity";
	public static final String CDE_NAME_RACE = "Race";
	public static final String CDE_NAME_CLINICAL_DIAGNOSIS = "Clinical Diagnosis";
	public static final String CDE_NAME_SITE_TYPE = "Site Type";
	public static final String CDE_NAME_COUNTRY_LIST = "Countries";
	public static final String CDE_NAME_STATE_LIST = "States";
	public static final String CDE_NAME_HISTOLOGICAL_QUALITY = "Histological Quality";
	//Constants for Advanced Search
	public static final String STRING_OPERATORS = "StringOperators";
	public static final String DATE_NUMERIC_OPERATORS = "DateNumericOperators";
	public static final String ENUMERATED_OPERATORS = "EnumeratedOperators";
	public static final String MULTI_ENUMERATED_OPERATORS = "MultiEnumeratedOperators";
	
	public static final String [] STORAGE_STATUS_ARRAY = {
	        SELECT_OPTION,
			"CHECK IN",
			"CHECK OUT"
	};

	public static final String [] CLINICALDIAGNOSISARRAY = {
	        SELECT_OPTION,
			"CLINICALDIAGNOSIS 1",
			"CLINICALDIAGNOSIS 2",
			"CLINICALDIAGNOSIS 3"
	};
	
	public static final String [] HISTOLOGICAL_QUALITY_ARRAY = {
	        SELECT_OPTION,
			"GOOD",
			"OK",
			"DAMAGED"
	};
	
	// constants for Data required in query
	public static final String ALIAS_NAME_TABLE_NAME_MAP="objectTableNames";
	public static final String SYSTEM_IDENTIFIER_COLUMN_NAME = "IDENTIFIER";
	public static final String NAME = "name";
	
	
	public static final String[] EVENT_PARAMETERS = {	Constants.SELECT_OPTION,
							"Cell Specimen Review",	"Check In Check Out", "Collection",
							"Disposal", "Embedded", "Fixed", "Fluid Specimen Review",
							"Frozen", "Molecular Specimen Review", "Procedure", "Received",
							"Spun", "Thaw", "Tissue Specimen Review", "Transfer" };
	
	public static final String[] EVENT_PARAMETERS_COLUMNS = { "Identifier",
											"Event Parameter", "User", "Date / Time", "PageOf"};
	
	public static final String [] SHOPPING_CART_COLUMNS = {"","Identifier", 
													"Type", "Subtype", "Tissue Site", "Tissue Side", "Pathological Status"}; 
	
	
	public static final String ACCESS_DENIED_ADMIN = "access_denied_admin";
	public static final String ACCESS_DENIED_BIOSPECIMEN = "access_denied_biospecimen";
	
	//Constants required in AssignPrivileges.jsp
	public static final String ASSIGN = "assignOperation";
	public static final String PRIVILEGES = "privileges";
	public static final String OBJECT_TYPES = "objectTypes";
	public static final String OBJECT_TYPE_VALUES = "objectTypeValues";
	public static final String RECORD_IDS = "recordIds";
	public static final String ATTRIBUTES = "attributes";
	public static final String GROUPS = "groups";
	public static final String USERS_FOR_USE_PRIVILEGE = "usersForUsePrivilege";
	public static final String USERS_FOR_READ_PRIVILEGE = "usersForReadPrivilege";
	public static final String ASSIGN_PRIVILEGES_ACTION = "AssignPrivileges.do";
	public static final int CONTAINER_IN_ANOTHER_CONTAINER = 2;
	    /**
     * @param systemIdentifier
     * @return
     */
    public static String getUserPGName(Long identifier)
    {
        if(identifier == null)
	    {
	        return "USER_";
	    }
	    return "USER_"+identifier;
    }

    /**
     * @param systemIdentifier
     * @return
     */
    public static String getUserGroupName(Long identifier)
    {
        if(identifier == null)
	    {
	        return "USER_";
	    }
	    return "USER_"+identifier;
    }
	
	//Mandar 25-Apr-06 : bug 1414 : Tissue units as per type
	// tissue types with unit= count
	public static final String FROZEN_TISSUE_BLOCK = "Frozen Tissue Block";	// PREVIOUS FROZEN BLOCK 
	public static final String FROZEN_TISSUE_SLIDE = "Frozen Tissue Slide";	// SLIDE	 
	public static final String FIXED_TISSUE_BLOCK = "Fixed Tissue Block";	// PARAFFIN BLOCK	 
	public static final String NOT_SPECIFIED = "Not Specified";
	// tissue types with unit= g
	public static final String FRESH_TISSUE = "Fresh Tissue";			 
	public static final String FROZEN_TISSUE = "Frozen Tissue";			 
	public static final String FIXED_TISSUE = "Fixed Tissue";			 
	public static final String FIXED_TISSUE_SLIDE = "Fixed Tissue Slide";
	//tissue types with unit= cc
	public static final String MICRODISSECTED = "Microdissected"; 
	
//	 constants required for Distribution Report
	public static final String CONFIGURATION_TABLES = "configurationTables";
	public static final String[] DISTRIBUTION_TABLE_ALIAS = {"CollectionProtReg","Participant","Specimen",
															 "SpecimenCollectionGroup","DistributedItem"};
	public static final String TABLE_COLUMN_DATA_MAP = "tableColumnDataMap";
	public static final String CONFIGURE_RESULT_VIEW_ACTION = "ConfigureResultView.do";
	public static final String TABLE_NAMES_LIST = "tableNamesList";
	public static final String COLUMN_NAMES_LIST = "columnNamesList";
	public static final String DISTRIBUTION_ID = "distributionId";
	public static final String CONFIGURE_DISTRIBUTION_ACTION = "ConfigureDistribution.do";
	public static final String DISTRIBUTION_REPORT_ACTION = "DistributionReport.do";
	public static final String DISTRIBUTION_REPORT_SAVE_ACTION="DistributionReportSave.do";
	public static final String[] SELECTED_COLUMNS = {"Specimen.IDENTIFIER.Identifier : Specimen",
													"Specimen.TYPE.Type : Specimen",
													"SpecimenCharacteristics.TISSUE_SITE.Tissue Site : Specimen",
													"SpecimenCharacteristics.TISSUE_SIDE.Tissue Side : Specimen",
													"SpecimenCharacteristics.PATHOLOGICAL_STATUS.Pathological Status : Specimen",
													"DistributedItem.QUANTITY.Quantity : Distribution"};
	public static final String SPECIMEN_ID_LIST = "specimenIdList";
	public static final String DISTRIBUTION_ACTION = "Distribution.do?pageOf=pageOfDistribution";
	public static final String DISTRIBUTION_REPORT_NAME = "Distribution Report.csv";
	public static final String DISTRIBUTION_REPORT_FORM="distributionReportForm";
	public static final String DISTRIBUTED_ITEMS_DATA = "distributedItemsData";
	public static final String DISTRIBUTED_ITEM = "DistributedItem";
	//constants for Simple Query Interface Configuration
	public static final String CONFIGURE_SIMPLE_QUERY_ACTION = "ConfigureSimpleQuery.do";
	public static final String CONFIGURE_SIMPLE_QUERY_VALIDATE_ACTION = "ConfigureSimpleQueryValidate.do";
	public static final String CONFIGURE_SIMPLE_SEARCH_ACTION = "ConfigureSimpleSearch.do";
	public static final String SIMPLE_SEARCH_ACTION = "SimpleSearch.do";
	public static final String SIMPLE_SEARCH_AFTER_CONFIGURE_ACTION = "SimpleSearchAfterConfigure.do";
	public static final String PAGEOF_DISTRIBUTION = "pageOfDistribution";
	public static final String RESULT_VIEW_VECTOR = "resultViewVector";
	public static final String SIMPLE_QUERY_MAP = "simpleQueryMap";
	public static final String SIMPLE_QUERY_ALIAS_NAME = "simpleQueryAliasName";
	public static final String SIMPLE_QUERY_COUNTER = "simpleQueryCount";
	public static final String IDENTIFIER_FIELD_INDEX = "identifierFieldIndex";
	
	public static final String UNDEFINED = "Undefined";
	public static final String UNKNOWN = "Unknown";
	public static final String SEARCH_RESULT = "SearchResult.csv";
	
//	Mandar : LightYellow and Green colors for CollectionProtocol SpecimenRequirements. Bug id: 587 
//	public static final String ODD_COLOR = "#FEFB85";
//	public static final String EVEN_COLOR = "#A7FEAB";
//	Light and dark shades of GREY.
	public static final String ODD_COLOR = "#E5E5E5";
	public static final String EVEN_COLOR = "#F7F7F7"; 
		
	
	
	// TO FORWARD THE REQUEST ON SUBMIT IF STATUS IS DISABLED
	public static final String BIO_SPECIMEN = "/ManageBioSpecimen.do";
	public static final String ADMINISTRATIVE = "/ManageAdministrativeData.do";
	public static final String PARENT_SPECIMEN_ID = "parentSpecimenId";
	public static final String COLLECTION_REGISTRATION_ID = "collectionProtocolId";
	
	public static final String FORWARDLIST = "forwardToList";
	public static final String [][] SPECIMEN_FORWARD_TO_LIST = {
			{"Normal Submit",				"success"},
			{"Derive New From This Specimen",			"createNew"},
			{"Add Events",				"eventParameters"},
			{"Add More To Same Collection Group",	"sameCollectionGroup"}
	};

	public static final String [][] SPECIMEN_COLLECTION_GROUP_FORWARD_TO_LIST = {
			{"Normal Submit",				"success"},
			{"Add New Specimen",			"createNewSpecimen"}
	};
	
	public static final String [][] PROTOCOL_REGISTRATION_FORWARD_TO_LIST = {
			{"Normal Submit",							"success"},
			{"New Specimen Collection Group",			"createSpecimenCollectionGroup"}
	};

	public static final String [][] PARTICIPANT_FORWARD_TO_LIST = {
			{"Normal Submit",					"success"},
			{"New Participant Registration",	"createParticipantRegistration"}
	};
	
	//Constants Required for Advance Search
	//Tree related
	//public static final String PARTICIPANT ='Participant';
	public static final String MENU_COLLECTION_PROTOCOL ="Collection Protocol";
	public static final String MENU_SPECIMEN_COLLECTION_GROUP ="Specimen Collection Group";
	public static final String MENU_DISTRIBUTION_PROTOCOL = "Distribution Protocol";
	
	public static final String COLLECTION_PROTOCOL ="CollectionProtocol";
	public static final String SPECIMEN_COLLECTION_GROUP ="SpecimenCollectionGroup";
	public static final String DISTRIBUTION = "Distribution";
	public static final String DISTRIBUTION_PROTOCOL = "DistributionProtocol";
	public static final String CP = "CP";
	public static final String SCG = "SCG";
	public static final String D = "D";
	public static final String DP = "DP";
	public static final String C = "C";
	public static final String S = "S";
	public static final String P = "P";
	public static final String ADVANCED_CONDITION_NODES_MAP = "advancedConditionNodesMap";
	public static final String TREE_VECTOR = "treeVector";
	public static final String ADVANCED_CONDITIONS_ROOT = "advancedCondtionsRoot";
	public static final String ADVANCED_CONDITIONS_QUERY_VIEW = "advancedCondtionsQueryView";
	public static final String ADVANCED_SEARCH_ACTION = "AdvanceSearch.do";
	public static final String ADVANCED_SEARCH_RESULTS_ACTION = "AdvanceSearchResults.do";
	public static final String CONFIGURE_ADVANCED_SEARCH_RESULTS_ACTION = "ConfigureAdvanceSearchResults.do";
	public static final String PAGEOF_ADVANCE_QUERY_INTERFACE = "pageOfAdvanceQueryInterface";
	public static final String ADVANCED_QUERY_ADD = "Add";
	public static final String ADVANCED_QUERY_EDIT = "Edit";
	public static final String ADVANCED_QUERY_DELETE = "Delete";
	public static final String ADVANCED_QUERY_OPERATOR = "Operator";
	public static final String ADVANCED_QUERY_OR = "OR";
	public static final String ADVANCED_QUERY_AND = "pAND";
	public static final String EVENT_CONDITIONS = "eventConditions";
	
	public static final String IDENTIFIER_COLUMN_ID_MAP = "identifierColumnIdsMap";
	public static final String COLUMN_ID_MAP = "columnIdsMap";
	public static final String PARENT_SPECIMEN_ID_COLUMN = "PARENT_SPECIMEN_ID";
	public static final String COLUMN = "Column";
	public static final String COLUMN_DISPLAY_NAMES = "columnDisplayNames";
	public static final String PAGEOF_PARTICIPANT_QUERY_EDIT= "pageOfParticipantQueryEdit";
	public static final String PAGEOF_COLLECTION_PROTOCOL_QUERY_EDIT= "pageOfCollectionProtocolQueryEdit";
	public static final String PAGEOF_SPECIMEN_COLLECTION_GROUP_QUERY_EDIT= "pageOfSpecimenCollectionGroupQueryEdit";
	public static final String PAGEOF_SPECIMEN_QUERY_EDIT= "pageOfSpecimenQueryEdit";
	public static final String PARTICIPANT_COLUMNS = "particpantColumns";
	public static final String COLLECTION_PROTOCOL_COLUMNS = "collectionProtocolColumns";
	public static final String SPECIMEN_COLLECTION_GROUP_COLUMNS = "SpecimenCollectionGroupColumns";
	public static final String SPECIMEN_COLUMNS = "SpecimenColumns";
	public static final String USER_ID_COLUMN = "USER_ID";
	
	// -- menu selection related
	public static final String MENU_SELECTED = "menuSelected";
	
	public static final String GENERIC_SECURITYMANAGER_ERROR = "The Security Violation error occured during a database operation. Please report this problem to the adminstrator";
	public static final String PASSWORD_CHANGE_IN_SESSION = "changepassword";
	
	public static final String BOOLEAN_YES = "Yes";
	public static final String BOOLEAN_NO = "No";
	
	public static final String PACKAGE_DOMAIN = "edu.ucdavis.caelmir.domain";
	
	//Constants for isAuditable and isSecureUpdate required for Dao methods in Bozlogic
	public static final boolean IS_AUDITABLE_TRUE = true;
	public static final boolean IS_SECURE_UPDATE_TRUE = true;
	public static final boolean HAS_OBJECT_LEVEL_PRIVILEGE_FALSE = false;
	
	//Constants for HTTP-API
	public static final String CONTENT_TYPE = "CONTENT-TYPE";
	
	// For StorageContainer isFull status
	public static final String IS_CONTAINER_FULL_LIST = "isContainerFullList";
	public static final String [] IS_CONTAINER_FULL_VALUES = {
	        SELECT_OPTION,
	        "True",
	        "False"
	};
	
	public static final String STORAGE_CONTAINER_DIM_ONE_LABEL = "oneDimLabel";
	public static final String STORAGE_CONTAINER_DIM_TWO_LABEL = "twoDimLabel";
	
	public static final String NULL = "NULL";
	
//    public static final String SPECIMEN_TYPE_TISSUE = "Tissue";
//    public static final String SPECIMEN_TYPE_FLUID = "Fluid";
//    public static final String SPECIMEN_TYPE_CELL = "Cell";
//    public static final String SPECIMEN_TYPE_MOL = "Molecular";
    public static final String SPECIMEN_TYPE_COUNT = "Count";
    public static final String SPECIMEN_TYPE_QUANTITY = "Quantity";
    public static final String SPECIMEN_TYPE_DETAILS = "Details";
    public static final String SPECIMEN_COUNT = "totalSpecimenCount";
    public static final String TOTAL = "Total";
    public static final String SPECIMENS = "Specimens";

	//User Roles
	public static final String TECHNICIAN = "Technician";
	public static final String SUPERVISOR = "Supervisor";
	public static final String SCIENTIST = "Scientist";
	
	// for Add New
	public static final String ADD_NEW_STORAGE_TYPE_ID ="addNewStorageTypeId";
	public static final String ADD_NEW_COLLECTION_PROTOCOL_ID ="addNewCollectionProtocolId";
	public static final String ADD_NEW_SITE_ID ="addNewSiteId";
	public static final String ADD_NEW_USER_ID ="addNewUserId";
	public static final String ADD_NEW_USER_TO ="addNewUserTo";
	public static final String SUBMITTED_FOR = "submittedFor";
	public static final String SUBMITTED_FOR_ADD_NEW = "AddNew";
	public static final String SUBMITTED_FOR_FORWARD_TO = "ForwardTo";
	public static final String SUBMITTED_FOR_DEFAULT = "Default";
	public static final String FORM_BEAN_STACK= "formBeanStack";
	public static final String ADD_NEW_FORWARD_TO ="addNewForwardTo";
	public static final String FORWARD_TO = "forwardTo";
	public static final String ADD_NEW_FOR = "addNewFor";
	
	public static final String CHILD_CONTAINER_TYPE = "childContainerType";
	public static final String UNUSED = "Unused";
	public static final String TYPE = "Type";
	
	//Mandar: 28-Apr-06 Bug 1129
	public static final String DUPLICATE_SPECIMEN="duplicateSpecimen";
	
	////////////////////////////////////////////////////////
	
	// shital
	
	public static final String EXP_USERS = "expusers"; 	
	public static final String PROTOCOL= "protocol";
	public static final String COHORT = "cohort";
	public static final String ALLUSERS = "allusers";
	public static final String GROUP_LIST = "groupList";
	
	///
	
	//Identifiers for various Form beans
	public static final int EXP_CREATE_FORM_ID = 50;
	public static final String OPERATION="operation";
	
	//Cohort
	public static final int COHORT_CREATE_FORM_ID=51;
	public static final String ANIMAL_OBJECT = "animalobject";
	public static final String ANIMAL_LIST ="animalList";
    
     public static final String ADD_STUDY_ACTION = "AddStudyAction.do";
        
     public static final int STUDY_FORM_ID = 101;
     public static final int CASE_FORM_ID = 102;
     public static final int TISSUE_FORM_ID = 103;
     public static final int USER_GROUP_FORM_ID = 104;
     public static final int MICROARRAY_FORM_ID = 105;
     public static final int PROTEOMICS_FORM_ID = 106;
     
	
	public static final String STUDYLIST = "studyList"; 
	public static final String SELECTED_ANIMAL_LIST= "selectedAnimalList";
	
	public static final String ID = "id";
    public static final String USER_LIST = "userList";
    public static final String ROLE_LIST = "roleList";
    
    public static final String ADMINISTRATOR_GROUP_ID = "1";
    
    //Constants for eav start
    public static final int ENTITY_FORM_ID = 301;
    public static final int ATTRIBUTE_FORM_ID = 302;
    public static final int ENTITY_INFORMATION_FORM_ID = 303;
    public static final int ENTITY_DATA_FORM_ID = 304;
    public static final int ENTITY_DATA_INFORMATION_FORM_ID = 305;
    public static final int ENTITY_SELECTION_FORM_ID = 306; 
    
    public static final String FORM_DEFINITION_FORM = "formDefinitionForm" ;
    public static final String CONTROLS_FORM = "controlsForm" ;
    public static final String CACHE_MAP = "cacheMap";
    public static final String ADD_CONTROLS_TO_FORM = "addControlsToForm";
    public static final String SHOW_BUILD_FORM_JSP = "showBuildFormJSP";
    public static final String BUILD_FORM = "buildForm";
    public static final String SHOW_CREATE_FORM_JSP = "showCreateFormView";
    public static final String CONTROL_SELECTED_ACTION = "controlSelectedAction";
    public static final String SYSTEM_EXCEPTION = "systemException";
    public static final String APPLICATION_EXCEPTION = "applicationException";
    public static final String CONTAINER_INTERFACE = "containerInterface";
    public static final String ERRORS_LIST = "errorsList";
    public static final String SHOW_DYNAMIC_EXTENSIONS_HOMEPAGE = "showDynamicExtensionsHomePage";
    
    public static final String CONTROL_OPERATION = "controlOperation";
    public static final String SELECTED_CONTROL_ID = "selectedControlId";
    public static final String USER_SELECTED_TOOL = "userSelectedTool";
    //Constants for eav end
	
	
}