package nextstep.courses.domain;

import java.time.LocalDateTime;

import nextstep.courses.domain.enums.Status;
import nextstep.users.domain.NsUser;

public class Session extends BaseTimeEntity{
	private Long id;
	private String title;
	private Period period;
	private Image image;
	private Status status;
	private SessionRegistration sessionRegistration;

	public Session(
		LocalDateTime createdAt, LocalDateTime updatedAt,
		Long id, String title, Period period, Image image,
		Status status, SessionRegistration sessionRegistration
	) {
		super(createdAt, updatedAt);
		this.id = id;
		this.title = title;
		this.period = period;
		this.image = image;
		this.status = status;
		this.sessionRegistration = sessionRegistration;
	}

	public void apply(NsUser nsUser, long amount) {
		sessionRegistration.valid(amount);
		canApply();

		sessionRegistration.register(nsUser);
	}

	public void canApply() {
		if ( !status.isApplying() ) {
			throw new IllegalArgumentException("강의 신청 기간이 아닙니다.");
		}
	}
}
