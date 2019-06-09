package com.vic.wanandroid.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vic.wanandroid.R;
import com.vic.wanandroid.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends BaseFragment {
    View rootView;
    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     /*   if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_book, container, false);
        }*/
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

}
