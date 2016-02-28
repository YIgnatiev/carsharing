package com.maryjorapini.soulintention;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Виталий on 09/10/2014.
 */
public abstract class FragmentReplacer {

    public static final void popBackStack(final FragmentActivity _activity) {
        _activity.getSupportFragmentManager().popBackStack();
    }

    public static final boolean isEmpty(final FragmentActivity _activity) {
        return _activity.getSupportFragmentManager().getBackStackEntryCount() == 0;
    }
    public static final void replaceTopNavigationFragment(final FragmentActivity _activity,
                                                          final Fragment _fragment) {
        _activity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        _activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer_AM, _fragment)
                .commit();
    }

    public static final void replaceFragmentWithoutBackStack(final FragmentActivity _activity,
                                                             final Fragment _fragment) {
        if ( _activity.getSupportFragmentManager().getFragments() == null ||
                !((Object) _activity.getSupportFragmentManager().getFragments().get(0)).getClass().getName().
                        equals(((Object)_fragment).getClass().getName())) {
            _activity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            _activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainer_AM, _fragment)
                    .commit();

        }
    }

    public static final void replaceCurrentFragment(final FragmentActivity _activity,
                                                    final Fragment _fragment) {
        _activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer_AM, _fragment)
                .addToBackStack(((Object) _fragment).getClass().getSimpleName()).commit();

    }

    public static final void replaceFragmentWithAnim(final FragmentActivity _activity,
                                                     final Fragment _fragment) {
        _activity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations( R.anim.translate_out_to_top,


                        R.anim.translate_from_top)
                .replace(R.id.flContainer_AM, _fragment)
                .addToBackStack(((Object) _fragment).getClass().getSimpleName()).commit();
//        manageBackButton(_activity, true);
    }

    public static final void addFragment(final FragmentActivity _context,
                                         final Fragment _fragment) {

        _context.getSupportFragmentManager().beginTransaction()
                .add(R.id.flContainer_AM, _fragment)
                .commit();
    }

    public static final void manageBackButton(final FragmentActivity _activity, final boolean _doNeedToShow) {
        if (_activity instanceof MainFragmentActivity) {
            ((MainFragmentActivity) _activity).showBackButton(_doNeedToShow);
        }
    }

    public static final void manageLikeButton(final FragmentActivity _activity, final boolean _doNeedToShow) {
        if (_activity instanceof MainFragmentActivity) {
            ((MainFragmentActivity) _activity).showLikeButton(_doNeedToShow);
        }
    }
    public static final void clearBackStack(final FragmentActivity _activity) {
        _activity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
