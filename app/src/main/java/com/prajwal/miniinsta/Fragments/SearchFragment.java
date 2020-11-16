package com.prajwal.miniinsta.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.prajwal.miniinsta.Adapter.TagAdapter;
import com.prajwal.miniinsta.Adapter.UserAdapter;
import com.prajwal.miniinsta.Model.User;
import com.prajwal.miniinsta.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<User> musers;
    private UserAdapter userAdapter;
    private SocialAutoCompleteTextView searchBar;
    private RecyclerView recyclerViewTags;
    private List<String> mHashTags;
    private List<String> mHashTagsCount;
    private TagAdapter tagAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBar = view.findViewById(R.id.searchbar);
        recyclerViewTags = view.findViewById(R.id.recyclerview1_tags);
        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTags.setAdapter(tagAdapter);

        mHashTags = new ArrayList<>();
        mHashTagsCount = new ArrayList<String>();
        tagAdapter = new TagAdapter(getContext(),mHashTags,mHashTagsCount);


        musers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), musers ,true);
        recyclerView.setAdapter(userAdapter);
        readUser();
        readTags();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        return view;
    }

    private void readTags() {
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHashTags.clear();
                mHashTagsCount.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    mHashTags.add(snapshot.getKey());
                    mHashTagsCount.add(snapshot.getChildrenCount()+"");
                }
                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(searchBar.getText().toString())) {
                    musers.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        musers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void searchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("UserName").startAt(s).endAt(s+ "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    musers.add(user);

                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }
    private void filter(String text) {
        List<String> mSearchTags = new ArrayList<>();
        List<String> mSearchCount = new ArrayList<>();
        for(String s: mHashTags) {
            if(s.toLowerCase().contains(text.toLowerCase())) {
                mSearchTags.add(s);
                mSearchCount.add(mHashTagsCount.get(mHashTags.indexOf(s)));

            }

        }
        tagAdapter.filter(mSearchTags, mSearchCount);

    }
}
