package com.rafal.pathrecall.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rafal.pathrecall.engine.DifficultyProfiler;
import com.rafal.pathrecall.engine.GameManager;
import com.rafal.pathrecall.engine.GameSession;
import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;
import com.rafal.pathrecall.data.PathStats;
import com.rafal.pathrecall.ui.dialogs.GameOverDialog;
import com.rafal.pathrecall.ui.utils.ScoreAnimator;
import com.rafal.pathrecall.ui.views.GameBoardGridView;
import com.rafal.pathrecall.ui.views.PlayerLifeView;
import com.rafal.pathrecall.ui.views.RemainingLivesView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link GameBoardFragment#newInstance} factory method to
 * create zan instance of this fragment.
 *
 */
public class GameBoardFragment extends BaseFragment {
    public static final int COLUMNS_NUMBER = 10;

    @InjectView(R.id.mainGrid)
    GameBoardGridView mMainGrid;
    @InjectView(R.id.infoDescriptionTextView)
    TextView mInfoDescTextView;
    @InjectView(R.id.bottomButtonsBar)
    RelativeLayout mBottomButtonsLayout;

    @InjectView(R.id.idleButtonsLayout)
    RelativeLayout mIdleButtonsLayout;
    @InjectView(R.id.verifyButtonsLayout)
    RelativeLayout mVerifyButtonsLayout;
    @InjectView(R.id.boardFrameLayout)
    FrameLayout mBoardFrameLayout;

    @InjectView(R.id.turnsNumberValueView)
    TextView mTurnsNumberTextView;
    @InjectView(R.id.pointsToGetValueView)
    TextView mPointsToGetTextView;
    @InjectView(R.id.hitPointsValueView)
    TextView mHitPoitsTextView;
    @InjectView(R.id.missPointsValueView)
    TextView mMissPoitsTextView;
    @InjectView(R.id.scoreTextView)
    TextView mScoreTextView;
    @InjectView(R.id.levelTextView)
    TextView mLevelTextView;
    @InjectView(R.id.scoreFloatingView)
    TextView mScoreFloatingTextView;

    @InjectView(R.id.remainingLivesLayout)
    RemainingLivesView mRemainingLivesLayout;

    @Inject
    GameManager mGameManager;

    public static GameBoardFragment newInstance() {
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
        ButterKnife.inject(this, root);
        confViews();

        mGameManager.initializeGame();

        return root;
    }

    private void confViews() {
        mMainGrid.setNumColumns(COLUMNS_NUMBER);
        mGameManager.setGameStatusListener(mGameStateListener);
        mScoreFloatingTextView.setAlpha(0.0f);
        mRemainingLivesLayout.setLivesNumber(mGameManager.getLivesNumber());
        selectRemainingLivesViews();
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
                    DifficultyProfiler profiler = mGameManager.getCurrentDifficultyProfiler();

                    mHitPoitsTextView.setText(Integer.toString(profiler.getHitPoints()));
                    mMissPoitsTextView.setText(Integer.toString(profiler.getMissPoints()));
                    mTurnsNumberTextView.setText(Integer.toString(profiler.getTurnsNumber()));
                    mPointsToGetTextView.setText(Integer.toString(pathStats.getPointsToGet(profiler)));
                }
            });
        }

        @Override
        public void OnPointsReceived(final int score) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playScoreFloatingAnimation(score);
                    selectRemainingLivesViews();
                }
            });
        }

        @Override
        public void OnGameOver() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showGameOverDialog();
                }
            });
        }
    };

    private void showGameOverDialog() {
        String title = getString(R.string.game_over);
        String subtitle = String.format(getString(R.string.game_over_subtitle), mGameManager.getCurrentScore());
        GameOverDialog dialog = new GameOverDialog(getActivity(), title , subtitle);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // TODO: Go to menu view
            }
        });
        dialog.show();
    }

    private void selectRemainingLivesViews() {
        for(int i =0; i < mRemainingLivesLayout.getChildCount(); i++){
            PlayerLifeView view = (PlayerLifeView) mRemainingLivesLayout.getChildAt(i);
            view.setSelected(i < mGameManager.getLivesNumber() - mGameManager.getPlayerLostLives());
        }
    }

    private void configureViewsForState(GameSession.GameState newState) {
        Log.d("path", "Game sesson state changed: " + newState);
        hideAllButtonsBars();
        switch (newState) {
            case IDLE:
                mInfoDescTextView.setText(R.string.idle_state_description);
                mIdleButtonsLayout.setVisibility(View.VISIBLE);
                mScoreTextView.setText(Integer.toString(mGameManager.getCurrentScore()));
                mLevelTextView.setText(String.format(getString(R.string.level), mGameManager.getCurrentLevel()));
                break;
            case PLAYING_PATH:
                mInfoDescTextView.setText(R.string.playing_path_state_description);
                break;
            case USER_DRAW:
                mInfoDescTextView.setText(R.string.draw_state_description);
                mVerifyButtonsLayout.setVisibility(View.VISIBLE);
                break;
            case REPLAY_VERIFY:
                mInfoDescTextView.setText(R.string.replay_state_description);
                int fadeOutDuration = getResources().getInteger(R.integer.user_selection_fade_out_duration);
                int pathReplayDelay = getResources().getInteger(R.integer.path_replay_delay);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGameManager.playCurrentPathForVerification();
                    }
                }, fadeOutDuration + pathReplayDelay);
                break;
        }
    }

    private void playScoreFloatingAnimation(int score) {;
        String scoreString = Integer.valueOf(score).toString();
        if (score > 0) {
            scoreString = "+" + scoreString;
        }
        mScoreFloatingTextView.setText(scoreString);
        ScoreAnimator scoreAnimator = new ScoreAnimator();
        scoreAnimator.setAnimationListener(new ScoreAnimator.ScoreAnimationListener() {
            @Override
            public void onAnimationEnd() {
                mGameManager.onScorePresentationFinished();
            }
        });
        scoreAnimator.playFloatInOutAnimation(mScoreFloatingTextView, getActivity());
    }

    private void hideAllButtonsBars() {
        for (int i = 0; i < mBottomButtonsLayout.getChildCount(); i++) {
            mBottomButtonsLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
    }

    @OnTouch(R.id.gameBoardFragmentRoot)
    public boolean handleTouchOnRoot(View view, MotionEvent motionEvent) {
        motionEvent.offsetLocation(-mBoardFrameLayout.getX(), -mBoardFrameLayout.getY());
        return mMainGrid.onTouchEvent(motionEvent);
    }

    @OnClick({ R.id.playPathButton, R.id.clearButton, R.id.verifyPathButton })
    protected void handleClicks(Button button) {
        switch (button.getId()) {
            case R.id.playPathButton:
                mGameManager.playCurrentPath();
                break;
            case R.id.clearButton:
                mGameManager.clearBoard();
                break;
            case R.id.verifyPathButton:
                mGameManager.verifyPath();
                break;
        }
    }
}


