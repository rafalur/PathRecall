package com.rafal.pathrecall.engine;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameSession {

    public enum GameState{
        IDLE,
        PLAYING_PATH,
        USER_DRAW,
        REPLAY_VERIFY,
        SCORE_PRESENTATION
    }

    @Inject
    public GameSession(){
    }

    private GameState mState;
    private List<GameSessionStatusListener> mGameStatusListeners;
    private int mLevel;
    private int mCurrentRoundScore;

    public void init(){
        mLevel = 1;
        setState(GameState.IDLE);
    }

    public void reset() {
        init();
    }

    public GameState getState() {
        return mState;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setState(GameState state) {
        if(state != mState){
            if(mState == GameState.SCORE_PRESENTATION && state == GameState.IDLE){
                mLevel++;
            }
            this.mState = state;
            notifyStatusListeners(mState, state);
        }
    }

    private void notifyStatusListeners(GameState newState, GameState oldState) {
        if(mGameStatusListeners != null) {
            for (GameSessionStatusListener listener : mGameStatusListeners) {
                listener.onGameStateChanged(newState, oldState);
            }
        }
    }

    public void addGameStatusListener(GameSessionStatusListener listener){
        if(mGameStatusListeners == null)
            mGameStatusListeners = new ArrayList<GameSessionStatusListener>();
        mGameStatusListeners.add(listener);
    }

    public interface GameSessionStatusListener {
        public void onGameStateChanged(GameState newState, GameState oldState);
    }

    public int getCurrentRoundScore() {
        return mCurrentRoundScore;
    }

    public void setCurrentRoundScore(int mCurrentRoundScore) {
        this.mCurrentRoundScore = mCurrentRoundScore;
    }

}
