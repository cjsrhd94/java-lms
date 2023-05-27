package nextstep.sessions.domain;

import java.time.LocalDateTime;

public class SessionDate {

	private LocalDateTime startAt;

	private LocalDateTime endAt;

	public SessionDate(String startAt, String endAt) {
		this(LocalDateTime.parse(startAt), LocalDateTime.parse(endAt));
	}

	public SessionDate(LocalDateTime startAt, LocalDateTime endAt) {
		this.startAt = startAt;
		this.endAt = endAt;
	}
}