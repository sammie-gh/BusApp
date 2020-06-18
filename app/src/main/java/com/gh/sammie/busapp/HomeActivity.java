package com.gh.sammie.busapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gh.sammie.busapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    BottomSheetDialog bottomSheetDialog;
    CollectionReference userRef;
    SweetAlertDialog dialog;
    private FirebaseAuth mAuth;
    Fragment fragment = null;
    private Button btn_buy;
    private Button btn_update;
    TextView txt_userName;
    private Context context;
    TextView txt_phone;
    TextView txt_membership_id_number;
    TextView txt_emergency;
    ImageView img_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
// dialog
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Checking Database Please wait... ");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);


        mAuth = FirebaseAuth.getInstance();

        //hooks
        txt_membership_id_number = findViewById(R.id.txt_membership_id_number);
        txt_userName = findViewById(R.id.txt_user_name);
        img_user = findViewById(R.id.img_user);
        txt_phone = findViewById(R.id.txt_phone);
        btn_buy = findViewById(R.id.btn_buy);
        txt_emergency = findViewById(R.id.txt_emergency);
        btn_update = findViewById(R.id.btn_update);
        context = HomeActivity.this;

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, BookingActivity.class));
                finish();
            }
        });

        //init  data
        userRef = FirebaseFirestore.getInstance().collection("Users");

        if (getIntent() != null) {
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);

            if (isLogin) {
                if (!isFinishing() && !isDestroyed()) {
                    //copy from Barber Booking app
                    if (!dialog.isShowing())
                        dialog.show();
                }

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    DocumentReference currentUser = userRef.document(user.getUid());
                    Paper.init(HomeActivity.this);
                    Paper.book().write(Common.LOGGED_KEY, user.getUid()).toString(); //change for id login

                    btn_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showUpdateDialog(user.getUid(), true);
                        }
                    });

                    currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot userSnapShot = task.getResult();
                                if (userSnapShot != null) {
                                    if (!userSnapShot.exists()) {
                                        showUpdateDialog(user.getUid(), false);
                                        //                                                    bottomNavigationView.setEnabled(false);
                                    } else {
                                        //user already logged
                                        Common.currentUser = userSnapShot.toObject(User.class);
                                        setUserInformation();
                                        Log.d("currentUser", "onComplete: " + userSnapShot);
                                        //                                                    bottomNavigationView.setSelectedItemId(R.id.home_action);
                                        //                                            fragment = new HomeFragment();
                                        //                                            loafFragment(fragment);
                                    }
                                }

                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                            }
                        }
                    });

                }

            }
        }


    }

    private void showUpdateDialog(final String uid, Boolean pressed) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("One last stop 😁 !");
        if (pressed.equals(true)) {
            bottomSheetDialog.setCancelable(true);
        } else
            bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_info, null);

        Button btn_update = sheetView.findViewById(R.id.btn_update);
        final Calendar myCalendar = Calendar.getInstance();

        final TextInputEditText edt_name = sheetView.findViewById(R.id.edt_name);


        final TextInputEditText edt_next_kin = sheetView.findViewById(R.id.edt_next_kin);
        final TextInputEditText edt_phone = sheetView.findViewById(R.id.edt_phone);


        //Date picker dialog
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //update textView
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//                edt_date_birth.setText(sdf.format(myCalendar.getTime()));
//                edt_date_birth.setTextColor(getResources().getColor(R.color.gray_btn_bg_color));

//                txtAge.setText("Your age is " + getAge(year, monthOfYear, dayOfMonth));


            }

        };

        if (pressed.equals(false)) btn_update.setText("Save");
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                Random r = new Random();
                int randomNumber = r.nextInt();

                final User user = new User(Objects.requireNonNull(edt_name.getText()).toString().trim(),
                        Objects.requireNonNull(edt_phone.getText()).toString().trim(),
                        // must change to uid in database and user class and other places since token is given here
                        "", Objects.requireNonNull(edt_next_kin.getText()).toString().trim(), "null", "null", "null",
                        "GHS" + randomNumber
                );

                userRef.document(uid) //was previously uid as phone
                        .set(user)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                bottomSheetDialog.dismiss();
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toasty.success(HomeActivity.this, "Success Thank you Please restart the App  ", Toast.LENGTH_SHORT).show();
                                //load new
//                                fragment = new HomeFragment();
////                                loafFragment(fragment);

                            }
                        }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bottomSheetDialog.dismiss();
                        if (dialog.isShowing())
                            dialog.dismiss();

                        Common.currentUser = user;
//                        fragment = new HomeFragment();
//                        loafFragment(fragment);
//                        bottomNavigationView.setSelectedItemId(R.id.home_action);
                        Toasty.error(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void setUserInformation() {
        txt_userName.setText(Common.currentUser.getName()); //save to sharedprerence or save instace to prevent crash
        txt_phone.setText(MessageFormat.format("PHONE :{0}", Common.currentUser.getPhoneNumber()));
        txt_emergency.setText(MessageFormat.format("Emergency Contact :{0}", Common.currentUser.getNxtKin()));
        txt_membership_id_number.setText(MessageFormat.format("ID: {0}", Common.currentUser.getIdNumber()));

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("You are about to signOut !")
                        .setContentText("Click Ok to logout or cancel dismiss")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                mAuth.signOut();
                                finish();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}