package nextstep.sessions.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.sessions.exception.AlreadySignUpException;
import nextstep.sessions.exception.GuestUserSignUpException;
import nextstep.sessions.exception.NotRecruitingException;
import nextstep.sessions.exception.NumberFullException;
import nextstep.sessions.type.StatusType;
import nextstep.users.domain.NsUser;
import nextstep.users.domain.NsUserTest;

public class SessionTest {

	private SessionDate sessionDate;
	private String coveredImageUrl;
	private Session session;

	@BeforeEach
	void setUp() {
		this.sessionDate = new SessionDate("2023-04-03T00:00:00", "2023-06-01T00:00:00");
		this.coveredImageUrl = "http://nextstep/coveredImageUrl.png";
		this.session = new Session(sessionDate, coveredImageUrl, true, new Students(100));
	}

	@DisplayName("강의를 오픈한다.")
	@Test
	void test1() {
		this.session.open();

		Session expected = new Session(sessionDate, coveredImageUrl, true, StatusType.RECRUITING, new Students(100));
		assertThat(this.session).isEqualTo(expected);
	}

	@DisplayName("강의를 종료한다.")
	@Test
	void test2() {
		this.session.close();

		Session expected = new Session(sessionDate, coveredImageUrl, true, StatusType.TERMINATION, new Students(100));
		assertThat(this.session).isEqualTo(expected);
	}

	@DisplayName("수강 신청을 한다.")
	@Test
	void test3() {
		this.session.open();

		Session expected = new Session(sessionDate, coveredImageUrl, true, new Students(100, List.of(NsUserTest.JAVAJIGI)));
		expected.open();

		assertThat(this.session.signUp(NsUserTest.JAVAJIGI)).isEqualTo(expected);
	}

	@DisplayName("수강 신청 불가 - 준비중")
	@Test
	void test4() {
		assertThatThrownBy(() -> this.session.signUp(NsUserTest.JAVAJIGI)).isInstanceOf(NotRecruitingException.class);
	}

	@DisplayName("수강 신청 불가 - 종료")
	@Test
	void test5() {
		this.session.close();

		assertThatThrownBy(() -> this.session.signUp(NsUserTest.JAVAJIGI)).isInstanceOf(NotRecruitingException.class);
	}

	@DisplayName("수강 신청 불가 - 모집인원 초과")
	@Test
	void test6() {
		Session numberFullSession = new Session(sessionDate, coveredImageUrl, true, new Students(1));
		numberFullSession.open();
		numberFullSession.signUp(NsUserTest.JAVAJIGI);

		assertThatThrownBy(() -> numberFullSession.signUp(NsUserTest.SANJIGI)).isInstanceOf(NumberFullException.class);
	}

	@DisplayName("수강 신청 불가 - 게스트 유저")
	@Test
	void test7() {
		this.session.open();

		assertThatThrownBy(() -> this.session.signUp(NsUser.GUEST_USER)).isInstanceOf(GuestUserSignUpException.class);
	}

	@DisplayName("수강 신청 불가 - 해당 유저로 이미 신청 완료")
	@Test
	void test8() {
		this.session.open();

		this.session.signUp(NsUserTest.JAVAJIGI);
		assertThatThrownBy(() -> this.session.signUp(NsUserTest.JAVAJIGI)).isInstanceOf(AlreadySignUpException.class);
	}
}