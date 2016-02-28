package com.vakoms.meshly.interfaces;

/**
 * Created by Oleh Makhobey on 12.06.2015.
 * tajcig@ya.ru
 */
public interface SearchFragmentListener {

    void onCountUpdate(int _count);

    void onRemoveFilters(boolean _isVisible);

    void onGetCountForSelectedRangeAndSkills();

}
