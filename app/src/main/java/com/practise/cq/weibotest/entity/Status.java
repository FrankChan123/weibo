package com.practise.cq.weibotest.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class Status {
    /** 微博创建时间 */
    public String created_at;
    /** 微博ID */
    public String id;
    /** 微博MID */
    public String mid;
    /** 字符串型的微博ID */
    public String idstr;
    /** 微博信息内容 */
    public String text;
    /** 微博来源 */
    public String source;
    /** 是否已收藏，true：是，false：否  */
    public boolean favorited;
    /** 是否被截断，true：是，false：否 */
    public boolean truncated;
    /**（暂未支持）回复ID */
    public String in_reply_to_status_id;
    /**（暂未支持）回复人UID */
    public String in_reply_to_user_id;
    /**（暂未支持）回复人昵称 */
    public String in_reply_to_screen_name;
    /** 缩略图片地址（小图），没有时不返回此字段 */
    public String thumbnail_pic;
    /** 中等尺寸图片地址（中图），没有时不返回此字段 */
    public String bmiddle_pic;
    /** 原始图片地址（原图），没有时不返回此字段 */
    public String original_pic;
    /** 地理信息字段 */
    public Geo geo;
    /** 微博作者的用户信息字段 */
    public User user;
    /** 被转发的原微博信息字段，当该微博为转发微博时返回 */
    public Status retweeted_status;
    /** 转发数 */
    public int reposts_count;
    /** 评论数 */
    public int comments_count;
    /** 表态数 */
    public int attitudes_count;
    /** 暂未支持 */
    public int mlevel;
    /**
     * 微博的可见性及指定可见分组信息。该 object 中 type 取值，
     * 0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；
     * list_id为分组的组号
     */
    public Visible visible;
    /** 微博配图地址。多图时返回多图链接。无配图返回"[]" */
    public ArrayList<String> pic_urls;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Status retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getMlevel() {
        return mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public Visible getVisible() {
        return visible;
    }

    public void setVisible(Visible visible) {
        this.visible = visible;
    }

    public ArrayList<String> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(ArrayList<String> pic_urls) {
        this.pic_urls = pic_urls;
    }

   /**先将String解析为JSONObject，再将JSONObject解析为Status*/
    public static Status parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Status.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**将JSONObject解析为Status方法*/
    public static Status parse(JSONObject tempJsonObject)throws JSONException {
        if (null == tempJsonObject) {
            return null;
        }
        Status status = new Status();
        status.created_at       = tempJsonObject.optString("created_at");
        status.id               = tempJsonObject.optString("id");
        status.mid              = tempJsonObject.optString("mid");
        status.idstr            = tempJsonObject.optString("idstr");
        status.text             = tempJsonObject.optString("text");
        status.source           = tempJsonObject.optString("source");
        status.favorited        = tempJsonObject.optBoolean("favorited", false);
        status.truncated        = tempJsonObject.optBoolean("truncated", false);

        // Have NOT supported
        status.in_reply_to_status_id   = tempJsonObject.optString("in_reply_to_status_id");
        status.in_reply_to_user_id     = tempJsonObject.optString("in_reply_to_user_id");
        status.in_reply_to_screen_name = tempJsonObject.optString("in_reply_to_screen_name");

        status.thumbnail_pic    = tempJsonObject.optString("thumbnail_pic");
        status.bmiddle_pic      = tempJsonObject.optString("bmiddle_pic");
        status.original_pic     = tempJsonObject.optString("original_pic");
        status.geo              = Geo.parse(tempJsonObject.optJSONObject("geo"));
        status.user             = User.parse(tempJsonObject.optJSONObject("user"));
        status.retweeted_status = Status.parse(tempJsonObject.optJSONObject("retweeted_status"));
        status.reposts_count    = tempJsonObject.optInt("reposts_count");
        status.comments_count   = tempJsonObject.optInt("comments_count");
        status.attitudes_count  = tempJsonObject.optInt("attitudes_count");
        status.mlevel           = tempJsonObject.optInt("mlevel", -1);    // Have NOT supported
        status.visible          = Visible.parse(tempJsonObject.optJSONObject("visible"));

        JSONArray picUrlsArray = tempJsonObject.optJSONArray("pic_urls");
        if (picUrlsArray != null && picUrlsArray.length() > 0) {
            int length4pic = picUrlsArray.length();
            status.pic_urls = new ArrayList<String>(length4pic);
            JSONObject tmpObject = null;
            for (int ix = 0; ix < length4pic; ix++) {
                tmpObject = picUrlsArray.optJSONObject(ix);
                if (tmpObject != null) {
                    status.pic_urls.add(tmpObject.optString("thumbnail_pic"));
                }
            }
        }

        return status;
    }

    /**将String解析为Status集合方法*/
    public static List<Status> parse2List(String jsonString) throws JSONException {

        List<Status> statuses = null;
        if (null == jsonString) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("statuses");
        if (jsonArray != null && jsonArray.length() > 0){
            int length = jsonArray.length();
            statuses = new ArrayList<Status>(length);
            JSONObject tempJsonObject = null;
            for (int i=0; i < jsonArray.length(); i++){
                tempJsonObject = jsonArray.optJSONObject(i);
                Status status = parse(tempJsonObject);
                statuses.add(status);
            }
        }
        //status.ad = tempJsonObject.optString("ad", "");
        return statuses;
    }

//    /**将JSONObject解析为Status方法*/
//    public static Status parse(JSONObject jsonObject) throws JSONException {
//        if (null == jsonObject) {
//            return null;
//        }
//        
//        Status status = new Status();
//        status.created_at       = tempJsonObject.optString("created_at");
//        status.id               = tempJsonObject.optString("id");
//        status.mid              = tempJsonObject.optString("mid");
//        status.idstr            = tempJsonObject.optString("idstr");
//        status.text             = tempJsonObject.optString("text");
//        status.source           = tempJsonObject.optString("source");
//        status.favorited        = jsonObject.optBoolean("favorited", false);
//        status.truncated        = jsonObject.optBoolean("truncated", false);
//
//        // Have NOT supported
//        status.in_reply_to_status_id   = tempJsonObject.optString("in_reply_to_status_id");
//        status.in_reply_to_user_id     = tempJsonObject.optString("in_reply_to_user_id");
//        status.in_reply_to_screen_name = tempJsonObject.optString("in_reply_to_screen_name");
//
//        status.thumbnail_pic    = tempJsonObject.optString("thumbnail_pic");
//        status.bmiddle_pic      = tempJsonObject.optString("bmiddle_pic");
//        status.original_pic     = tempJsonObject.optString("original_pic");
//        status.geo              = Geo.parse(jsonObject.optJSONObject("geo"));
//        status.user             = User.parse(jsonObject.optJSONObject("user"));
//        status.retweeted_status = Status.parse(jsonObject.optJSONObject("retweeted_status"));
//        status.reposts_count    = jsonObject.optInt("reposts_count");
//        status.comments_count   = jsonObject.optInt("comments_count");
//        status.attitudes_count  = jsonObject.optInt("attitudes_count");
//        status.mlevel           = jsonObject.optInt("mlevel", -1);    // Have NOT supported
//        status.visible          = Visible.parse(jsonObject.optJSONObject("visible"));
//
//        JSONArray picUrlsArray = jsonObject.optJSONArray("pic_urls");
//        if (picUrlsArray != null && picUrlsArray.length() > 0) {
//            int length = picUrlsArray.length();
//            status.pic_urls = new ArrayList<String>(length);
//            JSONObject tmpObject = null;
//            for (int ix = 0; ix < length; ix++) {
//                tmpObject = picUrlsArray.optJSONObject(ix);
//                if (tmpObject != null) {
//                    status.pic_urls.add(tmpObject.optString("thumbnail_pic"));
//                }
//            }
//        }
//
//        //status.ad = tempJsonObject.optString("ad", "");
//
//        return status;
//    }
}
