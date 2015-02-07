package com.rafal.pathrecall;

import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Path;
import com.rafal.pathrecall.data.PathStats;
import com.rafal.pathrecall.data.Player;
import com.rafal.pathrecall.data.Refree;

import javax.inject.Inject;

public class GameManager implements GameSession.GameSessionStatusListener {
    public static final int BOARD_SIZE = 10;

    @Inject Board mBoard;
    @Inject PathPlayer mPlayer;
    @Inject GameSession mCurrentGameSession;
    @Inject Player mCurrentPlayer;

    private Path mPath;
    private GameStateListener mGameStatusListener;

    private boolean mIsDrawingEnabled;

    public GameManager(){
        PathRecallApp.getObjectGraph().inject(this);
    };

    public void initializeGame() {
        mCurrentGameSession.addGameStatusListener(this);
        mCurrentGameSession.init();
        mPlayer.setStateListener(mPathPlayerListener);
        generateRandomPath(4);
    }

    public Board getBoard() {
        return mBoard;
    }

    public void playRandomPath(){
        if(!mPlayer.isPathPlaying()){
            generateRandomPathAndPlayIt(4);
        }
    }

    public void playCurrentPath(){
        mPlayer.loadPath(mPath);
        mCurrentGameSession.setState(GameSession.GameState.PLAYING_PATH);
        mPlayer.playPath();
    }

    private void prepareNextPath(){
        generateRandomPath(4);
    }

    private void generateRandomPathAndPlayIt(int turnsNumber){
        generateRandomPath(turnsNumber);
        playCurrentPath();
    }

    private void generateRandomPath(int turnsNumber){
        mPath = Path.generateRandomPath(turnsNumber);
        if(mGameStatusListener != null){
            mGameStatusListener.OnCurrentPathStatsChanged(mPath.getStats());
        }
    }

    public void enableDrawing(boolean enable) {
        mIsDrawingEnabled = enable;
    }

    public boolean isDrawingEnabled() {
        return mIsDrawingEnabled;
    }

    public void clearBoard() {
        mBoard.clear();
    }

    public void verifyPath(){
        Path playerPath = mBoard.generatePathFromBoardSelection();

        mBoard.clearSelectionLeavingLastStateFadedOut();

        Refree.countAndAddPointsForPlayer(mCurrentPlayer, playerPath, mPath);

        mCurrentGameSession.setState(GameSession.GameState.REPLAY_VERIFY);
    }

    @Override
    public void onGameStateChanged(GameSession.GameState newState, GameSession.GameState oldState) {
        enableDrawing(newState == GameSession.GameState.USER_DRAW);
        if(newState == GameSession.GameState.IDLE){
            prepareNextPath();
        }
        if(mGameStatusListener != null){
            mGameStatusListener.OnGameSessionStateChanged(newState);
        }
    }

    public void setGameStatusListener(GameStateListener gameStatusListener) {
        this.mGameStatusListener = gameStatusListener;
    }

    private PathPlayer.PathPlayerStateListener mPathPlayerListener = new PathPlayer.PathPlayerStateListener() {
        @Override
        public void onPathPlayFinished() {
            if(mCurrentGameSession.getState() == GameSession.GameState.PLAYING_PATH) {
                mCurrentGameSession.setState(GameSession.GameState.USER_DRAW);
            }
            else if (mCurrentGameSession.getState() == GameSession.GameState.REPLAY_VERIFY){
                mCurrentGameSession.setState(GameSession.GameState.IDLE);
                mBoard.clear();
            }
        }
    };

    public int getCurrentScore() {
        if(mCurrentPlayer != null) {
            return mCurrentPlayer.getScore();
        }
        return 0;
    }

    public int getCurrentLevel(){
        return mCurrentGameSession.getLevel();
    }

    public void playCurrentPathForVerification() {
        mPlayer.playPath();
    }

    public interface GameStateListener{
        public void OnGameSessionStateChanged(GameSession.GameState newState);
        public void OnCurrentPathStatsChanged(PathStats pathStats);
    }
}
