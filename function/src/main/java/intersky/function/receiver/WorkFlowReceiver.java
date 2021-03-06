package intersky.function.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseReceiver;
import intersky.function.handler.GridHandler;
import intersky.function.handler.WorkFlowHandler;
import intersky.function.view.activity.GridDetialActivity;


public class WorkFlowReceiver extends BaseReceiver {

    public Handler mHandler;

    public WorkFlowReceiver(Handler mHandler)
    {
        this.mHandler = mHandler;
        this.intentFilter = new IntentFilter();
        intentFilter.addAction(GridDetialActivity.ACTION_WORKFLOW_HIT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(GridDetialActivity.ACTION_WORKFLOW_HIT))
        {
            if(mHandler != null)
            {
                Message msg = new Message();
                msg.what = WorkFlowHandler.UPDATA_LIST;
                msg.obj = intent;
                mHandler.sendMessage(msg);
            }

        }

    }
}
