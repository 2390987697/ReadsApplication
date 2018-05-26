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
 * ���Ų˵�����ҳ
 */
public class NewsDetaiActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;// ���Բ���
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack; // ���ذ�ť
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;// ���尴ť
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;// ����ť
	@ViewInject(R.id.open_left_fragment) // �������ť
	private ImageButton btnMenu;
	@ViewInject(R.id.news_detail)
	private WebView mWebView;// ����
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;// ������
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
		// ����ֻ�sd��·��
		//
		// System.out.println("sd��·��:" + sdcardPath);
		// mWebView.loadUrl("http://www.baidu.com");// ���ص���ҳ���ӵķ���
		mWebView.loadUrl(mUrl);
		// mWebView.loadUrl("file:///android_asset/724D6A55496A11726628.html");
		WebSettings settings = mWebView.getSettings();// �õ�mWebView�����ö���(���е����ö��������setting��������洢)
		settings.setBuiltInZoomControls(true); // ��ʾ���Ű�ť(web��ҳ��֧��)
		settings.setUseWideViewPort(true);// ֧��˫������(web��ҳ��֧��)
		settings.setJavaScriptEnabled(true);// ֧��js����

		mWebView.setWebViewClient(new WebViewClient() {
			// ��ʼ������ҳ
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("��ʼ������ҳ��...");
				pbLoading.setVisibility(View.VISIBLE);
			}

			// ��ҳ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("��ҳ���ؽ�����...");
				pbLoading.setVisibility(View.INVISIBLE);
			}

			// ����������ת���ߴ˷���
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl("��ת����" + url);// ����ת����ʱǿ���ڵ�ǰWebView�м���
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		// mWebView.goBack();// �����ϸ�ҳ��
		// mWebView.goForward();// �����¸�ҳ��

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// ���ȷ����仯
				super.onProgressChanged(view, newProgress);
				System.err.println("����:" + newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				// ��ҳ����
				System.out.println("��ҳ����:" + title);
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
			// �޸���ҳ�����С�ķ���
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;
		default:
			break;
		}
	}

	private int mTempWhich;// ��¼��ʱѡ��������С(���ȷ��֮ǰ��)

	private int mCurrentWhich = 2;// ��¼��ǰѡ�е������ȡ��(���ȷ��֮���),Ĭ����������

	/**
	 * չʾѡ�������С�ĵ���
	 */
	private void showChooseDialog() {
		String items[] = { "���������", "�������", "��������", "С������", "��С������" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�����С����");
		builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ����ѡ������������޸���ҳ�����С

				WebSettings settings = mWebView.getSettings();
				switch (mTempWhich) {// ע��:���which����������ʹ��
				case 0:
					// ��������
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					// �������
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					// ��������
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					// С������
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					// ��С������
					settings.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}
				mCurrentWhich = mTempWhich;
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
	}

	// ȷ��SDcard������ڴ���ͼƬtest.jpg
	// �����ܵ�ʵ��
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();

		oks.setTheme(OnekeyShareTheme.SKYBLUE);// �޸�������ʽ

		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");// ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
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
