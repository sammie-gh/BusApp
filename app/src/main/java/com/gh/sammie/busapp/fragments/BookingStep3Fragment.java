package com.gh.sammie.busapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.busapp.Common;
import com.gh.sammie.busapp.EventBus.DisplayTimeSlotEvent;
import com.gh.sammie.busapp.Interface.ITimeSlotLoadListener;
import com.gh.sammie.busapp.R;
import com.gh.sammie.busapp.SpacesItemDecoration;
import com.gh.sammie.busapp.adapter.MyTimeSlotAdapter;
import com.gh.sammie.busapp.model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import es.dmoral.toasty.Toasty;



public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {
    //variable
    private DocumentReference barberDoc;
    private ITimeSlotLoadListener iTimeSlotLoadListener;
    private SweetAlertDialog dialog;
    Unbinder unbinder;
    private LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;

    @BindView(R.id.txt_current_date)
    TextView currentDate;
//    @BindView(R.id.calenderView)
//    HorizontalCalendarView calendarView;
//    private SimpleDateFormat simpleDateFormat;



    //EVENT  bus start
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loadAllTimeSlotAvailable(DisplayTimeSlotEvent event) {

        // in booking ac. we have pass this event with isDisplay = true
        if (event.isDisplay()) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0); //add current date
            loadAvailableTimeSlotofBarber(Common.currentBus.getBarberId(),
                    Common.simpleDateFormat.format(date.getTime()));
        }

    }


    private void loadAvailableTimeSlotofBarber(String barberId, final String bookDate) {
        dialog.show();
        ///AllSalon/Ho/Branch/IW9io42puOugPB5do2rj/Barbers/H1fXpNpzFt0FH28mUpTr
        barberDoc = FirebaseFirestore.getInstance()
                .collection("AllStation")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentSalon.getSalonId())
                .collection("Bus")
                .document(Common.currentBus.getBarberId());

        //Get information of this barber
        barberDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists())//if barber avail
                    {
                        //Get information of booking '
                        //if not created return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllStation")
                                .document(Common.city)
                                .collection("Branch")
                                .document(Common.currentSalon.getSalonId())
                                .collection("Bus")
                                .document(Common.currentBus.getBarberId())
                                .collection(bookDate); // ate simpleformat with dd_MM_yyyy

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty())//if dont have any appointment
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();

                                    else {
                                        //if have appointm
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);


                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });

    }


    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance() {

        if (instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;

        Common.simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");// this is a key

        dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

//        Common.bookingDate = Calendar.getInstance();
//        Common.bookingDate.add(Calendar.DATE, 0);  //init current date


    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemview = inflater.inflate(R.layout.fragment_booking_step_three, container, false);

        unbinder = ButterKnife.bind(this, itemview);

        init(itemview);
        return itemview;
    }

    private void init(View itemview) {

        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        //calender
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 7); //   days left user can book

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemview, R.id.calenderView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.bookingDate.getTimeInMillis() != date.getTimeInMillis()) {
                    Common.bookingDate = date; // this code will not load again if you select new
                    loadAvailableTimeSlotofBarber(Common.currentBus.getBarberId(),
                            Common.simpleDateFormat.format(date.getTime()));
                }
            }
        });

        currentDate.setText(MessageFormat.format("Date is {0}", DateFormat.format("EEE, MMM d, yyyy", startDate).toString()));

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                String selectedDateStr = DateFormat.format("EEE, MMM d, yyyy", date).toString();
                currentDate.setText(MessageFormat.format("Date is {0}", selectedDateStr));

                if (Common.bookingDate.getTimeInMillis() != date.getTimeInMillis()) {
                    Common.bookingDate = date; // this code will not load again if you select new
                    loadAvailableTimeSlotofBarber(Common.currentBus.getBarberId(),
                            Common.simpleDateFormat.format(date.getTime()));
                }
            }
        });

    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(), timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toasty.error(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    @Override
    public void onTimeSlotLoadEmpty() {

        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();


    }
}
