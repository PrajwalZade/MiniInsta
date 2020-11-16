package com.prajwal.miniinsta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.prajwal.miniinsta.Fragments.HomeFragment;
import com.prajwal.miniinsta.Fragments.ProfileFragment;
import com.prajwal.miniinsta.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class EditPfofileActivity extends AppCompatActivity {
    private ImageView close;
    private CircleImageView imageProfile;
    private TextView save, changePhoto;
    private MaterialEditText fullName, userName, bio;
    private FirebaseUser firebaseUser;
    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pfofile);
        close = findViewById(R.id.close);
        imageProfile = findViewById(R.id.image_profile);
        save = findViewById(R.id.save);
        changePhoto = findViewById(R.id.change_photo);
        fullName = findViewById(R.id.full_name);
        userName = findViewById(R.id.username);
        bio = findViewById(R.id.bio);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("Uploads");

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                fullName.setText(user.getName());
                userName.setText(user.getUserName());
                bio.setText(user.getBio());
                Picasso.get().load(user.getImageurl()).into(imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                Intent intent = new  Intent(EditPfofileActivity.this, HomeFragment.class);
                startActivity(intent);
                Toast.makeText(EditPfofileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditPfofileActivity.this);
            }
        });
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditPfofileActivity.this);
            }
        });

    }

    private void updateProfile() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("fullname", fullName.getText().toString());
        map.put("username", userName.getText().toString());
        map.put("bio", bio.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).updateChildren(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            mImageUri = result.getUri();
            uploadImage();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(mImageUri != null) {
            final StorageReference fibref = storageRef.child(System.currentTimeMillis() + ".jpg");
            uploadTask = fibref.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fibref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("imageurl").setValue(url);
                        pd.dismiss();
                    } else {
                        Toast.makeText(EditPfofileActivity.this, " Upload failed try again", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }  else {
            Toast.makeText(this, "No image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}
