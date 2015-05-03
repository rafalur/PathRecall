package com.rafal.pathrecall.modules;

import com.rafal.pathrecall.engine.difficulty.Difficulty;
import com.rafal.pathrecall.engine.difficulty.DifficultyProfiler;
import com.rafal.pathrecall.engine.difficulty.EasyDifficultyProfiler;
import com.rafal.pathrecall.engine.difficulty.HardDifficultyProfiler;
import com.rafal.pathrecall.engine.difficulty.NormarDifficultyProfiler;
import com.rafal.pathrecall.engine.GameManager;
import com.rafal.pathrecall.data.Board;
import com.rafal.pathrecall.data.Brick;
import com.rafal.pathrecall.engine.Player;
import com.rafal.pathrecall.ui.fragments.GameBoardFragment;
import com.rafal.pathrecall.ui.views.GameBoardGridView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {GameBoardFragment.class, GameBoardGridView.class, GameManager.class, Board.class},
        addsTo = AppModule.class,
        library = true
)

public class GameEngineModule {

    private final String mPlayerName;
    private final Difficulty mDifficulty;

    public GameEngineModule(String playerName, Difficulty difficulty){
        mPlayerName = playerName;
        mDifficulty = difficulty;
    }

    @Provides
    @Singleton
    public GameManager provideGameManager(){
        return new GameManager();
    }

    @Provides
    public Brick[][] provideBricks(){
        Brick[][] bricks = new Brick[Board.BOARD_SIZE][Board.BOARD_SIZE];

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                bricks[i][j] = new Brick();
            }
        }

        return bricks;
    }

    @Provides
    public DifficultyProfiler getDifficultyProfiler(){
        switch (mDifficulty) {
            case EASY:
                return  new EasyDifficultyProfiler();
            case HARD:
                return  new HardDifficultyProfiler();
        }
        return new NormarDifficultyProfiler();
    }

    @Provides
    public Player getPlayer(){
        return  new Player(mPlayerName);
    }
}
