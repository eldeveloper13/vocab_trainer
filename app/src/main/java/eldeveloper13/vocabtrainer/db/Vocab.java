package eldeveloper13.vocabtrainer.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Vocabs")
public class Vocab extends Model {

    @Column(name = "Word")
    public String word;

    @Column(name = "Meaning")
    public String meaning;

    @Column(name = "Deck")
    public Deck deck;

    public Vocab() {
        super();
    }

    public Vocab(String word, String meaning, Deck deck) {
        super();
        this.word = word;
        this.meaning = meaning;
        this.deck = deck;
    }
}
