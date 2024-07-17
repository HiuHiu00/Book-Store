package entity;

import java.util.ArrayList;
import java.util.List;

public class GenreProvider {
    private static final List<Genre> genreList = new ArrayList<>();

    static {
        genreList.add(Genre.builder().Genre("Fantasy").genreImageUrl("Fantasy.jpg").build());
        genreList.add(Genre.builder().Genre("Science Fiction").genreImageUrl("Science Fiction.jpg").build());
        genreList.add(Genre.builder().Genre("Dystopian").genreImageUrl("Dystopian.jpg").build());
        genreList.add(Genre.builder().Genre("Action & Adventure").genreImageUrl("Action & Adventure.jpg").build());
        genreList.add(Genre.builder().Genre("Mystery").genreImageUrl("Mystery.jpg").build());
        genreList.add(Genre.builder().Genre("Horror").genreImageUrl("Horror.jpg").build());
        genreList.add(Genre.builder().Genre("Thriller & Suspense").genreImageUrl("Thriller & Suspense.jpg").build());
        genreList.add(Genre.builder().Genre("Historical Fiction").genreImageUrl("Historical Fiction.jpg").build());
        genreList.add(Genre.builder().Genre("Romance").genreImageUrl("Romance.jpg").build());
        genreList.add(Genre.builder().Genre("LGBTQ+").genreImageUrl("LGBTQ+.jpg").build());
        genreList.add(Genre.builder().Genre("Contemporary").genreImageUrl("Contemporary.jpg").build());
        genreList.add(Genre.builder().Genre("Literary Fiction").genreImageUrl("Literary Fiction.jpg").build());
        genreList.add(Genre.builder().Genre("Women’s Fiction").genreImageUrl("Women’s Fiction.jpg").build());
        genreList.add(Genre.builder().Genre("Magical Realism").genreImageUrl("Magical Realism.jpg").build());
        genreList.add(Genre.builder().Genre("Graphic Novel").genreImageUrl("Graphic Novel.jpg").build());
        genreList.add(Genre.builder().Genre("Short Story").genreImageUrl("Short Story.jpg").build());
        genreList.add(Genre.builder().Genre("Young Adult").genreImageUrl("Young Adult.jpg").build());
        genreList.add(Genre.builder().Genre("New Adult").genreImageUrl("New Adult.jpg").build());
        genreList.add(Genre.builder().Genre("Children’s").genreImageUrl("Children’s.jpg").build());
        genreList.add(Genre.builder().Genre("Memoir & Autobiography").genreImageUrl("Memoir & Autobiography.jpg").build());
        genreList.add(Genre.builder().Genre("Biography").genreImageUrl("Biography.jpg").build());
        genreList.add(Genre.builder().Genre("Food & Drink").genreImageUrl("Food & Drink.jpg").build());
        genreList.add(Genre.builder().Genre("Art & Photography").genreImageUrl("Art & Photography.jpg").build());
        genreList.add(Genre.builder().Genre("Self-help").genreImageUrl("Self-help.jpg").build());
        genreList.add(Genre.builder().Genre("History").genreImageUrl("History.jpg").build());
        genreList.add(Genre.builder().Genre("Travel").genreImageUrl("Travel.jpg").build());
        genreList.add(Genre.builder().Genre("True Crime").genreImageUrl("True Crime.jpg").build());
        genreList.add(Genre.builder().Genre("Humor").genreImageUrl("Humor.jpg").build());
        genreList.add(Genre.builder().Genre("Essays").genreImageUrl("Essays.jpg").build());
        genreList.add(Genre.builder().Genre("Guide").genreImageUrl("Guide.jpg").build());
        genreList.add(Genre.builder().Genre("Religion & Spirituality").genreImageUrl("Religion & Spirituality.jpg").build());
        genreList.add(Genre.builder().Genre("Humanities & Social Sciences").genreImageUrl("Humanities & Social Sciences.jpg").build());
        genreList.add(Genre.builder().Genre("Parenting & Families").genreImageUrl("Parenting & Families.jpg").build());
        genreList.add(Genre.builder().Genre("Science & Technology").genreImageUrl("Science & Technology.jpg").build());
    }

    public static List<Genre> getGenreList() {
        return genreList;
    }
}
