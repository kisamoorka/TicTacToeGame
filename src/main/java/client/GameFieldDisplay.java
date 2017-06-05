package client;

import game.GameField;
import game.GameSettings;

/**
 * Created by Moorka on 01.06.2017.
 */
public class GameFieldDisplay {

    static void showField(GameField field){

        int sizeX = GameSettings.getFieldSize()  + 1;
        int sizeY = GameSettings.getFieldSize() *2 + 1;
        String  [][] fieldView = new String [sizeX][sizeY];


        for (int i = 0; i < sizeX; i ++){
            for (int j = 0; j < sizeY; j ++){
                fieldView[i][j] = GameSettings.getEmptyView();
            }
        }
        for (int i = 0; i < GameSettings.getFieldSize(); i ++){
            fieldView[0][i*2 + 2] = String.valueOf(i);
            fieldView[i  + 1][0] = String.valueOf(i);
        }

        for (int i = 0 ; i < GameSettings.getFieldSize(); i ++){
            for (int j = 0; j < GameSettings.getFieldSize(); j ++){
                switch (field.getField()[i][j]){
                    case EMPTY_CELL:
                        fieldView[i  + 1][j*2  + 2] = GameSettings.getEmptyView();
                        break;
                    case CROSS_CELL:
                        fieldView[i  + 1][j *2 + 2] = GameSettings.getCrossView();
                        break;
                    case ZERO_CELL:
                        fieldView[i  + 1][j *2 + 2] = GameSettings.getZeroView();
                        break;
                }
            }
        }


        for (int i = 0 ; i < sizeX; i ++){
            StringBuffer line = new StringBuffer("");

            for (int j = 0; j < sizeY; j ++){

                line.append(fieldView[i][j]);
            }
            System.out.println(line);
        }

    }


}
