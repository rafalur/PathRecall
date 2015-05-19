package com.rafal.pathrecall.ui.fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.R;
import com.rafal.pathrecall.data.PathStats;
import com.rafal.pathrecall.engine.difficulty.Difficulty;
import com.rafal.pathrecall.engine.difficulty.DifficultyProfiler;
import com.rafal.pathrecall.engine.GameManager;
import com.rafal.pathrecall.engine.GameSession;
import com.rafal.pathrecall.modules.GameEngineModule;
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
import dagger.ObjectGraph;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link GameBoardFragment#newInstance} factory method to
 * create zan instance of this fragment.
 *
 */
public class GameBoardFragment extends BaseFragment {
    public static final int COLUMNS_NUMBER = 10;
    public static final String ARG_PLAYER_NAME = "player_name";
    public static final String ARG_DIFFICULTY = "difficulty";

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
    @InjectView(R.id.playerNameView)
    TextView mPlayerNameView;
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


    private boolean mBoardInitialAnimationPlayed;
    private String mPlayerName;

    public static GameBoardFragment newInstance(String playerName, Difficulty difficulty) {
        GameBoardFragment fragment = new GameBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PLAYER_NAME, playerName);
        bundle.putSerializable(ARG_DIFFICULTY, difficulty);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayerName = getArguments().getString(ARG_PLAYER_NAME);
        Difficulty difficulty = (Difficulty)getArguments().getSerializable(ARG_DIFFICULTY);
        ObjectGraph scopedGraph = ((PathRecallApp) getActivity().getApplication()).createScopedGraph(new GameEngineModule(mPlayerName, difficulty));
        scopedGraph.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((PathRecallApp) getActivity().getApplication()).restoreMainObjectGraph();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        mPlayerNameView.setText(mPlayerName);

        mMainGrid.getViewTreeObserver().addOnGlobalLayoutListener(mGridLayoutListener);
    }

    ViewTreeObserver.OnGlobalLayoutListener mGridLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if(!mBoardInitialAnimationPlayed && mMainGrid.getWidth() > 0 && mMainGrid.getHeight() > 0){
                mBoardInitialAnimationPlayed = true;
                mMainGrid.setVisibility(View.VISIBLE);
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
                        R.animator.board_init_scale_anim);
                set.setTarget(mMainGrid);
                set.setInterpolator(new OvershootInterpolator(1.2f));
                set.start();
            }
        }
    };

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


