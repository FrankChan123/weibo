package com.practise.cq.weibotest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.practise.cq.weibotest.R;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CQ on 2016/5/24 0024.
 */
public class ImagesWallAdapter extends ArrayAdapter<String> implements AbsListView.OnScrollListener{

    private Set<BitmapWorkerTask> taskCollection;
    private LruCache<String, Bitmap> memoryCache;
    private GridView imagesWall;
    private int mfirstVisibleItem;
    private int mvisibleItemCount;
    private String[] urls;

    private boolean isFirstEnter = true;

    public ImagesWallAdapter(Context context, int textViewResourceId, String[] urls, GridView imagesWall) {
        super(context, textViewResourceId, urls);

        this.urls = urls;
        this.imagesWall = imagesWall;
        taskCollection = new HashSet<BitmapWorkerTask>();
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

        imagesWall.setOnScrollListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String url = getItem(position);
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_gridview, null);
        }else {
            view = convertView;
        }

        final ImageView iv = (ImageView)view.findViewById(R.id.iv_image);

        /**为每个ImageView设置了一个唯一的Tag，
         * 这个Tag的作用是为了后面能够准确地找回这个ImageView，不然异步加载图片会出现乱序的情况*/
        iv.setTag(url);

        setImage(url, iv);

        return view;

    }

    /**将url作为键值，从缓存中取出bitmap加载到imageView中
     * 没有则加载默认图片*/
    private void setImage(String url, ImageView iv) {
        Bitmap bitmap = getBitmapFromMemoryCache(url);
        if (bitmap != null){
            iv.setImageBitmap(bitmap);
        }else {
            iv.setImageResource(R.mipmap.timeline_image_failure);
        }
    }

    private void addBitmap2MemoryCache(String url, Bitmap bitmap){
        if (getBitmapFromMemoryCache(url) == null){
            memoryCache.put(url, bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String url) {
        return memoryCache.get(url);
    }


    /**滑动时不加载图片*/
    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            loadImgs(mfirstVisibleItem, mvisibleItemCount);
        }else {
            cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int itemTotalCount) {
        mfirstVisibleItem = firstVisibleItem;
        mvisibleItemCount = visibleItemCount;
        /**首次进入时，应该加载图片*/
        if (isFirstEnter && mvisibleItemCount > 0){
            loadImgs(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    private void loadImgs(int firstVisibleItem, int visibleItemCount) {
        try{
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++){
                String url = urls[i];
                Bitmap bitmap = getBitmapFromMemoryCache(url);
                if (bitmap == null){
                    BitmapWorkerTask task = new BitmapWorkerTask();
                    taskCollection.add(task);
                    task.execute(url);
                }else {
                    ImageView iv = (ImageView) imagesWall.findViewWithTag(url);
                    if (iv != null && bitmap != null){
                        iv.setImageBitmap(bitmap);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**取消所有正在下载或等待下载的任务*/
    public void  cancelAllTasks(){
        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private String url;

        /**开启子线程，执行下载*/
        @Override
        protected Bitmap doInBackground(String... urls) {
            url = urls[0];
            Bitmap bitmap = downloadBitmap(urls[0]);
            if (bitmap != null){
                addBitmap2MemoryCache(urls[0], bitmap);
            }
            return bitmap;
        }

        /**下载返回bitmap，设置到imageView*/
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView iv = (ImageView)imagesWall.findViewWithTag(url);
            if (iv != null && bitmap != null){
                iv.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }

        /**根据指定url下载bitmap*/
        private Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5*1000);
                connection.setReadTimeout(10*1000);
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
            return bitmap;
        }
    }

}
