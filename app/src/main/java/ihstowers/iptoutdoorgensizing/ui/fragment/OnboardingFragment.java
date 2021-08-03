package ihstowers.iptoutdoorgensizing.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import ihstowers.iptoutdoorgensizing.R;
import ihstowers.iptoutdoorgensizing.ui.onboarding.CircleIndicatorView;
import ihstowers.iptoutdoorgensizing.ui.onboarding.OnboardingItem;
import ihstowers.iptoutdoorgensizing.ui.onboarding.OnboardingPagerAdapter;

public class OnboardingFragment extends Fragment {
    ViewPager screenPager;
    OnboardingPagerAdapter onBoardingPagerAdapter;

    CircleIndicatorView circleIndicatorView;

    ImageButton ibNext;
    Button btnSkip;
    Button btnFinish;
    FrameLayout buttonsLayout;
    View divider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_onboarding, container, false);
//TODO
     /*   prefsManager= CommonPrefsManager_.getInstance_(getActivity());
        if (!prefsManager.isFirstLaunch()) {
            NavHostFragment.findNavController(this).navigate(R.id.action_global_nav_gen_sizing);
        }*/

        circleIndicatorView = rootView.findViewById(R.id.circle_indicator_view);
        ibNext = rootView.findViewById(R.id.ib_next);
        btnSkip = rootView.findViewById(R.id.btn_skip);
        btnFinish = rootView.findViewById(R.id.btn_finish);
        buttonsLayout = rootView.findViewById(R.id.buttons_layout);
        divider = rootView.findViewById(R.id.divider);

        final List<OnboardingItem> mList = new ArrayList<>();
        mList.add(new OnboardingItem(R.string.onboarding_message, R.drawable.ic_onboarding_icons_01));

        // setup viewpager

        onBoardingPagerAdapter = new OnboardingPagerAdapter(getContext(), mList);
        screenPager = rootView.findViewById(R.id.vp_onboarder_pager);
        screenPager.setAdapter(onBoardingPagerAdapter);

        circleIndicatorView.setPageIndicators(mList.size());

        // setup tablayout with viewpager


        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // addBottomDots(position);
                circleIndicatorView.setCurrentPage(position);
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == mList.size() - 1) {
                    // last page. make button text to GOT IT
                    loaddLastScreen();
                } else {
                    // still pages are left
                    loaddScreen();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        btnSkip.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_global_nav_gen_sizing));
        btnFinish.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_global_nav_gen_sizing));

        ibNext.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < mList.size()) {
                // move to next screen
                screenPager.setCurrentItem(current);
            } else {
                NavHostFragment.findNavController(this).navigate(R.id.action_global_nav_gen_sizing);
            }
        });
        return rootView;
    }


    private void loaddScreen() {
        btnFinish.setVisibility(View.GONE);
        ibNext.setVisibility(View.VISIBLE);
        btnSkip.setVisibility(View.VISIBLE);
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        ibNext.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.VISIBLE);
        btnSkip.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO prefsManager.setFirstLaunch(false);

    }

    private int getItem(int i) {
        return screenPager.getCurrentItem() + i;
    }

}
