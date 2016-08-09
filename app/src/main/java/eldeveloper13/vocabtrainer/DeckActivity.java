package eldeveloper13.vocabtrainer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eldeveloper13.vocabtrainer.db.Deck;
import eldeveloper13.vocabtrainer.db.Vocab;
import eldeveloper13.vocabtrainer.wordList.WordListActivity;

public class DeckActivity extends AppCompatActivity {

    @BindView(R.id.deck_listview)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);
        ButterKnife.bind(this);

        updateDeckList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.deck_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_deck:
                showCreateDeckDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCreateDeckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create new Deck");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String deckName = input.getText().toString();
                createNewDeck(deckName);
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

    private void createNewDeck(String deckName) {
        Deck deck = new Deck(deckName);
        deck.save();
        updateDeckList();
    }

    private void updateDeckList() {
        List<Deck> deckList = getDeckList();
        mListView.setAdapter(new DeckAdapter(this, deckList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deck deck = (Deck) mListView.getAdapter().getItem(position);
                Intent intent = WordListActivity.getStartActivityIntent(DeckActivity.this, deck);
                startActivity(intent);
            }
        });
    }

    private List<Deck> getDeckList() {
        return new Select().from(Deck.class).execute();
    }
}
