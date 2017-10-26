package com.ruffian.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruffian.library.RTextView;

/**
 * 示例FragmentCorner
 * 备注:版本迭代升级示例 1.0.1+
 *
 * @author ZhongDaFeng
 */

public class FragmentVersion extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_version, null);
        //RTextView textView = (RTextView) mView.findViewById(R.id.id_typeface);
        //textView.setTypeface("fonts/huakangshaonv.ttf");
        return mView;
    }
}
