package com.rafal.pathrecall;

import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Path;


public class GameManager implements GameSession.GameSessionStatusListener {
    public static final int BOARD_SIZE = 10;

    private static GameManager mInstance;

    private Board mBoard;
    private Path mPath;
    private GameSession mCurrentGameSession;
    private GameStateListener mGameStatusListener;

    private boolean mIsDrawingEnabled;
    private PathPlayer mPlayer;

    private GameManager(){
        mBoard = new Board();
        mPlayer = new PathPlayer(mBoard);
        mPlayer.setStateListener(mPathPlayerListener);


    };

    public static synchronized GameManager instance(){
        if(mInstance == null)
            mInstance = new GameManager();
        return mInstance;
    }

    public void startGame() {
        loadGameSession(new GameSession());
        mCurrentGameSession.addGameStatusListener(this);
        mCurrentGameSession.init();
    }

    public Board getBoard() {
        return mBoard;
    }

    public void playRandomPath(){
        if(!mPlayer.isPathPlaying()){
            generateRandomPathAndPlayIt();
        }
    }

    private void generateRandomPathAndPlayIt(){
        mPath = Path.generateRandomPath(4);
        mPlayer.loadPath(mPath);
        mCurrentGameSession.setState(GameSession.GameState.PLAYING_PATH);
        mPlayer.playPath();
    }

    public void loadGameSession(GameSession session){
        mCurrentGameSession = session;
    }

    public void enableDrawing(boolean enable) {
        mIsDrawingEnabled = enable;
    }

    public boolean isDrawingEnabled() {
        return mIsDrawingEnabled;
    }

    @Override
    public void onGameStateChanged(GameSession.GameState newState, GameSession.GameState oldState) {
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
            mCurrentGameSession.setState(GameSession.GameState.USER_DRAW);
        }
    };

    public interface GameStateListener{
        public void OnGameSessionStateChanged(GameSession.GameState newState);
    }
}
