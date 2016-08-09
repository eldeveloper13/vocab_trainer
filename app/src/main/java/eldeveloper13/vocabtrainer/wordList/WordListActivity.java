package eldeveloper13.vocabtrainer.wordList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eldeveloper13.vocabtrainer.R;
import eldeveloper13.vocabtrainer.db.Deck;
import eldeveloper13.vocabtrainer.db.Vocab;
import eldeveloper13.vocabtrainer.review.ReviewActivity;

public class WordListActivity extends AppCompatActivity {

    @BindView(R.id.words_listview)
    ListView mWordListView;

    Deck mDeck;

    public static Intent getStartActivityIntent(Context context, @NonNull Deck deck) {
        Intent intent = new Intent(context, WordListActivity.class);
        intent.putExtra(Extras.DECK_ID, deck.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        ButterKnife.bind(this);

        Long deckId = getIntent().getLongExtra(Extras.DECK_ID, -1);
        mDeck = new Select().from(Deck.class).where("ID = ?", deckId).executeSingle();
        updateWordListView();
        Button addWordButton = new Button(this);
        addWordButton.setText("Add Word");
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWordDialog();
            }
        });
        mWordListView.addFooterView(addWordButton);

        registerForContextMenu(mWordListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.words_listview) {
            ListView listview = (ListView) v;
            AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.word_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        switch (menuItemIndex) {
            case 0:
                editVocab(contextMenuInfo.position);
                return true;
            case 1:
                deleteVocab(contextMenuInfo.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteVocab(int position) {
        Vocab vocab = (Vocab) mWordListView.getAdapter().getItem(position);
        vocab.delete();
        updateWordListView();
    }

    private void editVocab(int position) {
        final Vocab vocab = (Vocab) mWordListView.getAdapter().getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Word");
        final View view = LayoutInflater.from(this).inflate(R.layout.add_word_dialog, null);

        final EditText editWord = (EditText) view.findViewById(R.id.edit_word);
        final EditText editMeaning = (EditText) view.findViewById(R.id.edit_meaning);
        editWord.setText(vocab.word);
        editMeaning.setText(vocab.meaning);

        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newWord = editWord.getText().toString();
                String newMeaning = editMeaning.getText().toString();

                vocab.word = newWord;
                vocab.meaning = newMeaning;
                vocab.save();
                updateWordListView();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showAddWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Word");
        final View view = LayoutInflater.from(this).inflate(R.layout.add_word_dialog, null);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editWord = (EditText) view.findViewById(R.id.edit_word);
                String word = editWord.getText().toString();

                EditText editMeaning = (EditText) view.findViewById(R.id.edit_meaning);
                String meaning = editMeaning.getText().toString();

                Vocab vocab = new Vocab(word, meaning, mDeck);
                vocab.save();
                updateWordListView();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @OnClick(R.id.review_button)
    void onReviewClicked() {
        reviewVocabs();
    }

    private void updateWordListView() {
        List<Vocab> wordList = getWordList();
        mWordListView.setAdapter(new WordListAdapter(this, wordList));
    }

    private void reviewVocabs() {
        startActivity(ReviewActivity.getStartActivityIntent(this, mDeck));
    }

    private List<Vocab> getWordList() {
        return new Select().from(Vocab.class).where("Deck = ?", mDeck.getId()).execute();
    }

    private class Extras {
        public static final String DECK_ID = "EXTRAS_DECK_ID";
    }
}
