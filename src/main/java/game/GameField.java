package game;

import client.GameFieldDisplay;

/**
 * Created by Moorka on 01.06.2017.
 */
public class GameField {

    private GameSettings.CellState[][] field;


    public GameField() {
        field = new GameSettings.CellState[GameSettings.getFieldSize()][GameSettings.getFieldSize()];
        initField();
    }

    public GameSettings.CellState[][] getField() {
        return field;
    }

    private void initField() {

        for (int i = 0; i < GameSettings.getFieldSize(); i++) {
            for (int j = 0; j < GameSettings.getFieldSize(); j++) {
                field[i][j] = GameSettings.CellState.EMPTY_CELL;
            }
        }
    }

    public boolean setCell(int x, int y, GameSettings.CellState state) {
        boolean result = false;
        if ((-1 < x) &&( x < GameSettings.getFieldSize())) {
            if ((-1 < y ) && ( y < GameSettings.getFieldSize())) {
                if ((field[x][y] == GameSettings.CellState.EMPTY_CELL) && (state != GameSettings.CellState.EMPTY_CELL)) {
                    field[x][y] = state;
                    result = true;
                }
            } else result = false;
        } else  result = false;

        return result;
    }

    public GameSettings.GameResult check() {
        GameSettings.GameResult result = GameSettings.GameResult.GAME_CONTINUE;

        if (check(GameSettings.CellState.CROSS_CELL)) {
            result = GameSettings.GameResult.CROSS_WIN;
        } else if (check(GameSettings.CellState.ZERO_CELL)) {
            result = GameSettings.GameResult.ZERO_WIN;
        } else {
            boolean fieldFull = true;

            for (int i = 0; i < GameSettings.getFieldSize(); i++) {
                for (int j = 0; j < GameSettings.getFieldSize(); j++) {
                    if (field[i][j] == GameSettings.CellState.EMPTY_CELL) {

                        fieldFull = false;
                        break;
                    }
                }
                if (!fieldFull) {
                    break;
                }
            }

            if (fieldFull) result = GameSettings.GameResult.DAWN;
        }

        return result;
    }

    private boolean check(GameSettings.CellState state) {
        boolean result = true;

        for (int i = 0; i < GameSettings.getFieldSize(); i++) {
            boolean stateLine = true;
            for (int j = 0; j < GameSettings.getFieldSize(); j++) {
                if (field[i][j] != state) {
                    stateLine = false;
                    break;
                }
            }

            if (stateLine) return true;
        }

        for (int i = 0; i < GameSettings.getFieldSize(); i++) {
            boolean stateLine = true;
            for (int j = 0; j < GameSettings.getFieldSize(); j++) {
                if (field[j][i] != state) {
                    stateLine = false;
                    break;
                }
            }

            if (stateLine) return true;
        }

        boolean stateLine = true;
        for (int i = 0; i < GameSettings.getFieldSize(); i++) {
            if (field[i][i] != state) {

                stateLine = false;
                break;
            }

        }
        if (stateLine) return true;

        stateLine = true;
        for (int i = 0; i < GameSettings.getFieldSize(); i++) {
            if (field[i][GameSettings.getFieldSize() - i - 1] != state) {

                stateLine = false;
                break;
            }

        }
        if (stateLine) return true;


        return false;
    }


}
