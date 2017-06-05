import org.junit.Test;
import game.GameField;
import game.GameSettings;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Moorka on 01.06.2017.
 */
public class GameFieldTest {

    @Test
    public void testSetCell() {
        GameField gameField = new GameField();

        assertTrue("setCellTest true", gameField.setCell(1, 0, GameSettings.CellState.CROSS_CELL));
        assertFalse("setCellTest false", gameField.setCell(1, 12, GameSettings.CellState.CROSS_CELL));

    }

    @Test
    public void testGameStateZeroWin() {
        GameField gameField = new GameField();
        gameField.setCell(0, 0, GameSettings.CellState.ZERO_CELL);
        gameField.setCell(1, 1, GameSettings.CellState.ZERO_CELL);
        gameField.setCell(2, 2, GameSettings.CellState.ZERO_CELL);

        assertEquals("test ZeroWin ", gameField.check(), GameSettings.GameResult.ZERO_WIN);

    }

    @Test
    public void testGameStateContinue() {
        GameField gameField = new GameField();
        gameField.setCell(0, 0, GameSettings.CellState.ZERO_CELL);
        gameField.setCell(0, 1, GameSettings.CellState.CROSS_CELL);
        gameField.setCell(0, 2, GameSettings.CellState.ZERO_CELL);
        gameField.setCell(1, 0, GameSettings.CellState.ZERO_CELL);

        assertEquals("test GameContinue ", gameField.check(), GameSettings.GameResult.GAME_CONTINUE);

    }


}
