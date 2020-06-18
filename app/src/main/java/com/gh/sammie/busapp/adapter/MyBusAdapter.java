package com.gh.sammie.busapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.busapp.EventBus.EnableNextButton;
import com.gh.sammie.busapp.Interface.IRecyclerItemSelectedListener;
import com.gh.sammie.busapp.R;
import com.gh.sammie.busapp.model.Bus;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyBusAdapter extends RecyclerView.Adapter<MyBusAdapter.MyViewHolder> {


    Context context;
    List<Bus> barberList;
    List<CardView> cardViewList;
//    LocalBroadcastManager localBroadcastManager;


    public MyBusAdapter(Context context, List<Bus> barberList) {
        this.context = context;
        this.barberList = barberList;
        cardViewList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_barber, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.txt_barber_name.setText(barberList.get(i).getName());
        myViewHolder.ratingBar.setRating((float) barberList.get(i).getRating());

        if (!cardViewList.contains(myViewHolder.card_barber))
            cardViewList.add(myViewHolder.card_barber);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background for all item not selected
                for (CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }

                // set background for selected
                myViewHolder.card_barber.setCardBackgroundColor(
                        context.getResources().getColor(R.color.colorAccent));

                ////EVENT BUS
                EventBus.getDefault().postSticky(new EnableNextButton(2, barberList.get(i)));


            }
        });


    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_barber_name;
        RatingBar ratingBar;
        CardView card_barber;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_barber_name = itemView.findViewById(R.id.txt_barber_name);
            ratingBar = itemView.findViewById(R.id.rtb_barber);
            card_barber = itemView.findViewById(R.id.card_barber);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }


    }
}
