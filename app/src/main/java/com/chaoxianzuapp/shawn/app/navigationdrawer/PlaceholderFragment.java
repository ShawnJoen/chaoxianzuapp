package com.chaoxianzuapp.shawn.app.navigationdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chaoxianzuapp.shawn.app.BasicInfo;
import com.chaoxianzuapp.shawn.app.MainActivity;
import com.chaoxianzuapp.shawn.app.R;
import com.chaoxianzuapp.shawn.app.board.BoardFragment;
import com.chaoxianzuapp.shawn.app.board.BoardListAdapter;
import com.chaoxianzuapp.shawn.app.requestdata.BoardList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by shawn on 2015-08-09.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private int sectionNumber;

    private BoardListAdapter boardListAdapter;

    public PlaceholderFragment() {
    }
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    private View rootView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        rootView = null;
        if(sectionNumber == 1 || sectionNumber == 2){

            BoardFragment boardFragment = new BoardFragment(getActivity() ,getActivity() ,inflater, container, "내용이 없습니다.");

            rootView =  boardFragment.Inflater(sectionNumber);

            this.boardListAdapter =  boardFragment.getBoardListAdapter();
        }

        adView = (AdView)rootView.findViewById(R.id.adBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        return rootView;
    }
    private AdView adView;
    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }
    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }
    @Override
    public void onStart() {

        if(sectionNumber == 1 || sectionNumber == 2){

            this.boardListAdapter.clear();

            BasicInfo.setInitPaging();

            if(sectionNumber == 1) {

                /*검색 페이지에서 지역 변경후 다시올때 재 셋팅용*/
                TextView bo_city_ = (TextView) rootView.findViewById(R.id.bo_city_);
                bo_city_.setText(BasicInfo.activeBoardCityItem.getLabel());
            }
            new BoardList(getActivity(),this.boardListAdapter).start();
        }
        super.onStart();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        ((MainActivity) activity).onSectionAttached(sectionNumber);
    }
}
