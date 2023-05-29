package nextstep.courses.service;

import nextstep.courses.domain.Registration;
import nextstep.courses.domain.RegistrationRepository;
import nextstep.courses.domain.Session;
import nextstep.courses.domain.SessionRepository;
import nextstep.qna.NotFoundException;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("registrationService")
public class RegistrationService {

  private SessionRepository sessionRepository;

  private RegistrationRepository registrationRepository;

  @Transactional
  public void registerSession(NsUser nsUser, long sessionId) {
    Session session = sessionRepository.findById(sessionId)
        .orElseThrow(NotFoundException::new);

    Registration registration = Registration.createRegistration(nsUser, session);
    registrationRepository.save(registration);
  }

  @Transactional
  public void cancelRegistration(long registrationId) {
    Registration registration = registrationRepository.findById(registrationId)
        .orElseThrow(NotFoundException::new);

    registration.cancel();
    registrationRepository.save(registration);
  }

}