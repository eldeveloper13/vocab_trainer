package eldeveloper13.vocabtrainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eldeveloper13.vocabtrainer.db.Deck;

/**
 * Created by ericl on 7/31/2016.
 */
public class DeckAdapter extends ArrayAdapter<Deck> {

    public DeckAdapter(Context context, List<Deck> decks) {
        super(context, android.R.layout.simple_list_item_1, decks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView textView = (TextView) v.findViewById(android.R.id.text1);
        textView.setText(getItem(position).name);
        return v;
    }
}
