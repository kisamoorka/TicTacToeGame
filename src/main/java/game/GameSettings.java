package game;

/**
 * Created by Moorka on 01.06.2017.
 */
public final class GameSettings {

    public  enum CellState {
        EMPTY_CELL, CROSS_CELL, ZERO_CELL
    }

    public  enum GameResult {
        GAME_CONTINUE, DAWN, CROSS_WIN, ZERO_WIN
    }

    private static final int FIELD_SIZE = 3;

    private static final String CROSS_VIEW = "x";
    private static final String EMPTY_VIEW = " ";
    private static final String ZERO_VIEW = "o";


    public static String getCrossView() {
        return CROSS_VIEW;
    }

    public static String getEmptyView() {
        return EMPTY_VIEW;
    }

    public static String getZeroView() {
        return ZERO_VIEW;
    }


    public static int getFieldSize() {
        return FIELD_SIZE;
    }

}
