package server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Moorka on 03.06.2017.
 */

class SessionsManager {

    private static  Map sessions = new HashMap<Integer, Session>();

    static Session.Status addPlayer(Channel channel){

        Session.Status result = Session.Status.DUMMY;
        boolean isNewSession = true;

        for (Iterator<Map.Entry<Integer, Session>> entries = sessions.entrySet().iterator(); entries.hasNext(); ) {
            Map.Entry<Integer, Session> entry = entries.next();

            if (entry.getValue().getStatus().equals(Session.Status.WAIT_PLAYER_ZERO)){
                entry.getValue().setPlayerZero(channel);
                isNewSession = false;
                sessions.put(channel.getId() , entry.getValue());
                result = Session.Status.STEP_CROSS;
                break;
            }
        }

        if (isNewSession){
            Session newSession = new Session();
            newSession.setPlayerCross(channel);
            sessions.put(channel.getId(), newSession);
            result = Session.Status.WAIT_PLAYER_ZERO;
        }

        return  result;
    }


    static Session.Status step(Integer playerId, byte x, byte y){
        Session.Status result = Session.Status.DUMMY;

        Session session = (Session) sessions.get(playerId);

        if (session!= null){
            session.doStep(playerId, x, y);
            result = session.getStatus();
        }

        return result;
    }

    static void disconnect(Integer playerId){

        Session session = (Session) sessions.get(playerId);

        if (session!= null){
            Integer idCross = session.disconnectPlayerCross();
            Integer idZero = session.disconnectPlayerZero();
            try {
                sessions.remove(idZero);
                sessions.remove(idCross);
            } catch ( Exception e) {

            }
        }
    }
}
