package com.gh.sammie.busapp.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gh.sammie.busapp.Common;
import com.gh.sammie.busapp.EventBus.ConfirmBookingEvent;
import com.gh.sammie.busapp.MainActivity;
import com.gh.sammie.busapp.R;
import com.gh.sammie.busapp.model.BookingInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;


public class BookingStep4Fragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SweetAlertDialog dialog;
    private SweetAlertDialog successDialog;
    private SimpleDateFormat simpleDateFormat;
    Activity activity;
    @BindView(R.id.txt_booking_barber_text)
    TextView txt_booking_barber_text;
    Unbinder unbinder;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_salona_ddress)
    TextView txt_salon_address;
    @BindView(R.id.txt_salon_name)
    TextView txt_salon_name;
    @BindView(R.id.txt_salon_open_hours)
    TextView txt_salon_open_hours;
    @BindView(R.id.txt_salon_phone)
    TextView txt_salon_phone;
    @BindView(R.id.txt_salon_website)
    TextView txt_salon_website;


    @OnClick(R.id.btn_confirm)
    void confirmBooking() {
        confirmBookingMethod();
        //        requestPay();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity) {
            activity = (Activity) context;
        }

    }

//    private void requestPay() {
//        Intent intent = new Intent(getActivity(), PaymentActivity.class);
//        startActivity(intent);
//        getActivity().finish();
//
//
//        //        new AlertDialog.Builder(getActivity())
////                .setTitle("Payment Notice !")
////                .setMessage("Please to continue booking please make the following payment of ...GH₵")
////                .setCancelable(false)
////                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int i) {
////                        PayWithSlydepay.Pay(getActivity(), "Payment for room",
////                                3,
////                                "Payment made for booking " + Common.currentUser.getName() + " tel: " + Common.currentUser.getPhoneNumber(),
////                                Common.currentUser.getName(),
////                                "", "121", "", RequestCode.IMPORT);
////                        dialog.dismiss();
////
////                    }
////                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                dialog.dismiss();
////            }
////        }).show();
//    }


    private void confirmBookingMethod() {
        EventBus.getDefault().unregister(this);
        //create booking information
        //process Timestamp
        //Timestamp to filter all booking  within date is greater today
        //or only display all future booking

        dialog.show();
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-");
        //split ex :9:00 -10:00
        //get startime :get 9:00
//        String[] startTimeConvert = convertTime[0].split(":");
//        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); // we get 9
//        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); // we get 00

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        Calendar bookingDateWithhourHouse = Calendar.getInstance();
//        bookingDateWithhourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
//        bookingDateWithhourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
//        bookingDateWithhourHouse.set(Calendar.MINUTE, startMinInt);

//create timestamp object and apply to BookingInformation
//        Timestamp timestamp = new Timestamp(bookingDateWithhourHouse.getTime());
        final BookingInformation bookingInformation = new BookingInformation();
        bookingInformation.setCityBook(Common.city);
        bookingInformation.setTimestamp(null);
        bookingInformation.setDone(false);

        // always fals coz we will use this  field to filter display
        bookingInformation.setBarberId(Common.currentBus.getBarberId());
        bookingInformation.setBarberName(Common.currentBus.getName());
        bookingInformation.setCustomerName(Common.currentUser.getName());
        bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setSalonAddress(Common.currentSalon.getAddress());
        bookingInformation.setSalonId(Common.currentSalon.getSalonId());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));
        bookingInformation.setSalonName(Common.currentSalon.getName());
        bookingInformation.setTime(Common.convertTimeSlotToString(Common.currentTimeSlot));
        bookingInformation.setCustomer_id(Common.currentUser.getIdNumber());
        bookingInformation.setGender(Common.currentUser.getGender());
        if (user != null) {
            bookingInformation.setUid(user.getUid());
        }
        bookingInformation.setIsConfirm("Booking is not confirmed");

        //submit to babrber documment
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllStation")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentSalon.getSalonId())
                .collection("Bus")
                .document(Common.currentBus.getBarberId())
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        //write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //function to check if booking exist we preevent nw booking
                        addToUserBooking(bookingInformation);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(requireActivity(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToUserBooking(final BookingInformation bookingInformation) {
        //First create new collection
        final CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");

        //Check if exist document in this collection

        //Get current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp todayTimeStamp = new Timestamp(calendar.getTime());
        userBooking
                .whereGreaterThanOrEqualTo("timestamp", todayTimeStamp)
                .whereEqualTo("done", false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                            //setData
                            userBooking.document()
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            showSuccessDialog();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            Toasty.error(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            if (dialog.isShowing()) dialog.dismiss();

                            resetStaticData();
                            Objects.requireNonNull(requireActivity()).finish(); //close activity
                            Toasty.success(Objects.requireNonNull(getContext()), "Failed something went wrong!", Toast.LENGTH_SHORT).show();
                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    }
                });
    }

    private void showSuccessDialog() {
        successDialog = new SweetAlertDialog(requireActivity(), SweetAlertDialog.SUCCESS_TYPE);
        successDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        successDialog.setTitleText("Payment Successful");
        successDialog.setContentText("Payment Successful Thank you");
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.setCancelable(false);
        successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Intent mStartActivity = new Intent(requireContext(), MainActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(requireContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
            }
        })

                .show();
//        Toasty.success(Objects.requireNonNull(getContext()), "Payment Successful Thank you ", Toast.LENGTH_LONG).show();

    }


    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentSalon = null;
        Common.currentBus = null;
        Common.bookingDate.add(Calendar.DATE, 0); //s
        // current date added

    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().removeAllStickyEvents();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setDataBooking(ConfirmBookingEvent event) {
        if (event.isConfirm()) {
            setData();
        }
    }

    private void setData() {
        txt_booking_barber_text.setText(Common.currentBus.getName());
        txt_booking_time_text.setText(new StringBuilder("Seat #:" + Common.convertTimeSlotToString(Common.currentTimeSlot)) //txt_time is seats number
                .append(" on ")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_salon_address.setText("Station :" + Common.currentSalon.getAddress());
        txt_salon_website.setText(Common.currentSalon.getWebsite());
        txt_salon_name.setText(Common.currentSalon.getName());
        txt_salon_open_hours.setText(Common.currentSalon.getOpenHours());
        txt_salon_phone.setText("Price :" + Common.currentSalon.getPhone() + "GHS"); //use as price

    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {

        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply format for date display confirm
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four, container, false);

        unbinder = ButterKnife.bind(this, itemView);
        return itemView;

    }


}
