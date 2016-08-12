package youdrive.today.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import rx.Observer;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.adapters.EmailAdapter;
import youdrive.today.adapters.PhoneNumberAdapter;
import youdrive.today.databinding.ActivityInviteBinding;
import youdrive.today.helpers.AppUtils;
import youdrive.today.models.ReferralRules;
import youdrive.today.network.ApiClient;
import youdrive.today.response.BaseResponse;

import static android.provider.ContactsContract.CommonDataKinds;
import static android.provider.ContactsContract.Contacts;
import static android.provider.ContactsContract.RawContacts;

public class InviteActivity extends BaseActivity implements PhoneNumberAdapter.OnInviteClickListener {

    public final static int MODE_SMS = 1;
    public final static int MODE_EMAIL = 2;
    private ActivityInviteBinding b;
    public ApiClient mClient;
    private EmailAdapter emailAdapter;
    private PhoneNumberAdapter phoneNumberAdapter;
    private ReferralRules referralRules;

    private int mode;

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this, R.layout.activity_invite);
        b.setListener(this);
        mClient = new ApiClient();
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        referralRules = getIntent().getParcelableExtra("referralData");
        setData();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setData() {
        mode = getIntent().getIntExtra("mode", MODE_EMAIL);
        getReadContactsPermission(() ->
        {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            b.recyclerView.setHasFixedSize(true);

            b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            b.etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    fillRecyclerView();
                }
            });
            if (mode == MODE_EMAIL) {
                b.recyclerView.setAdapter(emailAdapter = new EmailAdapter(getEmailContacts(null)));
                setTitle(R.string.invite_email);
            } else {
                b.recyclerView.setAdapter(phoneNumberAdapter = new PhoneNumberAdapter(getPhones(null), this));
                setTitle(R.string.invite_sms);
            }
        });

    }

    private void fillRecyclerView() {
        if (mode == MODE_EMAIL)
            emailAdapter.setData(getEmailContacts(b.etSearch.getText().toString().toLowerCase()));
        else phoneNumberAdapter.setData(getPhones(b.etSearch.getText().toString().toLowerCase()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mode == MODE_EMAIL) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_send_email, menu);
            return true;
        } else return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.send_email: {
                mClient.inviteUsersEmail(emailAdapter.getInvites()).subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), R.string.invitation_send_fail, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.isSuccess()) {
                            Toast.makeText(getApplicationContext(), R.string.invitation_send_success, Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.invitation_send_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static ArrayList<EmailAdapter.EmailContact> contacts = null;

    private ArrayList<EmailAdapter.EmailContact> getEmailContacts(String searchString) {
        if (contacts == null) {
            contacts = new ArrayList<>();
            HashSet<String> emlRecsHS = new HashSet<>();
            ContentResolver cr = getContentResolver();
            String[] PROJECTION = new String[]{RawContacts._ID,
                    Contacts.DISPLAY_NAME,
                    Contacts.PHOTO_ID,
                    CommonDataKinds.Email.DATA,
                    CommonDataKinds.Photo.CONTACT_ID};
            String order = "CASE WHEN "
                    + Contacts.DISPLAY_NAME
                    + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
                    + Contacts.DISPLAY_NAME
                    + ", "
                    + CommonDataKinds.Email.DATA
                    + " COLLATE NOCASE";
            String filter = CommonDataKinds.Email.DATA + " NOT LIKE ''";
            Cursor cur = cr.query(CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        // names comes in hand sometimes
                        String name = cur.getString(1);
                        String emlAddr = cur.getString(3);

                        // keep unique only
                        if (emlRecsHS.add(emlAddr.toLowerCase()))
                            contacts.add(new EmailAdapter.EmailContact(name, emlAddr));
                    } while (cur.moveToNext());
                }
                cur.close();
            }
        }

        if (TextUtils.isEmpty(searchString) || contacts == null) return contacts;
        else {
            ArrayList<EmailAdapter.EmailContact> filteredContacts = new ArrayList<>();
            final int size = contacts.size();
            for (int i = 0; i < size; ++i) {
                EmailAdapter.EmailContact emailContact = contacts.get(i);
                if (emailContact.name.toLowerCase().contains(searchString) || emailContact.email.toLowerCase().contains(searchString))
                    filteredContacts.add(emailContact);
            }
            return filteredContacts;
        }
    }

    private ArrayList<PhoneNumberAdapter.PhoneContact> phoneContacts = null;

    private ArrayList<PhoneNumberAdapter.PhoneContact> getPhones(String searchString) {
        if (phoneContacts == null) {
            phoneContacts = new ArrayList<>();
            HashSet<String> emlRecsHS = new HashSet<>();
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
            if (cursor != null) {
                if (cursor.moveToFirst()) {

                    do {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (pCur != null) {
                                while (pCur.moveToNext()) {
                                    String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    if (emlRecsHS.add(contactNumber))
                                        phoneContacts.add(new PhoneNumberAdapter.PhoneContact(name, contactNumber));
                                    break;
                                }
                                pCur.close();
                            }
                        }

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(searchString) || phoneContacts == null) return phoneContacts;
        else {
            ArrayList<PhoneNumberAdapter.PhoneContact> filteredPhoneContacts = new ArrayList<>();
            final int size = phoneContacts.size();
            for (int i = 0; i < size; ++i) {
                PhoneNumberAdapter.PhoneContact phoneContact = phoneContacts.get(i);
                if (phoneContact.name.toLowerCase().contains(searchString) || phoneContact.phone.contains(searchString))
                    filteredPhoneContacts.add(phoneContact);
            }
            return filteredPhoneContacts;
        }
    }

    public void onSearchClear(View view) {
        b.etSearch.setText("");
        fillRecyclerView();
    }

    /***
     * Клик по кнопке в списке телефонов
     */
    @Override
    public void onClick(PhoneNumberAdapter.PhoneContact phoneContact) {
        Uri uri = Uri.parse("smsto:" + phoneContact.phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", AppUtils.getShareText(this, referralRules));
        startActivity(it);
    }
}
