package nextstep.courses.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import nextstep.courses.DuplicatedException;
import nextstep.courses.RegistrationFulledException;
import nextstep.courses.RegistrationNotOpenedException;
import nextstep.users.domain.NsUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {

  private Session s1;
  private Session s2;
  private NsUser javajigi;
  private NsUser sanjigi;

  @BeforeEach
  public void setUp() {
    s1 = new Session("tdd", LocalDateTime.now(),
        LocalDateTime.now().plusMonths(2), "tdd-img", SessionType.PAID, 1);
    s2 = new Session("atdd", LocalDateTime.now(),
        LocalDateTime.now().plusMonths(1), "atdd-img", SessionType.PAID, 30);
    javajigi = new NsUser(1L, "javajigi", "password", "name",
        "javajigi@slipp.net");
    sanjigi = new NsUser(2L, "sanjigi", "password", "name",
        "sanjigi@slipp.net");
  }

  @DisplayName("강의 수강신청은 강의 상태가 모집중일 때만 가능하다.")
  @Test
  public void createRegistration_throwException_ifSessionStatusNotRECRUITMENT() {
    s1.registerClose();

    assertThatThrownBy(() -> Registration.createRegistration(javajigi, s1)).isInstanceOf(
        RegistrationNotOpenedException.class);
  }

  @DisplayName("강의 수강신청 시 최대인원이 넘어가면 예외를 던진다.")
  @Test
  public void createRegistration_throwException_ifRegistrationFull() {
    s1.registerOpen();
    Registration.createRegistration(javajigi, s1);

    assertThatThrownBy(() -> Registration.createRegistration(sanjigi, s1)).isInstanceOf(
        RegistrationFulledException.class);
  }

  @DisplayName("강의 수강신청 시 사용자는 강의를 중복신청할 수 없다.")
  @Test
  public void createRegistration_throwException_ifUserAlreadyRegisterSession() {
    s2.registerOpen();
    Registration.createRegistration(javajigi, s2);

    assertThatThrownBy(() -> Registration.createRegistration(javajigi, s2)).isInstanceOf(
        DuplicatedException.class);
  }

  @DisplayName("강의 수강신청을 취소한다.")
  @Test
  public void cancel() {
    s1.registerOpen();
    Registration registration = Registration.createRegistration(javajigi, s1);
    registration.cancel();

    assertAll(
        () -> assertThat(registration.isCanceled()).isTrue(),
        () -> assertThat(s1.hasNsUser(javajigi)).isFalse()
    );
  }

  @DisplayName("사용자는 강의 수강신청 취소 후 다시 신청할 수 있다.")
  @Test
  public void createRegistration_AfterCancel() {
    s1.registerOpen();
    Registration registration = Registration.createRegistration(javajigi, s1);
    registration.cancel();

    assertAll(
        () -> assertThat(s1.hasNsUser(javajigi)).isFalse(),
        () -> Registration.createRegistration(javajigi, s1),
        () -> assertThat(s1.hasNsUser(javajigi)).isTrue()
    );
  }
}