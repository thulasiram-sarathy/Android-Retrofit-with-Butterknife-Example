package cozycodes.project.retrofit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cozycodes.project.retrofit.interfaces.CozyApi;
import cozycodes.project.retrofit.requests.RegisterReq;
import cozycodes.project.retrofit.responses.RegisterResp;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity  {

//    private EditText editUsername, editPassword, editFirstname, editLastname;
//    private Button btnRegister, btnLogin;
    private ArrayList<RegisterReq> registerList;
    private ProgressDialog pDialog;

    @BindView(R.id.editUsername)
    EditText editUsername;

    @BindView(R.id.editPassword)
    EditText editPassword;

    @BindView(R.id.editFirstname)
    EditText editFirstname;

    @BindView(R.id.editLastname)
    EditText editLastname;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        /*editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editFirstname = (EditText) findViewById(R.id.editFirstname);
        editLastname = (EditText) findViewById(R.id.editLastname);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(this);
       btnLogin.setOnClickListener(this);*/

        registerList = new ArrayList<>();

    }

   /* @Override
    public void onClick(View view) {

       if(view == btnRegister){
           userRegistration();
       }
        else if(view == btnLogin){
           Intent startLogin = new Intent(this, LoginActivity.class);
           startActivity(startLogin);
       }
    }*/
   @OnClick({R.id.btnRegister, R.id.btnLogin})
   public void onClick(Button v) {
       if (v.getId() == R.id.btnRegister) {
           userRegistration();
       } else if (v.getId() == R.id.btnLogin) {
           Intent startLogin = new Intent(this, LoginActivity.class);
           startActivity(startLogin);
       }

   }

    private void userRegistration(){
//        openDialogBox();
        String uname = editUsername.getText().toString();
        String pass = editPassword.getText().toString();
        String fname =  editFirstname.getText().toString();
        String lname = editLastname.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.35/retrofit/").
                        addConverterFactory(GsonConverterFactory.create())
                .build();

        CozyApi apiService = retrofit.create(CozyApi.class);
        RegisterReq reg = new RegisterReq();

        reg.setUsername(uname);
        reg.setPassword(pass);
        reg.setFirstname(fname);
        reg.setLastname(lname);

        Call<RegisterResp> registerRespCall = apiService.setRegistrationDetails(reg.getUsername(), reg.getPassword(), reg.getFirstname(),reg.getLastname());

        registerRespCall.enqueue(new Callback<RegisterResp>() {
            @Override
            public void onResponse(Response<RegisterResp> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    registerList = response.body().getRegister();


                    for (int i = 0; i < registerList.size(); i++) {
                        String res_msg = registerList.get(i).getRes_msg();
                        String res_code =registerList.get(i).getRes_code();

                        Log.i("msg", "Response message: " + res_msg);
                        Toast.makeText(MainActivity.this, res_msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
