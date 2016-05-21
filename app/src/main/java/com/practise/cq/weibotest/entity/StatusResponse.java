package com.practise.cq.weibotest.entity;

import java.util.ArrayList;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class StatusResponse {

    private ArrayList<Status> statuses;
    private int totalNum;

    public StatusResponse(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }


}
