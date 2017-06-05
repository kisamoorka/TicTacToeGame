package server;

import game.GameField;
import game.GameSettings;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import protocol.GameResponce;


import static org.jboss.netty.buffer.ChannelBuffers.buffer;

/**
 * Created by Moorka on 03.06.2017.
 */
public class Session {


    private GameField gameField;

    public Session() {
        System.out.println("Добавлена новая сессия");
        gameField = new GameField();
        setStatus(Status.WAIT_PLAYER_CROSS);

    }
    public enum Status {
        WAIT_PLAYER_CROSS, WAIT_PLAYER_ZERO,  STEP_CROSS, STEP_ZERO, DUMMY
    }

    private Status status;
    private Integer currentStep;

    private Channel PlayerCrossChannel;
    private Channel PlayerZeroChannel;

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }


    public void setPlayerCross(Channel playerCross) {
        System.out.println("Добавлен игрок х = " + playerCross.getId());
        PlayerCrossChannel = playerCross;

        setStatus(Status.WAIT_PLAYER_ZERO);

    }

    public void setPlayerZero(Channel playerZero) {
        System.out.println("Добавлен игрок o = " + playerZero.getId());
        PlayerZeroChannel = playerZero;
        setStatus(Status.STEP_CROSS);

        ChannelBuffer channelBuffer = buffer(3);
        channelBuffer.writeByte(GameResponce.STEP);
        channelBuffer.writeByte(-1);
        channelBuffer.writeByte(-1);

        if (PlayerCrossChannel.isOpen()) {
            PlayerCrossChannel.write(channelBuffer);
            currentStep = PlayerCrossChannel.getId();
        }


    }


    public Integer disconnectPlayerCross() {

        Integer result = -1;
        try {
            if (PlayerCrossChannel.isOpen()) {
                result = PlayerCrossChannel.getId();
            }

            PlayerCrossChannel.disconnect();

        } catch (Exception e) {

        }
        return result;
    }

    public Integer disconnectPlayerZero() {
        Integer result = -1;
        try {
            if (PlayerZeroChannel.isOpen()) {
                result = PlayerZeroChannel.getId();
            }
            PlayerZeroChannel.disconnect();
        } catch (Exception e) {
        }
        return result;
    }


    public boolean isCurrentStep(Integer playerId) {
        return playerId.equals(this.currentStep) ? true : false;
    }

    public void doStep(Integer playerId, byte x, byte y) {

        if (!playerId.equals(currentStep)) {
            return;
        }
        GameSettings.CellState cellState = GameSettings.CellState.EMPTY_CELL;

        if (getStatus().equals(Status.STEP_CROSS)) {
            cellState = GameSettings.CellState.CROSS_CELL;
        } else if (getStatus().equals(Status.STEP_ZERO)) {
            cellState = GameSettings.CellState.ZERO_CELL;
        }

        if (gameField.setCell(x, y, cellState)) {

            GameSettings.GameResult gameResult = gameField.check();


            switch (gameResult) {

                case GAME_CONTINUE: {

                    setStatus(getStatus().equals(Status.STEP_CROSS) ? Status.STEP_ZERO : Status.STEP_CROSS);
                    currentStep = PlayerCrossChannel.getId().equals(currentStep) ? PlayerZeroChannel.getId() : PlayerCrossChannel.getId();

                    ChannelBuffer channelBuffer = buffer(3);
                    channelBuffer.writeByte(GameResponce.STEP);
                    channelBuffer.writeByte(x);
                    channelBuffer.writeByte(y);


                    if (getStatus().equals(Status.STEP_CROSS)) {
                        if (PlayerCrossChannel.isOpen()) {
                            Channels.write(PlayerCrossChannel, channelBuffer);
                        }
                    } else {
                        if (PlayerZeroChannel.isOpen()) {
                            Channels.write(PlayerZeroChannel, channelBuffer);
                        }
                    }

                    break;
                }

                case DAWN: {

                    ChannelBuffer channelBuffer = buffer(3);
                    channelBuffer.writeByte(GameResponce.DAWN);
                    channelBuffer.writeByte(x);
                    channelBuffer.writeByte(y);

                    if (PlayerZeroChannel.isOpen()) {
                        Channels.write(PlayerZeroChannel, channelBuffer);
                    }
                    if (PlayerCrossChannel.isOpen()) {
                        Channels.write(PlayerCrossChannel, channelBuffer);
                    }
                    break;
                }
                case ZERO_WIN: {
                    ChannelBuffer channelBuffer = buffer(3);
                    channelBuffer.writeByte(GameResponce.WIN);
                    channelBuffer.writeByte(x);
                    channelBuffer.writeByte(y);

                    if (PlayerZeroChannel.isOpen()) {
                        Channels.write(PlayerZeroChannel, channelBuffer);
                    }

                    ChannelBuffer channelBuffe2 = buffer(3);
                    channelBuffe2.writeByte(GameResponce.FAIL);
                    channelBuffe2.writeByte(x);
                    channelBuffe2.writeByte(y);

                    if (PlayerCrossChannel.isOpen()) {
                        Channels.write(PlayerCrossChannel, channelBuffe2);
                    }
                    break;
                }
                case CROSS_WIN: {
                    ChannelBuffer channelBuffer = buffer(3);
                    channelBuffer.writeByte(GameResponce.FAIL);
                    channelBuffer.writeByte(x);
                    channelBuffer.writeByte(y);

                    if (PlayerZeroChannel.isOpen()) {
                        Channels.write(PlayerZeroChannel, channelBuffer);
                    }

                    ChannelBuffer channelBuffer2 = buffer(3);
                    channelBuffer2.writeByte(GameResponce.WIN);
                    channelBuffer2.writeByte(x);
                    channelBuffer2.writeByte(y);

                    if (PlayerCrossChannel.isOpen()) {
                        Channels.write(PlayerCrossChannel, channelBuffer2);
                    }
                    break;
                }

            }

        }
    }
}



