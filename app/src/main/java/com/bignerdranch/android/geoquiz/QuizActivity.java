package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private int mCurrentIndex = 1;
    private boolean mIsCheater;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    // 更新问题
    private void updateQuestion(boolean isNext, Bundle savedInstanceState){

        mIsCheater = false;
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        }else{
            if(isNext){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
            }else{
                mCurrentIndex -= 1;
                if(mCurrentIndex < 0){
                    mCurrentIndex = mCurrentIndex + mQuestionBank.length;
                }
            }
        }

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    // 检查问题正确性, 如果题目正确就跳转道下一题
    private void checkAnswer(boolean userPressedTrue){
        boolean anserIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if (userPressedTrue == anserIsTrue){
                messageResId = R.string.correct_toast;
                updateQuestion(true, null);
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    // 在 Activity 启动的时候首先会触发的事件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // 通过资源 id 获取组件
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(true, null);
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);

        // 设置监听事件
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);

        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(true, null);
            }
        });

        // 作弊查看答案
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "start a new activity");

                boolean answer = mQuestionBank[mCurrentIndex].isAnswerTrue();
                // 通过 startActivity 来启动 CheatActivity
                // startActivity(CheatActivity.newIntent(QuizActivity.this, answer));

                // 同过 startActivityForResult 方法来从指定的 子 activity 中获取结果值
                startActivityForResult(CheatActivity.newIntent(QuizActivity.this, answer), REQUEST_CODE_CHEAT);
            }
        });

        // 上一题
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateQuestion(false, null);
            }
        });

        updateQuestion(false, savedInstanceState);
    }

    // 当从子 Activity 返回时，系统会调用该方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        switch (requestCode){
            case REQUEST_CODE_CHEAT:
                if(result == null){
                    return;
                }
                mIsCheater = CheatActivity.wasAnswerShown(result);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    // 在 activity 重新构建前保存数据
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}