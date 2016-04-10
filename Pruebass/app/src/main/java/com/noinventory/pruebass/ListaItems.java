package com.noinventory.pruebass;

import java.util.List;

/**
 * Created by hugo on 6/04/16.
 */
public class ListaItems {
     // Encapsulamiento de Posts
        private List<Item> items;

        public ListaItems(List<Item> items) {
            this.items = items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public List<Item> getItems() {
            return items;
        }

}
