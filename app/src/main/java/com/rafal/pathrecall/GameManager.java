package com.rafal.pathrecall;

import android.os.Handler;
import android.util.Log;

import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Path;
import com.rafal.pathrecall.data.Point;

import java.util.Timer;
import java.util.TimerTask;


public class GameManager {
    public static final int BOARD_SIZE = 10;

    private static GameManager mInstance;

    private Board mBoard;
    private Path mPath;
    private GameSession mCurrentGameSession;

    private boolean mIsDrawingEnabled;
    private PathPlayer mPlayer;

    private GameManager(){
        mBoard = new Board();
        mPlayer = new PathPlayer(mBoard);
    };

    public static synchronized GameManager instance(){
        if(mInstance == null)
            mInstance = new GameManager();
        return mInstance;
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
}
