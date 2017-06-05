package client;

import game.GameField;
import game.GameSettings;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;
import protocol.GameResponce;
import server.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

/**
 * Created by Moorka on 03.06.2017.
 */
class Player {

    static GameField gameField = new GameField();
    static GameSettings.CellState cellState = GameSettings.CellState.EMPTY_CELL;
    static GameSettings.CellState enemyCellState = GameSettings.CellState.EMPTY_CELL;
    private static Channel channel;


    static void setChannel(Channel channel) {
        Player.channel = channel;
    }

    static void ReceiveMessage(byte type, byte x, byte y) throws IOException {

        switch (type){

            case  GameResponce.YOU_CROSS:{
                System.out.println("--Добро пожаловать в игру! Вы играете крестиками. Мы уже ищем вам соперника, пожалуйста, подождите!");
                cellState = GameSettings.CellState.CROSS_CELL;
                enemyCellState = GameSettings.CellState.ZERO_CELL;
                break;
            }
            case GameResponce.YOU_ZERO:{
                System.out.println("--Добро пожаловать в игру! Вы играете ноликами.");
                cellState = GameSettings.CellState.ZERO_CELL;
                enemyCellState = GameSettings.CellState.CROSS_CELL;
                GameFieldDisplay.showField(gameField);

                break;
            }
            case GameResponce.STEP:{

                if ((x != -1) && (y != -1 )) {
                    gameField.setCell(x, y, enemyCellState);
                    System.out.println("--Противник сделал ход");
                }
                GameFieldDisplay.showField(gameField);


                if (cellState.equals(GameSettings.CellState.CROSS_CELL)) {
                    System.out.println("--Ваш ход, крестик...");
                } else if (cellState.equals(GameSettings.CellState.ZERO_CELL)){
                    System.out.println("--Ваш ход, нолик...");
                }

                boolean realStep = false;
                while (!realStep) {


                    byte xy[] = getXY();
                    if (xy.length == 2) {
                        if (gameField.setCell(xy[0], xy[1], cellState)) {
                            GameFieldDisplay.showField(gameField);
                            realStep = true;

                            ChannelBuffer channelBuffer = buffer(2);
                            channelBuffer.writeByte(xy[0]);
                            channelBuffer.writeByte(xy[1]);

                            if (channel.isOpen()){
                                Channels.write(channel, channelBuffer);
                            }

                        } else {
                            System.out.println("--Ход не корректный. Выберите свободную клетку");
                        }
                    }
                }

                break;
            }
            case GameResponce.DISCONNECT:{

                System.out.println("--Descnnect");
               // channel.close();
                break;
            }
            case GameResponce.FAIL: {
//                if ((x != -1) && (y != -1 )) {
//                    if (gameField.setCell(x, y, enemyCellState)) {
//                        System.out.println("--Противник сделал ход");
//                        GameFieldDisplay.showField(gameField);
//
//                    }
//                }

                setEnemyStep(x, y);
                System.out.println("--Вы проиграли.");
                channel.disconnect();
                break;
            }
            case  GameResponce.WIN:{
//                if ((x != -1) && (y != -1 )) {
//                    if (gameField.setCell(x, y, enemyCellState)) {
//                        System.out.println("--Противник сделал ход");
//                        GameFieldDisplay.showField(gameField);
//
//                    }
//                }
                setEnemyStep(x, y);
                System.out.println("--Поздравляю, вы победили!");
                channel.disconnect();
                break;
            }
            case  GameResponce.DAWN :{
//                if ((x != -1) && (y != -1 )) {
//                    if (gameField.setCell(x, y, enemyCellState)) {
//                        System.out.println("--Противник сделал ход");
//                        GameFieldDisplay.showField(gameField);
//
//                    }
//                }
                setEnemyStep(x, y);
                System.out.println("--Победила дружба =)");
                channel.disconnect();
                break;
            }

            default:{}

        }
    }

    private static byte[] getXY() {

        byte[] result = new byte[2];
        result[0] = -1;
        result[1] = -1;

        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();


        if (string.length() == 2) try {
            result[1] = Byte.parseByte(string.substring(0, 1));
            result[0] = Byte.parseByte(string.substring(1, 2));
        } catch (Exception ignored) {

        }
        return result;
    }


    private static void setEnemyStep (byte x, byte y ){

        if ((x != -1) && (y != -1 )) {
            if (gameField.setCell(x, y, enemyCellState)) {
                System.out.println("--Противник сделал ход");
                GameFieldDisplay.showField(gameField);
            }
        }
    }
}



