package com.ncookhom.MyOrders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ncookhom.R;

import java.util.ArrayList;
import java.util.List;


public class CompleteFrag extends Fragment {

    TextView check_txtV ;
    RecyclerView complete_recycler ;
    CompleteAdapter completeAdapter ;
    RecyclerView.LayoutManager layoutManager ;
    List<OrdersModel> list = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getParcelableArrayList("complete_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        check_txtV = getActivity().findViewById(R.id.complete_check_txt);
        complete_recycler = getActivity().findViewById(R.id.frag_complete_recycler);
        layoutManager = new LinearLayoutManager(getActivity());

        if (list.size() > 0){
            check_txtV.setVisibility(View.GONE);
            complete_recycler.setVisibility(View.VISIBLE);
            complete_recycler.setLayoutManager(layoutManager);
            complete_recycler.setHasFixedSize(true);
            complete_recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            completeAdapter = new CompleteAdapter(getActivity(), list);
            complete_recycler.setAdapter(completeAdapter);
        } else {
            check_txtV.setVisibility(View.VISIBLE);
            complete_recycler.setVisibility(View.GONE);
        }
    }
}