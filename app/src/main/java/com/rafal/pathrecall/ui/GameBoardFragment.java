package com.rafal.pathrecall.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rafal.pathrecall.GameManager;
import com.rafal.pathrecall.GameSession;
import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;
import com.rafal.pathrecall.data.PathStats;
import com.rafal.pathrecall.ui.views.GameBoardGridView;

import javax.inject.Inject;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link GameBoardFragment#newInstance} factory method to
 * create zan instance of this fragment.
 *
 */
public class GameBoardFragment extends Fragment {
    public static final int COLUMNS_NUMBER = 10;

    private GameBoardGridView mMainGrid;
    private TextView mInfoDescTextView;
    private FrameLayout mBottomButtonsLayout;

    private RelativeLayout mPlayPathButtonsLayout;
    private RelativeLayout mIdleButtonsLayout;
    private RelativeLayout mVerifyButtonsLayout;

    private TextView mTurnsNumberTextView;
    private TextView mPointsToGetTextView;
    private TextView mScoreTextView;
    private TextView mLevelTexteView;

    @Inject
    GameManager mGameManager;

    public static GameBoardFragment newInstance(String param1, String param2) {
        GameBoardFragment fragment = new GameBoardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PathRecallApp.getObjectGraph().inject(this);

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
        mInfoDescTextView = (TextView)root.findViewById(R.id.infoDescriptionTextView);
        mBottomButtonsLayout = (FrameLayout)root.findViewById(R.id.bottomButtonsBar);
        mPlayPathButtonsLayout = (RelativeLayout)root.findViewById(R.id.playPathButtonsLayout);
        mIdleButtonsLayout = (RelativeLayout)root.findViewById(R.id.idleButtonsLayout);
        mVerifyButtonsLayout = (RelativeLayout)root.findViewById(R.id.verifyButtonsLayout);
        root.findViewById(R.id.playPathButton).setOnClickListener(mClickListener);
        root.findViewById(R.id.clearButton).setOnClickListener(mClickListener);
        root.findViewById(R.id.verifyPathButton).setOnClickListener(mClickListener);
        root.findViewById(R.id.undoButton).setOnClickListener(mClickListener);

        root.findViewById(R.id.gameBoardFragmentRoot).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("Touch", "Board layout event, event: " + motionEvent);
                motionEvent.offsetLocation(-mMainGrid.getX(), -mMainGrid.getY());
                return mMainGrid.onTouchEvent(motionEvent);
            }
        });

        //TODO: make info bar separate view
        mTurnsNumberTextView = (TextView)root.findViewById(R.id.turnsNumberValueView);
        mPointsToGetTextView = (TextView)root.findViewById(R.id.pointsToGetValueView);
        mScoreTextView = (TextView)root.findViewById(R.id.scoreTextView);
        mLevelTexteView = (TextView)root.findViewById(R.id.levelTextView);
    }

    private void confViews() {
        mMainGrid.setNumColumns(COLUMNS_NUMBER);

        mGameManager.setGameStatusListener(mGameStateListener);
        mGameManager.initializeGame();
    }

    private void resetInfoDescTextView() {
        mInfoDescTextView.setAlpha(1.0f);
        mInfoDescTextView.setScaleX(1.0f);
        mInfoDescTextView.setScaleY(1.0f);
    }

    private GameManager.GameStateListener mGameStateListener = new GameManager.GameStateListener() {
        @Override
        public void OnGameSessionStateChanged(final GameSession.GameState newState) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    configureViewsForState(newState);
                }
            });
        }

        @Override
        public void OnCurrentPathStatsChanged(final PathStats pathStats) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTurnsNumberTextView.setText(Integer.toString(pathStats.getTurnsNumber()));
                    mPointsToGetTextView.setText(Integer.toString(pathStats.getPointsToGet()));
                }
            });
        }
    };

    private void configureViewsForState(GameSession.GameState newState) {
        Log.d("path", "Game sesson state changed: " + newState);
        hideAllButtonsBars();
        switch (newState) {
            case IDLE:
                mInfoDescTextView.setText(R.string.idle_state_description);
                mIdleButtonsLayout.setVisibility(View.VISIBLE);
                mScoreTextView.setText(Integer.toString(mGameManager.getCurrentScore()));
                mLevelTexteView.setText(String.format(getString(R.string.level), mGameManager.getCurrentLevel()));
                break;
            case PLAYING_PATH:
                mInfoDescTextView.setText(R.string.playing_path_state_description);
                mPlayPathButtonsLayout.setVisibility(View.VISIBLE);
                break;
            case USER_DRAW:
                mInfoDescTextView.setText(R.string.draw_state_description);
                mVerifyButtonsLayout.setVisibility(View.VISIBLE);
                break;
            case REPLAY_VERIFY:
                //mMainGrid.setSelectionFadedOut();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGameManager.playCurrentPathForVerification();
                    }
                }, 2000);
                break;
        }
    }

    private void hideAllButtonsBars() {
        for (int i = 0; i < mBottomButtonsLayout.getChildCount(); i++) {
            mBottomButtonsLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.playPathButton:
                    mGameManager.playCurrentPath();
                break;
                case R.id.clearButton:
                    mGameManager.clearBoard();
                    break;
                case R.id.undoButton:

                    break;
                case R.id.verifyPathButton:
                    mGameManager.verifyPath();
                    break;
            }
        }
    };
}


