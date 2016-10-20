package com.wmx.android.wrstar.testemoji;//package com.wmx.android.wrstar.testemoji;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.byl.testemoji.emoji.EmojiEditText;
//import com.byl.testemoji.emoji.EmojiTextView;
//import com.byl.testemoji.util.EmojiUtil;
//import com.wmx.android.wrstar.R;
//
//public class MainActivity extends Activity {
//
//	private EmojiEditText et_emoji;
//	private EmojiTextView tv_emoji;
//	private TextView tv_result;
//	private Button btn_ok;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		et_emoji=(EmojiEditText) findViewById(R.id.et_emoji);
//		tv_emoji=(EmojiTextView) findViewById(R.id.tv_emoji);//ԭʼ�ַ�
//		tv_result=(TextView) findViewById(R.id.tv_result);//���˺��ַ�
//		btn_ok=(Button) findViewById(R.id.btn_ok);
//
//		btn_ok.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if(TextUtils.isEmpty(et_emoji.getText().toString())){
//					return;
//				}
//				tv_emoji.setText("ԭʼ���Ϊ��"+et_emoji.getText().toString());
//				if(EmojiUtil.containsEmoji(et_emoji.getText().toString())){//�ж��Ƿ���emojicon
//					Toast.makeText(getApplicationContext(), "�����к���emojicon����", Toast.LENGTH_SHORT).show();
//					tv_result.setText("���˽��Ϊ��"+EmojiUtil.filterEmoji(et_emoji.getText().toString()));
//				}else{
//					Toast.makeText(getApplicationContext(), "�����в���emojicon����", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//
//	}
//
//}
