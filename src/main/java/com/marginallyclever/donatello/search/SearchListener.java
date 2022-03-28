package com.marginallyclever.donatello.search;

/**
 * A listener for search events from the {@link SearchBar}.
 */
public interface SearchListener {
    void searchFor(String search,boolean caseSensitive);
}
