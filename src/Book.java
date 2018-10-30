import java.util.List;

public class Book {
    private String title;
    private int price;
    private int count;
    public Book (String title, int price, int count)
    {
        this.count=count;
        this.price = price;
        this.title = title;
    }
    public static Integer FindBook(List<Book> books, String title)
    {
        for (Book book:books) {
            if (book.title.equalsIgnoreCase(title))
            {
                if (book.count>0)
                    return book.price;
                else return null;
            }
        }
        return null;
    }

    public static Integer Remove(List<Book> books, String title)
    {
        for (Book book:books) {
            if (book.title.equalsIgnoreCase(title))
            {
                if (book.count>0)
                {
                    book.count--;
                    return book.price;
                }
                else return null;
            }
        }
        return null;
    }
}
