package com.practise.cq.weibotest.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/21 0021.
 */
public class Comment implements Serializable{

    /** 评论创建时间 */
    public String created_at;
    /** 评论的 ID */
    public long id;
    /** 评论的内容 */
    public String text;
    /** 评论的来源 */
    public String source;
    /** 评论作者的用户信息字段 */
    public User user;
    /** 评论的 MID */
    public String mid;
    /** 字符串型的评论 ID */
    public String idstr;
    /** 评论的微博信息字段 */
    public Status status;
    /** 评论来源评论，当本评论属于对另一评论的回复时返回此字段 */
    public Comment reply_comment;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Comment getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(Comment reply_comment) {
        this.reply_comment = reply_comment;
    }


    public static Comment parse(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        Comment comment = new Comment();
        comment.created_at    = jsonObject.optString("created_at");
        comment.id            = jsonObject.optLong("id");
        comment.text          = jsonObject.optString("text");
        comment.source        = jsonObject.optString("source");
        comment.user          = User.parse(jsonObject.optJSONObject("user"));
        comment.mid           = jsonObject.optString("mid");
        comment.idstr         = jsonObject.optString("idstr");
        comment.status        = Status.parse(jsonObject.optJSONObject("status"));
        comment.reply_comment = Comment.parse(jsonObject.optJSONObject("reply_comment"));

        return comment;
    }

    public static List<Comment> parse2List(String response)throws JSONException {
        if (response == null){
            return null;
        }

        List<Comment> comments = null;
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("comments");
        if (jsonArray != null && jsonArray.length() >= 1){
            int length = jsonArray.length();
            comments = new ArrayList<Comment>(length);
            JSONObject tmpJsonObject = null;
            for (int i=0; i<length; i++){
                tmpJsonObject = jsonArray.optJSONObject(i);
                Comment comment = parse(tmpJsonObject);
                comments.add(comment);
            }
        }

        return comments;
    }
}
