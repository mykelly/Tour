package com.tour.SQLite;

public class Constant {
	public static final String TOUR_DATA="tour_data";
	public static final int VERSION=4;
	
	public class DataTourTable {
		public final static String TABLE_NAME = "data_tour";
		public final static String ID = "id"; // auto increase integer 
		public final static String TOUR_ID = "tour_id"; // auto increase integer 
		public final static String TOUR_TITLE = "tour_title";
		public final static String TOUR_DAY = "tour_day";
		public final static String TOUR_NO = "tour_no";
		public final static String TOUR_LINEID = "tour_lineid";
		public final static String TOUR_TYPE = "tour_type";
		public final static String TOUR_LINE_CONTENT = "tour_line_content";
		public final static String TOUR_PTYPE = "tour_ptype";
		public final static String TOUR_PLACE = "tour_place";
		public final static String TOUR_FLAT = "tour_flat";
		public final static String TOUR_LEADER = "tour_leader";
		public final static String TOUR_LEADER_PHONE = "tour_leader_phone";
		public final static String TOUR_DRIVER = "tour_driver";
		public final static String TOUR_DRIVER_PHONE = "tour_driver_phone";
		public final static String TOUR_GUIDEID = "tour_guideid";
		public final static String TOUR_GUIDE = "tour_guide";
		public final static String TOUR_GUIDE_PHONE = "tour_guide_phone";
		public final static String TOUR_BOOKING_SEAT = "tour_booking_seat";
		public final static String TOUR_CT_COUNT_LIST = "tour_ct_count_list";
		public final static String TOUR_CT_COUNT = "tour_ct_count";
		public final static String TOUR_CT_MAX = "tour_ct_max";
		public final static String TOUR_DATE = "tour_date";
		public final static String TOUR_ZIP = "tour_zip";
		public final static String TOUR_UPDATA_TIME = "tour_update_time";
		public final static String TOUR_REMARK = "tour_remark";
		public final static String TOUR_CLOCK = "tour_clock";//数据格式如：早上起床,6:30|中午吃饭,12:30|晚餐,18:00
		
	}
 
	public final static String CREATE_DATA_TOUR_TABLE = "create table IF NOT EXISTS " + DataTourTable.TABLE_NAME + "(" +
			DataTourTable.ID + " integer primary key autoincrement," +
			DataTourTable.TOUR_ID + " integer," +
			DataTourTable.TOUR_TITLE + " varchar(80)," +
			DataTourTable.TOUR_DAY + " integer,"+
			DataTourTable.TOUR_NO + " varchar(10),"+
			DataTourTable.TOUR_LINEID + " integer,"+
			DataTourTable.TOUR_LINE_CONTENT + " text,"+
			DataTourTable.TOUR_TYPE + " varchar(10),"+
			DataTourTable.TOUR_PTYPE + " varchar(10),"+
			DataTourTable.TOUR_PLACE + " varchar(100),"+
			DataTourTable.TOUR_FLAT + " varchar(10),"+
			DataTourTable.TOUR_LEADER + " varchar(10),"+
			DataTourTable.TOUR_LEADER_PHONE + " varchar(20),"+
			DataTourTable.TOUR_DRIVER + " varchar(10),"+
			DataTourTable.TOUR_DRIVER_PHONE + " varchar(20),"+
			DataTourTable.TOUR_GUIDEID + " integer,"+
			DataTourTable.TOUR_GUIDE + " varchar(20),"+
			DataTourTable.TOUR_GUIDE_PHONE + " varchar(20),"+
			DataTourTable.TOUR_BOOKING_SEAT + " integer,"+
			DataTourTable.TOUR_CT_COUNT_LIST + " varchar(20),"+
			DataTourTable.TOUR_CT_COUNT + " integer,"+
			DataTourTable.TOUR_CT_MAX + " integer,"+
			DataTourTable.TOUR_DATE + " text,"+
			DataTourTable.TOUR_ZIP + " varchar(50),"+
			DataTourTable.TOUR_UPDATA_TIME + " datetime,"+
			DataTourTable.TOUR_REMARK + " varchar(50),"+
			DataTourTable.TOUR_CLOCK + " varchar(80)"+
			")";
	public class DataTourDateTable {
		public final static String TABLE_NAME = "data_tour_date";
		public final static String ID = "id"; // auto increase integer
		public final static String 	DATE_ID="date_id";                    //int(8)	
		public final static String DATE_TOURID="date_tourid";	          //int(8)团id	团关键id,所有的行程就这id关联
		public final static String DATE_LINEID="date_lineid";	          //int(8)	线id
		public final static String DATE_NO="date_no";	                  //tinyint(2)	序号,第几天
		public final static String DATE_DESTINATION="date_destination";   //varchar(20)	目的地
		public final static String DATE_TRANSPORT="date_transport";	      //tinyint(1)	交通工具	类型会转成中文给你
		public final static String DATE_CONTENT="date_content";          //	text	行程
		public final static String DATE_BREAKFAST="date_breakfast";	     //varchar(50)	早餐
		public final static String DATE_LUNCH="date_lunch";	            //varchar(50)	午餐
		public final static String DATE_DINNER="date_dinner";	       //varchar(50)	晚餐
		public final static String DATE_SUPPER="date_supper";	      //varchar(50)	夜宵
		public final static String DATE_FOOD="date_food";	         //varchar(200)	美食,用","分开	团关联的美食
		public final static String DATE_FOOD_ID="date_food_id";	    //varchar(50)	美食id,用","分开	团关联的美食id
		public final static String DATE_PLACE="date_place";	       //varchar(200)	景点	同美食
		public final static String DATE_PLACE_ID="date_place_id";	//varchar(50)	景点id	同餐厅
		public final static String DATE_HOTEL="date_hotel";	       //varchar(30)	酒店	暂只有一个
		public final static String DATE_HOTEL_ID="date_hotel_id";	//mediumint(5)	酒店id	暂只有一个
		public final static String DATE_MOVIE="date_movie";	      //varchar(200)	视频	同餐厅
		public final static String DATE_MOVIE_ID="date_movie_id";	//varchar(50)	视频id	同餐厅
		public final static String DATE_REMARK="date_remark";	   //varchar(50)	备注	同美食
		public final static String TOUR_DATE="date_tour_date";	   //varchar(50)	 日期
		public final static String TOUR_WEEK="date_tour_week";	   //varchar(50)	星期

	}
	public final static String CREATE_DATA_TOUR_DATA_TABLE = "create table IF NOT EXISTS " + DataTourDateTable.TABLE_NAME + "(" +
			DataTourDateTable.ID + " integer primary key autoincrement," +
			DataTourDateTable.DATE_ID + " integer," +
			DataTourDateTable.DATE_TOURID + " integer," +
			DataTourDateTable.DATE_LINEID + " integer,"+
			DataTourDateTable.DATE_NO + " TEXT,"+
			DataTourDateTable.DATE_DESTINATION + " TEXT,"+
			DataTourDateTable.DATE_TRANSPORT + " text,"+
			DataTourDateTable.DATE_CONTENT + " TEXT,"+
			DataTourDateTable.DATE_BREAKFAST + " TEXT,"+
			DataTourDateTable.DATE_LUNCH + " TEXT,"+
			DataTourDateTable.DATE_DINNER + " TEXT,"+
			DataTourDateTable.DATE_SUPPER + " TEXT,"+
			DataTourDateTable.DATE_FOOD + " TEXT,"+
			DataTourDateTable.DATE_FOOD_ID + " INTEGER,"+
			DataTourDateTable.DATE_PLACE + " TEXT,"+
			DataTourDateTable.DATE_PLACE_ID + " integer,"+
			DataTourDateTable.DATE_HOTEL + " TEXT,"+
			DataTourDateTable.DATE_HOTEL_ID + " integer,"+
			DataTourDateTable.DATE_MOVIE + " TEXT,"+
			DataTourDateTable.DATE_MOVIE_ID + " INTEGER,"+
			DataTourDateTable.DATE_REMARK + " text,"+
			DataTourDateTable.TOUR_DATE + " datetime,"+
			DataTourDateTable.TOUR_WEEK + " text"+
			")";
	public class DataCustomerTable {
		public final static String TABLE_NAME = "data_customer";
		public final static String ID = "id"; // auto increase integer
		public final static String CT_ID="CT_id";	                   //int(10) unsigned	客戶id
		public final static String CT_CHECK_CODE="ct_check_code";	//varchar(10)	
		public final static String CT_TITLE="ct_title";         	//varchar(50)	客戶名
		public final static String CT_TYPE="ct_type";	            //tinyint(1) unsigned	類型:0成人,1小童,2長者,3會員	会用中文给你
		public final static String CT_TYPE_REMARK="ct_type_remark"; //varchar(20)	类型说明，如baby
		public final static String CT_SEX="ct_sex";	              //enum('0','1')	0女,1男	同上
		public final static String CT_AGE="ct_age";	              //tinyint(2) unsigned	年齡
		public final static String CT_BIRTHDAY="ct_birthday";    //	date	生日
		public final static String CT_IDNO="ct_idno" ;	         //varchar(18)	身份證
		public final static String CT_PHOENE="ct_phone";	     //varchar(40)	電話
		public final static String CT_MAIL="ct_mail"; 	        //varchar(50)	郵件
		public final static String CT_ADDR ="ct_addr";	         //varchar(255)	地址
		public final static String CT_CREAT_TIME="ct_create_time";	//datetime	創建時間
		public final static String CT_CREATE_USER="ct_create_user";	//varchar(30)	創建用戶
		public final static String CT_CREATE_UID ="ct_create_uid";//smallint(5)	創建用戶id
		public final static String CT_TID="ct_tid" ;	             //int(10)	旅游团id	对应团的id
		public final static String CT_SEAT ="ct_seat";	             //tinyint(2)	座位号	关键的座位号
		public final static String CT_PLACE="ct_place"; 	       //enum('','0','1','2','3')	''不接送,0羅湖 1皇崗 2深圳灣 3福田	转为中文给你
		public final static String CT_REMARK="ct_remark";	      //varchar(255)	其它備註
		public final static String CT_TEAM="ct_team";	           //varchar(5)	組別
		public final static String CT_ABSENT="ct_absent";	           //varchar(5)	是否缺席
	}
	public final static String CREATE_DATA_CUSTOMER_TABLE = "create table IF NOT EXISTS " + DataCustomerTable.TABLE_NAME + "(" +
			DataCustomerTable.ID + " integer primary key autoincrement," +
			DataCustomerTable.CT_ID + " integer," +
			DataCustomerTable.CT_CHECK_CODE + " TEXT," +
			DataCustomerTable.CT_TITLE + " TEXT,"+
			DataCustomerTable.CT_TYPE + " INTEGER,"+
			DataCustomerTable.CT_TYPE_REMARK + " TEXT,"+
			DataCustomerTable.CT_SEX + " text,"+
			DataCustomerTable.CT_AGE + " INTEGER,"+
			DataCustomerTable.CT_BIRTHDAY + " INTEGER,"+
			DataCustomerTable.CT_IDNO + " TEXT,"+
			DataCustomerTable.CT_PHOENE + " TEXT,"+
			DataCustomerTable.CT_MAIL + " TEXT,"+
			DataCustomerTable.CT_ADDR + " TEXT,"+
			DataCustomerTable.CT_CREAT_TIME + " INTEGER,"+
			DataCustomerTable.CT_CREATE_USER + " TEXT,"+
			DataCustomerTable.CT_CREATE_UID + " integer,"+
			DataCustomerTable.CT_TID + " INTEGER,"+
			DataCustomerTable.CT_SEAT + " TEXT,"+
			DataCustomerTable.CT_PLACE + " INTEGER,"+
			DataCustomerTable.CT_REMARK + " TEXT,"+
			DataCustomerTable.CT_TEAM + " text,"+
			DataCustomerTable.CT_ABSENT + " text"+
			")";
	public class DataFoodTable {
		public final static String TABLE_NAME = "data_food";
		public final static String ID = "id"; // auto increase integer
		public final static String FOOD_ID="food_id" ;	     //mediumint(6)	id
		public final static String FOOD_TITLE="food_title";	 //varchar(50)	標題
		public final static String FOOD_TYPE="food_type" ;	//tinyint(1) unsigned	菜系	同理
		public final static String FOOD_AREA_PID ="food_area_pid";	//mediumint(6) unsigned	省id	地区会转化成中文字符,合在food_area里
		public final static String FOOD_AREA_CID="food_area_cid"; 	//mediumint(6) unsigned	城市id
		public final static String FOOD_AREA="food_area" ;	//varchar(50)	地区
		public final static String FOOD_CONTACT="food_contact"; 	//varchar(20)	联系人
		public final static String FOOD_PHONE ="food_phone";	//varchar(30)	联系电话
		public final static String FOOD_FAX ="food_fax"	;//varchar(20)	fax
		public final static String FOOD_CONTENT ="food_content";	//text	描述內容
		public final static String FOOD_PHOTO ="food_photo";	//text	图片,多个用","分开	到时会处理成只有文件名,方便你引用压缩包里的图片
		public final static String FOOD_PHOTO_DES="food_photo_dsc"; 	//text	图片描述,同上
		public final static String FOOD_CREATE_TIME ="food_create_time";	//datetime	
		public final static String FOOD_EDIT_INFO ="food_edit_info";	//varchar(40)	

	}
	public final static String CREATE_DATA_FOOD_TABLE = "create table IF NOT EXISTS " + DataFoodTable.TABLE_NAME + "(" +
			DataFoodTable.ID + " integer primary key autoincrement," +
			DataFoodTable.FOOD_ID + " integer," +
			DataFoodTable.FOOD_TITLE + " TEXT," +
			DataFoodTable.FOOD_TYPE + " TEXT,"+
			DataFoodTable.FOOD_AREA_PID + " INTEGER,"+
			DataFoodTable.FOOD_AREA_CID + " INTEGER,"+
			DataFoodTable.FOOD_AREA + " TEXT,"+
			DataFoodTable.FOOD_CONTACT + " TEXT,"+
			DataFoodTable.FOOD_PHONE + " TEXT,"+
			DataFoodTable.FOOD_FAX + " TEXT,"+
			DataFoodTable.FOOD_CONTENT + " TEXT,"+
			DataFoodTable.FOOD_PHOTO + " TEXT,"+
			DataFoodTable.FOOD_PHOTO_DES + " TEXT,"+
			DataFoodTable.FOOD_CREATE_TIME + " INTEGER,"+
			DataFoodTable.FOOD_EDIT_INFO + " TEXT"+
			")";
	public class DataHotelTable {
		public final static String TABLE_NAME = "data_hotel";
		public final static String ID = "id"; // auto increase integer
		public final static String HOTEL_ID ="hotel_id"	;//mediumint(6)	id
		public final static String HOTEL_TITLE="hotel_title" ;	//varchar(50)	標題
		public final static String HOTEL_AREA_PID="hotel_area_pid" ;	//mediumint(6) unsigned	省id	同理
		public final static String HOTEL_AREA_CID ="hotel_area_cid";	//mediumint(6) unsigned	城市id
		public final static String HOTEL_AREA ="hotel_area";	      //varchar(50)	地区
		public final static String HOTEL_LEVEL="hotel_level"; 	    //tinyint(2)	星级	同理
		public final static String HOTEL_CONTACT="hotel_contact" ;	//varchar(20)	联系人
		public final static String HOTEL_PHONE ="hotel_phone";	  //varchar(30)	联系电话
		public final static String HOTEL_FAX ="hotel_fax";	    //varchar(20)	fax
		public final static String HOTEL_CONTENT="hotel_content" ;		//text詳細內容
		public final static String HOTEL_PHOTO ="hotel_photo";		   //text图片,多个用","分开	到时会处理成只有文件名,方便你引用压缩包里的图片
		public final static String HOTEL_PHOTO_DES="hotel_photo_dsc" ;		//text图片描述,同上
		public final static String HOTEL_CREATE_TIME ="hotel_create_time";	//datetime	
		public final static String HOTEL_EDIT_INFO="hotel_edit_info";	//varchar(40)	
	}
	public final static String CREATE_DATA_HOTEL_TABLE = "create table IF NOT EXISTS " + DataHotelTable.TABLE_NAME + "(" +
			DataHotelTable.ID + " integer primary key autoincrement," +
			DataHotelTable.HOTEL_ID + " integer," +
			DataHotelTable.HOTEL_TITLE + " TEXT," +
			DataHotelTable.HOTEL_AREA_PID + " INTEGER,"+
			DataHotelTable.HOTEL_AREA_CID + " INTEGER,"+
			DataHotelTable.HOTEL_AREA + " TEXT,"+
			DataHotelTable.HOTEL_LEVEL + " TEXT,"+
			DataHotelTable.HOTEL_CONTACT + " TEXT,"+
			DataHotelTable.HOTEL_PHONE + " TEXT,"+
			DataHotelTable.HOTEL_FAX + " TEXT,"+
			DataHotelTable.HOTEL_CONTENT + " TEXT,"+
			DataHotelTable.HOTEL_PHOTO + " TEXT,"+
			DataHotelTable.HOTEL_PHOTO_DES + " TEXT,"+
			DataHotelTable.HOTEL_CREATE_TIME + " INTEGER,"+
			DataHotelTable.HOTEL_EDIT_INFO + " TEXT"+
			")";
	public class DataPlaceTable {
		public final static String TABLE_NAME = "data_place";
		public final static String ID = "id"; // auto increase integer
		public final static String PLACE_ID="place_id";	//mediumint(6)	
		public final static String PLACE_TITLE ="place_title";	//varchar(50)	標題,景點
		public final static String PLACE_AREA_PID="place_area_pid"; 	//mediumint(6) unsigned	省id	同理
		public final static String PLACE_AREA_CID ="place_area_cid";	//mediumint(6) unsigned	城市id
		public final static String PLACE_AREA ="place_area";	//varchar(50)	地区
		public final static String PLACE_CONTACT="place_contact" ;	//varchar(20)	联系人
		public final static String PLACE_PHONE ="place_phone"	;//varchar(30)	联系电话
		public final static String PLACE_FAX="place_fax" ;	      //varchar(20)	fax
		public final static String PLACE_CONTENT="place_content" ;	//text	說明內容
		public final static String PLACE_PHOTO="place_photo" ;	   //text	图片,多个用","分开	到时会处理成只有文件名,方便你引用压缩包里的图片
		public final static String PLACE_PHOTO_DSC ="place_photo_dsc";  //	text	图片描述,同上
		public final static String PLACE_CREATE_TIME="place_create_time" ;	//datetime	
		public final static String PLACE_EDIT_INFO ="place_edit_info";	//varchar(40)
	}
	public final static String CREATE_DATA_PLACE_TABLE = "create table IF NOT EXISTS " + DataPlaceTable.TABLE_NAME + "(" +
			DataPlaceTable.ID + " integer primary key autoincrement," +
			DataPlaceTable.PLACE_ID + " integer," +
			DataPlaceTable.PLACE_TITLE + " TEXT," +
			DataPlaceTable.PLACE_AREA_PID + " INTEGER,"+
			DataPlaceTable.PLACE_AREA_CID + " INTEGER,"+
			DataPlaceTable.PLACE_AREA + " TEXT,"+
			DataPlaceTable.PLACE_CONTACT + " TEXT,"+
			DataPlaceTable.PLACE_PHONE + " TEXT,"+
			DataPlaceTable.PLACE_FAX + " TEXT,"+
			DataPlaceTable.PLACE_CONTENT + " TEXT,"+
			DataPlaceTable.PLACE_PHOTO + " TEXT,"+
			DataPlaceTable.PLACE_PHOTO_DSC + " TEXT,"+
			DataPlaceTable.PLACE_CREATE_TIME + " INTEGER,"+
			DataPlaceTable.PLACE_EDIT_INFO + " TEXT"+
			")";
	public class MoviePhoneTable {
		public final static String TABLE_NAME = "data_movie";
		public final static String ID = "movie_id"; // auto increase integer
		public final static String MOVIE_TITLE = "movie_title"; // auto increase integer
		public final static String MOVIE_KEY="movie_key";	         //varchar(50)	关键词
		public final static String MOVIE_CONTACT="movie_contact";	         //varchar(20)联系人
		public final static String MOVIE_PHONE="movie_phone";	         //varchar(30)	联系电话
		public final static String MOVIE_CONTENT="movie_content";	     //text	說明內容
		public final static String MOVIE_URL ="movie_url";	              //varchar(50)	視頻url
		public final static String MOVIE_CREATE_TIME="movie_create_time"; 	//datetime	
		public final static String MOVIE_EDIT_INFO="movie_edit_info";	  //varchar(40)
	}
	public final static String CREATE_MOVIE_PHONE_TABLE = "create table IF NOT EXISTS " + MoviePhoneTable.TABLE_NAME + "(" +
			MoviePhoneTable.ID + " integer primary key autoincrement," +
			MoviePhoneTable.MOVIE_TITLE + " TEXT," +
			MoviePhoneTable.MOVIE_KEY + " TEXT," +
			MoviePhoneTable.MOVIE_CONTACT + " TEXT," +
			MoviePhoneTable.MOVIE_PHONE + " TEXT," +
			MoviePhoneTable.MOVIE_CONTENT + " TEXT," +
			MoviePhoneTable.MOVIE_URL + " TEXT,"+
			MoviePhoneTable.MOVIE_CREATE_TIME + " INTEGER,"+
			MoviePhoneTable.MOVIE_EDIT_INFO + " TEXT"+
			")";
}
