package eldeveloper13.vocabtrainer.review;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eldeveloper13.vocabtrainer.R;
import eldeveloper13.vocabtrainer.db.Deck;
import eldeveloper13.vocabtrainer.db.Vocab;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.txt_word)
    TextView mTxtWord;

    @BindView(R.id.edittext_answer)
    EditText mEditAnswer;

    @BindView(R.id.submit_button)
    Button mSubmitButton;

    @BindView(R.id.txt_correct_answer)
    TextView mTxtCorrectAnswer;

    private List<Vocab> mVocabList = new ArrayList<>();
    private int questionIndex = 0;
    private long mDeckId = -1;

    public static Intent getStartActivityIntent(Context context, @NonNull Deck deck) {
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra(Extras.DECK_ID, deck.getId());
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        mDeckId = getIntent().getLongExtra(Extras.DECK_ID, -1);
        mVocabList = getVocabList(mDeckId);
        questionIndex = 0;

        displayQuestion();
    }

    @OnClick(R.id.submit_button)
    void onSubmitClicked() {
        mEditAnswer.clearFocus();
        closeKeyboard();
        if (mSubmitButton.getText().equals("Submit")) {
            String answer = mEditAnswer.getText().toString();
            mTxtCorrectAnswer.setVisibility(View.VISIBLE);
            if (answer.equalsIgnoreCase(mVocabList.get(questionIndex).meaning.toString())) {
                mEditAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
            } else {
                mTxtCorrectAnswer.setText(mVocabList.get(questionIndex).meaning);
                mEditAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_incorrect, 0);
            }

            mEditAnswer.setFocusable(false);
            mEditAnswer.setFocusableInTouchMode(false);
            mSubmitButton.setText("Next");
        } else {
            questionIndex ++;
            if (questionIndex >= mVocabList.size()) {
                shuffleVocabList();
                questionIndex = 0;
            }
            displayQuestion();
        }
    }

    private void displayQuestion() {
        if (questionIndex >= mVocabList.size())
            questionIndex = 0;

        mTxtWord.setText(mVocabList.get(questionIndex).word);
        mTxtWord.setBackgroundColor(Color.WHITE);
        mTxtCorrectAnswer.setText("");

        mEditAnswer.setText("");
        mEditAnswer.setFocusable(true);
        mEditAnswer.setFocusableInTouchMode(true);
        mEditAnswer.requestFocus();
        mEditAnswer.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        mTxtCorrectAnswer.setVisibility(View.INVISIBLE);
        mSubmitButton.setText("Submit");
    }

    private List<Vocab> getVocabList(Long deckId) {
        return new Select().from(Vocab.class).where("Deck = ?", deckId).orderBy("RANDOM()").execute();
    }

    private void shuffleVocabList() {
        mVocabList = getVocabList(mDeckId);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private class Extras {
        public static final String DECK_ID = "EXTRA_DECK_ID";
    }
}
