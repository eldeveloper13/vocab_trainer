package eldeveloper13.vocabtrainer.wordList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eldeveloper13.vocabtrainer.R;
import eldeveloper13.vocabtrainer.db.Vocab;

/**
 * Created by ericl on 7/30/2016.
 */
public class WordListAdapter extends ArrayAdapter<Vocab> {

    public WordListAdapter(Context context, List<Vocab> words) {
        super(context, R.layout.word_list_item, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.word_list_item, null);
        }

        Vocab vocab = getItem(position);
        if (vocab != null) {
            TextView textView1 = (TextView) v.findViewById(R.id.word_item_text1);
            TextView textView2 = (TextView) v.findViewById(R.id.word_item_text2);

            textView1.setText(vocab.word);
            textView2.setText(vocab.meaning);
        }
        return v;
    }
}
