package com.rafal.pathrecall.engine;

import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Path;
import com.rafal.pathrecall.data.PathStats;

import javax.inject.Inject;

public class GameManager implements GameSession.GameSessionStatusListener {

    @Inject Board mBoard;
    @Inject PathPlayer mPlayer;
    @Inject GameSession mCurrentGameSession;
    @Inject Player mCurrentPlayer;
    @Inject DifficultyProfiler mDifficultyProfiler;

    private Path mPath;
    private GameStateListener mGameStatusListener;

    private boolean mIsDrawingEnabled;

    public GameManager(){
        PathRecallApp.getObjectGraph().inject(this);
    };

    public void initializeGame() {
        mCurrentGameSession.addGameStatusListener(this);
        mCurrentGameSession.init();
        mDifficultyProfiler.resetToDefault();
        mPlayer.setStateListener(mPathPlayerListener);
        generateRandomPath(4);
    }

    public Board getBoard() {
        return mBoard;
    }

    public void playCurrentPath(){
        mPlayer.loadPath(mPath);
        mCurrentGameSession.setState(GameSession.GameState.PLAYING_PATH);
        mPlayer.playPath();
    }

    public int getLivesNumber(){
        return GameParameters.DEFAULT_LIVES_NUMBER;
    }

    private void prepareNextPath(){
        generateRandomPath(mDifficultyProfiler.getTurnsNumber() + 1);
    }

    private void generateRandomPath(int sectionsNumber){
        mPath = Path.generateRandomPath(sectionsNumber);
        notifyOnCurrentPathStatsChanged();
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

        int score = Refree.countAndAddPointsForPlayer(mCurrentPlayer, playerPath, mPath, mDifficultyProfiler);
        mCurrentGameSession.setCurrentRoundScore(score);

        mCurrentGameSession.setState(GameSession.GameState.REPLAY_VERIFY);
    }

    @Override
    public void onGameSessionStateChanged(GameSession.GameState newState, GameSession.GameState oldState) {
        enableDrawing(newState == GameSession.GameState.USER_DRAW);
        if(newState == GameSession.GameState.IDLE){
            mDifficultyProfiler.updateForLevel(mCurrentGameSession.getLevel());
            prepareNextPath();
        }
        notifyOnGameSessionStateChanged(newState);
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
                mCurrentGameSession.setState(GameSession.GameState.SCORE_PRESENTATION);

                notifyOnPointsReceived();
                mBoard.clear();
            }
        }
    };

    private void notifyOnGameSessionStateChanged(GameSession.GameState newState) {
        if(mGameStatusListener != null){
            mGameStatusListener.OnGameSessionStateChanged(newState);
        }
    }

    private void notifyOnCurrentPathStatsChanged() {
        if(mGameStatusListener != null){
            mGameStatusListener.OnCurrentPathStatsChanged(mPath.getStats());
        }
    }

    private void notifyOnPointsReceived() {
        if(mGameStatusListener != null){
            mGameStatusListener.OnPointsReceived(mCurrentGameSession.getCurrentRoundScore());
        }
    }

    private void notifyOnGameOver() {
        if(mGameStatusListener != null){
            mGameStatusListener.OnGameOver();
        }
    }

    public int getCurrentScore() {
        if(mCurrentPlayer != null) {
            return mCurrentPlayer.getScore();
        }
        return 0;
    }

    public int getPlayerLostLives(){
        return mCurrentPlayer.getLostLives();
    }

    public int getCurrentLevel(){
        return mCurrentGameSession.getLevel();
    }

    public DifficultyProfiler getCurrentDifficultyProfiler() {
        return mDifficultyProfiler;
    }

    public void playCurrentPathForVerification() {
        mPlayer.playPath();
    }

    public void onScorePresentationFinished() {
        mCurrentGameSession.setState(GameSession.GameState.IDLE);

        if(isGameOver()){
            notifyOnGameOver();
        }
    }
    
    public boolean isGameOver(){
        return mCurrentPlayer.getLostLives() >= GameParameters.DEFAULT_LIVES_NUMBER;
    }

    public interface GameStateListener{
        public void OnGameSessionStateChanged(GameSession.GameState newState);
        public void OnCurrentPathStatsChanged(PathStats pathStats);
        public void OnPointsReceived(int score);
        public void OnGameOver();
    }
}
