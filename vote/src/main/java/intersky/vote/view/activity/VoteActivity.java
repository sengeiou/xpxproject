package intersky.vote.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.select.entity.Select;
import intersky.vote.entity.Vote;
import intersky.vote.entity.VoteSelect;
import intersky.vote.presenter.VotePresenter;


@SuppressLint("ClickableViewAccessibility")
public class VoteActivity extends BaseActivity
{

	public VotePresenter mVotePresenter = new VotePresenter(this);
	public VoteSelect mSelectItemModel;
	public RelativeLayout timeLayer;
	public RelativeLayout formLayer;
	public RelativeLayout voterLayer;
	public LinearLayout itemLayer;
	public EditText content;
	public TextView time;
	public TextView form;
	public TextView voter;
	public Switch mSwitch;
	public Select mselectVoteType;
	public ArrayList<Contacts> mVoters = new ArrayList<Contacts>();
	public ArrayList<VoteSelect> mVoteItems = new ArrayList<VoteSelect>();
	public PopupWindow popupWindow1;
	public RelativeLayout mRelativeLayout;
	public Uri fileUri;
	public String mfilepath;
	public boolean isdelete = false;
	public boolean isuploading = false;
	public Vote vote;
	public boolean issub = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mVotePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mVotePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mVotePresenter.Start();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mVotePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mVotePresenter.Resume();
	}

	public View.OnClickListener setDateTime = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mVotePresenter.showEndTimeDialog();
		}
	};

	public View.OnClickListener setFrorm = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mVotePresenter.doForm();
		}
	};

	public View.OnClickListener setVoter = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mVotePresenter.selectVoter();
		}
	};

	public View.OnClickListener mSave = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mVotePresenter.doSave();
		}
	};

	public View.OnClickListener mShowAddListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.showAdd(v);
		}
	};

	public View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.addPic(v);
		}
	};


	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.takePhoto(v);
		}

	};

	public View.OnClickListener mShowPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.showPic( v);
		}
	};

	public View.OnClickListener mSaveListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.doSave();
		}
	};

	public View.OnClickListener maddItemListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.additem();
		}
	};

	public View.OnClickListener mdelItemListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVotePresenter.deleteitem();
		}
	};

	public View.OnClickListener mdodelItemListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method vstub
			mVotePresenter.dodelete(v);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mVotePresenter.takePhotoResult(requestCode, resultCode, data);
	}
}
