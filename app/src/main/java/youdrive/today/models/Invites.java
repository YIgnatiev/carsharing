package youdrive.today.models;

import java.util.ArrayList;

import youdrive.today.adapters.EmailAdapter;

/**
 * Created by Leonov Oleg, http://pandorika-it.com on 28.03.16.
 */
public class Invites {
    ArrayList<EmailAdapter.EmailContact> invitees;

    public Invites(ArrayList<EmailAdapter.EmailContact> invitees) {
        this.invitees = invitees;
    }
}
