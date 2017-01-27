package com.chaoxianzuapp.shawn.app.navigationdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.db.SqlLite;
import com.chaoxianzuapp.shawn.app.dialog.DialogBoard;
import com.chaoxianzuapp.shawn.app.dialog.DialogLogin;
import com.chaoxianzuapp.shawn.app.member.Join;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks{
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DrawerLayout mDrawerLayout;

    //private RecyclerView mDrawerList;
    private ListView mDrawerListView;
    //private Button mLoginButton;

    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    private LinearLayout mleft_login;
    private RelativeLayout mleft_set;
    private TextView mleft_login_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        /*left 로그인*/
        mleft_login_logout = (TextView)view.findViewById(R.id.mleft_login_logout);
        mleft_set = (RelativeLayout)view.findViewById(R.id.mleft_set);
        mleft_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu p = new PopupMenu(getActivity(), view);
                Menu menu = p.getMenu();
                getActivity().getMenuInflater().inflate(R.menu.user_settings, menu);

                MenuItem user_modify = menu.findItem(R.id.user_modify);
                user_modify.setTitle("회원 정보 수정");

                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.user_modify:

                                Intent toJoin = new Intent(getActivity(), Join.class);
                                startActivity(toJoin);

                                mDrawerLayout.closeDrawer(mFragmentContainerView);
                                break;
                        }
                        return false;
                    }
                });
                p.show();
            }
        });
        mleft_login = (LinearLayout)view.findViewById(R.id.mleft_login);
        mleft_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mleft_login_logout.getText().toString().equals("로그인")) {

                    DialogLogin.login(getActivity(),null);
                }else{

                    BasicInfo.userInfo.clear();
                    if (BasicInfo.sqlLite != null) {

                        /* 자동 로그인 상태 취소*/
                        if ("1".equals(BasicInfo.userSet.get("auto_login"))) {

                            StringBuilder SQL = new StringBuilder("UPDATE ");
                            SQL.append(SqlLite.DB_USER_LOGIN_INFO);
                            SQL.append(" set auto_login = '0', mb_id = '',mb_password = '' ");

                            BasicInfo.sqlLite.execSQL(SQL.toString());

                            BasicInfo.userSet.put("auto_login", "0");
                            BasicInfo.userSet.put("mb_id", "");
                            BasicInfo.userSet.put("mb_password", "");
                        }
                    }
                    Toast.makeText(getActivity(), "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
                }
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            }
        });

        mDrawerListView = (ListView)view.findViewById(R.id.drawer_listview);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        NavigationDrawerListViewAdapter listViewAdapter = new NavigationDrawerListViewAdapter(getActivity());//리스트 뷰 커스텀

        listViewAdapter.addItem(new NavigationItem(0 ,"커뮤니티" , R.mipmap.header_menu_icon01));
        //listViewAdapter.addItem(new NavigationItem(4, "회원가입", R.mipmap.header_menu_icon05));
        listViewAdapter.addItem(new NavigationItem(1 , "공지사항" , R.mipmap.header_menu_icon02));
        listViewAdapter.addItem(new NavigationItem(2 , "사이트 접속" , R.mipmap.header_icon_intro));
        //listViewAdapter.addItem(new NavigationItem(2, "1대1 문의", R.mipmap.header_menu_icon04));
        listViewAdapter.addItem(new NavigationItem(3, "종료", R.mipmap.header_menu_icon07));

        listViewAdapter.notifyDataSetChanged();//seq순으로 소팅

        mDrawerListView.setAdapter(listViewAdapter);

        /*mDrawerListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), "setOnItemSelectedListener" + position, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        onNavigationDrawerItemSelected(mCurrentSelectedPosition);//액티비티 키는 순간 기본 레이아웃 자동선택
        return view;
    }
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }
    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }
    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     * @param toolbar      The Toolbar of the activity.
     */
    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.myPrimaryDarkColor));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }
    private void selectItem(int position) {

        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
    }
    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
}
