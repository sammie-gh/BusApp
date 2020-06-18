package com.gh.sammie.busapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.gh.sammie.busapp.model.Bus;
import com.gh.sammie.busapp.model.BookingInformation;
import com.gh.sammie.busapp.model.Station;
import com.gh.sammie.busapp.model.User;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";

    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
    public static final String LOGGED_KEY = "UserLogged";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Station currentSalon;
    public static int step = 0;
    public static String city = "";
    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
    public static final String TITLE_KEY = "title";
    public static final String CONTENT_TYPE = "content";
    public static Bus currentBus;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    public static BookingInformation currentBooking;
    public static String currentBookingId = "";

    //sldepay
    public static String MERCHANT_KEY = "1522254751831";
    // email or mobile number associated with the merchant account
    public static String EMAIL_OR_MOBILE_NUMBER = "ofori.d.evans@gmail.com";
    // callback url
    //    public static String CALLBACK_URL = "https://www.slydepay.com.gh/";
//    public static String CALLBACK_URL = "https://webhook.site/#!/25091d9b-4752-44f0-90bf-d6fde4012423/634cd837-794f-41d7-856f-425388cebfe7/1";
    public static String CALLBACK_URL = "https://webhook.site/25091d9b-4752-44f0-90bf-d6fde4012423";


    public class RequestCode {
        public static final int IMPORT = 9999;
        public static final int WRITE_PERMISSION = 101;
    }

    public static final int BUS_SEATS_TOTAL = 15;

    public static String convertTimeSlotToString(int slot) { //seats
        switch (slot) {
            case 0:
                return "1";
            case 1:
                return "2";
            case 2:
                return "3";
            case 3:
                return "4";
            case 4:
                return "5";
            case 5:
                return "6";
            case 6:
                return "7";
            case 7:
                return "8";
            case 8:
                return "9";
            case 9:
                return "10";
            case 10:
                return "11";
            case 11:
                return "12";
            case 12:
                return "13";
            case 13:
                return "14";
            case 14:
                return "15";
            default:
                return "closed";

        }
    }

    public static String convertTimeStampToStringKey(Timestamp timestamp) {
        Date data = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(data);

    }

    public enum TOKEN_TYPE {
        CLIENT,
        BARBER,
        MANAGER,
    }

    public static void showNotification(Context context, int notification_id, String title, String content, Intent intent) {

        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,
                    notification_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "medi_app_client_o1";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Medi App Booking Client App", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Client App");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);

        }

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
//
//
//        builder.setContentTitle(title)
//                .setContentText(content)
//                .setAutoCancel(false)
//                .setSmallIcon(R.drawable.ic_local_hospital_red_24dp)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_local_hospital_red_24dp));
//
//        if (pendingIntent != null)
//            builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//
//        notificationManager.notify(notification_id, notification);


    }


    public static String formatShoppingItemName(String name) {

        return name.length() > 13 ? new StringBuilder(name.substring(0, 10)).append("...")
                .toString() : name;

    }

//    public static void updateToken(Context context, String token) {
//
//        FirebaseAuth s = FirebaseAuth.getInstance();
//
//        if (s.getCurrentUser() != null) {
//            MyToken myToken = new MyToken();
//            myToken.setToken(token);
//            myToken.setTokenType(TOKEN_TYPE.CLIENT); //cox code run from babrber staff app
//            myToken.setUid(s.getUid());
//
//            FirebaseFirestore.getInstance()
//                    .collection("Tokens")
//                    .document(s.getUid()) //to change to use member login change to userphone but only update token if user update profile in homeactivty or make uppdate to change profile
//                    .set(myToken)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                        }
//                    });
//
//        } else {
//            Paper.init(context);
//            String user = Paper.book().read(Common.LOGGED_KEY);
//            if (user != null) {
//                if (!TextUtils.isEmpty(user)) {
//                    MyToken myToken = new MyToken();
//                    myToken.setToken(token);
//                    myToken.setTokenType(TOKEN_TYPE.CLIENT); //cox code run from babrber staff app
//                    myToken.setUid(user);
//
//                    FirebaseFirestore.getInstance()
//                            .collection("Tokens")
//                            .document(user)
//                            .set(myToken)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                }
//                            });
//                }
//            }
//
//        }
//
//
////        //First check if login
////        Paper.init(context);
////        String user = Paper.book().read(Common.LOGGED_KEY);
////        if (user != null) {
////            if (!TextUtils.isEmpty(user)) {
////                MyToken myToken = new MyToken();
////                myToken.setToken(token);
////                myToken.setTokenType(TOKEN_TYPE.BARBER); //cox code run from babrber staff app
////                myToken.setUser(user);
////
////                //submit on Firestore
////                FirebaseFirestore.getInstance()
////                        .collection("Tokens")
////                        .document(user)
////                        .set(myToken)
////                        .addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////
////                            }
////                        });
////
////
////            }
////        }
//    }

}
