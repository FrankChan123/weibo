package com.practise.cq.weibotest.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by CQ on 2016/5/17 0017.
 */
public class Visible implements Serializable{

    /** type 取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博 */
    public int type;
    /** 分组的组号 */
    public int list_id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public static Visible parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        Visible visible = new Visible();
        visible.type    = jsonObject.optInt("type", 0);
        visible.list_id = jsonObject.optInt("list_id", 0);

        return visible;
    }
}
