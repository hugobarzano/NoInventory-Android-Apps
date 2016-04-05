package com.noinventory.volley;

/**
 * Created by hugo on 5/04/16.
 */
import java.util.List;

public class ListaPost {
    // Encapsulamiento de Posts
    private List<Post> items;

    public ListaPost(List<Post> items) {
        this.items = items;
    }

    public void setItems(List<Post> items) {
        this.items = items;
    }

    public List<Post> getItems() {
        return items;
    }

    public Post get(int position) {
        return  items.get(position);
    }

    public int size(){
        return this.items.size();
    }
}