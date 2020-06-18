package com.gh.sammie.busapp.fragments;


//public class HomeFragment extends Fragment implements IBannerLoadListener, ILookBookLoadlistener, IBookingInfoLoadListener, IBookingInformationChangeListener, ICountItemCartListener {
//
//    CartDatabase cartDatabase;
//    @BindView(R.id.notification_badge)
//    NotificationBadge notificationBadge;
//    @BindView(R.id.layout_user_information)
//    LinearLayout layout_user_info;
//    @BindView(R.id.txt_user_name)
//    TextView txt_userName;
//    @BindView(R.id.banner_slider)
//    Slider banner_slidey;
//    @BindView(R.id.recycler_look_book)
//    RecyclerView recyclerView_look_book;
//    @BindView(R.id.card_booking_info)
//    CardView card_booking_info;
//
//    @BindView(R.id.txt_time)
//    TextView txt_time;
//    @BindView(R.id.txt_status)
//    TextView txt_status;
//    @BindView(R.id.txt_salon_address)
//    TextView txt_salon_address;
//    @BindView(R.id.txt_salon_barber)
//    TextView txt_salon_barber;
//    @BindView(R.id.txt_time_remain)
//    TextView txt_time_remain;
//    @BindView(R.id.lotiie_animation)
//    LottieAnimationView lotiie_animation;
//    @BindView(R.id.txt_book_info)
//    TextView txt_book_info;
//
//    @BindView(R.id.txt_phone)
//    TextView txt_phone;
//    @BindView(R.id.txt_membership_id_number)
//    TextView txt_membership_id_number;
//    @BindView(R.id.img_user)
//    ImageView img_user;
//
//    private SweetAlertDialog dialog;

//    //Firetsore
//    private CollectionReference bannerRef, lookBookRef;
//    //interface
//    private IBannerLoadListener iBannerLoadListener;
//    private ILookBookLoadlistener iLookBookLoadlistener;
//    private IBookingInfoLoadListener iBookingInfoLoadListener;
//
//    private IBookingInformationChangeListener iBookingInformationChangeListener;
//    private FirebaseAuth mAuth;
//    private FirebaseUser user;
//    private Unbinder unbinder;
//    private CollectionReference userRef;
//    private BottomSheetDialog bottomSheetDialog;
//
//    private ListenerRegistration userBookingListener = null;
//    private com.google.firebase.firestore.EventListener<QuerySnapshot> userBookingEvent = null;
//
//    public HomeFragment() {
//        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
//        lookBookRef = FirebaseFirestore.getInstance().collection("Lookbook");
//
//
//    }
//
//    @OnClick(R.id.btn_delete_booking)
//    void deleteBooking() {
//        deleteBookingFromBarber(false);
//    }
//
//    @OnClick(R.id.btn_change_booking)
//    void changeBooking() {
//        changeBookingFromUser();
//
//    }
//
//    private void changeBookingFromUser() {
//        //show dialog
//        androidx.appcompat.app.AlertDialog.Builder confirmDialog = new androidx.appcompat.app.AlertDialog
//                .Builder(getActivity())
//                .setCancelable(false)
//                .setTitle("Hey!")
//                .setMessage("Do you really want to change booking information ? \n Because we will delete our old booking information\nJust confirm !")
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteBookingFromBarber(true);
//
//                    }
//                });
//
//        confirmDialog.show();
//
//
//    }
//
//    private void deleteBookingFromBarber(final boolean isChange) {
//
//        /* to deleet we need to delete from barber collection
//         * userbooking colletion
//         * final event calandar*/
//        //we need to load Common.currentBookg coz we need some data from booking infrmation
//
//        if (Common.currentBooking != null) {
//            if (!dialog.isShowing())
//                dialog.setTitle("Deleting");
//            dialog.show();
//
//            //AllSalon/Ho/Branch/IW9io42puOugPB5do2rj/Barbers/H1fXpNpzFt0FH28mUpTr/29_03_2020
//            //AllSalon/Ho/Branch/IW9io42puOugPB5do2rj/Barbers/H1fXpNpzFt0FH28mUpTr/21_05_2019
//            DocumentReference barberBookingInfo = FirebaseFirestore.getInstance()
//                    .collection("AllSalon")
//                    .document(Common.currentBooking.getCityBook())
//                    .collection("Branch")
//                    .document(Common.currentBooking.getSalonId())
//                    .collection("Barbers")
//                    .document(Common.currentBooking.getBarberId())
//                    .collection(Common.convertTimeStampToStringKey(Common.currentBooking.getTimestamp()))
//                    .document(Common.currentBooking.getSlot().toString());
//
//            //when we have document just delete it
//            barberBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//
//                    //delete data freom user
//                    deleteBookingFromUser(isChange);
//                }
//            });
//
//        } else {
//            Toasty.warning(getContext(), "Current Booking must not be null", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    private void deleteBookingFromUser(final boolean isChange) {
//        //get alll info from user object
//        if (!TextUtils.isEmpty(Common.currentBookingId)) {
//            DocumentReference userBookingInfo = FirebaseFirestore.getInstance()
//                    .collection("Users")
////                    .document(mAuth.getUid())
//                    .document(Common.currentUser.getPhoneNumber())
//                    .collection("Booking")
//                    .document(Common.currentBookingId);
//
//            //delete
//            userBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toasty.warning(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//
//                    //after we delete from user databse we delete from calander
//                    Paper.init(getActivity());
//                    if (Paper.book().read(Common.EVENT_URI_CACHE) != null) {
//                        String eventString = Paper.book().read(Common.EVENT_URI_CACHE).toString();
//                        Uri eventUri = null;
//                        if (eventString != null && !TextUtils.isEmpty(eventString))
//                            eventUri = Uri.parse(eventString);
//
//                        if (eventUri != null)
//                            getActivity().getContentResolver().delete(eventUri, null, null);
//
//                    }
//                    Toasty.success(getActivity(), "Success delete booking", Toast.LENGTH_SHORT).show();
//
//                    //Refresh
//                    loadUserBooking();
//
//                    //check if ischange -> call from change button we will fire interface
//                    if (isChange)
//                        iBookingInformationChangeListener.onBookingInformationChange();
//                    dialog.dismiss();
//
//                }
//            });
//        } else {
//            dialog.dismiss();
//            Toasty.warning(getContext(), "Booking information Id ust not be empty", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @OnClick(R.id.card_ciew_booking)
//    void booking() {
//        startActivity(new Intent(getActivity(), BookingActivity.class));
//    }
//
//    @OnClick(R.id.card_View_history)
//    void openHistoryActivity() {
//        startActivity(new Intent(getActivity(), HistoryActivity.class));
//    }
//
//    @OnClick(R.id.card_view_profile)
//    void openProfileUpdate() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        //init
//        userRef = FirebaseFirestore.getInstance().collection("Users");
//
//        if (user != null) {
//            DocumentReference currentUser = userRef.document(user.getUid());
//            currentUser.get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot userSnapShot = task.getResult();
//                                if (userSnapShot.exists()) {
//                                    if (dialog.isShowing()) {
//                                        dialog.dismiss();
//                                    }
//                                    showUpdateDialog(user.getUid());
////                                                    bottomNavigationView.setEnabled(false);
//                                }
//
//                            }
//                        }
//                    });
//
//        }
//
//    }
//
//    private void showUpdateDialog(final String uid) {
//        bottomSheetDialog = new BottomSheetDialog(getContext());
//        bottomSheetDialog.setCancelable(true);
//        bottomSheetDialog.setTitle("Kindly update your information");
//        bottomSheetDialog.setCanceledOnTouchOutside(true);
//        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_info, null);
//
//        Button btn_update = sheetView.findViewById(R.id.btn_update);
//        final Calendar myCalendar = Calendar.getInstance();
//
//        final TextInputEditText edt_name = sheetView.findViewById(R.id.edt_name);
//
//        final TextInputEditText edt_next_kin = sheetView.findViewById(R.id.edt_next_kin);
//        final TextInputEditText edt_phone = sheetView.findViewById(R.id.edt_phone);
// ;
//
//        //get previous client information
//        edt_name.setText(Common.currentUser.getName());
//        edt_next_kin.setText(Common.currentUser.getNxtKin());
//        edt_phone.setText(Common.currentUser.getPhoneNumber());
//
//
//        //Date picker dialog
//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                //update textView
//                String myFormat = "dd/MM/yy"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//
//
//
//            }
//
//        };
//
//
//
//        btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                }
////                Random r = new Random();
////                int randomNumber = r.nextInt();
//
//                final User user = new User(edt_name.getText().toString().trim(),
//                        edt_phone.getText().toString().trim(),// must change to uid in database and user class and other places since token is given here
//                        edt_next_kin.getText().toString().trim(),
//                        null,null,null,null,
//                        Common.currentUser.getIdNumber()
//                );
//
//
//                userRef.document(uid) //was previously uid as phone
//                        .set(user)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                bottomSheetDialog.dismiss();
//                                if (dialog.isShowing()) {
//                                    dialog.dismiss();
//                                }
//                                Toasty.success(getActivity(), "Profile updated Thank you ", Toast.LENGTH_SHORT).show();
//                                //load new
////                                fragment = new HomeFragment();
////                                loafFragment(fragment);
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        bottomSheetDialog.dismiss();
//                        if (dialog.isShowing())
//                            dialog.dismiss();
//
//                        Common.currentUser = user;
////                        fragment = new HomeFragment();
////                        loafFragment(fragment);
////                        bottomNavigationView.setSelectedItemId(R.id.home_action);
//                        Toasty.error(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
//
//        bottomSheetDialog.setContentView(sheetView);
//        bottomSheetDialog.show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        loadUserBooking();
//        countCartItem();
//    }
//
//    private void loadUserBooking() {
//        CollectionReference userBooking = FirebaseFirestore.getInstance()
//                .collection("Users")
//                .document(Common.currentUser.getPhoneNumber())
//                .collection("Booking");
//        //Get current date
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, 0);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//
//        Timestamp todayTimeStamp = new Timestamp(calendar.getTime());
//
//        //select booking information from firebase with dome = false and timestamp greater today
//        userBooking
//                .whereGreaterThanOrEqualTo("timestamp", todayTimeStamp)
//                .whereEqualTo("done", false) //when staff confirms done value is set to true and does not display lookbook to user
//                .limit(1)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            if (!task.getResult().isEmpty()) {
//
//                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
//                                    iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation, queryDocumentSnapshot.getId());
//                                    break;  //Exit loop as as
//                                }
//                            } else
//                                iBookingInfoLoadListener.onBookingInfoLoadEmpty();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                iBookingInfoLoadListener.onBookingInfoLoadFailed(e.getMessage());
//
//            }
//        });
//
//        //if userbooking has been assign data collection we will make real time listener
//        if (userBookingEvent != null) {//if userbookingEvent already init
//
//
//            //that mean we just add 1 time
//            if (userBookingListener == null) { // only add if userbooking == null
//                userBookingListener = userBooking.addSnapshotListener(userBookingEvent);
//            }
//
//        }
//
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mAuth = FirebaseAuth.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
//        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        dialog.setTitleText("Loading");
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        unbinder = ButterKnife.bind(this, view);
//
//        cartDatabase = CartDatabase.getInstance(getContext());
//
//        //init
//        Slider.init(new PicassoImageLoadingService());
//        iBannerLoadListener = this;
//        iLookBookLoadlistener = this;
//        iBookingInfoLoadListener = this;
//        iBookingInformationChangeListener = this;
//
//        //checked if logged
//        if (user != null) {
//            setUserInformation();
//            loadBanner();
//            loadLookBook();
//            initRealTimeUserBooking();//need to declare above loaduserbooking
//            loadUserBooking();
//            countCartItem();
//        }// add feature to make non log view
//
//        return view;
//    }
//
//    private void initRealTimeUserBooking() {
//
//        //inifity loop can cause u money or data quota exceeds
//
//        if (userBookingEvent == null) { // we only init event if event i snull
//            userBookingEvent = new EventListener<QuerySnapshot>() {
//
//                @Override
//                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                    //in this event when it fired we will call loaduserBooking again
//                    loadUserBooking();
//                }
//            };
//
//        }
//    }
//
//    private void countCartItem() {
//        DatabaseUtils.countItemCart(cartDatabase, this);
//    }
//
//    private void loadLookBook() {
//        lookBookRef.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<Banner> lookbooks = new ArrayList<>();
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
//                                Banner banner = bannerSnapshot.toObject(Banner.class);
//                                lookbooks.add(banner);
//                            }
//                            iLookBookLoadlistener.onLookBookLoadSuccess(lookbooks);
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                iLookBookLoadlistener.onLookBooLoadFailed(e.getMessage());
//                Toasty.error(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();//remove
//
//
//            }
//        });
//    }
//
//    private void loadBanner() {
//        bannerRef.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        List<Banner> banners = new ArrayList<>();
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot bannerSnapshot : Objects.requireNonNull(task.getResult())) {
//                                Banner banner = bannerSnapshot.toObject(Banner.class);
//                                banners.add(banner);
//
//                            }
//                            iBannerLoadListener.onBannerLoadSuccess(banners);
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
//
//            }
//        });
//    }
//
//    private void setUserInformation() {
//        layout_user_info.setVisibility(View.VISIBLE);
//        txt_userName.setText(Common.currentUser.getName()); //save to sharedprerence or save instace to prevent crash
//        txt_phone.setText(MessageFormat.format("PHONE :{0}", Common.currentUser.getPhoneNumber()));
//        txt_membership_id_number.setText(MessageFormat.format("ID: {0}", Common.currentUser.getIdNumber()));
//
//        img_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("You are about to signOut !")
//                        .setContentText("Click Ok to logout or cancel dismiss")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                                mAuth.signOut();
//                                getActivity().finish();
//                            }
//                        })
//                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                            }
//                        })
//                        .show();
//
//
//            }
//        });
//    }
//
//    @Override
//    public void onBannerLoadSuccess(List<Banner> banners) {
//
//        banner_slidey.setAdapter(new HomeSliderAdapter(banners));
//    }
//
//    @Override
//    public void onBannerLoadFailed(String message) {
//        Toasty.error(Objects.requireNonNull(getActivity()), message, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onLookBookLoadSuccess(List<Banner> banners) {
//
//        recyclerView_look_book.setHasFixedSize(true);
//        recyclerView_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        recyclerView_look_book.setItemAnimator(new DefaultItemAnimator());
//        recyclerView_look_book.setAdapter(new LookBookAdapter(getActivity(), banners));
//
//        //initailze adapter here
////        LookBookAdapter lookBookAdapter = new LookBookAdapter(getActivity(),banners);
////        recyclerView_look_book.setAdapter(lookBookAdapter);
//
//    }
//
//    @Override
//    public void onLookBooLoadFailed(String message) {
//        Toasty.error(Objects.requireNonNull(getActivity()), message, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onBookingInfoLoadEmpty() {
//        card_booking_info.setVisibility(View.GONE);
//        lotiie_animation.setVisibility(View.VISIBLE);
//        txt_book_info.setText("Your Current Booking is Displayed Here ");
//    }
//
//    @Override
//    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation, String bookinId) {
//
//        Common.currentBooking = bookingInformation;
//        Common.currentBookingId = bookinId;
//
//
//        txt_salon_address.setText(bookingInformation.getSalonAddress());
//        txt_salon_barber.setText(bookingInformation.getBarberName());
//        txt_time.setText(bookingInformation.getTime());
//        txt_status.setText(bookingInformation.getIsConfirm().trim()); //set booking status
//
//        if (txt_status.getText().equals("Booking is not confirmed")) {
//            try {
//                txt_status.setTextColor(getResources().getColor(R.color.colorstatus_red));
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//        } else
//            try {
//                txt_status.setTextColor(getResources().getColor(R.color.colorstatus_green));
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//
//
//        String dateRemain = DateUtils.getRelativeTimeSpanString(
//                Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
//                Calendar.getInstance().getTimeInMillis(), 0).toString();
//
//        txt_time_remain.setText(dateRemain);
//
//        txt_book_info.setText("You have as Appointment NB:A Reminder \nNotification As time approaches will be sent from your calendar");
//        card_booking_info.setVisibility(View.VISIBLE);
//        lotiie_animation.setVisibility(View.GONE);
//
//
//        dialog.dismiss();
//    }
//
//    @Override
//    public void onBookingInfoLoadFailed(String message) {
//
//        Toasty.error(getActivity(), message, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onBookingInformationChange() {
//        //Here we will just start activity booking
//        startActivity(new Intent(getActivity(), BookingActivity.class));
//
//    }
//
//    @Override
//    public void onCartItemCountSuccess(int count) {
//        notificationBadge.setText(String.valueOf(count));
//    }
//
//    private String getAge(int year, int month, int day) {
//        Calendar dob = Calendar.getInstance();
//        Calendar today = Calendar.getInstance();
//
//        dob.set(year, month, day);
//
//        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
//            age--;
//        }
//
//        Integer ageInt = new Integer(age);
//        String ageS = ageInt.toString();
//
//        return ageS;
//    }
//}
