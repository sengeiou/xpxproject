package intersky.talk;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import intersky.apputils.AppUtils;

/**
 * 
 ******************************************
 * @author 廖乃波
 * @文件名称	:  FaceRelativeLayout.java
 * @创建时间	: 2013-1-27 下午02:34:17
 * @文件描述	: 带表情的自定义输入框
 ******************************************
 */
public class FaceRelativeLayout extends RelativeLayout implements
        OnItemClickListener, OnClickListener {

	private Context context;

	/** 表情页的监听事件 */
	private OnCorpusSelectedListener mListener;

	/** 显示表情页的viewpager */
	private ViewPager vp_face;

	/** 表情页界面集合 */
	private ArrayList<View> pageViews;

	/** 游标显示布局 */
	private LinearLayout layout_point;

	/** 游标点集合 */
	private ArrayList<ImageView> pointViews;

	/** 表情集合 */
	private List<List<ChatEmoji>> emojis;

	/** 表情区域 */
	private View view;

	/** 输入框 */
	public EditText et_sendmessage;

	/** 表情数据填充器 */
	private List<FaceAdapter> faceAdapters;

	/** 当前表情页 */
	private int current = 0;

	public DisplayMetrics metric;

	public FaceRelativeLayout(Context context) {
		super(context);
		this.context = context;
	}

	public FaceRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public FaceRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
		mListener = listener;
	}

	/**
	 * 表情选择监听
	 * 
	 * @author naibo-liao
	 * @时间： 2013-1-15下午04:32:54
	 */
	public interface OnCorpusSelectedListener {

		void onCorpusSelected(ChatEmoji emoji);

		void onCorpusDeleted();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		emojis = FaceConversionUtil.getInstace().emojiLists;
		onCreate();
	}

	private void onCreate() {
		metric = AppUtils.getWindowInfo(context);
		Init_View();
		Init_viewPager();
		Init_Point();
		Init_Data();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.btn_face) {// 隐藏表情选择框
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			ImageView imageView = (ImageView) v;
			if (view.getVisibility() == View.VISIBLE) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					et_sendmessage.setShowSoftInputOnFocus(true);
				} else {
					et_sendmessage.setInputType(InputType.TYPE_CLASS_TEXT);
				}
				view.setVisibility(View.GONE);
				et_sendmessage.setInputType(InputType.TYPE_CLASS_TEXT);
				et_sendmessage.requestFocus();
				imageView.setImageResource(R.drawable.imfacebtn);
				imm.showSoftInput(et_sendmessage, InputMethodManager.SHOW_FORCED);
			} else {
				et_sendmessage.requestFocus();
				view.setVisibility(View.VISIBLE);
				imageView.setImageResource(R.drawable.imkeyboard);
				imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					et_sendmessage.setShowSoftInputOnFocus(false);
				} else {
					et_sendmessage.setInputType(InputType.TYPE_NULL);
				}
			}

		} else if (i == R.id.et_sendmessage) {// 隐藏表情选择框
			if (view.getVisibility() == View.VISIBLE) {
				//view.setVisibility(View.GONE);
			}

		}
	}

	/**
	 * 隐藏表情选择框
	 */
	public boolean hideFaceView() {
		// 隐藏表情选择框
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	/**
	 * 初始化控件
	 */
	private void Init_View() {
		vp_face = (ViewPager) findViewById(R.id.vp_contains);
		et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
		layout_point = (LinearLayout) findViewById(R.id.iv_image);
		et_sendmessage.setOnClickListener(this);
		findViewById(R.id.btn_face).setOnClickListener(this);
		view = findViewById(R.id.ll_facechoose);

	}

	/**
	 * 初始化显示表情的viewpager
	 */
	private void Init_viewPager() {
		pageViews = new ArrayList<View>();
		// 左侧添加空页
		View nullView1 = new View(context);
		// 设置透明背景
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView1);

		// 中间添加表情页

		faceAdapters = new ArrayList<FaceAdapter>();
		for (int i = 0; i < emojis.size(); i++) {
			GridView view = new GridView(context);
			FaceAdapter adapter = new FaceAdapter(context, emojis.get(i));
			view.setAdapter(adapter);
			faceAdapters.add(adapter);
			view.setOnItemClickListener(this);
			view.setNumColumns(7);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing((int) (8 * metric.density));
			view.setVerticalSpacing((int) (25 * metric.density));
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
		}

		// 右侧添加空页面
		View nullView2 = new View(context);
		// 设置透明背景
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);
	}

	/**
	 * 初始化游标
	 */
	private void Init_Point() {

		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.shape_im_bg_round2);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = (int) (8* metric.density);
			layoutParams.rightMargin = (int) (8* metric.density);
			layoutParams.width = (int) (8* metric.density);
			layoutParams.height = (int) (8* metric.density);
			layout_point.addView(imageView, layoutParams);
			if (i == 0 || i == pageViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.shape_im_bg_round);
			}
			pointViews.add(imageView);

		}
	}

	/**
	 * 填充数据
	 */
	private void Init_Data() {
		vp_face.setAdapter(new ViewPagerAdapter(pageViews));

		vp_face.setCurrentItem(1);
		current = 0;
		vp_face.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				// 描绘分页点
				draw_Point(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						vp_face.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
						pointViews.get(1).setBackgroundResource(R.drawable.shape_im_bg_round);
					} else {
						vp_face.setCurrentItem(arg0 - 1);// 倒数第二屏
						pointViews.get(arg0 - 1).setBackgroundResource(
								R.drawable.shape_im_bg_round);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 绘制游标背景
	 */
	public void draw_Point(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.drawable.shape_im_bg_round);
			} else {
				pointViews.get(i).setBackgroundResource(R.drawable.shape_im_bg_round2);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ChatEmoji emoji = (ChatEmoji) faceAdapters.get(current).getItem(arg2);
		if (emoji.getId() == R.drawable.im_face_del_icon) {
			int selection = et_sendmessage.getSelectionStart();
			String text = et_sendmessage.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					et_sendmessage.getText().delete(start, end);
					return;
				}
				et_sendmessage.getText().delete(selection - 1, selection);
			}
		}
		if (!TextUtils.isEmpty(emoji.getCharacter())) {
			if (mListener != null)
				mListener.onCorpusSelected(emoji);
			SpannableString spannableString = FaceConversionUtil.getInstace()
					.addFace(getContext(), emoji.getId(), emoji.getCharacter());
			et_sendmessage.append(spannableString);
		}

	}
}
