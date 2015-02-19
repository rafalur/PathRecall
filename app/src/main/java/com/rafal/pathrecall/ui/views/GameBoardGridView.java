package com.rafal.pathrecall.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.rafal.pathrecall.engine.GameManager;
import com.rafal.pathrecall.PathRecallApp;
import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Brick;
import com.rafal.pathrecall.ui.utils.BoardDrawingOrderHelper;
import com.rafal.pathrecall.ui.utils.PathDrawHandler;
import com.rafal.pathrecall.ui.utils.UiUtils;

import javax.inject.Inject;

public class GameBoardGridView extends GridView implements Board.OnBoardStateChangedListener {

    private int mBrickSize;

    @Inject GameManager mGameManager;
    @Inject Board mBoard;
    @Inject PathDrawHandler mDrawHandler;
    @Inject BoardDrawingOrderHelper mDrawingOrderHelper;

    private BoardAdapter mBoardAdapter;

    public GameBoardGridView(Context context) {
        super(context);
        init(context);
    }

    public GameBoardGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameBoardGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        PathRecallApp.getObjectGraph().inject(this);

        calculateBrickSize();
        mBoardAdapter = new BoardAdapter(context);
        setAdapter(mBoardAdapter);
        setClipToPadding(false);
        setChildrenDrawingOrderEnabled(true);

        initBoard();
    }

    private void calculateBrickSize() {
        int screenWidth = UiUtils.getScreenSize(getContext()).x;
        mBrickSize = screenWidth/Board.BOARD_SIZE;
    }

    public void initBoard(){
        mBoard.setBrickSelectionListener(this);
        //mDrawHandler = new PathDrawHandler(mBoard);
        mDrawHandler.setSimulatedEventsListener(new PathDrawHandler.PathDrawSimulatedEventListener() {
            @Override
            public void onSimulatedEvent(MotionEvent event) {
                handleTouchEvent(event, true);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        Log.d("Touch", "BOARD event, event: " + ev);
        return handleTouchEvent(ev, false);
    }

    private boolean handleTouchEvent(MotionEvent ev, boolean simulated) {
        int childCount = getChildCount();
        boolean drawingEnabled = mGameManager.isDrawingEnabled();

        for(int i = 0; i< childCount; i++){
            View view = getChildAt(i);
            if(ev.getX() > view.getLeft() && ev.getX() < view.getRight()
                    && ev.getY() > view.getTop() && ev.getY() < view.getBottom()) {
                if (view instanceof BrickView) {
                    mDrawHandler.handleTouchOnBrick(i % Board.BOARD_SIZE, i / Board.BOARD_SIZE, ev, simulated, drawingEnabled);
                    return true;
                }
            }
        }
        mDrawHandler.handleTouchOnBrick(-1, -1, ev, simulated, drawingEnabled);
        return true;
    }

    @Override
    public void onBrickSelectionChanged(int x, int y, final boolean selected) {
        final int viewIndex = y*Board.BOARD_SIZE + x;

        post(new Runnable() {
            @Override
            public void run() {
                if(selected) {
                    mDrawingOrderHelper.select(viewIndex);
                }
                else {
                    mDrawingOrderHelper.deselect(viewIndex);
                }
                invalidate();
            }
        });
    }

    @Override
    public void onBrickSelectionShadeChanged(final int x, final int y, final float alpha) {
        final int viewIndex = y*Board.BOARD_SIZE + x;
        post(new Runnable() {
            @Override
            public void run() {
                ((BrickView)(getChildAt(viewIndex))).setSelectionShade(mBoard.getBrick(x, y).getSelectionShade());
            }
        });

    }

    @Override
    public void onBrickFadedOut(int x, int y) {
        final int viewIndex = y*Board.BOARD_SIZE + x;
        post(new Runnable() {
            @Override
            public void run() {
                ((BrickView)(getChildAt(viewIndex))).switchToUserFadedOutSelection();
            }
        });
    }

    @Override
    public void onBoardCleared() {
        post(new Runnable() {
            @Override
            public void run() {
                mBoardAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return mDrawingOrderHelper.getDrawingOrderForIndex(i);
    }

    private class BoardAdapter extends BaseAdapter {

        private Context mContext;


        public BoardAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return Board.BOARD_SIZE * Board.BOARD_SIZE;
        }

        @Override
        public Object getItem(int i) {
            return mGameManager.getBoard().getBrick( i % Board.BOARD_SIZE, i / Board.BOARD_SIZE);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            BrickView brick;
            if (view == null) {
                brick = new BrickView(mContext);
                brick.setLayoutParams(new GridView.LayoutParams (mBrickSize, mBrickSize));
            } else {
                brick = (BrickView) view;
            }

            brick.setSelectionShade(((Brick)getItem(i)).getSelectionShade());
            return brick;
        }
    }
}
