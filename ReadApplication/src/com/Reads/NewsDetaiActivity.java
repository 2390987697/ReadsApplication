package com.Reads;

import com.Reads.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 新闻菜单详情页
 */
public class NewsDetaiActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;// 线性布局
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack; // 返回按钮
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;// 字体按钮
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;// 分享按钮
	@ViewInject(R.id.open_left_fragment) // 侧边栏按钮
	private ImageButton btnMenu;
	@ViewInject(R.id.news_detail)
	private WebView mWebView;// 容器
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;// 进度条
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		ViewUtils.inject(this);

		llControl.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);

		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		mUrl = getIntent().getStringExtra("url");

		System.out.println("mUrl:" + mUrl);

		// String sdcardPath =
		// Environment.getExternalStorageDirectory().getAbsolutePath();//
		// 获得手机sd卡路径
		//
		// System.out.println("sd卡路径:" + sdcardPath);
		// mWebView.loadUrl("http://www.baidu.com");// 加载的网页链接的方法
		mWebView.loadUrl(mUrl);
		// mWebView.loadUrl("file:///android_asset/724D6A55496A11726628.html");
		WebSettings settings = mWebView.getSettings();// 拿到mWebView的设置对象(所有的设置都是在这个setting对象里面存储)
		settings.setBuiltInZoomControls(true); // 显示缩放按钮(web网页不支持)
		settings.setUseWideViewPort(true);// 支持双击缩放(web网页不支持)
		settings.setJavaScriptEnabled(true);// 支持js功能

		mWebView.setWebViewClient(new WebViewClient() {
			// 开始加载网页
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("开始加载网页了...");
				pbLoading.setVisibility(View.VISIBLE);
			}

			// 网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束了...");
				pbLoading.setVisibility(View.INVISIBLE);
			}

			// 所有链接跳转会走此方法
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl("跳转链接" + url);// 在跳转链接时强制在当前WebView中加载
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		// mWebView.goBack();// 跳到上个页面
		// mWebView.goForward();// 跳到下个页面

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// 进度发生变化
				super.onProgressChanged(view, newProgress);
				System.err.println("进度:" + newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				// 网页标题
				System.out.println("网页标题:" + title);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_textsize:
			// 修改网页字体大小的方法
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;
		default:
			break;
		}
	}

	private int mTempWhich;// 记录临时选择的字体大小(点击确定之前的)

	private int mCurrentWhich = 2;// 记录当前选中的字体大取消(点击确定之后的),默认正常字体

	/**
	 * 展示选择字体大小的弹窗
	 */
	private void showChooseDialog() {
		String items[] = { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体大小设置");
		builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 根据选择的字体项来修改网页字体大小

				WebSettings settings = mWebView.getSettings();
				switch (mTempWhich) {// 注意:这个which不能再这里使用
				case 0:
					// 最大号子体
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					// 大号子体
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					// 正常子体
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					// 小号子体
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					// 最小号子体
					settings.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}
				mCurrentWhich = mTempWhich;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	// 确保SDcard下面存在此张图片test.jpg
	// 分享功能的实现
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();

		oks.setTheme(OnekeyShareTheme.SKYBLUE);// 修改主题样式

		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
