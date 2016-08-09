package eldeveloper13.vocabtrainer.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name = "Decks")
public class Deck extends Model {

    @Column(name = "Name")
    public String name;

    public Deck() {
        super();
    }

    public Deck(String name) {
        super();
        this.name = name;
    }

    public List<Vocab> vocabs() {
        return getMany(Vocab.class, "Deck");
    }
}
