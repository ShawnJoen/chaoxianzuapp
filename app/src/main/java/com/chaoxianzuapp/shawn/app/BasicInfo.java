package com.chaoxianzuapp.shawn.app;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chaoxianzuapp.shawn.app.board.spinner.BoardCateItem;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardCityItem;
import com.chaoxianzuapp.shawn.app.board.spinner.BoardSortItem;
import com.chaoxianzuapp.shawn.app.db.SqlLite;
import com.chaoxianzuapp.shawn.app.requestdata.Login;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by shawn on 2015-07-15.
 */
public class BasicInfo {

    public static String language = "";

    /* 기본 URI*/
    public static final String BASE_URI = "http://qcl108.dothome.co.kr";
    public static final String URI_BASE = BASE_URI+ "/client/default.php";//기본 값 불러오기 링크
    public static final String URI_LOGIN = BASE_URI+ "/client/login.php";//로그인 링크
    public static final String URI_JOIN = BASE_URI+ "/client/join.php";//로그인 링크
    public static final String URI_BOARD_LIST = BASE_URI+ "/client/board/list.php";//게시판 LIST 링크
    public static final String URI_BOARD_VIEW = BASE_URI+ "/client/board/view.php";
    public static final String URI_BOARD_DEL = BASE_URI+ "/client/board/delete.php";
    public static final String URI_BOARD_GOOD = BASE_URI+ "/client/board/good.php";
    public static final String URI_BOARD_WRITE = BASE_URI+ "/client/board/write_update.php";//게시판 작성 링크
    public static final String URI_BOARD_COMMENT = BASE_URI+ "/client/board/view_comment.php";
    public static final String URI_BOARD_COMMENTDEL = BASE_URI+ "/client/board/delete_comment.php";
    public static final String URI_BOARD_COMMENTWRITE = BASE_URI+ "/client/board/write_comment_update.php";
    //spinner data
    public static List<BoardCateItem> bo_tables = new ArrayList<BoardCateItem>();//게시판 카테
    public static Set<BoardSortItem> bo_sorts = new TreeSet<BoardSortItem>();//게시판 소팅
    public static List<BoardCityItem> bo_city = new ArrayList<BoardCityItem>();//게시판 지역
    public static Map<Integer ,String> BO_CITY = new HashMap<Integer ,String>();
    public static BoardCateItem notice = null;//공지 사항
    //active
    public static BoardCateItem activeBoardCateItem = null;//선택된 게시판 카테
    public static BoardSortItem activeBoardSortItem = null;//선택된 게시판 소팅
    public static BoardCityItem activeBoardCityItem = null;//선택된 게시판 지역
    //deviceType
    public static final String DEVICE_TYPE = "M";
    public static String DEVICE_ID;
    public static String APP_VERSION;
    public static String HP;

    public static String APP_FILENAME = "";
    public static String APP_APKPATH = "";

    //게시판 변수
    public static int bg_flag = 0;/*좋아요 선택 여부*/
    public static int totalPage;//총 페이징 수
    public static boolean is_divPage;
    public static int pageNo = 1;
    public static int prevPageNo = 0;
    public static void setInitPaging(){
        is_divPage = false;
        totalPage = 0;
        prevPageNo = 0;
        pageNo = 1;
    }
    //밀도
    public static float density = 0;

    //login info
    public static Map<String ,String> userInfo = new HashMap<String ,String>();
    public static Map<String ,String> userSet = new HashMap<String ,String>();

    /* 외장 메모리 패스*/
    public static String ExternalPath = "/mnt/sdcard/";
    /* 외장 메모리 패스 체크 여부*/
    public static boolean ExternalChecked = false;
    //sqllite instance
    public static SqlLite sqlLite = null;

    //url
    //public static final String SOCKET_URL = "http://221.143.41.151:3000";
    //public static final String SOCKET_URL = "http://192.168.200.113:3000";

    //service intent
    //public static Intent SERVICE_SOCKET_INTENT = null;
    //Log.d key
    public static final String SERVICE_TAG = "서비스";
    public static final String NETWORK_LOG_TAG = "네트워크로그";
    public static final String BOARD_LOG_TAG = "게시판로그";
    public static final String NETWORK_IMG_TAG = "이미지다운로드";
    public static final String SQLLIST_TAG = "서큘라이트";
    public static final String GCM_TAG = "구글푸시";
    //status
    public static boolean STATE_NETWORK = true;
    public static boolean CLOSE_ALL_PROCESS = false;
    public static boolean WAITDATA_FLAG = true;

    //브로드 캐스트 액션 구분자
    //public static final String ACTION_SOCKET = "com.shawn.SOCKET.IO";

    //package name
    public static final String PACKAGE_MAIN = "com.chaoxianzuapp.shawn.app.MainActivity";
    public static final String PACKAGE_BOARD_SEARCH = "com.chaoxianzuapp.shawn.app.board.BoardSearch";
    public static final String PACKAGE_BOARD_VIEW = "com.chaoxianzuapp.shawn.app.board.BoardView";
    public static final String PACKAGE_BOARD_WRITE = "com.chaoxianzuapp.shawn.app.board.BoardWrite";
    public static final String PACKAGE_BOARD_COMMENT = "com.chaoxianzuapp.shawn.app.board.BoardComment";
    public static final String PACKAGE_MEMBER_TERMS = "com.chaoxianzuapp.shawn.app.member.Terms";
    public static final String PACKAGE_MEMBER_JOIN = "com.chaoxianzuapp.shawn.app.member.Join";
    //public static final String PACKAGE_CREAT_ROOM = "com.example.shawn.chattest.CastCreateRoom";
    //public static final String PACKAGE_IN_ROOM = "com.example.shawn.chattest.CastInRoom";
    public static String ACTIVITY_ENABLE = "com.chaoxianzuapp.shawn.app.Splash";

    /*액티비티 전송 코드*/
    public static final int BOARD_VIEW_ACTIVITY = 1001;
    public static final int BOARD_COMMENT_ACTIVITY = 1002;
    public static final int BOARD_WRITE_ACTIVITY = 1003;
    public static final int IMAGE_CAPTURE_ACTIVITY = 1004;
    public static final int IMAGE_ALBUM_ACTIVITY = 1005;

    public static void softInputMethod(Context context ,View view , int mode) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            switch(mode){
                case 1:
                        imm.showSoftInput(view, 0);
                    break;
                case 0:
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    break;
            }
        }
    }

    public static final String FORMAT_NUM = ",###";
    private static final String FORMAT_DATE = "yyyy. MM. dd HH:mm";
    private static final String FORMAT_YMDS = "yyyyMMddHHmmssSSS";

    public static String setDateText(int mode ,long t) {

        String f_;
        switch(mode){
            case 1:
                f_ = FORMAT_DATE;
                break;
            default:
                f_ = FORMAT_YMDS;
        }

        Date date;
        if(t == 0){
            date = new Date();
        }else {
            date = new Date(t);
        }
        SimpleDateFormat s = new SimpleDateFormat(f_);
        return s.format(date);
    }

    public static long mCurTime;
    public static long mLastTime;
    public static boolean setDoubleClick(int t){
        mLastTime = mCurTime;
        mCurTime = System.currentTimeMillis();
        if (mCurTime - mLastTime < t) {

            return true;
        }
        return false;
    }
    public static void setAutoLogin(Context context) {

        if("1".equals(BasicInfo.userSet.get("auto_login"))){

            new Login(context, BasicInfo.userSet.get("mb_id"), BasicInfo.userSet.get("mb_password")).start();
        }else{

            BasicInfo.WAITDATA_FLAG = false;//데이터 리십 끝
        }
    }
}
