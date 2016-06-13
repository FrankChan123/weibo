package com.practise.cq.weibotest.entity;

import java.util.List;

/**
 * Created by CQ on 2016/5/29 0029.
 */
public class CommentResponse {

    private List<Comment> comments;
    private int total_number;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}
