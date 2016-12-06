package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String EXTRA_HAS_SHOWN = "com.bignerdrranch.android.geoquiz.has_shown";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    private boolean mHasShown = false;

    // 创建启动该 Activity 需要的 Intent
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent cheatIntent = new Intent(packageContext, CheatActivity.class);

        cheatIntent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return cheatIntent;
    }

    // 解析回传给 Parent Activity 的 Intent 中的数据
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // getIntent() 方法调用方注入进来的 Intent 实例， getBooleanExtra 方法的第二个参数是 指定 Key 的默认值
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);

        // 修复选择导致作弊记录丢失的缺陷
        if(savedInstanceState != null){
            mHasShown = savedInstanceState.getBoolean(EXTRA_HAS_SHOWN);
            setAnswerText(mAnswerTextView);
            mShowAnswerButton.setVisibility(View.INVISIBLE);
            setAnswerShownResult(mHasShown);
        }

        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setAnswerText(mAnswerTextView);
                mHasShown = true;

                setAnswerShownResult(mHasShown);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    // 按钮隐藏动画
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radio = mShowAnswerButton.getWidth();

                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radio, 0);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });

                    anim.start();
                }else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);

        Log.d(TAG, TAG + ".onSaveInstanceState");
        saveInstanceState.putBoolean(EXTRA_HAS_SHOWN, mHasShown);
    }

    // 设置返回给父 Activity 的结果
    private void setAnswerShownResult(boolean isAnwserShown){
        Intent data = new Intent();

        data.putExtra(EXTRA_ANSWER_SHOWN, isAnwserShown);
        setResult(RESULT_OK, data);
    }

    private void setAnswerText(TextView mAnswerTextView){
        if(mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        }else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }
}
