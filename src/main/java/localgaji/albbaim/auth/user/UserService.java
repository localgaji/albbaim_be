package localgaji.albbaim.auth.user;

import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 추가
    public User makeNewUser(SignUpRequest signUpRequest) {
        log.debug("유저 저장 시작");

        User newUser = signUpRequest.toEntity();
        userRepository.save(newUser);

        log.debug("유저 저장 완료 {}", newUser.getUserId());

        return newUser;
    }

    // 유저에 매장 추가
    public void addWorkplace(User user, Workplace workplace) {
        log.debug("그룹 업데이트 시작");

        user.updateWorkplace(workplace);
        userRepository.save(user);

        log.debug("그룹 업데이트 완료");
    }
}
