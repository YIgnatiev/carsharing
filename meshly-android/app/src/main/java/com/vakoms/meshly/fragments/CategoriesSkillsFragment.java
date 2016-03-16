package com.vakoms.meshly.fragments;

import android.app.Fragment;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vakoms.meshly.models.skill.Skill;
import com.vakoms.meshly.utils.KeyboardUtil;
import com.vakoms.meshly.utils.P;

import java.util.ArrayList;
import java.util.List;

import meshly.vakoms.com.meshly.R;

/**
 * Created by taras.melko on 08.09.2014.
 */
public class CategoriesSkillsFragment extends Fragment implements View.OnClickListener {

    RelativeLayout containerLayout;
    AutoCompleteTextView autoCompleteSkill;
    LayoutInflater inflater;
    private ArrayAdapter<String> adapter;
    List<Skill> skillList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_add_event_skills, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        containerLayout = (RelativeLayout) view.findViewById(R.id.categories_skills_container_rl);
        autoCompleteSkill = (AutoCompleteTextView) view.findViewById(R.id.categories_autocomplete_skill_tv);
        generateskillList();
        initSkillLayout();
        String[] availableSkills = getResources().getStringArray(R.array.skillsList);


        adapter = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_list_item_1, availableSkills);
        autoCompleteSkill.setAdapter(adapter);
        autoCompleteSkill.setThreshold(1);

        autoCompleteSkill.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addSkill(autoCompleteSkill.getText().toString());
                }
                return false;
            }
        });

        view.findViewById(R.id.categories_skills_done_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSkillList();
                getActivity().onBackPressed();
            }
        });

        view.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void addSkill(String s) {
        if (s.equals("")) {
            return;
        } else {
            boolean isEqualSkill = false;
            for (int i = 0; i < skillList.size(); i++)
                if (s.equals(skillList.get(i).getName())) isEqualSkill = true;
            if (!isEqualSkill) {
                Skill item = new Skill();
                item.setName(s);
                skillList.add(item);
                initSkillLayout();
                autoCompleteSkill.setText("");
            } else {

            }
        }
    }

    private void removeSkill(int num) {
        skillList.remove(num);
        initSkillLayout();
    }

    private void initSkillLayout() {
        if (containerLayout != null)
            containerLayout.removeAllViews();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        int itemHeight = DpToPx(30.0f);
        int itemWidth;
        int dy = DpToPx(10.0f);
        int dx = DpToPx(5.0f);
        int left = dx, top = dy;
        for (int i = 0; i < skillList.size(); i++) {

            itemWidth = getStringWidth(skillList.get(i).getName());
            RelativeLayout.LayoutParams linLayoutParam =
                    new RelativeLayout.LayoutParams(itemWidth, itemHeight);

            RelativeLayout rLayout = (RelativeLayout) inflater.inflate(R.layout.item_skill, containerLayout, false);
            if (itemWidth + dx + left + dx > width) {
                left = dx;
                top += itemHeight + dy;
            }
            linLayoutParam.setMargins(left, top, 0, 0);

            TextView skillName = (TextView) rLayout.findViewById(R.id.item_skill_description_tv);
            skillName.setText(skillList.get(i).getName());
            TextView cancelImage = (TextView) rLayout.findViewById(R.id.item_skill_cancel_iv);

            final int[] currentNum = new int[]{i};

            cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeSkill(currentNum[0]);
                }
            });
            containerLayout.addView(rLayout, linLayoutParam);

            left += itemWidth + dx;
        }
    }

    private int getStringWidth(String name) {

        int result;

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(18 * getActivity().getApplicationContext().getResources().getDisplayMetrics().scaledDensity);
        mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        result = (int) mPaint.measureText(name, 0, name.length());

        result += DpToPx(35.0f);
        return result;
    }

    int DpToPx(float dp) {
        float scale = getActivity().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void generateskillList() {
        String str = P.getAddEventsSkills();
        String[] strArray = str.split(", ");
        skillList = new ArrayList<Skill>();
        for (int i = 0; i < strArray.length; i++) {
            if (!strArray[i].equals("")) {
                Skill item = new Skill();
                item.setName(strArray[i]);
                skillList.add(item);
            }
        }
    }

    private void saveSkillList() {
        StringBuilder skillBuilder = new StringBuilder();

        for (int i = 0; i < skillList.size(); i++) {
            skillBuilder.append(skillList.get(i).getName());
            if (i < skillList.size() - 1) skillBuilder.append(", ");
        }

        P.saveAddEventsSkills(skillBuilder.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onResume() {
        KeyboardUtil.showKeyBoard(autoCompleteSkill, getActivity());
        super.onResume();
    }

    @Override
    public void onPause() {
        KeyboardUtil.hideKeyBoard(autoCompleteSkill, getActivity());
        super.onPause();

    }

}
