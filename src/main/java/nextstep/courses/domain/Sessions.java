package nextstep.courses.domain;


import java.util.ArrayList;
import java.util.List;

public class Sessions {
    private List<Session> sessions = new ArrayList<>();

    public boolean addSession(Session session) {
        return sessions.add(session);
    }
}