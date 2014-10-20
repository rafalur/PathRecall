package com.rafal.pathrecall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Brick;
import com.rafal.pathrecall.utils.BoardDrawingOrderHelper;

public class GameBoardGridView extends GridView implements Board.OnBoardStateChangedListener {

    private BoardAdapter mBoardAdapter;
    private Board mBoard;
    private BoardDrawingOrderHelper mDrawingOrderHelper;

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
        mBoardAdapter = new BoardAdapter(context);
        setAdapter(mBoardAdapter);
        setClipToPadding(false);
        setChildrenDrawingOrderEnabled(true);

        mDrawingOrderHelper = new BoardDrawingOrderHelper();
    }

    public void setBoard(Board board){
        mBoard = board;
        mBoard.setBrickSelectionListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        int childCount = getChildCount();

        for(int i = 0; i< childCount; i++){
            View view = getChildAt(i);
            if(ev.getX() > view.getLeft() && ev.getX() < view.getRight()
                    && ev.getY() > view.getTop() && ev.getY() < view.getBottom()) {
                if (view instanceof BrickView) {
                    boolean isDrawingEnabled = GameManager.instance().isDrawingEnabled();
                    mBoard.setBrickSelected(i / Board.BOARD_SIZE, i % Board.BOARD_SIZE, isDrawingEnabled);
                }
            }
        }

        return true;
    }

    @Override
    public void onBrickSelectionChanged(int x, int y, boolean selected) {
        int viewIndex = y*Board.BOARD_SIZE + x;

        if(selected) {
            mDrawingOrderHelper.select(viewIndex);
        }
        else {
            mDrawingOrderHelper.deselect(viewIndex);
        }
    }

    @Override
    public void onBrickSelectionShadeChanged(int x, int y, float alpha) {
        int viewIndex = y*Board.BOARD_SIZE + x;
        ((BrickView)(getChildAt(viewIndex))).setSelectionShade(mBoard.getBrick(x, y).getSelectionShade());
        invalidate();
    }

    @Override
    public void onBoardCleared() {
        mBoardAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return mDrawingOrderHelper.getDrawingOrderForIndex(i);
    }

    private class BoardAdapter extends BaseAdapter {

        private Context mContext;
        private int mScreenWidth;

        public BoardAdapter(Context context){
            mContext = context;

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            mScreenWidth = display.getWidth();
        }

        @Override
        public int getCount() {
            return Board.BOARD_SIZE * Board.BOARD_SIZE;
        }

        @Override
        public Object getItem(int i) {
            return GameManager.instance().getBoard().getBrick(i / Board.BOARD_SIZE, i % Board.BOARD_SIZE);
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
                brick.setLayoutParams(new GridView.LayoutParams (mScreenWidth/Board.BOARD_SIZE, mScreenWidth/Board.BOARD_SIZE));
            } else {
                brick = (BrickView) view;
            }

            brick.setSelectionShade(((Brick)getItem(i)).getSelectionShade());
            return brick;
        }

        public void setBrickSelected(int x, int y, boolean selected) {
            GameManager.instance().getBoard().setBrickSelected(x, y, selected);
            notifyDataSetChanged();
        }
    }
}
