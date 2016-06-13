package com.practise.cq.weibotest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.practise.cq.weibotest.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CQ on 2016/5/24 0024.
 */
public class StringUtils {

    public static SpannableString getWeiboContent(final Context context, final TextView tv, String source) {
        String regexAt = "@[\u4e00-\u9fa5\\w]+";
        String regexTopic = "#[\u4e00-\u9fa5\\w]+#";
        String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";

        String regex = "(" + regexAt + ")|(" + regexTopic + " )|(" + regexEmoji + ")";



        SpannableString spannableString = new SpannableString(source);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);

        if (matcher.find()){
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            matcher.reset();
        }

        while (matcher.find()){
            final String atStr = matcher.group(1);
            final String topicStr = matcher.group(2);
            final String emojiStr = matcher.group(3);

            if (atStr != null){
                int start = matcher.start(1);
                MyClickableSpan clickableSpan = new MyClickableSpan(context){
                    @Override
                    public void onClick(View view) {

                    }
                };
                spannableString.setSpan(clickableSpan, start, start + atStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (topicStr != null){
                int start = matcher.start(2);
                MyClickableSpan clickableSpan = new MyClickableSpan(context){
                    @Override
                    public void onClick(View view) {

                    }
                };
                spannableString.setSpan(clickableSpan, start, start + topicStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (emojiStr != null){
                int start = matcher.start(3);

                int imageId = EmotionUtils.getImgByName(emojiStr);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);

                if (bitmap != null) {
                    int  size = (int) tv.getTextSize();
                    bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    ImageSpan imageSpan = new ImageSpan(context, bitmap);

                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spannableString;
    }

    private static class MyClickableSpan extends ClickableSpan{

        private Context context;

        public MyClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.txt_at_blue));
            ds.setUnderlineText(false);
        }
    }
}
