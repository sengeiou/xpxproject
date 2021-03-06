package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.R;
import com.bigwiner.android.ViewHelp;
import com.bigwiner.android.asks.ContactsAsks;
import com.bigwiner.android.asks.SourceAsks;
import com.bigwiner.android.entity.Complaint;
import com.bigwiner.android.entity.SourceData;
import com.bigwiner.android.view.BigwinerApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.AppActivityManager;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.entity.Contacts;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.apputils.WaitDialog;
import intersky.chat.ChatUtils;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.MyLinearLayout;
import intersky.select.entity.Select;


/**
 * Created by xpx on 2016/10/12.
 */

public class SourceListAdapter extends RecyclerView.Adapter {

    public ArrayList<SourceData> source;
    private Context mContext;
    private LayoutInflater mInflater;
    public Handler handler;
    public WaitDialog waitDialog;
    public String keyword = "";
    public SourceListAdapter(ArrayList<SourceData> source, Context mContext, Handler handler, WaitDialog waitDialog) {
        this.mContext = mContext;
        this.source = source;
        this.handler = handler;
        this.waitDialog = waitDialog;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public SourceData getItem(int position) {
        return source.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView area;
        TextView port;
        TextView name;
        TextView time;
        TextView count;
        TextView vcount;
        ImageView collectimg;
        MyLinearLayout lablelayer;
        CircleImageView imageView;

        public ViewHolder(@NonNull View convertView) {
            super(convertView);
            title = convertView.findViewById(R.id.conversation_title);
            lablelayer = convertView.findViewById(R.id.lable);
            port = convertView.findViewById(R.id.portvalue);
            area = convertView.findViewById(R.id.areavalue);
            name = convertView.findViewById(R.id.name);
            time = convertView.findViewById(R.id.time);
            count = convertView.findViewById(R.id.collectcount);
            vcount = convertView.findViewById(R.id.viewcount);
            collectimg = convertView.findViewById(R.id.collectimg);
            imageView = convertView.findViewById(R.id.conversation_img);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.source_list_data,null);
        return new ViewHolder(convertView);
    }


    public interface OnItemClickListener{
        void onItemClick(SourceData sourceData, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SourceData sourceData = getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onItemClick(sourceData,position,holder.itemView);
            }
        });

        viewHolder.lablelayer.removeAllViews();
        String lableall[] = sourceData.type.split(",");
        HashMap<String,String> lable = new HashMap<String, String>();
        for(int i = 0 ; i < lableall.length ; i++)
        {
            String[] temp = lableall[i].split("/");
            for(int j = 0 ; j < temp.length ; j++)
            {
                lable.put(temp[j],temp[j]);
            }
        }


        View top = holder.itemView.findViewById(R.id.top);
        if(position == 0)
        {
            top.setVisibility(View.VISIBLE);
        }
        else
        {
            top.setVisibility(View.GONE);
        }
        if(sourceData.sourcetype.contains("供"))
            ViewHelp.measureConversationLable(viewHolder.lablelayer,"#供",R.drawable.conversation_lable_shape_yellow,mInflater);
        else
            ViewHelp.measureConversationLable(viewHolder.lablelayer,"#需",R.drawable.conversation_lable_shape_green,mInflater);

        for (String key : lable.keySet()) {
            ViewHelp.measureConversationLable(viewHolder.lablelayer,"#"+key,R.drawable.conversation_lable_shape_gray,mInflater);
        }
        viewHolder.title.setText(sourceData.name);
        viewHolder.area.setText(sourceData.area);
        viewHolder.port.setText(sourceData.port);
        viewHolder.name.setText(sourceData.username);
        viewHolder.time.setText(sourceData.publicetime.substring(0,10));
        viewHolder.count.setText(sourceData.collectcount);
        viewHolder.vcount.setText(sourceData.viewcount);
        if(sourceData.iscollcet == 0)
            viewHolder.collectimg.setImageResource(R.drawable.uncollect);
        else
            viewHolder.collectimg.setImageResource(R.drawable.collect);
        viewHolder.collectimg.setTag(sourceData);
        viewHolder.count.setTag(sourceData);
        viewHolder.count.setOnClickListener(onCollectClickListener);
        viewHolder.collectimg.setOnClickListener(onCollectClickListener);

        View driver = viewHolder.itemView.findViewById(R.id.driver);
        if(position == source.size()-1)
        {
            driver.setVisibility(View.GONE);
        }
        else
        {
            driver.setVisibility(View.VISIBLE);
        }

        if(BigwinerApplication.mApp.contactManager.contactsHashMap.containsKey(sourceData.userid))
        {
            Contacts mContact = BigwinerApplication.mApp.contactManager.contactsHashMap.get(sourceData.userid);
            File file = ChatUtils.getChatUtils().getHead(mContact.mRecordid);

            if(mContact.sex == 0)
            {
                if(file != null)
                {
                    BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (25*ChatUtils.getChatUtils().mScreenDefine.density)
                            ,(int) (25*ChatUtils.getChatUtils().mScreenDefine.density),file);
                    RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                            .placeholder(R.drawable.default_user);
                    Glide.with(mContext).load(file).apply(options).into(new MySimpleTarget(viewHolder.imageView));
                }
                else
                {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override((int) (25* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                    Glide.with(mContext).load(ContactsAsks.getContactIconUrl(mContact.mRecordid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(viewHolder.imageView));
                }
            }
            else
            {
                if(file != null)
                {
                    BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (25*ChatUtils.getChatUtils().mScreenDefine.density)
                            ,(int) (25*ChatUtils.getChatUtils().mScreenDefine.density),file);
                    RequestOptions options = new RequestOptions().override(bitmapSize.width,bitmapSize.height)
                            .placeholder(R.drawable.default_wuser);
                    Glide.with(mContext).load(file).apply(options).into(new MySimpleTarget(viewHolder.imageView));
                }
                else
                {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_wuser).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override((int) (25* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                    Glide.with(mContext).load(ContactsAsks.getContactIconUrl(mContact.mRecordid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(viewHolder.imageView));
                }
            }
        }
        else
        {
            if(!sourceData.userid.equals(BigwinerApplication.mApp.mAccount.mRecordId))
            {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.contact_detial_head).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((int) (25* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                Glide.with(mContext).load(ContactsAsks.getContactIconUrl(sourceData.userid,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(viewHolder.imageView));
            }
            else
            {
                if(BigwinerApplication.mApp.mAccount.sex == 0)
                {
                    RequestOptions options = new RequestOptions()
                            .placeholder(com.bigwiner.R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override((int) (25* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                    Glide.with(mContext).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(viewHolder.imageView));
                }
                else
                {
                    RequestOptions options = new RequestOptions()
                            .placeholder(com.bigwiner.R.drawable.default_wuser).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override((int) (25* AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density));
                    Glide.with(mContext).load(ContactsAsks.getContactIconUrl(BigwinerApplication.mApp.mAccount.mRecordId,BigwinerApplication.mApp.contactManager.updataKey)).apply(options).into(new MySimpleTarget(viewHolder.imageView));
                }

            }
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return source.size();
    }


    public View.OnClickListener onCollectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SourceData sourceData = (SourceData) v.getTag();
            if(sourceData.iscollcet == 0)
            {
                waitDialog.show();
                SourceAsks.getSourceCollect(mContext,handler,sourceData);
            }
            else
            {
                waitDialog.show();
                SourceAsks.getSourceCollect(mContext,handler,sourceData);
            }
        }
    };
}
