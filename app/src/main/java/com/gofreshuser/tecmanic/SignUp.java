package com.gofreshuser.tecmanic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUp extends AppCompatActivity {

    Button backbutton,next;

    EditText first_name,last_name,email_add,create_pass,mobile;

    ProgressDialog progressDialog;


    //-------------------------------------------------
    SignInButton signInButton;
    LoginButton loginButton;
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;

//
//    FirebaseAuth auth;
//    DatabaseReference reference;

    //-------------------------------------------------

    String first_names="",last_names="",email="",id="",imgurl="",phone="";
    public static String TAG="Signup";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SignUp.this);
        setContentView(R.layout.activity_sign_up);

        backbutton=findViewById(R.id.back_button);

        next=findViewById(R.id.next);

        first_name=findViewById(R.id.first_name);

        last_name=findViewById(R.id.last_name);

        email_add=findViewById(R.id.email_add);

        create_pass=findViewById(R.id.create_pass);

        mobile=findViewById(R.id.mobile_num);

        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Please Wait");



        //-----------------------------------------------------------------------
        signInButton=findViewById(R.id.gsignin);
        loginButton=findViewById(R.id.loginfb);


//        auth=FirebaseAuth.getInstance();
//        reference= FirebaseDatabase.getInstance().getReference();


        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Google");


        callbackManager=CallbackManager.Factory.create();


        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(SignUp.this, loginResult+"", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(SignUp.this, "abc", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {

                email_add.setText(error+"");
            }
        });





        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sigin();
            }
        });
        //-----------------------------------------------------------------------
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void attemptRegister() {


        String getphone = mobile.getText().toString();
        String getname = first_name.getText().toString();
        String getlastname = last_name.getText().toString();
        String name = getname + " " + getlastname;
        String getpassword = create_pass.getText().toString();
        String getemail = email_add.getText().toString();

        boolean cancel = false;

        View focusView = null;

        if (TextUtils.isEmpty(getname)) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(getlastname)) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(getpassword)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            focusView = create_pass;
            cancel = true;
        }
        else if (TextUtils.isEmpty(getphone)) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            focusView = mobile;
            cancel = true;
        }

//
               else if (TextUtils.isEmpty(getemail)) {

                    Toast.makeText(this, "Enter Emailid", Toast.LENGTH_SHORT).show();

                    focusView = email_add;

                    cancel = true;
                }

//
           else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                if (ConnectivityReceiver.isConnected()) {
                    progressDialog.show();

                    makeRegisterRequest(name, getphone, getemail, getpassword);
                }
            }
        }




//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 6;
//    }
//
//    private boolean isPhoneValid(String phoneno) {
//        //TODO: Replace this with your own logic
//        return phoneno.length() > 9;
//    }

    private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {

        final AlertDialog loading=new ProgressDialog(SignUp.this);
        loading.setMessage("Loading...");
        loading.show();

        Toast.makeText(this, "Registering", Toast.LENGTH_SHORT).show();
        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_SIGN_UP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loading.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        progressDialog.dismiss();

                        String msg = response.getString("message");
                        Toast.makeText(SignUp.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SignUp.this, ShowLogin.class);
                        startActivity(i);
                        finish();

                    } else {
                        progressDialog.dismiss();


                        String error = response.getString("error");
                        Toast.makeText(SignUp.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(SignUp.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

//----------------------------------------------------------------------
    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){

            }else{
                loaduserprofile(currentAccessToken);
            }
        }
    };
    public  void loaduserprofile(AccessToken accessToken){
        GraphRequest graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    first_names=object.getString("first_name");
                    last_names=object.getString("last_name");
                    email=object.getString("email");
                    id=object.getString("id");
//                     phone=object.getString("mobile_phone");
                    imgurl="https://graph.facebook.com/"+id+"/picture?type=normal";


                    makeRegisterRequest(first_names+last_names,"",email,Baseurl.fixpassword);


//                    makeRegisterRequest(first_names+last_names,"",email,Baseurl.fixpassword);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
//-----------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

//-----------------------GOOGLE-------------------------------------
        if(requestCode==0){
//            Toast.makeText(this, data+"", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSigninResult(task);
        }
    }

    //---------------------------------------------------------------------------------------
    private void Sigin() {
        Intent signinIntent=googleSignInClient.getSignInIntent();
        startActivityForResult(signinIntent,0);
    }

    private void handleSigninResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account=task.getResult(ApiException.class);




            GoogleSignInAccount account1=GoogleSignIn.getLastSignedInAccount(SignUp.this);
            if(account1 !=null){
                String personName=account1.getDisplayName();
//                String personGivenName=account.getGivenName();
//                String personFamilyName=account.getFamilyName();
                email=account1.getEmail();

                String personId=account1.getId();
                Uri personphoto=account1.getPhotoUrl();



                makeRegisterRequest(personName,"",email,Baseurl.fixpassword);

            }else {
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setConfirmButtonBackgroundColor(Color.RED)
                .setTitleText("Something Went Wrong")
                .show();
            }

        }catch (Exception e){
            email_add.setText(e+"");
        }
    }


    //--------------------------------------------------------------------------------------



//    public  void  createfirebaseaccount(String emails, String password, final String fullname){
//        auth.createUserWithEmailAndPassword(emails,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if(task.isSuccessful()){
//
//                    String userid=auth.getCurrentUser().getUid();
//                    reference.child("Users").child(userid).setValue("");
//
//
//
//
//                    makeRegisterRequest(fullname,"",email,Baseurl.fixpassword);
//
////                    startActivity(new Intent(SignUp.this,Login.class));
//
//                    Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(SignUp.this, task.getException()+"", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }
}
