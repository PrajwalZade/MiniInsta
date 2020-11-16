package com.prajwal.miniinsta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegActivity extends AppCompatActivity {
    private EditText userName;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView login_user;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        userName = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.regester);
        login_user = findViewById(R.id.login_user);
        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegActivity.this, LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUserName = userName.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if(TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(RegActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 6) {
                    Toast.makeText(RegActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUserName, txtName, txtEmail, txtPassword);
                }
            }
        });

    }

    private void registerUser(final String userName, final String name, final String email, String password) {
        pd.setMessage("Please Wait");
        pd.show();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Name", name);
                map.put("Email", email);
                map.put("UserName", userName);
                map.put("ID", auth.getCurrentUser().getUid());
                map.put("Bio", "");
                map.put("imageurl", "deafult");
                ref.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(RegActivity.this, "Update the profile" + "for better  experience", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegActivity.this , MainActivity.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
