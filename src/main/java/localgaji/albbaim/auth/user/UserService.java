package localgaji.albbaim.auth.user;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.auth.user.userDTO.RequestAuth;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 추가
    public User makeNewUser(RequestAuth.SignUpRequest signUpRequest) {
        log.debug("유저 저장 시작");

        User newUser = signUpRequest.toEntity();
        userRepository.save(newUser);

        log.debug("유저 저장 완료 {}", newUser.getUserId());

        return newUser;
    }

    // 그룹 가입
    public void joinGroup(User user, Workplace workplace) {
        log.debug("그룹 업데이트 시작");

        user.updateGroup(workplace);
        userRepository.save(user);

        log.debug("그룹 업데이트 완료");
    }

    // 그룹에 가입된 유저 찾기
    public List<User> findUsersByWorkplace(Workplace workplace) {
        return userRepository.findByWorkplace(workplace)
                .orElseThrow(() -> new CustomException(ErrorType.ETC_ERROR));
    }
}
