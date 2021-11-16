package poi_localizer.view;

/**
 *
 * @author Bartosz Krawczyk
 * @version 1.0
 */

public class Constants {
    
    private Constants(){}
    
    public final static int MAX_CONNECTION_TIME = 30;
    
    public class Request
    {
        private Request(){}
        
        public final static String ACTION = "action";
                        
        public class User
        {
            private User(){}
            
            public final static String LOGIN = "login";
            public final static String PASSWORD = "password";
            public final static String CONNECTION_HASH = "connection_hash";
            
            public class Action
            {
                private Action(){}
                                
                public final static int REGISTER = 0;
                public final static int LOGIN = 1;
                public final static int REMOVE = 2;
                public final static int LOGOUT = 3;
            }
        }
        
        public class Place
        {
            private Place(){}
                                   
            public class Action
            {
                private Action(){}
                
                public final static int SELECT = 14;
                public final static int ADD = 15;
                public final static int EDIT = 16;
                public final static int REMOVE = 17;
            }
            
            public final static String CRITERIA = "criteria";
            public final static String TYPE = "type";
            public final static String RANGE = "range";
            public final static String LONGITUDE = "lng";
            public final static String LATITUDE = "lat";
            public final static String LONGITUDE_2 = "lng_2";
            public final static String LATITUDE_2 = "lat_2";
            public final static String PLACE_ID = "place_id";
            public final static String NAME = "name";
            public final static String VICINITY = "vicinity";
            
            public class Criteria
            {
                private Criteria(){}
                
                public final static int NAME = 18;
                public final static int COORDINATES = 19;
                public final static int ADDRESS = 20;
                public final static int NAME_AND_VICINITY = 140;
                
                public class Name
                {
                    private Name(){}

                    public final static String EXACT_FIT = "exact_fit";
                }
            }
            
            public class Photo
            {
                private Photo(){}
                
                public final static String NAME = "name";
                public final static String PHOTO_ID = "photo_id";
                
                public class Action {
                
                    private Action(){}
                    
                    public class Select
                    {
                        private Select(){}
                    
                        public final static String MODE = "mode";
                        public final static String PLACE_ID = "place_id";
                        public final static int SINGLE = 401;
                        public final static String NAME = "name";
                    }
                    
                    public final static int SELECT = 501;
                    public final static int ADD = 502;
                    public final static int REMOVE = 503;
                }  
           
            }
            
            public class Type
            {
                private Type(){}
                
                public class Action
                {
                    private Action(){}
                    
                    public final static int SELECT_ALL = 138;
                }
            }
            
            public class Review
            {
                private Review(){}
                
                public class Action {
                    
                    private Action(){}
                    
                    public final static int SELECT = 104;
                    public final static int ADD = 105;
                    public final static int REMOVE = 106;
                }
                
                public final static String REVIEW_ID = "review_id";
                public final static String REVIEW_TIME = "review_time";
                public final static String TEXT = "text";
                public final static String AUTHOR_NAME = "author_name";
                public final static String AUTHOR_URL = "author_url";
                public final static String RATING = "rating";
                public final static String USER_ID = "user_id";
                public final static String PLACE_ID = "place_id";
                public final static String CRITERIA = "criteria";
                
                public class Criteria
                {
                    private Criteria(){}
                    
                    public final static int ALL_REVIEWS = 201;
                    public final static int AUTHOR_NAME = 202;
                    public final static int PLACE = 203;
                    public final static int PLACE_AND_AUTHOR_NAME = 204;
                    public final static int USER = 205;
                    public final static int PLACE_AND_USER = 206;
                    public final static int NO_CRITERIA_SPECIFIED = 207;
                    public final static int SINGLE = 208;
                                    
                }
            }
            
            public class Event
            {
                private Event(){}
                
                public class Action {
                
                    private Action(){}
                    
                    public final static int SELECT = 80;
                    public final static int ADD = 81;
                    public final static int REMOVE = 82;
                    
                }  
                
                public class Criteria
                {
                    private Criteria(){}
                    
                    public final static int ALL_EVENTS = 97;
                    public final static int NAME = 98;
                    public final static int START_DATE = 99; 
                
                }
                
                public final static String EVENT_ID = "event_id";
                public final static String NAME = "name";
                public final static String START_TIME = "start_time";
                public final static String SUMMARY = "summary";
                public final static String URL = "url";
                public final static String CRITERIA = "criteria";
                
            }
                                   
            public class Addition {
            
                private Addition(){}
            
                public final static String NAME = "name";
                public final static String VICINITY = "vicinity";
                public final static String FORMATTED_PHONE_NUMBER = "formatted_phone";
                public final static String FORMATTED_ADDRESS = "formatted_address";
                public final static String LAT = "lat";
                public final static String LNG = "lng";
                public final static String RATING = "rating";
                public final static String URL_CID = "url_cid";
                public final static String INTERNATIONAL_PHONE_NUMBER = "international_phone";
                public final static String WEBSITE = "website";
                public final static String USER_ID = "user_id";
                public final static String SCOPE = "scope";
                public final static String OPEN_NOW = "open_now";
                public final static String PRICE_LEVEL = "price_level";
                public final static String UTC_OFFSET = "utc_offset";
                public final static String TYPE_ID = "type_id";
                public final static String PLACE_ID = "place_id";
                
                public class PriceLevel
                {
                    private PriceLevel(){}
                    
                    public final static int FREE = 32;
                    public final static int INEXPENSIVE = 33;
                    public final static int MODERATE = 34;
                    public final static int EXPENSIVE = 35;
                    public final static int VERY_EXPENSIVE = 36;
                }
            }
            
            public class Address
            {
                private Address(){}
                
                public final static String COMPONENTS_NUMBER = "comp_number";
                public final static String COMPONENT = "component_";
                public final static String COMPONENT_VALUE = "component_value_";
            }
            
            public class Range
            {
                private Range(){}
                
                public final static String EXACT = "exact";
                public final static String RADIAL = "radial";
                public final static String RADIUS = "radius";
                public final static String AREA = "area";
            }
        }
    }
    
    public class Response
    {
        private Response(){}
        
        public final static int NULL_MESSAGE = -100;
        public final static int TEST = -1000;
        
        public final static int NULL_ACTION = 4;
        public final static int INPUT_DATA_ERROR = 5;
        public final static int NOT_LOGGED = 6;
        
        public class User
        {
            private User(){}
            
            public final static int LOGIN_EMPTY = 7;
            public final static int PASSWORD_EMPTY = 8;
            public final static int LOGGED_IN = 9;
            public final static int LOGIN_OCCUPIED = 10;
            public final static int MAIL_OCCUPIED = 11;
            public final static int USER_ADDED = 12;
            public final static int ERROR_ADDING_USER = 13;
            public final static int LOGGED_OUT = -1;
            public final static String CONNECTION_HASH = "connection_hash";
        }        
        
        public class Place
        {
            private Place(){}
            
            public final static int NO_PLACE_TYPE = 21;
            public final static int NO_NAME_CRITERION = 22; 
            public final static int DATA_ON_PLACE = 23;
            public final static int DATA_ON_PLACE_APPROX = 24;
            public final static int DATA_ON_PLACE_RADIAL = 26;
            public final static int NO_RANGE_CRITERION = 25;
            public final static int NO_ADDRESS_COMPONENTS = 27;
            public final static int DATA_ON_PLACE_ADDRESS = 28;
            public final static int NO_PLACE_FOUND = 29;
            public final static int NO_PLACE_SPECIFIED = 49;
            public final static int INCORRECT_PLACE_NUMBER = 50;
            public final static int USER_IS_NOT_PLACE_AUTHOR = 56;
            public final static int PLACE_REMOVED = 57;
            public final static int PLACE_NOT_REMOVED = 58;
            public final static int DATA_ON_PLACE_AREA = 139;
            public final static int NO_VICINITY_CRITERION = 140;
                                   
            public class Addition
            {
                private Addition(){}
                
                public final static int INCORRECT_PHONE_NUMBER = 37;
                public final static int INCORRECT_WEBSITE = 38;
                public final static int INCORRECT_ADDRESS = 39;
                public final static int INCORRECT_LAT = 40;
                public final static int INCORRECT_LNG = 41;
                public final static int INCORRECT_INTERNATIONAL_PHONE_NUMBER = 42;
                public final static int INCORRECT_URL_CID = 43;
                public final static int INCORRECT_PRICE_LEVEL = 44;
                public final static int INCORRECT_UTC_OFFSET = 45;
                public final static int PLACE_NOT_ADDED = 46;
                public final static int PLACE_ADDED = 47;
                public final static int PLACE_DUPLICATE = 48;
                public final static int NAME_TOO_SHORT = 51;
                public final static int VICINITY_NAME_TOO_SHORT = 52;
                public final static int INCORRECT_TYPE_ID = 53;
                public final static int PLACE_EDITION_COMPLETED = 54;
                public final static int PLACE_EDITION_FAILED = 55;
                                
            }
            
            public class Photo
            {
                private Photo(){}
                
                public final static int INCORRECT_NAME = 301;
                public final static int INCORRECT_FORMAT = 302;
                public final static int INCORRECT_DIMENSIONS = 303;
                public final static int PLACE_DIR_NOT_AVAILABLE = 304;
                public final static int FILE_ALREADY_EXISTS = 305;
                public final static int UPLOAD_SUCCEEDED = 306;
                public final static int DESTINATION_NOT_AVAILABLE = 307;
                public final static int UPLOAD_FAILED = 308;
                public final static int RECORD_NOT_ADDED = 309;
                public final static int PHOTO_NOT_FOUND = 310;
                public final static int USER_NOT_AUTHOR = 311;
                public final static int PLACE_REMOVED = 312;
                public final static int PLACE_NOT_REMOVED = 313;
                public final static int DATA_ON_PHOTO = 314;
                public final static int CANNOT_SEND_IMAGE_DATA = 315;
                public final static int SENDING_IMAGE_DATA = 316;
                public final static int PHOTOS_NOT_FOUND = 317;
                public final static int DATA_ON_MULTIPLE_PHOTOS = 318;
                
            }
            
            public class Type
            {
                private Type(){}
                
                public final static int DATA_ON_MULTIPLE_TYPES = 136;
                public final static int NO_TYPE_FOUND = 137;            
            }
            
            
           
            public class Review
            {
                private Review(){}
                
                public static final int NO_REVIEW_TIME_CRITERION = 107;
                public static final int INCORRECT_REVIEW_TIME = 108;
                public static final int NO_TEXT_CRITERION = 109;
                public static final int INCORRECT_TEXT = 110;
                public static final int NO_AUTHOR_NAME_CRITERION = 111;
                public static final int INCORRECT_AUTHOR_NAME = 112;
                public static final int NO_AUTHOR_URL_CRITERION = 113;
                public static final int INCORRECT_AUTHOR_URL = 114;
                public static final int NO_RATING_CRITERION = 115;
                public static final int INCORRECT_RATING = 116;
                public static final int REVIEW_ADDED = 117;
                public static final int REVIEW_NOT_ADDED = 118;
                public static final int TEXT_TOO_SHORT = 119;
                public static final int TEXT_TOO_LONG = 120;
                public static final int NO_USER_ID = 121;
                public static final int USER_NOT_FOUND = 122;
                public static final int INCORRECT_USER_ID = 123;
                public static final int NO_PLACE_ID = 124;
                public static final int PLACE_NOT_FOUND = 125;
                public static final int INCORRECT_PLACE_ID = 126;
                public static final int REVIEW_DUPLICATED = 127;
                public static final int DATA_ON_REVIEW = 128;
                public static final int DATA_ON_MULTIPLE_REVIEWS = 129;
                public static final int NO_REVIEWS_FOUND = 130;
                public static final int NO_CRITERIA_SPECIFIED = 134;
                            
            }
           
            
            public class Event
            {
                private Event(){}
                
                public static final int NO_NAME_CRITERION = 83;
                public static final int NAME_TOO_LONG = 84;
                public static final int NO_START_TIME_CRITERION = 85;
                public static final int INCORRECT_START_TIME = 86;
                public static final int NO_SUMMARY_CRITERION = 87;
                public static final int SUMMARY_TO_LONG = 88;
                public static final int NO_URL_CRITERION = 89;
                public static final int INCORRECT_URL = 90;
                public static final int EVENT_ADDED = 94;
                public static final int EVENT_NOT_ADDED = 95;
                public static final int EVENT_DUPLICATED = 96;
                public final static int NO_CRITERIA_SPECIFIED = 100;    
                public final static int DATA_ON_EVENT = 101;
                public final static int DATA_ON_MULTIPLE_EVENTS = 102;
                public final static int NO_EVENTS_FOUND = 103;
                
            }                     
            
                        
        }
    }
}
