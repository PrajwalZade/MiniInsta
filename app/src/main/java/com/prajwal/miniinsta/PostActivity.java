package com.prajwal.miniinsta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private Uri imgUri;
    private ImageView close;
    private  ImageView imgAdded;
    private TextView post;
    SocialAutoCompleteTextView descp;
    private String imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        close = findViewById(R.id.close);
        imgAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        descp = findViewById(R.id.description);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();;
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        CropImage.activity().start(PostActivity.this);

    }

    private void upload() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(imgUri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            StorageTask uploadTask = filePath.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                   if(!task.isSuccessful()) {
                       throw task.getException();


                   }
                   return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imgUrl = downloadUri.toString();
                    DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Posts");
                    String postID = ref.push().getKey(); // get unique id
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postID", postID);
                    map.put("imgUrl", imgUrl);
                    map.put("description", descp.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid()); // give current user
                    ref.child(postID).setValue(map);

                    DatabaseReference hastagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTag = descp.getHashtags();
                    if(!hashTag.isEmpty()) {
                        for(String tag: hashTag) {
                            map.clear();

                            map.put("tag", tag.toLowerCase());
                            map.put("postID", postID);

                            hastagRef.child(tag.toLowerCase()).child(postID).setValue(map);
                        }
                    }
                    pd.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(PostActivity.this, "No image Was selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            imgAdded.setImageURI(imgUri);

        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ArrayAdapter<Hashtag> hashtagArrayAdapter = new HashtagArrayAdapter<>(getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    hashtagArrayAdapter.add(new Hashtag(snapshot.getKey(), (int) snapshot.getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        descp.setHashtagAdapter(hashtagArrayAdapter);
    }
}
