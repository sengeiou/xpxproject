package intersky.mail.entity;

import android.graphics.Color;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.mail.R;

public class MailContact {

	public TextView mTextView;
	public String mailAddress = "";
	public String mName = "";
	public String mRName = "";
	public boolean isselect = false;
	public boolean addressCurrect = false;
	public boolean itemselect = false;
	public int type = 0;
	public String pingyin = "";
	public String mailRecordID = "";
	public boolean islocal = false;
	public int colorhead = -1;
	public MailContact(String mName,String mailAddress) {
		this.mName = mName;
		if(AppUtils.checkEmail(mailAddress))
		{
			this.mailAddress = mailAddress.toLowerCase();
			addressCurrect = true;
		}
		if(this.mName != null)
		{
			this.pingyin= CharacterParser.getInstance().getSelling(this.mName);
			this.pingyin=this.pingyin.toLowerCase();
		}
	}

	public MailContact(String mName, String mailAddress, boolean isloacl) {
		this.mName = mName;
		if(isloacl)
		{
			this.mailAddress = mailAddress;
			this.mailRecordID = mailAddress;
		}
		else
			this.mailAddress = mailAddress.toLowerCase();
		addressCurrect = true;
		if(this.mName != null)
		{
			this.pingyin=CharacterParser.getInstance().getSelling(this.mName);
			this.pingyin=this.pingyin.toLowerCase();
		}
	}

	public MailContact(String mName, int type) {
		this.mName = mName;
		this.pingyin = mName.toLowerCase();
		this.type = type;
	}
	
	private boolean matchMail(String mailAddress) {
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

		Matcher matcher = pattern.matcher(mailAddress);
		return  matcher.matches();

	}

	public TextView getmTextView()
	{
		return mTextView;
	}

	public void setmTextView(TextView mTextView) {
		this.mTextView = mTextView;
		if(this.mTextView != null)
		{
			if(mRName.length() == 0)
			mTextView.setText(mName);
			else
				mTextView.setText(mRName);
			if(isselect)
			{
				this.mTextView.setTextColor(Color.rgb(255, 255, 255));
				if(addressCurrect == true)
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person_s);
				}
				else
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person_wrong_s);
				}
			}
			else
			{
				this.mTextView.setTextColor(Color.rgb(0, 0, 0));
				if(addressCurrect == true)
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person);
				}
				else
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person_wrong);
				}
				
			}
			
		}
	}
	
	public void reSetmTextView() {
		if(this.mTextView != null)
		{
			if(mRName.length() == 0)
			mTextView.setText(mName);
			else
				mTextView.setText(mRName);
			if(isselect)
			{
				this.mTextView.setTextColor(Color.rgb(255, 255, 255));
				if(addressCurrect == true)
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person_s);
				}
				else
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person_wrong_s);
				}
			}
			else
			{
				this.mTextView.setTextColor(Color.rgb(0, 0, 0));
				if(addressCurrect == true)
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person);
				}
				else
				{
					this.mTextView.setBackgroundResource(R.drawable.shape_mial_person_wrong);
				}
				
			}
			
		}
	}

	public String getMailAddress()
	{
		return mailAddress;
	}

	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress.toLowerCase();
	}

	public void setmName(String mName) {
		this.mName = mName;
		if(this.mName != null)
		{
			this.pingyin=CharacterParser.getInstance().getSelling(this.mName);
			this.pingyin=this.pingyin.toLowerCase();
		}
	}

}
