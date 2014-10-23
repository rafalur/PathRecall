package com.rafal.pathrecall;



import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import static android.animation.Animator.AnimatorListener;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link GameBoardFragment#newInstance} factory method to
 * create zan instance of this fragment.
 *
 */
public class GameBoardFragment extends Fragment {
    public static final int COLUMNS_NUMBER = 10;
    public static final int PLAYP_PATH_COUNTDOWN_MAX = 3;

    private GameBoardGridView mMainGrid;
    private Button mClearButton;
    private Button mPlayPathButton;
    private ToggleButton mToggleButton;
    private TextView mInfoDescTextView;

    private int mPlayPathCountdownCounter;

    private GameManager mGameManager;
    private AnimatorSet mCountdownAnimatorSet;

    public static GameBoardFragment newInstance(String param1, String param2) {
        GameBoardFragment fragment = new GameBoardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGameManager = GameManager.instance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_game_board, container, false);
        findViews(root);
        confViews();

        return root;
    }

    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void findViews(View root){
        mMainGrid = (GameBoardGridView)root.findViewById(R.id.mainGrid);
        mClearButton = (Button)root.findViewById(R.id.clearButton);
        mPlayPathButton = (Button)root.findViewById(R.id.playPathButton);
        mToggleButton = (ToggleButton)root.findViewById(R.id.drawToggleButton);
        mToggleButton = (ToggleButton)root.findViewById(R.id.drawToggleButton);
        mInfoDescTextView = (TextView)root.findViewById(R.id.infoDescriptionTextView);
    }

    private void confViews() {
        mMainGrid.setNumColumns(COLUMNS_NUMBER);
        mMainGrid.setBoard(GameManager.instance().getBoard());
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBoard();
            }
        });

        mPlayPathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mCountdownAnimatorSet == null || !mCountdownAnimatorSet.isRunning())) {
                    mPlayPathCountdownCounter = PLAYP_PATH_COUNTDOWN_MAX;
                    playPlayPathCountdownAnim();
                }
            }
        });

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mGameManager.enableDrawing(mToggleButton.isChecked());
            }
        });

        mToggleButton.setChecked(true);
    }

    private void playPlayPathCountdownAnim() {

        if(mPlayPathCountdownCounter > 0) {
            mInfoDescTextView.setText(Integer.toString(mPlayPathCountdownCounter));
            ObjectAnimator animX = ObjectAnimator.ofFloat(mInfoDescTextView, "scaleX", 1.0f, 3.0f);
            ObjectAnimator animY = ObjectAnimator.ofFloat(mInfoDescTextView, "scaleY", 1.0f, 3.0f);
            ObjectAnimator animAlpha = ObjectAnimator.ofFloat(mInfoDescTextView, "alpha", 1.0f, 0.0f);

            mCountdownAnimatorSet = new AnimatorSet();
            mCountdownAnimatorSet.playTogether(animX, animY, animAlpha);
            mCountdownAnimatorSet.setDuration(300);
            mCountdownAnimatorSet.start();

                mCountdownAnimatorSet.addListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mPlayPathCountdownCounter--;
                        playPlayPathCountdownAnim();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
        else {
            mInfoDescTextView.setText("");
            mGameManager.playRandomPath();
        }
    }

    private void clearBoard() {
        GameManager.instance().getBoard().clear();
    }
}


