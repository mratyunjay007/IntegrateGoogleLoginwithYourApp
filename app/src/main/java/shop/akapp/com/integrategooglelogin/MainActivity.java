package shop.akapp.com.integrategooglelogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , GoogleApiClient.OnConnectionFailedListener {

    TextView tvname,tvEmail;
    Button btnLogout;
    SignInButton btnSignIn;
    ImageView ivPic;
    int req_code=9001;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null)
        {
            String name=account.getDisplayName();
            String email=account.getEmail();
            if(account.getPhotoUrl()!=null) {
                String img_url = account.getPhotoUrl().toString();
                Glide.with(this).load(img_url).into(ivPic);
            }
                tvEmail.setText(email);
            tvname.setText(name);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        tvname=findViewById(R.id.tvName);
        tvEmail=findViewById(R.id.tvEmail);
        
        btnLogout=findViewById(R.id.btnLogout);
        btnSignIn=findViewById(R.id.btnSignin);
        ivPic=findViewById(R.id.ivPic);
        
        btnSignIn.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnSignin:
                signIN();
                break;
            case R.id.btnLogout:
                Logout();
                break;
        }
        
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
        
    }
    
    public void signIN()
    {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,req_code);
    }
    
    public void Logout()
    {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signedout", Toast.LENGTH_SHORT).show();
                        tvname.setText("");
                        tvEmail.setText("");
                        ivPic.setImageResource(R.drawable.rabit);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==req_code)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleresult(task);
        }
    }

    private void handleresult(Task<GoogleSignInAccount>  completedTask) {
        try
        {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String name=account.getDisplayName();
            String email=account.getEmail();
            if(account.getPhotoUrl()!=null) {
                String img_url = account.getPhotoUrl().toString();
                Glide.with(this).load(img_url).into(ivPic);
            };
            
            tvEmail.setText(email);
            tvname.setText(name);

        }
        catch (ApiException e){
            Log.w("Failed", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
    
    
    }
}
