package com.prajwal.miniinsta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.prajwal.miniinsta.CommentActivity;
import com.prajwal.miniinsta.Fragments.PostDetailsFragment;
import com.prajwal.miniinsta.Fragments.ProfileFragment;
import com.prajwal.miniinsta.Model.Post;
import com.prajwal.miniinsta.Model.User;
import com.prajwal.miniinsta.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImgUrl()).into(holder.postImage);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageurl().equals("default")) {
                    holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
                }
                holder.username.setText(user.getUserName());
                holder.author.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isLiked(post.getPostID(), holder.like);
        noOfLikes(post.getPostID(), holder.noOfLikes);
        getComments(post.getPostID(), holder.noOfComments);
        isSaved(post.getPostID(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostID()).child(firebaseUser.getUid()).setValue(true);

                    addNotification(post.getPostID(), post.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostID()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postID", post.getPostID());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postID", post.getPostID());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(post.getPostID()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(post.getPostID()).removeValue();


                }
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileID", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new ProfileFragment()).commit();
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileid", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new ProfileFragment()).commit();
            }
        });
        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileid", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new ProfileFragment()).commit();
            }
        });
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostID()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new PostDetailsFragment()).commit();

            }
        });

    }

    private void addNotification(String postID, String publisher) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", publisher);
        hashMap.put("text", "Liked your post");
        hashMap.put("postId", postID);
        hashMap.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(hashMap);

    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;

        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        SocialTextView description;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            author = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            description = itemView.findViewById(R.id.description);

        }
    }
    private void isSaved (final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    image.setImageResource(R.drawable.ic_save_black);
                    image.setTag("saved");
                } else {
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfLikes (String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments (String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}