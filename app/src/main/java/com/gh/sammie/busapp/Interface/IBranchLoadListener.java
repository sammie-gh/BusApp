package com.gh.sammie.busapp.Interface;


import com.gh.sammie.busapp.model.Station;

import java.util.List;

public interface IBranchLoadListener {

    void onBranchLoadSuccess(List<Station> salonList);
    void onBranchLoadFailed(String message);
}
