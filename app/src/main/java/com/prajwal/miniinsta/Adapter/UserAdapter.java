package com.prajwal.miniinsta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prajwal.miniinsta.Model.User;
import com.prajwal.miniinsta.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
   private List<com.prajwal.miniinsta.Model.User> mUser;
    private boolean isfragment;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<com.prajwal.miniinsta.Model.User> mUser, boolean isfragment) {
        this.mContext = mContext;
        this.mUser = mUser;
        this.isfragment = isfragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUser.get(position);
        holder.follow.setVisibility(View.VISIBLE);
        holder.userName.setText(user.getUserName());
        holder.Name.setText(user.getName());
        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imgProfile);
        isFollowed(user.getID(), holder.follow);
        if(user.getID().equals(firebaseUser.getUid())) {
            holder.follow.setVisibility(View.GONE);
        }
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.follow.getText().toString().equals("Follow")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").child(user.getID()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getID()).child("Followers").child(firebaseUser.getUid()).setValue(true);

                    addNotification(user.getID());
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").child(user.getID()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getID()).child("Followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });








    }

    private void addNotification(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", id);
        hashMap.put("text", "Started following you");
        hashMap.put("postId", "");
        hashMap.put("isPost", false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(hashMap);

    }

    private void isFollowed(final String id, final Button follow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()) {
                    follow.setText("Following");
                }else {
                    follow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgProfile;
        public TextView userName;
        public TextView Name;
        public Button follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.image_profile);
            userName = itemView.findViewById(R.id.username);
            Name = itemView.findViewById(R.id.fullname);
            follow = itemView.findViewById(R.id.follow);
        }
    }
}
